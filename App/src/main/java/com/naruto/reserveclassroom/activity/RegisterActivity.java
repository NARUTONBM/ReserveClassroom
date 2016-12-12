package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/9.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

	@Bind(R.id.pb_register)
	ProgressBar pb_register;
	@Bind(R.id.actv_reg_userphone)
	AutoCompleteTextView actv_reg_userPhone;
	@Bind(R.id.et_reg_password)
	EditText et_reg_password;
	@Bind(R.id.tv_reg_username)
	EditText tv_reg_username;
	@Bind(R.id.bt_username_register)
	Button bt_username_register;
	@Bind(R.id.sv_register)
	ScrollView sv_register;
	private Context mContext;
	private EditText et_phone_verify_code;
	private AlertDialog mDialog;
	private Button bt_reget_code;
	private TextView tv_reset_code;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);

		mContext = this;

		initUI();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {

		ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("注册");
		// Set up the register form.
		et_reg_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

				if (id == R.id.register || id == EditorInfo.IME_NULL) {

					register();

					return true;
				}

				return false;
			}
		});
	}

	@OnClick(R.id.bt_username_register)
	public void onClick() {

		register();
	}

	/**
	 * 点击注册按钮的操作事件
	 */
	private void register() {

		actv_reg_userPhone.setError(null);
		et_reg_password.setError(null);

		// 拿到输入框输入的内容
		final String userphone = actv_reg_userPhone.getText().toString();
		String username = tv_reg_username.getText().toString();
		String password = et_reg_password.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(userphone)) {

			actv_reg_userPhone.setError("这里不能为空");
			focusView = actv_reg_userPhone;
			cancel = true;
		} else if (!isuserPhoneValid(userphone)) {

			actv_reg_userPhone.setError("请输入正确的手机号");
			focusView = actv_reg_userPhone;
			cancel = true;
		}

		if (TextUtils.isEmpty(username)) {

			tv_reg_username.setError("姓名不能为空");
			focusView = tv_reg_username;
			cancel = true;
		} else if (!isusernameValid(username)) {

			tv_reg_username.setError("请输入正确的人名");
			focusView = tv_reg_username;
			cancel = true;
		}

		if (TextUtils.isEmpty(password)) {

			et_reg_password.setError("密码不能为空");
			focusView = et_reg_password;
			cancel = true;
		} else if (!isPasswordValid(password)) {

			et_reg_password.setError("密码不能少于6位");
			focusView = et_reg_password;
			cancel = true;
		}

		if (cancel) {

			focusView.requestFocus();
		} else {

			showProgress(true);

			// 新建 AVUser 对象实例
			final AVUser user = new AVUser();
			// 设置用户名和手机号
			user.setUsername(username);
			user.setMobilePhoneNumber(userphone);
			// 设置密码
			user.setPassword(password);
			user.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(AVException e) {

					if (e == null) {

						// 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
						AVUser currentUser = AVUser.getCurrentUser();
						currentUser = user;
						showSmsCodeDialog();
					} else {

						// 失败的原因可能有多种，常见的是用户名已经存在。
						showProgress(false);
						ToastUtil.showShort(mContext, e.getMessage());
					}
				}
			});
		}
	}

	private boolean isuserPhoneValid(String userphone) {

		// 设置人名的输入规则
		String phoneRE = "^1[3|4|5|7|8][0-9]{9}$";
		return userphone.matches(phoneRE);
	}

	private boolean isusernameValid(String username) {

		// 设置人名的输入规则
		String nameRE = "[\\u4E00-\\u9FA5]{2,4}";
		return username.matches(nameRE);
	}

	private boolean isPasswordValid(String password) {

		return password.length() >= 6;
	}

	private void showSmsCodeDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		mDialog = builder.create();
		View view = View.inflate(mContext, R.layout.dialog_phone_verify, null);

		mDialog.setView(view, 0, 0, 0, 0);
		mDialog.setCancelable(false);

		// 获取控件
		tv_reset_code = (TextView) view.findViewById(R.id.tv_reset_code);
		et_phone_verify_code = (EditText) view.findViewById(R.id.et_phone_verify_code);
		bt_reget_code = (Button) view.findViewById(R.id.bt_reget_code);
		final Button bt_phone_verify_ensure = (Button) view.findViewById(R.id.bt_phone_verify_ensure);
		mDialog.show();

		//设置重新获取验证码按钮不可点击
		bt_reget_code.setClickable(false);
		bt_reget_code.setBackgroundResource(R.drawable.bt_reget_code_bg_normal);
		//开始定时器
		startCountDownTimer(tv_reset_code, bt_reget_code);

		// 设置点击事件
		bt_phone_verify_ensure.setOnClickListener(RegisterActivity.this);
	}

	private void startCountDownTimer(final TextView tv_reset_code, final Button bt_reget_code) {

		new CountDownTimer(360000, 1000) {

			//定时中
			public void onTick(long millisUntilFinished) {

				tv_reset_code.setText(millisUntilFinished / 1000+"后可重新获取验证码");
			}

			//定时结束
			public void onFinish() {

				tv_reset_code.setText("请重新获取验证码进行验证！");
				bt_reget_code.setClickable(true);
				bt_reget_code.setBackgroundResource(R.drawable.bt_reget_code_bg_normal);
				bt_reget_code.setOnClickListener(RegisterActivity.this);
			}
		}.start();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			sv_register.setVisibility(show ? View.GONE : View.VISIBLE);
			sv_register.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					sv_register.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});

			pb_register.setVisibility(show ? View.VISIBLE : View.GONE);
			pb_register.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					pb_register.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply
			// showShort
			// and hide the relevant UI components.
			pb_register.setVisibility(show ? View.VISIBLE : View.GONE);
			sv_register.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		    case R.id.bt_reget_code:

				AVUser.requestMobilePhoneVerifyInBackground(AVUser.getCurrentUser().getMobilePhoneNumber(), new RequestMobileCodeCallback() {

					@Override
					public void done(AVException e) {

						if (e==null) {

							//再次开始倒计时
							startCountDownTimer(tv_reset_code, bt_reget_code);
						}else {

							e.printStackTrace();
						}
					}
				});

		        break;

			case R.id.bt_phone_verify_ensure:

				String code = et_phone_verify_code.getText().toString();
				AVOSCloud.verifySMSCodeInBackground(code, AVUser.getCurrentUser().getMobilePhoneNumber(), new AVMobilePhoneVerifyCallback() {

					@Override
					public void done(AVException e) {

						if (e == null) {

							et_phone_verify_code.requestFocus();
							startActivity(new Intent(RegisterActivity.this, MainActivity.class));
							RegisterActivity.this.finish();
						} else {

							e.printStackTrace();
						}
					}
				});
				mDialog.dismiss();

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
		AVAnalytics.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AVAnalytics.onResume(this);
	}
}