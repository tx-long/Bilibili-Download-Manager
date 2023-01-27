package cn.longtianxiang.app.bilibilidownloadmanager.downloader;

import cn.zhxu.okhttps.*;

import java.io.File;

/**
 * Created by 龍天翔 on 2023/1/26 at 3:00 PM.
 */
public class ContentDownloader {
    private static final HTTP HTTP = cn.zhxu.okhttps.HTTP.builder().build();
    private static final String REFERER = "https://www.bilibili.com";
    private static final String CONNECTION = "close";
    private Object id;
    private String filePath;
    private String url;
    private HttpResult httpResult;
    private SHttpTask sHttpTask;
    private Download.Ctrl ctrl;
    private ProcessListener processListener;
    private SuccessListener successListener;
    private FailureListener failureListener;
    private StatusListener statusListener;

    public ContentDownloader(Object id, String url, String filePath) {
        this.id = id;
        this.url = url;
        this.filePath = filePath;
        sHttpTask = HTTP.sync(url)
                .addHeader("referer", REFERER)
                .addHeader("connection", CONNECTION);
    }

    public void request(){
        httpResult = sHttpTask.get();
    }

    public long getLength(){
        if(httpResult == null){
            throw new RuntimeException("还未获取到响应");
        }
        return httpResult.getContentLength();
    }

    public String getFileName(){
        return new File(filePath).getName();
    }

    public void start() {
        if(httpResult == null){
            throw new RuntimeException("还未获取到响应");
        }
        ctrl = httpResult.getBody()
                .setOnProcess(process -> {
                    if(processListener != null){
                        processListener.onProcess(id, process);
                    }
                })
                .toFile(filePath)
                .setOnSuccess(file -> {
                    if(successListener != null){
                        successListener.onSuccess(id);
                    }
                })
                .setOnFailure(failure -> {
                    if(failureListener != null){
                        failureListener.onFailure(id, failure);
                    }
                })
                .setOnComplete(status -> {
                    if(statusListener != null){
                        statusListener.onStatus(id, status);
                    }
                })
                .start();
    }

    public void pause(){
        if(ctrl == null){
            throw new RuntimeException("下载还未开始");
        }
        ctrl.pause();
    }

    public void resume(){
        if(ctrl == null){
            throw new RuntimeException("下载还未开始");
        }
        ctrl.resume();
    }

    public void cancel(){
        if(ctrl == null){
            throw new RuntimeException("下载还未开始");
        }
        ctrl.cancel();
    }

    public void setProcessListener(ProcessListener processListener){
        this.processListener = processListener;
    }

    public void setSuccessListener(SuccessListener successListener){
        this.successListener = successListener;
    }

    public void setFailureListener(FailureListener failureListener){
        this.failureListener = failureListener;
    }

    public void setStatusListener(StatusListener statusListener){
        this.statusListener = statusListener;
    }

}
