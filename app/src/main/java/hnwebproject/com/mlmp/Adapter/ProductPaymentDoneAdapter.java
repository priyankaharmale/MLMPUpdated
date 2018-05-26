package hnwebproject.com.mlmp.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Activity.ProductDetailsActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Fragment.ProductFragment;
import hnwebproject.com.mlmp.Interface.OnCallBack;
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


/**
 * Created by hnwebmarketing on 1/27/2018.
 */

public class ProductPaymentDoneAdapter extends RecyclerView.Adapter<ProductPaymentDoneAdapter.MyViewHolder> {

    private ArrayList<ProductModel> productModelArrayList;
    Context context;
    String TAG = DashBoardActivity.class.getSimpleName();

    Drawable drawable;
    ProductFragment fragment;
    ProgressDialog myDialog;
    private String user_id;
    String addtocart;
    OnCallBack onCallBack;
    String onCallBack1;

    ArrayList<String> count;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_product_name, tv_item_product_count;
        public ImageView iv_product, iv_delete;
        public ProgressBar progress_item;
        public LinearLayout lay_frnd;

        public MyViewHolder(View view) {
            super(view);
            textView_product_name = (TextView) view.findViewById(R.id.tv_item_product_name);
            tv_item_product_count = (TextView) view.findViewById(R.id.tv_item_product_count);
            progress_item = (ProgressBar) view.findViewById(R.id.progress_item);

            iv_product = (ImageView) view.findViewById(R.id.iv_product);
        }
    }


    public ProductPaymentDoneAdapter(ArrayList<ProductModel> productModelArrayList, Context context) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_paymentdone, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ProductModel productModel = productModelArrayList.get(position);

        holder.textView_product_name.setText(productModel.getProduct_name());
        holder.tv_item_product_count.setText(productModel.getProduct_count());
        try {
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
                    .into(holder.iv_product);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }





    }



    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }







}
