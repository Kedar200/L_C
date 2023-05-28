package com.example.laptopcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.laptopcontroller.data.Devices;
import com.example.laptopcontroller.data.dbhandler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class Add_a_connection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aconnection);
        getSupportActionBar().hide();
//        Intent dashboard=new Intent(this,Dashobard.class);
//        startActivity(dashboard);
        dbhandler db = new dbhandler(this);
        Button btn=findViewById(R.id.addmore_btn);
        TextInputLayout name=findViewById(R.id.name);
        TextInputLayout addr=findViewById(R.id.addr);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addDevice(new Devices(name.getEditText().getText().toString(),addr.getEditText().getText().toString()));
            }
        });

    }
}