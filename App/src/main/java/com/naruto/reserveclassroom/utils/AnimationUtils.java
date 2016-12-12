package com.naruto.reserveclassroom.utils;
/*
 * Created by NARUTO on 2016/11/09.
 */

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AnimationUtils {

	public static int runningAnim = 0;

	/**
	 * 逆时针转出动画
	 * 
	 * @param v
	 *            需要旋转的控件
	 * @param delay
	 *            延时时间
	 */
	public static void RotateCounterAnim(View v, long delay) {

			// 创建逆时针转出动画
			RotateAnimation anim = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
			// 设置动画执行时间
			anim.setDuration(500);
			// 设置动画保持执行后的位置
			anim.setFillAfter(true);
			// 设置动画的延时
			anim.setStartOffset(delay);
			// 监听当前动画，如果正在执行，则阻止下一个动画的执行
			anim.setAnimationListener(new MyAnimationListener());

			v.startAnimation(anim);
	}

	/**
	 * 顺时针转入动画
	 *
	 * @param v
	 *            需要旋转的控件
	 * @param delay
	 *            延时时间
	 */
	public static void RotateAnim(View v, long delay) {

			// 创建顺时针转入动画
			RotateAnimation anim = new RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
			// 设置动画执行时间
			anim.setDuration(500);
			// 设置动画保持执行后的位置
			anim.setFillAfter(true);
			// 监听当前动画，如果正在执行，则阻止下一个动画的执行
			anim.setAnimationListener(new MyAnimationListener());
			// 设置动画的延时
			anim.setStartOffset(delay);

			v.startAnimation(anim);
	}

	private static class MyAnimationListener implements Animation.AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {

			runningAnim ++;
		}

		@Override
		public void onAnimationEnd(Animation animation) {

			runningAnim --;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}
	}
}