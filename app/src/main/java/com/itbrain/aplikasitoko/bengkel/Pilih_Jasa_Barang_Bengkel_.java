package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.Model.Barang;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Pilih_Jasa_Barang_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Pilih_Jasa_Barang_Bengkel_Adapter adapter;
    Spinner kategori;

    ArrayList<Barang> listBarang;
    List<String> listKategori;
    List<String> idKategori;
    List<String> listkategori;
    List<String> idkategori;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_jasa_barang_bengkel_);

        listKategori = new ArrayList<>();
        idKategori = new ArrayList<>();
        listkategori = new ArrayList<>();
        idkategori = new ArrayList<>();

        load();
        selectSpinner();
        cari = (EditText) findViewById(R.id.etCari);
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void load() {
        db = new Database_Bengkel_(this);
        listBarang = new ArrayList<>();
        adapter = new Pilih_Jasa_Barang_Bengkel_Adapter(listBarang, this);

        recyclerView = findViewById(R.id.rcvJasaBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        kategori = findViewById(R.id.spCari);
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    void selectData() {
        String sql;
        int i = kategori.getSelectedItemPosition();
        if( i == 0 ){
            sql ="SELECT * FROM tblbarang WHERE idkategori AND barang LIKE '%"+cari.getText().toString()+"%' ";
        }else if( i == 1 ){
            sql = "SELECT * FROM tblbarang WHERE idkategori =1 AND barang LIKE '%"+cari.getText().toString()+"%' ";
        }else{
            sql = "SELECT * FROM tblbarang WHERE idkategori !=1 AND barang LIKE '%"+cari.getText().toString()+"%' ";
        }


        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listBarang.clear();
            while (cursor.moveToNext()) {
                listBarang.add(new Barang(
                        cursor.getInt(cursor.getColumnIndex("idbarang")),
                        cursor.getString(cursor.getColumnIndex("barang")),
                        cursor.getString(cursor.getColumnIndex("hargabeli")),
                        cursor.getString(cursor.getColumnIndex("stok")),
                        cursor.getString(cursor.getColumnIndex("harga")),
                        cursor.getString(cursor.getColumnIndex("idkategori"))
                ));
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void selectedJasaBarang (Barang barang) {
        Intent in = new Intent();
        in.putExtra("idbarang", barang.getIdbarang());
        in.putExtra("barang", barang.getBarang());
        in.putExtra("harga", barang.getHarga());
        setResult(RESULT_OK, in);
        finish();
    }

    public void selectSpinner() {
        listkategori.clear();
        listKategori.add("Semua");
        listKategori.add("Jasa");
        listKategori.add("Orderdil");
        kategori.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,listKategori));
    }
}

class Pilih_Jasa_Barang_Bengkel_Adapter extends RecyclerView.Adapter<Pilih_Jasa_Barang_Bengkel_Adapter.ViewHolder>{

    private ArrayList<Barang> barang;

    public Pilih_Jasa_Barang_Bengkel_Adapter(ArrayList<Barang> barang, Context context) {
        this.barang = barang;
        this.context = context;
    }

    private Context context;

    @NonNull
    @Override
    public Pilih_Jasa_Barang_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilih_jasa_barang_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Pilih_Jasa_Barang_Bengkel_Adapter.ViewHolder holder, int position) {
        Barang item = barang.get(position);

        holder.tvHargaBeli.setText(ModulBengkel.removeE(item.getHargabeli()));
        holder.tvStok.setText(item.getStok());
        holder.tvHarga.setText(ModulBengkel.removeE(item.getHarga()));
        holder.tvBarang.setText(item.getBarang());

        holder.pilihJasaBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Pilih_Jasa_Barang_Bengkel_)context).selectedJasaBarang(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHargaBeli, tvStok, tvHarga, tvBarang;
        ConstraintLayout pilihJasaBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHargaBeli = itemView.findViewById(R.id.tvHargaBeli);
            tvStok = itemView.findViewById(R.id.tvStok);
            tvHarga = itemView.findViewById(R.id.tvHargaJual);
            tvBarang = itemView.findViewById(R.id.tvNamaBarang);
            pilihJasaBarang = itemView.findViewById(R.id.pilihJasaBarang);
        }
    }
}