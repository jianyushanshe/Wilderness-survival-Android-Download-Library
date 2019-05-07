package com.jianyushanshe.appupdatelib;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author:wangjianming
 * Time:2019/5/6 17:08
 * Description:DownloadIntercepter 下载拦截器
 */
public class DownloadIntercepter implements Interceptor {
    /**
     * 下载监听器
     */
    private DownloadListener downloadListener;
    /**
     * 线程池管理器
     */
    private Executor executor;

    /**
     * 拦截器构造方法
     *
     * @param downloadListener
     * @param executor
     */
    public DownloadIntercepter(DownloadListener downloadListener, Executor executor) {
        this.downloadListener = downloadListener;
        this.executor = executor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownloadResponseBody(response.body(), downloadListener, executor)).build();
    }
}
