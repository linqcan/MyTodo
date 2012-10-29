package com.linqcan.mytodo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private ArrayList<String> itemlist;
	private ArrayAdapter<String> m_adapter;
	private TaskProvider tp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tp = TaskProvider.getInstance(this);
        Cursor mCursor = tp.getAllAsCursor();
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(
        		this, 
        		android.R.layout.simple_list_item_1, 
        		mCursor, 
        		new String[] {"title"}, 
        		new int[] {android.R.id.text1}, 
        		0);
        setListAdapter(mAdapter);
        getActionBar().setHomeButtonEnabled(false);
                
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case R.id.add_task:
    			Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
    			intent.putExtra("action", 0);
    			startActivity(intent);
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
   
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Intent intent = new Intent(getApplicationContext(),ViewTaskActivity.class);
    	intent.putExtra("id", id);
    	startActivity(intent);
    	
    }
}
