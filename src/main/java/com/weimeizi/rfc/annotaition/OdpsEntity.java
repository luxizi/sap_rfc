package com.weimeizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname OdpsEntity
 * @Description TODO
 * @Date 2022-04-12 15:41
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface OdpsEntity {

    String value();
}
