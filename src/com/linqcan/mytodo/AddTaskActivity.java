package com.linqcan.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.linqcan.mytodo.AddTaskFragment.addTaskListener;

public class AddTaskActivity extends Activity implements addTaskListener{
	
	private static void putLogMessage(String msg){
		MainActivity.putLogMessage("Linqcan::AddTaskActivity", msg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		putLogMessage("Call orientation validation");
		MainActivity.validateOrientation(getResources());
		if(MainActivity.isDualPane()){
			if(savedInstanceState != null && savedInstanceState.getBoolean("mytodo") == true){
				MainActivity.setActivityInstanceStateBundle(savedInstanceState);
			}
			finish();
			return;
		}
		setContentView(R.layout.add_task_activity);
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		EditText fieldTitle = (EditText) findViewById(R.id.add_task_title);
		EditText fieldDesc =  (EditText) findViewById(R.id.add_task_description);
		outState.putString("title", fieldTitle.getText().toString());
		outState.putString("description", fieldDesc.getText().toString());
		outState.putBoolean("mytodo", true);
	}	

	public void onSaveTask(Task task) {
		putLogMessage("onSaveTask");
		Intent data = new Intent();
		data.putExtra("task", task);
		setResult(0, data);
		finish();
	}

	public void onCancel() {
		setResult(1);
		finish();
	}
}
