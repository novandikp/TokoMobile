package com.itbrain.aplikasitoko;

import android.content.Intent;
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
        getSupportActionBar().hide();
        setContentView(R.layout.menupegawailaundry);
        Simpan = findViewById(R.id.Simpan);
        NamaPegawai = findViewById(R.id.NamaPegawai);
        AlamatPegawai = findViewById(R.id.AlamatPegawai);
        notelpPegawai = findViewById(R.id.notelpPegawai);
        db = new DatabaseLaundry(this);
        if(isUpdate()){
//            set text edit pegawai
            NamaPegawai.setText(getIntent().getStringExtra("pegawai"));
            AlamatPegawai.setText(getIntent().getStringExtra("alamatpegawai"));
            notelpPegawai.setText(getIntent().getStringExtra("notelppegawai"));
//            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Update Pegawai", Toast.LENGTH_SHORT).show();
        }else{

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
        Intent intent = new Intent(MenuPegawaiLaundry.this, MenuDaftarPegawaiLaundry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idpegawai",-1) > -1;
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

    public void update(){
        String txtidpegawai = String.valueOf(getIntent().getIntExtra("idpegawai",-1));
        String txtpegawai = NamaPegawai.getText().toString();
        String txtalamatpegawai = AlamatPegawai.getText().toString();
        String txtnotelppegawai = notelpPegawai.getText().toString();
        if (TextUtils.isEmpty(txtpegawai)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else {
            db.exc("update tblpegawai set pegawai='"+ txtpegawai +"',alamatpegawai='"+ txtalamatpegawai +"',notelppegawai='"+ txtnotelppegawai +"' where idpegawai ='"+ txtidpegawai +"'");
        }
    }
}