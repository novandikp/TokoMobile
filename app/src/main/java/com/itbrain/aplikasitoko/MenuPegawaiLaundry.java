package com.itbrain.aplikasitoko;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPegawaiLaundry extends AppCompatActivity {
    Button Simpan;
    EditText NamaPegawai,AlamatPegawai,notelpPegawai;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupegawailaundry);
        Simpan = findViewById(R.id.Simpan);
        NamaPegawai = (EditText) findViewById(R.id.NamaPegawai);
        AlamatPegawai = (EditText) findViewById(R.id.AlamatPegawai);
        notelpPegawai = (EditText) findViewById(R.id.notelpPegawai);
        db = new DatabaseLaundry(this);
        if(isUpdate()){
            NamaPegawai.setText(getIntent().getStringExtra("pelanggan"));
            AlamatPegawai.setText(getIntent().getStringExtra("alamatpelanggan"));
            notelpPegawai.setText(getIntent().getStringExtra("notelppelanggan"));
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
        String txtNamaPegawai = NamaPegawai.getText().toString();
        String txtalamatPegawai = AlamatPegawai.getText().toString();
        String txtNotelpPegawai = notelpPegawai.getText().toString();
        if(TextUtils.isEmpty(txtNamaPegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(txtalamatPegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(txtNotelpPegawai)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into tblpegawai (pegawai,alamatpegawai,notelppegawai) values ('"+ txtNamaPegawai +"','"+ txtalamatPegawai +"','"+ txtNotelpPegawai +"')");
            finish();
        }
    }
}