package cn.longtianxiang.app.bilibilidownloadmanager.task;

import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Constant;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Dynamic;

import java.io.File;

/**
 * Created by 龍天翔 on 2023/1/27 at 12:30 AM.
 */
public class PathGetter {

    private static String getDirPathTitleNameTemp(String uid, String upName, String bvId, String title){
        return AppProperties.get(AppProperties.Key.POSITION_1) + File.separator +
                Constant.DIR_NAME_TEMP + File.separator +
                Dynamic.getDirNameUpName(uid, upName) + File.separator +
                Dynamic.getDirNameTitleName(bvId, title);
    }

    public static String getFilePathCoverTemp(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameTemp(uid, upName, bvId, title) + File.separator +
                Dynamic.getFileNameCover(bvId);
    }

    public static String getFilePathDetailJsonTemp(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameTemp(uid, upName, bvId, title) + File.separator +
                Constant.FILE_NAME_VIDEO_DETAIL;
    }

    public static String getFilePathDashVideo(String uid, String upName, String bvId, String title, int partIndex, String qualityDesc, String codecDesc, String partTitle){
        return getDirPathTitleNameTemp(uid, upName, bvId, title) + File.separator +
                Dynamic.getFileNameDashVideo(partIndex, qualityDesc, codecDesc, partTitle);
    }

    public static String getFilePathDashAudio(String uid, String upName, String bvId, String title, int partIndex){
        return getDirPathTitleNameTemp(uid, upName, bvId, title) + File.separator +
                Dynamic.getFileNameDashAudio(partIndex);
    }

    private static String getDirPathTitleNameHighest(String uid, String upName, String bvId, String title){
        return AppProperties.get(AppProperties.Key.POSITION_1) + File.separator +
                Constant.DIR_NAME_HIGHEST + File.separator +
                Dynamic.getDirNameUpName(uid, upName) + File.separator +
                Dynamic.getDirNameTitleName(bvId, title);
    }

    public static String getFilePathVideoHighest(String uid, String upName, String bvId, String title, int partIndex, String qualityDesc, String codecDesc, String partTitle){
        return getDirPathTitleNameHighest(uid, upName, bvId, title) + File.separator +
                Dynamic.getVideoFileName(partIndex, qualityDesc, codecDesc, partTitle);
    }

    private static String getDirPathTitleNameLower(String uid, String upName, String bvId, String title){
        return AppProperties.get(AppProperties.Key.POSITION_1) + File.separator +
                Constant.DIR_NAME_LOWER + File.separator +
                Dynamic.getDirNameUpName(uid, upName) + File.separator +
                Dynamic.getDirNameTitleName(bvId, title);
    }

    public static String getFilePathVideoLower(String uid, String upName, String bvId, String title, int partIndex, String qualityDesc, String codecDesc, String partTitle){
        return getDirPathTitleNameLower(uid, upName, bvId, title) + File.separator +
                Dynamic.getVideoFileName(partIndex, qualityDesc, codecDesc, partTitle);
    }

    public static String getFilePathCoverHighest(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameHighest(uid, upName, bvId, title) + File.separator +
                Dynamic.getFileNameCover(bvId);
    }

    public static String getFilePathDetailJsonHighest(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameHighest(uid, upName, bvId, title) + File.separator +
                Constant.FILE_NAME_VIDEO_DETAIL;
    }

    public static String getFilePathCoverLower(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameLower(uid, upName, bvId, title) + File.separator +
                Dynamic.getFileNameCover(bvId);
    }

    public static String getFilePathDetailJsonLower(String uid, String upName, String bvId, String title){
        return getDirPathTitleNameLower(uid, upName, bvId, title) + File.separator +
                Constant.FILE_NAME_VIDEO_DETAIL;
    }

}
