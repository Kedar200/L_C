package com.example.laptopcontroller;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.splashscreen.SplashScreen;

public class Dashobard extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        getSupportActionBar().hide();

//        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
//        v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));

    }
}
