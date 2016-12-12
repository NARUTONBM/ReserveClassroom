package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/10.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.db.dao.ContactsDao;
import com.naruto.reserveclassroom.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

public class PostReservationActivity extends AppCompatActivity {

	@Bind(R.id.tv_rese_roomNo)
	TextView tv_rese_roomNo;
	@Bind(R.id.et_manager_contact)
	EditText et_manager_contact;
	@Bind(R.id.et_reserver_contact)
	EditText et_reserver_contact;
	@Bind(R.id.tv_rese_time)
	TextView tv_rese_time;
	@Bind(R.id.tv_rese_time_present)
	TextView tv_rese_time_present;
	@Bind(R.id.bt_time_choose)
	Button bt_time_choose;
	@Bind(R.id.cb_use_multimedia)
	CheckBox cb_use_multimedia;
	@Bind(R.id.et_rese_reason)
	EditText et_rese_reason;
	@Bind(R.id.cb_agree_commitment)
	CheckBox cb_agree_commitment;
	@Bind(R.id.bt_read_commitment)
	Button bt_read_commitment;
	@Bind(R.id.bt_submit_reservation)
	Button bt_submit_reservation;
	@Bind(R.id.tv_rese_date)
	TextView tv_rese_date;
	private Context mContext;
	private List<ContactsDao.Contacts> mContactsForManager, mContactsForReserver;
	private ListView listView;
	private PopupWindow popupWindow;
	private int bt_choose_clicked = 0;
	private String mStartH;
	private String mStartM;
	private String mReseTime;
	private String mCurrentdate;
	private String mCurrentDateWeek;
	private List<Integer> mAvaiTSList;
	private String mRoomNo;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postreservation);
		ButterKnife.bind(this);

		mContext = this;

		initUI();
		initData();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {

		ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("提交申请");
		listView = new ListView(this);
		listView.setDividerHeight(0);
		listView.setBackgroundResource(R.mipmap.listview_background);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 拿到ReserveClassRoomActivity传过来的数据(申请的教室号、日期)，并将之设置给textview
		String date = getIntent().getStringExtra("date");
		mRoomNo = getIntent().getStringExtra("roomNo");
		mAvaiTSList = getIntent().getIntegerArrayListExtra("avaiTime");
		tv_rese_date.setText(date);
		tv_rese_roomNo.setText(mRoomNo + "教室借用申请");
		ContactsDao contactsDao = ContactsDao.getInstance(mContext);
		mContactsForManager = contactsDao.getContacts(0);
		mContactsForReserver = contactsDao.getContacts(1);
		String[] split = date.split(" ");
		mCurrentdate = split[0];
		mCurrentDateWeek = split[1];
	}

	@OnClick({R.id.bt_time_choose, R.id.bt_read_commitment, R.id.bt_submit_reservation})
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.bt_time_choose:

			// 使屏幕变暗
			makeWindowDark();
			// 展示时间选择器
			showTimePicker();

			break;

		case R.id.bt_read_commitment:

			// 跳转到一个页面展示承诺书

			break;

		case R.id.bt_submit_reservation:

			// 获取输入框的信息，并对输入的信息进行校验
			List<String> inputResult = provideInputInfo();
			// 提交申请
			submitReservation(inputResult);

			break;

		/*case R.id.ib_manager_down_arrow:

			// 弹出负责人的下拉选择联系人
			showPopupWindow(R.id.ib_manager_down_arrow, et_manager_contact);

			break;

		case R.id.ib_reserver_down_arrow:

			// 弹出联系人的下拉选择联系人
			showPopupWindow(R.id.ib_reserver_down_arrow, et_reserver_contact);

			break;*/

		default:

			break;
		}
	}

	/**
	 * 展示自定义的时间选择器
	 */
	private void showTimePicker() {

		bt_choose_clicked ++;
		TimePicker timePicker = new TimePicker(this, TimePicker.HOUR_24);
		if (bt_choose_clicked % 2 == 1) {

			// 奇数次点击设置开始时间
			if (mCurrentDateWeek.equals("星期六") || mCurrentDateWeek.equals("星期六")) {

				// 周末开始时间至少在8点之后
				timePicker.setRangeStart(8, 0);
			} else {

				// 工作日开始时间至少在18点之后
				timePicker.setRangeStart(18, 0);
			}
			// 最晚要在20点之前开始使用
			timePicker.setRangeEnd(20, 0);
			timePicker.setTopLineVisible(false);
			// 设置时间选择器的时间点击事件
			timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {

				@Override
				public void onTimePicked(String hour, String minute) {

					// 拿到当前的开始时间，并为何结束时间作对比做准备
					tv_rese_time_present.setText(hour + ":" + minute + "-");
					bt_time_choose.setText("选择结束时间");
					mStartH = hour;
					mStartM = minute;
					// 关闭时间选择器，将屏幕变亮
					makeWindowLight();
				}
			});
		} else {

			// 偶数次点击设置结束时间
			if (mCurrentDateWeek.equals("星期六") || mCurrentDateWeek.equals("星期六")) {

				// 周末结束时间至少在9点之后
				timePicker.setRangeStart(9, 0);
			} else {

				// 工作日结束时间至少在19点之后
				timePicker.setRangeStart(19, 0);
			}
			// 每天最晚可以到22点
			timePicker.setRangeEnd(22, 0);
			timePicker.setTopLineVisible(false);
			// 设置时间选择器的时间点击事件
			timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {

				@Override
				public void onTimePicked(String hour, String minute) {

					if (hour.compareTo(mStartH) > 0 || (hour.compareTo(mStartH) == 0 && minute.compareTo(mStartM) > 0)) {

						// 当结束时间在开始时间之后才是符合规则
						int startTS = Integer.parseInt(mStartH) * 3600 + Integer.parseInt(mStartM) * 60;
						int endTS = Integer.parseInt(hour) * 3600 + Integer.parseInt(minute) * 60;
						// 需要判断选择的时间是否在规定时间之内
						for (int i = 0; i < mAvaiTSList.size() / 2; i ++) {

							if (startTS >= mAvaiTSList.get(i * 2)) {

								// 如果开始时间戳与某个时间戳相等，则去判断结束时间是否下一个时间戳小或相等
								if (endTS <= mAvaiTSList.get(i * 2 + 1)) {

									// 符合，则设借用时间给tv
									mReseTime = mStartH + ":" + mStartM + "-" + hour + ":" + minute;
									tv_rese_time_present.setText(mReseTime);
									bt_time_choose.setText("选择开始时间");
								}
							} else {

								// 不符合，清空tv，并弹出吐司提示用户重新选择时间或教室
								tv_rese_time_present.setText(" ");
								ToastUtil.showLong(mContext, "注意可借时段，请重新选择借用时间或更换教室！");
							}
						}
					} else {

						// 不符合规则，提示用户重新选择时间
						ToastUtil.showShort(mContext, "结束时间必须在开始时间之后");
						bt_choose_clicked --;
					}
					// 关闭时间选择器，将屏幕变亮
					makeWindowLight();
				}
			});
		}
		timePicker.show();
	}

	/**
	 * 让屏幕变暗
	 */
	private void makeWindowDark() {

		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.5f;
		window.setAttributes(lp);
	}

	/**
	 * 让屏幕变亮
	 */
	private void makeWindowLight() {

		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 1f;
		window.setAttributes(lp);
	}

	/**
	 * 获取并校验输入的负责人和联系人信息
	 * 
	 * @return 返回符合规则的负责人和联系人
	 */
	private List<String> provideInputInfo() {

		// 创建一个放置输入信息的数组
		List<String> inputResult = new ArrayList<>();
		// 分别判断输入的负责人和联系人是否符合规则，符合则将之添加到准备好的数组中
		if (checkInputInfo(et_manager_contact))
			inputResult.add(et_manager_contact.getText().toString());
		if (checkInputInfo(et_reserver_contact))
			inputResult.add(et_reserver_contact.getText().toString());

		return inputResult;
	}

	/**
	 * 校验输入的内容是否符合规则
	 *
	 * @param editText
	 *            对应的输入框对象
	 * @return 校验的结果
	 */
	private boolean checkInputInfo(EditText editText) {

		String inputInfo = editText.getText().toString();
		if (inputInfo.isEmpty()) {

			editText.setError("此处不能为空！");
		} else {

			if (inputInfo.contains("#")) {

				String[] split = inputInfo.split("#");
				// 设置人名和手机号的匹配规则
				String nameRE = getString(R.string.nameRE);
				String phoneRE = getString(R.string.phoneRE);
				if (split[0].matches(nameRE) && split[1].matches(phoneRE)) {

					return true;
				} else {

					editText.setError("输入的信息不规范！");
				}
			} else {

				editText.setError("缺失“#”号！");
			}
		}

		return false;
	}

	/**
	 * 提交申请的方法
	 * 
	 * @param inputResult
	 *            输入的负责人和联系人信息
	 */
	private void submitReservation(List<String> inputResult) {

		// 获取classroom表
		AVQuery<AVObject> classroom = new AVQuery<>("Classroom");
		updateBorrowDetail(classroom, inputResult, new DataCallback() {

			@Override
			public void postData(AVObject avObject, ArrayList<HashMap<String, String>> borrowDetail) {

				// 用准备好的新的信息，覆盖原有的details
				avObject.put("borrowDetails", borrowDetail);
				avObject.saveInBackground(new SaveCallback() {

					@Override
					public void done(AVException e) {

						if (e == null) {

							System.out.println("update successfully!");
							Intent intent = new Intent(mContext, MainActivity.class);
							startActivity(intent);
						} else {

							System.out.println("update failed!");
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	/**
	 * 准备申请信息的更新数据
	 * 
	 * @param classroom
	 *            教室表
	 * @param inputResult
	 *            输入的负责人和联系人
	 * @param callback
	 *            回调方法
	 */
	private void updateBorrowDetail(final AVQuery<AVObject> classroom, List<String> inputResult, final DataCallback callback) {

		// 创建一个hashmap用于放置将要上传的申请信息
		final HashMap<String, String> updateHashMap = new HashMap<>();
		// 拿到输入的借用详细
		String reseReason = et_rese_reason.getText().toString();
		// 只有所有信息满足要求才能提交申请
		if (inputResult.size() != 2) {

			// 负责人和联系人输入不全
			ToastUtil.showShort(mContext, "请检查输入的负责人和联系人");
		} else if (mReseTime==null) {

			// 未选择借用时间
			ToastUtil.showShort(mContext, "请选择借用时间！");
		} else if (TextUtils.isEmpty(reseReason)) {

			// 活动详细内容为空
			et_rese_reason.setError("活动详细内容不能为空");
		} else if (reseReason.length() < 10) {

			// 活动详细内容少于10个字
			et_rese_reason.setError("活动详细内容不能少于10个字");
		} else if (!cb_agree_commitment.isChecked()) {

			// 未同意《教室借用承诺书》
			ToastUtil.showShort(mContext, "只有同意《教室借用承诺书》才能提交申请");
		} else {

			// 满足条件，往准备好的hashmap中插入申请信息
			updateHashMap.put("borrower", inputResult.get(0));
			updateHashMap.put("manager", inputResult.get(1));
			String[] split = mReseTime.split("-");
			updateHashMap.put("startT", mCurrentdate + " " + split[0] + ":00");
			updateHashMap.put("endT", mCurrentdate + " " + split[1] + ":00");
			int multiMedia = 0;
			if (cb_use_multimedia.isChecked()) {

				multiMedia = 1;
			}
			updateHashMap.put("multiMedia", String.valueOf(multiMedia));
			updateHashMap.put("currentState", String.valueOf(1));
			updateHashMap.put("activityDetail", reseReason);
			// 准备用于更新的数据
			classroom.whereContains("roomNo", mRoomNo);
			classroom.findInBackground(new FindCallback<AVObject>() {

				@Override
				public void done(List<AVObject> list, AVException e) {

					final ArrayList<HashMap<String, String>> borrowDetails = (ArrayList<HashMap<String, String>>) list.get(0).get("borrowDetails");
					borrowDetails.add(updateHashMap);
					classroom.getInBackground(list.get(0).getObjectId(), new GetCallback<AVObject>() {

						@Override
						public void done(AVObject avObject, AVException e) {

							callback.postData(avObject, borrowDetails);
						}
					});
				}
			});
		}
	}

	/**
	 * 根据点击的下拉按钮的位置，为不同的输入框弹出下拉选择框
	 * 
	 * @param imageviewId
	 *            被点击下拉按钮的id
	 * @param editTextId
	 *            需要弹出选择框的输入框对象
	 */
	private void showPopupWindow(int imageviewId, EditText editTextId) {

		// 初始化供popupwindow展示用的listview的数据
		initListView(imageviewId, editTextId);

		// 设置下拉选择框
		popupWindow = new PopupWindow(listView, editTextId.getWidth(), 500);
		// 设置点击区域外自动隐藏
		popupWindow.setOutsideTouchable(true);
		// 设置空的背景，相应点击事件
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置可获取焦点
		popupWindow.setFocusable(true);
		// 显示在指定控件下
		popupWindow.showAsDropDown(editTextId, 0, -5);
	}

	/**
	 * 根据点击的下拉按钮的位置，为不同的输入框初始化下拉选择框的数据以及回显
	 * 
	 * @param viewId
	 *            被点击下拉按钮的id
	 * @param editText
	 *            需要弹出选择框的输入框对象
	 */
	private void initListView(final int viewId, final EditText editText) {

		listView = new ListView(this);
		listView.setDividerHeight(0);
		listView.setBackgroundResource(R.mipmap.listview_background);

		// 设置数据适配器
		listView.setAdapter(new MyAdapter(editText));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// 给对应的输入框添加数据
				if (viewId == R.id.et_manager_contact) {

					ContactsDao.Contacts managerInfo = mContactsForManager.get(position);
					editText.setText(managerInfo.name + "#" + managerInfo.phone);
				} else if (viewId == R.id.et_reserver_contact) {

					ContactsDao.Contacts reserverInfo = mContactsForReserver.get(position);
					editText.setText(reserverInfo.name + "#" + reserverInfo.phone);
				}

				// 关闭popupwindow
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 下拉选择框的数据适配器
	 */
	private class MyAdapter extends BaseAdapter {

		int editTextId = 0;

		MyAdapter(EditText editText) {

			editTextId = editText.getId();
		}

		@Override
		public int getCount() {

			if (editTextId == R.id.et_manager_contact) {

				return mContactsForManager.size();
			} else if (editTextId == R.id.et_reserver_contact) {

				return mContactsForReserver.size();
			} else {

				return 0;
			}
		}

		@Override
		public ContactsDao.Contacts getItem(int position) {

			if (editTextId == R.id.et_manager_contact) {

				return mContactsForManager.get(position);
			} else if (editTextId == R.id.et_reserver_contact) {

				return mContactsForReserver.get(position);
			} else {

				return null;
			}
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {

			// 复用convertview
			ViewHolder holder;
			if (convertView == null) {

				convertView = View.inflate(getApplicationContext(), R.layout.listview_contacts_bg, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			// 为listview条目设置相应的数据
			holder.tv_contacts_name.setText(getItem(position).name);
			holder.tv_contacts_phone.setText(getItem(position).phone);
			holder.ib_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// 点击了某个条目的删除按钮，删除该条目
					if (editTextId == R.id.et_manager_contact) {

						mContactsForManager.remove(position);
					} else if (editTextId == R.id.et_reserver_contact) {

						mContactsForReserver.remove(position);
					}
					// 删除之后通知更新数据
					notifyDataSetChanged();
					// 如果数据全部删光则关闭popupwindow
					if (editTextId == R.id.et_manager_contact) {

						if (mContactsForManager.size() == 0) {

							popupWindow.dismiss();
						}
					} else if (editTextId == R.id.et_reserver_contact) {

						if (mContactsForReserver.size() == 0) {

							popupWindow.dismiss();
						}
					}
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {

		@Bind(R.id.tv_contacts_name)
		TextView tv_contacts_name;
		@Bind(R.id.tv_contacts_phone)
		TextView tv_contacts_phone;
		@Bind(R.id.ib_delete)
		ImageButton ib_delete;

		ViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}

	public interface DataCallback {

		void postData(AVObject avObject, ArrayList<HashMap<String, String>> details);
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
