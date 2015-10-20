package com.matcom.estateagent;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CalendarFragment extends Fragment {
	
	//
	SimpleCursorAdapter adapt;
	String evtvals;
	ListView ls;
	
	private static final String[] COLS = new String[]
			{ CalendarContract.Events._ID,CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART,
			CalendarContract.Events.EVENT_LOCATION,CalendarContract.Events.ACCOUNT_NAME,CalendarContract.Events.HAS_ATTENDEE_DATA};
	//CalendarContract.Attendees.ATTENDEE_EMAIL
//	private OnEventSelectedListener listener;
	public Cursor eventCursor;
	String email;
	//
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		View v = inflater.inflate(R.layout.activity_calendar_fragment, container, false);
    	email = ((ClientDetailWithSwipe) getActivity()).getEmail();
    	
    	//
    	ls = (ListView) v.findViewById(R.id.eventList);
    
    	getCalendarEvents(email);
		
        return v;
	}
	
	   private void getCalendarEvents(String email) {
	    	
	    	// Get list view and set adapter
	     	
	    	// Check Guests
		   // Check what event values have current email as attendee
	    	String orderBy = CalendarContract.Attendees.DTSTART + " DESC";
	    	String[] colsAttendees = new String[]
	    			{CalendarContract.Attendees.ATTENDEE_NAME, CalendarContract.Attendees.ATTENDEE_EMAIL,CalendarContract.Attendees.EVENT_ID,CalendarContract.Attendees.EVENT_COLOR};
	    	 Cursor attendeeEventCursor = getActivity().getContentResolver().query(
					  CalendarContract.Attendees.CONTENT_URI, colsAttendees,
					  CalendarContract.Attendees.ATTENDEE_EMAIL +" = '"+email+"'"
					  //null
					  , null, null);
	    	 //
	    	// looping through all rows and adding to list
	    	// Create an array of event id's which have the email value as a contact
	         if ( attendeeEventCursor.moveToFirst()) {
	        	 evtvals = "(";
	        	 int i = 0;
	             do {
	            	 i++;
	            	 if (i == attendeeEventCursor.getCount()) {
	            		 evtvals = evtvals+attendeeEventCursor.getString(2)+")";
					} else {
						evtvals = evtvals+attendeeEventCursor.getString(2)+",";
					}
	            	 
	                
	             } while (attendeeEventCursor.moveToNext());
	         }
	    	 //
	         if (attendeeEventCursor.getCount()>0) {
	        	 
	         	// Set up to select dates from today
	 			Date date = new Date();
	 			date.setHours(00);
	 			date.setMinutes(01);
	 			Calendar cal = new GregorianCalendar();
	 	        cal.setTime(date);
	 			// 
	 	        String select =  CalendarContract.Events.DTSTART +" > "+cal.getTimeInMillis() +" AND "+CalendarContract.Events._ID+ " IN "+evtvals;
	 	        String selecta =  CalendarContract.Events._ID+ " IN "+evtvals;
	 			//
	 			 eventCursor = getActivity().getContentResolver().query(
	 					  CalendarContract.Events.CONTENT_URI, COLS,
	 					  selecta
	 					  //null
	 					  , null, null);
	 			 //
	 			 //
	 			  String[] columns = new String[]{
	 					  eventCursor.getColumnName(1),eventCursor.getColumnName(2)};
	 					  //eventCursor.getColumnName(1),eventCursor.getColumnName(3),eventCursor.getColumnName(2)};
	 				//int[] views = new int[]{R.id.rowEvent,R.id.rowLocation,R.id.rowEventDate};
	 				int[] views = new int[]{R.id.rowEvent,R.id.rowEventDate};
	 				adapt = new SimpleCursorAdapter(getActivity(),
	 				R.layout.activity_calendar_fragment_row, eventCursor,  columns, views,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	 				//====================================
	 				// Format date to display in list view
	 				//====================================
	 				adapt.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
	 					Format df = DateFormat.getDateFormat(getActivity());
	 					Format tf = DateFormat.getTimeFormat(getActivity());
	 				    public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
	 				        if (aColumnIndex == 1) {
	 			                String evt = aCursor.getString(1)+" in "+aCursor.getString(3);
	 			                TextView textView = (TextView) aView;
	 			                textView.setText(evt);
//	 			                textView.setText("Create date: " + MyFormatterHelper.formatDate(getApplicationContext(), createDate));
	 			                return true;
	 			         }

	 				        if (aColumnIndex == 2) {
	 				                Long start = aCursor.getLong(aColumnIndex);
	 				                TextView textView = (TextView) aView;
	 				                textView.setText(df.format(start)+ " at "+tf.format(start));
//	 				                textView.setText("Create date: " + MyFormatterHelper.formatDate(getApplicationContext(), createDate));
	 				                return true;
	 				         }

	 				         return false;
	 				    }
	 				});
	 				//=============================
	 				
	 				
	 				//=============================
	 				
	 				ls.setAdapter(adapt);
	 				// setListAdapter(adapt);
	 				//eventCursor.close();
				
			}
	     	
	  
			
		}	
	
	

}
