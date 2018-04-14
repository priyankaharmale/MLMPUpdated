package hnwebproject.com.mlmp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.TrainingServicesModel;
import hnwebproject.com.mlmp.R;

public class OtherProgramDetailsFragment extends Fragment {
    TrainingServicesModel productModel;
    TextView tv_eventname, tv_location, tv_address, tv_date, tv_topic, tv_amounttime, tv_Audience;

    public OtherProgramDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otherprogramdetails, container, false);
        Bundle bundle = this.getArguments();
        initView(view);
        productModel = (TrainingServicesModel) bundle.getSerializable(AppConstant.KEY_BELT);


        tv_eventname.setText(productModel.getEvent_name());
        tv_location.setText(productModel.getLocation());
        tv_address.setText(productModel.getAddress());
        tv_date.setText(productModel.getDate());
        tv_topic.setText(productModel.getTopic());
        tv_Audience.setText(productModel.getAudience());
        tv_amounttime.setText(productModel.getAmount_of_time());


        return view;
    }

    private void initView(View view) {
        tv_eventname = (TextView) view.findViewById(R.id.tv_eventname);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_topic = (TextView) view.findViewById(R.id.tv_topic);
        tv_amounttime = (TextView) view.findViewById(R.id.tv_amounttime);
        tv_Audience = (TextView) view.findViewById(R.id.tv_Audience);


    }


}
