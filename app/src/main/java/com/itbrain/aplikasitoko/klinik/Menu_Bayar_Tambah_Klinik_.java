package com.itbrain.aplikasitoko.klinik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Bayar_Tambah_Klinik_ extends AppCompatActivity {
    ModulKlinik config,temp;
    DatabaseKlinik db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bayar_tambah_klinik_);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new ModulKlinik(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulKlinik(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseKlinik(this) ;
        v = this.findViewById(android.R.id.content);

        getBayar("");

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
                getBayar(a);

            }
        });


    ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBayar("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getBayar(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recbayar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterBayar(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("view_periksa")+ " flagperiksa=1 AND "+ModulKlinik.sLike("pasien",cari)+ModulKlinik.sOrderASC("fakturperiksa");
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idperiksa")+"__"+ModulKlinik.getString(c,"fakturperiksa") + "__" + ModulKlinik.getString(c,"pasien")+ "__" + ModulKlinik.getString(c,"tglperiksa")+ "__" + ModulKlinik.getString(c,"total");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
}

class AdapterBayar extends RecyclerView.Adapter<AdapterBayar.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public AdapterBayar(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Nama Pasien : " + row[2]);
        holder.notelp.setText("Total biaya : " + ModulKlinik.removeE(row[4]));
        holder.tvOpt.setText(row[3]);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = holder.cv.getTag().toString();
                Intent i = new Intent(c, Menu_pemeriksaan_klinik.class);
                i.putExtra("idorder", id);
                i.putExtra("type", "bayar");
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, notelp, tvOpt;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama = (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp = (TextView) itemView.findViewById(R.id.tNo);
            tvOpt = (TextView) itemView.findViewById(R.id.tgl);

        }
    }
}