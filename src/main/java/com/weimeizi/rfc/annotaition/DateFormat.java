package com.weimeizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname Format
 * @Description TODO (SAP)用到String类型的目标参数上，会自动将其按时间格式转换。
 * @Date 2020/12/3 21:51
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DateFormat {

    String value() default "yyyy-MM-dd HH:mm:ss";

    boolean isNow() default false;

    int plusDays() default 0;

    int plusHours() default 0;

    int plusMinutes() default 0;

    int plusSeconds() default 0;

    int plusMonths() default 0;

    int plusWeeks() default 0;

    int plusYears() default 0;
}
