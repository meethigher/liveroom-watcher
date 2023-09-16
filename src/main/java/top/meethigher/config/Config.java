package top.meethigher.config;

/**
 * 接口
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:10
 */
public interface Config {
//    String template = "https://api.live.bilibili.com/room/v1/Room/room_init?id=%s";

    String template="https://api.live.bilibili.com/room/v1/Room/get_info?room_id=%s";

    String getMusicTemplate ="https://meethigher.top/music/api.php?types=search&count=5&pages=1&source=%s&name=%s";

    String downMusicTemplate="https://meethigher.top/music/api.php?types=url&source=%s&id=%s";

    Long bot=201589043L;

    String[] initAdmin=new String[] {
            "750772010",
            "257636396"
    };
}