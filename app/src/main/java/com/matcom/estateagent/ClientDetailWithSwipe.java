package com.matcom.estateagent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
 
public class ClientDetailWithSwipe extends FragmentActivity implements
        ActionBar.TabListener, LoadCallLogA.onPstExecute,LoadCalendarEvents.onPostCalExecute,PhotoFragment.OnPhotoSelectedListener {
	
	//==============   OLd Client Details  ==================
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
	
	//==============   OLd Client Details  ==================

 
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" };
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail_with_swipe);
        // 0ld Client ==================
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		//setProgressBarIndeterminateVisibility(true);

		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_client_detail);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		id = intent.getStringExtra("id");
		//  Old Client ==================
 
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
 
        // Adding Tabs
        /*for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }*/
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.personl)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.gallery)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.calendar)
                .setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.show_image)
                .setTabListener(this));
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
    
    // =========  Old Client Details
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cltmenu, menu);
		return true;
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
		case R.id.help2Txt:
			Intent intentHelp = new Intent(this, Help2Activity.class);
			startActivity(intentHelp);
				return true;
//		case R.id.prefs:
//		startActivity(new Intent(this,PrefsActivity.class));
//			return true;
			
		default:
			return false;
		}
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
	
	
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}

	private void delPhotos()
	{
		
		new AlertDialog.Builder(this)
	    .setTitle(getResources().getString(R.string.delete_entry))
	    .setMessage(getResources().getString(R.string.delete_msg))
	    
	    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	performDelete();
	        }
	     })
	    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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
	            // Return to main person screen
	            actionBar.setSelectedNavigationItem(0);
	            viewPager.setAdapter(mAdapter);
	            // Get reference to current fragment
//	            PhotoFragment fragment = (PhotoFragment) getFragmentManager().findFragmentByTag(tag)  findFragmentById(R.id.);
//	            fragment.<specific_function_name>(); 
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

    // ========== Old Client Details
	@Override
	public void onImageSelected(String sel) {
		// TODO Auto-generated method stub
		selectedPhoto = sel;
		ActionBar actionbar = getActionBar();
		actionbar.setSelectedNavigationItem(3);
		
	}
	@Override
	public void onPstCalExecuted(ArrayList<String> calendarDetails) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPstExecuted(ArrayList<String> calls) {
		// TODO Auto-generated method stub
		
	}
 
}