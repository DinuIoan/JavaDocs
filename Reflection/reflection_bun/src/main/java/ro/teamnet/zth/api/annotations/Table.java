package ro.teamnet.zth.api.annotations;

import com.sun.deploy.security.ValidationState;

import java.lang.annotation.*;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

/**
 * Created by user on 7/7/2016.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";

}
