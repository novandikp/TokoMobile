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
import com.itbrain.aplikasitoko.Salon.ActivityExportExcelSalon;

import java.util.ArrayList;

public class LaporanJasa_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporanjasa_kwitansi);

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

        if (type.equals("jasa")) {
            adapter = new AdapterJasa(this, arrayList);
            recyclerView.setAdapter(adapter);
            getJasa("");
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
                if (type.equals("jasa")) {
                    getJasa(a);
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

    public void getJasa(String keyword){
        String hasil = "" ;
        if (TextUtils.isEmpty(keyword)){
            hasil="SELECT * FROM tbljasa";
        }else {
            hasil="SELECT * FROM tbljasa WHERE jasa LIKE '%"+keyword+"%' ORDER BY jasa";
        }
        Cursor c = db.sq(hasil);
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String jasa = c.getString(c.getColumnIndex("jasa"));

                String campur = jasa+"__";
                arrayList.add(campur);
            }
        } else {
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, ExportExcel_Kwitansi.class) ;
        i.putExtra("type","jasa") ;
        startActivity(i);
    }
}

class AdapterJasa extends RecyclerView.Adapter<AdapterJasa.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterJasa(Context a, ArrayList<String> kota) {
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
        private TextView jasa, harga, kategori;

        public ViewHolder(View view) {
            super(view);

            kategori = (TextView) view.findViewById(R.id.tNamas);
            kategori.setVisibility(View.INVISIBLE);
            jasa = (TextView) view.findViewById(R.id.tAlam);
            harga = (TextView) view.findViewById(R.id.tTelp);
            harga.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.jasa.setText(row[0]);
    }
}