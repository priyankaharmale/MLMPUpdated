package hnwebproject.com.mlmp.Fragment;


import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Adapter.ContactAdapter;
import hnwebproject.com.mlmp.Model.ContactModel;
import hnwebproject.com.mlmp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    TextView tv_sort_by;
    RecyclerView recycler_view;
    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view=inflater.inflate(R.layout.fragment_contacts, container, false);

        initView(view);
        getContactList();
        return view;
    }

    private void initView(View view) {
        tv_sort_by = (TextView) view.findViewById(R.id.tv_sort_by);
        tv_sort_by.setPaintFlags(tv_sort_by.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(mLayoutManager);
    }

    private void getContactList() {
        ArrayList<ContactModel>contactModels=new ArrayList<>();

       Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.sllider_menu_avtar);
        ContactModel contactModel=new ContactModel("12","Mike Taylor","");
        ContactModel contactModel2=new ContactModel("13","Arron Boca","");

        for(int i=0;i<10;i++)
        {
            if(i%2==0){
                contactModels.add(contactModel);
            }else {
                contactModels.add(contactModel2);
            }

        }

        //Toast.makeText(getActivity(), ""+contactModels.size(), Toast.LENGTH_SHORT).show();

        ContactAdapter adapter=new ContactAdapter(contactModels,getActivity());
        recycler_view.setAdapter(adapter);

    }

}
