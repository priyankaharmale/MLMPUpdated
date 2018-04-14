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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

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
    String[] title = {
            "1",
            "2",
            "3",
            "4",
            "6",
            "7",
            "8",
            "9",
            "10+",
    };
    ArrayList<String> count;

    public ProductAdapter(ArrayList<ProductModel> productModels, FragmentActivity activity, String user_id, String addtocart) {

        this.productModelArrayList = productModels;
        this.context = activity;
        this.user_id = user_id;
        this.addtocart = addtocart;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_product_name, tv_product_price, tv_add_to_cart;
        public EditText tv_count;
        public ImageView iv_product, iv_delete;
        public ProgressBar progress_item;
        public LinearLayout lay_frnd;

        public MyViewHolder(View view) {
            super(view);
            textView_product_name = (TextView) view.findViewById(R.id.tv_item_product_name);
            tv_add_to_cart = (TextView) view.findViewById(R.id.tv_add_to_cart);
            iv_product = (ImageView) view.findViewById(R.id.iv_product);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            tv_product_price = (TextView) view.findViewById(R.id.tv_item_product_price);
            tv_count = (EditText) view.findViewById(R.id.tv_count);
            lay_frnd = (LinearLayout) view.findViewById(R.id.lay);
            progress_item = (ProgressBar) view.findViewById(R.id.progress_item);
            drawable = ContextCompat.getDrawable(context, R.drawable.l);

        }
    }


    public ProductAdapter(ArrayList<ProductModel> productModelArrayList, Context context, String user_id, String addtocart, OnCallBack onCallBack) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;
        this.user_id = user_id;
        this.addtocart = addtocart;
        this.onCallBack = onCallBack;

    }


    public ProductAdapter(ArrayList<ProductModel> productModelArrayList, Context context, String user_id, String addtocart, String onCallBack1, OnCallBack onCallBack) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;
        this.user_id = user_id;
        this.addtocart = addtocart;
        this.onCallBack1 = onCallBack1;
        this.onCallBack = onCallBack;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ProductModel productModel = productModelArrayList.get(position);

        if (addtocart.equals("1")) {
            holder.tv_add_to_cart.setVisibility(View.GONE);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.iv_delete.setVisibility(View.VISIBLE);
           /* if(productModel.getProduct_count().equals("10+"))
            {
                holder.tv_count.setEnabled(true);      }
            else
            {
                holder.tv_count.setEnabled(false);      }*/
        } else {
            holder.tv_add_to_cart.setVisibility(View.VISIBLE);
            holder.tv_count.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.GONE);

        }


        holder.textView_product_name.setText(productModel.getProduct_name());
        holder.tv_product_price.setText(productModel.getPrice());
        holder.tv_count.setText(productModel.getProduct_count());
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

        holder.tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_id = productModel.getProduct_id();
                Log.e("product_id", product_id);
                Log.e("user_id", user_id);
                addToShoppingCart(product_id, user_id);
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_id = productModel.getProduct_id();
                removedToShoppingCart(product_id, user_id);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent2 = new Intent(context, ProductDetailsActivity.class);
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("productModel", productModel);
                intent2.putExtras(bundleObject);
                context.startActivity(intent2);

            }
        });
        holder.tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdialog(productModel.getProduct_id());
            }
        });
    }

    private void addToShoppingCart(final String product_id, final String user_id) {


        myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_ADD_PRODUCT_CART,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        Log.d("res_events", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //getCountData(user_id);
                                                Intent intent= new Intent(context,DashBoardActivity.class);
                                                context.startActivity(intent);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                message = j.getString("message");

                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        String reason = AppUtils.getVolleyError(context, error);
                        AlertUtility.showAlert(context, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_PRODUCT_ID, product_id);
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_QUANTITY, "1");

                } catch (Exception e) {
                    System.out.println("error" + e.toString());
                }
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }


    private void removedToShoppingCart(final String product_id, final String user_id) {


        myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_REMOVE_PRODUCT_CART,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        Log.d("res_events", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");
                            if (message_code == 1) {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                deleteReferesh();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                message = j.getString("message");
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        String reason = AppUtils.getVolleyError(context, error);
                        AlertUtility.showAlert(context, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_PRODUCT_ID, product_id);

                    params.put(AppConstant.KEY_USERID, user_id);

                } catch (Exception e) {
                    System.out.println("error" + e.toString());
                }
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void deleteReferesh() {
        onCallBack.callback();
    }

    private void reload() {
        onCallBack.fetchCount();
    }

    public void countdialog(final String product_id) {
        count = new ArrayList<>();

        for (int i = 0; i < title.length; i++) {

            count.add(title[i]);
        }

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_list);
        final RecyclerView recycler_view = (RecyclerView) dialog.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManagerGPS = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManagerGPS);
        CountAdapter adapter = new CountAdapter(count, context, user_id, product_id, onCallBack, dialog);
        recycler_view.setAdapter(adapter);
        dialog.show();
        ImageView declineButton = (ImageView) dialog.findViewById(R.id.iv_cross);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void getCountData(final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_ADDTOCART_PRODUCT_COUNT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();


                        Log.d(TAG, "res_ViewPRofile" + response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");

                            if (message_code == 1) {

                                String count = j.getString("response");
                                // tv_count.setText(count);

                            } else {
                                String count = j.getString("response");

                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                                builder.setMessage(count)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        myDialog.dismiss();

                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        String reason = AppUtils.getVolleyError(context, error);
                        AlertUtility.showAlert(context, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {

                    params.put(AppConstant.KEY_USERID, user_id);

                } catch (Exception e) {
                    System.out.println("error" + e.toString());
                }


                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
