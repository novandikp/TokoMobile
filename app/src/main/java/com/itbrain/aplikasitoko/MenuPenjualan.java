package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Util.Penjualan;
import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;
import com.itbrain.aplikasitoko.bengkel.Pembayaran_Jual_Oederdil_Bengkel_;

import java.util.ArrayList;
import java.util.Calendar;

public class MenuPenjualan extends AppCompatActivity implements Penjualan{
    Database_Bengkel_ db;
    String idjual;
    View v;
    ArrayList arrayList = new  ArrayList();
    int year,day,month;
    double hargabeli;
    Calendar calendar;
    String idpelanggan,idbarang;
    EditText namaPelanggan, namaBarang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan);

        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        namaPelanggan = findViewById(R.id.ePelanggan);
        namaBarang = findViewById(R.id.eBarang);


        db= new Database_Bengkel_(this);
        v=this.findViewById(android.R.id.content);
        idjual=getIdOrder();
        refresh();
        String tgl   = ModulBengkel.getDate("dd/MM/yyyy") ;
        ModulBengkel.setText(v,R.id.eTanggal,tgl);
        getBarang("");

        final EditText stok = findViewById(R.id.eStok);
        stok.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ModulBengkel.strToInt(stok.getText().toString())>ModulBengkel.strToInt(getStok())){
                    stok.setText(getStok());

                }else if(ModulBengkel.strToInt(stok.getText().toString())<0){
                    stok.setText("0");
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idbarang!=null){
            Cursor c = db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang",idbarang));
            c.moveToNext();
            ModulBengkel.setText(v,R.id.eStok,"1");
            ModulBengkel.setText(v,R.id.eHarga,ModulBengkel.unNumberFormat(ModulBengkel.removeE(ModulBengkel.getString(c,"harga"))));
            ModulBengkel.setText(v,R.id.eBarang,ModulBengkel.getString(c,"barang"));
            hargabeli = ModulBengkel.strToDouble(ModulBengkel.getString(c,"hargabeli"));

        }




        if (idpelanggan!=null){
            Cursor c = db.sq(ModulBengkel.selectwhere("tblpelanggan")+ModulBengkel.sWhere("idpelanggan",idpelanggan));
            c.moveToNext();
            ModulBengkel.setText(v,R.id.ePelanggan,ModulBengkel.getString(c,"pelanggan"));
        }


    }

    public String getStok(){
        Cursor c = db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang",idbarang));
        c.moveToNext();
        String stokya = ModulBengkel.getString(c,"stok");
        return stokya;
    }


    public void getBarang(String cari) {
        setTotal();
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPenjualan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qdetailjual") +ModulBengkel.sWhere("idorder",idjual)+" AND "+ ModulBengkel.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"iddetailorder")+"__"+ModulBengkel.getString(c,"barang") + "__" + ModulBengkel.getString(c,"jumlah")+ "__" + ModulBengkel.getString(c,"hargajual")+"__"+ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"teknisi");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void setTotal(){
        String q = ModulBengkel.selectwhere("tblorder") +ModulBengkel.sWhere("idorder",idjual);;
        Cursor c = db.sq(q) ;
        c.moveToNext();
        ModulBengkel.setText(v,R.id.eTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.getString(c,"total")));
    }

    public  void refresh(){
        idjual= getIdOrder();
        if (idjual.equals("belum")){
            db.exc("INSERT INTO tblorder (idpelanggan,nopol) values (1,'kosong');");
            idjual= getIdOrder();
            refresh();
        }else{
            setText();
            getBarang("");
        }
    }

    public void setText(){
        Cursor c = db.sq(ModulBengkel.selectwhere("qorder")+ModulBengkel.sWhere("idorder",idjual));
        c.moveToNext();
        String faktur="00000000";
        ModulBengkel.setText(v,R.id.eFaktur,faktur.substring(0,faktur.length()-ModulBengkel.getString(c,"idorder").length())+ModulBengkel.getString(c,"idorder")) ;
        ModulBengkel.setText(v,R.id.ePelanggan,ModulBengkel.getString(c,"pelanggan"));
        idpelanggan=ModulBengkel.getString(c,"idpelanggan");

    }


    public String cek(){
        String kondisi="belum";
        Cursor c = db.sq("SELECT * FROM tbldetailorder WHERE idorder="+idjual);
        while(c.moveToNext()){
            kondisi=ModulBengkel.getString(c,"idorder");
        }
        return kondisi;
    }


    public String getIdOrder(){
        idjual="belum";
        Cursor c = db.sq("SELECT * FROM tblorder WHERE statusbayar IS NULL");
        while(c.moveToNext()){
            idjual=ModulBengkel.getString(c,"idorder");
        }
        return idjual;
    }

    public void kalendar(View view) {
        showDialog(1);
    }
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.eTanggal,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
        }
    };

    public void pelanggan(View view) {
        Intent i = new Intent(MenuPenjualan.this,MenuPilih.class);
        i.putExtra("type","pelanggan");
        startActivityForResult(i,100);
    }

    public void barang(View view) {
        Intent i = new Intent(MenuPenjualan.this,MenuPilih.class);
        i.putExtra("type","barang");
        startActivityForResult(i,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==100){
            idpelanggan=data.getStringExtra("idpelanggan");
            namaBarang.setTextColor(Color.parseColor("#000000"));
        }else if(resultCode==200){
            idbarang=data.getStringExtra("idbarang");
        }
    }

    private double tambahlaba(){
        String harga = ModulBengkel.getText(v,R.id.eHarga);
        String stok = ModulBengkel.getText(v,R.id.eStok);
        double hargajual = ModulBengkel.strToDouble(harga) * ModulBengkel.strToDouble(stok);
        hargabeli = hargabeli*ModulBengkel.strToDouble(stok);
        return hargajual-hargabeli;
    }
    private void simpan(){
        String barang = ModulBengkel.getText(v,R.id.eBarang);
        String deskripsi = ModulBengkel.getText(v,R.id.eStok);
        String harga = ModulBengkel.getText(v,R.id.eHarga);
        String faktur=ModulBengkel.getText(v,R.id.eFaktur);
        String tgl = ModulBengkel.getText(v,R.id.eTanggal);
        String laba = ModulBengkel.doubleToStr(tambahlaba());
        Boolean status;
        if (ModulBengkel.strToInt(deskripsi)==0 || ModulBengkel.strToInt(harga)<=0) {
            status=false;
        }else{
            status=true;
        }

        if(!TextUtils.isEmpty(barang)  && !TextUtils.isEmpty(harga) && status && !TextUtils.isEmpty(deskripsi) ){
            String[] pp = {idpelanggan,faktur,tgl,idjual} ;
            String qq = ModulBengkel.splitParam("UPDATE tblorder SET idpelanggan=?,faktur=?,tglorder=? WHERE idorder=?   ",pp) ;
            db.exc(qq);
            String[] p = {idbarang,deskripsi,harga,"1",idjual,laba} ;
            String q = ModulBengkel.splitParam("INSERT INTO tbldetailorder (idbarang,jumlah,hargajual,idteknisi,idorder,laba) values(?,?,?,?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    public void reset(){
        EditText barang,harga,jumlah;
        barang=findViewById(R.id.eBarang);
        harga=findViewById(R.id.eHarga);
        jumlah=findViewById(R.id.eStok);
        barang.getText().clear();
        harga.getText().clear();
        jumlah.getText().clear();
    }

    public void tambahstok(View view) {
        int stok =ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))+1;
        ModulBengkel.setText(v,R.id.eStok,ModulBengkel.intToStr(stok));
    }

    public void kurangsok(View view) {
        int stok =ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))-1;
        ModulBengkel.setText(v,R.id.eStok,ModulBengkel.intToStr(stok));
    }

    public void simpan(View view) {
        simpan();
        reset();
        getBarang("");
    }


    public void ganti(View view) {
        String kondisi=cek();
        if (kondisi.equals("belum")){
            ModulBengkel.showToast(this,"Keranjang Masih Kosong");
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menyimpan pesanan ini dan mengganti pesanan lain ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String qq ="UPDATE tblorder SET statusbayar='belum' WHERE idorder="+idjual;
                            db.exc(qq);
                            refresh();

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Ganti Pesanan");
            alert.show();
        }

    }

    public void bayar(View view) {
        String kondisi=cek();
        if (kondisi.equals("belum")){
            ModulBengkel.showToast(this,"Keranjang Masih Kosong");
        }else{
            finish();
            Intent i = new Intent(MenuPenjualan.this, Pembayaran_Jual_Oederdil_Bengkel_.class);
            i.putExtra("idorder",idjual);
            startActivity(i);
        }
    }

    public void kembali(View view) {
        finish();
    }
}

class MasterPenjualan extends RecyclerView.Adapter<MasterPenjualan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPenjualan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi,tvHarga;
        private ImageView tvOpt;


        public ViewHolder(@NonNull View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
            tvOpt = (ImageView) view.findViewById(R.id.hapus);

            tvHarga=view.findViewById(R.id.tvHarga);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        if (row[4].equals("1")){
            viewHolder.deskripsi.setText("Teknisi :"+row[5]);
            viewHolder.tvHarga.setText(ModulBengkel.removeE(row[3]));
            viewHolder.tvOpt.setTag(row[0]);
        }else{
            viewHolder.deskripsi.setText("Jumlah : "+row[2]);
            double harga = ModulBengkel.strToDouble(row[3])*ModulBengkel.strToDouble(row[2]);
            viewHolder.tvHarga.setText(ModulBengkel.removeE(harga));
            viewHolder.tvOpt.setTag(row[0]);
        }
        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = viewHolder.tvOpt.getTag().toString();
                                Database_Bengkel_ db = new Database_Bengkel_(c);
                                String q = "DELETE FROM tbldetailorder WHERE iddetailorder="+id ;
                                db.exc(q);
                                ((Penjualan)c).getBarang("");

                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Hapus Data");
                alert.show();
            }
        });




    }
}

