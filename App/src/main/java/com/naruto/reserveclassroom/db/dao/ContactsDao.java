package com.naruto.reserveclassroom.db.dao;
/*
 * Created by NARUTO on 2016/11/12.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.naruto.reserveclassroom.db.ContactsOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactsDao {
	private ContactsOpenHelper contactsOpenHelper;

	// 1.私有化构造方法
	private ContactsDao(Context ctx) {

		// 创建数据库以及其表结构
		contactsOpenHelper = new ContactsOpenHelper(ctx);
	}

	// 2.声明一个当前类的对象
	private static ContactsDao sContactsDao = null;

	// 3.提供一个静态方法，如果当前类的对象为空，创建一个新的
	public static ContactsDao getInstance(Context ctx) {

		if (sContactsDao == null) {

			sContactsDao = new ContactsDao(ctx);
		}

		return sContactsDao;
	}

	/**
	 * 增加联系人的记录
	 *
	 * @param contactsType
	 *            联系人类型
	 * @param name
	 *            联系人名
	 * @param phone
	 *            联系人手机号
	 */
	public void insert(int contactsType, String name, String phone) {

		// 开启数据库
		SQLiteDatabase db = contactsOpenHelper.getWritableDatabase();

		// 创建一个contentvalue对象
		ContentValues values = new ContentValues();
		// 向创建的contentvalue对象中添加键值对
		values.put("contactsType", contactsType);
		values.put("name", name);
		values.put("phone", phone);
		// 执行插入数据的操作
		db.insert("contacts", null, values);

		db.close();
	}

	/**
	 * 根据人名删除一条联系人的记录
	 *
	 * @param name
	 *            所需删除的联系人的人名
	 */
	public void delete(String name) {

		// 开启数据库
		SQLiteDatabase db = contactsOpenHelper.getWritableDatabase();

		// 根据人名删除联系人的记录
		db.delete("contacts", "name = ?", new String[] {name});

		db.close();
	}

	/**
	 * 根据联系人的人名更新联系人数据
	 *
	 * @param contactsType
	 *            更新后的联系人的类型
	 * @param name
	 *            所需更新的联系人的人名
	 * @param phone
	 *            更新后的联系人的手机号
	 */
	public void update(int contactsType, String name, String phone) {

		// 开启数据库
		SQLiteDatabase db = contactsOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("contactsType", contactsType);
		values.put("phone", phone);
		db.update("contacts", values, "name = ?", new String[] {name});

		db.close();
	}

	/**
	 * 获取数据库中某一种类型的联系人的数据
	 * 
	 * @param contactsType
	 *            所需联系人的类型
	 * @return 查询类型的联系人数据
	 */
	public List<Contacts> getContacts(int contactsType) {

		// 开启数据库
		SQLiteDatabase db = contactsOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from contacts;", null);
		List<Contacts> contactsLists = new ArrayList<>();
		while (cursor.moveToNext()) {

			Contacts contact = new Contacts();
			contact._id = cursor.getInt(0);
			contact.contactsType = cursor.getInt(1);
			contact.name = cursor.getString(2);
			contact.phone = cursor.getString(3);
			if (contact.contactsType == contactsType) {

				contactsLists.add(contact);
			}
		}

		db.close();
		cursor.close();
		return contactsLists;
	}

	public class Contacts {

		public int _id;
		public int contactsType;
		public String name;
		public String phone;
	}
}
