package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MenuIdentitasLaundry extends AppCompatActivity {
    Button Simpan;
    EditText Nama,Alamat,NomorTelepon,Caption1,Caption2,Caption3;
    DatabaseLaundry db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitaslaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        Nama = (TextInputEditText) findViewById(R.id.Nama);
        Alamat = (TextInputEditText) findViewById(R.id.Alamat);
        NomorTelepon = (TextInputEditText) findViewById(R.id.NomorTelepon);
        Caption1 = (TextInputEditText) findViewById(R.id.Caption1);
        Caption2 = (TextInputEditText) findViewById(R.id.Caption2);
        Caption3 = (TextInputEditText) findViewById(R.id.Caption3);
        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }
    public void add(){
        String NamaToko = Nama.getText().toString();
        String AlamatToko = Alamat.getText().toString();
        String Nomor = NomorTelepon.getText().toString();
        String CaptionSatu = Caption1.getText().toString();
        String CaptionDua = Caption2.getText().toString();
        String CaptionTiga = Caption3.getText().toString();
        if(TextUtils.isEmpty(NamaToko)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(AlamatToko)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Nomor)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(CaptionSatu)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(CaptionDua)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(CaptionTiga)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into tblidentitas (namatoko,alamattoko,notelptoko,caption_1,caption_2,caption_3) values ('"+ NamaToko +"','"+ AlamatToko +"','"+ Nomor +"','"+ CaptionSatu +"','"+ CaptionDua +"','"+ CaptionTiga +"')");
            finish();
        }
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuIdentitasLaundry.this, LaundryMenuMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}