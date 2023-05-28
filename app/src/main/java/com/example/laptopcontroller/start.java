package com.example.laptopcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.laptopcontroller.data.Devices;
import com.example.laptopcontroller.data.dbhandler;

import java.util.ArrayList;
import java.util.List;

public class start extends AppCompatActivity {
    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Devices> devicesArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        devicesArrayList = new ArrayList<>();
        dbhandler db = new dbhandler(this);
        List<Devices> deviceList = db.getAll();

        for(Devices devices: deviceList){
            devicesArrayList.add(devices);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(start.this, devicesArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}