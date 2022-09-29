package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPelangganLaundry extends AppCompatActivity {
    EditText NamaPelanggan,AlamatPelanggan,NomerPelanggan,Hutang;
    Button Simpan;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menupelangganlaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        NamaPelanggan = (EditText) findViewById(R.id.NamaPelanggan);
        AlamatPelanggan = (EditText) findViewById(R.id.AlamatPelanggan);
        NomerPelanggan = (EditText) findViewById(R.id.NomerPelanggan);
        Hutang = (EditText) findViewById(R.id.txtHutang);
        db = new DatabaseLaundry(this);
//        getPelanggan();

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
                    update();
                }else{
                    add();
                }
            }
        });
    }

    public void Kembali(View view) {
        Intent intent = new Intent(MenuPelangganLaundry.this, MenuDaftarPelangganLaundry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idpelanggan",-1) > -1;
    }
    public void add() {
        String pelanggan = NamaPelanggan.getText().toString();
        String alamat = AlamatPelanggan.getText().toString();
        String notelp = NomerPelanggan.getText().toString();
        String hutang = "0";
        if (TextUtils.isEmpty(pelanggan)) {
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(alamat)) {
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(notelp)) {
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        } else {
            db.exc("insert into tblpelanggan (pelanggan,alamat,notelp,hutang) values ('" + pelanggan + "','" + alamat + "','" + notelp + "','" + hutang + "')");
            finish();
        }
    }

        public void update(){
            String idpelanggan = String.valueOf(getIntent().getIntExtra("idpelanggan",-1));
            String pelanggan = NamaPelanggan.getText().toString();
            String alamat = AlamatPelanggan.getText().toString();
            String notelp = NomerPelanggan.getText().toString();
            if(TextUtils.isEmpty(pelanggan)){
                Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
            }else{
                db.exc("UPDATE tblidentitas SET pelanggan='"+ pelanggan +"',alamat='"+ alamat +"',notelp='"+ notelp +"' where idpelanggan='"+ idpelanggan +"'");
                finish();
            }
        }

//        public void update(){
//            String idpelanggan = String.valueOf(getIntent().getIntExtra("idpelanggan",-1));
//            String pelangganupdet = NamaPelanggan.getText().toString();
//            String alamatupdet = AlamatPelanggan.getText().toString();
//            String notelpupdet = NomerPelanggan.getText().toString();
////            String hutangupdet = "0";
//            if(TextUtils.isEmpty(pelangganupdet)){
//                Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//            }
//            else if(TextUtils.isEmpty(alamatupdet)){
//                Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//            }
//            else if(TextUtils.isEmpty(notelpupdet)){
//                Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                db.exc("UPDATE tblpelanggan SET pelanggan='"+ pelanggan +"' where idpelanggan ='"+ idpelanggan +"'");
//                finish();
//            }
//    }
    }