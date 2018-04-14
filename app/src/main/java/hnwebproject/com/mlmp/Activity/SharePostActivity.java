package hnwebproject.com.mlmp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import hnwebproject.com.mlmp.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by neha on 3/27/2018..
 */

public class SharePostActivity extends AppCompatActivity {
    private static final String host = "api.linkedin.com";

    private static final String topCardUrl = "https://" + host + "/v1/people/~:(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private static final String shareUrl = "https://api.linkedin.com/v1/people/~/shares";
    ProgressDialog progress;
    final int callbackId = 42;
    ImageButton ib_lin, ib_twitter, ib_facebook, ib_gmail, ib_insta;
    EditText shareComment;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_sharepost);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ib_lin = (ImageButton) findViewById(R.id.ib_lin);
        ib_twitter = (ImageButton) findViewById(R.id.ib_twitter);
        ib_facebook = (ImageButton) findViewById(R.id.ib_facebook);
        ib_gmail = (ImageButton) findViewById(R.id.ib_gmail);
        ib_insta = (ImageButton) findViewById(R.id.ib_insta);
        shareComment = (EditText) findViewById(R.id.et_sharepost);
        shareDialog = new ShareDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //getUserData();


        ib_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating a string to share on LinkedIn profile

                if (shareComment.getText().toString().equals("")) {
                    shareComment.setError("Please Enter the Post");
                } else {

                    login_linkedin();
                    //  share();
                }

            }
        });

        ib_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareComment.getText().toString().equals("")) {
                    shareComment.setError("Please Enter the Post");
                } else {
                    Intent shareIntent = new PlusShare.Builder(SharePostActivity.this)
                            .setType("text/plain")
                            .setText(shareComment.getText().toString())
                            .getIntent();

                    startActivityForResult(shareIntent, 0);
                }
            }
        });

        ib_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shareComment.getText().toString().equals("")) {
                    shareComment.setError("Please Enter the Post");
                } else {

                    TweetComposer.Builder builder = new TweetComposer.Builder(SharePostActivity.this)
                            .text(shareComment.getText().toString());
                    builder.show();
                    //  share();
                }

            }
        });

        ib_facebook.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCheckPermission")
            @Override
            public void onClick(View view) {

                if (shareComment.getText().toString().equals("")) {
                    shareComment.setError("Please Enter the Post");
                } else {


                    // checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);

                    //   pushAppointmentsToCalender(SharePostActivity.this, "sdfsd", "sdfsd", "Pune", 1, 12 - 04 - 2018, true, true);

                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(shareComment.getText().toString())

                                // .setImageUrl(Uri.parse("https://www.numetriclabz.com/wp-content/uploads/2015/11/114.png"))
                                .build();
                        shareDialog.show(linkContent);  // Show facebook ShareDialog
                    }
                }
            }
        });

        ib_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareComment.getText().toString().equals("")) {
                    shareComment.setError("Please Enter the Post");
                }
                {
                    addToCalendar(SharePostActivity.this, "dfsdf", 2018-04-8 ,2018-04-8 );
                }/*else {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareComment.getText().toString());
                    shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);
                }*/
            }
        });

    }

    /*Once User's can authenticated,
      It make an HTTP GET request to LinkedIn's REST API using the currently authenticated user's credentials.
      If successful, A LinkedIn ApiResponse object containing all of the relevant aspects of the server's response will be returned.
     */
    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(SharePostActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    //  setUserProfile(result.getResponseDataAsJson());
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());
            }
        });
    }


    public void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Toast.makeText(SharePostActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                //Toast.makeText(getApplicationContext(), + error.toString(),
                //    Toast.LENGTH_LONG).show();
                Toast.makeText(SharePostActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }, true);
    }

    // This method is used to make permissions to retrieve data from linkedin</p>
    private static Scope buildScope() {
        return Scope.build(Scope.W_SHARE);
        //return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.R_FULLPROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);
        Log.i("Access token->", LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
        Toast.makeText(SharePostActivity.this, LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue(), Toast.LENGTH_SHORT).show();
        System.out.println("AccessToke...." + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().getValue());
        share();
    }

    public void share() {
/*
        String shareText = shareComment.getText().toString();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text*//**//*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);

        boolean installed = checkAppInstall("com.twitter.android");
        if (installed) {
            intent.setPackage("com.twitter.android");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Installed application first", Toast.LENGTH_LONG).show();
        }*/


        /*  Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setClassName("com.linkedin.android",
                "hnwebproject.com.mlmp.Activity.Activity.SharePostActivity");
        shareIntent.setType("text*//*");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        startActivity(shareIntent);*/

        //String shareJsonText = shareComment.getText().toString();
        String shareJsonText = "{ \n" +
                "  \"comment\":\"" + shareComment.getText() + "\"," +
                "   \"visibility\":{ " +
                "      \"code\":\"anyone\"" +
                "   }," +
                "   \"content\":{ " +
                "      \"title\":\"Android LinkedIn Integration/Login and Make User Profile\"," +
                "      \"description\":\"Login Integration with LinkedIn\"," +
                "      \"submitted-url\":\"https://www.numetriclabz.com/android-linkedin integrationlogin-and-make-userprofile\"," +
                "      \"submitted-image-url\":\"https://www.numetriclabz.com/?attachment_id=11320\"" +
                "   }" +
                "}";
        // Call the APIHealper.getInstance method and pass the current context.
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        // call the apiHelper.postRequest with argument(Current context,url and content)
        apiHelper.postRequest(SharePostActivity.this,
                shareUrl, shareJsonText, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse apiResponse) {
                        Log.e(" Response", apiResponse.toString());
                        Toast.makeText(getApplicationContext(), " Shared Sucessfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onApiError(LIApiError error) {
                        Log.e(" Response", error.toString());
                        Toast.makeText(getApplicationContext(), " Error", Toast.LENGTH_LONG).show();
                    }
                });
    }


    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: note(without alert) *******************/
        Toast.makeText(curActivity, "dsfbhsdf", Toast.LENGTH_SHORT).show();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1); // id, We need to choose from
        // our mobile for primary
        // its 1
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):
        eventValues.put("eventTimezone", "UTC/GMT +2:00");
   /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }
        System.out.println("EventDatedfds" + eventID);
        return eventID;

    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

    public long addAppointmentsToCalender(Activity curActivity, String title,
                                          String desc, String place, int status, long startDate,
                                          boolean needReminder, boolean needMailService) {
/***************** Event: add event *******************/
        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", title);
            eventValues.put("description", desc);
            eventValues.put("eventLocation", place);

            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);

            // values.put("allDay", 1); //If it is bithday alarm or such
            // kind (which should remind me for whole day) 0 for false, 1
            // for true
            eventValues.put("eventStatus", status); // This information is
            // sufficient for most
            // entries tentative (0),
            // confirmed (1) or canceled
            // (2):
            eventValues.put("eventTimezone", "UTC/GMT +5:30");
 /*
  * Comment below visibility and transparency column to avoid
  * java.lang.IllegalArgumentException column visibility is invalid
  * error
  */
            // eventValues.put("allDay", 1);
            // eventValues.put("visibility", 0); // visibility to default (0),
            // confidential (1), private
            // (2), or public (3):
            // eventValues.put("transparency", 0); // You can control whether
            // an event consumes time
            // opaque (0) or transparent (1).

            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

            Uri eventUri = curActivity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());

            if (needReminder) {
                /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                reminderValues.put("minutes", 5); // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                Uri reminderUri = curActivity.getApplicationContext()
                        .getContentResolver()
                        .insert(Uri.parse(reminderUriString), reminderValues);
            }

/***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

            if (needMailService) {
                String attendeuesesUriString = "content://com.android.calendar/attendees";
                /********
                 * To add multiple attendees need to insert ContentValues
                 * multiple times
                 ***********/
                ContentValues attendeesValues = new ContentValues();
                attendeesValues.put("event_id", eventID);
                attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
                attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee Email
                attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
                // Relationship_None(0),
                // Organizer(2),
                // Performer(3),
                // Speaker(4)
                attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
                // Required(2),
                // Resource(3)
                attendeesValues.put("attendeeStatus", 0); // NOne(0),
                // Accepted(1),
                // Decline(2),
                // Invited(3),
                // Tentative(4)

                Uri eventsUri = Uri.parse("content://calendar/events");
                Uri url = curActivity.getApplicationContext()
                        .getContentResolver()
                        .insert(eventsUri, attendeesValues);

                // Uri attendeuesesUri = curActivity.getApplicationContext()
                // .getContentResolver()
                // .insert(Uri.parse(attendeuesesUriString), attendeesValues);
            }
        } catch (Exception ex) {
            //     log.error("Error in adding event on calendar" + ex.getMessage());
        }

        return eventID;

    }

    private static void addToCalendar(Context ctx, final String title, final long dtstart, final long dtend) {
        final ContentResolver cr = ctx.getContentResolver();
        Cursor cursor ;
        if (Integer.parseInt(Build.VERSION.SDK) >= 8 )
            cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), new String[]{ "_id", "displayname" }, null, null, null);
        else
            cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id", "displayname" }, null, null, null);
        if ( cursor.moveToFirst() ) {
            final String[] calNames = new String[cursor.getCount()];
            final int[] calIds = new int[cursor.getCount()];
            for (int i = 0; i < calNames.length; i++) {
                calIds[i] = cursor.getInt(0);
                calNames[i] = cursor.getString(1);
                cursor.moveToNext();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setSingleChoiceItems(calNames, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues cv = new ContentValues();
                    cv.put("calendar_id", calIds[which]);
                    cv.put("title", title);
                    cv.put("dtstart", dtstart );
                    cv.put("hasAlarm", 1);
                    cv.put("dtend", dtend);

                    Uri newEvent ;
                    if (Integer.parseInt(Build.VERSION.SDK) >= 8 )
                        newEvent = cr.insert(Uri.parse("content://com.android.calendar/events"), cv);
                    else
                        newEvent = cr.insert(Uri.parse("content://calendar/events"), cv);

                    if (newEvent != null) {
                        long id = Long.parseLong( newEvent.getLastPathSegment() );
                        ContentValues values = new ContentValues();
                        values.put( "event_id", id );
                        values.put( "method", 1 );
                        values.put( "minutes", 15 ); // 15 minutes
                        if (Integer.parseInt(Build.VERSION.SDK) >= 8 )
                            cr.insert( Uri.parse( "content://com.android.calendar/reminders" ), values );
                        else
                            cr.insert( Uri.parse( "content://calendar/reminders" ), values );

                    }
                    dialog.cancel();
                }

            });

            builder.create().show();
        }
        cursor.close();
    }
}
