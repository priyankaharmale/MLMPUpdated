package hnwebproject.com.mlmp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Fragment.OtherProgramDetailsFragment;
import hnwebproject.com.mlmp.Fragment.ProductFragment;
import hnwebproject.com.mlmp.Model.TrainingServicesModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 2/7/2018.
 */

public class TrainingCentreOtherServiceAdapter extends RecyclerView.Adapter<TrainingCentreOtherServiceAdapter.MyViewHolder> {

    private ArrayList<TrainingServicesModel> trainingModels;
    Context context;
    Drawable drawable;
    ProductFragment fragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_eventname, tv_location, tv_address, tv_date;
        public ProgressBar progress_item;
        public LinearLayout lay_frnd;

        public MyViewHolder(View view) {
            super(view);
            tv_eventname = (TextView) view.findViewById(R.id.tv_eventname);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }

    public TrainingCentreOtherServiceAdapter(ArrayList<TrainingServicesModel> productModelArrayList, Context context) {
        this.trainingModels = productModelArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptor_other_services, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TrainingServicesModel productModel = trainingModels.get(position);

        holder.tv_eventname.setText(productModel.getEvent_name());
        holder.tv_address.setText(productModel.getAddress());
        holder.tv_date.setText(productModel.getDate());
        holder.tv_location.setText(productModel.getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_BELT, productModel);

                Fragment fragment = new OtherProgramDetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((DashBoardActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

    }


    @Override
    public int getItemCount() {
        return trainingModels.size();
    }

}

