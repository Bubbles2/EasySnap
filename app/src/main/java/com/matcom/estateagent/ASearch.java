package com.matcom.estateagent;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ASearch extends Activity implements OnItemClickListener {
	
	// Array list of advanced search results
	ArrayList<ASResult> asResults = new ArrayList<ASResult>(); 
	String firstname;
	String lastname;
	String salutation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asearch);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asearch, menu);
		return true;
	}
	

	
	public void onSearch(View v) {
					//
					asResults.clear();
					// set the custom dialog components - text, image and button
					EditText seraddress = (EditText) findViewById(R.id.txtSerAddress);
					EditText sernotes = (EditText) findViewById(R.id.txtSerNotes);
					//
					String serstring = seraddress.getText().toString();
					if (!serstring.isEmpty()) {
						advSearch(serstring);
					}
					// Search Notes
					String serNstring = sernotes.getText().toString();
					if (!serNstring.isEmpty()) {
						advSearchNotes(serNstring);
					}
					//
					if (!asResults.isEmpty()) {
						displaySearch();
					}
					// Hide soft Keyboard for input
					InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
				    mgr.hideSoftInputFromWindow(seraddress.getWindowToken(), 0);
				    mgr.hideSoftInputFromWindow(sernotes.getWindowToken(), 0);
				
	}
	
	//=====================================================================
	//   Advanced Search - Using address
	//=====================================================================
	public void advSearch(String seradd) {
	//=====================================================
		Cursor conACursor = null;
		final String sa1 = "%"+seradd+"%"; // contains an advanced search
		// Search Address
		 String[] conACOLS = new String[]
					{ ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID,ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
				 ContactsContract.CommonDataKinds.StructuredPostal._ID, ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME};
		 
		 
			conACursor = getContentResolver().query(
					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
					conACOLS,
					ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS+ " LIKE ?",
					new String[] { sa1 }
					, null);
			
			//
			  asResults.clear();
			//  
			while (conACursor.moveToNext()) {
				// Get Email address and Phone inline in the call
				  getMainDetails(conACursor.getString(2));
				  Log.i("DF - In address find  ",conACursor.getString(2)+"  "+conACursor.getString(1)+"  -  "+conACursor.getString(3)) ;
				//
				ASResult ar = new ASResult(conACursor.getString(2),getEmail(conACursor.getString(0)),null,getPhones(conACursor.getString(0)),null,conACursor.getString(3),seradd,conACursor.getString(1),
						firstname,lastname,salutation);
				 asResults.add(ar);
		      }
			
				 conACursor.close();
				 
	        }
	
		//=====================================================================
		//   Advanced Search - Using notes
		//=====================================================================
		public void advSearchNotes(String sernote) {
			Cursor noteCur = null;
			final String note1 = "%"+sernote+"%"; // contains an advanced search 
			//
			String[] conNCOLS = new String[]
			{ ContactsContract.CommonDataKinds.Note.CONTACT_ID,ContactsContract.CommonDataKinds.Note.DISPLAY_NAME,ContactsContract.CommonDataKinds.Note.NOTE ,ContactsContract.CommonDataKinds.Note._ID};
			//
	         noteCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
	        		 conNCOLS,
	        		ContactsContract.CommonDataKinds.Note.NOTE + " LIKE ?",
	        		new String[] { note1 },
	        		null);
	         
	         //
			  asResults.clear();
			//
				 	
			 	 while (noteCur.moveToNext()) {
			 		getMainDetails(noteCur.getString(0));
					    ASResult ar = new ASResult(noteCur.getString(3),getEmail(noteCur.getString(0)),null,getPhones(noteCur.getString(0)),null,noteCur.getString(1),sernote,noteCur.getString(2)
					    		,firstname,lastname,salutation);
						 asResults.add(ar);
					      }
			 	 
			 	noteCur.close();
		
			
		}
		public void displaySearch() {
			ASearchItemAdapter aa = new ASearchItemAdapter(this, R.layout.activity_asearch_row,asResults);
			//ArrayAdapter aa = new ASearchItemAdapter(this, R.layout.search_result_row,asResults);
			ListView myListView = (ListView)findViewById(R.id.listView1);
			myListView.setAdapter(aa);
			myListView.setOnItemClickListener(this);
		}

		
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			TextView name = (TextView) v.findViewById(R.id.contact);
			TextView id = (TextView) v.findViewById(R.id.hidden_value_id);
			// Launching new Activity 
	        Intent i = new Intent(getApplicationContext(), ClientDetailWithSwipe.class);
	        //
	        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag

	        // sending data (id and name) to new activity
	        i.putExtra("id", id.getText().toString());
	        i.putExtra("name", name.getText().toString());
	        startActivity(i);

			
		}
		
		//=====================================================================
		//   Advanced Search - Get Contact Details 
		//=====================================================================
		 public String getEmail(String contactId) {
			 Cursor conACursor = null;
			 String conaAddress="N/A";
			 String[] conACOLS = new String[] 
						{  ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.DATA1,ContactsContract.CommonDataKinds.Email.CONTACT_ID};
				conACursor = this.getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						conACOLS,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID+"= '"+contactId+"'",
						null, null);
				  while (conACursor.moveToNext()) {
			      	System.out.println("DF from Email Address  ======> "+conACursor.getString(0));
			        conaAddress = conACursor.getString(0);
			      }
					 conACursor.close();
					 return conaAddress;
				 }
		 
		 
		 public String getPhones(String contactId) {
			 Cursor conCursor = null;
			 String phn = "N/A";
			// Get Phone details
			  // Type 3 = Work Type 1 = Home Type 2 = Mobile Type 12 = Main
			String[] conCOLS = new String[]
			{ ContactsContract.CommonDataKinds.Phone.DATA1,
							  ContactsContract.CommonDataKinds.Phone.DATA2,
							  ContactsContract.CommonDataKinds.Phone.LABEL,
							  ContactsContract.CommonDataKinds.Phone.TYPE,
							  ContactsContract.CommonDataKinds.Phone.NUMBER,
							  ContactsContract.CommonDataKinds.Phone.CONTACT_ID
							  };
			conCursor = this.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					conCOLS,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"= '"+contactId+"'",
					null, null);
				
				while (conCursor.moveToNext()) {
					phn =  conCursor.getString(0);
				}
		 return phn;
		 }
		 
			//=====================================================================
			//   Advanced Search - Get Main Contact Details
			//=====================================================================
		 public void getMainDetails(String contactId) {
			 		// Get first name and last name to use with salutation
			      	String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
			      	String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId };
			      	Cursor nameCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
			      	while (nameCur.moveToNext()) {
			      	    firstname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
			      	    lastname = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
			      	    salutation = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));			      	    
			      	}
			      	nameCur.close();
			      
			      }
		
		 



}
