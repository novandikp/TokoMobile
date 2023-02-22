package com.itbrain.aplikasitoko.kasir;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ActivityUbahBarangKasir extends AppCompatActivity {

    String type;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    String id;
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ubah);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);


        try{
            type = getIntent().getStringExtra("type");
        }catch (Exception e){
            type="";
            finish();
        }
        id = getIntent().getStringExtra("id");


        setContentView(R.layout.form_ubah_barang_kasir_);
        //setTitle("Ubah barang");
        // Text Counter
        //limiter();
        setText();
        setBarang();

        ImageButton imageButton = findViewById(R.id.KembaliBarang);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void limiter() {
//            case "barang":
                etnamabarang = (EditText) findViewById(R.id.eKodeBarang);
                ethargabeli = (EditText) findViewById(R.id.eHargaBeli);
                ethargajual = (EditText) findViewById(R.id.eHargaJual);
                etsbarang = (EditText) findViewById(R.id.eStok);

                //tvRpmnamaBarang = (TextView) findViewById(R.id.tvRpmbarang);
                //tvRpmBeli = (TextView) findViewById(R.id.tvRpmbeli);
                //tvRpmJual = (TextView) findViewById(R.id.tvRpmjual);
                //tvRpmStok = (TextView) findViewById(R.id.tvRpmsbarang);

                textCounterBarang();
    }

    void textCounterBarang(){
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


    private void setBarang() {
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang=" + id);
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

    private void setText() {
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


    public void simpanBarang (View view){
        tambahbarang();
    }

    public void mbohBarang(View view){
        Intent i = new Intent(this, Tambah_Barang_Kasir.class) ;
        startActivity(i);
    }

    private void tambahbarang() {
        String barang = FFunctionKasir.getText(v, R.id.eNamaBarang);
        String hargajual = FFunctionKasir.getText(v, R.id.eHargaJual).replace(".", "");
        String hargabeli = FFunctionKasir.getText(v, R.id.eHargaBeli).replace(".", "");
        String stok = FFunctionKasir.getText(v, R.id.eStok);
        String kategori = getKategori();
        String titipan = getTitipan();

        if (!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(hargajual) && !TextUtils.isEmpty(hargabeli) && !TextUtils.isEmpty(stok) && !kategori.equals("0")) {
            String[] p = {barang, kategori, hargabeli, hargajual, stok, titipan, id};
            String q = FQueryKasir.splitParam("UPDATE tblbarang SET barang=?,idkategori=?,hargabeli=?,hargajual=?,stok=?,titipan=? WHERE idbarang=?   ", p);
            if (FFunctionKasir.strToDouble(hargabeli) > FFunctionKasir.strToDouble(hargajual)) {
                getPeringatan(q);
            } else {
                //Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ActivityUbahBarangKasir.this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ActivityUbahBarangKasir.this, Tambah_Kategori_Kasir.class);
                            i.putExtra("type", type);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(ActivityUbahBarangKasir.this, "Gagal mengubah Barang", Toast.LENGTH_SHORT).show();
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
            return "0";
        } else {
            return "1";
        }
    }

    private String getKategori() {
        Spinner spinner = (Spinner) findViewById(R.id.inputBarang);
        return arrayid.get(spinner.getSelectedItemPosition()).toString();
    }

}
