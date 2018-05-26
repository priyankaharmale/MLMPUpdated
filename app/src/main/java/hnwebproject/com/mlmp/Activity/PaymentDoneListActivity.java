package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.ecommerce.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Adapter.EventAdapter;
import hnwebproject.com.mlmp.Adapter.ProductAdapter;
import hnwebproject.com.mlmp.Adapter.ProductPaymentDoneAdapter;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.EventModel;
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


/**
 * Created by neha on 3/10/2018..
 */

public class PaymentDoneListActivity extends AppCompatActivity {
    ArrayList<Product> products;
    ImageButton ib_edit;
    RecyclerView recyclerView;

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymnetdonelist);

         getsaveData();
        System.out.println("Hello");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ib_edit = (ImageButton) toolbar.findViewById(R.id.ib_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PaymentDoneListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        getProduct();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getProduct() {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_PAYMENTDONE_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_ViewPRofile", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");


                            if (message_code == 1) {
                                ArrayList<ProductModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        ProductModel productModel = new ProductModel();
                                        productModel.setProduct_id(jsonObject.getString("product_id"));
                                        productModel.setProduct_name(jsonObject.getString("product_name"));
                                        productModel.setProduct_status(jsonObject.getString("product_status"));
                                        productModel.setPrice(jsonObject.getString("price"));
                                        productModel.setProduct_image(jsonObject.getString("product_image"));
                                        productModel.setProduct_count(jsonObject.getString("noofproduct"));
                                        productModels.add(productModel);
                                    }
                                }
                                ProductPaymentDoneAdapter adapter = new ProductPaymentDoneAdapter(productModels, PaymentDoneListActivity.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                              String  message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentDoneListActivity.this);
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

                        String reason = AppUtils.getVolleyError(PaymentDoneListActivity.this, error);
                        AlertUtility.showAlert(PaymentDoneListActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentDoneListActivity.this);
        requestQueue.add(stringRequest);
    }
    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }

}
