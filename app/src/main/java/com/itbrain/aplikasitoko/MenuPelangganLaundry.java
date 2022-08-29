package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MenuPelangganLaundry extends AppCompatActivity {
    EditText Pelanggan,Alamat,Nomor;
    Button Simpan;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupelangganlaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        Pelanggan = (TextInputEditText) findViewById(R.id.Pelanggan);
        Alamat = (TextInputEditText) findViewById(R.id.Alamat);
        Nomor = (TextInputEditText) findViewById(R.id.Nomor);
        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }
    public void add(){
        String pelanggan = Pelanggan.getText().toString();
        String alamat= Alamat.getText().toString();
        String nomor = Nomor.getText().toString();
        if(TextUtils.isEmpty(pelanggan)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(alamat)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(nomor)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into tblpelanggan (pelanggan,alamat,notelp) values ('"+ pelanggan +"','"+ alamat +"','"+ nomor +"')");
            finish();
        }
    }
}