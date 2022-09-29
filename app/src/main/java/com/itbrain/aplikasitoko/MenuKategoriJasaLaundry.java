package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuKategoriJasaLaundry extends AppCompatActivity {

    Button Simpan;
    EditText edtKategori;
    DatabaseLaundry db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menukategorijasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        edtKategori = (EditText) findViewById(R.id.edtKategori);
        db = new DatabaseLaundry(this);
        if(isUpdate()){
            edtKategori.setText(getIntent().getStringExtra("kategori"));
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
        Intent intent = new Intent(MenuKategoriJasaLaundry.this, MenukategoriLaundry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idkategori",-1) > -1;
    }
    public void add(){
        String kategori = edtKategori.getText().toString();
        if(TextUtils.isEmpty(kategori)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else{
            db.exc("insert into tblkategori (kategori) values ('"+ kategori +"')");
            finish();
        }
     }
    public void update(){
        String idkategori = String.valueOf(getIntent().getIntExtra("idkategori",-1));
        String kategori = edtKategori.getText().toString();
        if(TextUtils.isEmpty(kategori)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else{
            db.exc("UPDATE tblkategori SET kategori='"+ kategori +"' where idkategori='"+ idkategori +"'");
            finish();
        }
    }
}