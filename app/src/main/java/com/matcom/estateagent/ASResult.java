package com.matcom.estateagent;

import android.os.Parcel;
import android.os.Parcelable;

public class ASResult implements Parcelable {
	// Used to store advanced search results	
		public ASResult(String id,String email, String address , String phone, String mobile,  String name, String searchCriteria,
				String searchResultText,String firstname,String lastname,String salutation ) {
			super();
			this.id = id;
			
			this.email = email;
			this.address = address;
			this.phone = phone;
			this.mobile = mobile;
			
			this.name = name;
			this.searchCriteria = searchCriteria;
			this.searchResultText = searchResultText;
			
			this.firstname = firstname;
			this.lastname = lastname;
			this.salutation = salutation;
			
			
		}
		String id;
		String email;
		String address;
		String phone;
		String mobile;
		String name;
		String searchCriteria;
		String searchResultText;
		
		String firstname;
		String lastname;
		String salutation;


	    protected ASResult(Parcel in) {
	        id = in.readString();
	        email = in.readString();
	        address = in.readString();
	        phone = in.readString();
	        mobile = in.readString();
	        name = in.readString();
	        searchCriteria = in.readString();
	        searchResultText = in.readString();
	        
	       firstname = in.readString();
		   lastname = in.readString();
		   salutation = in.readString();
	    }

	    @Override
	    public int describeContents() {
	        return 0;
	    }

	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeString(id);
	        dest.writeString(email);
	        dest.writeString(address);
	        dest.writeString(phone);
	        dest.writeString(mobile);
	        dest.writeString(name);
	        dest.writeString(searchCriteria);
	        dest.writeString(searchResultText);
	        
	        dest.writeString(firstname);
	        dest.writeString(lastname);
	        dest.writeString(salutation);
	    }

	    @SuppressWarnings("unused")
	    public static final Parcelable.Creator<ASResult> CREATOR = new Parcelable.Creator<ASResult>() {
	        @Override
	        public ASResult createFromParcel(Parcel in) {
	            return new ASResult(in);
	        }

	        @Override
	        public ASResult[] newArray(int size) {
	            return new ASResult[size];
	        }
	    };
	}