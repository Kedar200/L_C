package com.example.laptopcontroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import com.example.laptopcontroller.data.Devices;
import com.example.laptopcontroller.data.dbhandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class start extends AppCompatActivity {
    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
            final ObjectAnimator slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.getHeight()
            );
            slideUp.setInterpolator(new AnticipateInterpolator());
            slideUp.setDuration(200L);

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.addListener(new AnimatorListenerAdapter() {
                @RequiresApi(api = Build.VERSION_CODES.S)
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenView.remove();
                }
            });

            // Run your animation.
            slideUp.start();
        });
        setContentView(R.layout.activity_start);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent add_a_connection=new Intent(this,Add_a_connection.class);
        FloatingActionButton button=findViewById(R.id.add_more_page);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(add_a_connection);
            }
        });
        refesh();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refesh();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refesh();
    }

    public void refesh(){
        dbhandler db = new dbhandler(this);
        List<Devices> deviceList = db.getAll();
        ArrayList<Devices> devicesArrayList=new ArrayList<>();
        for(Devices devices: deviceList){
            devicesArrayList.add(devices);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(start.this, devicesArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refesh();
    }
}