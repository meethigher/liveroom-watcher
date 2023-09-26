package top.meethigher.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 群组事件
 *
 * @author chenchuancheng
 * @since 2023/09/22 21:00
 */
public class GroupEvent implements Serializable {


    /**
     * 添加事件名称 xx
     * 添加提醒人列表 xx,xx
     * 添加提醒事件 HH:mm:ss
     * 添加提醒内容 xx
     */

    /**
     * 事件名称
     */
    private final String eventName;

    /**
     * 事件时间
     * HH:mm:ss
     */
    private final String eventTime;

    /**
     * 艾特消息
     */
    private final String atMsg;

    /**
     * 被艾特人列表
     */
    private final Set<String> atList = new HashSet<>();


    private GroupEvent(String eventName, String eventTime, String atMsg) {
        this.eventName = eventName;
        this.atMsg = atMsg;
        this.eventTime = eventTime;
    }


    public static GroupEvent getInstance(String eventName, String eventTime, String atMsg) {
        if(eventName.length()>15) {
//            throw new Ille
        }
        return new GroupEvent(eventName, eventTime, atMsg);
    }

    public void addAt(String at) {
        this.atList.add(at);
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getAtMsg() {
        return atMsg;
    }

    public Set<String> getAtList() {
        return atList;
    }
}
