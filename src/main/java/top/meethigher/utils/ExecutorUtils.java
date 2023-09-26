package top.meethigher.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 执行池
 *
 * @author chenchuancheng
 * @since 2023/09/26 21:05
 */
public class ExecutorUtils {


    private static final Executor executor=new ThreadPoolExecutor(10,100,60L, TimeUnit.SECONDS,new LinkedBlockingDeque<>(10));

    public static Executor executor() {
        return executor;
    }
}
