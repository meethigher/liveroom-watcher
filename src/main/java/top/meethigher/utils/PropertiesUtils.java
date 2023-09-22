package top.meethigher.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Property工具类
 *
 * @author chenchuancheng
 * @since 2023/09/22 12:02
 */
public class PropertiesUtils {


    /**
     * 解析properties
     * 优先加载jar包同级
     * 若不存在，则加载内置
     *
     * @param resource 配置文件
     * @return map
     */
    public static Map<String, String> loadToMap(String resource) throws IOException {
        Properties properties = load(resource);
        Map<String, String> map = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            map.put(key, value);
        }
        return map;
    }


    /**
     * 解析properties
     * 优先加载jar包同级
     * 若不存在，则加载内置
     *
     * @param resource 配置文件
     * @return map
     */
    public static Properties load(String resource) throws IOException {
        Properties properties = new Properties();
        //优先读取jar包同级配置文件，若不存在，则读取系统内置配置文件
        File file = new File(System.getProperty("user.dir").replace("\\", "/") + "/" + resource);
        InputStream is;
        if (file.exists()) {
            is = new FileInputStream(file);
        } else {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        }
        properties.load(is);
        return properties;
    }
}
