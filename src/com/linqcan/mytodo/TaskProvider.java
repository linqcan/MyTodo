package com.linqcan.mytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskProvider{

	public static final String TABLE_NAME = "tasks";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
    public static final String[] TABLE_COLUMNS = {"ID as _id",KEY_TITLE, KEY_DESCRIPTION}; 
    private static TaskOpenHelper mOpenHelper;
    private static TaskProvider tp;
    
    public TaskProvider() {}
    
    public static TaskProvider getInstance(Context context) {
    	if(tp != null){
    		return tp;
    	}
    	else{
    		mOpenHelper = new TaskOpenHelper(context);
    		tp = new TaskProvider();
    		return tp;
    	}
    }    
     
    public boolean insert(Task task) {
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_TITLE, task.getTitle());
    	values.put(KEY_DESCRIPTION, task.getDescription());
    	long rowid = db.insert(TABLE_NAME, null, values);
    	if(rowid > 0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public boolean delete(long id){
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    	int rows = db.delete(TABLE_NAME, "ID="+Long.toString(id), null);
    	if(rows > 0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public boolean update(Task task){
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_TITLE, task.getTitle());
    	values.put(KEY_DESCRIPTION, task.getDescription());
    	int rows = db.update(TABLE_NAME, values, "ID="+Long.toString(task.getId()), null);
    	if(rows > 0){
    		return true;
    	}
    	else{
    		return false;
    	}
    }

    public Cursor getAllAsCursor() {
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();   	
    	return db.query(TABLE_NAME, TABLE_COLUMNS, null , null, null, null, "ID DESC", null);
    }
    
    public Task getTaskById(long id){
    	SQLiteDatabase db = mOpenHelper.getReadableDatabase();
    	Cursor result = db.query(TABLE_NAME, TABLE_COLUMNS, "_id="+Long.toString(id), null, null, null, null);
    	if(result.getCount() > 0){
    		result.moveToFirst();
    		Task task = new Task();
    		task.setId(id);
    		task.setTitle(result.getString(1));
    		task.setDescription(result.getString(2));
    		return task;
    	}
    	else{
    		return null;
    	}
    }

}
