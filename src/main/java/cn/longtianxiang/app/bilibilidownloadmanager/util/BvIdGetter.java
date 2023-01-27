package cn.longtianxiang.app.bilibilidownloadmanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 龍天翔 on 2023/1/25 at 8:04 PM.
 */
public class BvIdGetter {

    public static String getBvId(String s) {
        //1.从url提取bvid
        Pattern pattern4B = Pattern.compile("(?<=www.bilibili.com/video/)BV1([a-z|A-Z|0-9]*)");
        Matcher matcher4B = pattern4B.matcher(s);
        if (matcher4B.find()) {
            return matcher4B.group();
        } else {
            //2.从url提取avid
            Pattern pattern4A = Pattern.compile("(?<=www.bilibili.com/video/av)[1-9][0-9]*");
            Matcher matcher4A = pattern4A.matcher(s);
            if (matcher4A.find()) {
                return ABBA.a2b(matcher4A.group());
            } else {
                //3.检查是否是BV号
                Pattern patternB = Pattern.compile("^(BV1([a-z|A-Z|0-9]*))$");
                Matcher matcherB = patternB.matcher(s);
                if (matcherB.find()) {
                    return matcherB.group();
                } else {
                    //4.检查是否是AV号
                    try {
                        long avid = Long.parseLong(s);
                        return ABBA.a2b(String.valueOf(avid));
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
    }

}
