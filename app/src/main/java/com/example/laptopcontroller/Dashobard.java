package com.example.laptopcontroller;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.splashscreen.SplashScreen;

import com.example.laptopcontroller.data.dbhandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Dashobard extends AppCompatActivity {

    String SERVER_IP;
    int SERVER_PORT;
    boolean CONNECTED=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        SERVER_IP=getIntent().getStringExtra("Ip");
        SERVER_PORT=1983;

        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
        Thread connect = new Thread(new connect());


        connect.start();
        SystemClock.sleep(1000);

        if(CONNECTED){
            Toast.makeText(this,"Yeah You Connected",Toast.LENGTH_SHORT).show();

            setContentView(R.layout.dashboard);
            TextView state=findViewById(R.id.state);
            Switch s=findViewById(R.id.shutdown);
            Button delete=findViewById(R.id.delete_conn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbhandler db = new dbhandler(v.getContext());
                    db.deleteById(getIntent().getIntExtra("Id",0));

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
                        Thread connect = new Thread(new connect());
                        connect.start();

                        Thread sendmsg=new Thread(new sendmsg("Hi","shutdown"));
                        sendmsg.start();
                        state.setText("Stopped");
                        state.setTextColor(getResources().getColor(R.color.red));


                    }
                }
            });


        }
        else{
            Toast.makeText(this,"Sad Life Check Your Network",Toast.LENGTH_SHORT).show();

            onBackPressed();
        }


    }
    private PrintWriter output;
    private BufferedReader input;

    class connect implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                CONNECTED=true;
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Thread sendmsg=new Thread(new sendmsg("Text","You Are Connected to"+socket.getLocalAddress() ));
                sendmsg.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class sendmsg implements Runnable {
        private String type="Initial";
        private String data="No data";

        sendmsg(String type,String data) {
            this.type = type;
            this.data=data;
        }
        @Override
        public void run() {
            JSONObject object=new JSONObject();

            try {
                object.put("type",type);
                object.put("data",data);
                output.write(object.toString());
                output.close();
            }
            catch (Exception e){

            }

        }
    }

}
