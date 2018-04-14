package hnwebproject.com.mlmp.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import hnwebproject.com.mlmp.R;


/**
 * Created by hnweb on 24-Jun-17.
 */

public class AlertUtility {

    public static void showAlert(Context context, String msg) {
        if (context == null) {
            return;
        }
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fb_login_icon);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(drawable);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showAlertwithTitle(Context context, String msg, String title) {
        if (context == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showAlert(Activity  activity_login, boolean b, String s) {
        if (activity_login == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_login);

       // builder.setTitle(title);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
