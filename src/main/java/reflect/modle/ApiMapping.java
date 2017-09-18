package reflect.modle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 上午10:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface ApiMapping {
    String value();
}
