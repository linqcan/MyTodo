package com.linqcan.mytodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskActivity extends Activity {
	
	public enum State{
		ADD,EDIT
	}
	
	private State ViewState;
	private long taskid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Intent intent = getIntent();
        int action = intent.getIntExtra("action", 0);
        taskid = intent.getLongExtra("id", 0);
        if(action == 1){
        	ViewState = State.EDIT;
        	populateFields(taskid);
        }
        else{
        	ViewState = State.ADD;
        }
    }
    
    private void populateFields(long id){    	
    	TaskProvider tp = TaskProvider.getInstance(this);
    	Task task = tp.getTaskById(id);
    	EditText title = (EditText) findViewById(R.id.add_task_title);
    	EditText desc = (EditText) findViewById(R.id.add_task_description);
    	title.setText(task.getTitle());
    	desc.setText(task.getDescription());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_add_task, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
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
    		TaskProvider tp = TaskProvider.getInstance(this);
    		Task task = new Task();
    		task.setId(taskid);
    		task.setTitle(title.getText().toString());
    		task.setDescription(desc.getText().toString());
    		boolean resultOk = false;
    		if(State.ADD == ViewState){
    			resultOk = tp.insert(task);
    		}
    		else if(State.EDIT == ViewState){
    			resultOk = tp.update(task);
    		}
    		if(!resultOk){
    			Toast.makeText(getApplicationContext(), "Error: Could not insert data!", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    		else{
    			if(State.ADD == ViewState){
    				intent = new Intent(this,MainActivity.class);
    			}
    			else if(State.EDIT == ViewState){
    				intent = new Intent(this, ViewTaskActivity.class);
    				intent.putExtra("id", taskid);
    			}
        		startActivity(intent);
        		finish();
        		return true;
    		}
    	
    	}
        return super.onOptionsItemSelected(item);
    }

}
