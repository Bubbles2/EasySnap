package com.matcom.estateagent;

import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
//import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;



public class PhotoFragment extends Fragment implements OnItemClickListener{
	
	//
	
	//
	String clientPhotos[];
	private OnPhotoSelectedListener listener;
	String path ;
	ProgressBar progressBar;
	//
	
	 public interface OnPhotoSelectedListener {
	      public void onImageSelected(String sel);
	    }
	 @Override
	    public void onAttach(Activity activity) {
	      super.onAttach(activity);
	      if (activity instanceof OnPhotoSelectedListener) {
	        listener = (OnPhotoSelectedListener) activity;
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implemenet the Photo selected Interface");
	      }
	    }
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			//getPhotos();
		
			new LoadPhoto().execute(new String[] { "None" });
		}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.activity_photo_fragment, container, false);
    }
/*	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	} */
    

	private MyPhoneImagesAdapter getPhotos() {
		// Inflate the layout for this fragment
    	File defaultDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    	String dirname = ((ClientDetailWithSwipe) getActivity()).getName();
		File f = new File(defaultDir, dirname.replace(" ", ""));
		path = f.getPath();
//		progressBar.setVisibility(View.VISIBLE);
		MyPhoneImagesAdapter myAdapter= null;
		//
		clientPhotos= f.list();
		if (clientPhotos != null) {
			
			
			 myAdapter = new MyPhoneImagesAdapter(getActivity(),path, clientPhotos);
			
		      
		}
		return myAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
     	
	}

	
	 //===================================================================================
	 // Create Background Process to Create the images adapter 
	 //===================================================================================
		// the class that will create a background thread and get call log
		// Note the last parameter is the Images adapter which we will pass in the post execute
		private class LoadPhoto extends AsyncTask<String, Void, MyPhoneImagesAdapter> {
			//
			ProgressDialog m_dialog;
			
			public LoadPhoto() {
				m_dialog = new ProgressDialog(getActivity());
		    }
		
			@Override
		    protected void onPreExecute() {
		        super.onPreExecute();
			        // initialize the dialog
		        String searchingString = getResources().getString(R.string.searchingString);
		        String searchMsgString = getResources().getString(R.string.searchMsgString);
		        m_dialog.setIndeterminate(true);
		        m_dialog.setCancelable(true);
//		        m_dialog.show(getActivity(), searchingString, searchMsgString);
		    }
			
			

			@Override
			protected MyPhoneImagesAdapter doInBackground(String... params) {
				// TODO Auto-generated method stub
			// Execution code
				// Note we can call a method in the main class but we are running it in
				// the asynchronous thread
//				getPhotos();
				return getPhotos();
			}
		
			@Override
			protected void onPostExecute(MyPhoneImagesAdapter result) {
				super.onPostExecute(result);
	// Note we can update the UI from the post execute method only note we pass the adapter which 
	// we created in the do in background
				GridView grid = (GridView) getView().findViewById(R.id.photoGrid);
			      grid.setAdapter(result);
			       // See: ImageAdapter.java
			      grid.setOnItemClickListener(new OnItemClickListener() {
			          public void onItemClick(AdapterView<?> parent, View v,
			                  int position, long id) {
			        	  String tst = "Pick";
			        	  String sel = path+"/"+clientPhotos[position];
			        	  listener.onImageSelected(sel);
				          }
			      });
//				   this.m_dialog.dismiss();
		       
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				// This method is called by publish progress
				super.onProgressUpdate(values);
			}

	
		
		}
		

 
}