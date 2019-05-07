package com.jianyushanshe.appupdatelib;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author:wangjianming
 * Time:2019/5/6 16:13
 * Description:DownloadService
 */
public interface DownloadService {

    /**
     * 下载文件
     *
     * @param url 文件下载地址
     * @return
     * @Streaming 添加该注解，防止内存溢出
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
