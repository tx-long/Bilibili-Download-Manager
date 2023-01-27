package cn.longtianxiang.app.bilibilidownloadmanager.task;

import cn.longtianxiang.app.bilibilidownloadmanager.downloader.ContentDownloader;
import cn.longtianxiang.app.bilibilidownloadmanager.downloader.ProcessListener;
import cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper.PlayUrl;
import cn.longtianxiang.app.bilibilidownloadmanager.jsonmapper.VideoDetail;
import cn.longtianxiang.app.bilibilidownloadmanager.ui.TaskDialog;
import cn.longtianxiang.app.bilibilidownloadmanager.util.Utils;
import cn.zhxu.okhttps.Process;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 龍天翔 on 2023/1/25 at 3:06 PM.
 */
public class Task {
    private static final int DOWNLOADING_SIZE = 5;
    private VideoDetail videoDetail;
    private Map<Integer, PlayUrl> playUrls = new HashMap<>();

    private TaskDialog taskDialog = new TaskDialog();

    private Task(){}

    public static Task create(String bvId, boolean showDialog) throws Exception {
        Task task = new Task();
        task.videoDetail = VideoDetail.fetch(bvId);
        if(task.videoDetail.getCode() != 0){
            throw new Exception("服务器返回响应码异常 > code: " + task.videoDetail.getCode() + ", message: " + task.videoDetail.getMessage());
        }
        for (VideoDetail.Part part : task.videoDetail.getParts()) {
            PlayUrl playUrl = PlayUrl.fetch(task.videoDetail.getBvId(), part.getCid());
            if(playUrl.getCode() != 0){
                throw new Exception("服务器返回响应码异常 > code: " + playUrl.getCode() + ", message: " + playUrl.getMessage());
            }
            task.playUrls.put(part.getIndex(), playUrl);
        }
        if(showDialog){
            SwingUtilities.invokeLater(() -> {

            });
        }
        return task;
    }

    public void check(){

    }

    public void start(boolean showDialog) throws InterruptedException {
        if(showDialog) {
            taskDialog.display(null);
        }
        taskDialog.showTitle(videoDetail.getTitle());
        PathUrlPair pathUrlPair = PathUrlPair.generate(videoDetail, playUrls);
        List<ContentDownloader> contentDownloaders = DownloadersGetter.get(pathUrlPair);
        AtomicLong totalLength = new AtomicLong();
        contentDownloaders.forEach(contentDownloader -> {
            contentDownloader.request();
            totalLength.addAndGet(contentDownloader.getLength());
        });
        taskDialog.showTotal(totalLength.get());
        CountDownLatch forAllDownloaderDone = new CountDownLatch(contentDownloaders.size());
        final AtomicLong al = new AtomicLong();
        contentDownloaders.forEach(contentDownloader -> {
            contentDownloader.setProcessListener(new ProcessListener() {
                private static final Map<Object, Long> eachDone = new ConcurrentHashMap<>();
                @Override
                public void onProcess(Object id, Process process) {
                    eachDone.put(id, process.getDoneBytes());
                    AtomicLong totalDone = new AtomicLong();
                    eachDone.values().forEach(totalDone::addAndGet);
                    taskDialog.showDone(totalLength.get(), totalDone.get());
                    taskDialog.showProgress(totalLength.get(), totalDone.get());
                    if(process.isDone()){
                        al.addAndGet(process.getDoneBytes());
                    }
                }
            });
            contentDownloader.setFailureListener((id, failure) -> {
                System.err.println(id + " 失败 > ");
                failure.getException().printStackTrace();
            });
            contentDownloader.setStatusListener((id, status) -> {
                if(status.value() == 3){
                    forAllDownloaderDone.countDown();
                }
            });
        });
        contentDownloaders.forEach(contentDownloader -> {
            contentDownloader.start();
        });
        forAllDownloaderDone.await();
        System.err.println("下载完成");
        System.err.println(totalLength.get());
        System.err.println(al.get());
    }

    public VideoDetail getVideoDetail() {
        return videoDetail;
    }

    public Map<Integer, PlayUrl> getPlayUrls() {
        return playUrls;
    }
}
