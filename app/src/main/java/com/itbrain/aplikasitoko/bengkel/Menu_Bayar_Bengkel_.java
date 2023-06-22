package com.itbrain.aplikasitoko.bengkel;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Bayar_Bengkel_ extends AppCompatActivity {
    ModulBengkel config, temp;
    Database_Bengkel_ db;
    View v;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bayar_bengkel_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new ModulBengkel(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulBengkel(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);

        getbayar("");

        ImageButton imageButton = findViewById(R.id.kembali24);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                getbayar(a);
            }
        });

    }

    public void getbayar(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvDaftar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterBayar(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qorder")+ " statusbayar='belum' AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("faktur");
        Cursor cursor = db.sq(q);
        while (cursor.moveToNext()) {
            String campur = ModulBengkel.getString(cursor,"idorder")+"__"+ModulBengkel.getString(cursor,"faktur") + "__" + ModulBengkel.getString(cursor,"pelanggan")+ "__" + ModulBengkel.getString(cursor,"tglorder")+ "__" + ModulBengkel.getString(cursor,"total");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();
    }
}

class AdapterBayar extends RecyclerView.Adapter<AdapterBayar.ViewHolder> {
    private ArrayList<String> data;
    Context context;
    View v;

    public AdapterBayar(Context a, ArrayList<String> kota) {
        this.data = kota;
        context = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran_bengkel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] row = data.get(position).split("__");
        holder.cv.setTag(row[0]);
        holder.Nomor.setText(row[1]);
        holder.Nama.setText(row[2]);
        holder.Total.setText(row[3]);
        holder.Tanggal.setText(ModulBengkel.removeE(row[4]));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.cv.getTag().toString();
                Intent i = new Intent(context, Menu_Servis_Bengkel_.class);
                i.putExtra("idorder",id);
                i.putExtra("type","bayar");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nomor,Nama,Total,Tanggal;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            Nomor= (TextView) itemView.findViewById(R.id.tvNomer);
            Nama = (TextView) itemView.findViewById(R.id.tvNama);
            Total=(TextView) itemView.findViewById(R.id.tvTotal);
            Tanggal=(TextView) itemView.findViewById(R.id.tgl);
        }
    }
}