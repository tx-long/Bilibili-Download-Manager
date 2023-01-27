package cn.longtianxiang.app.bilibilidownloadmanager.task;

import cn.longtianxiang.app.bilibilidownloadmanager.downloader.ContentDownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 龍天翔 on 2023/1/27 at 1:15 AM.
 */
public class DownloadersGetter {

    public static List<ContentDownloader> get(PathUrlPair pathUrlPair){
        List<ContentDownloader> result = new ArrayList<>();
        String coverUrl = pathUrlPair.getCoverUrl();
        String coverTempPath = pathUrlPair.getCoverTempPath();
        AtomicInteger index = new AtomicInteger();
        result.add(new ContentDownloader(index.incrementAndGet(), coverUrl, coverTempPath));
        pathUrlPair.getDashPathUrlPairs().forEach(dashPathUrlPair -> {
            dashPathUrlPair.getVideoPaths().forEach(videoPath -> {
                String videoUrl = videoPath.getVideoUrl();
                String dashVideoTempPath = videoPath.getDashVideoTempPath();
                result.add(new ContentDownloader(index.incrementAndGet(), videoUrl, dashVideoTempPath));
            });
            String audioUrl = dashPathUrlPair.getAudioUrl();
            String dashAudioTempPath = dashPathUrlPair.getDashAudioTempPath();
            result.add(new ContentDownloader(index.incrementAndGet(), audioUrl, dashAudioTempPath));
        });
        return result;
    }
}
