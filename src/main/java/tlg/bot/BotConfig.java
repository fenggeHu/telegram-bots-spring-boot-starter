package tlg.bot;

import lombok.Data;

/**
 * @author max.hu  @date 2024/10/24
 **/
@Data
public class BotConfig {
    // telegram username - 全局唯一
    private String id;
    // Bot view name
    private String name;
    private String token;
    // Bot实现类
    private String botClassName;
}
