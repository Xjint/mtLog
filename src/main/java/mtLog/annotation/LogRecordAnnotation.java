package mtLog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author walter.bai
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(value = LogRecordAnnotations.class)
public @interface LogRecordAnnotation {

  String success();

  String fail() default "";

  String operator();

  String bizNo();

  String category() default "";

  String detail() default "";

  String condition() default "";
}
