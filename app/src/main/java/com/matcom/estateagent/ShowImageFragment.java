package com.matcom.estateagent;

import android.os.Bundle;
import android.app.Activity;
//import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ShowImageFragment extends Fragment {
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   	
        return inflater.inflate(R.layout.activity_showimage_fragment, container, false);
    }
    
    
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// find our ImageView in the layout
         ImageView img = (ImageView) getView().findViewById(R.id.single_image);
         Bitmap bitmap = BitmapFactory.decodeFile(((ClientDetailWithSwipe) getActivity()).getSelectedPhoto());
         
         img.setImageBitmap(bitmap);
    }
    
   
 
}