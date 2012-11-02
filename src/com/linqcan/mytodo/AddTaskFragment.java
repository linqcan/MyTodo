package com.linqcan.mytodo;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskFragment extends Fragment{
	
	public enum State{
		ADD,EDIT
	}
	
	public interface addTaskListener{
		public void onSaveTask(Task task);
		public void onCancel();
	}
	
	public interface editTaskListener{
		public void onUpdateTask(Task task);
	}
	
	private static State ViewState;
	private static long taskid;
	
	private static void putLogMessage(String msg){
		MainActivity.putLogMessage("Linqcan::AddTaskFragment", msg);
	}
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if(savedInstanceState != null){
        	putLogMessage( "onCreateView Saved Instance exists");
        }
        return inflater.inflate(R.layout.add_task, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	Long id = null;
    	
    	if(getActivity() instanceof EditTaskActivity){
      		Intent intent = getActivity().getIntent();
    		id = intent.getLongExtra("id", -1);
    		if(MainActivity.hasActivitySavedInstanceStateBundle()){
				putLogMessage( "EditTaskActivity Saved instance exists!");
    			populateFromSaved();
    		}
			else if (id > -1){
				taskid = id;
				populateFromDatabase(taskid);
			}
    		ViewState = State.EDIT;
    	}
    	
    	
    	else if(getActivity() instanceof AddTaskActivity){
    		if(MainActivity.hasActivitySavedInstanceStateBundle()){
				putLogMessage( "AddTaskActivity Saved instance exists!");
    			populateFromSaved();
    		}
    		ViewState = State.ADD;
    	}
    	
    	else if(getActivity() instanceof MainActivity){ //DualPane
    		Bundle args = getArguments();
    		if(args != null){
				putLogMessage( "MainActivity Arguments are supplied");
				int mode = args.getInt("mode");
    			id = args.getLong("id", -1);
    			
  				switch(mode){
					case R.integer.ADD:
						ViewState = State.ADD;
						break;
					case R.integer.EDIT:
						ViewState = State.EDIT;
						break;
  				}
  				
    			if(MainActivity.hasActivitySavedInstanceStateBundle()){
    				putLogMessage( "MainActivity Saved instance exists!");
    				populateFromSaved();
    				return;
    			}
    			else if(id > -1 && ViewState == State.EDIT){
    				//Edit mode
    				taskid = id;
    				populateFromDatabase(taskid);
    				return;
    			}
    		}
    		else{
    			putLogMessage( "No arguments passed!");
    		}
    	}
    }
        
    private void populateFromDatabase(long id){
    	putLogMessage("Populating from database");
    	TaskProvider tp = TaskProvider.getInstance(getActivity());
    	Task task = tp.getTaskById(id);
    	EditText title = (EditText) getView().findViewById(R.id.add_task_title);
    	EditText desc = (EditText) getView().findViewById(R.id.add_task_description);
    	title.setText(task.getTitle());
    	desc.setText(task.getDescription());
    }
    
    private void populateFromSaved(){
    	putLogMessage( "Populating from saved instance");
    	Bundle savedInstanceState = MainActivity.popActivityInstanceStateBundle();
    	putLogMessage("Bundle: "+ savedInstanceState.toString());
    	EditText title = (EditText) getView().findViewById(R.id.add_task_title);
    	EditText desc = (EditText) getView().findViewById(R.id.add_task_description);
    	String titletext = savedInstanceState.getString("title");
    	String desctext = savedInstanceState.getString("description");
    	title.setText(titletext);
    	desc.setText(desctext);
    	putLogMessage( "Retrieved: "+ titletext + " " + desctext);
    }
 
    public AddTaskFragment.State getState(){
    	return ViewState;
    }
    
    public long getCurrentTaskId(){
    	return taskid;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.activity_add_task, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	switch(item.getItemId()){
    	case android.R.id.home:
    		break;
    	case R.id.add_task_menu_cancel:
    		putLogMessage("Cancel pressed!");
    		addTaskListener listener;
    		try{
    			listener = (addTaskListener) getActivity();
    		}
    		catch(ClassCastException e){
    			throw new ClassCastException(getActivity().toString() + " must implement addTaskListener");
    		}
    		listener.onCancel();
    		return true;

    	case R.id.add_task_menu_save:
    		putLogMessage("Save pressed!");
    		EditText title = (EditText) getView().findViewById(R.id.add_task_title);
    		EditText desc = (EditText) getView().findViewById(R.id.add_task_description);
    		
    		if(title.getText().toString().isEmpty()){
    			Toast.makeText(getActivity(), "Cannot add, please enter a title", Toast.LENGTH_SHORT).show();
    			return false;
    		}
    		
    		Task task = new Task();
    		task.setId(taskid);
    		task.setTitle(title.getText().toString());
    		task.setDescription(desc.getText().toString());
    		
    		if(State.ADD == ViewState){
    			addTaskListener addlistener;
        		try{
        			addlistener = (addTaskListener) getActivity();
        		}
        		catch(ClassCastException e){
        			throw new ClassCastException(getActivity().toString() + " must implement addTaskListener");
        		}
    			addlistener.onSaveTask(task);
    			Log.d(this.toString(), "Has called onSaveTask");
    			return true;
    		}
    		else if(State.EDIT == ViewState){
      			editTaskListener editlistener;
        		try{
        			editlistener = (editTaskListener) getActivity();
        		}
        		catch(ClassCastException e){
        			throw new ClassCastException(getActivity().toString() + " must implement editTaskListener");
        		}
    			editlistener.onUpdateTask(task);
    			Log.d(this.toString(),"Has called onUpdateTask");
    			return true;
    		}
    		break;    	
    	}
        return super.onOptionsItemSelected(item);
    }

}
