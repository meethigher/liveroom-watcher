package top.meethigher.entity;

import java.io.Serializable;
import java.util.Objects;

public class GroupRoom implements Serializable {

    private String room;

    private String group;

    public GroupRoom(String room, String group) {
        this.room = room;
        this.group = group;
    }

    public GroupRoom() {
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, group);
    }

    @Override
    public boolean equals(Object key) {
        if (this == key) {
            return true;
        }
        if (key == null || getClass() != key.getClass()) {
            return false;
        }
        GroupRoom that = (GroupRoom) key;

        return Objects.equals(this.room, that.room) &&
                Objects.equals(this.group, that.group);
    }
}
