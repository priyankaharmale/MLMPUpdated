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
import hnwebproject.com.mlmp.Model.TrainingModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 2/7/2018.
 */

public class TrainingCentreAdapter extends RecyclerView.Adapter<TrainingCentreAdapter.MyViewHolder> {

    private ArrayList<TrainingModel> trainingModels;
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

         /*   tv_product_price = (TextView) view.findViewById(R.id.tv_item_product_price);
            lay_frnd = (LinearLayout) view.findViewById(R.id.lay);
            progress_item = (ProgressBar) view.findViewById(R.id.progress_item);*/
            drawable = ContextCompat.getDrawable(context, R.drawable.l);

        }
    }


    public TrainingCentreAdapter(ArrayList<TrainingModel> productModelArrayList, Context context) {
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

        final TrainingModel productModel = trainingModels.get(position);


        holder.tv_training.setText(productModel.getTraining_center_experties());
        if (productModel.getTraining_center_experties().equals("red")) {

            holder.iv_training.setBackgroundResource(R.drawable.red_belt_pic);

        } else if (productModel.getTraining_center_experties().equals("black")) {
            holder.iv_training.setBackgroundResource(R.drawable.black_belt_pic);
        } else if (productModel.getTraining_center_experties().equals("white")) {
            holder.iv_training.setBackgroundResource(R.drawable.white_belt_picture);
        }

        holder.iv_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.KEY_BELT, productModel.getTraining_center_experties());

                Fragment fragment = new TrainingBeltDetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((DashBoardActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });
       /* try {
            Glide.with(context)
                    .load(productModel.getProduct_image())
                    .error(drawable)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_training);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }*/


      /*  holder.lay_frnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new ProductFragment();
                replaceFragment(fragment);
            }
        });
*/
    }


    @Override
    public int getItemCount() {
        return trainingModels.size();
    }

/*
    private void replaceFragment(Fragment fragment) {

        String backStateName = fragment.getClass().getName();
        FragmentManager manager = ((DashBoardActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }*/
}

