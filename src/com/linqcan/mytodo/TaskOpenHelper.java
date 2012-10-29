package com.linqcan.mytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "mytodo";
	private static final String TASKS_TABLE_CREATE =
			"CREATE TABLE " + TaskProvider.TABLE_NAME + " (" + 
			"ID INTEGER PRIMARY KEY," +
			TaskProvider.KEY_TITLE + " TEXT, " +
			TaskProvider.KEY_DESCRIPTION +" TEXT);";
	
	public TaskOpenHelper(Context context){
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TASKS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
