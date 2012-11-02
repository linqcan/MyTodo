package com.linqcan.mytodo;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewTaskFragment extends Fragment {
	
	private static long mTaskid;
	private static boolean mIsShowingTask = false;
	
	private static void putLogMessage(String msg){
		MainActivity.putLogMessage("Linqcan::ViewTaskFragment", msg);
	}

	public interface viewTaskListener{
		public void deleteTask(long taskid);
		public void editTask(long taskid);
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.view_task, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof MainActivity){ // Landscape
			putLogMessage("onActivityCreated MainActivity");
			Bundle args = getArguments();
			if(args != null){ //First call in landscape has no arguments
				putLogMessage("MainActivity arguments passed");
				putLogMessage(args.toString());
				mTaskid = args.getLong("id");
				setTaskInfo();
				return;
			}
		}
		else if(getActivity() instanceof ViewTaskActivity){ //Portrait
			putLogMessage("onActivityCreated ViewTaskActivity");
			ViewTaskActivity vta = (ViewTaskActivity) getActivity();
			mTaskid = vta.getTaskid();
			if(mTaskid != -1){
				setTaskInfo();				
				return;
			}
		}
		showEmptyLabel(true);
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		putLogMessage("onCreateOptionsMenu");
		if(mIsShowingTask){
			inflater.inflate(R.menu.activity_view_task, menu);
		}
    	//super.onCreateOptionsMenu(menu, inflater);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewTaskListener listener = null;
    	switch (item.getItemId()) {
            case R.id.view_task_delete:
            	if(!mIsShowingTask){
            		return false;
            	}
            	try{
            		listener = (viewTaskListener) getActivity();
            	}
            	catch(ClassCastException e){
            		return false;
            	}
            	listener.deleteTask(mTaskid);
            	return true;
            case R.id.view_task_edit:
            	if(!mIsShowingTask){
            		return false;
            	}
             	try{
            		listener = (viewTaskListener) getActivity();
            	}
            	catch(ClassCastException e){
            		return false;
            	}
             	putLogMessage("Edit task id=" + Long.toString(mTaskid));
            	listener.editTask(mTaskid);
            	return true;
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
				
	private void setTaskInfo(){
		TaskProvider mTaskProvider = TaskProvider.getInstance(getActivity());
        Task task = mTaskProvider.getTaskById(mTaskid);
        if(task != null && getView() != null){
        	showEmptyLabel(false);
        	TextView title = (TextView) getView().findViewById(R.id.view_task_title);
        	TextView desc = (TextView) getView().findViewById(R.id.view_task_description);
        	title.setText(task.getTitle());
        	desc.setText(task.getDescription());
        }
        else{
        	showEmptyLabel(true);
        }
	}
	
	private void showEmptyLabel(boolean hide){
		if(hide){
			getView().findViewById(R.id.view_task_title_label).setVisibility(View.INVISIBLE);
			getView().findViewById(R.id.view_task_description_label).setVisibility(View.INVISIBLE);
			getView().findViewById(R.id.view_task_empty_label).setVisibility(View.VISIBLE);
			mIsShowingTask = false;
		}
		else{
			getView().findViewById(R.id.view_task_title_label).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.view_task_description_label).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.view_task_empty_label).setVisibility(View.GONE);
			mIsShowingTask = true;
		}
	}
}
