package top.meethigher.utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件仓库
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/9/14 23:26
 */
public abstract class FileRepo<T> implements Repo<T> {

    private final String repoName;

    private final Set<T> dataSet;


    public FileRepo(String repoName) {
        this.repoName = repoName;
        this.dataSet = loadFromFile();
    }


    @Override
    public Set<T> list() {
        return this.dataSet;
    }

    @Override
    public void add(T t) {
        if(!dataSet.contains(t)) {
            dataSet.add(t);
            saveToFile();
        }
    }

    @Override
    public void remove(T t) {
        if(dataSet.contains(t)) {
            dataSet.remove(t);
            saveToFile();
        }
    }


    /**
     * 加载
     *
     * @return 值
     */
    private Set<T> loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(repoName))) {
            return (Set<T>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            File file = new File(repoName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    /**
     * 保存
     */
    private void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(repoName))) {
            outputStream.writeObject(this.dataSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
