package com.weimeizi.rfc.annotaition;

import java.lang.annotation.*;

/**
 * @Classname SAPEntity
 * @Description TODO （SAP）标注该注解的类才可以使用com.weimeizi.backstage.SAPEntityAssembleUtil否则会抛出异常
 * @Date 2021/2/6 13:57
 * @Author by 03126
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SAPEntity {
}
