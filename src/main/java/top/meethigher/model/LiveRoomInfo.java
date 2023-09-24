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

    private String author;

    private String image;

    private String uid;


    private String uname;
    public LiveRoomInfo(LiveState liveState, String title, String image,String uid) {
        this.liveState = liveState;
        this.title = title;
        this.image = image;
        this.uid=uid;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
