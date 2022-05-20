package me.lucanius.prac.tools.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command Framework - Command <br>
 *
 * @author minnymin3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();

    String permission() default "";

    String noPerm() default "§cNo permission.";

    String[] aliases() default {};

    String description() default "";

    String usage() default "";

    boolean inGameOnly() default true;

    boolean isAsync() default false;

}
