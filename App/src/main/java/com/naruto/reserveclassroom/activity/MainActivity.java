package com.naruto.reserveclassroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.bean.CurrentUserInfoBean;
import com.naruto.reserveclassroom.utils.AnimationUtils;
import com.naruto.reserveclassroom.utils.TestNetworkStateUtil;
import com.naruto.reserveclassroom.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;
	@Bind(R.id.fab_add_reserve)
	FloatingActionButton fab_add_reserve;
	@Bind(R.id.nav_view)
	NavigationView nav_view;
	@Bind(R.id.drawer_layout)
	DrawerLayout drawer_layout;
	private TextView tv_username;
	private TextView tv_userphone;
	private Context mContext;
	private AVUser currentUser = AVUser.getCurrentUser();
	private TextView tv_mainpage_title;
	private ListView lv_history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		setSupportActionBar(toolbar);
		initUI();
		initDataBase();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {

		ButterKnife.bind(this);

		// 判断当前是否有网络连接，若无网络连接，做相应UI和逻辑处理
		if (!TestNetworkStateUtil.isNetworkConnected(mContext)) {

			ToastUtil.showShort(mContext, "未检测到网络连接！");
		}
		// 找到主页面的两个控件
		tv_mainpage_title = (TextView) findViewById(R.id.tv_mainpage_title);
		lv_history = (ListView) findViewById(R.id.lv_history);
		if (!isLogin() || (!isLogin() && !TestNetworkStateUtil.isNetworkConnected(mContext))) {

			tv_mainpage_title.setText("请在登陆后使用预约相关功能！");
			lv_history.setVisibility(View.INVISIBLE);
		}
		// 设置右下角的加号的点击事件
		fab_add_reserve.setOnClickListener(this);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
						R.string.navigation_drawer_close);
		if (drawer_layout != null) {

			drawer_layout.setDrawerListener(toggle);
		}
		toggle.syncState();

		if (nav_view != null) {

			nav_view.setNavigationItemSelectedListener(this);
			// 找到头控件
			View headerView = nav_view.getHeaderView(0);
			// 找到头控件上的图片（用户头像）
			ImageButton ib_userpicture = (ImageButton) headerView.findViewById(R.id.ib_userpicture);
			// 设置用户头像的点击事件
			ib_userpicture.setOnClickListener(this);
			// 找到头控件上的两个textview
			tv_username = (TextView) headerView.findViewById(R.id.bt_username);
			tv_userphone = (TextView) headerView.findViewById(R.id.tv_userphone);
			if (isLogin()) {

				// 已登录，拿到当前实时的用户信息，给头控件设置回显
				List<CurrentUserInfoBean> currentUserInfo = getCurrentUserInfo();
				String username = currentUserInfo.get(0).getUsername();
				String phoneNumber = currentUserInfo.get(0).getMobilePhoneNumber();
				tv_username.setText(username);
				tv_userphone.setText(phoneNumber);
			} else {

				// 未登陆，提示用户登录
				tv_username.setText("请在登陆后使用预约相关功能！");
				tv_userphone.setText(" ");
			}
		}
	}

	/**
	 * 获取当前是否已登录
	 * 
	 * @return 是否登陆--true：已登录；false:未登录
	 */
	private boolean isLogin() {

		return AVUser.getCurrentUser() != null;
	}

	/**
	 * 获取当前用户状态信息
	 */
	private List<CurrentUserInfoBean> getCurrentUserInfo() {

		// 获取当前用户
		AVUser currentUser = AVUser.getCurrentUser();
		// 创建一个用户信息的泛型
		CurrentUserInfoBean currentUserInfoBean = new CurrentUserInfoBean();
		// 创建一个存储用户信息泛型的数组
		List<CurrentUserInfoBean> currentUserInfoBeanList = new ArrayList<>();
		// 获取用户的姓名，手机号，邮箱号
		currentUserInfoBean.setUsername(currentUser.getUsername());
		currentUserInfoBean.setMobilePhoneNumber(currentUser.getMobilePhoneNumber());
		currentUserInfoBean.setEmail(currentUser.getEmail());
		currentUserInfoBeanList.add(currentUserInfoBean);

		return currentUserInfoBeanList;
	}

	/**
	 * 初始化数据库
	 */
	private void initDataBase() {

		// 所有教室信息数据库拷贝
		CopyDBFiles("allclassrooms.db");
		// 所有可选联系人信息数据库拷贝
		CopyDBFiles("contacts.db");
	}

	/**
	 * 将数据库从资产目录拷贝到手机存储
	 * 
	 * @param dbName
	 *            数据库名
	 */
	private void CopyDBFiles(String dbName) {

		InputStream is = null;
		FileOutputStream fos = null;

		// 1.在files目录下创建同名文件
		File files = getFilesDir();
		File file = new File(files, dbName);
		if (file.exists()) {

			return;
		}
		// 2.输入流读取第三方assets目录的文件
		try {

			is = getAssets().open(dbName);
			// 3.将读取的内容写入到指定的目录中去
			fos = new FileOutputStream(file);
			// 4.每次读取的内容大小
			byte[] bytes = new byte[1024];
			int temp = -1;
			while ((temp = is.read(bytes)) != -1) {

				fos.write(bytes, 0, temp);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {

				if (is != null && fos != null) {

					is.close();
					fos.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	// 加号按钮的点击操作
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.fab_add_reserve:

			System.out.println("fab按钮被点击了");
			if (TestNetworkStateUtil.isNetworkConnected(mContext)) {

				if (fab_add_reserve != null && currentUser != null) {

					if (AnimationUtils.runningAnim > 0) {

						return;
					} else {

						AnimationUtils.RotateCounterAnim(v, 0);
					}
					// TODO: 2016-12-11 这里需要加入页面跳转过度动画，已让按钮动画完成并对计数-1
					startActivity(new Intent(mContext, ReserveClassRoomActivity.class));
				} else {

					ToastUtil.showShort(mContext, "请在登陆后使用预约相关功能！");
				}
			} else {

				ToastUtil.showShort(mContext, "无网络状态下无法获取教室信息，请检查网络连接！");
			}

			break;

		case R.id.ib_userpicture:

			if (TestNetworkStateUtil.isNetworkConnected(mContext)) {

				// 有网络时直接进入个人中心或登录页
				if (isLogin()) {

					// 已登录，进入个人中心页
					startActivity(new Intent(mContext, UserInfoActivity.class));
				} else {

					// 未登陆,进入登录页
					startActivity(new Intent(mContext, LoginActivity.class));
				}
			} else {

				// 无网络，可以进入登录或个人中心，但是要增加一条吐司
				if (isLogin()) {

					// 无网络时可以进入个人中心，但是要增加一条吐司，并禁用所有修改箭头的点击跳转
					startActivity(new Intent(mContext, UserInfoActivity.class));
				} else {

					ToastUtil.showShort(mContext, "当前无网络连接，无法实现账号登陆！");
				}
			}

			break;

		default:

			break;
		}
	}

	@Override
	public void onBackPressed() {

		if (drawer_layout != null) {

			if (drawer_layout.isDrawerOpen(GravityCompat.START)) {

				drawer_layout.closeDrawer(GravityCompat.START);
			} else {

				super.onBackPressed();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		switch (id) {

		case R.id.nav_camera:

			break;

		case R.id.nav_gallery:

			break;

		case R.id.nav_slideshow:

			break;

		case R.id.nav_manage:

			break;

		case R.id.nav_share:

			break;

		case R.id.nav_logout:

			AVUser.logOut();
			// 注销后，一系列提示和禁用操作
			AfterLogoutForbidden();

			break;

		default:
			break;
		}

		if (drawer_layout != null) {

			drawer_layout.closeDrawer(GravityCompat.START);
		}
		return true;
	}

	/**
	 * 注销后，一系列提示和禁用操作
	 */
	private void AfterLogoutForbidden() {

		tv_username.setText("请在登陆后使用预约相关功能！");
		tv_userphone.setText(" ");
		tv_mainpage_title.setText("请在登陆后使用预约相关功能！");
	}

	@Override
	protected void onResume() {

		super.onResume();

		AnimationUtils.RotateAnim(fab_add_reserve,0);
		if (isLogin()) {

			// 已登录，拿到当前实时的用户信息，给头控件设置回显
			List<CurrentUserInfoBean> currentUserInfo = getCurrentUserInfo();
			String username = currentUserInfo.get(0).getUsername();
			String phoneNumber = currentUserInfo.get(0).getMobilePhoneNumber();
			tv_username.setText(username);
			tv_userphone.setText(phoneNumber);
		} else {

			// 未登陆，提示用户登录
			tv_username.setText("请在登陆后使用预约相关功能！");
			tv_userphone.setText(" ");
			tv_mainpage_title.setText("请在登陆后使用预约相关功能！");
		}
		// 重新获得焦点检测网络状态
		if (!TestNetworkStateUtil.isNetworkConnected(mContext)) {

			ToastUtil.showShort(mContext, "未检测到网络连接！");
		}
		// 如果有新增的借用记录，在历史记录添加一条记录
	}
}