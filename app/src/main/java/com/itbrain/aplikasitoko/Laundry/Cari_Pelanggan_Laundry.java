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

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterPelangganLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Cari_Pelanggan_Laundry extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseLaundry db;
    Spinner spinner;
    String keyword = "", kategori = "";
    String a;
    List<String> idKat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_pelanggan_laundry);
        db = new DatabaseLaundry(this);
        getPelanggan(keyword);
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
                getPelanggan(keyword);
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
    public void getPelanggan(String keyword){
        recyclerView = (RecyclerView) findViewById(R.id.rcPelanggan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPelangganCari(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q;
        if (keyword.isEmpty()){
            q="SELECT * FROM tblpelanggan";
        }else {
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%'"+ QueryLaundry.sOrderASC("pelanggan");
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idpelanggan")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"alamat")+"__"+
                        ModulLaundry.getString(c,"notelp");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void tambahdata(View view) {
            DialogMasterPelangganLaundry dialogMasterPelangganLaundry =new DialogMasterPelangganLaundry(this, true, null,false);
    }
    public void getList(){
            getPelanggan(keyword);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan(keyword);

    }
}
class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder>{
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterListPelangganCari(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.list_transaksi_cari_pelanggan_laundry,viewGroup,false);
        return new PelangganCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.telp.setText(row[3]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter, ActivityTransaksiTerimaLaundry.class);
                terima.putExtra("idpelanggan",Integer.valueOf(row[0]));
                terima.putExtra("namapelanggan",row[1]);
                ((Cari_Pelanggan_Laundry)ctxAdapter).setResult(2000,terima);
                ((Cari_Pelanggan_Laundry)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PelangganCariViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp;
        public PelangganCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama= (TextView)itemView.findViewById(R.id.tvNamaPelanggan);
            alamat= (TextView)itemView.findViewById(R.id.tvAlamatPelanggan);
            telp= (TextView)itemView.findViewById(R.id.tvTelpPelanggan);
        }
    }
}