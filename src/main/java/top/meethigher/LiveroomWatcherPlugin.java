package top.meethigher;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import top.meethigher.config.Config;
import top.meethigher.worker.Manager;

public final class LiveroomWatcherPlugin extends JavaPlugin {
    public static final LiveroomWatcherPlugin INSTANCE = new LiveroomWatcherPlugin();

    private LiveroomWatcherPlugin() {
        super(new JvmPluginDescriptionBuilder("top.meethigher.liveroom-watcher", "1.0")
                .name("哔哩哔哩直播间监控")
                .author("meethigher")
                .build());
    }

    @Override
    public void onEnable() {
        MiraiLogger logger = getLogger();
        logger.info("Plugin loaded!");
        Config.logger=logger;
        new Manager(logger,System.currentTimeMillis()).start();
    }
}
