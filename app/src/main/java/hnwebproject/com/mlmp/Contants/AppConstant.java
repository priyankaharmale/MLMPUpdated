package hnwebproject.com.mlmp.Contants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hnwebmarketing on 1/19/2018.
 */

public class AppConstant {


    public static final String API_LOGIN = " http://tech599.com/tech599.com/johnrosh/mylifemypower/api/login.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    /*============================================Register==================================================*/
    public static final String API_REGISTER = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/registration.php";

    //public static final String KEY_USERNAME = "username";
    // public static final String KEY_PASSWORD = "password";
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DEVICE_TYPE = "device_type";// ('ios' OR 'android')
    public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')
    //public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')

    /*============================================ViewProfile==================================================*/
    public static final String API_VIEW_PROFILE = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/view_user_profile.php";
    //public static final String KEY_USERID = "user_id";

/*============================================EditProfile==================================================*/


    /*============================================EditProfile==================================================*/
    public static final String API_EDIT_PROFILE = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/edit_user_profile.php";

    public static final String KEY_USERID = "user_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_SERVICEID = "service_id";
    //public static final String KEY_USERNAME = "username";
    // public static final String KEY_FULL_NAME = "full_name";
    //public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";// ('ios' OR 'android')
    public static final String KEY_HEADLINE = "head_line";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_UNIVERSITY = "university_name";
    public static final String KEY_YEAR = "year";
    public static final String KEY_CITY_NAME = "city_name";
    public static final String KEY_PROFILEPIC = "profile_pic";
    public static final String KEY_YOUTUBE_LINK = "youtube_link";
    public static final String KEY_LINKEDIN_LINK = "linkedin_link";
    public static final String KEY_FACEBBOKJ_LINK = "facebook_link";
    public static final String KEY_INSTAGRAM_LINK = "instagram_link";
    public static final String KEY_TWITTER_LINK = "twitter_link";
    public static final String KEY_WEBSITE_LINK = "website";
    public static final String KEY_COMPANY1 = "company1";
    public static final String KEY_COMPANY2 = "company2";
    public static final String KEY_COMPANY3 = "company3";
    public static final String KEY_VIDEO_LINK = "video_url";


/*============================================GEt Uni==============================================================*/


    public static final String API_UPDATE_CITY = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/update_city_uni.php";

    /*public static final String KEY_UNIVERSITY= "university_name";
    public static final String KEY_YEAR= "year";
    public static final String KEY_CITY_NAME= "city_name";
    */


    public static final String API_GET_UNI = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/university_list.php";
    /*=================================================GEtCity=========================================================*/
    public static final String API_GET_CITY = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/city_list.php";
    public static final String API_PRODUCTS = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/product_list.php";
    public static final String API_SERVICE_LIST = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/service_list.php";
    public static final String API_SPEAKER_SERVICE = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/insert_service_discription.php";

    //public static final String KEY_USERID = "user_id";

    /************************************Product and Services************************************************/
    public static final String API_ADD_PRODUCT_CART = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/add_to_cart.php";
    public static final String API_REMOVE_PRODUCT_CART = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/remove_cart_product.php";

    public static final String KEY_PRODUCT_ID = "product_id";

    public static final String KEY_ADDTOCART_PRODUCT="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/get_cart_product_by_user_id.php";

    public static final String KEy_PRODUCT_BUYNOW="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/buy_now_products.php";
    public static final String KEY_ADDTOCART_PRODUCT_INCRDESC="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/qty_incress_decress.php";

    public static final String KEY_ADDTOCART_PRODUCT_COUNT="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/count_of_cart_products.php";
    public static final String KEY_PRODUCT_SINGLE_BUY="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/buy_single_product.php";

    /*------------------------------------Login with facebook-----------------------------*/
    public static final String API_LOGINWITH_FB = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/login_with_fb.php";

    //  public static final String KEY_FULL_NAME = "full_name";
    //   public static final String KEY_EMAIL = "email";
    // public static final String KEY_DEVICE_TYPE = "device_type";// ('ios' OR 'android')
    // public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')
    public static final String KEY_FACEBOOK_ID = "facebook_id"; // ('individual' OR 'organization')


    /*------------------------------------Login with gmail-----------------------------*/
    public static final String API_LOGINWITH_GMAIL = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/login_with_google.php";
    public static final String KEY_GAMIL_ID = "google_plus_id"; // ('individual' OR 'organization')

    //  public static final String KEY_FULL_NAME = "full_name";
    //   public static final String KEY_EMAIL = "email";
    // public static final String KEY_DEVICE_TYPE = "device_type";// ('ios' OR 'android')
    // public static final String KEY_USERROLE = "user_role"; // ('individual' OR 'organization')



    /**********************************Login with Twitter************************************************************/
    public static final String API_LOGINWITH_TWITTER = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/login_with_twittter.php";
    public static final String KEY_TWITTER_ID = "twitter_id"; // ('individual' OR 'organization')



    /*-------------------------------------Get Training centre----------------------------*/
    public static final String API_GET_TRAINING_CENTRE = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/training_center_list.php";
    public static final String API_GET_TRAINING_CENTREBELT = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/list_training_belt_service.php";
    public static final String API_GET_OTHERTRAINING = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/list_others_program.php";


    // public static final String KEY_USERID = "user_id";


    /*----------------------------------Insert Events-------------------------------*/
    public static final String API_INSERT_EVENT = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/insert_event_list_by_user_id.php";
    // public static final String KEY_USERID = "user_id";
    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_ADDRESS = "address";
    public static final String KEY_EVENT_DESCRIPTION = "description";
    public static final String KEY_EVENT_DATE = "event_date";
    public static final String KEY_EVENTID = "event_id";


    /*----------------------------------Insert Task-------------------------------*/
    public static final String API_INSERT_TASK = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/insert_todo_list_by_user_id.php";
    // public static final String KEY_USERID = "user_id";
    public static final String KEY_TASK_NAME = "name";
    public static final String KEY_TASK_NOTES = "notes";
    public static final String KEY_TASK_DATE = "date";

    /*----------------------------------fetch event-------------------------------*/
    public static final String API_FETCH_EVENT = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/fetch_event_list_by_user_id.php";
    public static final String KEY_DATE = "date";//[mm-dd-yyyy]
    public static final String EDIT_EVENT = "http://tech599.com/tech599.com/johnrosh/mylifemypower/api/edit_event_list_by_user_id.php";
    public static final String GET_DATEWISEEVENT_="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/fetch_event_list_by_date_and_user_id.php";
/********************************GPS For**********************************************/
    public static final String KEY_GPS_FOR = "KEY_GPS_FOR";
    public static final String KEY_BELT = "KEY_BELT";
    public static final String API_GPS_FOR_LIST="http://tech599.com/tech599.com/johnrosh/mylifemypower/api/fetch_categories.php";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


}
