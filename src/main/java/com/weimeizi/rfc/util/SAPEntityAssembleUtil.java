package com.weimeizi.rfc.util;

import com.sap.conn.jco.JCoTable;
import com.weimeizi.rfc.annotaition.DateFormat;
import com.weimeizi.rfc.annotaition.Ignore;
import com.weimeizi.rfc.annotaition.SAPEntity;
import com.weimeizi.rfc.annotaition.Text;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Classname SAPEntityAssembleUtil
 * @Description TODO SAP函数输出结果自动封装工具类
 * @Date 2021/2/6 11:33
 * @Author by 03126
 */
public class SAPEntityAssembleUtil {

    public static <T> List<T> assembleSAPEntity(JCoTable table, Class<T> clazz) {
        try {
            if (Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation.equals(SAPEntity.class))){
                throw new RuntimeException("SAPEntityAssembleUtil需要含有注解为@SAPEntity的类，当前类为" + clazz.getName());
            }
            Field[] fields = clazz.getDeclaredFields();
            List<T> list = new ArrayList<>(300000);
            for (int i = 0; i < table.getNumRows(); i++) {
                T t = clazz.newInstance();
                table.setRow(i);
                for (Field field : fields) {
                    Class<?> fieldType = field.getType();
                    Method setMethod = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), fieldType);
                    //如果是需要忽略的属性
                    if (Stream.of(field.getAnnotations())
                            .anyMatch(a -> a.annotationType().equals(Ignore.class))) {
                        continue;
                    }
                    if (fieldType == String.class) {
                        if (Stream.of(field.getAnnotations())
                                .anyMatch(a -> a.annotationType().equals(DateFormat.class))) {
                            DateFormat annotation = field.getAnnotation(DateFormat.class);
                            SimpleDateFormat format = new SimpleDateFormat(annotation.value());
                            String dateString;
                            if (annotation.isNow()) {
                                dateString = format.format(new Date());
                            } else {
                                Date date = table.getDate(field.getName());
                                dateString = format.format(date);
                            }
                            setMethod.invoke(t, dateString);
                        } else if (Stream.of(field.getAnnotations())
                                .anyMatch(a -> a.annotationType().equals(Text.class))) {
                            String string = table.getString(field.getName());
                            if (string == null){
                                string = "";
                            }
                            setMethod.invoke(t,string);
                        } else {
                            setMethod.invoke(t, table.getString(field.getName()));

                        }
                    } else if (fieldType == BigDecimal.class) {
                        setMethod.invoke(t, table.getBigDecimal(field.getName()));
                    } else if (fieldType == Short.class) {
                        setMethod.invoke(t, table.getShort(field.getName()));
                    } else if (fieldType == Integer.class) {
                        setMethod.invoke(t, table.getInt(field.getName()));
                    } else if (fieldType == Long.class) {
                        setMethod.invoke(t, table.getLong(field.getName()));
                    } else if (fieldType == Float.class) {
                        setMethod.invoke(t, table.getFloat(field.getName()));
                    } else if (fieldType == Double.class) {
                        setMethod.invoke(t, table.getDouble(field.getName()));
                    } else if (fieldType == Date.class) {
                        Date date = table.getDate(field.getName());
                        if (date == null){
                            date = new Date(0);
                        }
                        setMethod.invoke(t, date);
                    } else if (fieldType == Character.class) {
                        setMethod.invoke(t, table.getChar(field.getName()));
                    }
                }
                list.add(t);
            }
            return list;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("something wrongs in entity assemble");
        }
    }
}
