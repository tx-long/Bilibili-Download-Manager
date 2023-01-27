package cn.longtianxiang.app.bilibilidownloadmanager.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Created by 龍天翔 on 2023/1/24 at 6:15 PM.
 */
public class Utils {

    public static String getJarParentPath() {
        String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = java.net.URLDecoder.decode(path, StandardCharsets.UTF_8); // 转换处理中文及空格
        Objects.requireNonNull(path);
        return new File(path).getParent();
    }

    public static String getPercentageStr(double value, double total, int decimalPlaces){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(decimalPlaces);
        String s = nf.format(value / total * 100);
        return s + " %";
    }

    public static String smartFormatDataSize(long bytes){
        String result = "";
        int length = new DecimalFormat("#").format((double)bytes).length();
        if(length <= 3) {
            result = new DecimalFormat("#.##").format((double)bytes) + " B";
        }else
        if(length <= 6) {
            result = new DecimalFormat("#.##").format((double)bytes / 1024) + " KB";
        }else
        if(length <= 9) {
            result = new DecimalFormat("#.##").format((double)bytes / 1024 / 1024) + " MB";
        }else
        if(length <= 12) {
            result = new DecimalFormat("#.##").format((double)bytes / 1024 / 1024 / 1024) + " GB";
        }
        return result;
    }

    public static double getRate(double value, double total){
        return value / total;
    }

}
