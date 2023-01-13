package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Form_Tambah_Pelanggan_Restoran extends AppCompatActivity {

    Database_Restoran db;
    EditText edtnama, edtalamat, edtnomor;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_pelanggan_restoran_);
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
        edtnama.setText(getIntent().getStringExtra("namapelanggan"));
        edtalamat.setText(getIntent().getStringExtra("alamatpelanggan"));
        edtnomor.setText(getIntent().getStringExtra("notelpelanggan"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idpelanggan", -1) >=0;
    }

    public void load() {

        db = new Database_Restoran(this);
        v = this.findViewById(android.R.id.content);

        edtnama = findViewById(R.id.edtNama);
        edtalamat = findViewById(R.id.edtAlamat);
        edtnomor = findViewById(R.id.edtNomor);

        if (isUpdate()) {
            setText();
        }

    }

    public void simpan(View view) {
        String nama = edtnama.getText().toString();
        String alamat = edtalamat.getText().toString();
        String nomor = edtnomor.getText().toString();

        if (nama.isEmpty() || alamat.isEmpty() || nomor.isEmpty()) {
            Toast.makeText(this, "Isi Data Dengan Benar!", Toast.LENGTH_SHORT).show();
        } else {
            String sql = "INSERT INTO tblpelanggan (namapelanggan, notelppelanggan, alamatpelanggan) VALUES ('"+nama+"', '"+nomor+"', '"+alamat+"') ";
            if (isUpdate()) {
                String idpelanggan = String.valueOf(getIntent().getIntExtra("idpelanggan", 1));
                sql = "UPDATE tblpelanggan SET namapelanggan = '"+nama+"', notelppelanggan = '"+nomor+"', alamatpelanggan = '"+alamat+"' WHERE idpelanggan = '"+idpelanggan+"' ";
            }
            db.exc(sql);
            Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show();
            finish();
        }
        edtnama.setText("");
        edtalamat.setText("");
        edtnomor.setText("");
    }
}