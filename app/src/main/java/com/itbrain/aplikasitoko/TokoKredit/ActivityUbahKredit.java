package com.itbrain.aplikasitoko.TokoKredit;

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

public class ActivityUbahKredit extends AppCompatActivity {

    String type;
    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    String id;
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kredit);


        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);

        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        switch (type) {
            case "kategori":
                setContentView(R.layout.activity_ubah_kategori_kredit);
                setTitle("Ubah Kategori");
                limiter();
                setKategori();
                break;
            case "pelanggan":
                setContentView(R.layout.menu_ubah_pelanggan_pos_toko_kredit);
                setTitle("Ubah Pelanggan");
                limiter();
                setPelanggan();
                break;
            case "barang":
                setContentView(R.layout.activity_ubah_barang_kredit);
                setTitle("Ubah barang");
                limiter();
                setText();
                setBarang();
                break;
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setKategori() {
        Cursor c = db.sq("SELECT * FROM tblkategori WHERE idkategori=" + id);
        if (c.getCount() > 0) {
            c.moveToNext();
            FFunctionKredit.setText(v, R.id.tKategori, FFunctionKredit.getString(c, "kategori"));
        }
    }

    private void setPelanggan() {
        Cursor c = db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan=" + id);
        if (c.getCount() > 0) {
            c.moveToNext();
            FFunctionKredit.setText(v, R.id.tNama, FFunctionKredit.getString(c, "pelanggan"));
            FFunctionKredit.setText(v, R.id.tAlamat, FFunctionKredit.getString(c, "alamat"));
            FFunctionKredit.setText(v, R.id.tTelp, FFunctionKredit.getString(c, "telp"));
        }
    }

    private void setBarang() {
        Cursor c = db.sq("SELECT * FROM qbarang WHERE idbarang=" + id);
        if (c.getCount() > 0) {
            c.moveToNext();
            FFunctionKredit.setText(v, R.id.eBarang, FFunctionKredit.getString(c, "barang"));
            FFunctionKredit.setText(v, R.id.eHargaBeli, FFunctionKredit.removeE(FFunctionKredit.getString(c, "hargabeli")));
            FFunctionKredit.setText(v, R.id.eHargaJual, FFunctionKredit.removeE(FFunctionKredit.getString(c, "hargajual")));
            FFunctionKredit.setText(v, R.id.eStok, FFunctionKredit.getString(c, "stok"));
            Spinner spinner = findViewById(R.id.input2);
            int spinnerPosition = ((ArrayAdapter) spinner.getAdapter()).getPosition(FFunctionKredit.getString(c, "kategori"));
            spinner.setSelection(spinnerPosition);
            Spinner spinner2 = findViewById(R.id.input6);
            spinner2.setSelection(FFunctionKredit.strToInt(FFunctionKredit.getString(c, "titipan")));
        }
    }

    public void limiter() {
        switch (type) {
            case "kategori":
                etkategori = findViewById(R.id.tKategori);
                tvRpmkategorii = findViewById(R.id.tvRpmkategori);

                etkategori.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvRpmkategorii.setText(etkategori.length() + "/30");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case "barang":
                etnamabarang = findViewById(R.id.eBarang);
                ethargabeli = findViewById(R.id.eHargaBeli);
                ethargajual = findViewById(R.id.eHargaJual);
                etsbarang = findViewById(R.id.eStok);

                tvRpmnamaBarang = findViewById(R.id.tvRpmbarang);
                tvRpmBeli = findViewById(R.id.tvRpmbeli);
                tvRpmJual = findViewById(R.id.tvRpmjual);
                tvRpmStok = findViewById(R.id.tvRpmsbarang);

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
                break;
            case "pelanggan":
                etnamapelanggan = findViewById(R.id.tNama);
                etalamatpelanggan = findViewById(R.id.tAlamat);
                ettelppelanggan = findViewById(R.id.tTelp);

                tvRpmpelanggan = findViewById(R.id.tvNamaPelanggan);
                tvRpmalamat = findViewById(R.id.tvAlamat);
                tvTelp = findViewById(R.id.tvTelppelanggan);

                etnamapelanggan.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvRpmpelanggan.setText(etnamapelanggan.length() + "/30");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                etalamatpelanggan.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvRpmalamat.setText(etalamatpelanggan.length() + "/30");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                ettelppelanggan.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvTelp.setText(ettelppelanggan.length() + "/30");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
        }
    }

    private void setText() {
//        arrayList.add("Pilih Kategori");
//        arrayid.add("0");
        Spinner spinner = findViewById(R.id.input2);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKredit.select("tblkategori"));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                arrayList.add(FFunctionKredit.getString(c, "kategori"));
                arrayid.add(FFunctionKredit.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambah(View view) {
        switch (type) {
            case "kategori":
                tambahkategori();
                break;
            case "pelanggan":
                tambahpelanggan();
                break;
            case "barang":
                tambahbarang();
                break;
        }
    }

    private void tambahbarang() {
        String barang = FFunctionKredit.getText(v, R.id.eBarang);
        String hargajual = FFunctionKredit.getText(v, R.id.eHargaJual).replace(".", "");
        String hargabeli = FFunctionKredit.getText(v, R.id.eHargaBeli).replace(".", "");
        String stok = FFunctionKredit.getText(v, R.id.eStok);
        String kategori = getKategori();
        String titipan = getTitipan();

        if (!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(hargajual) && !TextUtils.isEmpty(hargabeli) && !TextUtils.isEmpty(stok) && !kategori.equals("0")) {
            String[] p = {barang, kategori, hargabeli, hargajual, stok, titipan, id};
            String q = FQueryKredit.splitParam("UPDATE tblbarang SET barang=?,idkategori=?,hargabeli=?,hargajual=?,stok=?,titipan=? WHERE idbarang=?   ", p);
            if (FFunctionKredit.strToDouble(hargabeli) > FFunctionKredit.strToDouble(hargajual)) {
                getPeringatan(q);
            } else {
                if (db.exc(q)) {
                    Toast.makeText(this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, ActivityMasterListKredit.class);
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
                            Toast.makeText(ActivityUbahKredit.this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ActivityUbahKredit.this, ActivityMasterListKredit.class);
                            i.putExtra("type", type);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(ActivityUbahKredit.this, "Gagal mengubah Barang", Toast.LENGTH_SHORT).show();
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
        String t = FFunctionKredit.getSpinnerItem(this, R.id.input6);
        if (t.equals("Kulakan")) {
            return "0";
        } else {
            return "1";
        }
    }

    private String getKategori() {
        Spinner spinner = findViewById(R.id.input2);
        return arrayid.get(spinner.getSelectedItemPosition()).toString();
    }

    private void tambahpelanggan() {
        String nama = FFunctionKredit.getText(v, R.id.tNama);
        String alamat = FFunctionKredit.getText(v, R.id.tAlamat);
        String telp = FFunctionKredit.getText(v, R.id.tTelp);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(telp)) {
            String[] p = {nama, alamat, telp, id};
            String q = FQueryKredit.splitParam("UPDATE tblpelanggan SET pelanggan=?,alamat=?,telp=? WHERE idpelanggan=?   ", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil mengubah Pelanggan", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, ActivityMasterListKredit.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal mengubah pelanggan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    private void tambahkategori() {
        if (!TextUtils.isEmpty(FFunctionKredit.getText(v, R.id.tKategori))) {
            if (db.exc(FQueryKredit.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=?   ", new String[]{FFunctionKredit.getText(v, R.id.tKategori), id}))) {
                Toast.makeText(this, "Berhasil mengubah Kategori", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, ActivityMasterListKredit.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal mengubah Kategori", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }
}
