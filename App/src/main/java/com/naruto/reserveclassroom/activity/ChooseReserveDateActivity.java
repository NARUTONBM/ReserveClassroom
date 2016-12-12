package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/13.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.utils.TimeFormatUtil;
import com.naruto.reserveclassroom.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ChooseReserveDateActivity extends AppCompatActivity {

	@Bind(R.id.ll_date_parent)
	LinearLayout ll_date_parent;
	private String mRoomNo;
	private Context mContext;
	private static final int FIRST_DAY = Calendar.MONDAY;
	// 创建一个用于存放两周内所有日期的集合
	private List<String> mDatesInTwoWeeksList = new ArrayList<>();
	private AVQuery<AVObject> mClassroom;
	private String mCurrentTimeStamp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_reserve_date);

		mContext = this;

		initData();
		initUI();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 拿到ReserveClassRoomActivity传过来的数据(申请的教室号)，并将之设置给ActionBar
		mRoomNo = getIntent().getStringExtra("roomNo");
		//获取位置索引值
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd EEEE");
		String currentDate = format.format(date);
		mCurrentTimeStamp = TimeFormatUtil.Date2TimeStamp(currentDate, "yyyy-MM-dd EEEE");
		// 得到两周内所有日期的条目信息，并将其放置在之前准备好的集合中
		DatesInTwoWeeks();
		// 获取classroom表
		mClassroom = new AVQuery<>("Classroom");
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {

		ButterKnife.bind(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(mRoomNo + "教室借用日期选择");

		addRoomInfo();
	}

	/**
	 * 添加一条教室信息的条目
	 */
	private void addRoomInfo() {

		for (int i = 0; i < mDatesInTwoWeeksList.size(); i ++) {

			String dateStr = mDatesInTwoWeeksList.get(i);
			String timeStamp = TimeFormatUtil.Date2TimeStamp(dateStr, "yyyy-MM-dd EEEE");
			if (timeStamp.compareTo(mCurrentTimeStamp) > 0) {

				addDateItem(dateStr, timeStamp);
			}
		}
	}

	/**
	 * 添加一条选择日期的条目
	 *
	 * @param timeStamp
	 *            当前条目日期的时间戳
	 * @param dateStr
	 *            当前条目的字符型日期
	 */
	private void addDateItem(final String dateStr, String timeStamp) {

		String[] split = dateStr.split(" ");
		String dateWeek = split[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.linearlayout_date_item, null);
		view.setLayoutParams(lp);
		ImageButton ib_item_ensure = (ImageButton) view.findViewById(R.id.ib_item_ensure);
		TextView tv_item_time = (TextView) view.findViewById(R.id.tv_item_time);
		TextView tv_item_date = (TextView) view.findViewById(R.id.tv_item_date);
		tv_item_date.setText(dateStr);
		if (dateWeek.equals("星期六") || dateWeek.equals("星期日")) {

			// 准备一个数组，放置全天时段
			ArrayList<String> wholeDayTimeList = new ArrayList<>();
			wholeDayTimeList.add("08:00");
			wholeDayTimeList.add("09:15");
			wholeDayTimeList.add("09:15");
			wholeDayTimeList.add("10:30");
			wholeDayTimeList.add("10:30");
			wholeDayTimeList.add("11:45");
			wholeDayTimeList.add("11:45");
			wholeDayTimeList.add("13:00");
			wholeDayTimeList.add("13:00");
			wholeDayTimeList.add("14:15");
			wholeDayTimeList.add("14:15");
			wholeDayTimeList.add("15:30");
			wholeDayTimeList.add("15:30");
			wholeDayTimeList.add("16:45");
			wholeDayTimeList.add("16:45");
			wholeDayTimeList.add("18:00");
			wholeDayTimeList.add("18:00");
			wholeDayTimeList.add("19:00");
			wholeDayTimeList.add("19:00");
			wholeDayTimeList.add("20:00");
			wholeDayTimeList.add("20:00");
			wholeDayTimeList.add("22:00");
			// 准备一个数组，放置全天时段整形数字
			ArrayList<Integer> wholeDayTSList = new ArrayList<>();
			wholeDayTSList.add(28800);
			wholeDayTSList.add(33300);
			wholeDayTSList.add(33300);
			wholeDayTSList.add(37800);
			wholeDayTSList.add(37800);
			wholeDayTSList.add(42300);
			wholeDayTSList.add(42300);
			wholeDayTSList.add(46800);
			wholeDayTSList.add(46800);
			wholeDayTSList.add(51300);
			wholeDayTSList.add(51300);
			wholeDayTSList.add(55800);
			wholeDayTSList.add(55800);
			wholeDayTSList.add(60300);
			wholeDayTSList.add(60300);
			wholeDayTSList.add(64800);
			wholeDayTSList.add(64800);
			wholeDayTSList.add(68400);
			wholeDayTSList.add(68400);
			wholeDayTSList.add(72000);
			wholeDayTSList.add(72000);
			wholeDayTSList.add(79200);
			DealForbiddenItem(wholeDayTimeList, wholeDayTSList, tv_item_time, timeStamp, ib_item_ensure, dateStr);
		} else {

			// 准备一个数组，放置全天时段
			ArrayList<String> wholeDayTimeList = new ArrayList<>();
			wholeDayTimeList.add("18:00");
			wholeDayTimeList.add("19:00");
			wholeDayTimeList.add("19:00");
			wholeDayTimeList.add("20:00");
			wholeDayTimeList.add("20:00");
			wholeDayTimeList.add("22:00");
			// 准备一个数组，放置全天时段整形数字
			ArrayList<Integer> wholeDayTSList = new ArrayList<>();
			wholeDayTSList.add(64800);
			wholeDayTSList.add(68400);
			wholeDayTSList.add(68400);
			wholeDayTSList.add(72000);
			wholeDayTSList.add(72000);
			wholeDayTSList.add(79200);
			DealForbiddenItem(wholeDayTimeList, wholeDayTSList, tv_item_time, timeStamp, ib_item_ensure, dateStr);
		}

		ll_date_parent.addView(view);
	}

	/**
	 * 获取两周内所有的日期
	 */
	private void DatesInTwoWeeks() {

		Calendar calendar = Calendar.getInstance();
		setToFirstDay(calendar);
		for (int i = 0; i < 14; i ++) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEEE");
			mDatesInTwoWeeksList.add(dateFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
	}

	/**
	 * 设置第一天
	 *
	 * @param calendar
	 *            日历
	 */
	private static void setToFirstDay(Calendar calendar) {

		while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {

			calendar.add(Calendar.DATE, -1);
		}
	}

	/**
	 * 根据教室id去查询该教室当前所有的借用信息
	 * 
	 * @param classroom
	 *            classroom表
	 */
	private void QueryForbiddenTime(AVQuery<AVObject> classroom, final DataCallback callback) {

		// 准备一个数组，存放classroom标下所有的数据
		final List<AVObject> objectList = new ArrayList<>();
		classroom.whereContains("roomNo", mRoomNo);
		classroom.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {

				if (e == null) {

					objectList.addAll(list);
					callback.postData(objectList);
				} else {

					// TODO: 2016-12-06
					// 在此处加入网络不畅，重新刷新数据的对话框。对应的错误代码为：0，错误原因：timeout/UnknownHostException
					System.out.println("错误代码：" + e.getCode());
					System.out.println("错误原因：" + e.getCause());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 根据传入条目的日期，判断当天是否有被借用的时间段
	 * 
	 * @param wholeDayTimeList
	 *            根据是否是周末，不同的全天时段的字符型Java时间集合
	 * @param wholeDayTSList
	 *            根据是否是周末，不同的全天时段的字符型时间戳集合
	 * @param tv_item_time
	 *            条目对象
	 * @param timeStamp
	 *            条目日期的时间戳
	 * @param ib_item_ensure
	 *            条目对应的ib
	 * @param dateStr
	 *            条目的字符型Java时间
	 */
	private void DealForbiddenItem(final List<String> wholeDayTimeList, final ArrayList<Integer> wholeDayTSList, final TextView tv_item_time,
					final String timeStamp, final ImageButton ib_item_ensure, final String dateStr) {

		QueryForbiddenTime(mClassroom, new DataCallback() {

			@Override
			public void postData(List<AVObject> objectList) {

				// 准备放置开始和结束时间的坐标的集合
				List<Integer> mEndPositionList = new ArrayList<>();
				List<Integer> mStartPositionList= new ArrayList<>();
				// 获取该教室的借用信息
				List borrowDetails = (List) objectList.get(0).get("borrowDetails");
				// 遍历每一条借用记录
				for (int i = 0; i < borrowDetails.size(); i ++) {

					// 获取当前记录的日期、开始时间和结束时间的时间戳
					HashMap detail = (HashMap) borrowDetails.get(i);
					String startT = detail.get("startT").toString();
					String endT = detail.get("endT").toString();
					String itemDate = startT.split(" ")[0];
					String itemDateTS = TimeFormatUtil.Date2TimeStamp(itemDate, "yyyy-MM-dd");
					int startTS = Integer.parseInt(TimeFormatUtil.Date2TimeStamp(startT, "yyyy-MM-dd HH:mm:ss")) - Integer.parseInt(itemDateTS);
					int endTS = Integer.parseInt(TimeFormatUtil.Date2TimeStamp(endT, "yyyy-MM-dd HH:mm:ss")) - Integer.parseInt(itemDateTS);
					if (timeStamp.equals(itemDateTS)) {

						// 对条目日期时间戳和记录的日期时间戳相同，做时间对比查询
						// 找到这条记录开始时间的坐标
						for (int j = 0; j < wholeDayTSList.size() / 2; j++) {

							if (startTS == wholeDayTSList.get(j * 2)) {

								mStartPositionList.add(j * 2);
								break;
							} else if (startTS < wholeDayTSList.get(j * 2)) {

								mStartPositionList.add((j - 1) * 2);
								break;
							}
						}
						// 找到这条记录结束时间的坐标
						for (int k = 0; k < wholeDayTSList.size() / 2; k++) {

							if (endTS <= wholeDayTSList.get(k * 2 + 1)) {

								mEndPositionList.add(k * 2 + 1);
								break;
							}
						}
					} else {

						continue;
					}
				}
				if (!mStartPositionList.isEmpty()&&!mEndPositionList.isEmpty()) {

					// 对开始和结束时间对比查询之后做从小到大的排序
					List<Integer> newStartPositionList = roleTheList(mStartPositionList);
					List<Integer> newEndPositionList = roleTheList(mEndPositionList);
					// 对排序之后的开始和结束时间坐标做查重处理，连续的两条合并为一条
					for (int x = 1; x < newStartPositionList.size(); x ++) {

						if (newStartPositionList.get(x).equals(newEndPositionList.get(x - 1))) {

							newStartPositionList.remove(x);
							newEndPositionList.remove(x - 1);
						}
					}
					// 用合并之后的开始和结束坐标，删除被借用的时段的Java时间和时间戳
					for (int y = newStartPositionList.size(); y > 0; y --) {

						int position = newStartPositionList.get(y - 1);
						for (int z = 0; z <= (newEndPositionList.get(y - 1) - newStartPositionList.get(y - 1)); z ++) {

							if (wholeDayTimeList.size() != 0) {

								wholeDayTimeList.remove(position);
								wholeDayTSList.remove(position);
							}
						}
					}
				}
				// 对剩下的可借时段的Java时间和时间戳做查重处理，连续的两条合并为一条
				int c = 1;
				for (int b = 0; b <= wholeDayTimeList.size() / 2 + 8; b ++) {

					if (c <= wholeDayTimeList.size() - 3) {

						if (wholeDayTimeList.get(c).equals(wholeDayTimeList.get(++ c))) {

							wholeDayTimeList.remove(-- c);
							wholeDayTSList.remove(c);
							wholeDayTimeList.remove(c);
							wholeDayTSList.remove(c);
						} else {

							c ++;
						}
					} else {

						break;
					}
				}
				// 对合并之后的时间段做最终结果展示。如果没有可借时段，就显示“该日期无可借时段”
				if (wholeDayTimeList.size() != 0) {

					StringBuilder builder = new StringBuilder();
					builder.append("可借时段:");
					for (int a = 0; a < wholeDayTimeList.size(); a ++) {

						if (a % 2 == 0) {

							builder.append(wholeDayTimeList.get(a)).append("-");
						} else {

							builder.append(wholeDayTimeList.get(a)).append(";");
						}
					}
					final String avaiTime = builder.toString();
					tv_item_time.setText(avaiTime);
					ib_item_ensure.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							// 创建一个intent对象
							Intent intent = new Intent(mContext, PostReservationActivity.class);
							// 传递数据
							intent.putExtra("roomNo", mRoomNo);
							intent.putExtra("date", dateStr);
							intent.putIntegerArrayListExtra("avaiTime", wholeDayTSList);
							// 启动跳转
							startActivity(intent);
						}
					});
				} else {

					tv_item_time.setText("该日期无可借时段");
					ib_item_ensure.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							ToastUtil.showShort(mContext, "该日期无可借时段，请选择其他日期，或选择其他教室");
						}
					});
				}
			}
		});
	}

	/**
	 * 对存储坐标的集合做从小到大的排序
	 * 
	 * @param positionList
	 *            需要排序的集合
	 * @return 返回排序之后的集合
	 */
	private List<Integer> roleTheList(List<Integer> positionList) {

		int temp = 0;
		// 从小到大排序
		if (positionList.size() > 1) {

			for (int i = 0; i < positionList.size(); i ++) {

				for (int j = 0; j < positionList.size() - i-1; j ++) {

					if (positionList.get(j) > positionList.get(j + 1)) {

						temp = positionList.get(j);
						positionList.set(j, positionList.get(j + 1));
						positionList.set(j + 1, temp);
					}
				}
			}
		}

		return positionList;
	}

	public interface DataCallback {

		void postData(List<AVObject> forbiddenTime);
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
		//重新获取焦点时，刷新数据
		ll_date_parent.removeAllViews();
		addRoomInfo();
	}
}
