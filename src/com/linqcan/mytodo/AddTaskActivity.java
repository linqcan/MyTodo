package com.linqcan.mytodo;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class AddTaskActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_add_task, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
    	switch(item.getItemId()){
    	case android.R.id.home:
    	case R.id.add_task_menu_cancel:
    		intent = new Intent(this,MainActivity.class);
    		startActivity(intent);
    		finish();
    		return true;

    	case R.id.add_task_menu_save:
    		EditText title = (EditText) findViewById(R.id.add_task_title);
    		EditText desc = (EditText) findViewById(R.id.add_task_description);
    		if(title.getText().toString().isEmpty() || desc.getText().toString().isEmpty()){
    			Toast.makeText(getApplicationContext(), "Empty fields!", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    		TaskProvider tp = new TaskProvider(getApplicationContext());
    		ContentValues cv = new ContentValues();
    		cv.put("title", title.getText().toString());
    		cv.put("description", desc.getText().toString());
    		boolean isinserted = tp.insert(cv);
    		if(!isinserted){
    			Toast.makeText(getApplicationContext(), "Could not insert data!", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    		else{
        		intent = new Intent(this,MainActivity.class);
        		startActivity(intent);
        		finish();
        		return true;
    		}
    	
    	}
        return super.onOptionsItemSelected(item);
    }

}
