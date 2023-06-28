package com.weimeizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname Partition
 * @Description TODO
 * @Date 2022-04-21 14:57
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Partition {

    String value();
    //int index() default 0;


}
