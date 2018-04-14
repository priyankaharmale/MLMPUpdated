package hnwebproject.com.mlmp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Fragment.ProductFragment;
import hnwebproject.com.mlmp.Fragment.TrainingBeltDetailsFragment;
import hnwebproject.com.mlmp.Model.TrainingServicesModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 2/7/2018.
 */

public class TrainingCentreServiceAdapter extends RecyclerView.Adapter<TrainingCentreServiceAdapter.MyViewHolder> {

    private ArrayList<TrainingServicesModel> trainingModels;
    Context context;
    Drawable drawable;
    ProductFragment fragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_training, tv_product_price;
        public ImageView iv_training;
        public ProgressBar progress_item;
        public LinearLayout lay_frnd;

        public MyViewHolder(View view) {
            super(view);
            tv_training = (TextView) view.findViewById(R.id.tv_training);
            iv_training = (ImageView) view.findViewById(R.id.iv_training);
            drawable = ContextCompat.getDrawable(context, R.drawable.l);
        }
    }

    public TrainingCentreServiceAdapter(ArrayList<TrainingServicesModel> productModelArrayList, Context context) {
        this.trainingModels = productModelArrayList;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainig_centre, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TrainingServicesModel productModel = trainingModels.get(position);
        holder.tv_training.setText(productModel.getPick_tranning());
        if (productModel.getPick_tranning().equals("Red Belt")) {
            holder.iv_training.setBackgroundResource(R.drawable.red_belt_pic);
        } else if (productModel.getPick_tranning().equals("1st Degree Black Belt")) {
            holder.iv_training.setBackgroundResource(R.drawable.black_belt_pic);
        } else if (productModel.getPick_tranning().equals("White Belt")) {
            holder.iv_training.setBackgroundResource(R.drawable.white_belt_picture);
        } else if (productModel.getPick_tranning().equals("2nd Degree Black Belt")) {
        holder.iv_training.setBackgroundResource(R.drawable.black_belt_pic);
    }
        holder.iv_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_BELT, productModel);

                Fragment fragment = new TrainingBeltDetailsFragment();
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

