package com.linqcan.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTaskActivity extends Activity {
	
	private static TaskProvider mTaskProvider;
	private long taskid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        taskid = intent.getLongExtra("id", 0);
        mTaskProvider = TaskProvider.getInstance(this);
        Log.i("ViewTaskActivity", "Fetching task");
        Task task = mTaskProvider.getTaskById(taskid);
        Log.i("ViewTaskActivity", "Fetched task");
        if(task != null){
        	TextView title = (TextView) findViewById(R.id.view_task_title);
        	TextView desc = (TextView) findViewById(R.id.view_task_description);
        	title.setText(task.getTitle());
        	desc.setText(task.getDescription());
        }
        else{
        	Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.view_task_delete:
            	DeleteDialog dd = new DeleteDialog(this);
            	dd.show();
            	return true;
            case R.id.view_task_edit:
            	Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
            	intent.putExtra("action", 1);
            	intent.putExtra("id", taskid);
            	startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void dodelete(){    	
    	if(mTaskProvider.delete(taskid)){
    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    		startActivity(intent);
    	}
    	else{
    		Toast.makeText(this, "An error occured while deleting task", Toast.LENGTH_SHORT).show();
    	}
    }

}
