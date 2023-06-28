package com.luxizi.rfc.mapper;

import com.alibaba.fastjson.JSON;
import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.Table;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordWriter;
import com.aliyun.odps.tunnel.TableTunnel;
import com.luxizi.rfc.annotaition.OdpsEntity;
import com.luxizi.rfc.annotaition.Partition;
import com.luxizi.rfc.config.AliYunConfig;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname MaxcomputeManager
 * @Description TODO
 * @Date 2022-04-11 10:15
 * @Author by 03126
 */
@Slf4j
public class MaxComputeMapper {

    private static TableTunnel tunnel;
    private static Odps odps;
    private static MaxComputeMapper mapper;

    private static boolean isDev = true;

    private static String projectName = AliYunConfig.PROJECT_DEV;

    private MaxComputeMapper() {
    }

    public static MaxComputeMapper getInstance(){
        //如果事先无初始化，默认创建开发环境
        if (mapper == null){
            mapper = new MaxComputeMapper();
            isDev = true;
            projectName = AliYunConfig.PROJECT_DEV;
            AliyunAccount account = new AliyunAccount(AliYunConfig.ACCESS_ID, AliYunConfig.ACCESS_KEY);
            odps = new Odps(account);
            odps.setEndpoint(AliYunConfig.ODPS_URL);
            odps.setDefaultProject(projectName);
            tunnel = new TableTunnel(odps);
            tunnel.setEndpoint(AliYunConfig.TUNNEL_URL);
            System.out.println("The connection config of Maxcompute" + JSON.toJSONString(odps) );
        }
        return mapper;
    }

    public static MaxComputeMapper getInstance(boolean dev) {
        if (mapper == null) {
            mapper = new MaxComputeMapper();
            isDev = dev;
            if (dev){
                projectName = AliYunConfig.PROJECT_DEV;
            }else {
                projectName = AliYunConfig.PROJECT_PRD;
            }
            AliyunAccount account = new AliyunAccount(AliYunConfig.ACCESS_ID, AliYunConfig.ACCESS_KEY);
            odps = new Odps(account);
            odps.setEndpoint(AliYunConfig.ODPS_URL);
            odps.setDefaultProject(projectName);
            tunnel = new TableTunnel(odps);
            tunnel.setEndpoint(AliYunConfig.TUNNEL_URL);
            System.out.println("The connection config of Maxcompute" + JSON.toJSONString(odps));
        }
        return mapper;
    }



    @SneakyThrows
    public <T> void overwrite(Collection<T> collection) {
        if (collection.size() == 0) {
            return;
        }
        //1.获取数据源注解的信息
        Iterator<T> iterator = collection.iterator();
        T element = iterator.next();
        Field[] fields = element.getClass().getDeclaredFields();
        //2.根据@Partition字段进行分组,并组装分区字符串
        List<Field> partField = Arrays.stream(fields)
                .filter(field -> {
                    field.setAccessible(true);
                    return field.getAnnotation(Partition.class) != null;
                })
                .collect(Collectors.toList());
        if (partField.size() != 0) {
            Map<String, List<T>> collect = collection.stream()
                    .collect(Collectors.groupingBy(ele -> {
                        Optional<String> gruop = partField.stream()
                                /* .sorted(Comparator.comparing(field -> {
                                     Partition part = field.getAnnotation(Partition.class);
                                     return part.index();
                                 }, (a, b) -> {
                                     if (a > b) {
                                         return 1;
                                     } else if (b > a) {
                                         return -1;
                                     }
                                     return 0;
                                 }))*/
                                .map(field -> {
                                    Partition part = field.getAnnotation(Partition.class);
                                    String key = part.value();
                                    String value = "";
                                    try {
                                        value = (String) field.get(ele);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        return key + "='" + field.get(ele) +"'";
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    throw new RuntimeException("分区字段解析出现异常");
                                })
                                .reduce((a, b) -> {
                                    return a = a + "," + b;
                                });
                        return gruop.get();
                    }));
            //3-1.写入ODPS
            for (Map.Entry<String, List<T>> entry : collect.entrySet()) {
                String tableName = entry.getValue().get(0).getClass().getAnnotation(OdpsEntity.class).value();
                PartitionSpec spec = new PartitionSpec(entry.getKey());
                Table table = odps.tables().get(entry.getValue().get(0).getClass().getAnnotation(OdpsEntity.class).value());
                System.out.println("The table of partition is:"+ projectName +tableName);
                table.createPartition(spec,true);
                TableTunnel.UploadSession session = tunnel.createUploadSession(projectName, tableName, spec, true);
                RecordWriter writer = session.openBufferedWriter();
                for (T t : entry.getValue()) {
                    Record record = session.newRecord();
                    for (Field field : fields) {
                        if (field.getAnnotation(Partition.class) != null) {
                            continue;
                        }
                        field.setAccessible(true);
                        String name = field.getName().toLowerCase();
                        Class<?> type = field.getType();
                        if (type == Integer.class || type == Short.class || type == Long.class) {
                            record.setBigint(name, (Long) field.get(t));
                        } else if (type == String.class) {
                            record.setString(name, (String) field.get(t));
                        } else if (type == Boolean.class) {
                            record.setBoolean(name, (Boolean) field.get(t));
                        } else if (type == Date.class) {
                            record.setDatetime(name, (Date) field.get(t));
                        } else if (type == Float.class || type == Double.class) {
                            record.setDouble(name, (Double) field.get(t));
                        } else if (type == BigDecimal.class) {
                            record.setDecimal(name, (BigDecimal) field.get(t));
                        }
                    }
                    writer.write(record);
                }
                writer.close();
                session.commit();
            }
        } else {
            T data = collection.iterator().next();
            String tableName = data.getClass().getAnnotation(OdpsEntity.class).value();
            TableTunnel.UploadSession session = tunnel.createUploadSession(projectName, tableName);
            RecordWriter writer = session.openBufferedWriter();
            for (T t : collection) {
                Record record = session.newRecord();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName().toLowerCase();
                    Class<?> type = field.getType();
                    if (type == Integer.class || type == Short.class || type == Long.class) {
                        record.setBigint(name, (Long) field.get(t));
                    } else if (type == String.class) {
                        record.setString(name, (String) field.get(t));
                    } else if (type == Boolean.class) {
                        record.setBoolean(name, (Boolean) field.get(t));
                    } else if (type == Date.class) {
                        record.setDatetime(name, (Date) field.get(t));
                    } else if (type == Float.class || type == Double.class) {
                        record.setDouble(name, (Double) field.get(t));
                    } else if (type == BigDecimal.class) {
                        record.setDecimal(name, (BigDecimal) field.get(t));
                    }
                }
                writer.write(record);
            }
            writer.close();
            session.commit();
        }
    }

    @SneakyThrows
    public <T> void insert(Collection<T> collection) {
        if (collection.size() == 0) {
            return;
        }
        //1.获取数据源注解的信息
        Iterator<T> iterator = collection.iterator();
        T element = iterator.next();
        Field[] fields = element.getClass().getDeclaredFields();
        //2.根据@Partition字段进行分组,并组装分区字符串
        List<Field> partField = Arrays.stream(fields)
                .filter(field -> {
                    field.setAccessible(true);
                    return field.getAnnotation(Partition.class) != null;
                })
                .collect(Collectors.toList());
        if (partField.size() != 0) {
            Map<String, List<T>> collect = collection.stream()
                    .collect(Collectors.groupingBy(ele -> {
                        Optional<String> gruop = partField.stream()
                                /* .sorted(Comparator.comparing(field -> {
                                     Partition part = field.getAnnotation(Partition.class);
                                     return part.index();
                                 }, (a, b) -> {
                                     if (a > b) {
                                         return 1;
                                     } else if (b > a) {
                                         return -1;
                                     }
                                     return 0;
                                 }))*/
                                .map(field -> {
                                    Partition part = field.getAnnotation(Partition.class);
                                    String key = part.value();
                                    String value = "";
                                    try {
                                        value = (String) field.get(ele);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        return key + "=" + field.get(ele);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    throw new RuntimeException("分区字段解析出现异常");
                                })
                                .reduce((a, b) -> {
                                    return a = a + "," + b;
                                });
                        return gruop.get();
                    }));
            //3-1.写入ODPS
            for (Map.Entry<String, List<T>> entry : collect.entrySet()) {
                String tableName = entry.getValue().get(0).getClass().getAnnotation(OdpsEntity.class).value();
                PartitionSpec spec = new PartitionSpec(entry.getKey());
                Table table = odps.tables().get(entry.getValue().get(0).getClass().getAnnotation(OdpsEntity.class).value());
                table.createPartition(spec,true);
                TableTunnel.UploadSession session = tunnel.createUploadSession(projectName, tableName, spec);
                RecordWriter writer = session.openBufferedWriter();
                for (T t : entry.getValue()) {
                    Record record = session.newRecord();
                    for (Field field : fields) {
                        if (field.getAnnotation(Partition.class) != null) {
                            continue;
                        }
                        field.setAccessible(true);
                        String name = field.getName().toLowerCase();
                        Class<?> type = field.getType();
                        if (type == Integer.class || type == Short.class || type == Long.class) {
                            record.setBigint(name, (Long) field.get(t));
                        } else if (type == String.class) {
                            record.setString(name, (String) field.get(t));
                        } else if (type == Boolean.class) {
                            record.setBoolean(name, (Boolean) field.get(t));
                        } else if (type == Date.class) {
                            record.setDatetime(name, (Date) field.get(t));
                        } else if (type == Float.class || type == Double.class) {
                            record.setDouble(name, (Double) field.get(t));
                        } else if (type == BigDecimal.class) {
                            record.setDecimal(name, (BigDecimal) field.get(t));
                        }
                    }
                    writer.write(record);
                }
                writer.close();
                session.commit();
            }
        } else {
            T data = collection.iterator().next();
            String tableName = data.getClass().getAnnotation(OdpsEntity.class).value();
            TableTunnel.UploadSession session = tunnel.createUploadSession(projectName, tableName);
            RecordWriter writer = session.openBufferedWriter();
            for (T t : collection) {
                Record record = session.newRecord();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String name = field.getName().toLowerCase();
                    Class<?> type = field.getType();
                    if (type == Integer.class || type == Short.class || type == Long.class) {
                        record.setBigint(name, (Long) field.get(t));
                    } else if (type == String.class) {
                        record.setString(name, (String) field.get(t));
                    } else if (type == Boolean.class) {
                        record.setBoolean(name, (Boolean) field.get(t));
                    } else if (type == Date.class) {
                        record.setDatetime(name, (Date) field.get(t));
                    } else if (type == Float.class || type == Double.class) {
                        record.setDouble(name, (Double) field.get(t));
                    } else if (type == BigDecimal.class) {
                        record.setDecimal(name, (BigDecimal) field.get(t));
                    }
                }
                writer.write(record);
            }
            writer.close();
            session.commit();
        }
    }

    /*@SneakyThrows
    public <T> List<T> query(String sql,Class<T> clazz) {
        OdpsEntity anno = clazz.getAnnotation(OdpsEntity.class);
        if (anno == null){
            throw new RuntimeException("结果类缺少@OdpsEntity注解");
        }
        DriverManager.getConnection(AliYunConfig.ODPS_WMZ_DATA_WAREHOUSE_URL)

    }*/


}
