package hnwebproject.com.mlmp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Adapter.EventAdapter;
import hnwebproject.com.mlmp.Model.EventModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by neha on 3/10/2018..
 * Developer :- Ganesh Kulkarni.
 */

public class EventListActivity extends AppCompatActivity {
    ArrayList<EventModel> eventModels;
    ImageButton  ib_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        System.out.println("Hello");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ib_edit = (ImageButton) toolbar.findViewById(R.id.ib_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(EventListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        if (getIntent().hasExtra("EventList")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                eventModels = (ArrayList<EventModel>) bundle.getSerializable("EventList");

            } else {
                Log.e("null", "null");
            }
        }
        EventAdapter adapter = new EventAdapter(eventModels, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
