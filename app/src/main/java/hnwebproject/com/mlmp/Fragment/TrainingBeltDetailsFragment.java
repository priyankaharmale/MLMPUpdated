package hnwebproject.com.mlmp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.Model.TrainingServicesModel;
import hnwebproject.com.mlmp.R;

public class TrainingBeltDetailsFragment extends Fragment {
    ImageView iv_belt, iv_back;
    TextView tv_beltname;
    TrainingServicesModel productModel;
    TextView tv_eventname,tv_location,tv_address,tv_date,tv_topic;

    public TrainingBeltDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_beltexp, container, false);
        Bundle bundle = this.getArguments();
        initView(view);
        productModel = (TrainingServicesModel) bundle.getSerializable(AppConstant.KEY_BELT);


        tv_beltname.setText(productModel.getPick_tranning());
        tv_eventname.setText(productModel.getEvent_name());
        tv_location.setText(productModel.getLocation());
        tv_address.setText(productModel.getAddress());
        tv_date.setText(productModel.getDate());
        tv_topic.setText(productModel.getTopic());


        if (productModel.getPick_tranning().equals("Red Belt")) {
            iv_belt.setBackgroundResource(R.drawable.red_belt_pic);
        } else if (productModel.getPick_tranning().equals("1st Degree Black Belt")) {
            iv_belt.setBackgroundResource(R.drawable.black_belt_pic);
        } else if (productModel.getPick_tranning().equals("White Belt")) {
            iv_belt.setBackgroundResource(R.drawable.white_belt_picture);
        } else if (productModel.getPick_tranning().equals("2nd Degree Black Belt")) {
            iv_belt.setBackgroundResource(R.drawable.black_belt_pic);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void initView(View view) {
        iv_belt = (ImageView) view.findViewById(R.id.iv_belt);
        tv_beltname = (TextView) view.findViewById(R.id.tv_beltname);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_eventname = (TextView) view.findViewById(R.id.tv_eventname);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_topic = (TextView) view.findViewById(R.id.tv_topic);


    }


}
