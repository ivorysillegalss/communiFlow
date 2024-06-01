package org.chenzc.communi.annonation;

import org.springframework.stereotype.Component;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Handler {
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";
}
