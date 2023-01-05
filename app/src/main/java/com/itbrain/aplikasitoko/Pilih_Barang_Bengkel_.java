package com.itbrain.aplikasitoko;

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
import android.widget.EditText;
import android.widget.TextView;

import com.itbrain.aplikasitoko.Model.Barang;

import java.util.ArrayList;
import java.util.Currency;

public class Pilih_Barang_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Pilih_Barang_Adapter adapter;

    ArrayList<Barang> listBarang;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_barang_bengkel_);

        load();
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
        adapter = new Pilih_Barang_Adapter(listBarang, this);

        recyclerView = findViewById(R.id.rcvBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT * FROM tblbarang WHERE idkategori !=1 AND barang LIKE '%"+cari.getText().toString()+"%' ";

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
        }adapter.notifyDataSetChanged();

    }

    public void selectedBarang(Barang barang) {
        Intent in = new Intent();
        in.putExtra("idbarang", barang.getIdbarang());
        in.putExtra("harga", barang.getHarga());
        in.putExtra("barang", barang.getBarang());
        setResult(RESULT_OK, in);
        finish();
    }
}




class Pilih_Barang_Adapter extends RecyclerView.Adapter<Pilih_Barang_Adapter.ViewHolder>{

    private ArrayList<Barang> barang;
    private Context context;

    public Pilih_Barang_Adapter(ArrayList<Barang> barang, Context context) {
        this.barang = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilih_barang_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Barang item = barang.get(position);

        holder.tvHargaBeli.setText(ModulBengkel.removeE(item.getHargabeli()));
        holder.tvStok.setText(item.getStok());
        holder.tvHarga.setText(ModulBengkel.removeE(item.getHarga()));
        holder.tvBarang.setText(item.getBarang());

        holder.pilihBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Pilih_Barang_Bengkel_)context).selectedBarang(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHargaBeli, tvStok, tvHarga, tvBarang;
        ConstraintLayout pilihBarang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHargaBeli = itemView.findViewById(R.id.tvHargaBeli);
            tvStok = itemView.findViewById(R.id.tvStok);
            tvHarga = itemView.findViewById(R.id.tvHargaJual);
            tvBarang = itemView.findViewById(R.id.tvNamaBarang);
            pilihBarang = itemView.findViewById(R.id.pilihBarang);
        }
    }
}