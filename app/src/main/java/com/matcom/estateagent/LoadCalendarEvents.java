package com.matcom.estateagent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.provider.CalendarContract;
import android.provider.CallLog;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

// the class that will create a background thread and generate thumbs
public class LoadCalendarEvents extends AsyncTask<Void , Void, ArrayList<String>> {
	ArrayList<String> calendarEvents = new ArrayList<String>();
	ClientDetail caller;


	public LoadCalendarEvents(ClientDetail caller) {
		super();
		this.caller = caller;
	}



	/**
	 * Generate thumbs for each of the Image objects in the array
	 * passed to this method. This method is run in a background task.
	 */
	@Override
	protected ArrayList<String> doInBackground(Void...params ) {

		// Get Call log
		getCalendarEvents();
		return calendarEvents;
	}


	/**
	 * Update the UI thread when requested by publishProgress()
	 */
//	@Override
//	protected void onProgressUpdate() {
//		cacheUpdated();
//	}
	
	 @Override
	    protected void onPostExecute(ArrayList<String>  calendarDetails) {
		// Display Call logs
//	    updCallLog();  
		 //
		 caller.onPstCalExecuted(calendarDetails);
	    }
	 
	 public interface onPostCalExecute {
	      public void onPstCalExecuted(ArrayList<String> calendarDetails);
	    }
	 public Cursor getCalendarEvents() {
		 //
		 Cursor eventCursor = null;
		 String[] COLS = new String[]
					{ CalendarContract.Events._ID,CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART,
					CalendarContract.Events.EVENT_LOCATION};
		 //
		// Set up to select dates from today
			Date date = new Date();
			date.setHours(00);
			date.setMinutes(01);
			Calendar cal = new GregorianCalendar();
	        cal.setTime(date);
			
			//
			 eventCursor = caller.getContentResolver().query(
					  CalendarContract.Events.CONTENT_URI, COLS,
					  CalendarContract.Events.DTSTART +" > "+cal.getTimeInMillis()
					  , null, null);
			 
			  while (eventCursor.moveToNext()) {
					calendarEvents.add(eventCursor.getString(1));	  
			      	System.out.println("== From Calendar  ======> Date  : "+eventCursor.getString(1));
			        
			      }
					 //conCallCursor.close();
					 return eventCursor;
		 
		 
		 
		 }

}

