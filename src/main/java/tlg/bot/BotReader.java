package tlg.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import tlg.bot.entity.Command;
import utils.JacksonUtil;

/**
 * 读取信息的Bot - 封装公共逻辑
 * https://rubenlagus.github.io/TelegramBotsDocumentation/telegram-bots.html
 *
 * @author max.hu  @date 2024/10/24
 **/
@Slf4j
public abstract class BotReader implements LongPollingSingleThreadUpdateConsumer {
    protected final BotConfig config;

    /**
     * 通过配置初始化相关的信息
     */
    public BotReader(BotConfig config) {
        this.config = config;
    }

    // 处理逐条消息
    @Override
    public void consume(Update update) {
        log.info("consume Update:\n{}", JacksonUtil.toJson(update));
        if (update.hasMessage()) {  // 消息：文字和图片等
            this.handleMessage(update.getMessage());
        } else if (update.hasEditedMessage()) {  // 修改消息：文字和图片等
            this.handleEditedMessage(update.getEditedMessage());
        } else if (update.hasCallbackQuery()) { // 交互Callback
            this.handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasPoll()) { // 投票
            log.warn("Poll: {}", update);
        } else {
            // ?
            log.error("FIXME consume: {}", update);
        }
    }

    // 需要实现
    protected void handleMessage(final Message message) {
        if (message.isCommand()) {
            log.info("command: {}", message.getText());
            this.doCommand(message);
        } else if (message.isUserMessage()) {
            log.info("user message: {}", message.getText());
        } else if (message.isChannelMessage()) {
            log.info("channel message: {}", message.getText());
        } else if (message.isGroupMessage()) {
            log.info("group message: {}", message.getText());
        } else if (message.isTopicMessage()) {
            log.info("topic message: {}", message.getText());
        } else if (message.isReply()) {
            log.info("reply: {}", message.getText());
        } else if (message.isSuperGroupMessage()) {
            log.info("super group message: {}", message.getText());
        } else {
            log.info("unknown: {}", message.getText());
        }
    }

    protected void handleEditedMessage(final Message message) {
        log.info("It's EditedMessage");
        this.handleMessage(message);
    }

    protected void doCommand(final Message message) {

    }

    // 第1个指令：这里只取第1个entity是指令的转成指令和参数
    // Message.isCommand()已经保证了第1个是command
    protected Command firstCommand(Message message) {
        var entity = message.getEntities().get(0);
        var text = message.getText();

        if (entity.getLength() == text.length()) {  // 只有命令没有参数
            return Command.builder().command(text).build();
        } else {    // 命令+后面的当成参数
            return Command.builder()
                    .command(text.substring(entity.getOffset(), entity.getLength()))
                    .parameter(text.substring(entity.getLength() + 1))
                    .build();
        }
    }

    // 需要实现
    protected void handleCallbackQuery(final CallbackQuery callbackQuery) {

    }

}
