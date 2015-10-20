package com.matcom.estateagent;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MyPhoneImagesAdapter extends BaseAdapter {
	// a list of resource IDs for the images we want to display
	private String[] images;
	
	private String[] files;
	
	private String path;
	
	private Context c;

	// a context so we can later create a view within it
	private Context myContext;
	private int photoCount ;
	
	// store a cache of resized bitmaps
	// Note: we're not managing the cache size to ensure it doesn't 
	// exceed any maximum memory usage requirements
	private Bitmap[] cache;

	// Constructor
	public MyPhoneImagesAdapter(Context c,String photoPath, String[] clientPhotos) {
		// Set up instance variable
		this.c = c;
		this.path = photoPath;
		
		// Post in asynch task
		//new LoadPhoto().execute(clientPhotos);
		loadPhotos(c, photoPath, clientPhotos);
	}

	private void loadPhotos(Context c, String photoPath, String[] clientPhotos) {
		path = photoPath+"/";
		myContext = c;
		photoCount = clientPhotos.length;
		
		// Review contents of Camera Directory
				/*path = Environment.getExternalStorageDirectory()+"/"+photodir;
				File dir = new File(path);
				files = dir.list();*/
		
		if (clientPhotos != null) {
		int count = 0, index = 0, j = clientPhotos.length;
		count = clientPhotos.length;
		
		// We now know how many images we have. Reserve the memory for an 
		// array of integers with length 'count' and initialize our cache.
		images = new String[count];
		cache = new Bitmap[count];

		// Next, (unsafely) try to get the values of each of those fields
		// into the images array.
		try {
			for(int i=0; i < j; i++)
					images[index++] =clientPhotos[i];
			 
		} catch(Exception e) {}
		// safer: catch IllegalArgumentException & IllegalAccessException
		}
	}

	@Override
	// the number of items in the adapter
	public int getCount() {
		return photoCount;
	}

	@Override
	// not implemented, but normally would return 
	// the object at the specified position
	public Object getItem(int position) {
		return null;
	}

	@Override
	// return the resource ID of the item at the current position
	public long getItemId(int position) {
		return 0;
		//return images[position];
	}

	// create a new ImageView when requested
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// we've been asked for an ImageView at a specific position. If
		// one doesn't already exist (ie, convertView is null) then we must create
		// one. Otherwise we can pass it convertView or a recycled view
		// that's been passed to us.
		
		ImageView imgView;

		
		if(convertView == null) {

			// create a new view
			imgView = new ImageView(myContext);
			imgView.setLayoutParams(new GridView.LayoutParams(200,200));
			
			//
	
			//imgView.setLayoutParams(new GridView.LayoutParams(300, 300));
			imgView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			imgView.setScaleType(ImageView.ScaleType.CENTER);
			imgView.setPadding(8, 8, 8, 8);
			
			//
		

		} else {
	
			// recycle an old view (it might have old thumbs in it!)
			imgView = (ImageView) convertView;
	
		}

		// see if we've stored a resized thumb in cache
		if(cache[position] == null) {
		
			// create a new Bitmap that stores a resized
			// version of the image we want to display. 
			BitmapFactory.Options options = new BitmapFactory.Options();
			//
			options.inSampleSize = 4;
			Bitmap thumb = BitmapFactory.decodeFile(path+images[position],options);
			Bitmap thumb2 = Bitmap.createScaledBitmap(thumb, 180, 180, false);
			// store the resized thumb in a cache so we don't have to re-generate it
			cache[position] = thumb2;
		}

		// use the resized image we have in the cache
		imgView.setImageBitmap(cache[position]);

		return imgView;
	}


	
}
