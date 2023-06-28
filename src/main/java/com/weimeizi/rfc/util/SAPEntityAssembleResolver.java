package com.weimeizi.rfc.util;

import com.sap.conn.jco.JCoTable;
import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.Ignore;
import com.weimeizi.rfc.annotaition.SAPEntity;
import com.weimeizi.rfc.annotaition.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Classname SAPEntityAssembleResolver
 * @Description TODO
 * @Date 2021/2/7 18:10
 * @Author by 03126
 */
public class SAPEntityAssembleResolver<T> {

    private Class<T> clazz;
    private List<FieldDetail> fieldDetails = new ArrayList<>(100);
    private LocalDateTime localDateTime;

    /**
     * @Classname FieldDetail
     * @Description TODO 存储需要解析的属性信息
     * @Date 2021/2/7 18:10
     * @Author by 03126
     */
    private class FieldDetail {
        Field field;
        String fieldName;
        Class fieldClass;
        DateFormat dateFormatAnnotation;
        Text textAnnotation;
        DateTimeFormatter dateTimeFormatter;
    }

    public SAPEntityAssembleResolver(Class<T> clazz) {
        this.localDateTime = LocalDateTime.now();
        this.clazz = clazz;
        if (Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation.equals(SAPEntity.class))) {
            throw new RuntimeException("SAPEntityAssembleUtil需要含有注解为@SAPEntity的类，当前类为" + clazz.getName());
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //不在SAP函数中的使用@Ignore注解忽略
            if (Stream.of(field.getAnnotations())
                    .anyMatch(a -> a.annotationType().equals(Ignore.class))) {
                continue;
            }
            Class<?> fieldType = field.getType();
            FieldDetail fieldDetail = new FieldDetail();
            fieldDetail.field = field;
            fieldDetail.fieldName = field.getName();
            fieldDetail.fieldClass = fieldType;
            if (fieldType == String.class) {
                Stream.of(field.getAnnotations())
                        .filter(a -> a instanceof DateFormat || a instanceof Text)
                        .findAny()
                        .ifPresent(a -> {
                            if (a instanceof DateFormat) {
                                fieldDetail.dateFormatAnnotation = (DateFormat) a;
                                fieldDetail.dateTimeFormatter = DateTimeFormatter.ofPattern(((DateFormat) a).value());
                            } else if (a instanceof Text) {
                                fieldDetail.textAnnotation = (Text) a;
                            }
                        });
            }
            fieldDetails.add(fieldDetail);
        }
    }

    public List<T> resolve(JCoTable table) {
        List<T> list = new ArrayList<>(1000000);
        try {
            int rowNum = table.getNumRows();
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            for (int i = 0; i < rowNum; i++) {
                table.setRow(i);
                T t = constructor
                        .newInstance();
                for (FieldDetail field : fieldDetails) {
                    if (field.fieldClass == String.class) {
                        if (field.dateFormatAnnotation != null) {
                            DateTimeFormatter formatter = field.dateTimeFormatter;
                            String dateString = null;
                            LocalDateTime time = null;
                            if (field.dateFormatAnnotation.isNow()) {
                                time = this.localDateTime;
                            } else {
                                Date date = table.getDate(field.fieldName);
                                ZoneId zoneId = ZoneId.systemDefault();
                                if (date != null) {
                                    time = LocalDateTime.ofInstant(date.toInstant(), zoneId);
                                }
                            }
                            if (time != null) {
                                if (field.dateFormatAnnotation.plusYears() != 0) {
                                    time = time.plusYears(field.dateFormatAnnotation.plusYears());
                                } else if (field.dateFormatAnnotation.plusMonths() != 0) {
                                    time = time.plusMonths(field.dateFormatAnnotation.plusMonths());
                                } else if (field.dateFormatAnnotation.plusWeeks() != 0) {
                                    time = time.plusWeeks(field.dateFormatAnnotation.plusWeeks());
                                } else if (field.dateFormatAnnotation.plusDays() != 0) {
                                    time = time.plusDays(field.dateFormatAnnotation.plusDays());
                                } else if (field.dateFormatAnnotation.plusHours() != 0) {
                                    time = time.plusHours(field.dateFormatAnnotation.plusHours());
                                } else if (field.dateFormatAnnotation.plusMinutes() != 0) {
                                    time = time.plusMinutes(field.dateFormatAnnotation.plusMinutes());
                                } else if (field.dateFormatAnnotation.plusSeconds() != 0) {
                                    time = time.plusSeconds(field.dateFormatAnnotation.plusSeconds());
                                }
                            }
                            dateString = formatter.format(time);
                            field.field.set(t, dateString);
                        } else if (field.textAnnotation != null) {
                            String content = table.getString(field.fieldName);
                            field.field.set(t, content == null ? "" : content);
                        } else {
                            field.field.set(t, table.getString(field.fieldName));
                        }
                    } else if (field.fieldClass == BigDecimal.class) {
                        field.field.set(t, table.getBigDecimal(field.fieldName));
                    } else if (field.fieldClass == Short.class) {
                        field.field.set(t, table.getShort(field.fieldName));
                    } else if (field.fieldClass == Integer.class) {
                        field.field.set(t, table.getInt(field.fieldName));
                    } else if (field.fieldClass == Long.class) {
                        field.field.set(t, table.getLong(field.fieldName));
                    } else if (field.fieldClass == Float.class) {
                        field.field.set(t, table.getFloat(field.fieldName));
                    } else if (field.fieldClass == Double.class) {
                        field.field.set(t, table.getDouble(field.fieldName));
                    } else if (field.fieldClass == Date.class) {
                        Date date = table.getDate(field.fieldName);
                        if (date == null) {
                            date = new Date(0);
                        }
                        field.field.set(t, date);
                    } else if (field.fieldClass == Character.class) {
                        field.field.set(t, table.getChar(field.fieldName));
                    }
                }
                list.add(t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
