package com.itbrain.aplikasitoko;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Menu_Pembayaran_Hutang_Bengkel_ extends AppCompatActivity {
    ModulBengkel config, temp;
    Database_Bengkel_ db;
    View view;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pembayaran_hutang_bengkel_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new ModulBengkel(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulBengkel(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new Database_Bengkel_(this);
        view = this.findViewById(android.R.id.content);

        getBayar("");
        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String a = eCari.getText().toString();
                arrayList.clear();
                getBayar(a);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        getBayar("");
    }

    public void getBayar(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvHutang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterHutang(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("tblpelanggan")+ " hutang>0 AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("pelanggan");
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idpelanggan")+"__"+ModulBengkel.getString(c,"pelanggan") + "__" + ModulBengkel.getString(c,"alamat")+ "__" + ModulBengkel.getString(c,"notelp")+ "__" + ModulBengkel.getString(c,"hutang");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();
    }
}

class AdapterHutang extends RecyclerView.Adapter<AdapterHutang.ViewHolder>{
    private ArrayList<String> data;
    Context context;

    View v;

    public AdapterHutang(Context a, ArrayList<String> kota) {
       this.data = kota;
       context = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bengkel_hutang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] row = data.get(position).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No Telp : "+row[3]);
        holder.hutang.setText("Total Hutang : "+ModulBengkel.removeE(row[4]));

        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MenuBayarHutang.class);
                i.putExtra("idpelanggan", holder.tvOpt.getTag().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,hutang,tvOpt;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hutang = itemView.findViewById(R.id.tHutang);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            cv=itemView.findViewById(R.id.cv);
        }
    }
}