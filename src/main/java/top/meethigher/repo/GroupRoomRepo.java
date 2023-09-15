package top.meethigher.repo;

import top.meethigher.entity.GroupRoom;
import top.meethigher.utils.FileRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupRoomRepo extends FileRepo<GroupRoom> {
    private GroupRoomRepo() {
        super("groupRoomRepo");
    }


    //在多线程操作singleton变量时，使用volatile保证线程改变的值立即回写主内存，保证其他线程能拿到最新的值
    private volatile static GroupRoomRepo repo;


    public static GroupRoomRepo getInstance() {
        //第一次检查
        if (repo == null) {
            //加锁第二次检查
            synchronized (GroupRoomRepo.class) {
                if (repo == null) {
                    repo = new GroupRoomRepo();
                }
            }
        }
        return repo;
    }


    public Set<String> getRoomSet() {
        Set<GroupRoom> list = this.list();
        Set<String> set=new HashSet<>();
        for (GroupRoom groupRoom : list) {
            set.add(groupRoom.getRoom());
        }
        return set;
    }

    public Set<String> getRoomSet(String group) {
        Set<GroupRoom> list=this.list();
        Set<String> set=new HashSet<>();
        for (GroupRoom groupRoom : list) {
            if(groupRoom.getGroup().equals(group)) {
                set.add(groupRoom.getRoom());
            }
        }
        return set;
    }
}
