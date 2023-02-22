package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Barang_Kasir_ extends AppCompatActivity {

    String type;
    String Id;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_barang_kasir_);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);



        ImageButton imageButton = findViewById(R.id.KembaliBarang);
        Button button = findViewById(R.id.mbohSimpan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setText();

        //try {
       //     type = getIntent().getStringExtra("type");
       // } catch (Exception e) {
        //    type = "";
         //   finish();
      //  }

//        switch (type) {
//            case "barang":
//                setContentView(R.layout.form_tambah_barang_kasir_);
//                setTitle("Tambah barang");
//
//                break;
//        }

        //limiter();
        setBarang();
    }


    private void setText() {
        arrayKategori.add("Semua");
        Spinner spinner = (Spinner) findViewById(R.id.inputBarang);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori"));
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                arrayList.add(FFunctionKasir.getString(c, "kategori"));
                arrayid.add(FFunctionKasir.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void limiter() {
        switch (type) {
            case "barang":
                etnamabarang = (EditText) findViewById(R.id.tvnamaBarang);
                ethargabeli = (EditText) findViewById(R.id.eHargaBeli);
                ethargajual = (EditText) findViewById(R.id.eHargaJual);
                etsbarang = (EditText) findViewById(R.id.eStok);

                tvRpmnamaBarang = (TextView) findViewById(R.id.tvnamaBarang);
                tvRpmBeli = (TextView) findViewById(R.id.tvHargaBeli);
                tvRpmJual = (TextView) findViewById(R.id.tvHargaJual);
                tvRpmStok = (TextView) findViewById(R.id.tvStok);

                textCounterBarang();
                break;

        }
    }

    void textCounterBarang() {
        etnamabarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmnamaBarang.setText(etnamabarang.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ethargabeli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmBeli.setText(ethargabeli.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ethargajual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmJual.setText(ethargajual.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etsbarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmStok.setText(etsbarang.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void simpanBarang(View view) {
        tambahbarang();
        editbarang();
    }

    private void tambahbarang() {
        String barang = FFunctionKasir.getText(v, R.id.eNamaBarang);
        String hargajual = FFunctionKasir.getText(v, R.id.eHargaJual);
        String hargabeli = FFunctionKasir.getText(v, R.id.eHargaBeli);
        String stok = FFunctionKasir.getText(v, R.id.eStok);
        String kategori = getKategori();
        String titipan = getTitipan();
        if (!TextUtils.isEmpty(kategori) && !TextUtils.isEmpty(barang) && !TextUtils.isEmpty(hargajual) && !TextUtils.isEmpty(hargabeli) && !TextUtils.isEmpty(stok)) {
                String[] p = {barang, kategori, hargabeli, hargajual, stok, titipan};
        String q = FQueryKasir.splitParam("INSERT INTO tblbarang (barang,idkategori,hargabeli,hargajual,stok,titipan) values(?,?,?,?,?,?)", p);

            if (FFunctionKasir.strToDouble(hargabeli) > FFunctionKasir.strToDouble(hargajual)) {
                getPeringatan(q);
            } else {
                Log.d("SQL Insert", "tambahbarang: "+q);
                if (db.exc(q)) {
                    Toast.makeText(this, "Berhasil menambah Barang", Toast.LENGTH_SHORT).show();
//
                    Intent i = new Intent(this, Tambah_Barang_Kasir.class);
                    i.putExtra("type", type);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Gagal Menambah Barang, Mohon periksa kembali", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (TextUtils.isEmpty(barang)) {
                Toast.makeText(this, "Mohon isi kategori terlebih dahulu.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void editbarang() {
        String barang = FFunctionKasir.getText(v, R.id.eNamaBarang);
        String hargajual = FFunctionKasir.getText(v, R.id.eHargaJual).replace(".", "");
        String hargabeli = FFunctionKasir.getText(v, R.id.eHargaBeli).replace(".", "");
        String stok = FFunctionKasir.getText(v, R.id.eStok);
        String kategori = getKategori();
        String titipan = getTitipan();

        if (!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(hargajual) && !TextUtils.isEmpty(hargabeli) && !TextUtils.isEmpty(stok) && !kategori.equals("0")) {
            String[] p = {barang, kategori, hargabeli, hargajual, stok, titipan, Id};
            String q = FQueryKasir.splitParam("UPDATE tblbarang SET barang=?,idkategori=?,hargabeli=?,hargajual=?,stok=?,titipan=? WHERE idbarang=?   ", p);
            if (FFunctionKasir.strToDouble(hargabeli) > FFunctionKasir.strToDouble(hargajual)) {
                getPeringatan(q);
            } else {
                if (db.exc(q)) {
                    Toast.makeText(this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, Tambah_Barang_Kasir.class);
                    i.putExtra("type", type);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Gagal mengubah Barang", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPeringatan(final String q) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Harga Jual lebih kecil dari Harga Beli, tetap Lanjutkan?");
        alertDialogBuilder.setPositiveButton("Lanjutkan",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        if (db.exc(q)) {
                            Toast.makeText(Form_Tambah_Barang_Kasir_.this, "Berhasil menambah Barang", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Form_Tambah_Barang_Kasir_.this, Form_Tambah_Kategori_Kasir_.class);
                            i.putExtra("type", type);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(Form_Tambah_Barang_Kasir_.this, "Gagal menambah Barang", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private String getTitipan() {
        String t = FFunctionKasir.getSpinnerItem(v, R.id.input23);
        if (t.equals("Kulakan")) {
            return "1";
        } else {
            return "0";
        }
    }

    private String getKategori() {
        try {
            Spinner spinner = (Spinner) findViewById(R.id.inputBarang);
            return arrayid.get(spinner.getSelectedItemPosition()).toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void setBarang() {
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang=" + Id);
        if (FFunctionKasir.getCount(c) > 0) {
            c.moveToNext();
            FFunctionKasir.setText(v, R.id.eNamaBarang, FFunctionKasir.getString(c, "barang"));
            FFunctionKasir.setText(v, R.id.eHargaBeli, FFunctionKasir.removeE(FFunctionKasir.getString(c, "hargabeli")).replace(".", ""));
            FFunctionKasir.setText(v, R.id.eHargaJual, FFunctionKasir.removeE(FFunctionKasir.getString(c, "hargajual")).replace(".", ""));
            FFunctionKasir.setText(v, R.id.eStok, FFunctionKasir.getString(c, "stok"));
            Spinner spinner = (Spinner) findViewById(R.id.inputBarang);
            spinner.setSelection(FFunctionKasir.strToInt(FFunctionKasir.getString(c, "idkategori")));
            Spinner spinner2 = (Spinner) findViewById(R.id.input23);
            spinner2.setSelection(FFunctionKasir.strToInt(FFunctionKasir.getString(c, "titipan")));
        }
    }

        //ImageButton imageButton = findViewById(R.id.Kembali);

        //imageButton.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  finish();
            //}
        //});
    //}

}