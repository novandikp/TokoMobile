package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MenuKategoriJasaLaundry extends AppCompatActivity {

    Button Simpan;
    EditText edtKategori;
    DatabaseLaundry db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menukategorijasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        edtKategori = (EditText) findViewById(R.id.edtKategori);

        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
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
}