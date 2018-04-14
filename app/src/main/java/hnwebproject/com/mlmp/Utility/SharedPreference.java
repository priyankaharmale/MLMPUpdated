package hnwebproject.com.mlmp.Utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreference {

    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_NAME_REMBER_ME = "AOP_PREFS_REM_ME";
    public static final String PREFS_KEY = "isLogin";

    public SharedPreference() {
        super();
    }





    public static Boolean IsLogin(Context context) {
        SharedPreferences settings;
        Boolean isLogin;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isLogin = settings.getBoolean(PREFS_KEY, false);
       return  isLogin;
    }


    public static void profileSaveRemberMe(Context context, String social_id, String user_id, String login_through, String rember_me, String password, String email, String full_name) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME_REMBER_ME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("social_id", social_id);
        editor.putString("user_id", user_id);
        editor.putString("full_name", full_name);
        editor.putString("login_through", login_through);
        editor.putString("rember_me", rember_me);
        editor.putString("password", password);
        editor.putString("email", email);
        editor.commit();
    }





    public static void profileSave(Context context, String user_id, String full_name, String user_role, String profile_pic,String login_through,String  password,String email) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("user_id", user_id);
        editor.putString("full_name", full_name);
        editor.putString("login_through", login_through);
        editor.putString("email_address", email);
        editor.putString("user_role", user_role);
        editor.putString("profile_photo", profile_pic);
        editor.putString("password", password);
        editor.putBoolean("login",true);

        editor.commit();
    }


    public static void profileSaveAfterupdate(Context context,  String full_name, String profile_pic,String email) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("full_name", full_name);
        editor.putString("email_address", email);
        editor.putString("profile_photo", profile_pic);
        editor.putBoolean("login",true);

        editor.commit();
    }



    ///////////////Logout///////////////////////////////////////////////////////

    public static void logout(Context context, boolean login) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putBoolean("login",false);
        //editor.remove("user_id");

        editor.putString("user_id","");
        editor.putString("user_name", "");
        editor.putString("email_id", "");
        editor.putString("phone", "");
        editor.putString("date_of_birth", "");
        editor.putString("profile_photo", "");
        editor.commit();
    }

    //////////////////////////////////////////////////////////////////////
    public static void user_type(Context context, String user_type) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("user_type", user_type);


        editor.commit();
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public static void clearSharedPreferenceRemember(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME_REMBER_ME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }



    public static void editProfiledData(Context context, String user_name, String email_id, String gender, String profile_photo)
    {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2


        editor.putString("full_name", user_name);
        editor.putString("email_address", email_id);
        editor.putString("gender", gender);
        editor.putString("profile_photo", profile_photo);


        editor.commit();

    }

    public static void editUnseencount(Context context, String unseenCount)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("unseen_count", unseenCount);
        Log.d("unseen_count123",""+unseenCount);
        editor.commit();

    }

    public static void statusLogin(Context context, String status)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString("status", status);
        Log.d("unseen_count123",""+status);
        editor.commit();

    }

}