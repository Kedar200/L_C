package com.example.laptopcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.laptopcontroller.data.Devices;
import com.example.laptopcontroller.data.dbhandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        Intent add_a_connection=new Intent(this,Add_a_connection.class);
        FloatingActionButton button=findViewById(R.id.add_more_page);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(add_a_connection);
            }
        });
        dbhandler db = new dbhandler(this);
        List<Devices> deviceList = db.getAll();

        for(Devices devices: deviceList){
            devicesArrayList.add(devices);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(start.this, devicesArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}