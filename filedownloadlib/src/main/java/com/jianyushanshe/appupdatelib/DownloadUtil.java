package com.jianyushanshe.appupdatelib;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Author:wangjianming
 * Time:2019/5/6 18:03
 * Description:DownloadUtil 文件下载工具
 */
public class DownloadUtil {
    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIMEOUT = 15;
    /**
     * DownloadUtil实例
     */
    private static DownloadUtil downloadUtil;

    /**
     * 私有化构造方法
     */
    private DownloadUtil() {
    }

    /**
     * 对外提供单例
     *
     * @return
     */
    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            synchronized (DownloadUtil.class) {
                if (downloadUtil == null) {
                    downloadUtil = new DownloadUtil();
                }
            }
        }
        return downloadUtil;
    }


    /**
     * 文件下载
     *
     * @param baseUrl          baseUrl  例如：http://www.baidu.com/
     * @param fileUrl          文件url，baseUrl后面拼接的部分，文件的后缀决定了文件的类型例如.jpg为图片;.apk为安卓安装包   例如：apk/jianyushanshe.apk
     * @param filePath         文件在sd卡存储的完整路径  例如：Android/data/com.jianyushanshe.appupdate/jianyushanshe.apk
     * @param downloadListener 下载监听
     */
    public void downloadFile(final String baseUrl, final String fileUrl, final String filePath, final DownloadListener downloadListener) {
        //线程池管理
        final Executor executor = new MainThreadExecutor();
        //下载拦截器
        final DownloadIntercepter downloadIntercepter = new DownloadIntercepter(downloadListener, executor);
        //创建okhttp客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(downloadIntercepter)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        //downloadService实例
        final DownloadService downloadService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(DownloadService.class);
        //开始下载回调
        if (downloadListener != null) {
            downloadListener.onStartDownload();
        }
        //创建线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //通过retrofit下载文件
                    Response<ResponseBody> response = downloadService.downloadFile(fileUrl).execute();
                    assert response.body() != null;
                    //将字节转换为文件
                    final File file = castByte2File(filePath, response.body().bytes());
                    if (downloadListener != null) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                //下载完成回调
                                downloadListener.onDownloadFinish(file);
                            }
                        });
                    }
                } catch (final IOException e) {
                    if (downloadListener != null) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                //下载失败回调
                                downloadListener.onDownloadFaild(e.getMessage());
                            }
                        });
                    }
                }
            }
        }).start();

    }

    /**
     * 二进制转文件
     * fileName文件的全路径  by：二进制数据
     *
     * @param fileName
     * @param by
     * @return
     */
    public static File castByte2File(String fileName, byte[] by) {
        {
            FileOutputStream fileout = null;
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                fileout = new FileOutputStream(file);
                fileout.write(by, 0, by.length);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                //抛出下载异常
                throw new DownloadException(e.getMessage(), e);
            } finally {
                try {
                    if (fileout != null)
                        fileout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file;
        }
    }

    /**
     * 自定义下载异常
     */
    private static class DownloadException extends RuntimeException {
        public DownloadException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }

    /**
     * 线程池管理
     */
    private class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }

}
