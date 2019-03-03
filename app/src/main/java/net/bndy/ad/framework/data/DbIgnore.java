package net.bndy.ad.framework.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbIgnore {
    boolean value() default true;
}
