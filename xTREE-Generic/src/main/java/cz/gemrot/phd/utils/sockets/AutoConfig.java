package cz.gemrot.phd.utils.sockets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If {@link IConfigurable} class is marked with {@link AutoConfig} annotation, its {@link Configurable} annotated fields will be {@link ConfigMap#autoConfig(Object)}ured.
 * @author Jimmy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ ElementType.TYPE })
public @interface AutoConfig {

}
