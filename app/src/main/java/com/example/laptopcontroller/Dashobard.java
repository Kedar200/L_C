package com.example.laptopcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.laptopcontroller.Network.Connect;
import com.example.laptopcontroller.data.dbhandler;

import org.json.JSONException;
import org.json.JSONObject;


public class Dashobard extends AppCompatActivity {

    String SERVER_IP;
    int SERVER_PORT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        SERVER_IP=getIntent().getStringExtra("Ip");
        SERVER_PORT=1983;


        Connect.ConnectListener listener=new Connect.ConnectListener() {


            @Override
            public void onConnected() {
                Log.d("Hello","Connect");

            }

            @Override
            public void onDisconnected() {
                Log.d("Hello","Disconnected");

            }

            @Override
            public void onMessageReceived(String message) {
                Log.d("Hello",message);
            }
        };
        Thread connect = new Thread(new Connect(SERVER_IP,SERVER_PORT,listener));
        connect.start();


            TextView state=findViewById(R.id.state);
            Switch s=findViewById(R.id.shutdown);
            Button delete=findViewById(R.id.delete_conn);
            SeekBar test=findViewById(R.id.test1);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbhandler db = new dbhandler(v.getContext());
                    db.deleteById(getIntent().getIntExtra("Id",0));

                }
            });

            test.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("function","Brightness");
                        obj.put("value",seekBar.getProgress());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Thread sendmsg=new Thread(new Connect.Sendmsg("Command",obj));
                    sendmsg.start();

                }
            });

            s.setChecked(true);
            s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(s.isChecked()){
                        String text=getIntent().getStringExtra("device_name")+" is turned off";
                        Toast.makeText(Dashobard.this,text,Toast.LENGTH_LONG).show();

                    }
                    else{
                        s.setEnabled(false);
                        JSONObject object=new JSONObject();
                        try {
                            object.put("function","lock");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Thread sendmsg=new Thread(new Connect.Sendmsg("Text",object));
                        sendmsg.start();
                        state.setText("Stopped");
                        state.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            });




    }



}
