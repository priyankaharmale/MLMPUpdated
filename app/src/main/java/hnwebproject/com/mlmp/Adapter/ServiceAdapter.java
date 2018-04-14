package hnwebproject.com.mlmp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Fragment.ServiceAppearancesFragment;
import hnwebproject.com.mlmp.Fragment.ServiceAutographSigningFragment;
import hnwebproject.com.mlmp.Fragment.SpeakerDetailsFragment;
import hnwebproject.com.mlmp.Fragment.TrainingExperienceFragment;
import hnwebproject.com.mlmp.Model.ServiceModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 1/30/2018.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private ArrayList<ServiceModel> productModelArrayList;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_product_name;
        public View  line;
        public RelativeLayout lay;
        public MyViewHolder(View view) {
            super(view);
            textView_product_name = (TextView) view.findViewById(R.id.tv_item_product_name);
            lay = (RelativeLayout) view.findViewById(R.id.lay);
            line = (View) view.findViewById(R.id.line);
        }
    }


    public ServiceAdapter(ArrayList<ServiceModel> productModelArrayList, Context context) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ServiceModel productModel = productModelArrayList.get(position);

        holder.textView_product_name.setText(productModel.getServices());

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(productModel.getServices().equals("Speaker")) {
                   SpeakerDetailsFragment fragment = new SpeakerDetailsFragment();
                   fragment.setService_id(productModel.getService_id());
                   replaceFragment(fragment);
               }else if(productModel.getServices().equals("Training Experience")) {
                    TrainingExperienceFragment fragment = new TrainingExperienceFragment();
                    fragment.setService_id(productModel.getService_id());
                    replaceFragment(fragment);
                }else if( productModel.getServices().equals("Appearances")) {
                   ServiceAppearancesFragment fragment = new ServiceAppearancesFragment();
                    fragment.setService_id(productModel.getService_id());
                    replaceFragment(fragment);
                }else if( productModel.getServices().equals("Autograph signing")) {
                   ServiceAutographSigningFragment fragment = new ServiceAutographSigningFragment();
                   fragment.setService_id(productModel.getService_id());
                   replaceFragment(fragment);
               }
            }
        });
    }


    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    private void replaceFragment(Fragment fragment) {

        String backStateName = fragment.getClass().getName();
        FragmentManager manager = ((DashBoardActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }
}
