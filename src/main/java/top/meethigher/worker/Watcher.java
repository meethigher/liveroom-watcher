package top.meethigher.worker;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;
import top.meethigher.config.Config;
import top.meethigher.constant.LiveState;
import top.meethigher.entity.GroupRoom;
import top.meethigher.model.LiveInfo;
import top.meethigher.repo.GroupRoomRepo;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 监听者
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:07
 */
public class Watcher {

    private final GroupRoomRepo groupRoomRepo;

    private final Map<String, LiveInfo> roomMap;

    private final MiraiLogger logger;


    public Watcher(MiraiLogger logger) {
        this.logger = logger;
        groupRoomRepo = GroupRoomRepo.getInstance();
        roomMap = new ConcurrentHashMap<>();
        new Timer("watcher-" + Integer.toHexString(hashCode())).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Set<String> roomSet = groupRoomRepo.getRoomSet();
                for (String room : roomSet) {
                    try {
                        LiveInfo liveInfo = getLiveInfo(room);
                        LiveInfo lastLiveInfo = roomMap.get(room);
                        if (lastLiveInfo == null) {
                            roomMap.put(room, liveInfo);
                            return;
                        }
                        if (!lastLiveInfo.getLiveState().equals(liveInfo.getLiveState())) {
                            notifyGroupInRoom(room, liveInfo);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }, 10 * 1000, 60 * 1000);
    }

    /**
     * 通知
     *
     * @param roomId
     * @param liveInfo
     */
    public void notifyGroupInRoom(String roomId, LiveInfo liveInfo) {
        roomMap.put(roomId, liveInfo);
        Set<GroupRoom> list = groupRoomRepo.list();
        if (list.isEmpty()) {
            return;
        }
        Set<String> groupSet = list.stream().map(GroupRoom::getGroup).collect(Collectors.toSet());
        Bot bot = Bot.getInstance(Config.bot);
        for (String group : groupSet) {
            long id = Long.parseLong(group);
            Group botGroup = bot.getGroup(id);
            if (botGroup == null) {
                logger.warning("未找到群组" + id);
                continue;
            }
            botGroup.sendMessage(String.format("您关注的直播标题为【%s】的直播间【https://live.bilibili.com/%s】当前状态【%s】", liveInfo.getTitle(), roomId, liveInfo.getLiveState().desc));
            //如果是直播中，则发送直播帧图
            if (liveInfo.getLiveState().equals(LiveState.STARTING)) {
                HttpResponse send = HttpRequest.get(liveInfo.getImage()).send();
                byte[] bytes = send.bodyBytes();
                Image image = botGroup.uploadImage(ExternalResource.create(bytes));
                botGroup.sendMessage(image);
            }
        }

    }


    /**
     * @param roomId
     * @return
     */
    private LiveInfo getLiveInfo(String roomId) {
        String api = String.format(Config.template, roomId);
        HttpResponse response = HttpRequest.get(api).charset(StandardCharsets.UTF_8.name()).send().charset(StandardCharsets.UTF_8.name());
        String s = response.bodyText();
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject data = jsonObject.getJSONObject("data");
        Integer status = data.getInteger("live_status");
        String title = data.getString("title");
        String image = data.getString("keyframe");
        return new LiveInfo(LiveState.findByCode(status), title, image);
    }

}
