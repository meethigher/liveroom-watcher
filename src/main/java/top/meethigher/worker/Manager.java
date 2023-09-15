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
import top.meethigher.utils.TimeUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 管理者
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:07
 */
public class Manager {

    private final Long stamp;

    private final AdminRepo adminRepo;

    private final GroupRoomRepo groupRoomRepo;

    private final MiraiLogger logger;


    public Manager(MiraiLogger logger) {
        this.stamp = System.currentTimeMillis();
        this.adminRepo = AdminRepo.getInstance();
        this.groupRoomRepo = GroupRoomRepo.getInstance();
        this.logger=logger;
        new Watcher(logger);
    }

    public void start() {
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            if(event.getBot().getId()!= Config.bot) {
                return;
            }
            Member sender = event.getSender();
            long senderId = sender.getId();
            Set<String> adminList = adminRepo.list();
            if (adminList.contains(Long.toString(senderId))) {
                MessageChain message = event.getMessage();
                SingleMessage singleMessage = message.get(1);
                String msg = singleMessage.toString().trim();
                Command command = Command.matchCommand(msg);
                if (command == null) {
                    return;
                }
                action(command, event, msg);
            }

        });
    }


    private void action(Command command, GroupMessageEvent event, String msg) {
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
                    event.getGroup().sendMessage("成功添加管理员！");
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
                    event.getGroup().sendMessage("成功删除管理员！");
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
                    event.getGroup().sendMessage("成功添加直播间！");
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
                    event.getGroup().sendMessage("成功删除直播间！");
                } else {
                    event.getGroup().sendMessage("格式不正确，请输入正确的格式: " + command.regex);
                }
                break;
            case HELP:
            default:
                List<String> commandList = Command.getCommandList();
                event.getGroup().sendMessage(String.join(System.lineSeparator(), commandList) +
                        System.lineSeparator() +
                        "运行时长：" + TimeUtils.convertToHumanReadable((System.currentTimeMillis() - stamp) / 1000));

        }
    }
}
