package tlg.bot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author max.hu  @date 2024/10/24
 **/
@Configuration
public class BotServerStarter implements CommandLineRunner {
    @Autowired
    private BotProperties botProperties;

    @Autowired
    private BotServer botServer;

    // Instantiate Telegram Bots API
    @Override
    public void run(String... args) {
        botServer.register(botProperties.getConfigs());
    }
}
