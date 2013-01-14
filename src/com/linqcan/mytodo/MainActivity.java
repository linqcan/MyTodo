package com.linqcan.mytodo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.linqcan.mytodo.AddTaskFragment.addTaskListener;
import com.linqcan.mytodo.AddTaskFragment.editTaskListener;
import com.linqcan.mytodo.DeleteDialogFragment.DeleteDialogListener;
import com.linqcan.mytodo.TaskListFragment.taskListListener;
import com.linqcan.mytodo.ViewTaskFragment.viewTaskListener;

public class MainActivity extends Activity 
							implements DeleteDialogListener, 
											addTaskListener,
											viewTaskListener,
											taskListListener,
											editTaskListener{

	private enum ActivityState{
		VIEW, ADD, EDIT
	}
	
	private static boolean mDualPane;
	private static boolean mOldDualPane;
	private static long mActiveTaskId = -1;
	private static String mLastFragment = null;
	private static String ACTIVITY_TAG = "Linqcan::MainActivity";
	private static ActivityState mActivityState = ActivityState.VIEW;
	private static Bundle mActivitySavedInstanceState = null;
	private static final String viewTaskTag = "viewTask";
	private static final String addTaskTag = "addTask";
	private static final String editTaskTag = "editTask";
	private static final boolean DEBUG_ON = true;
	
	private static void putLogMessage(String msg){
		putLogMessage(ACTIVITY_TAG, msg);
	}
	
	public static void putLogMessage(String tag, String msg){
		if(DEBUG_ON){
			Log.d(tag,msg);
		}
	}
	
//	private static void putToast(Context context,String msg){
//		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//	}
//	
//	private void localPutToast(String msg){
//		putToast(getApplicationContext(), msg);
//	}
//
	
	private void resetLastFragment(){
		putLogMessage("Resetting last fragment variable");
		mLastFragment = null;
	}
	
	private void resetActiveTaskId(){
		putLogMessage("Resetting active task id");
		mActiveTaskId = -1;
	}
	
	private void resetActivityState(){
		putLogMessage("Resetting activity state");
		mActivityState = ActivityState.VIEW;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		putLogMessage("Call orientation validation");
		validateOrientation(getResources());
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		if(savedInstanceState != null && savedInstanceState.getBoolean("mytodo") == true){
			mActivitySavedInstanceState = savedInstanceState;				
		}
		
		if(mDualPane){
			setContentView(R.layout.main_activity);
			if(mActivityState == ActivityState.ADD){
				addTask();
			}
			else if(mActivityState == ActivityState.EDIT){
				editTask(mActiveTaskId);
			}
			else if (mActivityState == ActivityState.VIEW){			
				viewTask(mActiveTaskId);
			}
			else{
				putLogMessage("Undefined ActivityState!");
			}
		}
		else{ //Single pane
			if(mLastFragment == viewTaskTag){
				resetLastFragment();
				viewTask(mActiveTaskId);
				return;
			}
			else if(mLastFragment == addTaskTag){
				resetLastFragment();
				addTask();
				return;
			}
			else if(mLastFragment == editTaskTag){
				resetLastFragment();
				editTask(mActiveTaskId);
				return;
			}
			else{
				showTaskList();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		if(mDualPane && (mActivityState == ActivityState.ADD || mActivityState == ActivityState.EDIT)){
			putLogMessage("Dual pane and Add/Edit. Saving instance.");
			EditText fieldTitle = (EditText) findViewById(R.id.add_task_title);
			EditText fieldDesc =  (EditText) findViewById(R.id.add_task_description);
			outState.putString("title", fieldTitle.getText().toString());
			outState.putString("description", fieldDesc.getText().toString());
			outState.putBoolean("mytodo", true);
			putLogMessage("Saved: " + fieldTitle.getText().toString() + " " + fieldDesc.getText().toString());
		}
		
		/*
		 * This is key to be able to handle rotation and decouple fragment from activity.
		 * Without this piece of code the fragment will be re-attached to MainActivity upon 
		 * rotation and onActivityCreated will be called for the fragment with MainActivity as
		 * activity. Removing the fragment upon tear-down ensures that onActivityCreated is called
		 * for the "right" activity (Add/Edit).
		 * Note that this must happen before onSaveInstanceState is called on super, side-effects
		 * unknown.
		 */
		if(mDualPane){
			Fragment fragment = getFragmentManager().findFragmentByTag(mLastFragment);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(fragment);
			ft.commit();
		}
		super.onSaveInstanceState(outState);
	}
	

	private void showTaskList(){
		putLogMessage("Setting default view (task list)");
		//Also, reset the cached task id
		resetActiveTaskId();
		setContentView(R.layout.main_activity);
	}
	
	public void viewTask(long taskid){
		putLogMessage("viewTask called with task id " + Long.toString(taskid));
		mActiveTaskId = taskid;
		mActivityState = ActivityState.VIEW;
		if(mDualPane){
			ViewTaskFragment fragment = new ViewTaskFragment();
			Bundle args = new Bundle();
			args.putLong("id", taskid);
			if(taskid >= 0){ // Do not set arguments if -1 (not initialized)
				fragment.setArguments(args);
			}
			switchFragment(fragment, viewTaskTag);
		}
		else{
			if(taskid <0){
				// No valid task to show, don't start activity
				putLogMessage("No valid task id");
				showTaskList();
				return;
			}
			Intent intent = new Intent(getApplicationContext(), ViewTaskActivity.class);
			intent.putExtra("id", taskid);
			startActivityForResult(intent,3);
		}
		
	}

	public void addTask(){
		mActivityState = ActivityState.ADD;
		if(mDualPane){
			AddTaskFragment fragment = new AddTaskFragment();
			Bundle args = new Bundle();
			args.putInt("mode", 0);
			fragment.setArguments(args);
			switchFragment(fragment, addTaskTag);
		}
		else{
			Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
			startActivityForResult(intent, 1);
		}
	}
	
	public void editTask(long taskid) {
		putLogMessage("editTask called with task id " + Long.toString(taskid));
		mActiveTaskId = taskid;
		mActivityState = ActivityState.EDIT;
		if(taskid < 0){
			return;
		}
		if(mDualPane){
			AddTaskFragment fragment = new AddTaskFragment();
			Bundle args = new Bundle();
			args.putLong("id", taskid);
			args.putInt("mode", 1);
			fragment.setArguments(args);
			switchFragment(fragment,editTaskTag);
		}
		else{
			Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
			intent.putExtra("id", taskid);
			startActivityForResult(intent, 2);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		putLogMessage("requestCode=" + Integer.toString(requestCode) + " resultCode=" + Integer.toString(resultCode));
		if(data != null){
			
			if(requestCode == 1 && resultCode == 0){ //From AddTask, save
				Task task = (Task) data.getSerializableExtra("task");
				onSaveTask(task);
			}
			
			else if(requestCode == 2 && resultCode == 0){ //From EditTask, update
				Task task = (Task) data.getSerializableExtra("task");
				onUpdateTask(task);
			}
			
			else if(requestCode == 3 && resultCode == 0){ //From ViewTask, update
				Task task = (Task) data.getSerializableExtra("task");
				onUpdateTask(task);
			}
			
			else if(requestCode == 3 && resultCode == 1){ //From ViewTask, delete
				deleteTask(data.getLongExtra("id", -1));
			}
		}
		else{ //Operations passing no data such as cancel
			if(requestCode == 1 && resultCode == 1){ //From AddTask, cancel
				putLogMessage("Received cancel from AddTask");
				onCancel();
			}			
		}
	}

	public void onClickDeleteOK() {
		//TODO		
	}

	private void switchFragment(Fragment fragment, String tag) {
		putLogMessage("Switching to fragment with tag " + tag);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.replace(R.id.fragment_container, fragment, tag);
		transaction.addToBackStack(tag);
		mLastFragment = tag;
		transaction.commit();		
	}
	
	private void popCurrentFragment(){
		putLogMessage("Removing last fragment from stack");
		if(getFragmentManager().getBackStackEntryCount() > 0){
			getFragmentManager().popBackStackImmediate();
		}
		//This ensures that the state machine is set
		// to the fragment at the top of the stack.
		int index = getFragmentManager().getBackStackEntryCount();
		BackStackEntry backStackEntry = getFragmentManager().getBackStackEntryAt(index-1);
		mLastFragment = backStackEntry.getName();
		if(mLastFragment == addTaskTag){
			mActivityState = ActivityState.ADD;
		}
		else if(mLastFragment == editTaskTag){
			mActivityState = ActivityState.EDIT;
		}
		else if(mLastFragment == viewTaskTag){
			mActivityState = ActivityState.VIEW;
		}
		putLogMessage("Top of stack is now fragment " + mLastFragment);
	}

	public void onSaveTask(Task task) {
		boolean resultOk = ViewHelper.addTask(task, getApplicationContext());
		if(resultOk){
			mActivityState = ActivityState.VIEW;
			finish();
			startActivity(getIntent());
		}
		else{
			Toast.makeText(getApplicationContext(), "Insertion failed!", Toast.LENGTH_SHORT).show();
		}
	}

	public void onCancel() {
		putLogMessage("onCancel called");
		if(mDualPane){
			putLogMessage("Landscape cancel");
			switch(mActivityState){
				case ADD:
				case EDIT:
					putLogMessage("Cancel from Add/Edit");
					popCurrentFragment();
					break;
				
				default:
					putLogMessage("Cancel from unknown state, do nothing.");
					break;
			}
		}
		else{
			putLogMessage("Portrait cancel");
			switch(mActivityState){
				case ADD:
					resetActivityState();
					break;
				default:
					putLogMessage("Unidentified call for cancel");
					break;
			}
		}
	}

	public void onUpdateTask(Task task) {
		boolean resultOk = ViewHelper.updateTask(task, getApplicationContext());
		if(resultOk){
			mActivityState = ActivityState.VIEW;
			finish();
			startActivity(getIntent());
		}
		else{
			Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
		}		
	}

	public void deleteTask(long taskid) {
		if(taskid < 0){
			return;
		}
		boolean resultOk = ViewHelper.deleteTask(taskid, getApplicationContext());
		if(resultOk){
			mActiveTaskId = -1;
			mActivityState = ActivityState.VIEW;
			finish();
			startActivity(getIntent());
		}
		else{
			Toast.makeText(getApplicationContext(), "Deletion failed!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public static void validateOrientation(Resources res){
		mOldDualPane = mDualPane;
		if(res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			mDualPane = false;
			putLogMessage("Portrait (No dual pane)");
		}
		else{
			mDualPane = true;
			putLogMessage("Landscape (Dual pane)");
		}
	}
	
	public static boolean isDualPane(){
		return mDualPane;
	}
	
	public static boolean rotationHasChanged(){
		return !(mOldDualPane == mDualPane);
	}
	
	public static void setActivityInstanceStateBundle(Bundle bundle){
		mActivitySavedInstanceState = bundle;
	}
	
	public static boolean hasActivitySavedInstanceStateBundle(){
		return mActivitySavedInstanceState != null;
	}
	
	public static Bundle popActivityInstanceStateBundle(){
		Bundle bundle = mActivitySavedInstanceState;
		//Always reset to null to ensure that
		//checks against null works.
		mActivitySavedInstanceState = null; 
		return bundle;
	}

}
