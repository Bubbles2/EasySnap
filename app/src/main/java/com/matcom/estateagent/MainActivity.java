package com.matcom.estateagent;



import java.util.ArrayList;

import android.R.bool;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.provider.ContactsContract;



public class MainActivity extends Activity implements OnItemClickListener{


	boolean advSearchSelected =false;
	public Cursor c;
	int contactGroup;
	public Cursor clearCursor;
	public static final String PREFS_NAME = "MyPrefsFile";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// This fixes problem of "Robot" icon being displayed when search option is selected instead of Logo
		//getActionBar().setIcon(R.drawable.ic_launcher);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
				
			    if (advSearchSelected == false) {
			    	// Get user preferences
			    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				    contactGroup = settings.getInt("contactGroup", 99);
			    	displayList(contactGroup);	
				}
			    else
			    {
			    	advSearchSelected =false;
			    	
			    }
				
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
				
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return MenuChoice(item);
	}
	
	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(this, SelectContactGroup.class);
			startActivity(intent);
				return true;
		case R.id.adv_sear:
			Intent intentSer = new Intent(this, ASearch.class);
			startActivity(intentSer);
				return true;
		case R.id.helpTxt:
			Intent intentHelp = new Intent(this, HelpActivity.class);
			startActivity(intentHelp);
				return true;
	} 
		return false;
		
	}

	public void displayList(int selectedGroup) {
		//=====================================================
		 // Display contacts set up on phone
		 //=====================================================
		    String[] projectiona = new String[]{
		    		ContactsContract.CommonDataKinds.GroupMembership._ID,ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
		    	    ,ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME,ContactsContract.CommonDataKinds.GroupMembership.CONTACT_STATUS
		    	};
		    String selectiona = null;
		    if(selectedGroup != 99){
		    selectiona = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+ " = '" 
			        +selectedGroup + "'" + " AND " + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + " = '"  + ("vnd.android.cursor.item/group_membership") + "'" ;
		    }
		    	 c = getContentResolver().query(
		    	        Data.CONTENT_URI,
		    	        projectiona,
		    	        //null,
		    	        //ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + gid,
		    	        selectiona,
		    	        null,
		    	        null
		    	);
		    	 
		    	// Set up columns to be displayed from content provider
		         String[] columns = new String[] {
		        		c.getColumnName(c.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME)),c.getColumnName(1)
		         		 };
		         // Set up views on screen to contain data
		         int[] views = new  int[] {R.id.contactNameTxtTxt,R.id.hidden_value_id};
		         // create adapter and link to detail layout
		         SimpleCursorAdapter  adapter;
		         adapter = new SimpleCursorAdapter(this,R.layout.detail_record  , c, columns, views);
		         // Get list view and set adapter
		         ListView list1 = (ListView) findViewById(R.id.list1);
		         //
		         list1.setAdapter(adapter);
		         // set the on click listener for the list view
		 		list1.setOnItemClickListener(this);
	        }  
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView name = (TextView) v.findViewById(R.id.contactNameTxtTxt);
		TextView id = (TextView) v.findViewById(R.id.hidden_value_id);
		// Launching new Activity 
        Intent i = new Intent(getApplicationContext(), ClientDetailWithSwipe.class);
        // sending data (id and name) to new activity
        i.putExtra("id", id.getText().toString());
        i.putExtra("name", name.getText().toString());
        startActivity(i);
		}
	
	private void setupSearchView() {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) findViewById(R.id.searchView);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
	}

}
