package top.meethigher.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 命令
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:10
 */
public enum Command {

    HELP("帮助", "帮助", false, false),
    ADMIN_LIST("管理员列表", "管理员列表", true, false),
    ADD_ADMIN("添加管理员", "添加管理员 (\\d+)", true, true),
    DEL_ADMIN("删除管理员", "删除管理员 (\\d+)", true, true),
    ROOM_LIST("直播间列表", "直播间列表", true, false),
    ADD_ROOM("添加直播间", "添加直播间 (\\d+)", true, true),
    DEL_ROOM("移除直播间", "移除直播间 (\\d+)", true, true),

    ;

    public String command;

    public boolean hasParam;

    public boolean admin;

    public String regex;


    Command(String command, String regex, boolean admin, boolean hasParam) {
        this.regex = regex;
        this.command = command;
        this.admin = admin;
        this.hasParam = hasParam;
    }


    public static Command matchCommand(String msg) {
        for (Command command : Command.values()) {
            if (Pattern.matches(command.regex, msg)) {
                return command;
            }
        }
        return null;
    }

    public static List<String> getCommandList() {
        List<String> list = new ArrayList<>();
        for (Command command : Command.values()) {
            list.add(command.command);
        }
        return list;
    }
}

