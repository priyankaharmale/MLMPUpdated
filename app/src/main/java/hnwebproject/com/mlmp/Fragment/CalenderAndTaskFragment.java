package hnwebproject.com.mlmp.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
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

import hnwebproject.com.mlmp.Activity.AddEventActivity;
import hnwebproject.com.mlmp.Activity.BusinessCardActivity;
import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Activity.EventListActivity;
import hnwebproject.com.mlmp.Activity.GetYearActivity;
import hnwebproject.com.mlmp.Activity.ViewPRofileActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.EventModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;
import hnwebproject.com.mlmp.calender.CalendarListener;
import hnwebproject.com.mlmp.calender.CustomCalendarView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

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
    EditText et_event_name, et_desc, et_address, et_email;
    TextInputLayout input_event_address, input_event_desc, input_event_name, input_layout_event_date, input_email;
    TextInputLayout input_task_name, input_notes, input_due_date;
    EditText et_task_name, et_notes;
    TextView tv_event_date, tv_due_date;
    String user_id, userEmail;
    ImageButton btn_add_to_do_task;
    ArrayList<EventModel> selected_Date_events;
    final int callbackId = 42;

    Button btn_inviteother;

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

        btn_inviteother = (Button) view.findViewById(R.id.btn_inviteother);

        btn_inviteother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  String message = " ";
                String messageTitle = isim + " Hello ";
                String messageBody = isim + " " + yurtIsmi + "\n" + " " + ulke_sehir + "\n" + " " + telNo + "\n" + " " + message;
               */
                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(getActivity());
                //dialogBuilder.setCancelable(true);

                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                final View dialogView1;
                dialogView1 = inflater1.inflate(R.layout.custom_emailadd_dailog, null);
                dialogBuilder1.setView(dialogView1);

                input_email = (TextInputLayout) dialogView1.findViewById(R.id.input_email);
                et_email = (EditText) dialogView1.findViewById(R.id.et_email);
                ImageView iv_cancle = (ImageView) dialogView1.findViewById(R.id.iv_cancle);

                iv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                    }
                });
                Button btn_add = (Button) dialogView1.findViewById(R.id.btn_submit);

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkValidationForEmail()) {

                            if (Utils.isNetworkAvailable(getActivity())) {
                                String emailTo = et_email.getText().toString();

                                //   String sms = messageSummary.getText().toString();
                                String subject = "Invite";
                                Intent email = new Intent();
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                                email.setData(Uri.parse("mailto:" + userEmail));

                              /*  email.putExtra(Intent.EXTRA_CC, new String[]{"xxxx@gmail.com"});
                                email.putExtra(Intent.EXTRA_BCC, new String[]{"xxx@gmail.com"});*/
                                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                                email.putExtra(Intent.EXTRA_TEXT, "https://drive.google.com/file/d/1yYr4TTLsuCz3Yk9CIyqQqtoJPEpG-NCX/view?usp=sharing");
                                email.setType("sms/rfc822");
                                //email.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 0);
                            } else {
                                Utils.myToast1(getActivity());
                            }
                        }
                    }
                });
                b = dialogBuilder1.create();
                b.show();

            }
        });
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

                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                String changed_date = mdformat.format(time.getTime());
                Log.d("changed_date", "" + changed_date);
                arraylistDate.clear();
                fetchEvents(user_id, changed_date, changed_date);
                //    customCalendarView.setBackgroundColorOfRedOrGreenNew(arraylistDate, todayDate, "UnAvailable");

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

/*

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
*/

                        Intent intent = new Intent(getContext(), AddEventActivity.class);
                        startActivity(intent);
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
                                    System.out.println("Datebdfdsf" + jsonObject.getString("event_date"));

                                }

                                System.out.println("ModelSize" + eventModels.size());
                                System.out.println("ArraySize" + arraylistDate.size());
                                customCalendarView.setBackgroundColorOfRedOrGreenNew(arraylistDate, date, "UnAvailable");


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
                                                checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
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

                    System.out.println("Event_Date" + date);
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

    private boolean checkValidationForEmail() {

        boolean ret = true;
        if (!Validations.hasText_input_layout(et_email, "Please Enter Email", input_email))
            ret = false;

        if (!Validations.isEmailAddress_input_layout(et_email, true, "Please Enter Valid Email ", input_email))
            ret = false;

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
                            tv_event_date.setText(year + "-" + "0" + day_month + "-" + day_date);//11-20-2017

                            //  2018-02-22

                            tv_event_date.setError(null);
                            Log.d("date12", tv_event_date.getText().toString());
                        } else {
                            // tv_event_date.setText(day_month + "/" + day_date + "/" + year);//11-20-2017
                            tv_event_date.setText(year + "-" + day_month + "-" + day_date);//11-20-2017

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
        userEmail = (settings.getString("email_address", ""));
    }


    public void getEventsList(ArrayList<EventModel> selected_Date_events) {
        System.out.println("Evnt" + selected_Date_events.size());


        if (selected_Date_events.size() == 0) {
            Toast.makeText(getActivity(), "No Event", Toast.LENGTH_SHORT).show();

        } else {
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

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(getContext(), p) == PERMISSION_GRANTED;
           /* String event_name = et_event_name.getText().toString();
            String desc = et_desc.getText().toString();
            String address = et_address.getText().toString();
            String date = tv_event_date.getText().toString();
            addEventToGoogle(event_name,desc,address,date);*/
        }

        if (!permissions)
            ActivityCompat.requestPermissions((Activity) getContext(), permissionsId, callbackId);
    }

    public void addEvent(final String event_name, final String desc, final String address, final String date) {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_CALENDAR)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        try {

                            int calenderId = -1;
                            String calenderEmaillAddress = "priyanka.harmale@gmail.com";
                            String[] projection = new String[]{
                                    CalendarContract.Calendars._ID,
                                    CalendarContract.Calendars.ACCOUNT_NAME};
                            ContentResolver cr = getActivity().getContentResolver();
                            Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                                    CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                                            CalendarContract.Calendars.NAME + "=? or " +
                                            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                                    new String[]{calenderEmaillAddress, calenderEmaillAddress,
                                            calenderEmaillAddress}, null);

                            if (cursor.moveToFirst()) {

                                if (cursor.getString(1).equals(calenderEmaillAddress)) {

                                    calenderId = cursor.getInt(0);
                                }
                            }
                            String myDate = date + "00:00:00";
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = formatter.parse(myDate);
                            long start2 = date.getTime();
                            long end2 = start2 + (24 * 60 * 60 * 1000); // For next 1hr
                          /*  long start2 = android.icu.util.Calendar.getInstance().getTimeInMillis(); // 2011-02-12 12h00
                            long end2 = android.icu.util.Calendar.getInstance().getTimeInMillis() + (4 * 60 * 60 * 1000); // 2011-02-12 13h00
*/
                            String title = event_name;

                            ContentValues cvEvent = new ContentValues();
                            cvEvent.put("calendar_id", calenderId);
                            cvEvent.put(CalendarContract.Events.TITLE, title);

                            cvEvent.put(CalendarContract.Events.DESCRIPTION, desc);
                            cvEvent.put(CalendarContract.Events.EVENT_LOCATION, address);


                            cvEvent.put("dtstart", start2);
                            cvEvent.put("hasAlarm", 1);
                            cvEvent.put("dtend", end2);

                            cvEvent.put("eventTimezone", TimeZone.getDefault().getID());


                            Uri uri = getActivity().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), cvEvent);


                            // get the event ID that is the last element in the Uri

                            long eventID = Long.parseLong(uri.getLastPathSegment());


                            ContentValues values = new ContentValues();

                            values.put(CalendarContract.Reminders.MINUTES, 2);
                            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);
                            cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
                            //Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String event_name = et_event_name.getText().toString();
        String desc = et_desc.getText().toString();
        String address = et_address.getText().toString();
        String date = tv_event_date.getText().toString();
        addEvent(event_name, desc, address, date);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) { // Activity.RESULT_OK
            Toast.makeText(getActivity(), "Mail Send", Toast.LENGTH_LONG).show();
            b.dismiss();
            inviteOther(et_email.getText().toString());
        }

    }


    private void inviteOther(final String email) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_INVITE_OTHER,
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
                                                Intent i = new Intent(getActivity(), DashBoardActivity.class);
                                                startActivity(i);

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
                    params.put("email_id", email);
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
