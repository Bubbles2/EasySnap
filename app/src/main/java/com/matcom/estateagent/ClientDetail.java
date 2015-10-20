package com.matcom.estateagent;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Contacts.PhotosColumns;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ClientDetail extends Activity implements LoadCallLogA.onPstExecute,LoadCalendarEvents.onPostCalExecute,PhotoFragment.OnPhotoSelectedListener  {

	ArrayList<String> attendeesAddress = new ArrayList<String>();
	ArrayList<String> phoneslist = new ArrayList<String>();
	
	String name;
	String address;
	String email;
	String telephone;
	

	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	String id;
	private static final int TAKE_PICTURE = 1;

	
	
	ListView lsv;
	//
	String clientPhotos[];
	String selectedPhoto;
	
	public ListView getLsv() {
		return lsv;
	}
	public ArrayList<String> getAttendeesAddress() {
		return attendeesAddress;
	}
	public ArrayList<String> getPhoneslist() {
		return phoneslist;
	}
	
	

	@Override
	public void onResume()
	{
		super.onResume();
		
			//
		File defaultDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File f = new File(defaultDir, name.replace(" ", ""));
		String path = f.getPath();

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		//setProgressBarIndeterminateVisibility(true);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_detail);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		id = intent.getStringExtra("id");
		//=========
	     //ActionBar gets initiated
        ActionBar actionbar = getActionBar();
         // play with colors
        // Background image
        //actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg))
         actionbar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tab_blue)));
        //actionbar.setBackgroundDrawable(new ColorDrawable());
        // 
      //Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      //
        
      //initiating both tabs and set text to it.
        ActionBar.Tab personTab = actionbar.newTab()
        		.setIcon(R.drawable.personl);
        ActionBar.Tab photoTab = actionbar.newTab().setIcon(R.drawable.gallery );
        ActionBar.Tab calendarTab = actionbar.newTab().setIcon(R.drawable.calendar);
        ActionBar.Tab imageTab = actionbar.newTab().setIcon(R.drawable.show_image);
 
     //create the two fragments we want to use for display content
        PersonFragment personFragment = new PersonFragment();
        PhotoFragment photoFragment = new PhotoFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        ShowImageFragment showImageFragment = new ShowImageFragment();
 
    //set the Tab listener. Now we can listen for clicks.
        //personTab.setTabListener(new MyTabsListener(personFragment));
   //     photoTab.setTabListener(new MyTabsListener(photoFragment));
        
      //  calendarTab.setTabListener(new MyTabsListener(calendarFragment));
 //       imageTab.setTabListener(new MyTabsListener(showImageFragment));
 
   //add the two tabs to the actionbar
        actionbar.addTab(personTab);
        actionbar.addTab(photoTab);
        actionbar.addTab(calendarTab);
        actionbar.addTab(imageTab);
  	}
	protected void updCallLog(ArrayList<String> calls) {
		 // Display Call logs
	     lsv = (ListView) findViewById(R.id.list1);
		 lsv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,calls));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cltmenu, menu);
		return true;
	}
	//
	public void takePhoto() {
		// create output file
		File defaultDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
		// now specifing custom folder name in defaultDir
		File f = new File(defaultDir, name.replace(" ", ""));
		boolean crt = f.mkdir();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = new File(f.getPath() + File.separator +
	            "IMG_"+ timeStamp + ".jpg");
		
		//
		
		Uri outputFileUri = Uri.fromFile(mediaFile);
		// Generate the intent
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(takePictureIntent, TAKE_PICTURE);
		//
		
		
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Note this is an intent with start/stop service 
		case R.id.takePhoto:
			takePhoto();
			ActionBar actionbar = getActionBar();
			actionbar.setSelectedNavigationItem(1);
			return true;
		case R.id.del_photo_dir:
			delPhotos();
			return true;
		case R.id.showMap:
			// Launching new Activity 
                   Intent i = new Intent(getApplicationContext(), GoogleMapActivity.class);
                   // sending data to new activity
                   i.putExtra("address",getAddress());
                   startActivity(i);
			return true;
		case R.id.makeCall:
			// Launching new Activity 
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+telephone));
			startActivity(callIntent);
			return true;
//		case R.id.prefs:
//		startActivity(new Intent(this,PrefsActivity.class));
//			return true;
			
		default:
			return false;
		}
	}
	
	@Override
			public void onPstExecuted(ArrayList<String> calls) {
				// TODO Auto-generated method stub
				updCallLog(calls);
			}
			@Override
			public void onPstCalExecuted(ArrayList<String> calendarDetails) {
				// TODO Auto-generated method stub
				
			}	
			
			public String getName() {
				return name;
			}
			public String getId() {
				return id;
			}
			@Override
			public void onImageSelected(String sel) {
				//
				selectedPhoto = sel;
				ActionBar actionbar = getActionBar();
				actionbar.setSelectedNavigationItem(3);
			        
				
			}
			private void delPhotos()
			{
				
				new AlertDialog.Builder(this)
			    .setTitle("Delete entry")
			    .setMessage("Are you sure you want to delete this entry?")
			    
			    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	performDelete();
			        }
			     })
			    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			     .show();
				
				
				
				
			}
			private void performDelete() {
				// create output file
				File defaultDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				// now specifing custom folder name in defaultDir
				String path = defaultDir.getAbsolutePath()+"/"+name.replace(" ", "");
				File f = new File(defaultDir, name.replace(" ", ""));
				//=========================================================
				
			    if (f.exists()) {
			       String deleteCmd = "rm -r " + path;
			        Runtime runtime = Runtime.getRuntime();
			        try {
			            runtime.exec(deleteCmd);
			        } catch (IOException e) { }
			    }
				//=========================================================
				}
			}
			public String getSelectedPhoto() {
				return selectedPhoto;
			}
			
			 public void sendEmail(View v) {

		    	 final Intent emailIntent = new Intent(Intent.ACTION_SEND); 
		    	 emailIntent.setType("text/plain"); 
		    	 emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"webmaster@website.com"}); 
		    	 emailIntent.putExtra(Intent.EXTRA_SUBJECT, "my subject"); 
		    	 emailIntent.putExtra(Intent.EXTRA_TEXT, "body text"); 
		    	 //
		    	 Uri myuri = Uri.fromFile(new File(selectedPhoto));
		     	 emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, myuri);
		    	 //
		    	 startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		    	 //
		     	
			
			}
			public String getAddress() {
				return address;
			}
			public void setAddress(String address) {
				this.address = address;
			}
			public String getEmail() {
				return email;
			}
			public void setEmail(String email) {
				this.email = email;
			}

		
}
