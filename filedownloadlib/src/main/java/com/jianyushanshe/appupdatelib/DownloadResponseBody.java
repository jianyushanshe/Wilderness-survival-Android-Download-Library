package com.jianyushanshe.appupdatelib;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Author:wangjianming
 * Time:2019/5/6 17:12
 * Description:DownloadResponseBody
 */
public class DownloadResponseBody extends ResponseBody {
    /**
     * 响应实体
     */
    private ResponseBody responseBody;
    /**
     * 下载监听
     */
    private DownloadListener downloadListener;

    private BufferedSource bufferedSource;
    private Executor executor;

    public DownloadResponseBody(ResponseBody responseBody, DownloadListener downloadListener, Executor executor) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
        this.executor = executor;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * Okio库的缓冲读文件
     *
     * @return
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读文件
     *
     * @param source
     * @return
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                if (downloadListener != null) {
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    final int progress = (int) (totalBytesRead * 100 / responseBody.contentLength());
                    if (executor != null) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                //下载进度回调
                                downloadListener.onProgress(progress, responseBody.contentLength(), totalBytesRead);
                            }
                        });
                    } else {
                        downloadListener.onProgress(progress, responseBody.contentLength(), totalBytesRead);
                    }
                }
                return bytesRead;
            }
        };
    }
}
