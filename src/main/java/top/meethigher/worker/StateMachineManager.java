package top.meethigher.worker;

import top.meethigher.cache.CacheStore;
import top.meethigher.cache.impl.DefaultCacheStore;
import top.meethigher.light.statemachine.StateMachine;

import java.util.concurrent.TimeUnit;

/**
 * 状态机管理员
 *
 * @author chenchuancheng
 * @since 2023/09/24 22:06
 */
public class StateMachineManager {

    private static final CacheStore<String, StateMachine> cacheStore = new DefaultCacheStore<>();


    public static void add(String uid, StateMachine stateMachine) {
        cacheStore.put(uid, stateMachine, 30L, TimeUnit.SECONDS);
    }

    public static StateMachine getStateMachine(String uid) {
        return cacheStore.get(uid);
    }

}
