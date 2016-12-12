package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/14.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.avos.avoscloud.AVAnalytics;
import com.naruto.reserveclassroom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePhoneNumberActivity extends AppCompatActivity {

	@Bind(R.id.actv_input_phonenum)
	AutoCompleteTextView actv_input_phonenum;
	@Bind(R.id.bt_verifity_phonenum)
	Button bt_verifity_phonenum;
	private Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_phonenumber);

		mContext = this;

		initUI();
	}
	/**
	 * 初始化UI
	 */
	private void initUI() {

		ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("更改绑定手机号");
	}


	@OnClick(R.id.bt_verifity_phonenum)
	public void onClick() {

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
	}
}
