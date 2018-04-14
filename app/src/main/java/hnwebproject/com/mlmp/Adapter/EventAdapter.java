package hnwebproject.com.mlmp.Adapter;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Activity.EventListActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.EventModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;


/**
 * Created by hnwebmarketing on 1/30/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private ArrayList<EventModel> productModelArrayList;
    Context context;
    AlertDialog b;
    ArrayList<EventModel> eventModels;

    TextInputLayout input_event_address, input_event_desc, input_event_name, input_layout_event_date;
    EditText et_event_name, et_desc, et_address;
    TextView tv_event_date, tv_due_date;
    private int mYear, mMonth, mDay;
    AlertDialog.Builder dialogBuilder1;
    String userId;

    String TAG = "Test";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_event_name, tv_event_adress, tv_event_desc, tv_event_dated;
        ImageButton ib_cal_right;

        public View line;
        public RelativeLayout lay;

        public MyViewHolder(View view) {
            super(view);
            tv_event_name = (TextView) view.findViewById(R.id.tv_event_name);
            tv_event_adress = (TextView) view.findViewById(R.id.tv_event_adress);
            tv_event_desc = (TextView) view.findViewById(R.id.tv_event_desc);
            tv_event_dated = (TextView) view.findViewById(R.id.tv_event_date);
            ib_cal_right = (ImageButton) view.findViewById(R.id.ib_cal_right);
        }
    }


    public EventAdapter(ArrayList<EventModel> productModelArrayList, Context context) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptorcustom_dialog, parent, false);
        getSaveData();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final EventModel model = productModelArrayList.get(position);
        holder.tv_event_name.setText(model.getEvent_name());
        holder.tv_event_dated.setText(model.getEvent_date());
        holder.tv_event_adress.setText(model.getAddress());
        holder.tv_event_desc.setText(model.getDescription());

        holder.ib_cal_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialogBuilder1= new AlertDialog.Builder(context);
                dialogBuilder1.setTitle("Add Event");
                //dialogBuilder.setCancelable(true);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView1 = inflater.inflate(R.layout.add_event_layout, null);
                dialogBuilder1.setView(dialogView1);
                input_event_address = (TextInputLayout) dialogView1.findViewById(R.id.input_event_address);
                input_event_desc = (TextInputLayout) dialogView1.findViewById(R.id.input_event_desc);
                input_event_name = (TextInputLayout) dialogView1.findViewById(R.id.input_event_name);
                input_layout_event_date = (TextInputLayout) dialogView1.findViewById(R.id.input_layout_event_date);

                et_event_name = (EditText) dialogView1.findViewById(R.id.et_event_name);
                et_desc = (EditText) dialogView1.findViewById(R.id.et_event_desc);
                et_address = (EditText) dialogView1.findViewById(R.id.et_event_address);
                tv_event_date = (TextView) dialogView1.findViewById(R.id.tv_event_date);
                Button btn_add = (Button) dialogView1.findViewById(R.id.btn_add);


                et_desc.setText(model.getDescription());
                et_address.setText(model.getAddress());
                tv_event_date.setText(model.getEvent_date());
                et_event_name.setText(model.getEvent_name());


                btn_add.setText("Edit");
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkValidation()) {

                            if (Utils.isNetworkAvailable(context)) {

                                String event_name = et_event_name.getText().toString();
                                String desc = et_desc.getText().toString();
                                String address = et_address.getText().toString();
                                String date = tv_event_date.getText().toString();

                                editEvent(event_name, desc, address, model.getEvent_id(), date,model.getEvent_date());
                            } else {
                                Utils.myToast1(context);
                            }

                        }
                    }
                });


                tv_event_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker_for_event_date(tv_event_date);
                    }
                });

                b = dialogBuilder1.create();
                b.show();

            }
        });


    }


    public void editEvent(final String eventName, final String desc, final String address, final String eventId, final String date,final  String event_date) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.EDIT_EVENT,
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
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                b.dismiss();
                                        getDatewiseEventList(event_date);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                        String reason = AppUtils.getVolleyError(context, error);
                        AlertUtility.showAlert(context, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_EVENTID, eventId);
                    params.put(AppConstant.KEY_EVENT_DATE, date);
                    params.put(AppConstant.KEY_EVENT_NAME, eventName);
                    params.put(AppConstant.KEY_EVENT_DESCRIPTION, desc);
                    params.put(AppConstant.KEY_EVENT_ADDRESS, address);
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

    void datePicker_for_event_date(final TextView tv_event_date) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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
/*

                        if (day_month < 10) {
                            tv_event_date.setText("0" + day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        } else {
                            tv_event_date.setText(day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        }
*/


                        if (day_month < 10) {
                            //  tv_event_date.setText("0" + day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setText(year+ "-"+ "0" + day_month+ "-" + day_date);//11-20-2017

                            //  2018-02-22

                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        } else {
                            // tv_event_date.setText(day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setText(year+ "-"+ day_month+ "-" + day_date);//11-20-2017

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

    private boolean checkValidation() {

        boolean ret = true;

        if (!Validations.hasText_input_layout(et_event_name, "Please Enter  Event Name", input_event_name))
            ret = false;

        if (!Validations.hasText_input_layout(et_desc, "Please Enter Description", input_event_desc))
            ret = false;

        if (!Validations.hasText_input_layout(et_address, "Please Enter Address", input_event_address))
            ret = false;

        if (tv_event_date.getText().equals("Event Date")) {
            input_layout_event_date.setError("Select Event Date");
        }
        return ret;
    }


    public void getDatewiseEventList(final  String event_date) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(context, context.getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.GET_DATEWISEEVENT_,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d(TAG, "res_ViewPRofile" + response);

                        try {
                            eventModels = new ArrayList<>();

                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                message = j.getString("message");
                               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                b.dismiss();

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();*/

                                JSONArray jsonArray = j.getJSONArray("response");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    EventModel model = new EventModel();
                                    model.setEvent_id(jsonObject.getString("event_id"));
                                    model.setEvent_name(jsonObject.getString("event_name"));
                                    model.setAddress(jsonObject.getString("address"));
                                    model.setDescription(jsonObject.getString("description"));
                                    model.setEvent_date(jsonObject.getString("event_date"));
                                    eventModels.add(model);

                                    System.out.println("Datebdfdsf"+jsonObject.getString("event_date"));

                                }
                                Intent intent2 = new Intent(context, EventListActivity.class);


                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("EventList", eventModels);
                                intent2.putExtras(bundleObject);
                                // intent2.putExtra("EventList", selected_Date_events);
                                context.startActivity(intent2);
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                        String reason = AppUtils.getVolleyError(context, error);
                        AlertUtility.showAlert(context, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_EVENT_DATE, event_date);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void getSaveData() {
        final SharedPreferences settings = context.getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
       /* name = settings.getString("full_name", "");
        email = settings.getString("email_address", "");*/
        userId = settings.getString("user_id", "");
       /* profile_photo = settings.getString("profile_photo", "");
        login_through = settings.getString("login_through", "");*/
       System.out.println("USER_ID" +userId);
    }


}
