package cn.longtianxiang.app.bilibilidownloadmanager.task;

import cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper.PlayUrl;
import cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper.VideoDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 龍天翔 on 2023/1/27 at 5:14 PM.
 */
public class PathUrlPair {
    private String coverUrl;
    private String coverPathHighest;
    private String coverPathLower;
    private String coverTempPath;
    private String detailJsonPathHighest;
    private String detailJsonPathLower;
    private String detailJsonTempPath;
    private List<DashPathUrlPair> dashPathUrlPairs = new ArrayList<>();

    public static PathUrlPair generate(VideoDetail videoDetail, Map<Integer, PlayUrl> playUrls){
        PathUrlPair result = new PathUrlPair();

        String upUid = videoDetail.getUpUid();
        String upName = videoDetail.getUpName();
        String title = videoDetail.getTitle();
        String bvId = videoDetail.getBvId();
        String coverUrl = videoDetail.getCoverUrl();

        String filePathCoverTemp = PathGetter.getFilePathCoverTemp(upUid, upName, bvId, title);
        String filePathCoverHighest = PathGetter.getFilePathCoverHighest(upUid, upName, bvId, title);
        String filePathCoverLower = PathGetter.getFilePathCoverLower(upUid, upName, bvId, title);
        result.coverUrl = coverUrl;
        result.coverTempPath = filePathCoverTemp;

        String filePathDetailJsonTemp = PathGetter.getFilePathDetailJsonTemp(upUid, upName, bvId, title);
        String filePathDetailJsonHighest = PathGetter.getFilePathDetailJsonHighest(upUid, upName, bvId, title);
        String filePathDetailJsonLower = PathGetter.getFilePathDetailJsonLower(upUid, upName, bvId, title);
        result.detailJsonTempPath = filePathDetailJsonTemp;

        videoDetail.getParts().forEach(part -> {
            int partIndex = part.getIndex();
            String partTitle = part.getTitle();
            PlayUrl playUrl = playUrls.get(partIndex);
            PlayUrl.AudioUrl audioUrl = playUrl.getAudioUrl();
            List<PlayUrl.VideoUrl> videoUrls = playUrl.getVideoUrls();
            String aUrl = audioUrl.getUrl();
            DashPathUrlPair dashPathUrlPair = new DashPathUrlPair();
            dashPathUrlPair.audioUrl = aUrl;
            dashPathUrlPair.dashAudioTempPath = PathGetter.getFilePathDashAudio(upUid, upName, bvId, title, partIndex);
            videoUrls.forEach(videoUrl -> {
                String qualityDesc = PlayUrl.Quality.findDescById(playUrl.getQualities(), videoUrl.getId());
                String codecDesc = videoUrl.getCodecDesc();
                String filePathDashVideo = PathGetter.getFilePathDashVideo(upUid, upName, bvId, title, partIndex, qualityDesc, codecDesc, partTitle);
                String filePathVideo;
                if(playUrl.isHighest()) {
                    filePathVideo = PathGetter.getFilePathVideoHighest(upUid, upName, bvId, title, partIndex, qualityDesc, codecDesc, partTitle);
                    result.coverPathHighest = filePathCoverHighest;
                    result.detailJsonPathHighest = filePathDetailJsonHighest;
                }else {
                    filePathVideo = PathGetter.getFilePathVideoLower(upUid, upName, bvId, title, partIndex, qualityDesc, codecDesc, partTitle);
                    result.coverPathLower = filePathCoverLower;
                    result.detailJsonPathLower = filePathDetailJsonLower;
                }
                String vUrl = videoUrl.getUrl();
                DashPathUrlPair.Video videoPath = new DashPathUrlPair.Video();
                videoPath.videoUrl = vUrl;
                videoPath.dashVideoTempPath = filePathDashVideo;
                videoPath.path = filePathVideo;
                dashPathUrlPair.videoPaths.add(videoPath);
            });
            result.dashPathUrlPairs.add(dashPathUrlPair);
        });
        return result;
    }

    public static class DashPathUrlPair{
        private String audioUrl;
        private String dashAudioTempPath;
        private List<Video> videoPaths = new ArrayList<>();

        public static class Video{
            private String path;
            private String videoUrl;
            private String dashVideoTempPath;

            public String getVideoUrl() {
                return videoUrl;
            }

            public String getDashVideoTempPath() {
                return dashVideoTempPath;
            }

            public String getPath() {
                return path;
            }
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public String getDashAudioTempPath() {
            return dashAudioTempPath;
        }

        public List<Video> getVideoPaths() {
            return videoPaths;
        }
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getCoverPathHighest() {
        return coverPathHighest;
    }

    public String getCoverPathLower() {
        return coverPathLower;
    }

    public String getCoverTempPath() {
        return coverTempPath;
    }

    public String getDetailJsonPathHighest() {
        return detailJsonPathHighest;
    }

    public String getDetailJsonPathLower() {
        return detailJsonPathLower;
    }

    public String getDetailJsonTempPath() {
        return detailJsonTempPath;
    }

    public List<DashPathUrlPair> getDashPathUrlPairs() {
        return dashPathUrlPairs;
    }
}
