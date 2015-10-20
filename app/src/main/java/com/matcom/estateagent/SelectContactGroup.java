package com.matcom.estateagent;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectContactGroup extends ListActivity {
ArrayList<String> grpDesc  = new ArrayList<String>();
ArrayList<Integer> grpId = new ArrayList<Integer>() ;
public static final String PREFS_NAME = "MyPrefsFile";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact_group);
		ListView lst = getListView();
		lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		getContactGroups();
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,grpDesc));
		// Get user preferences - Current Selection
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    int contactGroup = settings.getInt("contactGroup", 21);
		int curselpos =grpId.indexOf(contactGroup);
		lst.setItemChecked(curselpos, true );
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_contact_group, menu);
		return true;
	}
	
	public void getContactGroups() {
   	    String[] projectionb = new String[]{
   	    	    ContactsContract.Groups.TITLE,ContactsContract.Groups.ACCOUNT_NAME
   	    	 ,ContactsContract.Groups._ID
   	    	};

   	    	Cursor c2 = getContentResolver().query(
   	    			ContactsContract.Groups.CONTENT_URI,
   	    	        projectionb,
   	    	        //null,
   	    	        //ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + gid,
   	    	        null,
   	    	        null,
   	    	        null
   	    	);
   	   
   		    int i=0;
   		    if (c2.moveToFirst()) {
   				do {
   					grpDesc.add(c2.getString(0));
   					grpId.add(c2.getInt(2));
    						}
   				while (c2.moveToNext());
   			

   		    					}
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		int grp = grpId.get(position);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0 );
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt("contactGroup", grp);
   // Commit the edits!
	      editor.commit();
	      finish();
		
	}
	

}
