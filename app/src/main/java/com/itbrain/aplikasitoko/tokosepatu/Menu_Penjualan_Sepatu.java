package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Penjualan_Sepatu extends AppCompatActivity implements MenuPenjualanInter_Sepatu{
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    public static String idukuran;
    public static String idjual;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    int hargatampil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_sepatu);

        idukuran = getIntent().getStringExtra("idukuran");

        idjual=getIntent().getStringExtra("idjual");

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Pembelian");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));

        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        hargatampil=0;
        if (idukuran!=null){
            inputData();
        }

        getUkuran(idjual);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public void getUkuran(String id) {

        arrayList.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recDaf) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterDaftarBarang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String campur="";
        Cursor c = db.sq("SELECT * FROM view_orderdetail where idjual="+idjual+";");
        while(c.moveToNext()){
            campur = ModulTokoSepatu.getString(c,"iddetailjual")+"__"+ModulTokoSepatu.getString(c,"barang") + "__" + ModulTokoSepatu.getString(c,"deskripsi") + "__" + ModulTokoSepatu.getString(c,"ukuran") + "__" + ModulTokoSepatu.getString(c,"harga")+ "__" + ModulTokoSepatu.getString(c,"stok")+ "__" + ModulTokoSepatu.getString(c,"jumlah");

            arrayList.add(campur);



        }
        setHarga();

        adapter.notifyDataSetChanged();

    }
    public void inputData(){
        String hasil=" ";
        Cursor c = db.sq("SELECT * FROM tbldetailjual WHERE idjual="+idjual);
        while (c.moveToNext()){
            String data=ModulTokoSepatu.getString(c,"idukuran");

            if(idukuran==data||idukuran.equals(data)){
                hasil=hasil+"b ";
            }else{
                hasil=hasil+"s ";
            }
        }
        String regex=".+b.+";
        if (hasil.matches(regex)){
            db.exc("UPDATE tbldetailjual set idukuran="+idukuran+",jumlah=jumlah+1 where idukuran="+idukuran+" AND idjual="+idjual);
        }else{
            db.exc("INSERT INTO tbldetailjual (idukuran,idjual) VALUES (" + idukuran + "," + idjual + ");");
            db.exc("UPDATE tbldetailjual set idukuran="+idukuran+",jumlah=jumlah+1 where idukuran="+idukuran+" AND idjual="+idjual);
        }

//       if (tes[0]==".b"){
//           ModulTokoSepatu.showToast(this,"Sudah ada");
//       }else {
//
//       }
    }

    public void setHarga(){

        TextView hargaTotal = findViewById(R.id.hargaTotal);
        hargaTotal.setText("");
        Cursor c=db.sq("SELECT * FROM tbljual where idjual="+idjual);
        while (c.moveToNext()){
            hargaTotal.setText(ModulTokoSepatu.removeE(ModulTokoSepatu.getString(c,"total")));
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog alert;
        alertDialog.setMessage("Apakah anda yakin untuk kembali ke menu utama")
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent intent = new Intent(Menu_Penjualan_Sepatu.this,Aplikasi_Menu_Transaksi_Toko_Sepatu.class);
                        startActivity(intent);
//



                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alert=alertDialog.create();

        alert.setTitle("Kembali ke Menu Utama");
        alert.show();

    }


    public void tambah(View view) {
        finish();
        Intent i = new Intent(Menu_Penjualan_Sepatu.this,Menu_Penjualan_Barang_Transaksi_Sepatu.class);
        startActivity(i);
    }

    public void bayar(View view) {
        String jual;
        Cursor c=db.sq("SELECT * from tbljual where idjual="+idjual);
        if(c.getCount() > 0){
            c.moveToNext() ;
            jual=ModulTokoSepatu.getString(c,"total");

            if (jual.equals("0")||jual.equals("0")){
                ModulTokoSepatu.showToast(this,"Keranjang masih kosong");

            }else{
                Intent i = new Intent(Menu_Penjualan_Sepatu.this,Pembayaran_Sepatu.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                i.putExtra("id",idjual);
                startActivity(i);
            }

        }

    }
}


    class AdapterDaftarBarang extends RecyclerView.Adapter<AdapterDaftarBarang.ViewHolder>{
        ArrayList <String> data;
        Context c;

        public  AdapterDaftarBarang(Context a, ArrayList<String>kota){
            this.data=kota;
            c=a;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_penjualan_sepatu,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final String[] row = data.get(position).split("__");
            holder.hapus.setTag(row[0]);
            holder.barang.setText(row[1]);
            holder.deskripsi.setText(row[2]);
            holder.ukuran.setText(row[3]);
            holder.harga.setText(ModulTokoSepatu.removeE(row[4]));
            holder.eStok.setText(row[6]);
            holder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
                    String id = holder.hapus.getTag().toString();
                    db.exc("DELETE FROM tbldetailjual where iddetailjual="+id);
                    if (c instanceof MenuPenjualanInter_Sepatu){
                        ((MenuPenjualanInter_Sepatu)c).getUkuran(Menu_Penjualan_Sepatu.idjual);
                    }
                }
            });
            holder.tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int stok = ModulTokoSepatu.strToInt(holder.eStok.getText().toString());
                    stok+=1;
                    holder.eStok.setText(ModulTokoSepatu.intToStr(stok));
                    if (c instanceof MenuPenjualanInter_Sepatu){
                        ((MenuPenjualanInter_Sepatu)c).setHarga();
                    }

                }
            });
            holder.kurang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int stok = ModulTokoSepatu.strToInt(holder.eStok.getText().toString());
                    stok-=1;
                    holder.eStok.setText(ModulTokoSepatu.intToStr(stok));
                    if (c instanceof MenuPenjualanInter_Sepatu){
                        ((MenuPenjualanInter_Sepatu)c).setHarga();
                    }
                }
            });


            holder.eStok.addTextChangedListener(new TextWatcher() {
                DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int batas =ModulTokoSepatu.strToInt(holder.eStok.getText().toString());
                    int limit=ModulTokoSepatu.strToInt(row[5])+ModulTokoSepatu.strToInt(row[6]);
//                limit+=1;
                    if (batas>limit||batas<=0){
                        holder.eStok.setText("1");
                    }


                    db.exc("UPDATE tbldetailjual SET jumlah="+holder.eStok.getText().toString()+" WHERE iddetailjual="+holder.hapus.getTag().toString());

                }
            });


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView barang,deskripsi,harga,ukuran;
            EditText eStok;
            ImageView tambah,kurang,hapus;
            public ViewHolder(View itemView) {
                super(itemView);
                barang = itemView.findViewById(R.id.tvBarang);
                deskripsi= itemView.findViewById(R.id.tvDek);
                harga = itemView.findViewById(R.id.tvHarga);
                ukuran=itemView.findViewById(R.id.tvUkuran);
                eStok=itemView.findViewById(R.id.eStok);
                tambah= itemView.findViewById(R.id.tambah);
                hapus= itemView.findViewById(R.id.hapus);
                kurang= itemView.findViewById(R.id.kurang);


            }
        }
    }
