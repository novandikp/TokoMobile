package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Form_Tambah_Kategori_Menu_Restoran_ extends AppCompatActivity {

    Database_Restoran db;
    EditText edtKategori;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_kategori_menu_restoran_);

        load();

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setText() {
        edtKategori.setText(getIntent().getStringExtra("kategori"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idkategori", -1) >=0;
    }

    public void load() {
        db = new Database_Restoran(this);
        v = this.findViewById(android.R.id.content);

        edtKategori = findViewById(R.id.edtKategori);

        if (isUpdate()){
            setText();
        }
    }

    public void simpan(View view) {
        String kategori = edtKategori.getText().toString();

        if (kategori.isEmpty()) {
            Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show();
        } else {
            String sql = "INSERT INTO tblkategori (kategori) VALUES ('"+kategori+"') ";
            if (isUpdate()) {
                String idkategori = String.valueOf(getIntent().getIntExtra("idkategori", 1));
                sql = "UPDATE tblkategori SET kategori = '"+kategori+"' WHERE idkategori = '"+idkategori+"' ";
            }
            db.exc(sql);
            Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show();
            finish();
        }
        edtKategori.setText("");
    }
}