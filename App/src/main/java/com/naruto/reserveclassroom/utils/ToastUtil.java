package com.naruto.reserveclassroom.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	/**
	 * @param ctx	上下文环境
	 * @param msg	打印文本内容
	 */
	public static void showShort(Context ctx, String msg) {

		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param ctx	上下文环境
	 * @param msg	打印文本内容
	 */
	public static void showLong(Context ctx, String msg) {

		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
}
