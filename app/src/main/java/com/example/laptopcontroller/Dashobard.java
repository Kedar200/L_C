package com.example.laptopcontroller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.laptopcontroller.Network.Connect;
import com.example.laptopcontroller.data.dbhandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class Dashobard extends AppCompatActivity {

    String SERVER_IP;
    int SERVER_PORT;
    TextView state,percentage;
    Circleseek test;
    ProgressBar progressBar;
    View overlay;
    SwitchCompat s;
    ImageView screenshot;
    Timer t;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        SERVER_IP=getIntent().getStringExtra("Ip");
        SERVER_PORT=1983;

        Connect.ConnectListener listener=new Connect.ConnectListener() {

            @Override
            public void onConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overlay.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        t.scheduleAtFixedRate(new TimerTask(){
                            @Override
                            public void run(){
                                refreshdata();
                            }
                        },500,500);
                    }
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        overlay.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        test.setEnabled(false);
                        s.setEnabled(false);
                        t.cancel();
                        Toast.makeText(Dashobard.this, "Server Disconnected",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onMessageReceived(String message) {
                recived(message);
            }
        };
        Thread connect = new Thread(new Connect(SERVER_IP,SERVER_PORT,listener));
        connect.start();

            percentage=findViewById(R.id.percentage);
            screenshot=findViewById(R.id.Scrrenshot);
            state=findViewById(R.id.state);
            s=findViewById(R.id.shutdown);
            Button delete=findViewById(R.id.delete_conn);
            test=findViewById(R.id.test1);
            progressBar=findViewById(R.id.progressBar);
            overlay=findViewById(R.id.overlay);
            t= new Timer();


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbhandler db = new dbhandler(v.getContext());
                    db.deleteById(getIntent().getIntExtra("Id",0));
                    Dashobard.super.onBackPressed();
                }
            });

            test.setOnProgressChangeListener(new Circleseek.OnProgressChangeListener() {
                @Override
                public void onProgressChanged(float progress) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            percentage.setText((int)progress+"%");
                        }
                    });
                }

                @Override
                public void onstopchange(float progress) {

                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("function","Brightness");
                        obj.put("value",(int)progress);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Thread sendmsg=new Thread(new Connect.Sendmsg("Command",obj));
                    sendmsg.start();
                }
            });
            s.setTrackDrawable(getDrawable(R.drawable.track_bg));
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
                        state.setTextColor(Color.RED);
                    }
                }
            });




    }

    private void recived(String message) {

        JSONObject object;
        try {
            object =new JSONObject(message);

           String function=object.get("function").toString();
            if(function.equals("Screenshot")){
                String dataString = object.getString("data");
                String[] dataValues = dataString.replaceAll("[\\[\\]]", "").split(", ");

                byte[] imageBytes = new byte[dataValues.length];
                for (int i = 0; i < dataValues.length; i++) {
                    imageBytes[i] = (byte) Integer.parseInt(dataValues[i]);
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        screenshot.setImageBitmap(bitmap);

                    }
                });
            }
            else if(function.equals("Brightness")) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int a=object.getInt("value");
                            test.setProgress(a);
                            percentage.setText((int)a+"%");
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    @Override
    protected void onResume() {
        super.onResume();

    }


public void refreshdata(){

    JSONObject object=new JSONObject();
    try {
        object.put("function","getss");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Thread sendmsg=new Thread(new Connect.Sendmsg("Text",object));
    sendmsg.start();
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
    }
}