package cn.longtianxiang.app.bilibilidownloadmanager.downloader;

import cn.zhxu.okhttps.Download;

/**
 * Created by 龍天翔 on 2023/1/26 at 4:10 PM.
 */
public interface FailureListener {
    void onFailure(Object id, Download.Failure failure);
}
