package cn.longtianxiang.app.bilibilidownloadmanager.value;

import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.util.Utils;

import java.io.File;

/**
 * Created by 龍天翔 on 2023/1/26 at 11:46 PM.
 */
public class Dynamic {
    public static final String APP_PROPERTIES_FILE_PATH = Utils.getJarParentPath() + File.separator + "app.properties";

    public static class Url{

        private static final String DETAIL = "https://api.bilibili.com/x/web-interface/view/detail?bvid={BVID}";

        private static final String PLAY_URL = "https://api.bilibili.com/x/player/playurl?bvid={BVID}&cid={CID}&fnval={FNVAL}&fourk={FOURK}";

        public static String getDetailUrl(String bvId){
            return DETAIL.replace("{BVID}", bvId);
        }
        
        public static String getPlayUrlUrl(String bvId, String cid, String fnval, String fourk){
            return PLAY_URL.replace("{BVID}", bvId)
                    .replace("{CID}", cid)
                    .replace("{FNVAL}", fnval)
                    .replace("{FOURK}", fourk);
        }
        
    }

    public static String getDirNameUpName(String uid, String upName){
        return  "[" + uid + "]" + upName;
    }

    public static String getDirNameTitleName(String bvId, String title){
        return "[" + bvId + "]" + title;
    }

    public static String getFileNameCover(String bvId){
        return "Cover[" + bvId + "].jpg";
    }

    public static String getFileNameDashVideo(int partIndex, String qualityDesc, String codecDesc, String partTitle){
        return "[" + partIndex + "][" + qualityDesc + "][" + codecDesc + "]" + partTitle + ".m4s";
    }

    public static String getFileNameDashAudio(int partIndex){
        return "[" + partIndex + "][Audio].m4s";
    }

    public static String getVideoFileName(int partIndex, String qualityDesc, String codecDesc, String partTitle){
        return "[" + partIndex + "][" + qualityDesc + "][" + codecDesc + "]" + partTitle + ".mp4";
    }
}
