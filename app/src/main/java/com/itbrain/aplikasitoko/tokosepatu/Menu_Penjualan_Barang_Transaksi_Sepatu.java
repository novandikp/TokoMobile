package com.itbrain.aplikasitoko.tokosepatu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Penjualan_Barang_Transaksi_Sepatu  extends AppCompatActivity {
    public static String idjual;
    ModulTokoSepatu config,temp;
    Context a;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    ArrayList arrayKat = new  ArrayList();

    RecyclerView recyclerView;
    public static int setItem=0;
    public static String idkategori="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_barang_sepatu);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pilih Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


//        final String idbar = getIntent().getStringExtra("idbar");

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        getidJual();

        getKategori();
        RecyclerView rec=findViewById(R.id.recKat);
        getBarang("");





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
                getBarang(a);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i =  new Intent(Menu_Penjualan_Barang_Transaksi_Sepatu.this,Aplikasi_Menu_Transaksi_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.shop_menu_sepatu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id==android.R.id.home){
            finish();
        }else if(res_id==R.id.belanja){
            Intent i =  new Intent(Menu_Penjualan_Barang_Transaksi_Sepatu.this,Menu_Penjualan_Sepatu.class);
            i.putExtra("idjual",idjual);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }
        return true;
    }

    public void getKategori() {
        arrayKat.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recKat) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihKat(this,arrayKat) ;
        recyclerView.setAdapter(adapter);

        Cursor c = db.sq(ModulTokoSepatu.select("tblkategori")) ;
        while(c.moveToNext()){
            String kategori = ModulTokoSepatu.getString(c,"kategori") ;
            String idkategori = ModulTokoSepatu.getString(c,"idkategori") ;
            arrayKat.add(idkategori+"__"+kategori);
        }
        adapter.notifyDataSetChanged();
    }

    public void getBarang( String cari){
        arrayList.clear();
        recyclerView = findViewById(R.id.recBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihBar(this,arrayList);
        recyclerView.setAdapter(adapter);
        String barang,idbarang,deskripsi,harga;
        Cursor c ;
        c = db.sq(whereKat(idkategori,cari));
        while(c.moveToNext()){
            barang = ModulTokoSepatu.getString(c,"barang") ;
            deskripsi = ModulTokoSepatu.getString(c,"deskripsi");
            idbarang = ModulTokoSepatu.getString(c,"idbarang") ;
            harga=setHarga(idbarang);
            arrayList.add(idbarang+"__"+barang+"__"+deskripsi+"__"+harga);
        }

        adapter.notifyDataSetChanged();
    }

    public String whereKat(String id,String cari){
        if (idkategori.equals("0")){
            return ModulTokoSepatu.selectwhere("tblbarang")+ModulTokoSepatu.sLike("barang",cari) + " ORDER BY barang ASC";
        }
        if(cari.equals("")){
            return ModulTokoSepatu.selectwhere("tblbarang")+ModulTokoSepatu.sWhere("idkategori",id)+" ORDER BY barang ASC";
        } else {
            return ModulTokoSepatu.selectwhere("tblbarang")+ModulTokoSepatu.sWhere("idkategori",id)+" AND "+ModulTokoSepatu.sLike("barang",cari) + " ORDER BY barang ASC";
        }
    }

    public String setHarga(String idbar){
        Cursor c ;
        String harga="0";
        c = db.sq("SELECT MIN(harga) FROM tblukuran WHERE idbarang="+idbar);
        while(c.moveToNext()) {
            harga = ModulTokoSepatu.getString(c,"MIN(harga)");
        }
        return harga;
    }

    public  void getidJual(){
        Cursor c = db.sq("SELECT * FROM qjual WHERE bayar=0");
        if (c.getCount()>0){
            c.moveToLast();
            idjual = ModulTokoSepatu.getString(c,"idjual");
        }else{
            db.exc("INSERT INTO tbljual (idpelanggan) VALUES (1)");
            getidJual();
        }
    }
}

class AdapterPilihBar extends RecyclerView.Adapter<AdapterPilihBar.ViewHolder>{
    private ArrayList<String>data;
    Context c;

    public AdapterPilihBar(Context a, ArrayList<String>kota){
        this.data=kota;
        c=a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pilihbar_sepatu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String [] row = data.get(position).split("__");
        holder.barang.setText(row[1]);
        holder.desk.setText(row[2]);
        if (row[3].equals("null")){
            holder.harga.setText("Harga belum tersedia");
        }else {
            holder.harga.setText(ModulTokoSepatu.removeE(row[3]));
        }
        String inisia= Character.toString(row[1].charAt(0));
        holder.cvBarang.setTag(row[0]);
        holder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = holder.cvBarang.getTag().toString();
                holder.barang.setTransitionName("judula");
                holder.desk.setTransitionName("subjudula");
                holder.cvBarang.setTransitionName("kotaka");
                Pair<View, String> pair1= Pair.create((View)holder.barang,holder.barang.getTransitionName());
                Pair<View, String> pair2= Pair.create((View)holder.desk,holder.desk.getTransitionName());
                Pair<View, String> pair3= Pair.create((View)holder.cvBarang,holder.cvBarang.getTransitionName());

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity
                        )c,pair3);
                Intent intent = new Intent(c,Menu_Penjualan_Ukuran_Toko_Sepatu.class);
                intent.putExtra("idukur",id);
                intent.putExtra("idjual",Menu_Penjualan_Barang_Transaksi_Sepatu.idjual);
                c.startActivity(intent,optionsCompat.toBundle());


            }
        });
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barang,desk,harga;
        CardView cvBarang;
        public ViewHolder(View itemView) {

            super(itemView);
            cvBarang=itemView.findViewById(R.id.cvBarang);
            barang=itemView.findViewById(R.id.tvBarang);
            desk=itemView.findViewById(R.id.tvDek);
            harga=itemView.findViewById(R.id.tvHarga);


        }
    }
}
