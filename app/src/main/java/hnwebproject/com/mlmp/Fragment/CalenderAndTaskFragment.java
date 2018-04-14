package hnwebproject.com.mlmp.Fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import hnwebproject.com.mlmp.calender.CalendarListener;
import hnwebproject.com.mlmp.calender.CustomCalendarView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalenderAndTaskFragment extends Fragment implements View.OnClickListener {

    String TAG = CalenderAndTaskFragment.class.getSimpleName();
    MaterialCalendarView simpleCalendarView;
    CustomCalendarView customCalendarView;
    ImageButton ib_cal_right;
    FloatingActionButton ib_plus;
    AlertDialog b;
    private int mYear, mMonth, mDay;
    String todayDate;
    TextView tv_event_name, tv_event_adress, tv_event_desc, tv_event_dated;
    ArrayList<EventModel> eventModels;
    ArrayList<String> arraylistDate;
    ProgressDialog myDialog;
    EditText et_event_name, et_desc, et_address;
    TextInputLayout input_event_address, input_event_desc, input_event_name, input_layout_event_date;
    TextInputLayout input_task_name, input_notes, input_due_date;
    EditText et_task_name, et_notes;
    TextView tv_event_date, tv_due_date;
    String user_id;
    ImageButton btn_add_to_do_task;
    ArrayList<EventModel> selected_Date_events;

    public CalenderAndTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender_and_task, container, false);
        getsaveData();
        initView(view);
        return view;
    }

    private void initView(View view) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");

        todayDate = mdformat.format(calendar.getTime());
        Log.d("strDate", "" + todayDate);

        fetchEvents(user_id, todayDate, todayDate);

        tv_event_name = (TextView) view.findViewById(R.id.tv_event_name);
        tv_event_adress = (TextView) view.findViewById(R.id.tv_event_adress);
        tv_event_desc = (TextView) view.findViewById(R.id.tv_event_desc);
        tv_event_dated = (TextView) view.findViewById(R.id.tv_event_date);

        input_task_name = (TextInputLayout) view.findViewById(R.id.input_task_name);
        input_notes = (TextInputLayout) view.findViewById(R.id.input_notes);
        input_due_date = (TextInputLayout) view.findViewById(R.id.input_due_date);

        btn_add_to_do_task = (ImageButton) view.findViewById(R.id.btn_add_to_do_task);
        btn_add_to_do_task.setOnClickListener(this);

        et_task_name = (EditText) view.findViewById(R.id.et_task_name);
        et_notes = (EditText) view.findViewById(R.id.et_notes);
        tv_due_date = (TextView) view.findViewById(R.id.tv_due_date);
        tv_due_date.setOnClickListener(this);

        ib_cal_right = (ImageButton) view.findViewById(R.id.ib_cal_right);
        ib_cal_right.setOnClickListener(this);

        ib_plus = (FloatingActionButton) view.findViewById(R.id.ib_plusadd);
        ib_plus.setOnClickListener(this);

        customCalendarView = (CustomCalendarView) view.findViewById(R.id.calendar_view);
        customCalendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {

                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                String changed_date = mdformat.format(date.getTime());
                Log.d("changed date ", "" + changed_date);
                // Toast.makeText(getActivity(), "" + changed_date, Toast.LENGTH_SHORT).show();

                getSelectedDateEvent(changed_date);

                customCalendarView.setBackgroundColorOfRedOrGreenNew(arraylistDate, todayDate, "UnAvailable");
            }

            @Override
            public void onMonthChanged(Date time) {
                String month = String.valueOf(time.getMonth());

                SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy");
                String changed_date = mdformat.format(time.getTime());
                Log.d("changed_date", "" + changed_date);
                arraylistDate.clear();
                fetchEvents(user_id, changed_date,changed_date);

                //Toast.makeText(getActivity(), "" + changed_date, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_cal_right:

                Toast.makeText(getActivity(), "next", Toast.LENGTH_SHORT).show();


                break;
            case R.id.ib_plusadd:

                ib_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(getActivity());
                        dialogBuilder1.setTitle("Add Event");
                        //dialogBuilder.setCancelable(true);

                        LayoutInflater inflater1 = getActivity().getLayoutInflater();
                        final View dialogView1 = inflater1.inflate(R.layout.add_event_layout, null);
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

                        btn_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkValidation()) {

                                    if (Utils.isNetworkAvailable(getActivity())) {

                                        String event_name = et_event_name.getText().toString();
                                        String desc = et_desc.getText().toString();
                                        String address = et_address.getText().toString();
                                        String date = tv_event_date.getText().toString();

                                        add_event(event_name, desc, address, user_id, date);
                                    } else {
                                        Utils.myToast1(getActivity());
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

                break;

            case R.id.btn_add_to_do_task:

                if (checkValidationForTask()) {

                    //   Toast.makeText(getActivity(), "to do pressed", Toast.LENGTH_SHORT).show();

                    if (Utils.isNetworkAvailable(getActivity())) {

                        String task_name = et_task_name.getText().toString();
                        String notes = et_notes.getText().toString();
                        String task_date = tv_due_date.getText().toString();

                        add_task(task_name, notes, task_date, user_id);
                    } else {
                        Utils.myToast1(getActivity());
                    }

                }
                break;

            case R.id.tv_due_date:

                datePicker_for_event_date(tv_due_date);

                break;
        }
    }

    private void fetchEvents(final String user_id, final String strDate, final String date) {


        myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_FETCH_EVENT,
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

                            eventModels = new ArrayList<>();
                            arraylistDate = new ArrayList<>();

                            if (message_code == 1) {
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

                                    arraylistDate.add(jsonObject.getString("event_date"));
                                    System.out.println("Datebdfdsf"+jsonObject.getString("event_date"));

                                }

                                System.out.println("ModelSize" + eventModels.size());
                                System.out.println("ArraySize" + arraylistDate.size());
                                customCalendarView.setBackgroundColorOfRedOrGreenNew(arraylistDate, todayDate, "UnAvailable");


                                //  getSelectedDateEvent(date);

                                // Toast.makeText(getActivity(), "" + eventModels.size(), Toast.LENGTH_SHORT).show();
                            } else {
                                message = j.getString("message");

                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                            /*    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();*/

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
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_DATE, strDate);

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

    private void getSelectedDateEvent(String todayDate) {
        selected_Date_events = new ArrayList<>();
        if (!(eventModels.isEmpty())) {
            for (int i = 0; i < eventModels.size(); i++) {
                EventModel model = eventModels.get(i);
                Log.d(TAG, "Event date: " + model.getEvent_date());
                if (model.getEvent_date().equals(todayDate)) {

                    Log.d(TAG, "selected Event date: " + model.getEvent_date());
                    selected_Date_events.add(model);


                } else {
                    // show date greater than today date
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("MM/dd/yyyy");
                    String todayDate1 = mdformat.format(calendar.getTime());
                }

            }
            //  Toast.makeText(getActivity(), "todayDate selected date events:\t " + selected_Date_events.size(), Toast.LENGTH_SHORT).show();
            // seteventdetails(selected_Date_events, todayDate);

        }
        getEventsList(selected_Date_events);
    }

    private void seteventdetails(ArrayList<EventModel> selected_Date_events, String todayDate) {


        if (!(selected_Date_events.isEmpty())) {
            for (int i = 0; i < selected_Date_events.size(); i++) {
                EventModel model = selected_Date_events.get(i);
                tv_event_name.setText(model.getEvent_name());
                tv_event_dated.setText(model.getEvent_date());
                tv_event_adress.setText(model.getAddress());
                tv_event_desc.setText(model.getDescription());
            }
        }

    }


    private void add_task(final String task_name, final String notes, final String task_date, final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_INSERT_TASK,
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                et_task_name.setText("");
                                                et_notes.setText("");
                                                tv_due_date.setText("");

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
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_TASK_DATE, task_date);
                    params.put(AppConstant.KEY_TASK_NAME, task_name);
                    params.put(AppConstant.KEY_TASK_NOTES, notes);

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

    private void add_event(final String event_name, final String desc, final String address, final String user_id, final String date) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_INSERT_EVENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("add_uni", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");

                            fetchEvents(user_id, todayDate, todayDate);

                            if (message_code == 1) {
                                message = j.getString("message");

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                b.dismiss();
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

                                                b.dismiss();
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
                    params.put(AppConstant.KEY_USERID, user_id);
                    params.put(AppConstant.KEY_EVENT_DATE, date);
                    params.put(AppConstant.KEY_EVENT_NAME, event_name);
                    params.put(AppConstant.KEY_EVENT_DESCRIPTION, desc);
                    params.put(AppConstant.KEY_EVENT_ADDRESS, address);

                    System.out.println("Event_Date" +date);
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

    private boolean checkValidationForTask() {

        boolean ret = true;

        if (!Validations.hasText_input_layout(et_task_name, "Please Enter Task Name", input_task_name))
            ret = false;

       /* if (!Validations.hasText_input_layout(et_desc, "Please Enter Description", input_event_desc))
            ret = false;*/

        if (tv_due_date.getText().equals("Due Date")) {
            input_due_date.setError("Select Due Date");
        }
        return ret;
    }


    void datePicker_for_event_date(final TextView tv_event_date) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

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

    private void getsaveData() {
        final SharedPreferences settings = getActivity().getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }


    public void getEventsList(ArrayList<EventModel> selected_Date_events) {
        System.out.println("Evnt"+ selected_Date_events.size());



        if(selected_Date_events.size()==0)
        {
            Toast.makeText(getActivity(),"No Event",Toast.LENGTH_SHORT).show();

        }
        else
        {
            //Toast.makeText(getActivity(),"Acivity",Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(getContext(), EventListActivity.class);


            Bundle bundleObject = new Bundle();
            bundleObject.putSerializable("EventList", selected_Date_events);
            intent2.putExtras(bundleObject);
            // intent2.putExtra("EventList", selected_Date_events);
            startActivity(intent2);
        }



            /*final Dialog dialog = new Dialog(getActivity());
            // Include dialog.xml file

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog);
            // Set dialog title
            // set values for custom dialog components - text, image and button
            final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            ImageView declineButton = (ImageView) dialog.findViewById(R.id.iv_cross);
            dialog.show();

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            EventAdapter adapter = new EventAdapter(selected_Date_events, getActivity());
            recyclerView.setAdapter(adapter);*/


        // if decline button is clicked, close the custom dialog

    }


}
