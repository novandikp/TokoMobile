package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Form_Tambah_Meja_Restoran_ extends AppCompatActivity {

    Database_Restoran db;
    EditText edtMeja;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_meja_restoran_);

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
        edtMeja.setText(getIntent().getStringExtra("meja"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idmeja", -1) >=0;
    }

    public void load() {
        db = new Database_Restoran(this);
        v = this.findViewById(android.R.id.content);

        edtMeja = findViewById(R.id.edtMeja);

        if (isUpdate()) {
            setText();
        }
    }

    public void simpan(View view) {
        String meja = edtMeja.getText().toString();

        if (meja.isEmpty()) {
            Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show();
        } else {
            String sql = "INSERT INTO tblmeja (meja) VALUES ('"+meja+"') ";
            if (isUpdate()) {
                String idmeja = String.valueOf(getIntent().getIntExtra("idmeja", 1));
                sql = "UPDATE tblmeja SET meja = '"+meja+"' WHERE idmeja = '"+idmeja+"' ";
            }
            db.exc(sql);
            Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show();
            finish();
        }
        edtMeja.setText("");
    }
}