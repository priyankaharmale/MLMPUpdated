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

import hnwebproject.com.mlmp.Adapter.GPS_FOR_Adapter;
import hnwebproject.com.mlmp.Adapter.ProductAdapter;
import hnwebproject.com.mlmp.Adapter.TrainingCentreAdapter;
import hnwebproject.com.mlmp.Adapter.TrainingCentreOtherServiceAdapter;
import hnwebproject.com.mlmp.Adapter.TrainingCentreServiceAdapter;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.GpsModel;
import hnwebproject.com.mlmp.Model.ProductModel;
import hnwebproject.com.mlmp.Model.TrainingModel;
import hnwebproject.com.mlmp.Model.TrainingServicesModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingCentreFragment extends Fragment {

    RecyclerView recyclerView, recycler_view_gps, recycler_view_other;
    String userId;
    String[] title = {
            "Life",
            "Poetry",
            "Parenting",
            "Success",
            "Dummy"
    };

    ArrayList<GpsModel> gpsModels;

    public TrainingCentreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_training_centre, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_trining_centre);
        recycler_view_other = (RecyclerView) view.findViewById(R.id.recycler_view_other);
        recycler_view_gps = (RecyclerView) view.findViewById(R.id.recycler_view_gps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        gpsModels = new ArrayList<>();
        LinearLayoutManager layoutManagerGPS = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_gps.setLayoutManager(layoutManagerGPS);

        LinearLayoutManager layoutManagerother = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_other.setLayoutManager(layoutManagerother);

        /*for(int i=0;i<title.length;i++) {
            GpsModel gpsModel=new GpsModel();
            gpsModel.setGps_name(title[i]);
            gpsModels.add(gpsModel);

           // System.out.println(mStrings[i]);
        }*/


        getSaveData();

        // getTrainigCentre(userId);
        getProduct();
        getTrainigCentreBelt(userId);
        getTrainigCentreOtherServices(userId);
        // getGpsList();
    }

    private void getTrainigCentre(final String userId) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_TRAINING_CENTRE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        Log.d("res_training_centre", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");
                            if (message_code == 1) {
                                ArrayList<TrainingModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TrainingModel trainingModel = new TrainingModel();
                                        trainingModel.setTc_id(jsonObject.getString("tc_id"));
                                        trainingModel.setTraining_center_name(jsonObject.getString("training_center_name"));
                                        trainingModel.setTraining_center_experties(jsonObject.getString("training_center_experties"));
                                        productModels.add(trainingModel);
                                    }
                                }
                                TrainingCentreAdapter adapter = new TrainingCentreAdapter(productModels, getActivity());
                                recyclerView.setAdapter(adapter);
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
                    params.put(AppConstant.KEY_USERID, userId);
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


    private void getTrainigCentreBelt(final String userId) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_TRAINING_CENTREBELT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        Log.d("res_training_centre", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            if (message_code == 1) {
                                ArrayList<TrainingServicesModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TrainingServicesModel trainingModel = new TrainingServicesModel();

                                        trainingModel.setAddress(jsonObject.getString("address"));
                                        trainingModel.setAmount_of_time(jsonObject.getString("amount_of_time"));
                                        trainingModel.setAudience(jsonObject.getString("audience"));
                                        trainingModel.setBudget(jsonObject.getString("budget"));
                                        trainingModel.setCompany(jsonObject.getString("company"));
                                        trainingModel.setCreated_date(jsonObject.getString("created_date"));
                                        trainingModel.setDate(jsonObject.getString("date"));
                                        trainingModel.setEvent_name(jsonObject.getString("event_name"));
                                        trainingModel.setGps_training(jsonObject.getString("gps_training"));
                                        trainingModel.setLocation(jsonObject.getString("location"));
                                        trainingModel.setPick_tranning(jsonObject.getString("pick_tranning"));
                                        trainingModel.setSer_dis_id(jsonObject.getString("ser_dis_id"));
                                        trainingModel.setService_id(jsonObject.getString("service_id"));
                                        trainingModel.setSize_of_audience(jsonObject.getString("size_of_audience"));
                                        trainingModel.setTopic(jsonObject.getString("topic"));
                                        trainingModel.setUser_id(jsonObject.getString("user_id"));

                                        productModels.add(trainingModel);
                                    }
                                }
                                TrainingCentreServiceAdapter adapter = new TrainingCentreServiceAdapter(productModels, getActivity());
                                recyclerView.setAdapter(adapter);
                            } else {
                                String message;

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
                    params.put(AppConstant.KEY_USERID, userId);
                    params.put(AppConstant.KEY_SERVICEID, "2");
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

    private void getTrainigCentreOtherServices(final String userId) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_OTHERTRAINING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        Log.d("res_training_centre", response);
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            if (message_code == 1) {
                                ArrayList<TrainingServicesModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TrainingServicesModel trainingModel = new TrainingServicesModel();

                                        trainingModel.setAddress(jsonObject.getString("address"));
                                        trainingModel.setAmount_of_time(jsonObject.getString("amount_of_time"));
                                        trainingModel.setAudience(jsonObject.getString("audience"));
                                        trainingModel.setBudget(jsonObject.getString("budget"));
                                        trainingModel.setCompany(jsonObject.getString("company"));
                                        trainingModel.setCreated_date(jsonObject.getString("created_date"));
                                        trainingModel.setDate(jsonObject.getString("date"));
                                        trainingModel.setEvent_name(jsonObject.getString("event_name"));
                                        trainingModel.setGps_training(jsonObject.getString("gps_training"));
                                        trainingModel.setLocation(jsonObject.getString("location"));
                                        trainingModel.setPick_tranning(jsonObject.getString("pick_tranning"));
                                        trainingModel.setSer_dis_id(jsonObject.getString("ser_dis_id"));
                                        trainingModel.setService_id(jsonObject.getString("service_id"));
                                        trainingModel.setSize_of_audience(jsonObject.getString("size_of_audience"));
                                        trainingModel.setTopic(jsonObject.getString("topic"));
                                        trainingModel.setUser_id(jsonObject.getString("user_id"));

                                        productModels.add(trainingModel);
                                    }
                                }
                                TrainingCentreOtherServiceAdapter adapter = new TrainingCentreOtherServiceAdapter(productModels, getActivity());
                                recycler_view_other.setAdapter(adapter);
                            } else {
                                String message;

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
                    params.put(AppConstant.KEY_USERID, userId);
                    params.put(AppConstant.KEY_SERVICEID, "2");
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

    private void getSaveData() {
        final SharedPreferences settings = getActivity().getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        userId = settings.getString("user_id", "");
    }

    private void getGpsList() {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GPS_FOR_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_gps", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                ArrayList<TrainingModel> productModels = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        GpsModel gpsModel = new GpsModel();
                                        gpsModel.setGps_id(jsonObject.getString("category_id"));
                                        gpsModel.setGps_name(jsonObject.getString("category_name"));
                                        gpsModels.add(gpsModel);
                                    }
                                }

                                GPS_FOR_Adapter adapter = new GPS_FOR_Adapter(gpsModels, getActivity());
                                recycler_view_gps.setAdapter(adapter);


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
                });


        ;

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
                                ProductAdapter adapter = new ProductAdapter(productModels, getActivity(), userId, "0");
                                recycler_view_gps.setAdapter(adapter);
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

}
