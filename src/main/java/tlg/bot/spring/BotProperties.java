package tlg.bot.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tlg.bot.BotConfig;

import java.util.List;

/**
 * Bot配置项
 * @author max.hu  @date 2024/10/24
 **/

@Data
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotProperties {
    // bot config
    private List<BotConfig> configs;

    public BotConfig byToken(String token) {
        return configs.stream().filter(e -> token.equals(e.getToken())).findFirst().orElse(null);
    }

    public BotConfig byId(String id) {
        return configs.stream().filter(e -> id.equals(e.getId())).findFirst().orElse(null);
    }

}
