package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Form_Tambah_Jasa_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    EditText namaJasa, hargaJasa, pendapatanJasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_jasa_bengkel_);

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
        namaJasa.setText(getIntent().getStringExtra("barang"));
        hargaJasa.setText(ModulBengkel.unNumberFormat(ModulBengkel.removeE(getIntent().getStringExtra("harga"))));
        pendapatanJasa.setText(getIntent().getStringExtra("hargabeli"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idbarang", -1) >=0;
    }

    public void load() {
        db = new Database_Bengkel_(this);

        namaJasa = findViewById(R.id.etNamaJasa);
        hargaJasa = findViewById(R.id.etHarga);
        pendapatanJasa = findViewById(R.id.etPendapatan);

        if (isUpdate()) {
            setText();
        }
    }

    public void simpan (View view) {
        String jasa = namaJasa.getText().toString();
        String harga = hargaJasa.getText().toString();
        String pendapatan = pendapatanJasa.getText().toString();

        if (jasa.isEmpty() || harga.isEmpty() || pendapatan.isEmpty()) {
            pesan("Data Kosong");
        }else {
            String sql = "INSERT INTO tblbarang (idkategori, barang, stok, harga, hargabeli) VALUES (1, '"+jasa+"', 0, "+harga+", "+pendapatan+")";
            if (isUpdate()) {
                String idjasa = String.valueOf(getIntent().getIntExtra("idbarang", -1));
                sql = "UPDATE tblbarang SET barang = '"+jasa+"', harga = '"+harga+"', hargabeli = '"+pendapatan+"' WHERE idbarang = '"+idjasa+"'";
            }
            db.exc(sql);
            pesan("Simpan Data");
            finish();
        }

        namaJasa.setText("");
        hargaJasa.setText("");
        pendapatanJasa.setText("");

    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}