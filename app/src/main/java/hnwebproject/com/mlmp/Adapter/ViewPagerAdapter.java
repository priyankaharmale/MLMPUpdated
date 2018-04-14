package hnwebproject.com.mlmp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hnwebmarketing on 1/27/2018.
 */

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    String userId;
    Fragment fragment,fragment2;
    //====================================================================================================
    public ViewPagerAdapter(FragmentManager fm, Fragment fragment,Fragment fragment2) {
        super(fm);
        this.userId = userId;
        this.fragment = fragment;
        this.fragment2 = fragment2;
    }
    //====================================================================================================
    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
          return   fragment;
        } else if (position == 1) {
            return fragment2;
        } /*else if (position == 2) {
            return new What_They_Want_Fragment(userId);
        }*/

        else return null;
    }
    //====================================================================================================
    @Override
    public int getCount() {
        return 2;
    }
//====================================================================================================

}
