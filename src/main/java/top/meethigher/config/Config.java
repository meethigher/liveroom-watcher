package top.meethigher.config;

import jodd.util.PropertiesUtil;
import top.meethigher.utils.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 接口
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/11 22:10
 */
public class Config {
//    String template = "https://api.live.bilibili.com/room/v1/Room/room_init?id=%s";

    public static final String template = "https://api.live.bilibili.com/room/v1/Room/get_info?room_id=%s";

    public static final String getMusicTemplate = "https://meethigher.top/music/api.php?types=search&count=5&pages=1&source=%s&name=%s";

    public static final String downMusicTemplate = "https://meethigher.top/music/api.php?types=url&source=%s&id=%s";

    public static Long bot;

    public static String[] initAdmin;


    private static final String resource = "liveroom-watcher.properties";

    static {
        Properties properties = null;
        try {
            properties = PropertiesUtils.load(resource);
        } catch (IOException e) {
            System.out.println("加载配置文件出错");
            System.exit(0);
        }
        String botString = properties.getProperty("bot");
        try {
            bot = Long.valueOf(botString);
        } catch (Exception e) {
            System.out.println("解析机器人编号出错");
            System.exit(0);
        }
        try {
            String builtInAdministrator = properties.getProperty("built-in-administrator");
            initAdmin = builtInAdministrator.split(",");
        } catch (Exception e) {
            System.out.println("解析内置管理员出错");
            System.exit(0);
        }
    }
}