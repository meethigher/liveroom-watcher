package top.meethigher.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import top.meethigher.config.Config;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    public static String TENCENT = "tencent";

    public static String NETEASE = "netease";


    public static List<String> getMusic(String source, String musicName) {
        HttpResponse response = HttpRequest.post(String.format(Config.getMusicTemplate, source, musicName))
                .charset("utf-8")
                .send()
                .charset("utf-8");
        String s = response.bodyText();
        JSONArray jsonArray = JSON.parseArray(s);
        String template = "%s-%s-%s";
        List<String> list = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            List<String> artist = jsonObject.getList("artist", String.class);
            String name = jsonObject.getString("name");
            String id = jsonObject.getString("id");
            list.add(String.format(template, id, name, String.join(",", artist)));
        }
        return list;
    }

    public static String downMusic(String source, String id) {
        HttpResponse response = HttpRequest.post(String.format(Config.downMusicTemplate, source, id))
                .charset("utf-8")
                .send()
                .charset("utf-8");
        JSONObject jsonObject = JSON.parseObject(response.bodyText());
        String url = jsonObject.getString("url");
        if (url == null || url.isEmpty()) {
            return "本首为付费音乐！";
        }
        return url;
    }


}
