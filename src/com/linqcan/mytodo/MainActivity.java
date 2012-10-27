package com.linqcan.mytodo;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private ArrayList<String> itemlist;
	private ArrayAdapter<String> m_adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemlist = new ArrayList<String>();
        for(int i = 0; i < 4; ++i){
        	itemlist.add("Item " + Integer.toString(i));
        }
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemlist);
        setListAdapter(m_adapter);
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
    			startActivity(intent);
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
   
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Toast clicktoast = Toast.makeText(this, "Clicked " + Integer.toString(position), Toast.LENGTH_SHORT);
		clicktoast.show();
    }
}
