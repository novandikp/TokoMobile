    package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

    public class Form_Penjualan_Kasir_ extends AppCompatActivity {

        String faktur = "";
        FConfigKasir config,temp;
        DatabaseKasir db ;
        View v ;
        int year, month, day ;
        Calendar calendar ;
        String brg, plgn,tanggal,harga,totalbayar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_penjualan_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseKasir(this,config) ;



        plgn = temp.getCustom("idpelanggan","") ;
        if(TextUtils.isEmpty(plgn)){
            temp.setCustom("idpelanggan","1");
        }

        faktur = temp.getCustom("fakturbayar","") ;
        if(TextUtils.isEmpty(faktur)){
            getFaktur() ;
        } else {
            FFunctionKasir.setText(v,R.id.fakturSelesai,faktur) ;
        }

        Button tgl = (Button) findViewById(R.id.cariTanggal) ;
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        Button barang = (Button) findViewById(R.id.cariBarang) ;
        barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Form_Penjualan_Kasir_.this, ActivityPenjualanCariKasir.class) ;
                i.putExtra("type","barang") ;
                startActivity(i);
            }
        });

        Button pelanggan = (Button) findViewById(R.id.cariPelanggan) ;
        pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Form_Penjualan_Kasir_.this, ActivityPenjualanCariKasir.class) ;
                i.putExtra("type","pelanggan") ;
                startActivity(i);
            }
        });

        Button btnPlus = (Button) findViewById(R.id.btnPlus) ;
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = FFunctionKasir.getText(v,R.id.jumlah) ;
                int jum = FFunctionKasir.strToInt(jumlah) ;
                jum++ ;
                FFunctionKasir.setText(v,R.id.jumlah,FFunctionKasir.intToStr(jum)) ;

                hitung() ;
            }
        });


        Button btnMin = (Button) findViewById(R.id.btnMin) ;
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = FFunctionKasir.getText(v,R.id.jumlah) ;
                int jum = FFunctionKasir.strToInt(jumlah) ;
                if (jum > 1){
                    jum-- ;
                    FFunctionKasir.setText(v,R.id.jumlah,FFunctionKasir.intToStr(jum)) ;

                    hitung() ;
                }
            }
        });

        String tot = getTotal() ;
        FFunctionKasir.setText(v,R.id.totalPenjualan,"Rp. "+FFunctionKasir.numberFormat(tot)) ;
        setText() ;
        loadList() ;
        cekfaktur() ;
        hitung() ;

        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

        @Override
        public void onResume(){
            super.onResume();
            loadList();
        }


        @Override
        public void onBackPressed() {
            keluar();
        }


        public void hitung(){
            String jumlahbarang = FFunctionKasir.getText(v,R.id.jumlah) ;
            String harga = FFunctionKasir.getText(v,R.id.harga) ;

            if (!TextUtils.isEmpty(harga)){
                double total = FFunctionKasir.strToDouble(totalbayar) + (FFunctionKasir.strToDouble(jumlahbarang) * FFunctionKasir.strToDouble(harga)) ;
                String hasil = FFunctionKasir.doubleToStr(total) ;
                FFunctionKasir.setText(v,R.id.totalPenjualan,"Rp. " + FFunctionKasir.removeE(hasil)) ;
            }
        }

        public void cekfaktur(){
            Cursor c = db.sq(FQueryKasir.selectwhere("tblpenjualan")+FQueryKasir.sWhere("fakturbayar",faktur)) ;
            if(FFunctionKasir.getCount(c)> 0){
                Button a = (Button) findViewById(R.id.cariPelanggan) ;
                a.setEnabled(false);
                Button b = (Button) findViewById(R.id.cariTanggal) ;
                b.setEnabled(false);
            } else {
                db.exc("DELETE FROM tblbayar WHERE fakturbayar='"+faktur+"'") ;
                Button a = (Button) findViewById(R.id.cariPelanggan) ;
                a.setEnabled(true);
                Button b = (Button) findViewById(R.id.cariTanggal) ;
                b.setEnabled(true);
            }
        }

        public void selesaiPenjualan(View v){
            Intent i = new Intent(this, ActivityBayarKasir.class) ;
            i.putExtra("faktur",faktur) ;
            startActivity(i);
        }

        public String getTotal(){
            Cursor c = db.sq(FQueryKasir.selectwhere("tblbayar")+ FQueryKasir.sWhere("fakturbayar",faktur)) ;
            String total = "" ;
            if(FFunctionKasir.getCount(c) > 0){
                c.moveToNext() ;
                total = removeE(FFunctionKasir.getString(c,"jumlahbayar")) ;
                totalbayar = total ;
                return total ;
            } else {
                total = "0" ;
                totalbayar = "0" ;
                return total ;
            }
        }

        public void setText(){
            FConfigKasir fConfig = new FConfigKasir(getSharedPreferences("temp",this.MODE_PRIVATE)) ;
            brg = fConfig.getCustom("idbarang","") ;
            plgn = fConfig.getCustom("idpelanggan","") ;
            tanggal = fConfig.getCustom("tanggal","") ;

            if(!TextUtils.isEmpty(brg)){
                getBarang() ;
            } else {
                FFunctionKasir.setText(v,R.id.etBarang,"") ;
            }
            if(!TextUtils.isEmpty(plgn)){
                getPelanggan() ;
            } else {
                FFunctionKasir.setText(v,R.id.etPelanggan,"") ;
            }
            if(!TextUtils.isEmpty(tanggal)){
                FFunctionKasir.setText(v,R.id.etTanggall,tanggal) ;
            } else {
                FFunctionKasir.setText(v,R.id.etTanggall,FFunctionKasir.getDate("dd/MM/yyyy")) ;
            }
            FFunctionKasir.setText(v,R.id.faktur,faktur) ;
        }

        private void getFaktur() {
            try {
                String q = FQueryKasir.select("tblbayar") + FQueryKasir.sOrderDESC("fakturbayar");
                Cursor c = db.sq(q) ;
                int count = FFunctionKasir.getCount(c) ;
                if(count < 1){
                    faktur = nol(8)+"1" ;
                } else {
                    c.moveToNext() ;
                    int last = Integer.parseInt(FFunctionKasir.getString(c,"fakturbayar")) ; // 2
                    int fix = last + 1;
                    int leng = String.valueOf(fix).length() ;
                    faktur = nol(9-leng)+ fix ;
                }
                temp.setCustom("fakturbayar",faktur); ;
                FFunctionKasir.setText(v,R.id.faktur,faktur) ;
            }catch (Exception e){
                Toast.makeText(this, "Membuat Faktur Gagal", Toast.LENGTH_SHORT).show();
            }
        }

        public String nol(int total){
            String n = "" ;
            for (int i = 0 ; i<total;i++){
                n+= "0" ;
            }
            return n ;
        }

        public void simpanPenjualan(View view){
            String eTgl = FFunctionKasir.getText(v,R.id.etTanggall) ;
            String eBrg = FFunctionKasir.getText(v,R.id.etBarang) ;
            String ePlgn = FFunctionKasir.getText(v,R.id.etPelanggan) ;
            String eHrg = FFunctionKasir.getText(v,R.id.harga) ;
            String eJmlh = FFunctionKasir.getText(v,R.id.jumlah) ;
            String keterangan = FFunctionKasir.getText(v,R.id.etKeterangan) ;

            if(TextUtils.isEmpty(eBrg) || TextUtils.isEmpty(ePlgn) || TextUtils.isEmpty(eHrg) || TextUtils.isEmpty(eJmlh) || FFunctionKasir.strToDouble(eHrg)==0 || FFunctionKasir.strToDouble(eJmlh)==0){
                Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
            } else {
                Double bay = FFunctionKasir.strToDouble(eHrg) * FFunctionKasir.strToDouble(eJmlh) ;
                String qBayar,qpenj ;
                String[] bayar ; ;
                String[] penj = {faktur,
                        convertDate(eTgl),
                        eHrg,
                        eJmlh,
                        brg,
                        "0",
                        labarugi(eHrg),
                        keterangan} ;
                String q = FQueryKasir.selectwhere("tblbayar")+FQueryKasir.sWhere("fakturbayar",faktur) ;
                Cursor c = db.sq(q);
                if(FFunctionKasir.getCount(c) == 0){
                    bayar = new String[]{faktur, FFunctionKasir.doubleToStr(bay), plgn};
                    qBayar = FQueryKasir.splitParam("INSERT INTO tblbayar (fakturbayar,jumlahbayar,idpelanggan) VALUES (?,?,?)",bayar) ;

                    qpenj = FQueryKasir.splitParam("INSERT INTO tblpenjualan (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi, keterangan) VALUES (?,?,?,?,?,?,?,?)",penj) ;
                } else {
                    double hasil = FFunctionKasir.strToDouble(getTotal())+bay ;
                    qBayar = "UPDATE tblbayar SET jumlahbayar="+FFunctionKasir.quote(FFunctionKasir.doubleToStr(hasil))+" WHERE fakturbayar="+FFunctionKasir.quote(faktur) ;
                    qpenj = FQueryKasir.splitParam("INSERT INTO tblpenjualan (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi,keterangan) VALUES (?,?,?,?,?,?,?,?)",penj) ;
                }

                Cursor c2 = db.sq(FQueryKasir.selectwhere("tblbarang")+FQueryKasir.sWhere("idbarang",brg)) ;
                if(c2.getCount() > 0){
                    c2.moveToNext();
                    String stok = FFunctionKasir.getString(c2,"stok") ;
                    if(FFunctionKasir.strToDouble(stok) >= FFunctionKasir.strToDouble(FFunctionKasir.getText(v,R.id.jumlah))){
                        if(db.exc(qBayar) && db.exc(qpenj)){
                            Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();

                            FConfigKasir fConfig = new FConfigKasir(getSharedPreferences("temp",Form_Penjualan_Kasir_.MODE_PRIVATE)) ;
                            fConfig.setCustom("idbarang","");
                            fConfig.setCustom("tanggal","");
                            FFunctionKasir.setText(v,R.id.harga,"") ;
                            FFunctionKasir.setText(v,R.id.etKeterangan,"") ;
                            FFunctionKasir.setText(v,R.id.jumlah,"1") ;

                            loadList();
                            setText();
                            cekfaktur() ;
                        } else {
                            Toast.makeText(this, "Pembelian Gagal, Silahkan Restart HP Anda.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Stok Tidak Cukup untuk Pemesanan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        public void hapusPenj (View view){
            String[] id = view.getTag().toString().split("__") ;
            open(v,id[0],id[1]) ;
        }

        public void getBarang(){
            String q = FQueryKasir.selectwhere("tblbarang") + FQueryKasir.sWhere("idbarang",brg) ;
            Cursor c = db.sq(q) ;
            c.moveToNext() ;

            harga = removeE(FFunctionKasir.getString(c,"hargajual")) ;
            FFunctionKasir.setText(v,R.id.etBarang,FFunctionKasir.getString(c,"barang")) ;
            FFunctionKasir.setText(v,R.id.harga,harga) ;
        }

        public String removeE(String value){
            double hasil = FFunctionKasir.strToDouble(value) ;
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            return df.format(hasil) ;
        }

        public void getPelanggan(){
            String q = FQueryKasir.selectwhere("tblpelanggan") + FQueryKasir.sWhere("idpelanggan",plgn) ;
            Cursor c = db.sq(q) ;
            c.moveToNext() ;
            FFunctionKasir.setText(v,R.id.etPelanggan,FFunctionKasir.getString(c,"pelanggan")) ;
        }

        public String convertDate(String date){
            String[] a = date.split("/") ;
            return a[2]+a[1]+a[0];
        }

        public String labarugi(String harga){
            String q = FQueryKasir.selectwhere("tblbarang") + FQueryKasir.sWhere("idbarang",brg) ;
            Cursor c = db.sq(q) ;
            c.moveToNext() ;
            double jual = FFunctionKasir.strToDouble(harga) ;
            double beli = FFunctionKasir.strToDouble(FFunctionKasir.getString(c,"hargabeli")) ;
            double hasil = jual - beli ;
//        if(jual >= beli){
//            hasil = jual - beli ;
//        } else if(beli > jual){
//            hasil = beli - jual ;
//        }
            return FFunctionKasir.doubleToStr(hasil) ;
        }

        public void loadList(){
            try{
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvPenjualan) ;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
                ArrayList arrayList = new ArrayList() ;
                RecyclerView.Adapter adapter = new ListPenjualan(this,arrayList) ;
                recyclerView.setAdapter(adapter);

                String q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sWhere("fakturbayar",faktur) ;

                Cursor c = db.sq(q) ;
                if(FFunctionKasir.getCount(c) > 0){
                    while(c.moveToNext()){
                        String campur = FFunctionKasir.getString(c,"idpenjualan")+"__"+FFunctionKasir.getString(c,"fakturbayar")+"__"+
                                FFunctionKasir.getString(c,"barang")+"__"+ removeE(kali(FFunctionKasir.getString(c,"hargajual:1"),FFunctionKasir.getString(c,"jumlahjual")))
                                +"__"+FFunctionKasir.getString(c,"jumlahjual")+"__"+FFunctionKasir.getString(c,"ketpenjualan")+" " ;
                        arrayList.add(campur);
                    }
                } else {

                }
                adapter.notifyDataSetChanged();
                String tot = getTotal() ;
                FFunctionKasir.setText(v,R.id.totalPenjualan,"Rp. "+FFunctionKasir.numberFormat(tot)) ;
            }catch (Exception e){
                Toast.makeText(this, "Aplikasi Error. Silahkan restart HP Anda. atau silahkan hubungi komputerkit.dev@gmail.com", Toast.LENGTH_SHORT).show();
            }
        }

        public String kali(String a,String b){
            try {
                return FFunctionKasir.doubleToStr(FFunctionKasir.strToDouble(a)*FFunctionKasir.strToDouble(b)) ;
            }catch (Exception e){
                return "0" ;
            }
        }

        public void open(View view , final String id, final String min){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
            alertDialogBuilder.setPositiveButton("Hapus",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //yes
                            double hasil = FFunctionKasir.strToDouble(getTotal()) - FFunctionKasir.strToDouble(min) ;
                            if(db.exc("UPDATE tblbayar SET jumlahbayar="+FFunctionKasir.doubleToStr(hasil)+" WHERE fakturbayar="+FFunctionKasir.quote(faktur)) &&
                                    db.exc("DELETE FROM tblpenjualan WHERE idpenjualan="+id)){
                                loadList();
                                cekfaktur();
                            } else {
                                Toast.makeText(Form_Penjualan_Kasir_.this, "Hapus Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void keluar(){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Apakah Anda yakin akan keluar");
            alertDialogBuilder.setPositiveButton("Iya",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent i = new Intent(Form_Penjualan_Kasir_.this, Aplikasi_Kasir_Super_Mudah_Menu_Transaksi.class) ;
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                            startActivity(i);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        // Date Picker
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
                FConfigKasir fConfig = new FConfigKasir(getSharedPreferences("temp",Form_Penjualan_Kasir_.MODE_PRIVATE)) ;
                fConfig.setCustom("tanggal",FFunctionKasir.setDatePickerNormal(thn,bln+1,day));

                FFunctionKasir.setText(v, R.id.etTanggall, FFunctionKasir.setDatePickerNormal(thn,bln+1,day)) ;
            }
        };
        //End Date Picker
    }

    //ADAPTER listpenjualan

    class ListPenjualan extends RecyclerView.Adapter<ListPenjualan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public ListPenjualan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public ListPenjualan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_kasir, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView faktur, nma, harga;
            ConstraintLayout hapus;

            public ViewHolder(View view) {
                super(view);

                faktur = (TextView) view.findViewById(R.id.tvBarang);
                nma = (TextView) view.findViewById(R.id.tvDeskripsi);
                harga = (TextView) view.findViewById(R.id.tvHarga);
                hapus = (ConstraintLayout) view.findViewById(R.id.wHapus);
            }
        }

        @Override
        public void onBindViewHolder(ListPenjualan.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.harga.setText("Rp. " + FFunctionKasir.numberFormat(row[3]) + " \n" + row[5]);
            viewHolder.nma.setText("Jumlah: " + row[4]);
            viewHolder.faktur.setText(row[2]);
            viewHolder.hapus.setTag(row[0] + "__" + row[3]);
        }
    }



