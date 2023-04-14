package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class LaporanPelanggan_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db ;
    ArrayList arrayList = new ArrayList() ;
    RecyclerView.Adapter adapter ;
    String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporanpelanggan_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        type = getIntent().getStringExtra("type");
        db = new DatabaseCetakKwitansi(this);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (type.equals("pelanggan")) {
            adapter = new AdapterPelanggan(this, arrayList);
            recyclerView.setAdapter(adapter);
            getPelanggan("");
        }

        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString();
                if (type.equals("pelanggan")) {
                    getPelanggan(a);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPelanggan(String keyword){
        String hasil = "" ;
        if (TextUtils.isEmpty(keyword)){
            hasil="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
        }else {
            hasil="SELECT * FROM tblpelanggan WHERE idpelanggan>0 AND (pelanggan LIKE '%"+keyword+"%' OR alamat LIKE '%"+keyword+"%' OR notelp LIKE '%"+keyword+"%') ORDER BY pelanggan";
        }
        Cursor c = db.sq(hasil) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String nama = c.getString(c.getColumnIndex("pelanggan"));
                String alamat = c.getString(c.getColumnIndex("alamat"));
                String telp = c.getString(c.getColumnIndex("notelp"));

                String campur = nama +"__"+alamat+"__"+telp ;
                arrayList.add(campur);
            }
        } else {
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, ExportExcel_Kwitansi.class) ;
        i.putExtra("type", "pelanggan") ;
        startActivity(i);
    }

    class AdapterPelanggan extends RecyclerView.Adapter<AdapterPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterPelanggan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_caripelanggan_kwitansi, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nma, alamat, notelp;

            public ViewHolder(View view) {
                super(view);

                nma = (TextView) view.findViewById(R.id.tNamas);
                alamat = (TextView) view.findViewById(R.id.tAlam);
                notelp = (TextView) view.findViewById(R.id.tTelp);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            String[] row = data.get(i).split("__");

            viewHolder.nma.setText("Nama : " + row[0]);
            viewHolder.alamat.setText("Alamat : " + row[1]);
            viewHolder.notelp.setText("No. Telepon : " + row[2]);
        }

    }
}