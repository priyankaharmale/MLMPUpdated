package hnwebproject.com.mlmp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hnwebproject.com.mlmp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailsFragment extends Fragment {


    public ContactDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_details, container, false);

        return view;
    }

}
