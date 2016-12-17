package com.naruto.reserveclassroom.activity;
/*
 * Created by NARUTO on 2016/11/9.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.naruto.reserveclassroom.R;
import com.naruto.reserveclassroom.db.dao.AllRoomDao;
import com.naruto.reserveclassroom.db.dao.AllRoomDao.Child;
import com.naruto.reserveclassroom.db.dao.AllRoomDao.Group;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReserveClassRoomActivity extends AppCompatActivity {

	@Bind(R.id.elv_choose_room)
	ExpandableListView elv_choose_room;
	private Context mContext;
	private List<AllRoomDao.Group> mGroup;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_classroom);

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
		getSupportActionBar().setTitle("教室预约");
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		//获取数据库连接，并获取数据
		AllRoomDao allRoomDao = AllRoomDao.getInstance(mContext);
		mGroup = allRoomDao.getGroup();
		//创建数据适配器
		final MyAdapter mAdapter = new MyAdapter();
		elv_choose_room.setAdapter(mAdapter);
		// elv孩子条目的点击事件
		elv_choose_room.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
							int childPosition, long id) {

				// System.out.println("孩子节点被点击了！");
				Child childBody = mAdapter.getChild(groupPosition, childPosition);
				Intent intent = new Intent(mContext, ChooseReserveDateActivity.class);
				intent.putExtra("roomNo", childBody.roomNo);
				System.out.println(parent.getChildCount());
				startActivity(intent);

				return true;
			}
		});
	}

	private class MyAdapter extends BaseExpandableListAdapter {
		@Override
		public int getGroupCount() {

			return mGroup.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {

			return mGroup.get(groupPosition).childList.size();
		}

		@Override
		public Group getGroup(int groupPosition) {

			return mGroup.get(groupPosition);
		}

		@Override
		public Child getChild(int groupPosition, int childPosition) {

			return mGroup.get(groupPosition).childList.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
						ViewGroup parent) {

			TextView textView = new TextView(mContext);
			textView.setText(String.format("                       %s",
							getGroup(groupPosition).buildingNo));
			textView.setTextSize(24);
			textView.setPadding(10, 10, 10, 10);
			textView.setTextColor(Color.GREEN);

			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
						View convertView, ViewGroup parent) {

			// 复用convertview
			ViewHolder holder;
			if (convertView == null) {

				// 将布局文件转化成view付给convertView
				convertView = View.inflate(mContext, R.layout.elv_roomdetail_child_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			Child childBody = getChild(groupPosition, childPosition);
			holder.tv_roomNo.setText(childBody.roomNo);
			holder.tv_people_avai.setText(String.valueOf("可容纳" + childBody.peopleAvai + "人"));

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
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

	static class ViewHolder {

		@Bind(R.id.tv_roomNo)
		TextView tv_roomNo;
		@Bind(R.id.tv_people_avai)
		TextView tv_people_avai;

		ViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}
}
