package hnwebproject.com.mlmp.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Interface.OnCallBack;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


/**
 * Created by hnwebmarketing on 2/7/2018.
 */

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.MyViewHolder> {

    private ArrayList<String> countArraylist;
    Context context;
    ProgressDialog myDialog;
    OnCallBack onCallBack;
    Dialog dialog;
    private String user_id, product_id;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_count;

        public MyViewHolder(View view) {
            super(view);
            tv_count = (TextView) view.findViewById(R.id.tv_count);

        }
    }

    public CountAdapter(ArrayList<String> productModelArrayList, Context context, String user_id, String product_id, OnCallBack onCallBack, Dialog dialog) {
        this.countArraylist = productModelArrayList;
        this.context = context;
        this.user_id = user_id;
        this.product_id = product_id;
        this.onCallBack = onCallBack;
        this.dialog = dialog;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptor_count, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final String count = countArraylist.get(position);
        holder.tv_count.setText(count);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToShoppingCart(count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countArraylist.size();
    }

    private void addToShoppingCart(final String count) {


        System.out.println("Count..."+count);

        myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_ADDTOCART_PRODUCT_INCRDESC,
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
                                            public void onClick(DialogInterface dialog1, int id) {

                                                dialog.dismiss();
                                                referesh();

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
                    params.put(AppConstant.KEY_QUANTITY, count);

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

    private void referesh() {
        onCallBack.callback();
    }
    private void reload() {
        onCallBack.fetchCount();
    }

}

