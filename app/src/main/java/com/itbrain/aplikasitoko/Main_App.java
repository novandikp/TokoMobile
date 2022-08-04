package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main_App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
    }

    public void Pindah(View view) {
        Intent intent = new Intent(Main_App.this, MainApp.class);
        startActivity(intent);
    }
}