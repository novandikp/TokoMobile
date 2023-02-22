package com.itbrain.aplikasitoko.rentalmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuLaporanBagPegawai_Mobil extends AppCompatActivity {

    DatabaseRentalMobil db;
    String type;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanbagpegawai_mobil);
        type = getIntent().getStringExtra("type");
        db = new DatabaseRentalMobil(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (type.equals("pegawai")) {
            getPegawai("");

        }


    final EditText eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String a = eCari.getText().toString() ;
            arrayList.clear();
            if (type.equals("pegawai")){
                getPegawai(a);
            }

        }
    });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
   }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("pegawai")) {
            getPegawai("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPegawai(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPegawaiMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("tblpegawai")+" idpegawai!=1 AND "+ModulRentalMobil.sLike("pegawai",cari) + " ORDER BY pegawai ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idpegawai")+"__"+ModulRentalMobil.getString(c,"pegawai") + "__" + ModulRentalMobil.getString(c,"alamatpegawai")+ "__" + ModulRentalMobil.getString(c,"nopegawai")+ "__" + ModulRentalMobil.getString(c,"ktppegawai");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, MenuExportExcel_Mobil.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}

class AdapterLaporanPegawaiMobil extends RecyclerView.Adapter<AdapterLaporanPegawaiMobil.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLaporanPegawaiMobil(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_mastersatu_mobil, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No Telp : "+row[3]);
        holder.nik.setText("NIK : "+row[4]);
        holder.tvOpt.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,nik,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.t1);
            nik = itemView.findViewById(R.id.t2);
            alamat = itemView.findViewById(R.id.t3);
            notelp = itemView.findViewById(R.id.t4);
            tvOpt = itemView.findViewById(R.id.tvOpt);
        }
    }
}