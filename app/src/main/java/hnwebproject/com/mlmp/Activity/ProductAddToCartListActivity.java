package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Adapter.ProductAdapter;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Interface.OnCallBack;
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


/**
 * Created by neha on 3/10/2018..
 */


public class ProductAddToCartListActivity extends AppCompatActivity implements OnCallBack {
    RecyclerView recyclerView;
    String user_id;
    Button btn_buynow;
    String message;
    OnCallBack ml;
    TextView tv_noRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocartlist);
        ml = ProductAddToCartListActivity.this;

        btn_buynow = (Button) findViewById(R.id.btn_buynow);
        tv_noRecord = (TextView) findViewById(R.id.tv_norecord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductAddToCartListActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductAddToCartListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        getsaveData();
        getProduct();
        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productBuyNow();
            }
        });

    }


    public void getProduct() {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(ProductAddToCartListActivity.this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_ADDTOCART_PRODUCT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_ViewPRofile", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            message = j.getString("message");


                            if (message_code == 1) {
                                ArrayList<ProductModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ProductModel productModel = new ProductModel();
                                        productModel.setProduct_id(jsonObject.getString("product_id"));
                                        productModel.setProduct_category(jsonObject.getString("product_category"));
                                        productModel.setProduct_name(jsonObject.getString("product_name"));
                                        productModel.setCopy_version(jsonObject.getString("copy_version"));
                                        productModel.setGrade(jsonObject.getString("grade"));
                                        productModel.setProduct_status(jsonObject.getString("product_status"));
                                        productModel.setPrice(jsonObject.getString("price"));
                                        productModel.setProduct_image(jsonObject.getString("product_image"));
                                        productModel.setProduct_count(jsonObject.getString("quantity"));
                                        productModels.add(productModel);
                                    }
                                }
                                ProductAdapter adapter = new ProductAdapter(productModels, ProductAddToCartListActivity.this, user_id, "1", ml);
                                recyclerView.setAdapter(adapter);
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductAddToCartListActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (message.equals("Cart products not present")) {
                                                    tv_noRecord.setVisibility(View.VISIBLE);
                                                    recyclerView.setVisibility(View.GONE);
                                                    btn_buynow.setVisibility(View.GONE);
                                                } else {
                                                    tv_noRecord.setVisibility(View.GONE);
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    btn_buynow.setVisibility(View.VISIBLE);

                                                }
                                            }
                                        });
                                AlertDialog alert = builder.create();
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

                        String reason = AppUtils.getVolleyError(ProductAddToCartListActivity.this, error);
                        AlertUtility.showAlert(ProductAddToCartListActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductAddToCartListActivity.this);
        requestQueue.add(stringRequest);
    }

    public void getProductCount() {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(ProductAddToCartListActivity.this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_ADDTOCART_PRODUCT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_ViewPRofile", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                ArrayList<ProductModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ProductModel productModel = new ProductModel();
                                        productModel.setProduct_id(jsonObject.getString("product_id"));
                                        productModel.setProduct_category(jsonObject.getString("product_category"));
                                        productModel.setProduct_name(jsonObject.getString("product_name"));
                                        productModel.setCopy_version(jsonObject.getString("copy_version"));
                                        productModel.setGrade(jsonObject.getString("grade"));
                                        productModel.setProduct_status(jsonObject.getString("product_status"));
                                        productModel.setPrice(jsonObject.getString("price"));
                                        productModel.setProduct_image(jsonObject.getString("product_image"));
                                        productModel.setProduct_count(jsonObject.getString("quantity"));
                                        productModels.add(productModel);
                                    }
                                }
                                ProductAdapter adapter = new ProductAdapter(productModels, ProductAddToCartListActivity.this, user_id, "1", "1", ml);
                                recyclerView.setAdapter(adapter);
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductAddToCartListActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                            }
                                        });
                                AlertDialog alert = builder.create();
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

                        String reason = AppUtils.getVolleyError(ProductAddToCartListActivity.this, error);
                        AlertUtility.showAlert(ProductAddToCartListActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductAddToCartListActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
        System.out.println("user_id" + user_id);
    }

    @Override
    public void callback() {
        getProduct();
    }

    @Override
    public void fetchCount() {
        getProductCount();
    }


    public void productBuyNow() {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(ProductAddToCartListActivity.this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEy_PRODUCT_BUYNOW,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_ViewPRofile", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductAddToCartListActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                getProduct();

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                message = j.getString("message");
                                Toast.makeText(ProductAddToCartListActivity.this, message, Toast.LENGTH_SHORT).show();
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

                        String reason = AppUtils.getVolleyError(ProductAddToCartListActivity.this, error);
                        AlertUtility.showAlert(ProductAddToCartListActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(ProductAddToCartListActivity.this);
        requestQueue.add(stringRequest);
    }
}
