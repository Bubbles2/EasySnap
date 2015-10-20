package com.matcom.estateagent;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ASearchItemAdapter extends ArrayAdapter<ASResult> {
	int resource;
	  public ASearchItemAdapter(Context context,
	                         int resource,
	                         List<ASResult> items) {
	    super(context, resource, items);
	    this.resource = resource;
	  }

	  @Override
	  public View getView(int position, View view, ViewGroup parent) {
	    LinearLayout noteView;

	    ASResult item = getItem(position);

	    String noteString = item.searchResultText;
	    String conString = item.name;
	    String addrString = item.searchCriteria;
	    String idString = item.id;
	    
	    String emlString = item.email;
	    String telString = item.phone;
	    
	     
	    if (view == null) {
	      noteView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, noteView, true);
	    } else {
	      noteView = (LinearLayout) view;
	    }

	    TextView conView = (TextView)noteView.findViewById(R.id.contact);
	    TextView emlView = (TextView)noteView.findViewById(R.id.email);
	    TextView telView = (TextView)noteView.findViewById(R.id.telephone);
	    
	    TextView notView = (TextView)noteView.findViewById(R.id.note);
	    TextView id = (TextView)noteView.findViewById(R.id.hidden_value_id);

	    conView.setText(conString);
	    emlView.setText(emlString);
	    telView.setText(telString);
	    notView.setText(noteString);
	    id.setText(idString);
	    
		if (emlString.isEmpty() ||  telString.isEmpty() || emlString.equals("N/A") ||  telString.equals("N/A")) {
			conView.setBackgroundColor(Color.argb(50,255,73,128));
			conView.setTextColor(Color.argb(50,0,150,253));
		} else {
			conView.setBackgroundColor(Color.argb(0,255,73,128));
		}
	  
	    

	    return noteView;
	  }

}
