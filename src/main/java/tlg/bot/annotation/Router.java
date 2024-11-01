package tlg.bot.annotation;

import java.lang.annotation.*;

/**
 * 路由器 - Method路由到实现
 * eg: @Router(anno = Command.class) 使用Command注解路由
 * @author max.hu  @date 2024/11/01
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Router {
    Class<? extends Annotation> anno();
}
