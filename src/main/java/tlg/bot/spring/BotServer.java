package tlg.bot.spring;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tlg.bot.BotReader;
import tlg.bot.BotConfig;

import java.util.List;

/**
 * 集中管理Bot及状态
 *
 * @author max.hu  @date 2024/10/24
 **/
@Slf4j
@Service
public class BotServer implements ApplicationContextAware {
    @Autowired
    private BotProperties botProperties;
    @Setter
    private int tryTimes = 3;
    final TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

    public void register(List<BotConfig> configs) {
        if (null == configs) return;
        configs.parallelStream().forEach(e -> register(e));
    }

    // 重试和捕获异常处理    TODO 状态检测： 怎么检测连接和重试 -- 通过定时任务扫描TelegramBotsLongPollingApplication.isRunning
    public BotSession register(final BotConfig config) {
        var botName = config.getName();
        for (int i = 0; i < tryTimes; i++) {
            try {
                var bot = this.getBotBean(config);
                // 连接Telegram API，连接成功-BotSession
                BotSession session = botsApplication.registerBot(config.getToken(),
                        () -> TelegramUrl.DEFAULT_URL, new DefaultGetUpdatesGenerator(), bot);
                log.info("register Bot {}, Running={}", botName, session.isRunning());
                return session;
            } catch (Exception e) {
                log.error(botName + " Register Bot", e);
                try {
                    botsApplication.unregisterBot(config.getToken());
                } catch (TelegramApiException ex) {
                    log.error("Try unregisterBot: {}", botName, ex);
                }
                this.sleep(5000);
            }
        }
        return null;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("Try Register Bot: Thread Sleep Err", e);
        }
    }

    // 为了使用Spring的相关能力，先把Bot注册成Spring Bean，再注册成TelegramBots
    @Bean
    public void registerBean() {
        var configs = botProperties.getConfigs();
        if (null == configs) return;
        configs.parallelStream().forEach(e -> registerBean(e));
    }

    @SneakyThrows
    public void registerBean(final BotConfig config) {
        Class<?> tClass = Class.forName(config.getBotClassName());
        applicationContext.registerBean(config.getId(), tClass, config);
    }

    @SneakyThrows
    private BotReader getBotBean(final BotConfig config) {
        return (BotReader) applicationContext.getBean(config.getId());
    }

    private AnnotationConfigApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (null == applicationContext) {
            applicationContext = (AnnotationConfigApplicationContext) ctx;
        }
    }
}