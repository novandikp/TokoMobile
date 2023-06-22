package com.itbrain.aplikasitoko.bengkel;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class Ubah_Kategori_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    EditText etKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_kategori_bengkel_);

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
        etKategori.setText(getIntent().getStringExtra("kategori"));
    }

    public boolean isUpdate() {
      return  getIntent().getIntExtra("idkategori", -1) >=0;
    }

    public void load() {
        db = new Database_Bengkel_(this);


        etKategori = findViewById(R.id.tvKategori);
        if (isUpdate()) {
            setText();
        }
    }

    public void Simpan(View view) {
        String kategori = etKategori.getText().toString();

        if (kategori.isEmpty()) {
            pesan("Data Kosong");
        }else {
            String sql = "INSERT INTO tblkategori (kategori) VALUES ('"+kategori+"')";
            if(isUpdate()){
                String idkategori = String.valueOf(getIntent().getIntExtra("idkategori",-1));
                sql = "UPDATE tblkategori SET kategori = '"+kategori+"' WHERE idkategori = '"+idkategori+"'";
            }

           db.exc(sql);
           pesan("Simpan Berhasil");
           finish();
        }

        etKategori.setText("");
    }


    public void pesan (String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}
