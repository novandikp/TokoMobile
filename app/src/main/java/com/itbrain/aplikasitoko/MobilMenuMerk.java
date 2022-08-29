package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class MobilMenuMerk extends AppCompatActivity {

    TextInputEditText EditMerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menumerkmobil);
        EditMerk=findViewById(R.id.editText3);
    }

    public void add(){
        String merk=EditMerk.getText().toString();

    }

    public void Kembali(View view) {
        Intent intent = new Intent( MobilMenuMerk.this, DaftarMerkMobil.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}