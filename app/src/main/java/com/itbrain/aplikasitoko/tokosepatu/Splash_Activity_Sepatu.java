package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class Splash_Activity_Sepatu extends AppCompatActivity {
    public static boolean status = false;
    public static boolean statusdemo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sepatu);
        View view =getWindow().getDecorView();
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE| View.SYSTEM_UI_FLAG_FULLSCREEN
        );
//        try{
////            status = Lisensi.checkLisence(this);
////            statusdemo = Lisensi.checkLisenceDemo(this);
//        }catch (Exception e){
//
//        }
//        splash();
//    }
//
//    private void splash(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                // close this activity
//
//
//
//                if(status || statusdemo) {
//                    SharedPreferences sp = getSharedPreferences("MyPrefs", 0);
//                    if (sp.getBoolean("first", true)) {
//                        sp.edit().putBoolean("first", false).apply();
//                        Intent intent = new Intent(SplashActivity.this, MenuIntro.class);
//                        startActivity(intent);
//                        finish();
//                        // Show Intro Activity
//                    } else {
//                        Intent intent = new Intent(SplashActivity.this, MenuUtama.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }else{
//                    Intent intent = new Intent(SplashActivity.this, MenuUtama.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        }, 3000); // duration
//        ImageView img = findViewById(R.id.ok);
//
//        img.animate().alpha(1).setDuration(700).setStartDelay(500).start();
//        img.animate().translationY(0).setDuration(700).setStartDelay(500).start();
    }
}
