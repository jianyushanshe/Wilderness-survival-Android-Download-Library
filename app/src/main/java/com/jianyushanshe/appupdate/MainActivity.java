package com.jianyushanshe.appupdate;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianyushanshe.appupdatelib.DownloadListener;
import com.jianyushanshe.appupdatelib.DownloadUtil;

import java.io.File;
import java.math.BigDecimal;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Author:wangjianming
 * Time:2019/5/7
 * Description:通过retrofit下载文件
 */
public class MainActivity extends AppCompatActivity {
    TextView tvProgress;
    Button btDownload;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProgress = findViewById(R.id.tv_progress);
        btDownload = findViewById(R.id.bt_download);
        ivPhoto = findViewById(R.id.iv_photo);
        //请求读写权限
        PermissionGen.needPermission(this, 100, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        });

        //http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg
        final String url = "http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg";
        //文件完整路径
        final String filePath = getFileName(getSdPath() + "/jianyushanshe/", "jianyushanshe.png");
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadUtil.getInstance().downloadFile(url, filePath, new DownloadListener() {
                    @Override
                    public void onStartDownload() {
                        Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int progress, long totalSize, long downloadSize) {
                        tvProgress.setText(progress + "%" + "--已下载：" + byte2Mega(downloadSize) + "/" + byte2Mega(totalSize));
                    }

                    @Override
                    public void onDownloadFaild(String e) {
                        Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFinish(File file) {
                        //下载完成，安装apk
                        //installApk(file);
                        //下载完成展示图片
                        ivPhoto.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void permisssionOk() {

    }

    @PermissionFail(requestCode = 100)
    public void permissionFail() {
    }


    /**
     * 文件的完整路径
     *
     * @param path
     * @param FileName
     * @return
     */
    public String getFileName(String path, String FileName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path.concat(FileName);
    }

    /**
     * 安装apk
     * <p>
     * 版本>24的，需要用FileProvide方式，要在AndroidManifest.xml中配置provider
     * <p>
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * android:authorities="com.jianyushanshe.appupdate.FileProvider"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/file_paths" />
     * </provider>
     *
     * @param file
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".FileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    /**
     * @throws Exception
     * @功能：获取sd卡根目录
     * @param：
     * @return：目录
     */
    public static String getSdPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return "/sdcard/";
        }
    }

    /**
     * @param count byte长度
     * @return 小于1m返回kb，否则返回mb
     * @功能：将byte转化为kb或者mb
     */
    public static String byte2Mega(long count) {
        StringBuffer sbf = new StringBuffer();
        if (count < 1024 * 1024) {// 小于1m
            float total = (float) count / 1024;
            sbf.append(floatTo1p(total) + "KB");
        } else {
            float total = (float) count / 1024 / 1024;
            sbf.append(floatTo1p(total) + "MB");
        }

        return sbf.toString();
    }


    // float保留一位数字
    public static Float floatTo1p(float data) {
        BigDecimal bd = new BigDecimal(data);
        float num = bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return num;
    }
}
