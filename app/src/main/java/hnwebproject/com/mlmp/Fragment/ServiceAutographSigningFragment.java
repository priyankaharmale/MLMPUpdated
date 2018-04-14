package hnwebproject.com.mlmp.Fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceAutographSigningFragment extends Fragment implements View.OnClickListener {

    private String TAG = ServiceAutographSigningFragment.class.getSimpleName();
    TextInputLayout input_event_name, input_company, input_location, input_address, input_date, input_audience, input_size_of_audience, input_ammount_of_time;
    TextInputLayout input_topic, input_budget;
    EditText et_event_name, et_company, et_location, et_address, et_audience, et_size_of_audience, et_topic, et_budget, et_ammount_of_time;
    Button btn_submit;
    EditText et_date;
    private String user_id, service_id;


    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public ServiceAutographSigningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_autograph_signing, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        getsaveData();

        input_event_name = (TextInputLayout) view.findViewById(R.id.input_event_name);
        input_company = (TextInputLayout) view.findViewById(R.id.input_company);
        input_location = (TextInputLayout) view.findViewById(R.id.input_location);
        input_address = (TextInputLayout) view.findViewById(R.id.input_address);
        input_date = (TextInputLayout) view.findViewById(R.id.input_date);
        input_audience = (TextInputLayout) view.findViewById(R.id.input_audience);
        input_size_of_audience = (TextInputLayout) view.findViewById(R.id.input_size_of_audience);
        input_topic = (TextInputLayout) view.findViewById(R.id.input_topic);
        input_budget = (TextInputLayout) view.findViewById(R.id.input_budget);
        input_ammount_of_time = (TextInputLayout) view.findViewById(R.id.input_ammount_of_time);

        et_event_name = (EditText) view.findViewById(R.id.et_event_name);
        et_company = (EditText) view.findViewById(R.id.et_company);
        et_location = (EditText) view.findViewById(R.id.et_location);
        et_address = (EditText) view.findViewById(R.id.et_address);
        et_date = (EditText) view.findViewById(R.id.et_date);
        et_audience = (EditText) view.findViewById(R.id.et_audience);
        et_size_of_audience = (EditText) view.findViewById(R.id.et_size_of_audience);
        et_topic = (EditText) view.findViewById(R.id.et_topic);
        et_budget = (EditText) view.findViewById(R.id.et_budget);
        et_ammount_of_time = (EditText) view.findViewById(R.id.et_ammount_of_time);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        et_date.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(getActivity())) {

                        String event_name = et_event_name.getText().toString();
                        String company = et_company.getText().toString();
                        String location = et_location.getText().toString();
                        String address = et_address.getText().toString();
                        String date = et_date.getText().toString();
                        String audience = et_audience.getText().toString();
                        String size_of_audience = et_size_of_audience.getText().toString();
                        String topic = et_topic.getText().toString();
                        String budget = et_budget.getText().toString();
                        String ammount_of_time = et_ammount_of_time.getText().toString();

                        submit(user_id, event_name, company, location, address, date, audience, size_of_audience, topic, budget, ammount_of_time, service_id);


                    } else {
                        Utils.myToast1(getActivity());
                    }
                }
                break;

            case R.id.et_date:

                datePicker_for_event_date(et_date);

                break;
        }
    }

    private void submit(final String user_id, final String event_name, final String company, final String location, final String address, final String date, final String audience, final String size_of_audience, final String topic, final String budget, final String ammount_of_time, final String service_id) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_SPEAKER_SERVICE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                et_event_name.setText("");
                                                et_company.setText("");
                                                et_location.setText("");
                                                et_address.setText("");
                                                et_date.setText("");
                                                et_audience.setText("");
                                                et_size_of_audience.setText("");
                                                et_topic.setText("");
                                                et_budget.setText("");
                                                et_ammount_of_time.setText("");

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
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

                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                        String reason = AppUtils.getVolleyError(getActivity(), error);
                        AlertUtility.showAlert(getActivity(), reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {

                    params.put("user_id", user_id);
                    params.put("service_id", service_id);
                    params.put("event_name", event_name);
                    params.put("company", company);
                    params.put("location", location);
                    params.put("address", address);
                    params.put("date", date);
                    params.put("audience", audience);
                    params.put("size_of_audience", size_of_audience);
                    params.put("topic", topic);
                    params.put("budget", budget);
                    params.put("amount_of_time", ammount_of_time);
                    params.put("gps_training", " ");
                    params.put("pick_tranning", " ");

                    Log.d("params_servi", "params is: \t" + params);


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

    void datePicker_for_event_date(final EditText tv_event_date) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day_date;
                        if (dayOfMonth < 10) {
                            day_date = "0" + dayOfMonth;
                        } else {
                            day_date = String.valueOf(dayOfMonth);
                        }
                        int day_d = Integer.parseInt(day_date);


                        int day_month = monthOfYear + 1;

                        if (day_month < 10) {
                            tv_event_date.setText("0" + day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        } else {
                            tv_event_date.setText(day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        }

                    }
                }, mYear, mMonth, mDay);

        //for disbling privious date

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();


    }

    private void getsaveData() {
        final SharedPreferences settings = getActivity().getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }

    private boolean checkValidation() {


        boolean ret = true;



        if (!Validations.hasText_input_layout(et_event_name, "Please Enter Event Name", input_event_name))
            ret = false;

        if (!Validations.hasText_input_layout(et_company, "Please Enter Comapny Name", input_company))
            ret = false;

        if (!Validations.hasText_input_layout(et_location, "Please Enter Location ", input_location))
            ret = false;

        if (!Validations.hasText_input_layout(et_address, "Please Enter Address", input_address))
            ret = false;

        if (!Validations.hasText_input_layout(et_ammount_of_time, "Please Enter Amount Time ", input_ammount_of_time))
            ret = false;
        if (!Validations.hasText_input_layout(et_date, "Please Select Event Date ", input_date))
            ret = false;
        if (!Validations.hasText_input_layout(et_audience, "Please Enter Audience ", input_audience))
            ret = false;
        if (!Validations.hasText_input_layout(et_size_of_audience, "Please Enter Size of Audience ", input_size_of_audience))
            ret = false;
        if (!Validations.hasText_input_layout(et_topic, "Please Enter Topic ", input_topic))
            ret = false;
        if (!Validations.hasText_input_layout(et_budget, "Please Enter Budget", input_budget))
            ret = false;
        return ret;
    }

}
