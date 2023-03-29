package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Penjualan_Ukuran_Toko_Sepatu extends AppCompatActivity implements InterfacePenjualanUkuran_Sepatu {
    public  String stok;
    public String idukur;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    public static String idukuran;
    public static int setukuran=0;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_ukuran_sepatu);
        idukur = getIntent().getStringExtra("idukur");

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pilih Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idukuran="";

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);

        setBarang(idukur);
        TextView tv = findViewById(R.id.tvBarang);
        TextView tvdes = findViewById(R.id.tvDek);
        CardView cl= findViewById(R.id.cvBarang);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        RotateAnimation rotate = new RotateAnimation(0,90, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        ImageView img = findViewById(R.id.imageView3);
//        tv.setTransitionName("judula");
//        tvdes.setTransitionName("subjudula");
        cl.setTransitionName("kotaka");
        img.startAnimation(rotate);
        rotate.setFillAfter(true);


        getUkuran();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.shop_menu_sepatu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RotateAnimation rotate = new RotateAnimation(90,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        ImageView img = findViewById(R.id.imageView3);
        img.startAnimation(rotate);
        rotate.setFillAfter(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id==android.R.id.home){
            onBackPressed();
        }else if(res_id==R.id.belanja){
            Intent i =  new Intent(Menu_Penjualan_Ukuran_Toko_Sepatu.this,Menu_Penjualan_Sepatu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            i.putExtra("idjual",Menu_Penjualan_Barang_Transaksi_Sepatu.idjual);
            startActivity(i);
        }
        return true;
    }

    public void back (View view){
        onBackPressed();
    }


    private void setBarang(String idbarang){
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang="+idbarang) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.tvBarang,ModulTokoSepatu.getString(c,"barang")) ;
            ModulTokoSepatu.setText(v,R.id.tvDek,ModulTokoSepatu.getString(c,"deskripsi")) ;

        }


    }


    public void setHarga(String idukuran){
        TextView tv=findViewById(R.id.textView17);
        tv.setVisibility(View.VISIBLE);
        Cursor c = db.sq("SELECT * FROM tblukuran WHERE idukuran="+idukuran) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.tvPrice,ModulTokoSepatu.removeE(ModulTokoSepatu.getString(c,"harga"))) ;
            stok = ModulTokoSepatu.getString(c,"stok");
            if (stok.equals("0")){
                ModulTokoSepatu.setText(v,R.id.tvStokBarang,"Stok sudah habis");
            } else  if (stok.equals("1")){
                ModulTokoSepatu.setText(v,R.id.tvStokBarang,"Stok tinggal 1");
            }else{
                ModulTokoSepatu.setText(v,R.id.tvStokBarang,"Jumlah Stok : "+stok);
            }
        }
    }

    public void getUkuran() {
        arrayList.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Menu_Penjualan_Ukuran_Toko_Sepatu.this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recUkuran) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihUkur(this,arrayList) ;
        recyclerView.setAdapter(adapter);

        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tblukuran")+ModulTokoSepatu.sWhere("idbarang",idukur) + " ORDER BY idukuran ASC") ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idukuran")+"__"+ModulTokoSepatu.getString(c,"ukuran") + "__" + ModulTokoSepatu.getString(c,"stok");


            arrayList.add(campur);
        }


        adapter.notifyDataSetChanged();
    }




    public void beliaja(View view) {
        if (idukuran == ""){
            ModulTokoSepatu.showToast(this,"Pilih dahulu ukuran yang dibeli");
        }else {
            Cursor c = db.sq("SELECT * FROM tblukuran WHERE idukuran="+idukuran) ;
            if(c.getCount() > 0){
                c.moveToNext() ;

                stok=ModulTokoSepatu.getString(c,"stok") ;
                if (stok.equals("0")){
                    ModulTokoSepatu.showToast(this,"Stok sudah habis");
                }else{
                    Intent i = new Intent(Menu_Penjualan_Ukuran_Toko_Sepatu.this, Menu_Penjualan_Sepatu.class);
                    i.putExtra("idukuran", idukuran);
                    i.putExtra("idjual", Menu_Penjualan_Barang_Transaksi_Sepatu.idjual);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                    startActivity(i);

                }
            }


        }
    }
}

class AdapterPilihUkur extends RecyclerView.Adapter<AdapterPilihUkur.ViewHolder>{
    Context c;
    ArrayList<String>data;
    int row_index=0;

    public  AdapterPilihUkur(Context a, ArrayList<String>kota){
        this.data=kota;
        c=a;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pilihukur_sepatu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String[] row = data.get(position).split("__");
        holder.ukuran.setText(ModulTokoSepatu.toUppercase(row[1]));
        holder.cvUkur.setTag(row[0]);

        holder.cvUkur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu_Penjualan_Ukuran_Toko_Sepatu.setukuran=position;
                row_index=position;
                notifyDataSetChanged();


            }
        });

        if(row_index==position){
            holder.cvUkur.setCardBackgroundColor(Color.parseColor("#3f51b5"));
            holder.ukuran.setTextColor(Color.parseColor("#ffffff"));
            Menu_Penjualan_Ukuran_Toko_Sepatu.idukuran= holder.cvUkur.getTag().toString();
            if (c instanceof InterfacePenjualanUkuran_Sepatu){
                ((InterfacePenjualanUkuran_Sepatu)c).setHarga(holder.cvUkur.getTag().toString());
            }
        }else{
            holder.cvUkur.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.ukuran.setTextColor(Color.parseColor("#212121"));
        }
    }





    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ukuran;
        CardView cvUkur;
        public ViewHolder(View itemView) {
            super(itemView);
            ukuran=itemView.findViewById(R.id.tvUkur);
            cvUkur=itemView.findViewById(R.id.cvUkur);
        }
    }
}
