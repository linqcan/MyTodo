package com.linqcan.mytodo;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ViewHelper {
	
	public static SimpleCursorAdapter getTaskListAdapter(Context context){
        TaskProvider tp = TaskProvider.getInstance(context);
        Cursor mCursor = tp.getAllAsCursor();
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(
        		context, 
        		android.R.layout.simple_list_item_1, 
        		mCursor, 
        		new String[] {"title"}, 
        		new int[] {android.R.id.text1}, 
        		0);
        return mAdapter;
	}
	
	public static boolean addTask(Task task, Context context){
		TaskProvider tp = TaskProvider.getInstance(context);
		return tp.insert(task);
		
	}
	
	public static boolean updateTask(Task task, Context context){
		TaskProvider tp = TaskProvider.getInstance(context);
		return tp.update(task);
	}
	
	public static boolean deleteTask(long taskid,Context context){
		TaskProvider tp = TaskProvider.getInstance(context);
		return tp.delete(taskid);
	}

}
