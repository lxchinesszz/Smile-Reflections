package reflect.modle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 上午11:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface GateWayMapping {
    String version();
    String value();
}
