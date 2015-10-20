package com.matcom.estateagent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.provider.CallLog;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

// the class that will create a background thread and generate thumbs
public class LoadCallLogA extends AsyncTask<Void , Void, ArrayList<String>> {
	ArrayList<String> callDetails = new ArrayList<String>();
	ClientDetail caller;
	String callerName;


	public LoadCallLogA(ClientDetail caller) {
		super();
		this.caller = caller;
	}
	public LoadCallLogA(ClientDetail caller, String name) {
		super();
		this.caller = caller;
		this.callerName = name;
	}



	public LoadCallLogA(ArrayList<String> callDetails) {
		super();
		this.callDetails = callDetails;
	}


	/**
	 * Generate thumbs for each of the Image objects in the array
	 * passed to this method. This method is run in a background task.
	 */
	@Override
	protected ArrayList<String> doInBackground(Void...params ) {

		// Get Call log
		getCalls();
		return callDetails;
	}


	/**
	 * Update the UI thread when requested by publishProgress()
	 */
//	@Override
//	protected void onProgressUpdate() {
//		cacheUpdated();
//	}
	
	 @Override
	    protected void onPostExecute(ArrayList<String>  callDetails) {
		// Display Call logs
//	    updCallLog();  
		 //
		 caller.onPstExecuted(callDetails);
	    }
	 
	 public interface onPstExecute {
	      public void onPstExecuted(ArrayList<String> calls);
	    }
	 public Cursor getCalls() {
		 Cursor conCallCursor = null;
		 String conCall="*** NO Calls  ***";
		 String[] conACOLS = new String[] 
					{  
				 	android.provider.CallLog.Calls.NUMBER, 
	                android.provider.CallLog.Calls.TYPE,
	                android.provider.CallLog.Calls.CACHED_NAME,
	                android.provider.CallLog.Calls.DATE,
	                android.provider.CallLog.Calls.DURATION,
					};
		 
		// Defines a string to contain the selection clause
	        String mSelectionClause = null;

	        // Initializes an array to contain selection arguments
	        String[] mSelectionArgs = {""};
	        String cnam = android.provider.CallLog.Calls.CACHED_NAME;
	        mSelectionClause    = android.provider.CallLog.Calls.CACHED_NAME+ " ='"+this.callerName+"'";
//	        mSelectionClause    = android.provider.CallLog.Calls.CACHED_NAME+ " >= ?";
//	        mSelectionArgs[1]   = createDate(2013,1,1).toString();
	        
			conCallCursor = caller.getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI,
					conACOLS,
					mSelectionClause,
					null, null);
			
			  while (conCallCursor.moveToNext()) {
				  
			  Long millis = Long.parseLong(conCallCursor.getString(3));
			  Date date = new Date(millis);
			  SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
				  sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				  String formattedDate = sdf.format(date);
				  //
				  String calldet = conCallCursor.getString(2) +"  "+formattedDate;
				  callDetails.add(calldet);
				  //
				  
		      	System.out.println("== From Call Log  ======> Date  : "+formattedDate+" No.     "+conCallCursor.getString(0)+" Name     "+conCallCursor.getString(2));
		        
		      }
				 //conCallCursor.close();
				 return conCallCursor;
			 }

}

