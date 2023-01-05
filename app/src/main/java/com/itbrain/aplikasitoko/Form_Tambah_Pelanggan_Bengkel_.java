package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Form_Tambah_Pelanggan_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    EditText etNama, etAlamat, etNomor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_pelanggan_bengkel_);

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
        etNama.setText(getIntent().getStringExtra("pelanggan"));
        etAlamat.setText(getIntent().getStringExtra("alamat"));
        etNomor.setText(getIntent().getStringExtra("notelp"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idpelanggan", -1) >=0;
    }

    public void load() {
        db = new Database_Bengkel_(this);

        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etNomor = findViewById(R.id.etNomor);

        if (isUpdate()) {
            setText();
        }
    }

    public void simpan(View view) {
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String nomor = etNomor.getText().toString();

        if (nama.isEmpty() || alamat.isEmpty() || nomor.isEmpty()) {
            pesan("Data Kosong");
        }else {
            String sql = "INSERT INTO tblpelanggan (pelanggan, alamat, notelp) VALUES ('"+nama+"', '"+alamat+"', "+nomor+")";
            if (isUpdate()) {
                String idpelanggan = String.valueOf(getIntent().getIntExtra("idpelanggan", -1));
                sql = "UPDATE tblpelanggan SET pelanggan = '"+nama+"', alamat = '"+alamat+"', notelp = '"+nomor+"' WHERE idpelanggan = '"+idpelanggan+"' ";
            }
            db.exc(sql);
            pesan("Simpan Data");
            finish();
        }

        etNama.setText("");
        etAlamat.setText("");
        etNomor.setText("");

    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}