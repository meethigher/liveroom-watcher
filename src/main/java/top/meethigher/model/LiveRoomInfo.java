package top.meethigher.model;

import top.meethigher.constant.LiveState;

/**
 * 直播间状态
 *
 * @author chenchuancheng
 * @since 2023/09/22 20:44
 */
public class LiveRoomInfo {

    private LiveState liveState;

    private String title;

    private String image;

    public LiveRoomInfo(LiveState liveState, String title, String image) {
        this.liveState = liveState;
        this.title = title;
        this.image = image;
    }

    public LiveState getLiveState() {
        return liveState;
    }

    public void setLiveState(LiveState liveState) {
        this.liveState = liveState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
