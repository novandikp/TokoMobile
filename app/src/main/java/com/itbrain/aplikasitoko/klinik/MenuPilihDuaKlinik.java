package com.itbrain.aplikasitoko.klinik;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuPilihDuaKlinik extends AppCompatActivity {
    ModulKlinik config,temp;
    DatabaseKlinik db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ConstraintLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pilih_klinik);


        config = new ModulKlinik(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulKlinik(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseKlinik(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        final String type= getIntent().getStringExtra("type");
        if (type.equals("pelanggan")){

            getPelanggan("");

        }else if(type.equals("jasa")){


            getJasa("");

        }else if(type.equals("teknisi")){

            getDokter("");
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
                }else if(type.equals("jasa")){
                    getJasa(a);
                }else if(type.equals("teknisi")){
                    getDokter(a);
                }
            }
        });

    }

    public void getDokter(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recbayar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihDokterDua(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("tbldokter")+" iddokter!=1 AND " +ModulKlinik.sLike("dokter",cari) + " ORDER BY dokter ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"iddokter")+"__"+ModulKlinik.getString(c,"dokter") + "__" + ModulKlinik.getString(c,"alamatdokter")+ "__" + ModulKlinik.getString(c,"nodokter");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }



    public void getJasa(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recbayar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPilihJasa(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(ModulKlinik.selectwhere("tbljasa")+ModulKlinik.sLike("jasa",cari)) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idjasa")+"__"+ModulKlinik.getString(c,"jasa") + "__" + ModulKlinik.getString(c,"harga");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recbayar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPelangganDua(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("tblpasien") +" idpasien!=1 AND "+ModulKlinik.sLike("pasien",cari) + " ORDER BY pasien ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idpasien")+"__"+ModulKlinik.getString(c,"pasien") + "__" + ModulKlinik.getString(c,"alamat")+ "__" + ModulKlinik.getString(c,"notelp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }




}

class AdapterPilihPelangganDua extends RecyclerView.Adapter<AdapterPilihPelangganDua.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPelangganDua(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pelanggan, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c, MenuTambahJanjiKlinik.class);
                i.putExtra("idpelanggan",holder.tvOpt.getTag().toString());
                ((MenuPilihDuaKlinik)c).setResult(100,i);
                ((MenuPilihDuaKlinik)c).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            cv=itemView.findViewById(R.id.cv);

        }
    }
}



class AdapterPilihDokterDua extends RecyclerView.Adapter<AdapterPilihDokterDua.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihDokterDua(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pelanggan, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c, MenuTambahJanjiKlinik.class);
                i.putExtra("idteknisi",holder.tvOpt.getTag().toString());
                ((MenuPilihDuaKlinik)c).setResult(300,i);
                ((MenuPilihDuaKlinik)c).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);

        }
    }
}

