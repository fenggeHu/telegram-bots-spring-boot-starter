package tlg.bot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给Bot实现类映射Command实现，分别使用类映射和方法名映射。
 * 1，value - 用于Bot类的Method注解，把command命令映射到方法名，默认映射到与方法同名。
 * eg: @Command("/go") 或者 @Command - 映射到 /方法名
 * 2，cmd - 用在Bot类上注解，关联cmd的实现。
 * eg: @Command(cmd = {Bot1CommandExecutor.class})
 * 同时Bot1CommandExecutor.class的Method可以注解command映射到Method name。
 * <p>
 * 3，多种注解方式结合的优先级：
 *  TODO
 *
 * @author max.hu  @date 2024/10/31
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * command命令映射到Method。可用于Bot实现类的Method和关联cmd类的Method
     */
    String value() default "";

    /**
     * 在Bot类上注解关联cmd类，可以关联多个cmd类
     */
    Class<?>[] cmd() default {};
}
