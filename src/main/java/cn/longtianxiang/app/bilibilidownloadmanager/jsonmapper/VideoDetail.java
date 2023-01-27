package cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper;

import cn.longtianxiang.app.bilibilidownloadmanager.value.Constant;
import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Dynamic;
import cn.zhxu.data.Mapper;
import cn.zhxu.okhttps.HttpResult;
import cn.zhxu.okhttps.OkHttps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 龍天翔 on 2023/1/25 at 4:25 PM.
 */
public class VideoDetail {
    private Mapper mapper;
    private String json;

    public static VideoDetail fetch(String bvId) throws Exception {
        String url = Dynamic.Url.getDetailUrl(bvId);
        Mapper m;
        String j;
        try {
            HttpResult.Body body = OkHttps.sync(url)
                    .get()
                    .getBody();
            m = body.toMapper();
            j = body.toString();
            if (m.getInt("code") != 0) {
                body = OkHttps.sync(url)
                        .addHeader("Cookie", (String) AppProperties.get(AppProperties.Key.COOKIE_SESSDATA))
                        .get()
                        .getBody();
                m = body.toMapper();
                j = body.toString();
            }
        }catch (RuntimeException e){
            throw new Exception("获取视频详情出现异常 > bvId: " + bvId, e);
        }
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.mapper = m;
        videoDetail.json = j;
        return videoDetail;
    }

    public int getCode(){
        return mapper.getInt("code");
    }

    public String getMessage(){
        return mapper.getString("message");
    }

    public String getBvId(){
        String result = null;
        try {
            result = mapper.getMapper("data").getMapper("View").getString("bvid");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    public String getAvId(){
        String result = null;
        try {
            result = mapper.getMapper("data").getMapper("View").getString("aid");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    public int getVideosCount(){
        int result = -1;
        try{
            result = mapper.getMapper("data").getMapper("View").getInt("videos");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    public String getCoverUrl(){
        String result = null;
        try{
            result = mapper.getMapper("data").getMapper("View").getString("pic");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    public String getTitle(){
        String result = null;
        try{
            result = mapper.getMapper("data").getMapper("View").getString("title");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Part> getParts(){
        List<Part> result = new ArrayList<>();
        try{
            mapper.getMapper("data").getMapper("View").getArray("pages").forEach((integer, data) -> {
                int index = data.toMapper().getInt("page");
                long cid = data.toMapper().getLong("cid");
                String title = data.toMapper().getString("part");
                result.add(new Part(index, cid, title));
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        result.sort(Comparator.comparingInt(o -> o.index));
        return result;
    }

    public static class Part{
        private int index;
        private long cid;
        private String title;

        public Part(int index, long cid, String title) {
            this.index = index;
            this.cid = cid;
            this.title = title;
        }

        public long getCid() {
            return cid;
        }

        public String getTitle() {
            return title;
        }

        public int getIndex() {
            return index;
        }
    }

    public String getUpName(){
        try {
            return mapper.getMapper("data").getMapper("View").getMapper("owner").getString("name");
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getUpUid(){
        try {
            return mapper.getMapper("data").getMapper("View").getMapper("owner").getString("mid");
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getJson() {
        return json;
    }
}
