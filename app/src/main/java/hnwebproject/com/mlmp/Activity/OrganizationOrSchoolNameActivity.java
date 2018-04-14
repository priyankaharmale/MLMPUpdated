package hnwebproject.com.mlmp.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.City;
import hnwebproject.com.mlmp.Model.University;
import hnwebproject.com.mlmp.R;
import hnwebproject.com.mlmp.Utility.AlertUtility;
import hnwebproject.com.mlmp.Utility.AppUtils;
import hnwebproject.com.mlmp.Utility.Utils;


public class OrganizationOrSchoolNameActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = OrganizationOrSchoolNameActivity.class.getSimpleName();
    Button btn_next;
    ArrayList<University> arrayList;
    ArrayList<City> city_arrayList;
    CustomDialogList cd_Child;
    CustomDialogListCity cdCity;
    TextView tv_sel_uni, tv_sel_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_or_school_name);

        getUni();
        getCity();

        initView();
    }

    private void initView() {

        tv_sel_uni = (TextView) findViewById(R.id.tv_sel_uni);
        tv_sel_city = (TextView) findViewById(R.id.tv_sel_city);

        tv_sel_city.setOnClickListener(this);
        tv_sel_uni.setOnClickListener(this);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                if (tv_sel_uni.getText().equals("")) {
                    Toast.makeText(this, "Please Select the University", Toast.LENGTH_SHORT).show();
                } else if (tv_sel_city.getText().equals("")) {
                    Toast.makeText(this, "Please Select the City", Toast.LENGTH_SHORT).show();

                } else {
                    Intent i = new Intent(OrganizationOrSchoolNameActivity.this, GetYearActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Univercity", tv_sel_uni.getText().toString());
                    bundle.putString("City", tv_sel_city.getText().toString());
                    i.putExtras(bundle);
                    startActivity(i);
                }

                break;


            case R.id.tv_sel_city:

                cdCity = new CustomDialogListCity(OrganizationOrSchoolNameActivity.this, city_arrayList, tv_sel_city);
                cdCity.show();

                break;

            case R.id.tv_sel_uni:

                cd_Child = new CustomDialogList(OrganizationOrSchoolNameActivity.this, arrayList);
                cd_Child.show();

                break;


        }
    }

    public class CustomDialogList extends Dialog {

        public Activity c;
        ListView listView;
        ArrayList<University> arrayList;
        TextView tv_select_title;

        public CustomDialogList(Activity a, ArrayList<University> list) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.arrayList = list;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.custom_spinner);

            listView = (ListView) findViewById(R.id.list_view_custom_list);

            tv_select_title = (TextView) findViewById(R.id.txt_dia);
            tv_select_title.setText("Select University");


            listView.setAdapter(new CustomDialogList.adapterCustomDialogList(OrganizationOrSchoolNameActivity.this, arrayList));

        }


        public class adapterCustomDialogList extends BaseAdapter {


            Context context;
            ArrayList<University> arrayList;
            private LayoutInflater inflater = null;

            public adapterCustomDialogList(Activity mainActivity, ArrayList<University> arrayList) {
                // TODO Auto-generated constructor stub

                context = mainActivity;
                this.arrayList = arrayList;
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return arrayList.size();


            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return arrayList.get(position);

            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            //changes
            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            public class Holder {
                TextView tv_Catagory_Title, tv_catagory_title_list_item;

            }

            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                // TODO Auto-generated method stub
                final Holder holder = new adapterCustomDialogList.Holder();
                final View rowView;

                int type = getItemViewType(position);

                rowView = inflater.inflate(R.layout.item_custom_dialog_list_view, null);


                holder.tv_Catagory_Title = (TextView) rowView.findViewById(R.id.tv_catagory_title_list_item);

                holder.tv_Catagory_Title.setText(arrayList.get(position).getUniName());

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tv_sel_uni.setText(arrayList.get(position).getUniName());
                        // selected_child_id = arrayList.get(position).getTchid();

                        cd_Child.dismiss();
                    }
                });
                return rowView;
            }
        }


    }

    public class CustomDialogListCity extends Dialog {

        public Activity c;
        ListView listView;
        ArrayList<City> arrayList;
        TextView tv_select_title, tv_sel_city;

        public CustomDialogListCity(Activity a, ArrayList<City> list, TextView tv_sel_city) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.arrayList = list;
            this.tv_sel_city = tv_sel_city;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.custom_spinner);

            listView = (ListView) findViewById(R.id.list_view_custom_list);

            tv_select_title = (TextView) findViewById(R.id.txt_dia);
            tv_select_title.setText("Select City");

            listView.setAdapter(new adapterCustomDialogList(OrganizationOrSchoolNameActivity.this, arrayList, tv_sel_city));
        }


        public class adapterCustomDialogList extends BaseAdapter {

            Context context;
            ArrayList<City> arrayList;
            TextView textView;
            private LayoutInflater inflater = null;

            public adapterCustomDialogList(Activity mainActivity, ArrayList<City> arrayList, TextView tv_sel_city) {
                // TODO Auto-generated constructor stub

                context = mainActivity;
                this.arrayList = arrayList;
                this.textView = tv_sel_city;
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return arrayList.size();


            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return arrayList.get(position);

            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            //changes
            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            public class Holder {
                TextView tv_Catagory_Title;

            }

            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                // TODO Auto-generated method stub
                final Holder holder = new adapterCustomDialogList.Holder();
                final View rowView;

                int type = getItemViewType(position);

                rowView = inflater.inflate(R.layout.item_custom_dialog_list_view, null);


                holder.tv_Catagory_Title = (TextView) rowView.findViewById(R.id.tv_catagory_title_list_item);

                holder.tv_Catagory_Title.setText(arrayList.get(position).getCityName());

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        textView.setText(arrayList.get(position).getCityName());
                        // selected_child_id = arrayList.get(position).getTchid();

                        cdCity.dismiss();
                    }
                });
                return rowView;
            }
        }


    }

    private void getUni() {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_UNI,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_uni", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                JSONArray jsonArray = j.getJSONArray("response");
                                arrayList = new ArrayList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    University university = new University();
                                    university.setUniName(jsonObject.getString("university"));
                                    arrayList.add(university);
                                }

                                Log.d(TAG, "university" + arrayList.size());

                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrganizationOrSchoolNameActivity.this);
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

                        String reason = AppUtils.getVolleyError(OrganizationOrSchoolNameActivity.this, error);
                        AlertUtility.showAlert(OrganizationOrSchoolNameActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {


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

    private void getCity() {
        final ProgressDialog myDialog = Utils.DialogsUtils.showProgressDialog(this, getString(R.string.processing));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_GET_CITY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();

                        Log.d("res_uni", response);

                        try {
                            JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");


                            if (message_code == 1) {
                                JSONArray jsonArray = j.getJSONArray("response");
                                city_arrayList = new ArrayList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    City university = new City();
                                    university.setCityName(jsonObject.getString("city"));
                                    city_arrayList.add(university);
                                }

                                Log.d(TAG, "city" + city_arrayList.size());
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrganizationOrSchoolNameActivity.this);
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

                        String reason = AppUtils.getVolleyError(OrganizationOrSchoolNameActivity.this, error);
                        AlertUtility.showAlert(OrganizationOrSchoolNameActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {


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

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
