package hnwebproject.com.mlmp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Fragment.GPSForLifeFragment;
import hnwebproject.com.mlmp.Model.GpsModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 2/7/2018.
 */

public class GPS_FOR_Adapter extends RecyclerView.Adapter<GPS_FOR_Adapter.MyViewHolder> {

private ArrayList<GpsModel> productModelArrayList;
        Context context;



public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_gps_for;
    public View line;

    public MyViewHolder(View view) {
        super(view);
        tv_gps_for = (TextView) view.findViewById(R.id.tv_gps_for);
        line = (View) view.findViewById(R.id.line);

    }
}


    public GPS_FOR_Adapter(ArrayList<GpsModel> productModelArrayList, Context context) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gps_for, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final GpsModel productModel = productModelArrayList.get(position);

       holder.tv_gps_for.setText(productModel.getGps_name());
        holder.tv_gps_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.KEY_GPS_FOR, productModel.getGps_name());

                Fragment fragment = new GPSForLifeFragment();
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
        return productModelArrayList.size();
    }

}

