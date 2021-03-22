package messenger;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Olga Petrova
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag("staging")
public @interface Staging {
}
