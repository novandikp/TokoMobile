package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Form_Tambah_Menu_Restoran_ extends AppCompatActivity {

    Database_Restoran db;
    EditText edtMakanan, edtHarga;
    Spinner spKategori;
    List<String> listNamaKategori;
    List<String> listIdkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_menu_restoran_);

        db = new Database_Restoran(this);

        listNamaKategori = new ArrayList<>();
        listIdkategori = new ArrayList<>();

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
        edtMakanan.setText(getIntent().getStringExtra("makanan"));
        edtHarga.setText(getIntent().getStringExtra("harga"));
        spKategori.setSelection(listIdkategori.indexOf(getIntent().getStringExtra("idkategori").trim()));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idbarang", -1) >=0;
    }

    public void load() {
        edtMakanan = findViewById(R.id.edtMakanan);
        edtHarga = findViewById(R.id.edtHarga);
        spKategori = findViewById(R.id.spKategori);

        selectData();

        if (isUpdate()) {
            setText();
        }
    }

    public void simpan(View view) {
        String kodekategori = listIdkategori.get(spKategori.getSelectedItemPosition());
        String makanan = edtMakanan.getText().toString();
        String harga = edtHarga.getText().toString();

        if (makanan.isEmpty() || harga.isEmpty()) {
            Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show();
        } else {
//            String sql = "INSERT INTO tblmakanan (idkategori, makanan, harga) VALUES ('"+kodekategori+"', '"+makanan+"', '"+harga+"') ";
//            if (isUpdate()){
//                String idmakanan = String.valueOf(getIntent().getIntExtra("idmakanan", 1));
//                sql = "UPDATE tblmakanan SET idkategori = '"+kodekategori+"', makanan = '"+makanan+"', harga = '"+harga+"' WHERE idmakanan = '"+idmakanan+"' ";
//            }
//            db.exc(sql);
//            Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show();
//            finish();

            if (isUpdate()) {
                String idmakanan = String.valueOf(getIntent().getIntExtra("idmakanan", -1));
                String sql = "UPDATE tblmakanan SET idkategori = '"+kodekategori+"', makanan = '"+makanan+"', harga = '"+harga+"' WHERE idmakanan = '"+idmakanan+"' ";
                db.exc(sql);
                Toast.makeText(this, "Edit Data", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String sql = "INSERT INTO tblmakanan (idkategori, makanan, harga) VALUES ('"+kodekategori+"', '"+makanan+"', '"+harga+"') ";
                db.exc(sql);
                Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        edtMakanan.setText("");
        edtHarga.setText("");

    }

    public void selectData() {
        String sql = "SELECT * FROM tblkategori WHERE idkategori";
        Cursor c = db.sq(sql);
        if (c != null) {
            while(c.moveToNext()) {
                listNamaKategori.add(c.getString(c.getColumnIndex("kategori")));
                listIdkategori.add(c.getString(c.getColumnIndex("idkategori")));
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listNamaKategori);
        spKategori.setAdapter(adapter);

    }
}