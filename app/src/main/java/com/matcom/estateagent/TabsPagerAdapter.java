package com.matcom.estateagent;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
 
public class TabsPagerAdapter extends FragmentPagerAdapter  {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new PersonFragment();
        case 1:
            // Games fragment activity
            return new PhotoFragment();
        case 2:
            // Movies fragment activity
            return new CalendarFragment();
        case 3:
            // Movies fragment activity
            return new ShowImageFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

	  
  
}