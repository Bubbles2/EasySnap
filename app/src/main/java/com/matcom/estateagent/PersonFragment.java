package com.matcom.estateagent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;


public class PersonFragment extends Fragment {
	
	ArrayList<String> callDetails = new ArrayList<String>();

	 private GoogleMap map;
	 LatLng cursel;
	 LatLng cursel2;
	
	
	
	String name;
	String address;
	String id;
	String email;
	
	ArrayList<String> attendeesAddress = new ArrayList<String>();
	ArrayList<String> phoneslist = new ArrayList<String>();
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		   View v = inflater.inflate(R.layout.activity_person_fragment, container, false);
	        return v;
		    }
	 
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			String caller = getActivity().getLocalClassName().toLowerCase();
			
			if (caller.equals("clientdetailwithswipe")) {
				
				name = ((ClientDetailWithSwipe) getActivity()).getName();
	        	id = ((ClientDetailWithSwipe) getActivity()).getId();
				
			} else {
				
				if (getArguments().containsKey("Contact_ID")) {
					// Load the dummy content specified by the fragment
					// arguments. In a real-world scenario, use a Loader
					// to load content from a content provider.
					id = getArguments().getString("Contact_ID");
					name = getArguments().getString("Contact_Name");
					
				}

			}

			
		} 
	 
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	        TextView namesc = (TextView) getView().findViewById(R.id.nameTxt);
     		namesc.setText(name);
     		TextView addsc = (TextView) getView().findViewById(R.id.addressTxt);
     		TextView emailsc = (TextView) getView().findViewById(R.id.emailtxt);
     		TextView phonesc = (TextView) getView().findViewById(R.id.phonetxt);
     		// Get Photo
     		long lid = Long.parseLong(id);
     		Bitmap bmp = BitmapFactory.decodeStream(openPhoto(lid)); 
      		ImageView img = (ImageView) getView().findViewById(R.id.conImage);
     		img.setImageBitmap(bmp);
     		// Get Address
     		address = getAddress(id);
     		addsc.setText(address);
     		((ClientDetailWithSwipe) getActivity()).setAddress(address);
     		email = getEmail(id);
     		emailsc.setText(email);
     		((ClientDetailWithSwipe) getActivity()).setEmail(email);
     		String phn = getPhones(id);
     		phonesc.setText(phn);
     		((ClientDetailWithSwipe) getActivity()).setTelephone(phn);
     		
     		//
     		displayCallList(getCalls(name));
	}

 

	 public String getAddress(String contactId) {
		 // Sorcha 288
		 Cursor conACursor = null;
		 String conaAddress="N/A";
		 String[] conACOLS = new String[]
					{ ContactsContract.CommonDataKinds.StructuredPostal.DATA1,ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID};
			conACursor = getActivity().getContentResolver().query(
					ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
					conACOLS,
					ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID+"= '"+contactId+"'",
					null, null);
			  while (conACursor.moveToNext()) {
		      	System.out.println("=== DataDF from Contacts Address  ======> "+conACursor.getString(0)+" id "+conACursor.getString(2));
		        conaAddress = conACursor.getString(0);
		        attendeesAddress.add(conaAddress);
		      }
				 conACursor.close();
				 return conaAddress;
			 }
	 public String getEmail(String contactId) {
		 Cursor conACursor = null;
		 String conaAddress="N/A";
		 String[] conACOLS = new String[] 
					{  ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.DATA1,ContactsContract.CommonDataKinds.Email.CONTACT_ID};
			conACursor = getActivity().getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					conACOLS,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID+"= '"+contactId+"'",
					null, null);
			  while (conACursor.moveToNext()) {
		      	System.out.println("=== DataDF from Email Address  ======> "+conACursor.getString(0));
		        conaAddress = conACursor.getString(0);
		        attendeesAddress.add(conaAddress);
		      }
				 conACursor.close();
				 return conaAddress;
			 }
	 
	 public String getEmailContactIds(String mail) {
		 Cursor conECursor = null;
		 String contactid = "N/A";
	      	System.out.println("=== *** getEmailContactIds ** Mail ======> "+mail);

		// Get Email details from contacts 
			String[] conECOLS = new String[]
						{ ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.DATA1,ContactsContract.CommonDataKinds.Email.CONTACT_ID};
				conECursor =getActivity().getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						conECOLS,
						ContactsContract.CommonDataKinds.Email.ADDRESS+"= '"+mail+"'",
						null, null);
				  if (conECursor.moveToNext()) {
			      	System.out.println("=== DataDF from Contacts ** Mail ======> "+conECursor.getString(0)+" ??? "+conECursor.getString(1)
			      			+" id "+conECursor.getString(2));
			      	contactid= conECursor.getString(2);
			      	//attendeesContactId.add(contactid);
			      	
	 }
				  return contactid;
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
		conCursor = getActivity().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				conCOLS,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"= '"+contactId+"'",
				null, null);
			
			while (conCursor.moveToNext()) {
				phn =  conCursor.getString(0);
		    	phoneslist.add(conCursor.getString(0));
			}
			/*System.out.println("Phone DataDF from Phones  ======> "
					+				 
					" Label   "+conCursor.getString(2)+
					" Type "+conCursor.getString(3)+
					" Num "+conCursor.getString(4));
				   //   	PhoneNos phone = new PhoneNos(img,conCursor.getString(0));
			      	phn =  conCursor.getString(0);
			    	phoneslist.add(conCursor.getString(0));
			      }*/
	 return phn;
	 } 
	 
		private LatLng getLongLat(String addr) {
			
			
			 cursel = new LatLng(0,0);
			  double longt=0;
			  double lat=0;

			// Find Address
			Geocoder geoCoder =	new Geocoder(
					getActivity(), Locale.getDefault());
			try {
				List<Address> addresses = geoCoder.getFromLocationName
						(addr, 5);

				if (addresses.size() > 0) 
				{
					
						longt = addresses.get(0).getLongitude();
						lat = addresses.get(0).getLatitude();
						cursel = new LatLng(lat, longt);
						
				}
			}
				 
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
		 	System.out.println("=== Get Long Lat  ======> "+addr+"  Adr "+longt+"  Adr "+lat);
		
			return cursel;
		}
		// Access Call Log
		//
		 public Cursor getCalls(String callerName) {
			 callDetails.clear();
			 Cursor conCallCursor = null;
			 String conCall="*** NO Calls  ***";
			 String[] conACOLS = new String[] 
						{  
					 	android.provider.CallLog.Calls.NUMBER, 
		                android.provider.CallLog.Calls.TYPE,
		                android.provider.CallLog.Calls.CACHED_NAME,
		                android.provider.CallLog.Calls.DATE,
		                android.provider.CallLog.Calls.DURATION,
//		                android.provider.CallLog.Calls.CONTENT_ITEM_TYPE,
//		                android.provider.CallLog.Calls.CONTENT_TYPE
						};
			 
			// Defines a string to contain the selection clause
		        String mSelectionClause = null;

		        // Initializes an array to contain selection arguments
		        String[] mSelectionArgs = {""};
		        String cnam = android.provider.CallLog.Calls.CACHED_NAME;
		        mSelectionClause    = android.provider.CallLog.Calls.CACHED_NAME+ " ='"+callerName+"'";
//		        mSelectionClause    = android.provider.CallLog.Calls.CACHED_NAME+ " >= ?";
//		        mSelectionArgs[1]   = createDate(2013,1,1).toString();
		        String orderBy = android.provider.CallLog.Calls.DATE + " DESC"+","+android.provider.CallLog.Calls.TYPE;
		        String callType[] = {"In - ","Out -","Missed - "};
				conCallCursor = getActivity().getContentResolver().query(
						android.provider.CallLog.Calls.CONTENT_URI,
						conACOLS,
						mSelectionClause,
						null,
						orderBy);
				
				  while (conCallCursor.moveToNext()) {
				  Long millis = Long.parseLong(conCallCursor.getString(3));
				  Date date = new Date(millis);
				  SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
					  sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					  String formattedDate = sdf.format(date);
					  //
					  String calldet = callType[conCallCursor.getInt(1)-1] +"  "+formattedDate;
					  callDetails.add(calldet);
					  //
					  
			      	System.out.println("== From Call Log  ======> Date  : "+formattedDate+" No.     "+conCallCursor.getString(0)+" Name     "+conCallCursor.getString(2));
			        
			      }
					 //conCallCursor.close();
					 return conCallCursor;
				 }
    
			public void displayCallList(Cursor callLogCursor) {
				//=====================================================
				 // Display contacts set up on phone
				 //=====================================================
				    // call detail line is a layout with only a text view we use  this because 
				    // the simple list item does not respect the required style
				         ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.call_detail_line, callDetails);
				         // Get list view and set adapter
				         ListView list1 = (ListView) getView().findViewById(R.id.callList);
				         //
				         list1.setAdapter(adapter);
				         // set the on click listener for the list view
				 		
			        }  
			
			
			public InputStream openPhoto(long contactId) {
			     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
			     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
			     Cursor cursor = getActivity().getContentResolver().query(photoUri,
			          new String[] {Contacts.Photo.PHOTO}, null, null, null);
			     if (cursor == null) {
			         return null;
			     }
			     try {
			         if (cursor.moveToFirst()) {
			             byte[] data = cursor.getBlob(0);
			             if (data != null) {
			                 return new ByteArrayInputStream(data);
			             }
			         }
			     } finally {
			         cursor.close();
			     }
			     return null;
			 }
}