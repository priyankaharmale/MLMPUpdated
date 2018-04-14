package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;


public class GetYearActivity extends AppCompatActivity {

    EditText et_year;
    Button btn_next;
    String univercity, city;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_year);

        getsaveData();
        initView();


    }

    private void initView() {

        et_year = (EditText) findViewById(R.id.et_year);
        btn_next = (Button) findViewById(R.id.btn_next);
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        univercity = bundle.getString("Univercity");
        city = bundle.getString("City");
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidation()) {
                    if (Utils.isNetworkAvailable(GetYearActivity.this)) {

                        saveCityYearUni(univercity, city, et_year.getText().toString(), user_id);

                    } else {
                        Utils.myToast1(GetYearActivity.this);
                    }
                }
               /* Bundle bundle = new Bundle();
                bundle.putString("year", et_year.getText().toString());
                bundle.putString("Univercity", univercity);
                bundle.putString("City", city);
                i.putExtras(bundle);*/

            }
        });
    }


    private void saveCityYearUni(final String univercity, final String city, final String  year, final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_UPDATE_CITY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("add_uni", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                message = j.getString("message");

                                AlertDialog.Builder builder = new AlertDialog.Builder(GetYearActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(GetYearActivity.this, ViewPRofileActivity.class);
                                                startActivity(i);

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");

                                AlertDialog.Builder builder = new AlertDialog.Builder(GetYearActivity.this);
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

                        String reason = AppUtils.getVolleyError(GetYearActivity.this, error);
                        AlertUtility.showAlert(GetYearActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_YEAR, year);
                    params.put(AppConstant.KEY_UNIVERSITY, univercity);
                    params.put(AppConstant.KEY_CITY_NAME, city);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }

    private boolean checkValidation() {

        boolean ret = true;

        if (!Validations.hasText(et_year, "Please Enter Year"))
            ret = false;


        return ret;
    }
}
