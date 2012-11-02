package com.linqcan.mytodo;


import android.os.Bundle;
import android.app.ListFragment;
import android.graphics.Color;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TaskListFragment extends ListFragment {
	
	public interface taskListListener{
		public void viewTask(long taskid);
		public void addTask();
	}
	
	private static void putLogMessage(String msg){
		MainActivity.putLogMessage("Linqcan::TaskListFragment",msg);
	}
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        getActivity().getActionBar().setHomeButtonEnabled(false);
        setHasOptionsMenu(true);
    	return inflater.inflate(R.layout.list_tasks, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        SimpleCursorAdapter adapter = ViewHelper.getTaskListAdapter(getActivity());
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.activity_main, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
		case R.id.add_task:
			taskListListener listener = null;
			try{
				listener = (taskListListener) getActivity();
			}
			catch(ClassCastException e){ return super.onOptionsItemSelected(item);}
			listener.addTask();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	}
    }
   
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
    	taskListListener listener = null;
    	try{
    		listener = (taskListListener) getActivity();
    	}
    	catch(ClassCastException e){return;}
    	getListView().setItemChecked(position, true);
    	listener.viewTask(id);
    }
}
