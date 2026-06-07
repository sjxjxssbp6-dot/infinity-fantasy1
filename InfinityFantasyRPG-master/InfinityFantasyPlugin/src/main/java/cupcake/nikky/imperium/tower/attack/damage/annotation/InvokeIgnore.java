package cupcake.nikky.imperium.tower.attack.damage.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeIgnore {

    boolean ifEvasion() default true;

    boolean ifNotAvailable() default true;

    boolean ifIgnoreKnockBack() default false;

    boolean ifInvulnerability() default true;

}
