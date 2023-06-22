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

public class ActivityTambahKredit extends AppCompatActivity {

    String type;
    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new FConfigKredit(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);

        type = getIntent().getStringExtra("type");
        switch (type) {
            case "kategori":
                setContentView(R.layout.activity_tambah_kategori_kredit);
                setTitle("Tambah Kategori");
                break;
            case "pelanggan":
                setContentView(R.layout.menu_tambah_pelanggan_pos_toko_kredit);
                setTitle("Tambah Pelanggan");
                break;
            case "barang":
                setContentView(R.layout.activity_tambah_barang_kredit);
                setTitle("Tambah barang");
                setText();
                break;
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        limiter();

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setText() {
//        arrayid.add("0");
        Spinner spinner = (Spinner) findViewById(R.id.input2);
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

    public void limiter() {
        if (type.equals("kategori")) {
            etkategori = (EditText) findViewById(R.id.tKategori);
            tvRpmkategorii = (TextView) findViewById(R.id.tvRpmkategori);

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
        } else if (type.equals("barang")) {
            etnamabarang = (EditText) findViewById(R.id.eBarang);
            ethargabeli = (EditText) findViewById(R.id.eHargaBeli);
            ethargajual = (EditText) findViewById(R.id.eHargaJual);
            etsbarang = (EditText) findViewById(R.id.eStok);

            tvRpmnamaBarang = (TextView) findViewById(R.id.tvRpmbarang);
            tvRpmBeli = (TextView) findViewById(R.id.tvRpmbeli);
            tvRpmJual = (TextView) findViewById(R.id.tvRpmjual);
            tvRpmStok = (TextView) findViewById(R.id.tvRpmsbarang);

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
        } else if (type.equals("pelanggan")) {
            etnamapelanggan = (EditText) findViewById(R.id.tNama);
            etalamatpelanggan = (EditText) findViewById(R.id.tAlamat);
            ettelppelanggan = (EditText) findViewById(R.id.tTelp);

            tvRpmpelanggan = (TextView) findViewById(R.id.tvNamaPelanggan);
            tvRpmalamat = (TextView) findViewById(R.id.tvAlamat);
            tvTelp = (TextView) findViewById(R.id.tvTelppelanggan);

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
        }
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
        String hargajual = FFunctionKredit.getText(v, R.id.eHargaJual);
        String hargabeli = FFunctionKredit.getText(v, R.id.eHargaBeli);
        String stok = FFunctionKredit.getText(v, R.id.eStok);
        String kategori = getKategori();
        String titipan = getTitipan();

        if (!TextUtils.isEmpty(kategori) && !TextUtils.isEmpty(barang) && !TextUtils.isEmpty(hargajual) && !TextUtils.isEmpty(hargabeli) && !TextUtils.isEmpty(stok)) {
            String[] p = {barang, kategori, hargabeli, hargajual, stok, titipan};
            String q = FQueryKredit.splitParam("INSERT INTO tblbarang (barang,idkategori,hargabeli,hargajual,stok,titipan) values(?,?,?,?,?,?)", p);
            if (FFunctionKredit.strToDouble(hargabeli) > FFunctionKredit.strToDouble(hargajual)) {
                getPeringatan(q);
            } else {
                if (db.exc(q)) {
                    Toast.makeText(this, "Berhasil menambah Barang", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, ActivityMasterListKredit.class);
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

    private void getPeringatan(final String q) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Harga Jual lebih kecil dari Harga Beli, tetap Lanjutkan?");
        alertDialogBuilder.setPositiveButton("Lanjutkan",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        if (db.exc(q)) {
                            Toast.makeText(ActivityTambahKredit.this, "Berhasil menambah Barang", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ActivityTambahKredit.this, ActivityMasterListKredit.class);
                            i.putExtra("type", type);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(ActivityTambahKredit.this, "Gagal menambah Barang", Toast.LENGTH_SHORT).show();
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
        try {
            Spinner spinner = (Spinner) findViewById(R.id.input2);
            return arrayid.get(spinner.getSelectedItemPosition()).toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void tambahpelanggan() {
        String nama = FFunctionKredit.getText(v, R.id.tNama);
        String alamat = FFunctionKredit.getText(v, R.id.tAlamat);
        String telp = FFunctionKredit.getText(v, R.id.tTelp);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(telp)) {
            String[] p = {nama, alamat, telp};
            String q = FQueryKredit.splitParam("INSERT INTO tblpelanggan (pelanggan,alamat,telp) values(?,?,?)", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil menambah Pelanggan", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, ActivityMasterListKredit.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal menambah pelanggan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    private void tambahkategori() {
        if (!TextUtils.isEmpty(FFunctionKredit.getText(v, R.id.tKategori))) {
            if (db.exc(FQueryKredit.splitParam("INSERT INTO tblkategori (kategori) values(?)", new String[]{FFunctionKredit.getText(v, R.id.tKategori)}))) {
                Toast.makeText(this, "Berhasil menambah Kategori", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, ActivityMasterListKredit.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal menambah Kategori", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        }

    }
}
