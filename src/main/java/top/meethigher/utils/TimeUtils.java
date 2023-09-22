package top.meethigher.utils;

/**
 * 时间工具类
 *
 * @author chenchuancheng
 * @since 2023/09/22 20:44
 */
public class TimeUtils {

    /**
     * 转换为人性化描述
     *
     * @param seconds 秒
     * @return {@link String}
     */
    public static String convertToHumanReadable(long seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("秒数不能为负数");
        }
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(String.format("%02d", days)).append("天");
        }
        if (hours > 0) {
            result.append(String.format("%02d", hours)).append("时");
        }
        if (minutes > 0) {
            result.append(String.format("%02d", minutes)).append("分");
        }
        if (remainingSeconds > 0 || result.length() == 0) {
            result.append(String.format("%02d", remainingSeconds)).append("秒");
        }
        return result.toString();
    }
}
