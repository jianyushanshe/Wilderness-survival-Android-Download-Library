package com.jianyushanshe.appupdatelib;

import java.io.File;

/**
 * Author:wangjianming
 * Time:2019/5/6 16:00
 * Description:DownloadListener
 */
public interface DownloadListener {
    /**
     * 开始下载
     */
    void onStartDownload();

    /**
     * 下载进度
     *
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 下载成功
     *
     * @param file
     */
    void onDownloadFinish(File file);
    /**
     * 下载失败
     *
     * @param e
     */
    void onDownloadFaild(String e);
}
