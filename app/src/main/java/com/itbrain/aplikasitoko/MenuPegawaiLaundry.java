package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MenuPegawaiLaundry extends AppCompatActivity {
    Button Simpan;
    EditText NamaPegawai,AlamatPegawai,NotelpPegawai;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupegawailaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        NamaPegawai = (TextInputEditText) findViewById(R.id.NamaPegawai);
        AlamatPegawai = (TextInputEditText) findViewById(R.id.AlamatPegawai);
        NotelpPegawai = (TextInputEditText) findViewById(R.id.NomorTeleponPegawai);
        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }
    public void add(){
        String pegawai = NamaPegawai.getText().toString();
        String alamatpegawai = AlamatPegawai.getText().toString();
        String notelppegawai = NotelpPegawai.getText().toString();
        if(TextUtils.isEmpty(pegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(alamatpegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(notelppegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
//        else if(TextUtils.isEmpty(nomerktp)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
        else{
            db.exc("insert into tblpegawai (pegawai,alamatpegawai,notelppegawai) values ('"+ pegawai +"','"+ alamatpegawai +"','"+ notelppegawai +"')");
            finish();
        }
    }
}