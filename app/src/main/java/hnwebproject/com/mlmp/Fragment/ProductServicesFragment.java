package hnwebproject.com.mlmp.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hnwebproject.com.mlmp.Adapter.ViewPagerAdapter;
import hnwebproject.com.mlmp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductServicesFragment extends Fragment {

    String TAG = ProductServicesFragment.class.getSimpleName();

    public static ViewPager viewPager;
    TabLayout tabLayout;
    private String[] pageTitle = {"PRODUCTS", "SERVICES"};

    public ProductServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_services, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_product);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);


        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }
        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ProductFragment fragment = new ProductFragment();
        ServicesFragment fragment2 = new ServicesFragment();

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragment, fragment2);

        viewPager.setAdapter(pagerAdapter);
        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //change ViewPager page when tab selected
        //tabLayout.setupWithViewPager(viewPager);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.e("onTabSelected", "onTabUnselected" + tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("onTabUnselected", "onTabUnselected" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("onTabReselected", "onTabReselected" + tab.getPosition());
            }
        });


    }

}
