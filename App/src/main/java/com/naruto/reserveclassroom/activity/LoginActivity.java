package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/6.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.utils.TestNetworkStateUtil;
import com.naruto.reserveclassroom.utils.ToastUtil;
import com.naruto.reserveclassroom.utils.UserInfoUtil;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

	@Bind(R.id.pb_login)
	ProgressBar pb_login;
	@Bind(R.id.actv_username)
	AutoCompleteTextView actv_username;
	@Bind(R.id.et_password)
	EditText et_password;
	@Bind(R.id.bt_username_login)
	Button bt_username_login;
	@Bind(R.id.bt_username_register)
	Button bt_username_register;
	@Bind(R.id.sv_login)
	ScrollView sv_login;
	@Bind(R.id.cb_remember_name_psw)
	CheckBox cb_remember_name_psw;
	private Context mContext;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);

		mContext = this;
		initUI();
		initData();

		if (AVUser.getCurrentUser() != null) {

			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}
	}

	/**
	 * 初始化界面，找到控件
	 */
	private void initUI() {

		ButterKnife.bind(this);

		if (!TestNetworkStateUtil.isNetworkConnected(mContext)) {

			ToastUtil.showShort(mContext,"当前无网络连接,将无法实现登陆功能！");
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("登陆");

		et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

				if (id == R.id.login || id == EditorInfo.IME_NULL) {

					login();

					return true;
				}

				return false;
			}
		});
	}

	/**
	 * 获取保存的用户名密码
	 */
	private void initData() {

		// 获取存储的用户登录信息
		Map<String, String> userInfo = UserInfoUtil.getUserInfo(this);

		if (userInfo != null) {

			// 如果不为空,则直接将相关参数设置给输入框
			actv_username.setText(userInfo.get("userInputStr"));
			et_password.setText(userInfo.get("password"));
		}
	}

	@OnClick({R.id.bt_username_login, R.id.bt_username_register})
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.bt_username_login:

			// 点击了登录按钮，触发登陆
			login();

			break;

		case R.id.bt_username_register:

			if (TestNetworkStateUtil.isNetworkConnected(mContext)) {

				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				finish();
			}else {

				ToastUtil.showShort(mContext, "无网络状态下无法进行注册！");
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 登录按钮的登录方法
	 */
	private void login() {

		String phone = null;
		String userName = null;
		actv_username.setError(null);
		et_password.setError(null);

		// 获取当前两个输入框的内容
		String userInputStr = actv_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		// 设置手机号的匹配正则表达式
		String regularExpression = "^1(3|4|5|7|8)\\d{9}$";
		// 判断用户输入的是用户名还是手机号
		if (userInputStr.matches(regularExpression)) {

			phone = userInputStr;
		} else {

			userName = userInputStr;
		}
		// 获取cb是否被选中
		boolean isChecked = cb_remember_name_psw.isChecked();

		boolean cancel = false;
		View focusView = null;


		if (TextUtils.isEmpty(password)) {

			et_password.setError("密码不能为空");
			focusView = et_password;
			cancel = true;
		} else if (!isPasswordValid(password)) {

			et_password.setError("密码不能少于6位");
			focusView = et_password;
			cancel = true;
		}

		if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(phone)) {

			actv_username.setError("这里不能为空");
			focusView = actv_username;
			cancel = true;
		}

		if (isChecked) {

			// 点击了记住用户名密码，将用户名密码写到userinfo.text中
			if (userName != null) {

				UserInfoUtil.saveUserInfo(mContext, userName, password);
			} else if (phone != null) {

				UserInfoUtil.saveUserInfo(mContext, phone, password);
			}
		}

		if (cancel) {

			focusView.requestFocus();
		} else {

			showProgress(true);

			// 根据用户输入账户类型不同采取不同的登录方式
			if (userName != null) {

				// 用户名登录
				AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {

					@Override
					public void done(AVUser avUser, AVException e) {

						if (e == null) {

							finish();
							startActivity(new Intent(mContext, MainActivity.class));
						} else {

							showProgress(false);
							ToastUtil.showShort(mContext, e.getMessage());
						}
					}
				});
			} else if (phone != null) {

				// 手机号登录
				AVUser.loginByMobilePhoneNumberInBackground(phone, password,
								new LogInCallback<AVUser>() {

									@Override
									public void done(AVUser avUser, AVException e) {

										if (e == null) {

											finish();
											startActivity(new Intent(mContext, MainActivity.class));
										} else {

											showProgress(false);
											ToastUtil.showShort(mContext, e.getMessage());
										}
									}
								});
			}
		}
	}

	private boolean isPasswordValid(String password) {

		// TODO: Replace this with your own logic
		return password.length() >= 6;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 * 
	 * @param show
	 *            传入的Boolean类型的值，对应不同的控件，会有不同的显示/消失的设置
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			sv_login.setVisibility(show ? View.GONE : View.VISIBLE);
			sv_login.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
							.setListener(new AnimatorListenerAdapter() {

								@Override
								public void onAnimationEnd(Animator animation) {

									sv_login.setVisibility(show ? View.GONE : View.VISIBLE);
								}
							});

			pb_login.setVisibility(show ? View.VISIBLE : View.GONE);
			pb_login.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
							.setListener(new AnimatorListenerAdapter() {

								@Override
								public void onAnimationEnd(Animator animation) {

									pb_login.setVisibility(show ? View.VISIBLE : View.GONE);
								}
							});
		} else {

			// The ViewPropertyAnimator APIs are not available, so simply showShort
			// and hide the relevant UI components.
			pb_login.setVisibility(show ? View.VISIBLE : View.GONE);
			sv_login.setVisibility(show ? View.GONE : View.VISIBLE);
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
		AVAnalytics.onPause(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		AVAnalytics.onResume(this);
	}
}