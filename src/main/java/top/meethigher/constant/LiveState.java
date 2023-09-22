package top.meethigher.constant;

/**
 * 直播状态
 *
 * @author chenchuancheng
 * @since 2023/09/22 20:45
 */
public enum LiveState {

    CLOSED(0,"已下播"),
    STARTING(1,"直播中"),
    LOOP(2,"轮播中"),
    ;

    public int code;

    public String desc;

    LiveState(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LiveState findByCode(Integer code) {
        for (LiveState liveState : LiveState.values()) {
            if(liveState.code==code) {
                return liveState;
            }
        }
        return CLOSED;
    }
}
