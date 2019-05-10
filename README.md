
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
	//url：文件下载的完整url
	//filePath:文件的SD卡完整存储路径
	//downloadListener：下载监听
	
	//url="http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg"; 
	//filePath=SD卡路径+"你的文件夹路径"+"文件名称.后缀";  //例如：getSdPath()+"/jianyushanshe/"+"jysh.jpg";

    DownloadUtil.getInstance().downloadFile(url, filePath, new DownloadListener() {
                    @Override
                    public void onStartDownload() {
                      //开始下载
                    }

                    @Override
                    public void onProgress(int progress, long totalSize, long downloadSize) {
                        //progress下载进度
                        //totalSize 文件总大小 byte
                        //downloadSize 文件已下载大小 byte
                        
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

可下载Demo查看具体的使用过程。里面包含下载安装Apk的方法和图片下载展示的方法。
