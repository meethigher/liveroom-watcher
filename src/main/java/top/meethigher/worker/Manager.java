package top.meethigher.worker;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.MiraiLogger;
import top.meethigher.config.Config;
import top.meethigher.constant.Command;
import top.meethigher.entity.GroupRoom;
import top.meethigher.repo.AdminRepo;
import top.meethigher.repo.GroupRoomRepo;
import top.meethigher.utils.MusicUtils;
import top.meethigher.utils.TimeUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 管理者
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:07
 */
public class Manager {

    public static AtomicBoolean aBoolean=new AtomicBoolean(false);

    private final Long stamp;

    private final AdminRepo adminRepo;

    private final GroupRoomRepo groupRoomRepo;

    private final MiraiLogger logger;


    public Manager(MiraiLogger logger) {
        this.stamp = System.currentTimeMillis();
        this.adminRepo = AdminRepo.getInstance();
        this.groupRoomRepo = GroupRoomRepo.getInstance();
        this.logger = logger;
        new Watcher(logger);
    }

    public Manager(MiraiLogger logger, Long stamp) {
        this.stamp = stamp;
        this.adminRepo = AdminRepo.getInstance();
        this.groupRoomRepo = GroupRoomRepo.getInstance();
        this.logger = logger;
        new Watcher(logger);
    }

    public void start() {
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            if (event.getBot().getId() != Config.bot) {
                return;
            }
            Member sender = event.getSender();
            long senderId = sender.getId();
            MessageChain message = event.getMessage();
            SingleMessage singleMessage = message.get(1);
            String msg = singleMessage.toString().trim();
            Command command = Command.matchCommand(msg);
            if (command == null) {
                return;
            }
            action(command, event, msg, Long.toString(senderId));

        });
    }


    private void action(Command command, GroupMessageEvent event, String msg, String senderId) {
        boolean needAdmin = command.needAdmin;
        boolean isInitAdmin=false;
        for (String s : Config.initAdmin) {
            if(senderId.equals(s)) {
                isInitAdmin=true;
                break;
            }
        }
        if(!isInitAdmin) {
            if (needAdmin && !adminRepo.isAdmin(senderId)) {
                event.getGroup().sendMessage("管理员方可操作，您没有权限！");
                return;
            }
        }
        switch (command) {
            case ADMIN_LIST:
                Set<String> list = adminRepo.list();
                if (list.isEmpty()) {
                    event.getGroup().sendMessage("尚未添加管理员！");
                } else {
                    String join = String.join(System.lineSeparator(), list);
                    event.getGroup().sendMessage("管理员列表：" + System.lineSeparator() + join);
                }
                break;
            case ADD_ADMIN:
                // 创建匹配器对象
                Matcher matcher = Pattern.compile(command.regex).matcher(msg);
                // 进行匹配和提取
                if (matcher.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String matchedNumber = matcher.group(1);
                    adminRepo.add(matchedNumber);
                    event.getGroup().sendMessage("成功添加管理员" + matchedNumber + "！");
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case DEL_ADMIN:
                // 进行匹配和提取
                Matcher matcher1 = Pattern.compile(command.regex).matcher(msg);
                if (matcher1.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String matchedNumber = matcher1.group(1);
                    adminRepo.remove(matchedNumber);
                    event.getGroup().sendMessage("成功删除管理员" + matchedNumber + "！");
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case ROOM_LIST:
                Set<String> roomSet = groupRoomRepo.getRoomSet(String.valueOf(event.getGroup().getId()));
                if (roomSet.isEmpty()) {
                    event.getGroup().sendMessage("尚未添加直播间！");
                } else {
                    String join = String.join(System.lineSeparator(), roomSet);
                    event.getGroup().sendMessage("直播间列表：" + System.lineSeparator() + join);
                }
                break;
            case ADD_ROOM:
                // 进行匹配和提取
                Matcher matcher2 = Pattern.compile(command.regex).matcher(msg);
                if (matcher2.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String room = matcher2.group(1);
                    long id = event.getGroup().getId();
                    groupRoomRepo.add(new GroupRoom(room, Long.toString(id)));
                    event.getGroup().sendMessage("成功添加直播间" + room + "！");
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case DEL_ROOM:
                // 进行匹配和提取
                Matcher matcher3 = Pattern.compile(command.regex).matcher(msg);
                if (matcher3.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String room = matcher3.group(1);
                    long id = event.getGroup().getId();
                    groupRoomRepo.remove(new GroupRoom(room, Long.toString(id)));
                    event.getGroup().sendMessage("成功删除直播间" + room + "！");
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case SEARCH_MUSIC_NETEASE:
                // 进行匹配和提取
                Matcher matcher4 = Pattern.compile(command.regex).matcher(msg);
                if (matcher4.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String musicName = matcher4.group(1);
                    List<String> music = MusicUtils.getMusic(MusicUtils.NETEASE, musicName);
                    event.getGroup().sendMessage("音乐列表：" + System.lineSeparator() + String.join(System.lineSeparator(), music));
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case SEARCH_MUSIC_TENCENT:
                // 进行匹配和提取
                Matcher matcher5 = Pattern.compile(command.regex).matcher(msg);
                if (matcher5.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String musicName = matcher5.group(1);
                    List<String> music = MusicUtils.getMusic(MusicUtils.TENCENT, musicName);
                    event.getGroup().sendMessage("音乐列表：" + System.lineSeparator() + String.join(System.lineSeparator(), music));
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case DOWN_MUSIC_TENCENT:
                // 进行匹配和提取
                Matcher matcher6 = Pattern.compile(command.regex).matcher(msg);
                if (matcher6.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String musicId = matcher6.group(1);
                    String url = MusicUtils.downMusic(MusicUtils.TENCENT, musicId);
                    event.getGroup().sendMessage(url);
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case DOWN_MUSIC_NETEASE:
                // 进行匹配和提取
                Matcher matcher7 = Pattern.compile(command.regex).matcher(msg);
                if (matcher7.find()) {
                    // 使用group(1)获取捕获组中的数字
                    String musicId = matcher7.group(1);
                    String url = MusicUtils.downMusic(MusicUtils.NETEASE, musicId);
                    event.getGroup().sendMessage(url);
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case LOG:
                aBoolean.set(!aBoolean.get());
                event.getGroup().sendMessage("debug模式已切换至 "+aBoolean.get());
                break;
            case HELP:
            default:
                List<String> commandList = Command.getCommandList();
                event.getGroup().sendMessage("命令列表：" + System.lineSeparator() +
                        String.join(System.lineSeparator(), commandList) +
                        System.lineSeparator() +
                        "运行时长：" + TimeUtils.convertToHumanReadable((System.currentTimeMillis() - stamp) / 1000));

        }
    }
}
