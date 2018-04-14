package hnwebproject.com.mlmp.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {

    RecyclerView recycler_view;
    LinearLayout lay_shopping_cart;
    private String user_id;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product2, container, false);
        initView(view);
        getsaveData();
        return view;
    }

    private void initView(View view) {
        lay_shopping_cart = (LinearLayout) view.findViewById(R.id.lay_shopping_cart);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(mLayoutManager);

        lay_shopping_cart.setOnClickListener(this);


        if (Utils.isNetworkAvailable(getActivity())) {
            getProduct();

        } else {
            Utils.myToast1(getActivity());
        }
    }

    private void getProduct() {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_PRODUCTS,
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
                                        productModel.setDescription(jsonObject.getString("description"));
                                        productModels.add(productModel);
                                    }
                                }
                                ProductAdapter adapter = new ProductAdapter(productModels, getActivity(), user_id,"0");
                                recycler_view.setAdapter(adapter);
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                        String reason = AppUtils.getVolleyError(getActivity(), error);
                        AlertUtility.showAlert(getActivity(), reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {


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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_shopping_cart:

                break;
        }
    }

    private void getsaveData() {
        final SharedPreferences settings = getActivity().getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
        System.out.println("user_id"+user_id);
    }
}
