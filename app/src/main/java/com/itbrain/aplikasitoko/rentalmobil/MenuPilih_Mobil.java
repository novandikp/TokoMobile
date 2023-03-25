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

public class MenuPilih_Mobil extends AppCompatActivity {
    ModulRentalMobil config, temp;
    DatabaseRentalMobil db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ConstraintLayout spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pilih_mobil);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new ModulRentalMobil(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulRentalMobil(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseRentalMobil(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final String type= getIntent().getStringExtra("type");
        if (type.equals("pelanggan")){
            getPelanggan("");
        }else if(type.equals("jasa")){
            getKendaraan("");
        }else if(type.equals("teknisi")){
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
                if (type.equals("pelanggan")){
                    getPelanggan(a);
                }else if(type.equals("jasa")){
                    getKendaraan(a);
                }else if(type.equals("teknisi")){
                    getPegawai(a);
                }
            }
        });

    }

    public void getPegawai(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPegawaiMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("tblpegawai")+" idpegawai!=1 AND "+ModulRentalMobil.sLike("pegawai",cari) + " ORDER BY pegawai ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idpegawai")+"__"+ModulRentalMobil.getString(c,"pegawai") + "__" + ModulRentalMobil.getString(c,"alamatpegawai")+ "__" + ModulRentalMobil.getString(c,"nopegawai")+ "__" + ModulRentalMobil.getString(c,"ktppegawai");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getKendaraan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihKendaraanMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("tblkendaraan")+ModulRentalMobil.sWhere("flagada","0")+" AND "+ModulRentalMobil.sLike("mobil",cari) + " ORDER BY mobil ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idkendaraan")+"__"+ModulRentalMobil.getString(c,"mobil") + "__" + ModulRentalMobil.getString(c,"tahunkeluaran")+ "__" + ModulRentalMobil.getString(c,"plat")+ "__" + ModulRentalMobil.getString(c,"harga");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
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





class AdapterPilihKendaraanMobil extends RecyclerView.Adapter<AdapterPilihKendaraanMobil.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterPilihKendaraanMobil(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_mastersatu_mobil, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mobil,tahun,plat,harga, tvOpt;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            mobil = (TextView) view.findViewById(R.id.t1);
            tahun = (TextView) view.findViewById(R.id.t2);
            plat = (TextView) view.findViewById(R.id.t3);
            harga = (TextView) view.findViewById(R.id.t4);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cv);


        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");
        viewHolder.mobil.setText(row[1]);
        viewHolder.tahun.setText("Tahun Keluaran : "+row[2]);
        viewHolder.plat.setText("Plat : "+row[3]);
        viewHolder.harga.setText("Harga : "+ModulRentalMobil.removeE(row[4])+" /hari");
        viewHolder.cvBarang.setTag(row[0]);
        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c, MenuRental_Mobil. class);
                i.putExtra("idbarang",viewHolder.cvBarang.getTag().toString());
                ((MenuPilih_Mobil)c).setResult(200,i);
                ((MenuPilih_Mobil)c).finish();
            }
        });



        viewHolder.tvOpt.setVisibility(View.GONE);

    }
}





