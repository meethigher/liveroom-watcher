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

    HELP("- 帮助", "帮助", false, false),
    ADMIN_LIST("- 管理员列表", "管理员列表", true, false),
    ADD_ADMIN("- 添加管理员 QQ", "添加管理员 (\\d{1,15})", true, true),
    DEL_ADMIN("- 删除管理员 QQ", "删除管理员 (\\d{1,15})", true, true),
    ROOM_LIST("- 直播间列表", "直播间列表", true, false),
    ADD_ROOM("- 添加直播间 bili直播间号", "添加直播间 (\\d{1,15})", true, true),
    DEL_ROOM("- 删除直播间 bili直播间号", "删除直播间 (\\d{1,15})", true, true),
    SEARCH_MUSIC_TENCENT("- 搜索腾讯音乐 音乐名","搜索腾讯音乐 (\\S{1,15})",false,true),
    SEARCH_MUSIC_NETEASE("- 搜索网易音乐 音乐名","搜索网易音乐 (\\S{1,15})",false,true),
    DOWN_MUSIC_TENCENT("- 下载腾讯音乐 音乐id","下载腾讯音乐 (\\S{1,15})",true,true),
    DOWN_MUSIC_NETEASE("- 下载网易音乐 音乐id","下载网易音乐 (\\S{1,15})",true,true),
    REMINDER_LIST("- 提醒事件列表","提醒事件列表",true,false),
    ADD_REMINDER("- 添加提醒事件 事件名称","添加提醒事件 (\\S{1,15})",true,false),
    DEL_REMINDER("- 删除提醒事件 事件名称","删除提醒事件 (\\S{1,15})",true,false),
    ;

    public String command;

    public boolean hasParam;

    public boolean needAdmin;

    public String regex;


    Command(String command, String regex, boolean needAdmin, boolean hasParam) {
        this.regex = regex;
        this.command = command;
        this.needAdmin = needAdmin;
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

