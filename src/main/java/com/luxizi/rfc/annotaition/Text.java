package com.luxizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname Text
 * @Description TODO（SAP）数据库字段类型为text不能为空，所以注解了之后空（null）默认赋值空字符串（""）；
 * @Date 2020/12/17 16:55
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Text {

}
