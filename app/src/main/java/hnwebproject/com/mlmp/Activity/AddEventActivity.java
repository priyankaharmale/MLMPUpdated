package hnwebproject.com.mlmp.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.EventModel;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by PC-21 on 18-Apr-18.
 */

public class AddEventActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText et_event_name, et_desc, et_address;
    TextView tv_event_dated;
    private int mYear, mMonth, mDay;
    String user_id;
    final int callbackId = 42;
    ProgressDialog myDialog;
    ArrayList<EventModel> eventModels;
    ArrayList<String> arraylistDate;
    String todayDate;
    AlertDialog b;
    String email_username;
    String CLASSTAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    TextInputLayout input_event_address, input_event_desc, input_event_name, input_layout_event_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);

        getsaveData();
        input_event_address = (TextInputLayout) findViewById(R.id.input_event_address);
        input_event_desc = (TextInputLayout) findViewById(R.id.input_event_desc);
        input_event_name = (TextInputLayout) findViewById(R.id.input_event_name);
        input_layout_event_date = (TextInputLayout) findViewById(R.id.input_layout_event_date);

        et_event_name = (EditText) findViewById(R.id.et_event_name);
        et_desc = (EditText) findViewById(R.id.et_event_desc);
        et_address = (EditText) findViewById(R.id.et_event_address);
        tv_event_dated = (TextView) findViewById(R.id.tv_event_date);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(AddEventActivity.this)) {

                        String event_name = et_event_name.getText().toString();
                        String desc = et_desc.getText().toString();
                        String address = et_address.getText().toString();
                        String date = tv_event_dated.getText().toString();

                        add_event(event_name, desc, address, user_id, date);
                    } else {
                        Utils.myToast1(AddEventActivity.this);
                    }

                }
            }
        });


        tv_event_dated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker_for_event_date(tv_event_dated);
            }
        });
    }

    private boolean checkValidation() {

        boolean ret = true;

        if (!Validations.hasText_input_layout(et_event_name, "Please Enter  Event Name", input_event_name))
            ret = false;

        if (!Validations.hasText_input_layout(et_desc, "Please Enter Description", input_event_desc))
            ret = false;

        if (!Validations.hasText_input_layout(et_address, "Please Enter Address", input_event_address))
            ret = false;

        if (tv_event_dated.getText().equals("Event Date")) {
            input_layout_event_date.setError("Select Event Date");
        }
        return ret;
    }

    void datePicker_for_event_date(final TextView tv_event_date) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
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

    private void add_event(final String event_name, final String desc, final String address, final String user_id, final String date) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(AddEventActivity.this, getString(R.string.processing));
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


                            if (message_code == 1) {
                                message = j.getString("message");

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  b.dismiss();
                                               /* checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
                                                onBackPressed();*/
                                                chooseaccountdialog();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                // b.dismiss();
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

                        String reason = AppUtils.getVolleyError(AddEventActivity.this, error);
                        AlertUtility.showAlert(AddEventActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddEventActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }

    private void checkPermissions(int callbackId, String email_usernam, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(AddEventActivity.this, p) == PERMISSION_GRANTED;

        }
      /*  String event_name = et_event_name.getText().toString();
        String desc = et_desc.getText().toString();
        String address = et_address.getText().toString();
        String date = tv_event_dated.getText().toString();
        addEventToGoogle(event_name, desc, address, date, email_usernam);*/
        if (!permissions) {
            ActivityCompat.requestPermissions((Activity) AddEventActivity.this, permissionsId, callbackId);
        }
        else
        {
             String event_name = et_event_name.getText().toString();
        String desc = et_desc.getText().toString();
        String address = et_address.getText().toString();
        String date = tv_event_dated.getText().toString();
        addEventToGoogle(event_name, desc, address, date, email_usernam);
        }
    }

    public void addEventToGoogle(final String event_name, final String desc, final String address, final String date, final String email_usernam) {
        Dexter.withActivity(AddEventActivity.this)
                .withPermission(Manifest.permission.WRITE_CALENDAR)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        try {

                            int calenderId = -1;
                            //    String calenderEmaillAddress = "mike222taylor@gmail.com";
                            String[] projection = new String[]{
                                    CalendarContract.Calendars._ID,
                                    CalendarContract.Calendars.ACCOUNT_NAME};
                            ContentResolver cr = getContentResolver();
                            Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                                    CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                                            CalendarContract.Calendars.NAME + "=? or " +
                                            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                                    new String[]{email_usernam, email_usernam,
                                            email_usernam}, null);

                            if (cursor.moveToFirst()) {

                                if (cursor.getString(1).equals(email_usernam)) {

                                    calenderId = cursor.getInt(0);
                                }
                            }
                            String myDate = date + " " + "00:00:00";
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


                            Uri uri = getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), cvEvent);


                            // get the event ID that is the last element in the Uri

                            long eventID = Long.parseLong(uri.getLastPathSegment());


                            ContentValues values = new ContentValues();

                            values.put(CalendarContract.Reminders.MINUTES, 2);
                            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);
                            cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
                            //Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
                            //       Toast.makeText(AddEventActivity.this, "Event Add to the Google account successfully", Toast.LENGTH_LONG).show();

                            b.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                            builder.setMessage("Event Add to the Google account successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                         /*   Intent i_login = new Intent(AddEventActivity.this, .class);
                                            startActivity(i_login);
                                           */
                                            /*Fragment fragment = new GPSForLifeFragment();
                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_frame, fragment);
                                            ft.addToBackStack(fragment.getClass().getName());
                                            ft.commit();*/

                                            Intent intent = new Intent(AddEventActivity.this, DashBoardActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
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
        String date = tv_event_dated.getText().toString();
        if(requestCode==42)
        {
            checkPermissions(callbackId, email_username, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

        }
     //   addEventToGoogle(event_name, desc, address, date, email_username);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //updateUI(false);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result to the Twitter login button.
        // mLoginButton.onActivityResult(requestCode, resultCode, data);

        //client.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(CLASSTAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getGivenName();
            String full_name = acct.getDisplayName();
            Uri personPhotoUrl = acct.getPhotoUrl();
            try {
                if (personPhotoUrl.equals(null)) {

                } else {
                    Log.e(CLASSTAG, " image " + personPhotoUrl.toString());
                }
            } catch (Exception e) {
                Log.e(CLASSTAG, e.getMessage());
            }
            email_username = acct.getEmail();

            String google_ids = acct.getId();
            Log.e(CLASSTAG, "Name: " + personName + ", email: " + email_username + " IDs " + google_ids);


            String image_user = String.valueOf(personPhotoUrl);

           /* Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
*/
            Object register_token_id = "";

            checkPermissions(callbackId, email_username, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

            // LoginWithGooglePlus(google_ids, email_username, personName, image_user, register_token_id, full_name);

        }
    }


    public void chooseaccountdialog() {

        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(AddEventActivity.this);
       /* dialogBuilder1.setTitle("Add Event");
        dialogBuilder1.requestWindowFeature(Window.FEATURE_NO_TITLE);*/

        //dialogBuilder.setCancelable(true);

        LayoutInflater inflater1 = getLayoutInflater();
        final View dialogView1 = inflater1.inflate(R.layout.custom_dialoggoogle, null);
        dialogBuilder1.setView(dialogView1);


        Button btn_signIn = (Button) dialogView1.findViewById(R.id.btn_google);
        ImageView iv_cancle = (ImageView) dialogView1.findViewById(R.id.iv_cancle);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        b = dialogBuilder1.create();
        b.show();

    }
}
