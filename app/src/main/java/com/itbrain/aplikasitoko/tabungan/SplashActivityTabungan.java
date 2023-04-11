package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;
//import com.komputerkit.aplikasitabunganplus.Kunci.AktivasiDemo;
//import com.komputerkit.aplikasitabunganplus.Kunci.Lisensi;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;

public class SplashActivityTabungan extends AppCompatActivity {
    public static boolean status = false;
    public static  boolean statusdemo =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_tabungan);
        getSupportActionBar().hide();
        try{
//            status = LisensiBaru.checkLisence(this);
//            statusdemo = Lisensi.checkLisenceDemo(this);
        }catch (Exception e){

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                status = true;
                // This method will be executed once the timer is over
                // Start your app main activity
                // close this activity
                if(!statusdemo && !status){
//                    Intent intent = new Intent(SplashActivityTabungan.this, AktivasiDemo.class);
//                    startActivity(intent);
//                    finish();
                }else {
                    PrefTabungan sp = new PrefTabungan(getSharedPreferences("device_index", MODE_PRIVATE));
                    if (sp.getFirst()) {
//                        Intent intent = new Intent(SplashActivityTabungan.this, IntroActivity.class);
//                        startActivity(intent);
//                        finish();
                        // Show Intro Activity
                    } else {
                        Intent intent = new Intent(SplashActivityTabungan.this, MainActivityTabungan.class);
//                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 2000); // duration
    }
}
