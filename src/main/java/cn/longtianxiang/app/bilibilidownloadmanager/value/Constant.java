package cn.longtianxiang.app.bilibilidownloadmanager.value;

import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.util.Utils;

import java.io.File;

/**
 * Created by 龍天翔 on 2023/1/21 at 7:52 PM.
 */
public class Constant {
    public static final String APP_NAME = "Bilibili Downloader";

    public static class Url{

        public static final String NAV = "https://api.bilibili.com/nav";

    }

    public static final String FILE_NAME_VIDEO_DETAIL = "detail.json";

    public static final String DIR_NAME_TEMP = "临时";
    public static final String DIR_NAME_HIGHEST = "最高画质";
    public static final String DIR_NAME_LOWER = "低画质";
}
