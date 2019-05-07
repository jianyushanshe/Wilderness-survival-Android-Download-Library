# Wilderness-survival-Android-Download-Library
⚡️Wilderness-survival-Android-NetWorkLibrary(荒野求生之Android文件下载库) 通过retrofit下载文件，方便你轻松获取荒野中的各种资源！

**1.集成步骤**

在项目的gradle中添加引用

```
llprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
在app的gradle中添加引用

```
dependencies {
	     	dependencies {
	        implementation 'com.github.jianyushanshe:Wilderness-survival-Android-Download-Library:1.0'
	}
```

**2.使用方法**

在需要下载文件的地方调用如下方法：

```
	//baseUrl：基类url
	//fileUrl：文件的url
	//filePath:文件的SD卡完整存储路径
	//downloadListener：下载监听
	
	//例如：http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg
	//baseUrl="http://k.zol-img.com.cn/";  //必须以/结尾
	//fileUrl="sjbbs/7692/a7691515_s.jpg";
	//filePath=SD卡路径+"你的文件夹路径"+"文件名称.后缀";  getSdPath()+"/jianyushanshe/"+"jysh.jpg";

    DownloadUtil.getInstance().downloadFile(baseUrl, fileUrl, filePath, new DownloadListener() {
                    @Override
                    public void onStartDownload() {
                      //开始下载
                    }

                    @Override
                    public void onProgress(int progress) {
                        //下载进度
                    }

         	    @Override
                    public void onDownloadFinish(File file) {
                        //下载完成，安装apk
                        //installApk(file);
                        //下载完成展示图片
                        ivPhoto.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    }
                    
                    @Override
                    public void onDownloadFaild(String e) {
                     //下载失败  
                    }
                });
```

