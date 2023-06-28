package com.luxizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname Ignore
 * @Description TODO (SAP)忽略转换该字段
 * @Date 2020/12/3 21:41
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Ignore {


}
