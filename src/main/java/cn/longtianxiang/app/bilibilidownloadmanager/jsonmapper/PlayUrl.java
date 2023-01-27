package cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper;

import cn.longtianxiang.app.bilibilidownloadmanager.value.Constant;
import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Dynamic;
import cn.zhxu.data.Array;
import cn.zhxu.data.Mapper;
import cn.zhxu.okhttps.HttpResult;
import cn.zhxu.okhttps.OkHttps;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 龍天翔 on 2023/1/25 at 4:26 PM.
 */
public class PlayUrl {
    private static final Map<Integer, String> FNVAL_MAP = new HashMap<>();
    private static final String FNVAL;
    private static final String FOURK = "1";
    private static final int[] additionalQualitiesToDownloadChoiceFirst = {126, 125};   //额外要下载的画质 {杜比视界, HDR 真彩色}

    static {
        FNVAL_MAP.put(16, "DASH 格式");
        FNVAL_MAP.put(64, "是否需求 HDR 视频");
        FNVAL_MAP.put(128, "是否需求 4K 分辨率");
        FNVAL_MAP.put(256, "是否需求杜比音频");
        FNVAL_MAP.put(512, "是否需求杜比视界");
        FNVAL_MAP.put(1024, "是否需求 8K 分辨率");
        FNVAL_MAP.put(2048, "是否需求 AV1 编码");
        int orResult = -1;
        for (Integer key : FNVAL_MAP.keySet()) {
            if(orResult == -1) {
                orResult = key;
            }else {
                orResult = orResult | key;
            }
        }
        FNVAL = String.valueOf(orResult);
    }

    private Mapper mapper;
    private String json;

    public static PlayUrl fetch(String bvId, long cid) throws Exception {
        String url = Dynamic.Url.getPlayUrlUrl(bvId, String.valueOf(cid), FNVAL, FOURK);
        Mapper m;
        String j;
        try {
            HttpResult.Body body = OkHttps.sync(url)
                    .addHeader("Cookie", (String) AppProperties.get(AppProperties.Key.COOKIE_SESSDATA))
                    .get()
                    .getBody();
            m = body.toMapper();
            j = body.toString();
        } catch (RuntimeException e) {
            throw new Exception("获取视频播放地址出现异常 > bvId: " + bvId + ", cid: " + cid, e);
        }
        PlayUrl playUrl = new PlayUrl();
        playUrl.mapper = m;
        playUrl.json = j;
        return playUrl;
    }

    public int getCode(){
        return mapper.getInt("code");
    }

    public String getMessage(){
        return mapper.getString("message");
    }

    public List<Quality> getQualities(){
        List<Quality> result = new ArrayList<>();
        try {
            mapper.getMapper("data").getArray("support_formats").forEach((integer, data) -> {
                int code = data.toMapper().getInt("quality");
                String desc = data.toMapper().getString("new_description");
                result.add(new Quality(code, desc));
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        result.sort((o1, o2) -> -(o1.id - o2.id));
        return result;
    }

    public static class Quality{
        private int id;
        private String desc;

        public Quality(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        public static String findDescById(List<Quality> qualities, int id){
            for (Quality quality : qualities) {
                if(quality.id == id){
                    return quality.desc;
                }
            }
            return null;
        }
    }

    public List<VideoUrl> getVideoUrls() {
        List<VideoUrl> videoUrls = new ArrayList<>();
        try {
            mapper.getMapper("data").getMapper("dash").getArray("video").forEach((integer, data) -> {
                Mapper m = data.toMapper();
                int code = m.getInt("id");
                String url = m.getString("baseUrl");
                int codecId = m.getInt("codecid");
                videoUrls.add(new VideoUrl(code, url, codecId));
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        videoUrls.sort((o1, o2) -> -(o1.id - o2.id));
        int highestId = videoUrls.get(0).id;
        List<VideoUrl> result = new ArrayList<>();
        videoUrls.forEach(videoUrl -> {
            if(videoUrl.id == highestId){
                result.add(videoUrl);
            }
        });
        AtomicBoolean isAddedOne = new AtomicBoolean(false);
        for (int qualityId : additionalQualitiesToDownloadChoiceFirst) {
            if(isAddedOne.get()){
                break;
            }
            videoUrls.forEach(videoUrl -> {
                if (highestId != qualityId && videoUrl.id == qualityId) {
                    result.add(videoUrl);
                    isAddedOne.set(true);
                }
            });
        }
        return result;
    }

    public static class VideoUrl{
        private static final Map<Integer, String> codecDescs = new HashMap<>();
        static {
            codecDescs.put(7, "AVC");
            codecDescs.put(12, "HEVC");
            codecDescs.put(13, "AV1");
        }
        private int id;
        private String url;
        private int codecId;

        public VideoUrl(int id, String url, int codecId) {
            this.id = id;
            this.url = url;
            this.codecId = codecId;
        }

        public int getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public int getCodecId() {
            return codecId;
        }

        public String getCodecDesc(){
            String codecDesc = codecDescs.get(codecId);
            if(codecDesc == null){
                codecDesc = "codec_" + codecId;
            }
            return codecDesc;
        }
    }

    public Boolean isHighest(){
        List<VideoUrl> videoUrls = getVideoUrls();
        List<Quality> qualities = getQualities();
        if(videoUrls == null || qualities == null){
            return null;
        }
        if(videoUrls.get(0).id == qualities.get(0).id){
            return true;
        }else {
            return false;
        }
    }

    public AudioUrl getAudioUrl() {
        Array array;
        try {
            array = mapper.getMapper("data").getMapper("dash").getMapper("flac").getArray("audio");
            if(array == null){
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            try {
                array = mapper.getMapper("data").getMapper("dash").getMapper("dolby").getArray("audio");
                if(array == null){
                    throw new NullPointerException();
                }
            }catch (NullPointerException e1){
                array = mapper.getMapper("data").getMapper("dash").getArray("audio");
            }
        }
        List<AudioUrl> audioUrls = new ArrayList<>();
        try {
            array.forEach((integer, data) -> {
                Mapper m = data.toMapper();
                int code = m.getInt("id");
                String url = m.getString("baseUrl");
                audioUrls.add(new AudioUrl(code, url));
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        audioUrls.sort((o1, o2) -> -(o1.id - o2.id));
        return audioUrls.get(0);
    }

    public static class AudioUrl{
        private int id;
        private String url;

        public AudioUrl(int id, String url) {
            this.id = id;
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }
    }

    public String getJson() {
        return json;
    }
}
