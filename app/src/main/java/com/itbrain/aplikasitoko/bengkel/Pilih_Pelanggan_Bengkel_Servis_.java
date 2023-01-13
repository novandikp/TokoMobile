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
import android.widget.EditText;
import android.widget.TextView;

import com.itbrain.aplikasitoko.Model.Pelanggan;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Pilih_Pelanggan_Bengkel_Servis_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Pilih_Pelanggan_Adapter_Servis adapter;

    ArrayList<Pelanggan> listPelanggan;
    private EditText cari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_pelanggan_bengkel_servis);

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

        listPelanggan = new ArrayList<>();
        adapter = new Pilih_Pelanggan_Adapter_Servis(listPelanggan, this);

        recyclerView = findViewById(R.id.rcvPelanggan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT * FROM tblpelanggan WHERE idpelanggan !=1 AND pelanggan LIKE '%"+cari.getText().toString()+"%' ";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listPelanggan.clear();
            while (cursor.moveToNext()) {
                listPelanggan.add(new Pelanggan(
                        cursor.getInt(cursor.getColumnIndex("idpelanggan")),
                        cursor.getString(cursor.getColumnIndex("notelp")),
                        cursor.getString(cursor.getColumnIndex("pelanggan")),
                        cursor.getString(cursor.getColumnIndex("alamat"))
                ));
            }
            adapter.notifyDataSetChanged();
        }

    }

    public void selectedPelanggan(Pelanggan pelanggan) {
        Intent in = new Intent(String.valueOf(Pilih_Pelanggan_Bengkel_Servis_.class));
        in.putExtra("idpelanggan", pelanggan.getIdpelanggan());
        in.putExtra("pelanggan", pelanggan.getPelanggan());
        setResult(RESULT_OK, in);
        finish();

    }
}

class Pilih_Pelanggan_Adapter_Servis extends RecyclerView.Adapter<Pilih_Pelanggan_Adapter_Servis.ViewHolder>{

    private ArrayList<Pelanggan> pelanggan;
    private Context context;

    public Pilih_Pelanggan_Adapter_Servis(ArrayList<Pelanggan> pelanggan, Context context) {
        this.pelanggan = pelanggan;
        this.context = context;
    }

    @NonNull
    @Override
    public Pilih_Pelanggan_Adapter_Servis.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilih_pelanggan_bengkel_servis_, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull Pilih_Pelanggan_Adapter_Servis.ViewHolder holder, int position) {
        Pelanggan item = pelanggan.get(position);

        holder.tvPelanggan.setText(item.getPelanggan());
        holder.tvAlamat.setText(item.getAlamat());
        holder.tvNotelp.setText(item.getNotelp());

        holder.pilihPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Pilih_Pelanggan_Bengkel_Servis_)context).selectedPelanggan(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pelanggan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPelanggan, tvAlamat, tvNotelp;
        ConstraintLayout pilihPelanggan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pilihPelanggan = itemView.findViewById(R.id.pilihPelanggan);
            tvPelanggan = itemView.findViewById(R.id.tvNamaPelanggan);
            tvAlamat = itemView.findViewById(R.id.tvAlamatPelanggan);
            tvNotelp = itemView.findViewById(R.id.tvNomorPelanggan);
        }
    }
}