package hnwebproject.com.mlmp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;
import hnwebproject.com.mlmp.Utility.Validations;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_login;
    Button btn_sign_up;
    EditText et_password, et_email, et_username, et_name;
    TextInputLayout input_password, input_email, input_username, input_name;
   /* RadioGroup radioGroup;
    RadioButton rd_individual, rd_organization;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        // radioGroup = (RadioGroup) findViewById(R.id.radio_group_user_role);
        // rd_individual = (RadioButton) findViewById(R.id.rd_individual);
        // rd_organization = (RadioButton) findViewById(R.id.rd_organization);

        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_username = (EditText) findViewById(R.id.et_username);
        et_name = (EditText) findViewById(R.id.et_name);
        input_password = (TextInputLayout) findViewById(R.id.input_password);
        input_email = (TextInputLayout) findViewById(R.id.input_email);
        input_username = (TextInputLayout) findViewById(R.id.input_username);
        input_name = (TextInputLayout) findViewById(R.id.input_name);
        tv_login = (TextView) findViewById(R.id.tv_login);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        tv_login.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);

        tv_login.setPaintFlags(tv_login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_sign_up:


                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(RegisterActivity.this)) {


                        String password = et_password.getText().toString();
                        String email = et_email.getText().toString();
                        String username_ = et_username.getText().toString();
                        String name = et_name.getText().toString();
                        String user_role = "user_role";

                       /* // get selected radio button from radioGroup
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        user_role = radioButton.getText().toString();
*/
                        // Toast.makeText(RegisterActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                        register(name, username_, email, password, user_role);
                    } else {
                        Utils.myToast1(RegisterActivity.this);
                    }
                }


                break;
        }
    }

    private void register(final String name, final String username, final String email, final String password, final String user_role) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_REGISTER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //myDialog.dismiss();

                        System.out.println("res_register" + response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {


                                //Intent i_login = new Intent(Activity_register.this, Activity_Edit_Profile.class);
                               /* i_login.putExtra("full_name", full_name);
                                i_login.putExtra("email_id", email_id);
                                i_login.putExtra("gender", gender);*/


                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                               /* Intent i_login = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i_login);*/


                                                Intent i_getuniversicity_name = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i_getuniversicity_name);

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

                        String reason = AppUtils.getVolleyError(RegisterActivity.this, error);
                        AlertUtility.showAlert(RegisterActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {

                    params.put(AppConstant.KEY_FULL_NAME, name);
                    params.put(AppConstant.KEY_EMAIL, email);
                    params.put(AppConstant.KEY_PASSWORD, password);
                    params.put(AppConstant.KEY_USERNAME, username);
                    params.put(AppConstant.KEY_USERROLE, user_role);
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


    private boolean checkValidation() {

        boolean ret = true;

        if (!Validations.hasText_input_layout(et_name, "Please Enter Name", input_name))
            ret = false;

        if (!Validations.hasText_input_layout(et_username, "Please Enter Username", input_username))
            ret = false;

        if (!Validations.hasText_input_layout(et_password, "Please Enter Password", input_password))
            ret = false;

        if (!Validations.hasText_input_layout(et_email, "Please Enter Email ID ", input_email))
            ret = false;

        if (!Validations.check_text_length_7_text_layout(et_password, "Password atleast 7 characters", input_password))
            ret = false;

        if (!Validations.isEmailAddress_input_layout(et_email, true, "Please Enter Valid Email ID", input_email))
            ret = false;
        if (!Validations.check_text_length(et_name, input_name))
            ret = false;
        if (!Validations.check_text_length(et_username, input_username))
            ret = false;
     /*   int id_radio_group = radioGroup.getCheckedRadioButtonId();
        if (id_radio_group == -1 || id_radio_group == 0) {

            AlertUtility.showAlert(this, "Please select Individual or Organization");
            ret = false;
        }
*/

        return ret;
    }

}
