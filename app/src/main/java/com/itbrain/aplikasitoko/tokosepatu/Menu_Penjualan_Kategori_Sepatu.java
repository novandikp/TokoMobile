package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Penjualan_Kategori_Sepatu extends AppCompatActivity implements Pembelian_Sepatu{
    public static String idjual;
    ModulTokoSepatu config,temp;
    Context a;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_kategori_sepatu);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        idjual=getIdJual();
        refresh();
        TextView judul = findViewById(R.id.tvjudul);
        String type = getIntent().getStringExtra("type");
        if (type.equals("kat")){
            refresh();
        }else if(type.equals("pelanggan")){
            judul.setText("Pilih Pelanggan");
            getPelanggan("");
        }

//        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tbljual")+ModulTokoSepatu.sWhere("idjual",idjual));
//        c.moveToNext();
//        String pelanggan =".belum"+ModulTokoSepatu.getString(c,"idpelanggan")+".";
//        ModulTokoSepatu.showToast(this,pelanggan);
//        if (pelanggan.equals(".belumnull.")){
//            ModulTokoSepatu.showToast(this,"belum");
//        }



    }

    @Override
    public void onBackPressed() {
        Intent i =  new Intent(Menu_Penjualan_Kategori_Sepatu.this,Aplikasi_Menu_Transaksi_Toko_Sepatu.class);
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
            Intent i =  new Intent(Menu_Penjualan_Kategori_Sepatu.this,Menu_Penjualan_Sepatu.class);
            i.putExtra("idjual",idjual);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }
        return true;
    }


    public void refresh(){
        TextView judul = findViewById(R.id.tvjudul);
        if (idjual=="Belum"){
            db.exc("INSERT INTO tbljual (total,idpelanggan) VALUES (0,1)");
            idjual=getIdJual();
            refresh();

        }
        else {
            judul.setText("Pilih kategori");
            getKategori();

        }
    }



    public void getKategori(){
        arrayList.clear();
        recyclerView = findViewById(R.id.recKatBel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihKat(this,arrayList) ;
        recyclerView.setAdapter(adapter);

        Cursor c ;
        c = db.sq(ModulTokoSepatu.select("tblkategori")+ModulTokoSepatu.sOrderASC("kategori"));
        while(c.moveToNext()){
            String kategori = ModulTokoSepatu.getString(c,"kategori") ;
            String idkategori = ModulTokoSepatu.getString(c,"idkategori") ;
            arrayList.add(idkategori+"__"+kategori);
        }

        adapter.notifyDataSetChanged();
    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recKatBel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPelanggan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tblpelanggan") + ModulTokoSepatu.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idpelanggan")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"alamat")+ "__" + ModulTokoSepatu.getString(c,"notelp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public String getIdJual(){
        String idjual ="Belum";
        Cursor c;
        c=db.sq("SELECT * FROM tbljual where status IS NULL AND idjual IS NOT NULL");
        while (c.moveToNext()){
            idjual = ModulTokoSepatu.getString(c,"idjual");
        }
        return idjual;

    }

    public String getIdpel(){
        String idpelanggan ="Belum";
        Cursor c;
        c=db.sq("SELECT * FROM tbljual where status IS NULL AND idjual="+idjual);
        while (c.moveToNext()){
            idpelanggan = ModulTokoSepatu.getString(c,"idjual");
        }
        return idpelanggan;

    }


}

class AdapterPilihKat extends RecyclerView.Adapter<AdapterPilihKat.ViewHolder>{
    private ArrayList<String>data;
    Context c;
    int row_index=Menu_Penjualan_Barang_Transaksi_Sepatu.setItem;

    public AdapterPilihKat (Context a, ArrayList<String> kota){
        this.data=kota;
        c=a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pilihkat_sepatu, parent,false);
        return new ViewHolder(view)  ;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String[] row = data.get(position).split("__");
        holder.kategori.setText(row[1]);
        holder.cvKat.setTag(row[0]);

        holder.cvKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_Penjualan_Barang_Transaksi_Sepatu.setItem =position;
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            holder.cvKat.setCardBackgroundColor(Color.parseColor("#3f51b5"));
            holder.kategori.setTextColor(Color.parseColor("#ffffff"));
            String id = holder.cvKat.getTag().toString();
            Menu_Penjualan_Barang_Transaksi_Sepatu.idkategori=id;
            ((Menu_Penjualan_Barang_Transaksi_Sepatu)c).getBarang("");
        }else{
            holder.cvKat.setCardBackgroundColor(Color.parseColor("#fafafa"));
            holder.kategori.setTextColor(Color.parseColor("#212121"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kategori;
        private CardView cvKat;
        public ViewHolder(View itemView) {

            super(itemView);
            kategori=itemView.findViewById(R.id.eKategori);
            cvKat=itemView.findViewById(R.id.cvKat);
        }
    }
}

class AdapterPilihPelanggan extends RecyclerView.Adapter<AdapterPilihPelanggan.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPelanggan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilihpelanggan_sepatu, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cvPel.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);
        holder.cvPel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
                String id = holder.cvPel.getTag().toString();
                db.exc("UPDATE tbljual SET idpelanggan="+id+" WHERE idjual="+Menu_Penjualan_Kategori_Sepatu.idjual+";");
                ((Menu_Penjualan_Kategori_Sepatu)c).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp;
        CardView cvPel;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            cvPel=itemView.findViewById(R.id.cvPelanggan);

        }
    }
}
