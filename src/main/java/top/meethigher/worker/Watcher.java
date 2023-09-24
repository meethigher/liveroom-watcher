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
import top.meethigher.light.retry.RetryHolder;
import top.meethigher.model.LiveRoomInfo;
import top.meethigher.repo.GroupRoomRepo;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 监听者
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:07
 */
public class Watcher {

    private final GroupRoomRepo groupRoomRepo;

    private final Map<String, LiveRoomInfo> roomMap;

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
                        LiveRoomInfo liveRoomInfo = getLiveInfo(room);
                        LiveRoomInfo lastLiveRoomInfo = roomMap.get(room);
                        if (lastLiveRoomInfo == null) {
                            roomMap.put(room, liveRoomInfo);
                            return;
                        }
                        if (!lastLiveRoomInfo.getLiveState().equals(liveRoomInfo.getLiveState())) {
                            notifyGroupInRoom(room, liveRoomInfo);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }, 10 * 1000, 60 * 1000);
    }


    public void notifyGroupInRoom(String roomId, LiveRoomInfo liveRoomInfo) {
        roomMap.put(roomId, liveRoomInfo);
        Set<GroupRoom> list = groupRoomRepo.list();
        if (list.isEmpty()) {
            return;
        }
        Set<String> groupSet = new HashSet<>();
        for (GroupRoom groupRoom : list) {
            if (groupRoom.getRoom().equals(roomId)) {
                groupSet.add(groupRoom.getGroup());
            }
        }
        Bot bot = Bot.getInstance(Config.bot);
        for (String group : groupSet) {
            long id = Long.parseLong(group);
            Group botGroup = bot.getGroup(id);
            if (botGroup == null) {
                logger.warning("未找到群组" + id);
                continue;
            }
            botGroup.sendMessage(String.format("您关注的主播【%s】直播标题为【%s】的直播间【https://live.bilibili.com/%s】当前状态【%s】",
                    liveRoomInfo.getAuthor(),
                    liveRoomInfo.getTitle(),
                    roomId,
                    liveRoomInfo.getLiveState().desc));
            //如果是直播中，则发送直播帧图
            if (liveRoomInfo.getLiveState().equals(LiveState.STARTING)) {
                HttpResponse send = HttpRequest.get(liveRoomInfo.getImage()).send();
                byte[] bytes = send.bodyBytes();
                Image image = botGroup.uploadImage(ExternalResource.create(bytes));
                botGroup.sendMessage(image);
            }
        }

    }


    private LiveRoomInfo getLiveInfo(String roomId) throws Exception {
        String api = String.format(Config.template, roomId);
        //获取直播间信息，出错重试
        LiveRoomInfo liveRoomInfo = RetryHolder.getRetryHolder(5, 500, (Predicate<LiveRoomInfo>) Objects::nonNull, e -> logger.error(e.getMessage())).executeWithRetry(() -> {
            HttpResponse response = HttpRequest.get(api).charset(StandardCharsets.UTF_8.name()).send().charset(StandardCharsets.UTF_8.name());
            String s = response.bodyText();
            JSONObject jsonObject = JSON.parseObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            Integer status = data.getInteger("live_status");
            String title = data.getString("title");
            String image = data.getString("keyframe");
            String uid = data.getString("uid");
            return new LiveRoomInfo(LiveState.findByCode(status), title, image, uid);
        });
        String uid = liveRoomInfo.getUid();
        String userApi = String.format(Config.userInfo, uid);
        //获取用户名，出错重试
        String uname = RetryHolder.getRetryHolder(5, 500, (Predicate<String>) Objects::nonNull, e -> logger.error(e.getMessage()))
                .executeWithRetry(() -> {
                    HttpResponse response = HttpRequest.get(userApi).charset(StandardCharsets.UTF_8.name()).send().charset(StandardCharsets.UTF_8.name());
                    return JSON.parseObject(response.bodyText()).getJSONObject("data").getString("name");
                });
        liveRoomInfo.setUname(uname);
        return liveRoomInfo;
    }

}
