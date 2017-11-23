package cz.gemrot.phd.utils.sockets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * We support {@link AutoConfig} of classes implementing {@link IConfigurable} that has {@link Configurable} annotated fields.
 * 
 * Every field annotated with {@link Configurable} will be tried to be configured with values from {@link ConfigMap}.
 * 
 * Supported types: int, long, double, float, boolean, String
 * 
 * @author Jimmy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ ElementType.FIELD })
public @interface Configurable {

}
