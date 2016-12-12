package com.naruto.reserveclassroom.leancloud;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/*
 * Created by NARUTO on 2016/11/9.
 */

public class MyLeanCloudApp extends Application {

	@Override
	public void onCreate() {

		super.onCreate();

		// 初始化参数依次为this，APPID，Appkey
		AVOSCloud.initialize(this, "eCdxr3Utb1xBc0RtuYFSjQC1-gzGzoHsz", "fv5PkBjuw3n4C4EIBcGz3dHB");
	}
}
