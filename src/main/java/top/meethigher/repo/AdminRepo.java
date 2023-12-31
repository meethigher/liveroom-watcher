package top.meethigher.repo;

import top.meethigher.light.repo.FileRepo;

/**
 * 管理员仓库
 *
 * @author chenchuancheng
 * @since 2023/09/22 20:43
 */
public class AdminRepo extends FileRepo<String> {

    private AdminRepo() {
        super("adminRepo");
    }

    //在多线程操作singleton变量时，使用volatile保证线程改变的值立即回写主内存，保证其他线程能拿到最新的值
    private volatile static AdminRepo repo;


    public static AdminRepo getInstance() {
        //第一次检查
        if (repo == null) {
            //加锁第二次检查
            synchronized (AdminRepo.class) {
                if (repo == null) {
                    repo = new AdminRepo();
                }
            }
        }
        return repo;
    }

    public boolean isAdmin(String id) {
        return list().contains(id);
    }
}
