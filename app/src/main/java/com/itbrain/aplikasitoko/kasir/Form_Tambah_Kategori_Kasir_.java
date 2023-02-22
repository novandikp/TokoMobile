package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Kategori_Kasir_ extends AppCompatActivity {

    String type;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kategori_kasir);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);

        setText();


        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.tambah_kategori_kasir);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_Tambah_Kategori_Kasir_.this, Tambah_Kategori_Kasir.class);
                startActivity(intent);
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.sPencarianKategori);
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
                getKategori(a);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getKategori("");
    }

    private void setText() {
        arrayKategori.add("Semua");
       // Spinner spinner = (Spinner) findViewById(R.id.sKategori);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori"));
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                arrayList.add(FFunctionKasir.getString(c, "kategori"));
                arrayid.add(FFunctionKasir.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void getKategori(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvKat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterKategori(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c;
        if (TextUtils.isEmpty(cari)) {
            c = db.sq(FQueryKasir.select("tblkategori") + FQueryKasir.sOrderASC("kategori"));
        } else {
            c = db.sq(FQueryKasir.selectwhere("tblkategori") + FQueryKasir.sLike("kategori", cari) + FQueryKasir.sOrderASC("kategori"));
        }
        if (FFunctionKasir.getCount(c) > 0) {
            FFunctionKasir.setText(v, R.id.tJumlahKat, "Jumlah Data : " + FFunctionKasir.intToStr(FFunctionKasir.getCount(c)));
            while (c.moveToNext()) {
                String kategori = FFunctionKasir.getString(c, "kategori");
                String idkategori = FFunctionKasir.getString(c, "idkategori");
                arrayList.add(idkategori + "__" + kategori);
            }
        } else {
            FFunctionKasir.setText(v, R.id.tJumlahKat, "Jumlah Data : 0");
        }
        Toast.makeText(this, "Jumlah data ada " + arrayList.size(), Toast.LENGTH_SHORT);
        adapter.notifyDataSetChanged();
    }


    public void kate(View view) {
        Intent i = new Intent(this, Tambah_Kategori_Kasir.class);
        startActivity(i);
    }

    public void wHapus(final View view) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String q = "";
                        String id = view.getTag().toString();
                        if (cekBarang(id)) {
                            q = "DELETE FROM tblkategori WHERE idkategori=" + id;
                            db.exc(q);
                            getKategori("");
                        } else {
                            Toast.makeText(Form_Tambah_Kategori_Kasir_.this, "Masih terdapat Barang", Toast.LENGTH_SHORT).show();
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

    public void wUbah(View view) {
        Intent i = new Intent(this, ActivityUbahKategoriKasir.class);
        String id = view.getTag().toString();
        i.putExtra("id",id);
        startActivity(i);
    }


    private boolean cekBarang(String id) {
        Cursor c = db.sq(FQueryKasir.selectwhere("tblbarang") + FQueryKasir.sWhere("idkategori = ", id));
        if (FFunctionKasir.getCount(c) > 0) {
            return false;
        } else if (FFunctionKasir.getCount(c) == 0) {
            return true;
        } else {
            return false;
        }
    }

    class AdapterKategori extends RecyclerView.Adapter<AdapterKategori.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterKategori(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterKategori.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_kasir_master_list_kategori, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView kategori;
            ConstraintLayout ubah, hapus;

            public ViewHolder(View view) {
                super(view);

                kategori = (TextView) view.findViewById(R.id.tNama);
                ubah = (ConstraintLayout) view.findViewById(R.id.wHapusBarang);
                hapus = (ConstraintLayout) view.findViewById(R.id.wUbahBarang);
            }
        }

        @Override
        public void onBindViewHolder(AdapterKategori.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.kategori.setText(row[1]);
            viewHolder.ubah.setTag(row[0]);
            viewHolder.hapus.setTag(row[0]);
        }
    }
}



