package com.itbrain.aplikasitoko.CetakKwitansi;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class TambahPelanggan_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    TextInputEditText edtPelanggan, edtAlamat, edtTelp;
    String pelanggan, alamat, telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambahpelanggan_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);
        edtPelanggan = (TextInputEditText) findViewById(R.id.edtNama);
        edtAlamat = (TextInputEditText) findViewById(R.id.edtAlamat);
        edtTelp = (TextInputEditText) findViewById(R.id.edtTelp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnSimpan(View view) {
        pelanggan = edtPelanggan.getText().toString();
        alamat = edtAlamat.getText().toString();
        telp = edtTelp.getText().toString();
        if (pelanggan.equals("") || alamat.equals("") || telp.equals("")){
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            if (db.insertPelanggan(pelanggan,alamat,telp)){
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
