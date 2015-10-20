package com.matcom.estateagent;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchViewActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	//
	private SimpleCursorAdapter adapter;
	String query;
	Cursor cursor;
	Boolean singleSelection;

	
	//====================================================================
	// This class is called when we select a suggested search result
	//====================================================================

	private TextView resultText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_search_view);

		resultText = (TextView)findViewById(R.id.searchViewResult);
		//
		// If an item has been selected from the suggestions list ie. "SEARCH_SUGGESTION_CLICKED"
		//
		String s = getIntent().getAction().toString();
		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(s)) {
			// Get data for selected for item 
			Cursor phoneCursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
			phoneCursor.moveToFirst();
			// get Index for name and Id
			int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			int idInt = phoneCursor.getColumnIndex(ContactsContract.Contacts._ID);
			String name = phoneCursor.getString(idDisplayName);
			String contactId = phoneCursor.getString(idInt);
			phoneCursor.close();
			// Launching new Activity 
                   Intent i = new Intent(getApplicationContext(), ClientDetailWithSwipe.class);
                   // sending data (id and name) to new activity
                   i.putExtra("id", contactId);
                   i.putExtra("name", name);
                   startActivity(i);
                   
		}
		else
		{
			singleSelection=false;
			// Create a new adapter and bind it to the List View
			
			  /*adapter = new SimpleCursorAdapter(this,
				      android.R.layout.simple_list_item_1, null,
				      new String[] { Contacts.DISPLAY_NAME },
				      new int[] { android.R.id.text1 }, 0);*/
			
			
		    adapter = new SimpleCursorAdapter(this,
		      android.R.layout.simple_list_item_2, null,
		      new String[] { Contacts.DISPLAY_NAME,Contacts._ID},
		      new int[] { android.R.id.text1,android.R.id.text2}, 0);
		    setListAdapter(adapter);
//		    getListView().setOnItemClickListener((OnItemClickListener) this);
//		    getListView().setOnItemSelectedListener((OnItemSelectedListener) this);
		    //
		    
		    //
		    query = getIntent().getStringExtra(SearchManager.QUERY);
		    // Initiate the Cursor Loader
		    getLoaderManager().initLoader(0, null, this);

		    // Get the launch Intent
		    //parseIntent(getIntent());	
		}


		//setupSearchView();
		
		
	}

	private void setupSearchView() {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) findViewById(R.id.searchView);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
	}


	protected void onNewIntent (Intent intent) {
		String s = "A";
		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
			//handles suggestion clicked query
			String displayName = getDisplayNameForContact(intent);
			resultText.setText(displayName);
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			resultText.setText("should search for query: '" + query + "'...");
		}
	}

	private String getDisplayNameForContact(Intent intent) {
		Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
		phoneCursor.moveToFirst();
		int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		String name = phoneCursor.getString(idDisplayName);
		phoneCursor.close();
		return name;
	}
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	// this is only called once the user selects enter on a list of suggestions

    	// Search on any contact data by appending the query string to the contacts provider
    	// note we do not include any selection criteria
    	Uri  contentUri =
                        Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(query));
    	//
    	// Construct the new query in the form of a Cursor Loader.
	    String[] projection = { Contacts._ID,Contacts.LOOKUP_KEY,Contacts.DISPLAY_NAME};
//            
            return new CursorLoader(this,
                    contentUri,
                    projection,
                    null,
                    null,
                    null);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    	 // Replace the result Cursor displayed by the Cursor Adapter with
	    // the new result set.
    	//
    	this.cursor =cursor;
    	//
	    adapter.swapCursor(cursor);	
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    	// Remove the existing result Cursor from the List Adapter.
	    adapter.swapCursor(null);
    }

	
	 @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data
			TextView name = (TextView) v.findViewById(android.R.id.text1);
			TextView con_id = (TextView) v.findViewById(android.R.id.text2);
			// Launching new Activity 
	        Intent i = new Intent(getApplicationContext(), ClientDetailWithSwipe.class);
	        //
	        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag

	        // sending data (id and name) to new activity
	        i.putExtra("id", con_id.getText().toString());
	        i.putExtra("name", name.getText().toString());
	        startActivity(i);

	  }
	

}
