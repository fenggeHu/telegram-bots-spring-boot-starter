package tlg.bot.entity;

import lombok.Builder;
import lombok.Data;

/**
 * /command text
 *
 * @author max.hu  @date 2024/10/28
 **/
@Data
@Builder
public class Command {
    private Long chatId;
    // 指令 /command
    private String command;
    // 参数 xxx
    private String parameter;
}
