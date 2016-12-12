package com.naruto.reserveclassroom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by NARUTO on 2016/11/10.
 */

public class AllRoomOpenHelper extends SQLiteOpenHelper {
	public AllRoomOpenHelper(Context context) {

		super(context, "allclassrooms.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 创建数据库中的表
		db.execSQL("create table allrooms (_id integer primary key, roomNo varchar(10) not null, peopleavai integer not null , reserveavai integer not null );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}