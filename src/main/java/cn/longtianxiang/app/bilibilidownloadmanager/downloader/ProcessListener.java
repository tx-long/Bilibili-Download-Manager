package cn.longtianxiang.app.bilibilidownloadmanager.downloader;

import cn.zhxu.okhttps.Process;

/**
 * Created by 龍天翔 on 2023/1/26 at 4:06 PM.
 */
public interface ProcessListener {
    void onProcess(Object id, Process process);
}
