package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FormTambahTeknisi extends AppCompatActivity {

    Database_Bengkel_ db;
    EditText namaTeknisi, alamatTeknisi, nomorTeknisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_teknisi);

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
        namaTeknisi.setText(getIntent().getStringExtra("teknisi"));
        alamatTeknisi.setText(getIntent().getStringExtra("alamatteknisi"));
        nomorTeknisi.setText(getIntent().getStringExtra("noteknisi"));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idteknisi", -1) >=0;
    }

    public void load() {
        db = new Database_Bengkel_(this);

        namaTeknisi = findViewById(R.id.edtNama);
        alamatTeknisi = findViewById(R.id.edtAlamat);
        nomorTeknisi = findViewById(R.id.edtNoTelp);

        if (isUpdate()) {
            setText();
        }
    }

    public void simpan(View view) {
        String nama = namaTeknisi.getText().toString();
        String alamat = alamatTeknisi.getText().toString();
        String nomor = nomorTeknisi.getText().toString();

        if (nama.isEmpty() || alamat.isEmpty() || nomor.isEmpty()) {
            pesan("Data Kosong");
        }else {
            String sql = "INSERT INTO tblteknisi (teknisi, alamatteknisi, noteknisi) VALUES ('"+nama+"', '"+alamat+"', "+nomor+")";
            if (isUpdate()) {
                String idteknisi = String.valueOf(getIntent().getIntExtra("idteknisi", -1));
                sql = "UPDATE tblteknisi SET teknisi = '"+nama+"', alamatteknisi = '"+alamat+"', noteknisi = '"+nomor+"' WHERE idteknisi = '"+idteknisi+"' ";
            }
            db.exc(sql);
            pesan("Simpan Data");
            finish();
        }

        namaTeknisi.setText("");
        alamatTeknisi.setText("");
        nomorTeknisi.setText("");
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}