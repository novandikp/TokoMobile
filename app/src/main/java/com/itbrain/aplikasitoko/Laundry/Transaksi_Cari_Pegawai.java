package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterPegawaiLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Transaksi_Cari_Pegawai extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseLaundry db;
    Spinner spinner;
    String keyword = "", kategori = "";
    String a;
    List<String> idKat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_pegawai_laundry);
        db = new DatabaseLaundry(this);
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final EditText edtCari = (EditText) findViewById(R.id.eCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = edtCari.getText().toString();
                    getPegawai(keyword);
                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPegawai(String keyword) {
        recyclerView = (RecyclerView) findViewById(R.id.rcPelanggan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPegawaiCari(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q;
        if (keyword.isEmpty()) {
            q = "SELECT * FROM tblpegawai";
        } else {
            q = "SELECT * FROM tblpegawai WHERE pegawai LIKE '%" + keyword + "%'" + QueryLaundry.sOrderASC("pegawai");
        }
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = ModulLaundry.getString(c, "idpegawai") + "__" +
                        ModulLaundry.getString(c, "pegawai") + "__" +
                        ModulLaundry.getString(c, "alamatpegawai") + "__" +
                        ModulLaundry.getString(c, "notelppegawai");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahdata(View view) {
        DialogMasterPegawaiLaundry dialogMasterPegawaiLaundry = new DialogMasterPegawaiLaundry(this, true, null, false);
    }

    public void getList() {
        getPegawai(keyword);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPegawai(keyword);
    }
}
    class AdapterListPegawaiCari extends RecyclerView.Adapter<AdapterListPegawaiCari.PegawaiCariViewHolder> {
        private Context ctxAdapter;
        private ArrayList<String> data;

        public AdapterListPegawaiCari(Context ctxAdapter, ArrayList<String> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }


        @NonNull
        @Override
        public PegawaiCariViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.list_transaksi_cari_pegawai_laundry, viewGroup, false);
        return new PegawaiCariViewHolder(view);
    }

        @Override
        public void onBindViewHolder (@NonNull PegawaiCariViewHolder holder,int i){
        final String[] row = data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.telp.setText(row[3]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter, ActivityTransaksiTerimaLaundry.class);
                terima.putExtra("idpegawai", Integer.valueOf(row[0]));
                terima.putExtra("namapegawai", row[1]);
                ((Transaksi_Cari_Pegawai) ctxAdapter).setResult(1000, terima);
                ((Transaksi_Cari_Pegawai) ctxAdapter).finish();
            }
        });
    }
        @Override
        public int getItemCount() {
            return data.size();
        }

        class PegawaiCariViewHolder extends RecyclerView.ViewHolder{
            private TextView nama,alamat,telp;
            public PegawaiCariViewHolder(@NonNull View itemView) {
                super(itemView);
                nama=(TextView) itemView.findViewById(R.id.tvNamaPegawai);
                alamat=(TextView) itemView.findViewById(R.id.tvAlamatPegawai);
                telp=(TextView) itemView.findViewById(R.id.tvTelpPegawai);
            }
        }
    }