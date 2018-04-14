package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Fragment.CalenderAndTaskFragment;
import hnwebproject.com.mlmp.Fragment.ContactsFragment;
import hnwebproject.com.mlmp.Fragment.ProductServicesFragment;
import hnwebproject.com.mlmp.Fragment.TrainingCentreFragment;
import hnwebproject.com.mlmp.Interface.OnCallBack;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.SharedPreference;
import hnwebproject.com.mlmp.Utility.Utils;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, OnCallBack {

    TextView nav_email, nav_name, tv_digital_business_card_tool_bar;
    String TAG = DashBoardActivity.class.getSimpleName();

    ImageView iv_view_profile_pic, imageviewicon_tool_bar;
    ImageButton ib_notification, ib_edit;
    LinearLayout ll_addTocart;
    String name, email, userId, profile_photo, login_through;
    private GoogleApiClient mGoogleApiClient;
    LinearLayout ll_navigation;
    Drawable drawable;
    Toolbar toolbar;
    TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getSaveData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_digital_business_card_tool_bar = (TextView) toolbar.findViewById(R.id.tv_digital_business_card_tool_bar);
        imageviewicon_tool_bar = (ImageView) toolbar.findViewById(R.id.imageviewicon_tool_bar);
        ib_notification = (ImageButton) toolbar.findViewById(R.id.ib_notification);
        ll_addTocart = (LinearLayout) toolbar.findViewById(R.id.ib_edit1);
        ib_edit = (ImageButton) toolbar.findViewById(R.id.ib_edit);
        tv_count = (TextView) toolbar.findViewById(R.id.tv_count);
        tv_digital_business_card_tool_bar.setVisibility(View.GONE);
        ll_addTocart.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        ib_edit.setOnClickListener(this);

        ll_addTocart.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        drawable = ContextCompat.getDrawable(this, R.drawable.sllider_menu_avtar);
        nav_email = (TextView) headerView.findViewById(R.id.nav_email);
        ll_navigation = (LinearLayout) headerView.findViewById(R.id.ll_navigation);
        nav_name = (TextView) headerView.findViewById(R.id.nav_name);

        iv_view_profile_pic = (ImageView) headerView.findViewById(R.id.iv_view_profile_pic);

        nav_email.setText(email);
        nav_name.setText(name);


        ll_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ViewPRofileActivity.class);
                startActivity(intent);
            }
        });

        String img = profile_photo.replace(" ", "%20");

        Log.d("img", "is:\t" + img);
        getProfileData();

        try {
            Glide.with(DashBoardActivity.this)
                    .load(img)
                    .error(drawable)
                    .centerCrop()
                    .crossFade()
                    // .placeholder(R.mipmap.defaulteventimagesmall)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            // progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //  progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(iv_view_profile_pic);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


        navigationView.setNavigationItemSelectedListener(this);

        initView();
    }

    private void initView() {
        ll_addTocart.setVisibility(View.VISIBLE);
        Fragment fragment = new ProductServicesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        try {
             /*Google login*/
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }


    }


    private void getSaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        name = settings.getString("full_name", "");
        email = settings.getString("email_address", "");
        userId = settings.getString("user_id", "");
        profile_photo = settings.getString("profile_photo", "");
        Log.d("profile_photo", "is: " + profile_photo);
        Log.d("USER", "is: " + userId);
        login_through = settings.getString("login_through", "");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_digital_business:

              /*  Fragment fragmentB = new BusinessCardFragment();
                setWhiteBgtoolbar();
                replace(fragmentB);*/

                Intent i_login = new Intent(DashBoardActivity.this, BusinessCardActivity.class);
                startActivity(i_login);

                ll_addTocart.setVisibility(View.GONE);

                break;

            case R.id.nav_contacts:
                Fragment fragment = new ContactsFragment();
                ll_addTocart.setVisibility(View.GONE);

                removeWhiteToolBar();
                replace(fragment);
                break;

            case R.id.nav_training_centre:

                Fragment fragmentT = new TrainingCentreFragment();
                ll_addTocart.setVisibility(View.GONE);

                removeWhiteToolBar();
                replace(fragmentT);
                break;
            case R.id.nav_calender_task:

                Fragment fragmentC = new CalenderAndTaskFragment();
                ll_addTocart.setVisibility(View.GONE);

                removeWhiteToolBar();
                replace(fragmentC);
                break;
            case R.id.nav_product_service:

                Fragment fragmentp = new ProductServicesFragment();
                ll_addTocart.setVisibility(View.VISIBLE);
                removeWhiteToolBar();
                replace(fragmentp);

                break;
            case R.id.nav_donation:
                removeWhiteToolBar();
                ll_addTocart.setVisibility(View.GONE);
                break;

            case R.id.nav_logout:
                ll_addTocart.setVisibility(View.GONE);

                removeWhiteToolBar();
                showLogoutAlert();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeWhiteToolBar() {
        tv_digital_business_card_tool_bar.setVisibility(View.GONE);
        imageviewicon_tool_bar.setVisibility(View.VISIBLE);
        ib_notification.setVisibility(View.VISIBLE);
        ib_edit.setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.BLACK);
    }

    private void setWhiteBgtoolbar() {
        tv_digital_business_card_tool_bar.setVisibility(View.VISIBLE);
        imageviewicon_tool_bar.setVisibility(View.GONE);
        ib_notification.setVisibility(View.GONE);
        ib_edit.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(Color.WHITE);
    }


    private void replace(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    private void showLogoutAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Logout Successfully");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (login_through.equals("Facebook")) {
                    LoginManager.getInstance().logOut();
                    AccessToken.setCurrentAccessToken(null);
                    SharedPreference.clearSharedPreference(DashBoardActivity.this);
                    dialog.cancel();
                    Intent in = new Intent(DashBoardActivity.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);


                } else if (login_through.equals("Gmail")) {

                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                mGoogleApiClient.disconnect();
                                SharedPreference.clearSharedPreference(DashBoardActivity.this);
                                Intent in = new Intent(DashBoardActivity.this, LoginActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);

                            }
                        });
                    }
                } else {
                    SharedPreference.clearSharedPreference(DashBoardActivity.this);
                    // prefs.edit().clear().apply();
                    //dialog.cancel();
                    Intent in = new Intent(DashBoardActivity.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);


                    dialog.cancel();


                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_edit:

                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
                Intent i_login = new Intent(DashBoardActivity.this, ViewPRofileActivity.class);

                startActivity(i_login);
                break;

            case R.id.ib_edit1:

                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
                Intent addtocart = new Intent(DashBoardActivity.this, ProductAddToCartListActivity.class);

                startActivity(addtocart);

                break;
        }
    }

    private void getProfileData() {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(DashBoardActivity.this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.KEY_ADDTOCART_PRODUCT_COUNT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();


                        Log.d(TAG, "res_ViewPRofile" + response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");

                            if (message_code == 1) {

                                String count = j.getString("response");
                                tv_count.setText(count);

                            } else {
                                final String count = j.getString("response");

                                final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                                builder.setMessage(count)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                if(count.equals("No product in cart"))
                                                {
                                                    tv_count.setText("0");
                                                }

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

                        String reason = AppUtils.getVolleyError(DashBoardActivity.this, error);
                        AlertUtility.showAlert(DashBoardActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DashBoardActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void callback() {

    }

    @Override
    public void fetchCount() {

    }


}
