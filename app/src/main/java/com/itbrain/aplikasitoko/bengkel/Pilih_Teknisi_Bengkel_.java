package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.itbrain.aplikasitoko.Model.Teknisi;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Pilih_Teknisi_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Pilih_Teknisi_Adapter adapter;

    ArrayList<Teknisi> listTeknisi;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_teknisi_bengkel_);

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

        listTeknisi = new ArrayList<>();
        adapter = new Pilih_Teknisi_Adapter(listTeknisi, this);

        recyclerView = findViewById(R.id.rcvTeknisi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    protected void onResume() {
        super.onResume();
        selectData();
    }

    @SuppressLint("Range")
    public void selectData() {
        String sql = "SELECT * FROM tblteknisi WHERE idteknisi !=1 AND teknisi LIKE '%"+cari.getText().toString()+"%' ";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listTeknisi.clear();
            while (cursor.moveToNext()) {
                listTeknisi.add(new Teknisi(
                        cursor.getInt(cursor.getColumnIndex("idteknisi")),
                        cursor.getString(cursor.getColumnIndex("teknisi")),
                        cursor.getString(cursor.getColumnIndex("alamatteknisi")),
                        cursor.getString(cursor.getColumnIndex("noteknisi"))
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void selectedTeknisi (Teknisi teknisi) {
        Intent in = new Intent();
        in.putExtra("idteknisi", teknisi.getIdteknisi());
        in.putExtra("teknisi", teknisi.getTeknisi());
        setResult(RESULT_OK, in);
        finish();
    }
}


class Pilih_Teknisi_Adapter extends RecyclerView.Adapter<Pilih_Teknisi_Adapter.ViewHolder> {
    private ArrayList<Teknisi> teknisi;
    private Context context;

    public Pilih_Teknisi_Adapter(ArrayList<Teknisi> teknisi, Context context) {
        this.teknisi = teknisi;
        this.context = context;
    }

    @NonNull
    @Override
    public Pilih_Teknisi_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilih_teknisi_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Pilih_Teknisi_Adapter.ViewHolder holder, int position) {
        Teknisi item = teknisi.get(position);

        holder.tvTeknisi.setText(item.getTeknisi());
        holder.tvAlamat.setText(item.getAlamatteknisi());
        holder.tvNomor.setText(item.getNoteknisi());

        holder.pilihTeknisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Pilih_Teknisi_Bengkel_)context).selectedTeknisi(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teknisi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeknisi, tvAlamat, tvNomor;
        ConstraintLayout pilihTeknisi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pilihTeknisi = itemView.findViewById(R.id.pilihTeknisi);
            tvTeknisi = itemView.findViewById(R.id.tvNamaTeknisi);
            tvAlamat = itemView.findViewById(R.id.tvAlamatTeknisi);
            tvNomor = itemView.findViewById(R.id.tvNoTeknisi);
        }
    }
}