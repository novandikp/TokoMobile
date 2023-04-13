package com.itbrain.aplikasitoko.rentalmobil;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuPilihPelangganMobil extends AppCompatActivity {

    ModulRentalMobil config, temp;
    DatabaseRentalMobil db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ConstraintLayout spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pilih_pelanggan_mobil);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new ModulRentalMobil(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulRentalMobil(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseRentalMobil(this);
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final String type = getIntent().getStringExtra("type");
        if (type.equals("pelanggan")) {
            getPelanggan("");
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
                if (type.equals("pelanggan")){
                    getPelanggan(a);
                }
            }
        });

    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPelangganMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("tblpelanggan")+" idpelanggan!=1 AND "+ModulRentalMobil.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idpelanggan")+"__"+ModulRentalMobil.getString(c,"pelanggan") + "__" + ModulRentalMobil.getString(c,"alamat")+ "__" + ModulRentalMobil.getString(c,"notelp")+ "__" + ModulRentalMobil.getString(c,"noktp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

}

class AdapterPilihPelangganMobil extends RecyclerView.Adapter<AdapterPilihPelangganMobil.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPelangganMobil(Context a, ArrayList<String> kota) {
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
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c, MenuRental_Mobil.class);
                i.putExtra("idpelanggan",holder.tvOpt.getTag().toString());
                ((MenuPilihPelangganMobil)c).setResult(100,i);
                ((MenuPilihPelangganMobil)c).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt,nik;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.t1);
            alamat = (TextView) itemView.findViewById(R.id.t3);
            notelp=(TextView) itemView.findViewById(R.id.t4);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            nik=(TextView) itemView.findViewById(R.id.t2);

            cv=itemView.findViewById(R.id.cv);

        }
    }
}

