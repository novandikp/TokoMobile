package com.itbrain.aplikasitoko.bengkel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class Form_Tambah_Barang_Bengkel_ extends AppCompatActivity {


    //    Model config, temp;
    Database_Bengkel_ db;
    Spinner kategori;
    EditText kodeBarang, namaBarang, stokBarang, hargaJual, hargaBeli;
    List<String> listnamakategori;
    List<String> idkategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_barang_bengkel_);

        db = new Database_Bengkel_(this);

        listnamakategori = new ArrayList<>();
        idkategori = new ArrayList<>();
//        config = new Model(getSharedPreferences("config", this.MODE_PRIVATE));
//        temp = new Model(getSharedPreferences("temp", this.MODE_PRIVATE));

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
        namaBarang.setText(getIntent().getStringExtra("barang"));
        stokBarang.setText(getIntent().getStringExtra("stok"));
        hargaJual.setText(ModulBengkel.unNumberFormat(ModulBengkel.removeE(getIntent().getStringExtra("harga"))));
        hargaBeli.setText(ModulBengkel.unNumberFormat(ModulBengkel.removeE(getIntent().getStringExtra("hargabeli"))));
        kategori.setSelection(idkategori.indexOf(getIntent().getStringExtra("idkategori").trim()));
    }

    public boolean isUpdate() {
        return getIntent().getIntExtra("idbarang", -1) >=0;
    }

    public void load() {
        db = new Database_Bengkel_(this);

        namaBarang = findViewById(R.id.etNamaBarang);
        stokBarang = findViewById(R.id.etStokBarang);
        hargaJual = findViewById(R.id.etHargaJual);
        hargaBeli = findViewById(R.id.etHargaBeli);
        kategori = findViewById(R.id.spkategoribarang);

        selectData();

        if (isUpdate()) {
            setText();
        }

    }


    public void simpan(View view) {
        String kodekategori = idkategori.get(kategori.getSelectedItemPosition());
        String namabarang = namaBarang.getText().toString();
        String stokbarang = stokBarang.getText().toString();
        String hargajual = hargaJual.getText().toString();
        String hargabeli = hargaBeli.getText().toString();

        if (namabarang.isEmpty() || stokbarang.isEmpty() || hargajual.isEmpty() || hargabeli.isEmpty()) {
           pesan("Masukkan Data Dengan Benar!");
        }else if (ModulBengkel.strToDouble(hargabeli) > ModulBengkel.strToDouble(hargajual)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin simpan harga beli lebih tinggi daripada harga jual?")
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (isUpdate()) {
                                String idbarang = String.valueOf(getIntent().getIntExtra("idbarang", -1));
                                String sql = "UPDATE tblbarang SET idkategori='"+ kodekategori +"', stok = '"+stokbarang+"', harga = '"+hargajual+"', barang = '"+namabarang+"', hargabeli = '"+hargabeli+"' WHERE idbarang = '"+idbarang+"'";
                                db.exc(sql);
                                pesan("Edit Berhasil");
                                finish();

                            } else {
                                String sql = "INSERT INTO tblbarang (idkategori, barang, stok, harga, hargabeli) VALUES ("+kodekategori+", '"+namabarang+"', "+stokbarang+", "+hargajual+", "+hargabeli+")";
                                db.exc(sql);
                                pesan("Simpan Data");
                                finish();
                            }


                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            alert = alertDialog.create();
            alert.setTitle("Simpan Data");
            alert.show();
            } else if (isUpdate()) {
            String idbarang = String.valueOf(getIntent().getIntExtra("idbarang", -1));
            String sql = "UPDATE tblbarang SET idkategori='"+ kodekategori +"', stok = '"+stokbarang+"', harga = '"+hargajual+"', barang = '"+namabarang+"', hargabeli = '"+hargabeli+"' WHERE idbarang = '"+idbarang+"'";
            db.exc(sql);
            pesan("Edit Berhasil");
            finish();

        } else {
            String sql = "INSERT INTO tblbarang (idkategori, barang, stok, harga, hargabeli) VALUES ("+kodekategori+", '"+namabarang+"', "+stokbarang+", "+hargajual+", "+hargabeli+")";
            db.exc(sql);
            pesan("Simpan Data");
            finish();
            }
        }

//        else {
//            String sql = "INSERT INTO tblbarang (idkategori, barang, stok, harga, hargabeli) VALUES ("+kodekategori+", '"+namabarang+"', "+stokbarang+", "+hargajual+", "+hargabeli+")";
//            if (isUpdate()) {
//                String idbarang = String.valueOf(getIntent().getIntExtra("idbarang", -1));
//                sql = "UPDATE tblbarang SET idkategori='"+ kodekategori +"',stok = '"+stokbarang+"', harga = '"+hargajual+"', barang = '"+namabarang+"', hargabeli = '"+hargabeli+"' WHERE idbarang = '"+idbarang+"'";
//            }
//            db.exc(sql);
//            pesan("Simpan Data");
//            finish();
//        }

//        namaBarang.setText("");
//        stokBarang.setText("");
//        hargaJual.setText("");
//        hargaBeli.setText("");



    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }


    public void selectData() {

        String sql = "SELECT * FROM tblkategori WHERE idkategori !=1";
        Cursor cursor = db.sq(sql);
        if (cursor !=null) {
            while(cursor.moveToNext()) {
                listnamakategori.add(cursor.getString(cursor.getColumnIndex("kategori")));
                idkategori.add(cursor.getString(cursor.getColumnIndex("idkategori")));
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listnamakategori);
        kategori.setAdapter(adapter);
    }


}