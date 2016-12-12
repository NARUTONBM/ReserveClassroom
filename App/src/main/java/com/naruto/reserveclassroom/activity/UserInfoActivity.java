package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/13.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.utils.TestNetworkStateUtil;
import com.naruto.reserveclassroom.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

	@Bind(R.id.iv_userpicture)
	ImageView iv_userpicture;
	@Bind(R.id.iv_pic_forward)
	ImageView iv_pic_forward;
	@Bind(R.id.tv_userphone)
	TextView tv_userphone;
	@Bind(R.id.iv_phone_forward)
	ImageView iv_phone_forward;
	@Bind(R.id.iv_viefiy_forward)
	ImageView iv_viefiy_forward;
	@Bind(R.id.bt_username)
	Button bt_username;
	private Context mContext;
	private AVUser mCurrentUser;
	private String mUsername;
	private String mMobilePhoneNumber;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ButterKnife.bind(this);

		mContext = this;

		initData();
		initUI();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		if (TestNetworkStateUtil.isNetworkConnected(mContext)) {

			mCurrentUser = AVUser.getCurrentUser();
			mUsername = mCurrentUser.getUsername();
			mMobilePhoneNumber = mCurrentUser.getMobilePhoneNumber();
			// System.out.println("Username:" + mUsername + "MobilePhone:" +
			// mMobilePhoneNumber);
		}
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {

		ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("用户中心");
		if (!TestNetworkStateUtil.isNetworkConnected(mContext)) {

			ToastUtil.showShort(mContext, "当前无网络连接，将无法修改个人信息！");
		} else {

			// System.out.println("Username:" + mUsername + " MobilePhone:" +
			// mMobilePhoneNumber);
			bt_username.setText(mUsername);
			tv_userphone.setText(mMobilePhoneNumber);
		}

	}

	/**
	 * 弹出修改头像的对话框
	 */
	private void showUserPicUpdateDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		AlertDialog dialog = builder.create();
		View view = View.inflate(mContext, R.layout.dialog_update_user_pic, null);

		// 设置自定义对话框样式
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

		// 获取button控件
		Button bt_camera_upload = (Button) view.findViewById(R.id.bt_camera_upload);
		Button bt_album_upload = (Button) view.findViewById(R.id.bt_album_upload);
		// 设置相应按钮的点击事件
		bt_camera_upload.setOnClickListener(this);
		bt_album_upload.setOnClickListener(this);
	}

	@OnClick({R.id.iv_pic_forward, R.id.iv_phone_forward, R.id.iv_viefiy_forward, R.id.bt_username})
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.iv_pic_forward:

			// 弹出修改头像的对话框
			showUserPicUpdateDialog();

			break;

		case R.id.bt_username:

			// 提示用户用户名不可更改
			ToastUtil.showLong(mContext, "用户不可更改，将在身份验证之后自动更改为您的姓名！");

			break;

		case R.id.iv_phone_forward:

			startActivity(new Intent(mContext, UpdatePhoneNumberActivity.class));

			break;

		case R.id.iv_viefiy_forward:

			break;

		case R.id.bt_camera_upload:

			break;

		case R.id.bt_album_upload:

			break;

		default:

			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {

		super.onPause();
		AVAnalytics.onPause(mContext);
	}

	@Override
	protected void onResume() {

		super.onResume();

		AVAnalytics.onResume(mContext);

		// 刷新当前用户的信息
		initData();
		initUI();
	}
}
