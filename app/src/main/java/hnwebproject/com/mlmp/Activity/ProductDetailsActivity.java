package hnwebproject.com.mlmp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


/**
 * Created by neha on 3/29/2018..
 */

public class ProductDetailsActivity extends AppCompatActivity {
    TextView tv_title, tv_descption, tv_grade, tv_copyversion, tv_price;
    ImageView iv_product;
    ProductModel productModel;
    Drawable drawable;
    ProgressBar progress_item;
    String TAG = ProductDetailsActivity.class.getSimpleName();

    Button btn_add_Tocart, btn_buyNow;
    String user_id;
    ProgressDialog myDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_add_Tocart = (Button) findViewById(R.id.btn_add_Tocart);
        btn_buyNow = (Button) findViewById(R.id.btn_buyNow);
        setSupportActionBar(toolbar);
        drawable = ContextCompat.getDrawable(ProductDetailsActivity.this, R.drawable.l);

        progress_item = (ProgressBar) findViewById(R.id.progress_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);

        getsaveData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("productModel")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                productModel = (ProductModel) bundle.getSerializable("productModel");
            } else {
                Log.e("null", "null");
            }
        }
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_descption = (TextView) findViewById(R.id.tv_descption);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_copyversion = (TextView) findViewById(R.id.tv_copyversion);
        tv_price = (TextView) findViewById(R.id.tv_price);

        iv_product = (ImageView) findViewById(R.id.iv_product);


        tv_title.setText(productModel.getProduct_name());
        tv_descption.setText(productModel.getDescription());
        tv_grade.setText(productModel.getGrade());
        tv_copyversion.setText(productModel.getCopy_version());
        tv_price.setText(productModel.getPrice());


        try {
            Glide.with(ProductDetailsActivity.this)
                    .load(productModel.getProduct_image())
                    .error(drawable)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress_item.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress_item.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(iv_product);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        btn_add_Tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToShoppingCart(productModel.getProduct_id(), user_id);
            }
        });
        btn_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productBuyNow(user_id);
            }
        });

    }

    private void addToShoppingCart(final String product_id, final String user_id) {


        myDialog = Utils.DialogsUtils.showProgressDialog(ProductDetailsActivity.this, getString(R.string.processing));

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
                                Toast.makeText(ProductDetailsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                message = j.getString("message");

                                Toast.makeText(ProductDetailsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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
                        String reason = AppUtils.getVolleyError(ProductDetailsActivity.this, error);
                        AlertUtility.showAlert(ProductDetailsActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
        System.out.println("user_id" + user_id);
    }


    private void productBuyNow(final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(ProductDetailsActivity.this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_PRODUCT_SINGLE_BUY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();


                        Log.d(TAG, "res_ViewPRofile" + response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");

                            String message = j.getString("message");
                            if (message_code == 1) {

                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductDetailsActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProductDetailsActivity.this);
                                builder.setMessage(message)
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
                        String reason = AppUtils.getVolleyError(ProductDetailsActivity.this, error);
                        AlertUtility.showAlert(ProductDetailsActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_PRODUCT_ID, productModel.getProduct_id());
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

}
