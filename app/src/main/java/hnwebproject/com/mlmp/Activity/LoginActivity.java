package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.SharedPreference;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    String CLASSTAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "AndroidBash";
    private EditText et_username, et_password;
    private Button btn_login;
    private ImageButton ib_fb, ib_gmail, ib_twitter;
    private TextView tv_sign_up;
    private String hash_key_FB = "";
    private CallbackManager callbackManager;
    TwitterAuthClient mTwitterAuthClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));


        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
///////////////////////////////gmail///////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

///////////////////////////////////gmail/////////////////////
        initView();

        mTwitterAuthClient = new TwitterAuthClient();


        ib_twitter = (ImageButton) findViewById(R.id.ib_twitter);
        ib_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTwitterAuthClient.authorize(LoginActivity.this, new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
// Success
                        Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                        loginNew(twitterSessionResult);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                        Toast.makeText(LoginActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loginWithFB();
    }


    private void loginWithFB() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Toast.makeText(LoginActivity.this, "Facebook Login sucess by User", Toast.LENGTH_SHORT).show();
                AccessToken accessTokenFB = loginResult.getAccessToken();
                getFacebookData(accessTokenFB);
                Log.d("AccessToken", String.valueOf(accessTokenFB));
            }

            @Override
            public void onCancel() {
                //AlertUtility.showAlert(RegisterActivity.this, false, "Facebook Login Canceled by User");
                Toast.makeText(LoginActivity.this, "Facebook Login Canceled by User", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //AlertUtility.showAlert(RegisterActivity.this, false, "Facebook Login Error");
                Toast.makeText(LoginActivity.this, "Facebook Login Error", Toast.LENGTH_SHORT).show();
            }
        });

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                hash_key_FB = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // fbsha1key();

    }

    /*Hash Key generator*/
    private void fbsha1key() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private void getFacebookData(AccessToken accessTokenFB) {

        GraphRequest graphRequest = GraphRequest.newMeRequest(accessTokenFB, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {

                    Log.d("GraphRequest", "object = " + object.toString());
                    Log.d("GraphRequest", "response = " + response);

                    try {
                        String facebook_id = object.getString("id");
                        String email_address = object.getString("email");
                        String name = object.getString("name");
                        String object_key = hash_key_FB;
                        String profile_url = object.getString("link");
                        String profileImageUrl = ImageRequest.getProfilePictureUri(object.optString("id"), 500, 500).toString();
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String gender = object.getString("gender");
                        String languages = object.getString("locale");
                        //String birthday = object.getString("birthday");
                        Log.d("profile_image", profileImageUrl);
                        //checkFBUserExist(object.getString("id"), object.getString("name"), object.getString("email"),profileImageUrl);
                        String fullname = first_name + " " + last_name;

                        String register_token_id = "";

                        LoginWithFB(facebook_id, email_address, name, profileImageUrl, register_token_id, fullname);


                        Log.d("userDataFB ", object.getString("id") + " :: " + object.getString("name") + " :: " + object.getString("email"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                if (response != null) {
                    Log.d("GraphRequest", "response= " + response.toString());
                }
            }
        });
        Bundle parameters = new Bundle();
        //parameters.putString("fields", "id,name,email");
        parameters.putString("fields", "id,name,email,link,first_name,last_name,locale,gender");
        //parameters.putString("fields", "id,name,email,gender,birthday,link,first_name,middle_name,last_name,locale,hometown,location");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result to the Twitter login button.
        // mLoginButton.onActivityResult(requestCode, resultCode, data);

        //client.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                //firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }
*/
    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        btn_login = (Button) findViewById(R.id.btn_login);
        ib_fb = (ImageButton) findViewById(R.id.ib_fb);
        ib_gmail = (ImageButton) findViewById(R.id.ib_gmail);
        ib_gmail.setOnClickListener(this);
        ib_fb.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_sign_up.setOnClickListener(this);
        tv_sign_up.setPaintFlags(tv_sign_up.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(LoginActivity.this)) {

                        String password = et_password.getText().toString();

                        String username = et_username.getText().toString();

                        login(username, password);
                    } else {
                        Utils.myToast1(LoginActivity.this);
                    }
                }
                break;

            case R.id.tv_sign_up:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_fb:
                // Toast.makeText(this, "Fb login", Toast.LENGTH_SHORT).show();
                Log.d("hash_key_FB", hash_key_FB);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));

                break;

            case R.id.ib_gmail:
                signIn();
                break;

        }
    }

    private void login(final String username, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_LOGIN,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //myDialog.dismiss();

                        Log.d("res_login", "is:\t" + response);
                        String university_name = "", status = "";
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {

                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String user_id = jsonObject.getString("user_id");
                                    String user_role = jsonObject.getString("user_role");
                                    String full_name = jsonObject.getString("full_name");
                                    String email = jsonObject.getString("email");
                                    String profile_pic = jsonObject.getString("profile_pic");
                                    university_name = jsonObject.getString("university_name");
                                    status = jsonObject.getString("status");

                                    SharedPreference.statusLogin(getApplicationContext(), status);
                                    SharedPreference.profileSave(getApplicationContext(), user_id, full_name, user_role, profile_pic, "app", password, email);
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                final String finalUniversity_name = university_name;

                                final String finalStatus = status;
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (finalStatus.equals("0")) {
                                                    Intent i_login = new Intent(LoginActivity.this, ViewPRofileActivity.class);
                                                    startActivity(i_login);
                                                } else {
                                                    Intent i_login = new Intent(LoginActivity.this, DashBoardActivity.class);
                                                    startActivity(i_login);
                                                }


                                               /* if(finalUniversity_name.equals("")|| finalUniversity_name.isEmpty()) {

                                                    Intent i_login = new Intent(LoginActivity.this, OrganizationOrSchoolNameActivity.class);
                                                    startActivity(i_login);
                                                }else {
                                                    Intent i_login = new Intent(LoginActivity.this, ViewPRofileActivity.class);
                                                    startActivity(i_login);
                                                }*/

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                        //myDialog.dismiss();

                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        String reason = AppUtils.getVolleyError(LoginActivity.this, error);
                        AlertUtility.showAlert(LoginActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {


                    params.put(AppConstant.KEY_PASSWORD, password);
                    params.put(AppConstant.KEY_USERNAME, username);


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


    private boolean checkValidation() {

        boolean ret = true;

        if (!Validations.hasText(et_username, "Please Enter Full Name"))
            ret = false;

        if (!Validations.hasText(et_password, "Please Enter Password"))
            ret = false;
        return ret;
    }

    /*handling sing in for google plus*/
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
            String email_username = acct.getEmail();

            String google_ids = acct.getId();
            Log.e(CLASSTAG, "Name: " + personName + ", email: " + email_username + " IDs " + google_ids);


            String image_user = String.valueOf(personPhotoUrl);

           /* Intent intent = new Intent(this, DashBoardActivity.class);
            startActivity(intent);
*/
            Object register_token_id = "";
            LoginWithGooglePlus(google_ids, email_username, personName, image_user, register_token_id, full_name);

        }
    }

    private void LoginWithGooglePlus(final String google_ids, final String email_username, String personName, String image_user, Object register_token_id, final String full_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_LOGINWITH_GMAIL,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //myDialog.dismiss();

                        Log.d("res_login_gmail", "is:\t" + response);
                        String university_name = "";
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {

                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String user_id = jsonObject.getString("user_id");
                                    String user_role = jsonObject.getString("user_role");
                                    String full_name = jsonObject.getString("full_name");
                                    String email = jsonObject.getString("email");
                                    String profile_pic = jsonObject.getString("profile_pic");
                                    university_name = jsonObject.getString("university_name");
                                    String password = "";
                                    SharedPreference.profileSave(getApplicationContext(), user_id, full_name, user_role, profile_pic, "app", password, email);
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                final String finalUniversity_name = university_name;
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (finalUniversity_name.equals("") || finalUniversity_name.isEmpty()) {
                                                    SharedPreference.statusLogin(getApplicationContext(), "1");
                                                    Intent i_login = new Intent(LoginActivity.this, OrganizationOrSchoolNameActivity.class);
                                                    startActivity(i_login);
                                                } else {
                                                    Intent i_login = new Intent(LoginActivity.this, DashBoardActivity.class);
                                                    startActivity(i_login);
                                                }

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                        //myDialog.dismiss();
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        String reason = AppUtils.getVolleyError(LoginActivity.this, error);
                        AlertUtility.showAlert(LoginActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_FULL_NAME, full_name);
                    params.put(AppConstant.KEY_EMAIL, email_username);
                    params.put(AppConstant.KEY_GAMIL_ID, google_ids);
                    params.put(AppConstant.KEY_DEVICE_TYPE, "android");

                   /* //  public static final String KEY_FULL_NAME = "full_name";
                    //   public static final String KEY_EMAIL = "email";
                    // public static final String KEY_DEVICE_TYPE = "device_type";// ('ios' OR 'android')
                    // public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')
                    public static final String KEY_GAMIL_ID = "facebook_id";*/


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


    private void LoginWithFB(final String facebook_id, final String email_address, final String name, String profileImageUrl, String register_token_id, String fullname) {

        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_LOGINWITH_FB,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_login_fb", "is:\t" + response);
                        String university_name = "";
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {

                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String user_id = jsonObject.getString("user_id");
                                    String user_role = jsonObject.getString("user_role");
                                    String full_name = jsonObject.getString("full_name");
                                    String email = jsonObject.getString("email");
                                    String profile_pic = jsonObject.getString("profile_pic");
                                    university_name = jsonObject.getString("university_name");

                                    String password = "";

                                    SharedPreference.profileSave(getApplicationContext(), user_id, full_name, user_role, profile_pic, "app", password, email);
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                final String finalUniversity_name = university_name;
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (finalUniversity_name.equals("") || finalUniversity_name.isEmpty()) {
                                                    SharedPreference.statusLogin(getApplicationContext(), "1");
                                                    Intent i_login = new Intent(LoginActivity.this, OrganizationOrSchoolNameActivity.class);
                                                    startActivity(i_login);
                                                } else {
                                                    Intent i_login = new Intent(LoginActivity.this, DashBoardActivity.class);
                                                    startActivity(i_login);
                                                }
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                        String reason = AppUtils.getVolleyError(LoginActivity.this, error);
                        AlertUtility.showAlert(LoginActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_FULL_NAME, name);
                    params.put(AppConstant.KEY_EMAIL, email_address);
                    params.put(AppConstant.KEY_FACEBOOK_ID, facebook_id);
                    params.put(AppConstant.KEY_DEVICE_TYPE, "android");

                   /* //  public static final String KEY_FULL_NAME = "full_name";
                    //   public static final String KEY_EMAIL = "email";
                    // public static final String KEY_DEVICE_TYPE = "device_type";// ('ios' OR 'android')
                    // public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')
                    public static final String KEY_GAMIL_ID = "facebook_id";*/


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


    public void loginNew(Result<TwitterSession> result) {

//Creating a twitter session with result's data
        final TwitterSession session = result.data;


//Getting the username from session
        final String username = session.getUserName();
        final TwitterAuthToken authToken = session.getAuthToken();

        String token = authToken.token;
        String secret = authToken.secret;
        Log.e("SESSION", username + " " + authToken);

        getUserData(session, username);

    }

    public void getUserData(TwitterSession session, final String username) {
        Call<User> userResult = TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, true);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User user = result.data;
                String twitterImage = user.profileImageUrl;
                try {
                    System.out.println("{" + " id : " + user.idStr + " name : " + user.name + " followers : " + String.valueOf(user.followersCount) + " createdAt : " + user.createdAt + " username :" + username + "}");
                    Log.d("useridid", user.idStr);
                    Log.d("name", user.name);
                    Log.d("followers ", String.valueOf(user.followersCount));
                    Log.d("createdAt", user.createdAt);
                    String[] name = user.name.split(" ");
                    Toast.makeText(LoginActivity.this, user.idStr + user.email + user.name, Toast.LENGTH_LONG).show();
                    LoginWithTwitter(user.idStr, user.email, user.name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void LoginWithTwitter(final String twitter_ids, final String email_username, final String full_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_LOGINWITH_TWITTER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //myDialog.dismiss();

                        Log.d("res_login_gmail", "is:\t" + response);
                        String university_name = "";
                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {

                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String user_id = jsonObject.getString("user_id");
                                    String user_role = jsonObject.getString("user_role");
                                    String full_name = jsonObject.getString("full_name");
                                    String email = jsonObject.getString("email");
                                    String profile_pic = jsonObject.getString("profile_pic");
                                    university_name = jsonObject.getString("university_name");
                                    String password = "";
                                    SharedPreference.profileSave(getApplicationContext(), user_id, full_name, user_role, profile_pic, "app", password, email);
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                final String finalUniversity_name = university_name;
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (finalUniversity_name.equals("") || finalUniversity_name.isEmpty()) {
                                                    SharedPreference.statusLogin(getApplicationContext(), "1");

                                                    Intent i_login = new Intent(LoginActivity.this, OrganizationOrSchoolNameActivity.class);
                                                    startActivity(i_login);
                                                } else {
                                                    Intent i_login = new Intent(LoginActivity.this, DashBoardActivity.class);
                                                    startActivity(i_login);
                                                }

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                        //myDialog.dismiss();
                        //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        String reason = AppUtils.getVolleyError(LoginActivity.this, error);
                        AlertUtility.showAlert(LoginActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_FULL_NAME, full_name);
                    params.put(AppConstant.KEY_EMAIL, email_username);
                    params.put(AppConstant.KEY_TWITTER_ID, twitter_ids);
                    params.put(AppConstant.KEY_DEVICE_TYPE, "android");


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

}
