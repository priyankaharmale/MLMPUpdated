package hnwebproject.com.mlmp.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Activity.VideoViewActivity;
import hnwebproject.com.mlmp.Activity.WebviewActivity;
import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.SharedPreference;
import hnwebproject.com.mlmp.Utility.Utils;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessCardFragment extends Fragment implements View.OnClickListener {

    String TAG = BusinessCardFragment.class.getSimpleName();
    ImageView iv_view_profile_pic, iv_video;
    Button btn_phone_number, btn_email, btn_invite_to_MY_DBC, btn_sharepsot;
    TextView tv_name, tv_title, tv_mail_id, tv_company3, tv_company2, tv_company1, tv_education;
    ImageButton ib_you_tube, ib_lin, ib_fb, ib_insta, ib_twitter;
    Drawable drawable;
    private String user_id;
    private String email;
    public static ActionBarDrawerToggle toggle;
    private TextView tv_website;

    String youtube_link, linkedin_link, facebook_link, instagram_link, twitter_link;
    private String video_url;

    public BusinessCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_card, container, false);

        getsaveData();
        initView(view);
        if (Utils.isNetworkAvailable(getActivity())) {
            getProfileData(user_id);
        } else {
            Utils.myToast1(getActivity());
        }
        return view;
    }

    private void initView(View view) {
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(getActivity(), drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        showBackButton();

        drawable = ContextCompat.getDrawable(getActivity(), R.drawable.sllider_menu_avtar);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_website = (TextView) view.findViewById(R.id.tv_website);
        tv_website.setPaintFlags(tv_website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_company3 = (TextView) view.findViewById(R.id.tv_company3);
        tv_company2 = (TextView) view.findViewById(R.id.tv_company2);
        tv_company1 = (TextView) view.findViewById(R.id.tv_company1);
        tv_education = (TextView) view.findViewById(R.id.tv_education);
        iv_video = (ImageView) view.findViewById(R.id.iv_video);
        iv_video.setOnClickListener(this);
        iv_view_profile_pic = (ImageView) view.findViewById(R.id.iv_view_profile_pic);
        btn_phone_number = (Button) view.findViewById(R.id.btn_phone_number);
        btn_email = (Button) view.findViewById(R.id.btn_email);
        btn_invite_to_MY_DBC = (Button) view.findViewById(R.id.btn_invite_to_MY_DBC);
        btn_sharepsot = (Button) view.findViewById(R.id.btn_sharepsot);
        ib_you_tube = (ImageButton) view.findViewById(R.id.ib_you_tube);
        ib_lin = (ImageButton) view.findViewById(R.id.ib_lin);
        ib_fb = (ImageButton) view.findViewById(R.id.ib_fb);
        ib_twitter = (ImageButton) view.findViewById(R.id.ib_twitter);
        ib_insta = (ImageButton) view.findViewById(R.id.ib_insta);

        ib_you_tube.setOnClickListener(this);
        ib_lin.setOnClickListener(this);
        ib_insta.setOnClickListener(this);
        ib_twitter.setOnClickListener(this);
        ib_fb.setOnClickListener(this);
        btn_phone_number.setOnClickListener(this);
        btn_email.setOnClickListener(this);
        btn_invite_to_MY_DBC.setOnClickListener(this);
        btn_sharepsot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_number:

                call(btn_phone_number.getText().toString());

                break;
            case R.id.btn_email:

                sendEmail(email);

                break;

            case R.id.btn_invite_to_MY_DBC:

                sendEmail(email);

                break;

            case R.id.ib_you_tube:
                Toast.makeText(getActivity(), "ib_you_tube", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra("url", youtube_link);

                startActivity(intent);

                break;

            case R.id.ib_lin:
                Toast.makeText(getActivity(), "ib_lin", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), WebviewActivity.class);
                intent1.putExtra("url", linkedin_link);
                startActivity(intent1);

                break;
            case R.id.ib_fb:
                Intent intent2 = new Intent(getActivity(), WebviewActivity.class);
                intent2.putExtra("url", facebook_link);
                startActivity(intent2);
                break;

            case R.id.ib_insta:
                Intent intent3 = new Intent(getActivity(), WebviewActivity.class);
                intent3.putExtra("url", instagram_link);
                startActivity(intent3);
                break;

            case R.id.ib_twitter:
                Intent intent4 = new Intent(getActivity(), WebviewActivity.class);
                intent4.putExtra("url", twitter_link);
                startActivity(intent4);
                break;

            case R.id.iv_video:

                Intent myIntent = new Intent(getActivity(), VideoViewActivity.class);
                // myIntent.putExtra("VIDEOurl", video_url);
                myIntent.putExtra("VIDEOurl", video_url);
                startActivity(myIntent);

                break;




        }
    }

    private void sendEmail(String email) {
        Toast.makeText(getActivity(), "send mail", Toast.LENGTH_SHORT).show();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", " ", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void call(CharSequence text) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + text));
        this.startActivity(callIntent);
    }

    private void getProfileData(final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(getActivity(), getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_VIEW_PROFILE,
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

                                JSONObject jsonObject = j.getJSONObject("response");

                                String user_id = jsonObject.getString("user_id");
                                String user_role = jsonObject.getString("user_role");
                                String full_name = jsonObject.getString("full_name");
                                String username = jsonObject.getString("username");
                                String email = jsonObject.getString("email");
                                String profile_pic = jsonObject.getString("profile_pic");
                                String phone = jsonObject.getString("phone");
                                String head_line = jsonObject.getString("head_line");
                                String education = jsonObject.getString("education");
                                String youtube_link = jsonObject.getString("youtube_link");
                                String linkedin_link = jsonObject.getString("linkedin_link");
                                String facebook_link = jsonObject.getString("facebook_link");
                                String instagram_link = jsonObject.getString("instagram_link");
                                String twitter_link = jsonObject.getString("twitter_link");
                                String website = jsonObject.getString("website");
                                String company1 = jsonObject.getString("company1");
                                String company2 = jsonObject.getString("company2");
                                String company3 = jsonObject.getString("company3");
                                video_url = jsonObject.getString("video_url");

                                //  String business = jsonObject.getString("business");

                                setDatatoView(full_name, username, email, profile_pic, phone, head_line, education, user_role,
                                        youtube_link, linkedin_link, facebook_link, instagram_link, twitter_link,
                                        website, company1, company2, company3);


                                SharedPreference.profileSaveAfterupdate(getApplicationContext(), full_name, profile_pic, email);
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

    private void setDatatoView(String full_name, String username, String email, String profile_pic, String phone, String head_line, String education, String user_role, String youtube_link, String linkedin_link, String facebook_link, String instagram_link, String twitter_link, String website, String company1, String company2, String company3) {
        tv_name.setText(full_name);
        tv_website.setText(website);
        tv_title.setText(head_line);
        tv_education.setText(education);
        btn_phone_number.setText(phone);

        if (company3.isEmpty()) {
            tv_company3.setVisibility(View.GONE);
        } else {
            tv_company3.setText(company3);
        }

        if (company2.isEmpty()) {
            tv_company2.setVisibility(View.GONE);
        } else {
            tv_company2.setText(company3);
        }

        // tv_company2.setText(company2);
        tv_company1.setText(company1);

        try {
            Glide.with(getApplicationContext())
                    .load(profile_pic)
                    .error(drawable)
                    .crossFade()
                    .placeholder(drawable)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            //    progress_gift_details.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //   progress_gift_details.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(iv_view_profile_pic);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }


    private void getsaveData() {
        final SharedPreferences settings = getActivity().getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
