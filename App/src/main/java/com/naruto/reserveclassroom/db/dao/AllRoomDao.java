package com.naruto.reserveclassroom.db.dao;
/*
 * Created by NARUTO on 2016/11/10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.naruto.reserveclassroom.db.AllRoomOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AllRoomDao {
	private AllRoomOpenHelper allRoomOpenHelper;

	// 1.私有化构造方法
	private AllRoomDao(Context ctx) {

		// 创建数据库以及其表结构
		allRoomOpenHelper = new AllRoomOpenHelper(ctx);
	}

	// 2.声明一个当前类的对象
	private static AllRoomDao sAllRoomDao = null;

	// 3.提供一个静态方法，如果当前类的对象为空，创建一个新的
	public static AllRoomDao getInstance(Context ctx) {

		if (sAllRoomDao == null) {

			sAllRoomDao = new AllRoomDao(ctx);
		}

		return sAllRoomDao;
	}

	/**
	 * 增加一条教室信息的记录
	 *
	 * @param roomNo
	 *            插入的教室的教室号
	 */
	public void insert(String roomNo, int peopleAvai, int reserveAvai) {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("buildingNo", roomNo);
		values.put("peopleavai", peopleAvai);
		values.put("reserveavai", reserveAvai);
		db.insert("allrooms", null, values);

		db.close();
	}

	/**
	 * 根据教室号删除一条教室的记录
	 *
	 * @param roomNo
	 *            需要删除记录的教室号
	 */
	public void delete(String roomNo) {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();

		db.delete("allrooms", "roomNo = ?", new String[] {roomNo});

		db.close();
	}

	/**
	 * 根据教室号变更教室的信息
	 * 
	 * @param roomNo
	 *            所需更新数据的教室号
	 * @param peopleAvai
	 *            更新后教室所能容纳的人数
	 * @param reserveAvai
	 *            更新后教室是否可借
	 */
	public void update(String roomNo, int peopleAvai, int reserveAvai) {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("peopleavai", peopleAvai);
		values.put("reserveavai", reserveAvai);
		db.update("allrooms", values, "roomNo = ?", new String[] {roomNo});

		db.close();
	}

	/**
	 * 查询数据库中所有被被锁定的app
	 *
	 * @return 查询到的所有被锁定的app的集合
	 */
	public List<String> findAll() {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("allrooms", new String[] {"buildingNo"}, null, null, null, null,
						"_id");
		List<String> allroomLists = new ArrayList<>();
		while (cursor.moveToNext()) {

			allroomLists.add(cursor.getString(0));
		}

		cursor.close();
		db.close();

		return allroomLists;
	}

	/**
	 * 每次查询20条数据
	 *
	 * @param index
	 *            查询的索引值
	 * @return 查询到的20条锁定app的集合
	 */
	public List<String> find(int index) {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery("select buildingNo from allrooms order by _id limit ?,20;",
						new String[] {index + ""});

		List<String> allRoomLists = new ArrayList<>();
		while (cursor.moveToNext()) {

			allRoomLists.add(cursor.getString(0));
		}

		cursor.close();
		db.close();

		return allRoomLists;
	}

	/**
	 * 获取父节点的数据
	 * 
	 * @return 父节点数据的数组
	 */
	public List<Group> getGroup() {

		// 父节点就四组数据A1-N,A1-S,A4-N,A4-S,直接写死
		List<Group> groupLists = new ArrayList<>();

		Group Group1 = new Group();
		// A1-N
		Group1.buildingNo = "A1-N";
		Group1.idx = "1";
		Group1.childList = getChild(Group1.buildingNo);
		groupLists.add(Group1);

		// A1-S
		Group Group2 = new Group();
		Group2.buildingNo = "A1-S";
		Group2.idx = "2";
		Group2.childList = getChild(Group2.buildingNo);
		groupLists.add(Group2);

		// A4-N
		Group Group3 = new Group();
		Group3.buildingNo = "A4-N";
		Group3.idx = "3";
		Group3.childList = getChild(Group3.buildingNo);
		groupLists.add(Group3);

		// A4-S
		Group Group4 = new Group();
		Group4.buildingNo = "A4-S";
		Group4.idx = "4";
		Group4.childList = getChild(Group4.buildingNo);
		groupLists.add(Group4);

		return groupLists;
	}

	/**
	 * 获取数据库中某一栋楼的所有教室数据
	 * 
	 * @param buildingNo
	 *            楼号
	 * @return 查询楼号的所有教室
	 */
	public List<Child> getChild(String buildingNo) {

		// 开启数据库
		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from allrooms;", null);
		List<Child> childLists = new ArrayList<>();
		while (cursor.moveToNext()) {

			Child child = new Child();
			child._id = cursor.getInt(0);
			child.roomNo = cursor.getString(1);
			child.peopleAvai = cursor.getInt(2);
			child.reserveAvai = cursor.getInt(3);
			if (child.roomNo.contains(buildingNo)) {

				childLists.add(child);
			}
		}

		db.close();
		cursor.close();
		return childLists;
	}

	public class Group {

		public String buildingNo;
		public String idx;
		public List<Child> childList;
	}

	public class Child {

		public int _id;
		public String roomNo;
		public int peopleAvai;
		public int reserveAvai;
	}

	/**
	 * 获取某一栋楼的可借用教室数和教室总数
	 * 
	 * @return 查询楼号的可借用教室数和教室总数
	 */
	public List<Integer> getCount(String buildingNo) {

		SQLiteDatabase db = allRoomOpenHelper.getWritableDatabase();
		int count = 0;
		int avaiCount = 0;
		List<Integer> counts = new ArrayList<>();
		Cursor cursor = db.rawQuery("select count(*) from allrooms;", null);
		if (cursor.moveToNext()) {

			String roomNo = cursor.getString(1);
			if (roomNo.contains(buildingNo)) {

				count ++;
				if (cursor.getInt(3) == 1) {

					avaiCount ++;
				}
			}
		}
		counts.add(avaiCount);
		counts.add(count);

		cursor.close();
		db.close();

		return counts;
	}
}
