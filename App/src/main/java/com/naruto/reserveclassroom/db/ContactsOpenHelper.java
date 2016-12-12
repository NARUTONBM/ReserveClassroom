package com.naruto.reserveclassroom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by NARUTO on 2016/11/12.
 */

public class ContactsOpenHelper extends SQLiteOpenHelper {
	public ContactsOpenHelper(Context context) {

		super(context, "contacts.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 创建数据库中的表
		db.execSQL("create table contacts (_id integer primary key, contactstype integer not null, name varchar(10) not null , phone varchar(20) not null );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}