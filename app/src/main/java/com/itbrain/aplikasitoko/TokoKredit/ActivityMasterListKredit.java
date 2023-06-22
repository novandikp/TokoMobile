package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ActivityMasterListKredit extends AppCompatActivity {

    String type;
    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);
        type = getIntent().getStringExtra("type");


        switch (type) {
            case "kategori":
                setContentView(R.layout.menu_kategori_pos_toko_kredit);
                getKategori("");
                setTitle("Kategori");
                break;
            case "barang":
                setContentView(R.layout.activity_master_barang_kredit);
                setText();
                getBarang("");
                setTitle("Barang");

                Spinner spinner = findViewById(R.id.sKategori);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getBarang("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "pelanggan":
                setContentView(R.layout.menu_pelanggan_pos_toko_kredit);
                getPelanggan("");
                setTitle("Pelanggan");
                break;
        }

        ImageView imageView = findViewById(R.id.imageView59);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText eCari = findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();
                if (type.equals("kategori")) {
                    getKategori(a);
                } else if (type.equals("barang")) {
                    getBarang(a);
                } else if (type.equals("pelanggan")) {
                    getPelanggan(a);
                }
            }
        });
    }

    private void setText() {
        arrayKategori.add("Semua");
        arrayid.add("0");
        Spinner spinner = findViewById(R.id.sKategori);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKredit.select("tblkategori"));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                arrayKategori.add(FFunctionKredit.getString(c, "kategori"));
                arrayid.add(FFunctionKredit.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public String whereKat(String cari) {
        Spinner spinner = findViewById(R.id.sKategori);
        String id = arrayid.get(spinner.getSelectedItemPosition()).toString();
        if (id.equals("0")) {
            return FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sLike("barang", cari) + " ORDER BY barang ASC";
        } else {
            return FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idkategori", id) + " AND " + FQueryKredit.sLike("barang", cari) + " ORDER BY barang ASC";
        }
    }

    public void tambah(View view) {
        Intent i = new Intent(this, ActivityTambahKredit.class);
        i.putExtra("type", type);
        startActivity(i);
    }

    public void editmaster(View view) {
        Intent i = new Intent(this, ActivityUbahKredit.class);
        i.putExtra("type", type);
        i.putExtra("id", view.getTag().toString());
        startActivity(i);
    }

    public void getKategori(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterKategoriKredit(this, arrayList);
        recyclerView.setAdapter(adapter);

        Cursor c;
        if (TextUtils.isEmpty(cari)) {
            c = db.sq(FQueryKredit.select("tblkategori") + FQueryKredit.sOrderASC("kategori"));
        } else {
            c = db.sq(FQueryKredit.selectwhere("tblkategori") + FQueryKredit.sLike("kategori", cari) + FQueryKredit.sOrderASC("kategori"));
        }
        if (c.getCount() > 0) {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String kategori = FFunctionKredit.getString(c, "kategori");
                String idkategori = FFunctionKredit.getString(c, "idkategori");
                arrayList.add(idkategori + "__" + kategori);
            }
        } else {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    private void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPelangganKredit(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.select("tblpelanggan") + " EXCEPT " + FQueryKredit.selectwhere("tblpelanggan") + FQueryKredit.sWhere("idpelanggan", "1") + FQueryKredit.sOrderASC("pelanggan");
        } else {
            q = FQueryKredit.selectwhere("tblpelanggan") + FQueryKredit.sLike("pelanggan", cari) + " EXCEPT " + FQueryKredit.selectwhere("tblpelanggan") + FQueryKredit.sWhere("idpelanggan", "1") + FQueryKredit.sOrderASC("pelanggan");
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String campur = FFunctionKredit.getString(c, "idpelanggan") + "__" + FFunctionKredit.getString(c, "pelanggan") + "__" + FFunctionKredit.getString(c, "alamat") + "__" + FFunctionKredit.getString(c, "telp");
                arrayList.add(campur);
            }
        } else {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    private void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterBarangKredit(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = whereKat(cari);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String campur = FFunctionKredit.getString(c, "idbarang") + "__" + FFunctionKredit.getString(c, "barang") + "__" + FFunctionKredit.getString(c, "stok") + "__" + FFunctionKredit.removeE(FFunctionKredit.getString(c, "hargajual"));
                arrayList.add(campur);
            }
        } else {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void kategori(View v) {
        String idkat = v.getTag().toString();
        Toast.makeText(this, idkat, Toast.LENGTH_SHORT).show();
    }

    //dialog
    public void hapus(final View view) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String q = "";
                        String id = view.getTag().toString();
                        switch (type) {
                            case "kategori":
                                if (bisaHapusKategori(id)) {
                                    q = "DELETE FROM tblkategori WHERE idkategori=" + id;
                                    db.exc(q);
                                    getKategori("");
                                } else {
                                    Toast.makeText(ActivityMasterListKredit.this, "Masih terdapat Barang", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "barang":
                                if (bisaHapusBarang(id)) {
                                    q = "DELETE FROM tblbarang WHERE idbarang=" + id;
                                    db.exc(q);
                                    getBarang("");
                                } else {
                                    Toast.makeText(ActivityMasterListKredit.this, "Barang masih digunakan untuk data transaksi", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "pelanggan":
                                if (bisaHapusPelanggan(id)) {
                                    q = "DELETE FROM tblpelanggan WHERE idpelanggan=" + id;
                                    db.exc(q);
                                    getPelanggan("");
                                } else {
                                    Toast.makeText(ActivityMasterListKredit.this, "Pelanggan masih digunakan untuk data transaksi", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean bisaHapusKategori(String id) {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idkategori", id));
        return c.getCount() == 0;
    }

    private boolean bisaHapusBarang(String id) {
        Cursor penjualan = db.sq("SELECT * FROM tblpenjualan WHERE idbarang = " + id + "");
        Cursor penjualanKredit = db.sq("SELECT * FROM tblpenjualan_kredit WHERE idbarang = " + id + "");
        Cursor retur = db.sq("SELECT * FROM tblreturn WHERE idbarang = " + id + "");
        return penjualan.getCount() == 0 && penjualanKredit.getCount() == 0 && retur.getCount() == 0;
    }

    private boolean bisaHapusPelanggan(String id) {
        Cursor bayar = db.sq("SELECT * FROM tblbayar WHERE idpelanggan = " + id + "");
        Cursor bayarKredit = db.sq("SELECT * FROM tblbayar_kredit WHERE idpelanggan = " + id + "");
        Cursor kredit = db.sq("SELECT * FROM tblkredit WHERE idpelanggan = " + id + "");
        return bayar.getCount() == 0 && bayarKredit.getCount() == 0 && kredit.getCount() == 0;
    }
}

class AdapterKategoriKredit extends RecyclerView.Adapter<AdapterKategoriKredit.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    AdapterKategoriKredit(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_kredit_master_list_kategori, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kumfun;
        ConstraintLayout ubah, hapus;

        public ViewHolder(View view) {
            super(view);

            kumfun = view.findViewById(R.id.aww);
            ubah = view.findViewById(R.id.asu);
            hapus = view.findViewById(R.id.wcok);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.kumfun.setText(row[1]);
        viewHolder.ubah.setTag(row[0]);
        viewHolder.hapus.setTag(row[0]);
    }
}

class MasterBarangKredit extends RecyclerView.Adapter<MasterBarangKredit.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterBarangKredit(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_master_list_barang_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang, stok, harga;
        ConstraintLayout ubah, hapus;

        public ViewHolder(View view) {
            super(view);

            barang = view.findViewById(R.id.barang);
            harga = view.findViewById(R.id.tHarga);
            stok = view.findViewById(R.id.stok);
            ubah = view.findViewById(R.id.wHapus);
            hapus = view.findViewById(R.id.wUbah);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        viewHolder.stok.setText("Sisa Stok : " + row[2]);
        viewHolder.harga.setText("Harga : Rp. " + row[3]);
        viewHolder.ubah.setTag(row[0]);
        viewHolder.hapus.setTag(row[0]);
    }
}

class MasterPelangganKredit extends RecyclerView.Adapter<MasterPelangganKredit.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPelangganKredit(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_master_list_pelanggan_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nama, alamat, telp;
        ConstraintLayout ubah, hapus;

        public ViewHolder(View view) {
            super(view);

            telp = view.findViewById(R.id.telp);
            alamat = view.findViewById(R.id.alamat);
            nama = view.findViewById(R.id.nama);
            ubah = view.findViewById(R.id.wHapus);
            hapus = view.findViewById(R.id.wUbah);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.nama.setText(row[1]);
        viewHolder.alamat.setText(row[2]);
        viewHolder.telp.setText(row[3]);
        viewHolder.ubah.setTag(row[0]);
        viewHolder.hapus.setTag(row[0]);
    }
}