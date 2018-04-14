package hnwebproject.com.mlmp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hnwebproject.com.mlmp.Contants.AppConstant;
import hnwebproject.com.mlmp.R;

public class GPSForLifeFragment extends Fragment {
    RelativeLayout rl_unit;
    ImageView iv_back, iv_arrow;
    LinearLayout ll_listunit;
    String isClick = "1";
    TextView tv_gpsfor;

    public GPSForLifeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gpsfor_life, container, false);

        initView(view);

        Bundle bundle = this.getArguments();

        String str_gpsfor = bundle.getString(AppConstant.KEY_GPS_FOR);
        tv_gpsfor.setText(str_gpsfor);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });
        rl_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClick.equals("1")) {
                    ll_listunit.setVisibility(View.VISIBLE);
                    iv_arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    isClick="0";

                }
                else
                {
                    isClick="1";
                    ll_listunit.setVisibility(View.GONE);
                    iv_arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }

            }
        });
        return view;
    }

    private void initView(View view) {
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        ll_listunit = (LinearLayout) view.findViewById(R.id.ll_listunit);
        rl_unit = (RelativeLayout) view.findViewById(R.id.rl_unit);
        iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        tv_gpsfor=(TextView) view.findViewById(R.id.tv_gpsfor);
    }


}
