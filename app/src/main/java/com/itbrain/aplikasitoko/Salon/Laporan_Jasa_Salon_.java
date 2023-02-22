package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Jasa_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    View v;
    DatabaseSalon db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_jasa_salon_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        db = new DatabaseSalon(this);


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appbar = (Toolbar) findViewById(R.id.toolbar68);
//        setSupportActionBar(appbar);
//        type = getIntent().getStringExtra("type");
        //String title = "judul";

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.jancok);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterJasa(this, arrayList);
        recyclerView.setAdapter(adapter);
        getJasa("");

        final EditText eCari = (EditText) findViewById(R.id.asu);
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
                getJasa(a);
            }
        });
       // FunctionSalon.btnBack(title, getSupportActionBar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getJasa(String cari) {
        String hasil = "";
        if (TextUtils.isEmpty(cari)) {
            hasil = QuerySalon.select("tbljasa") + QuerySalon.sOrderASC("jasa") + " LIMIT 30";
        } else {
            hasil = QuerySalon.selectwhere("tbljasa") + QuerySalon.sLike("jasa", cari) + QuerySalon.sOrderASC("jasa");
        }
        Cursor c = db.sq(hasil);
        if (FunctionSalon.getCount(c) > 0) {
            FunctionSalon.setText(v, R.id.babi, "Jumlah Data : " + FunctionSalon.intToStr(FunctionSalon.getCount(c)));
            while (c.moveToNext()) {
                String jasa = FunctionSalon.getString(c, "jasa");
                String harga = FunctionSalon.getString(c, "harga");
                String idjasa = FunctionSalon.getString(c, "idjasa");

                String campur = jasa + "__" + FunctionSalon.removeE(harga) + "__" + idjasa;
                arrayList.add(campur);
            }
        } else {
            FunctionSalon.setText(v, R.id.babi, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcelSalon.class) ;
        i.putExtra("type","jasa") ;
        startActivity(i);
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_jasa_salon, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView jasa, harga, opt;

            public ViewHolder(View view) {
                super(view);

                jasa = (TextView) view.findViewById(R.id.tvNamaJasa);
                harga = (TextView) view.findViewById(R.id.tvHargaJasa);
                opt = (TextView) view.findViewById(R.id.tvOpt);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            final String[] row = data.get(i).split("__");

            viewHolder.jasa.setText("Jasa : " + row[0]);
            viewHolder.harga.setText("Harga : Rp. " + row[1]);
            viewHolder.opt.setVisibility(View.INVISIBLE);
        }
    }
}