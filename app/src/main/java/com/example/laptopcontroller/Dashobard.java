package com.example.laptopcontroller;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.splashscreen.SplashScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Dashobard extends AppCompatActivity {

    String SERVER_IP;
    int SERVER_PORT;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        getSupportActionBar().hide();
        SERVER_IP="192.168.29.121";
        SERVER_PORT=1983;
        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
        Thread connect = new Thread(new connect());
        connect.start();
        Switch s=findViewById(R.id.shutdown);



    }
    private PrintWriter output;
    private BufferedReader input;
    class connect implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Thread sendmsg=new Thread(new sendmsg("Hello"));
                sendmsg.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class sendmsg implements Runnable {
        private String message="Initial";
        sendmsg(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            try {
                output.write(message);
                output.close();
            }
            catch (Exception e){

            }

        }
    }

}
