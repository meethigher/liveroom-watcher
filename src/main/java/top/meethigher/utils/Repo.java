package top.meethigher.utils;


import java.util.Set;

/**
 * 仓库
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/14 23:23
 */
public interface Repo<T> {

    /**
     * 查询列表
     *
     * @return 所有内容
     */
    Set<T> list();

    /**
     * 添加
     *
     * @param t 内容
     */
    void add(T t);

    /**
     * 移除
     *
     * @param t 数据
     */
    void remove(T t);
}
