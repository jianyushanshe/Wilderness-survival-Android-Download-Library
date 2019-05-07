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
     * @param progress 进度
     * @param totalSize 文件总大小 byte
     * @param downloadSize 文件已下载大小 byte
     */
    void onProgress(int progress, long totalSize, long downloadSize);

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
