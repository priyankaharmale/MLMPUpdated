package hnwebproject.com.mlmp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.listeners.DeepLinkListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.MultipartRequest.MultiPart_Key_Value_Model;
import hnwebproject.com.mlmp.MultipartRequest.MultipartFileUploaderAsync;
import hnwebproject.com.mlmp.MultipartRequest.OnEventListener;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.SharedPreference;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Utils_Dialog;
import hnwebproject.com.mlmp.Utility.Validations;


public class ViewPRofileActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = ViewPRofileActivity.class.getSimpleName();
    ImageButton ib_edit;
    Button btn_save;
    EditText et_name, et_username, et_email, et_phone, et_headline, et_education, et_comapany;
    EditText et_youtube_link, et_linkedInLInk, et_facbook_link, et_instagramlink, et_twitter_link, et_website, et_video_link;
    String profile_pic;
    public static final int REQUEST_CAMERA = 5;
    public static File destination;
    String camImage;
    String companyDesc = "", companyName = "", industry = "", jobTitle = "";

    protected static final int REQUEST_STORAGE_ACCESS_PERMISSION = 102;
    ImageButton user_camerabutton;
    String user_id;
    private int GALLERY = 1, CAMERA = 2;

    ArrayList<MultiPart_Key_Value_Model> mult_list;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private ArrayList<String> mSelectPath;
    private static final int SELECT_GIFT_IMAGE = 3;
    String image_path;
    private EditText et_comapany2;
    private EditText et_comapany3;
    ImageView iv_profile_photo23;
    private ProgressBar progress;
    private String video_url;
    private static final String host = "api.linkedin.com";
    TextInputLayout input_name, input_username, input_email, input_phone, input_headline, input_education, input_business, input_company2, input_company3, input_website, input_youtube_link, input_linkedInLInk, input_facebook_link, input_instagramlink, input_twitter_link, input_video_link;

    private static final String topCardUrl = "https://" + host + "/v1/people/~:" + "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        initView();
        if (Utils.isNetworkAvailable(ViewPRofileActivity.this)) {
            getProfileData(user_id);
        } else {
            Utils.myToast1(ViewPRofileActivity.this);
        }
    }


    private void initView() {

        getsaveData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);

        ib_edit = (ImageButton) toolbar.findViewById(R.id.ib_edit);
        ib_edit.setOnClickListener(this);


        et_name = (EditText) findViewById(R.id.et_name);
        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_headline = (EditText) findViewById(R.id.et_headline);
        et_education = (EditText) findViewById(R.id.et_education);
        et_comapany = (EditText) findViewById(R.id.et_business);
        et_comapany2 = (EditText) findViewById(R.id.et_company2);
        et_comapany3 = (EditText) findViewById(R.id.et_company3);

        et_video_link = (EditText) findViewById(R.id.et_video_link);
        et_youtube_link = (EditText) findViewById(R.id.et_youtube_link);
        et_linkedInLInk = (EditText) findViewById(R.id.et_linkedInLInk);
        et_facbook_link = (EditText) findViewById(R.id.et_facbook_link);
        et_instagramlink = (EditText) findViewById(R.id.et_instagramlink);
        et_twitter_link = (EditText) findViewById(R.id.et_twitter_link);
        et_website = (EditText) findViewById(R.id.et_website);
        et_video_link = (EditText) findViewById(R.id.et_video_link);

        input_name = (TextInputLayout) findViewById(R.id.input_name);
        input_username = (TextInputLayout) findViewById(R.id.input_username);
        input_email = (TextInputLayout) findViewById(R.id.input_email);
        input_phone = (TextInputLayout) findViewById(R.id.input_phone);
        input_headline = (TextInputLayout) findViewById(R.id.input_headline);
        input_education = (TextInputLayout) findViewById(R.id.input_education);
        input_business = (TextInputLayout) findViewById(R.id.input_business);
        input_company2 = (TextInputLayout) findViewById(R.id.input_company2);
        input_company3 = (TextInputLayout) findViewById(R.id.input_company3);
        input_website = (TextInputLayout) findViewById(R.id.input_website);
        input_youtube_link = (TextInputLayout) findViewById(R.id.input_youtube_link);
        input_linkedInLInk = (TextInputLayout) findViewById(R.id.input_linkedInLInk);
        input_facebook_link = (TextInputLayout) findViewById(R.id.input_facebook_link);
        input_instagramlink = (TextInputLayout) findViewById(R.id.input_instagramlink);
        input_twitter_link = (TextInputLayout) findViewById(R.id.input_twitter_link);
        input_video_link = (TextInputLayout) findViewById(R.id.input_video_link);


        user_camerabutton = (ImageButton) findViewById(R.id.user_camerabutton);
        btn_save = (Button) findViewById(R.id.btn_save);
        user_camerabutton.setOnClickListener(this);
        btn_save.setOnClickListener(this);


        iv_profile_photo23 = (ImageView) findViewById(R.id.iv_view_profile_detail);
        progress = (ProgressBar) findViewById(R.id.progress);

        et_comapany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_linkedin();
            }
        });

        et_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linker();
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
                System.out.println("printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btn_save:

                Intent i=new Intent(ViewPRofileActivity.this,DashBoardActivity.class);
                startActivity(i);

                break;
*/
            case R.id.user_camerabutton:
                //pickImage();
                showPictureDialog();
                break;

            case R.id.btn_save:


                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(ViewPRofileActivity.this)) {

                        String name = et_name.getText().toString();
                        String username = et_username.getText().toString();
                        String email = et_email.getText().toString();
                        String phone = et_phone.getText().toString();
                        String headline = et_headline.getText().toString();
                        String education = et_education.getText().toString();
                        String company = et_comapany.getText().toString();
                        String company2 = et_comapany2.getText().toString();
                        String company3 = et_comapany3.getText().toString();

                        String youtube_link = et_youtube_link.getText().toString();
                        String linkedInLInk = et_linkedInLInk.getText().toString();
                        String facebook_link = et_facbook_link.getText().toString();
                        String instagramlink = et_instagramlink.getText().toString();
                        String twitter_link = et_twitter_link.getText().toString();
                        String website = et_website.getText().toString();
                        String video_link = et_video_link.getText().toString();


                        update(name, username, email, phone, headline, education, company, user_id, youtube_link, linkedInLInk, facebook_link, instagramlink, twitter_link
                                , website, video_link, company2, company3);
                    } else {
                        Utils.myToast1(ViewPRofileActivity.this);
                    }
                }


                break;
        }
    }

    private void getProfileData(final String user_id) {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
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
                                profile_pic = jsonObject.getString("profile_pic");
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
                                video_url = jsonObject.getString("video_link");

                                //  String business = jsonObject.getString("business");

                                setDatatoView(full_name, username, email, profile_pic, phone, head_line, education, user_role,
                                        youtube_link, linkedin_link, facebook_link, instagram_link, twitter_link,
                                        website, company1, company2, company3, video_url);

                                SharedPreference.profileSaveAfterupdate(getApplicationContext(), full_name, profile_pic, email);
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewPRofileActivity.this);
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

                        String reason = AppUtils.getVolleyError(ViewPRofileActivity.this, error);
                        AlertUtility.showAlert(ViewPRofileActivity.this, reason);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void setDatatoView(String full_name, String username, String email, String profile_pic,
                               String phone, String head_line, String education, String user_role,
                               String youtube_link, String linkedin_link, String facebook_link,
                               String instagram_link, String twitter_link, String website,
                               String company1, String company2, String company3, String video_url) {

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.sllider_menu_avtar);
        et_name.setText(full_name);
        et_username.setText(username);
        et_email.setText(email);
        et_phone.setText(phone);
        et_headline.setText(head_line);
        et_education.setText(education);
        et_comapany.setText(company1);
        et_comapany2.setText(company2);
        et_comapany3.setText(company3);
        et_instagramlink.setText(instagram_link);
        et_youtube_link.setText(youtube_link);
        et_twitter_link.setText(twitter_link);
        et_linkedInLInk.setText(linkedin_link);
        et_facbook_link.setText(facebook_link);
        et_website.setText(website);
        et_video_link.setText(video_url);
        String img = profile_pic.replace(" ", "%20");

        Log.d("img", "is:\t" + img);


        try {
            Glide.with(ViewPRofileActivity.this)
                    .load(img)
                    .error(drawable)
                    .centerCrop()
                    .crossFade()
                    // .placeholder(R.mipmap.defaulteventimagesmall)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(iv_profile_photo23);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }


    private void getsaveData() {
        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        user_id = (settings.getString("user_id", ""));
        System.out.println("user_id.." + user_id);
    }

    @Override
    public boolean onSupportNavigateUp() {

        final SharedPreferences settings = getSharedPreferences("AOP_PREFS", Context.MODE_PRIVATE); //1
        String status = settings.getString("status", null);

        if (status.equals("0")) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            onBackPressed();

        }
        return true;
    }


    private void update(final String name, final String username, final String email, final String phone, final String headline, final String education, final String company, final String user_id, String youtube_link, String linkedInLInk, String facebook_link, String instagramlink, String twitter_link, String website, String video_link, String company2, String company3) {

        final ProgressDialog myDialog = Utils_Dialog.DialogsUtils.showProgressDialog(ViewPRofileActivity.this, "Processing...");
        mult_list = new ArrayList<MultiPart_Key_Value_Model>();

        MultiPart_Key_Value_Model OneObject = new MultiPart_Key_Value_Model();


        Map<String, String> fileParams = new HashMap<>();
        // fileParams.put("img", image_path);


        if (image_path == null || image_path.equals("") || image_path.isEmpty()) {

            // fileParams.put(AppConstant.KEY_PROFILEPIC,profile_pic);
            Log.d("image_path", "image_path:\t" + profile_pic);
        } else {
            fileParams.put(AppConstant.KEY_PROFILEPIC, image_path);
            profile_pic = image_path;
            Log.d("image_path", "image_path:\t" + image_path);
        }

        Map<String, String> Stringparams = new HashMap<>();

        Stringparams.put(AppConstant.KEY_FULL_NAME, name);
        Stringparams.put(AppConstant.KEY_EDUCATION, education);
        Stringparams.put(AppConstant.KEY_EMAIL, email);
        Stringparams.put(AppConstant.KEY_PHONE, phone);
        Stringparams.put(AppConstant.KEY_HEADLINE, headline);
        Stringparams.put(AppConstant.KEY_USERNAME, username);
        Stringparams.put(AppConstant.KEY_USERID, user_id);
        Stringparams.put(AppConstant.KEY_CITY_NAME, user_id);
        Stringparams.put(AppConstant.KEY_UNIVERSITY, user_id);
        Stringparams.put(AppConstant.KEY_YEAR, user_id);
        Stringparams.put(AppConstant.KEY_YOUTUBE_LINK, youtube_link);
        Stringparams.put(AppConstant.KEY_LINKEDIN_LINK, linkedInLInk);
        Stringparams.put(AppConstant.KEY_FACEBBOKJ_LINK, facebook_link);
        Stringparams.put(AppConstant.KEY_INSTAGRAM_LINK, instagramlink);
        Stringparams.put(AppConstant.KEY_TWITTER_LINK, twitter_link);
        Stringparams.put(AppConstant.KEY_WEBSITE_LINK, website);
        Stringparams.put("business", company);
        Stringparams.put(AppConstant.KEY_COMPANY1, company);
        Stringparams.put(AppConstant.KEY_COMPANY2, company2);
        Stringparams.put(AppConstant.KEY_COMPANY3, company3);
        Stringparams.put(AppConstant.KEY_VIDEO_LINK, video_link);

        String requestURL = AppConstant.API_EDIT_PROFILE;
        OneObject.setUrl(requestURL);
        OneObject.setFileparams(fileParams);
        OneObject.setStringparams(Stringparams);


        Log.e(TAG, Stringparams.toString());
        Log.e("Fileparam", fileParams.toString());
        MultipartFileUploaderAsync someTask = new MultipartFileUploaderAsync(getApplicationContext(), OneObject, new OnEventListener<String>() {

            @Override
            public void onSuccess(String object) {
                Log.d("object", object);
                try {
                    JSONObject jsonObj = new JSONObject(object);
                    int message_code = Integer.parseInt(jsonObj.getString("message_code"));
                    String message = jsonObj.getString("message");
                    Log.d("Message", message);

                    myDialog.dismiss();

                    if (message_code == 1) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPRofileActivity.this);
                        builder.setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        SharedPreference.statusLogin(getApplicationContext(), "1");

                                        Intent i_login = new Intent(ViewPRofileActivity.this, DashBoardActivity.class);
                                        startActivity(i_login);


                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    } else {
                        AlertUtility.showAlert(ViewPRofileActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

                Log.d(TAG, "Exception" + e);
            }
        });
        someTask.execute();

        return;
    }


    /*   public void onActivityResult(int requestCode, int resultCode, Intent data) {

           super.onActivityResult(requestCode, resultCode, data);

   *//*        if (requestCode == SELECT_GIFT_IMAGE) {

            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                System.out.println("mselected" + String.valueOf(mSelectPath.get(0)));

                image_path = mSelectPath.get(0);

                iv_profile_photo23.setBackgroundResource(0);
                try {
                    Glide.with(this)
                            .load(new File(String.valueOf(mSelectPath.get(0))))
                            .crossFade()
                            .into(iv_profile_photo23);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }*//*
    }*/
    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(ViewPRofileActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                isPermissionGrantedImageGallery();
                                break;
                            case 1:
                                isPermissionGrantedImage();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void isPermissionGrantedImage() {

        System.out.println("Click Image");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(ViewPRofileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_ACCESS_PERMISSION);
        } else {
            camerImage();
        }

    }

    public void isPermissionGrantedImageGallery() {

        System.out.println("Click Image");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(ViewPRofileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_ACCESS_PERMISSION);
        } else {
            choosePhotoFromGallary();
        }

    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ViewPRofileActivity.this, permission)) {
            new android.app.AlertDialog.Builder(ViewPRofileActivity.this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ViewPRofileActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(ViewPRofileActivity.this, new String[]{permission}, requestCode);
        }
    }

    public void camerImage() {
        System.out.println("Click Image11");
        String name = AppConstant.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".png");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ViewPRofileActivity.this, ViewPRofileActivity.this.getApplicationContext().getPackageName() + ".my.package.name.provider", destination));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);
        Log.i("Access token->", LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
        Toast.makeText(ViewPRofileActivity.this, LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue(), Toast.LENGTH_SHORT).show();
        System.out.println("AccessToke...." + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
        String ddfdfds = LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue();

        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
        deepLinkHelper.onActivityResult(this, requestCode, resultCode, data);
        getUserData(ddfdfds);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(ViewPRofileActivity.this.getContentResolver(), data.getData());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    // Log.i("Path", imagePath12);
                    FileOutputStream fo;
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    image_path = destination.getAbsolutePath();

                    try {
                        Glide.with(ViewPRofileActivity.this)
                                .load(image_path)
                                //.error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                                .into(iv_profile_photo23);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewPRofileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_CAMERA) {


            System.out.println("REQUEST_CAMERA");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            String imagePath = destination.getAbsolutePath();
            Log.i("Path", imagePath);
            image_path = imagePath;
            Toast.makeText(ViewPRofileActivity.this, camImage, Toast.LENGTH_SHORT).show();


            try {
                Glide.with(ViewPRofileActivity.this)
                        .load(image_path)
                        //.error(R.drawable.no_image)
                        .centerCrop()
                        .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                        .into(iv_profile_photo23);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        }
    }

    public void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Toast.makeText(ViewPRofileActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                //Toast.makeText(getApplicationContext(), + error.toString(),
                //    Toast.LENGTH_LONG).show();
                Toast.makeText(ViewPRofileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(ViewPRofileActivity.this, "LIApiError" + error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("REsultgdfgf" + error.toString());

            }
        }, true);
    }

    // This method is used to make permissions to retrieve data from linkedin</p>
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
        //  return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.R_FULLPROFILE);
    }


/*    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,picture-url,public_profile_url,industry,summary,specialties,picture-urls::(original),positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type,recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)";

        apiHelper.getRequest(ViewPRofileActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    //  setUserProfile(result.getResponseDataAsJson());
                    // progress.dismiss();

                    JSONObject json = result.getResponseDataAsJson();
                    JSONObject obj = json.getJSONObject("positions").getJSONArray("values").getJSONObject(0);
                    JSONObject positions = obj.getJSONObject("company");
                    //  JSONObject objeducation =       json.getJSONObject("educations").getJSONArray("values").getJSONObject(0);;
                    // String education= objeducation.getString("degree");
                    if (obj.has("title"))
                        jobTitle = obj.getString("title");

                    if (obj.has("summary"))
                        companyDesc = obj.getString("summary");

                    if (positions.has("name"))
                        companyName = positions.getString("name");

                    if (json.has("industry"))
                        industry = json.getString("industry");
                    // progress.dismiss();

                    System.out.println("Comapnay name:" + companyName);
                    System.out.println("Comapnay inductry:" + industry);
                    System.out.println("Comapnay companyDesc:" + companyDesc);
                    System.out.println("Comapnay jobTitle:" + jobTitle);
                    et_comapany.setText(companyName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());</p>
            }
        });
    }*/

    public void getUserData(String authtoken) {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,picture-url,public_profile_url,industry,summary,specialties,picture-urls::(original),positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type,recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)";
        //  String url = "https://api.linkedin.com/v1/people/~";
        //  String url = "https://api.linkedin.com/v1/people/~?format=json";
    /*   String url="https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,picture-url,industry,summary,specialties,positions:(id,title,summary,start-date," +
               "end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:" +
               "(id,school-name,field-of-study,start-date,end-date,degree,activities,notes)," +
               "associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name)," +
               "authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name)," +
               "office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:" +
               "(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number" +
               ",start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type," +
               "recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)" +
               "?oauth2_access_token="+ authtoken;*/

        // String url="https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url)?format=json";

        apiHelper.getRequest(ViewPRofileActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                System.out.println("REsultgdfgf" + result);
                String df = result.toString();
                Toast.makeText(ViewPRofileActivity.this, "SuccessProfile", Toast.LENGTH_SHORT).show();
                try {
                    //  setUserProfile(result.getResponseDataAsJson());
                    // progress.dismiss();

                    JSONObject json = result.getResponseDataAsJson();
                    JSONObject obj = json.getJSONObject("positions").getJSONArray("values").getJSONObject(0);
                    JSONObject positions = obj.getJSONObject("company");
                    //  JSONObject objeducation =       json.getJSONObject("educations").getJSONArray("values").getJSONObject(0);;
                    // String education= objeducation.getString("degree");
                    if (obj.has("title"))
                        jobTitle = obj.getString("title");

                    if (obj.has("summary"))
                        companyDesc = obj.getString("summary");

                    if (positions.has("name"))
                        companyName = positions.getString("name");

                    if (json.has("industry"))
                        industry = json.getString("industry");
                    // progress.dismiss();

                    System.out.println("Comapnay name:" + companyName);
                    System.out.println("Comapnay inductry:" + industry);
                    System.out.println("Comapnay companyDesc:" + companyDesc);
                    System.out.println("Comapnay jobTitle:" + jobTitle);
                    et_comapany.setText(companyName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                Toast.makeText(ViewPRofileActivity.this, "LIApiError" + error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Error" + error.toString());

                // ((TextView) findViewById(R.id.error)).setText(error.toString());</p>
            }
        });
    }

    public void setUserProfile(JSONObject response) {

        try {
           /* tv_email.setText(response.get("emailAddress").toString());
            tv_name.setText(response.get("formattedName").toString());
            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(circleImageView);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidation() {

        boolean ret = true;


        if (!Validations.hasText_input_layout(et_name, "Please Enter Name", input_name))
            ret = false;

        if (!Validations.hasText_input_layout(et_username, "Please Enter Username", input_username))
            ret = false;


        if (!Validations.hasText_input_layout(et_email, "Please Enter Email ", input_email))
            ret = false;


        if (!Validations.isEmailAddress_input_layout(et_email, true, "Please Enter Valid Email ", input_email))
            ret = false;

        if (!Validations.hasText_input_layout(et_phone, "Please Enter Phone Number ", input_phone))
            ret = false;

        if (!Validations.check_text_length_10(et_phone, "Please Enter Valid Phone Number ", input_phone))
            ret = false;

        if (!Validations.hasText_input_layout(et_headline, "Please Enter Title ", input_headline))
            ret = false;

        if (!Validations.hasText_input_layout(et_education, "Please Enter Education ", input_education))
            ret = false;

        if (!Validations.hasText_input_layout(et_comapany, "Please Enter Company ", input_business))
            ret = false;
        if (!Validations.hasText_input_layout(et_comapany2, "Please Enter Company ", input_company2))
            ret = false;
        if (!Validations.hasText_input_layout(et_comapany3, "Please Enter Company ", input_company3))
            ret = false;
        if (!Validations.hasText_input_layout(et_website, "Please Enter Website ", input_website))
            ret = false;
        if (!Validations.hasText_input_layout(et_youtube_link, "Please Enter Youtube Link ", input_youtube_link))
            ret = false;
        if (!Validations.hasText_input_layout(et_linkedInLInk, "Please Enter LinkedIn Link ", input_linkedInLInk))
            ret = false;
        if (!Validations.hasText_input_layout(et_facbook_link, "Please Enter Facebook Link ", input_facebook_link))
            ret = false;
        if (!Validations.hasText_input_layout(et_instagramlink, "Please Enter Instagram Link ", input_instagramlink))
            ret = false;
        if (!Validations.hasText_input_layout(et_twitter_link, "Please Enter Twitter Link ", input_twitter_link))
            ret = false;
        if (!Validations.hasText_input_layout(et_video_link, "Please Enter Twitter Link ", input_video_link))
            ret = false;
     /*   int id_radio_group = radioGroup.getCheckedRadioButtonId();
        if (id_radio_group == -1 || id_radio_group == 0) {

            AlertUtility.showAlert(this, "Please select Individual or Organization");
            ret = false;
        }
*/

        return ret;
    }


    public void linker() {
        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();

        // Open the current user's profile
        deepLinkHelper.openCurrentProfile(this, new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                // Successfully sent user to LinkedIn app
                Toast.makeText(ViewPRofileActivity.this, "Current profile opened successfully.", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {
                // Error sending user to LinkedIn app
                Log.e(TAG, "Current profile open error : " + error.toString());
                Toast.makeText(ViewPRofileActivity.this, "Failed to open current profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
