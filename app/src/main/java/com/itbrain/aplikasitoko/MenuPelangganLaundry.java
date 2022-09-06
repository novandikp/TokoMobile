package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuPelangganLaundry extends AppCompatActivity {
    EditText NamaPelanggan,AlamatPelanggan,NomerPelanggan,Hutang;
    Button Simpan;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menupelangganlaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        NamaPelanggan = (EditText) findViewById(R.id.NamaPelanggan);
        AlamatPelanggan = (EditText) findViewById(R.id.AlamatPelanggan);
        NomerPelanggan = (EditText) findViewById(R.id.NomerPelanggan);
        Hutang = (EditText) findViewById(R.id.txtHutang);
        db = new DatabaseLaundry(this);

        if(isUpdate()){
            NamaPelanggan.setText(getIntent().getStringExtra("pelanggan"));
            AlamatPelanggan.setText(getIntent().getStringExtra("alamatpelanggan"));
            NomerPelanggan.setText(getIntent().getStringExtra("notelppelanggan"));
            Hutang.setText(getIntent().getStringExtra("hutang"));
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
        }
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdate()){

                }else{
                    add();
                }
            }
        });
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idpelanggan",-1) > -1;
    }
    public void add(){
        String pelanggan = NamaPelanggan.getText().toString();
        String alamat = AlamatPelanggan.getText().toString();
        String notelp = NomerPelanggan.getText().toString();
        String hutang = "0";
        if(TextUtils.isEmpty(pelanggan)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(alamat)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(notelp)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into tblpelanggan (pelanggan,alamat,notelp,hutang) values ('"+ pelanggan +"','"+ alamat +"','"+ notelp +"','"+ hutang +"')");
            finish();
        }
    }
}