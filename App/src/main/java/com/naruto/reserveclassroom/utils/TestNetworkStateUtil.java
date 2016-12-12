package com.naruto.reserveclassroom.utils;
/*
 * Created by NARUTO on 2016/11/13.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TestNetworkStateUtil {

	/**
	 * 判断是否有网路连接
	 *
	 * @param mContext
	 *            上下文
	 * @return 是否有网络--true：有网络；false：无网络
	 */
	public static boolean isNetworkConnected(Context mContext) {

		if (mContext != null) {

			ConnectivityManager cm = (ConnectivityManager) mContext
							.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null) {

				return networkInfo.isAvailable();
			}
		}

		return false;
	}
}
