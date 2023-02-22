package com.itbrain.aplikasitoko.apotek;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

import java.util.Locale;

public class Menu_Bayar_Apotek extends AppCompatActivity {
    String idjual,type;
    DatabaseApotek db;
    View v;
    String idpelanggan;
    ModulApotek config;
    boolean stat = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bayar_apotek);
        ImageButton imageButton = findViewById(R.id.kembali4);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db= new DatabaseApotek(this);
        v=this.findViewById(android.R.id.content);
        idjual= getIntent().getStringExtra("idjual");
        type= getIntent().getStringExtra("type");
        setText();
        config=new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));

        Locale local = new Locale("id","id");
        final EditText pengeluaran = findViewById(R.id.eBayar);
        pengeluaran.addTextChangedListener(new TextWatcherPengeluaran_Apotek(pengeluaran,local,2));

        pengeluaran.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a= ModulApotek.unNumberFormat(pengeluaran.getText().toString());
                if (a.equals("")){

                }else{
                    double kembali = ModulApotek.strToDouble(a)- ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)));
                    ModulApotek.setText(v,R.id.eKembali, ModulApotek.removeE(kembali));
                    if (kembali<0){
                        TextInputLayout tv= findViewById(R.id.textInputLayout20);
                        tv.setHint("Sisa Pembayaran");
                    }else{
                        TextInputLayout tv= findViewById(R.id.textInputLayout20);
                        tv.setHint("Kembali");
                    }

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setText(){

        if (type.equals("jual")){
            Cursor c = db.sq(ModulApotek.selectwhere("tbljual")+ ModulApotek.sWhere("idjual",idjual));
            c.moveToNext();
            idpelanggan = ModulApotek.getString(c,"idpelanggan");
            ModulApotek.setText(v,R.id.eFaktur, ModulApotek.getString(c,"fakturjual"));
            ModulApotek.setText(v,R.id.eTotal, ModulApotek.removeE(ModulApotek.getString(c,"total")));
        }else{
            Cursor c = db.sq(ModulApotek.selectwhere("tblorderbeli")+ ModulApotek.sWhere("idorderbeli",idjual));
            c.moveToNext();
            idpelanggan = ModulApotek.getString(c,"idsupplier");
            ModulApotek.setText(v,R.id.eFaktur, ModulApotek.getString(c,"fakturorderbeli"));
            ModulApotek.setText(v,R.id.eTotal, ModulApotek.removeE(ModulApotek.getString(c,"totalorderbeli")));
        }

    }

    private void transaksi(){
        double saldo;
        Cursor c= db.sq(ModulApotek.select("tbltransaksi"));
        if (c.getCount()>0){
            c.moveToLast();
            saldo= ModulApotek.strToDouble(ModulApotek.getString(c,"saldo"));
        }else{
            saldo=0;
        }

        String tgl="";
        Cursor b=db.sq(ModulApotek.selectwhere("tblorderbeli")+ ModulApotek.sWhere("idorderbeli",idjual));
        while(b.moveToNext()){
            tgl= ModulApotek.getString(b,"tglorderbeli");

        }

        double bayar = ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eBayar)));
        double total = ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)));
        double masuk;
        if (bayar<total){
            saldo=saldo-bayar;
            masuk =bayar;

        }else{
            saldo-=total;
            masuk=total;
        }


        String faktur = ModulApotek.getText(v,R.id.eFaktur);
        String [] isi={tgl,"Pembelian Barang",faktur, ModulApotek.doubleToStr(masuk), ModulApotek.doubleToStr(saldo)};
        String q= ModulApotek.splitParam("INSERT INTO tbltransaksi (tgltransaksi,kettransaksi,fakturtran,keluar,saldo) VALUES (?,?,?,?,?)",isi);
        db.exc(q);
    }

    public void transaks(){
        double saldo;
        Cursor c= db.sq(ModulApotek.select("tbltransaksi"));
        if (c.getCount()>0){
            c.moveToLast();
            saldo= ModulApotek.strToDouble(ModulApotek.getString(c,"saldo"));
        }else{
            saldo=0;
        }

        String tgl="";
        Cursor b=db.sq(ModulApotek.selectwhere("tbljual")+ ModulApotek.sWhere("idjual",idjual));
        while(b.moveToNext()){
            tgl= ModulApotek.getString(b,"tgljual");

        }

        double bayar = ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eBayar)));
        double total = ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)));
        double masuk;
        if (bayar<total){
            saldo=saldo+bayar;
            masuk =bayar;

        }else{
            saldo+=total;
            masuk=total;
        }


        String faktur = ModulApotek.getText(v,R.id.eFaktur);
        String [] isi={tgl,"Penjualan Barang",faktur, ModulApotek.doubleToStr(masuk), ModulApotek.doubleToStr(saldo)};
        String q= ModulApotek.splitParam("INSERT INTO tbltransaksi (tgltransaksi,kettransaksi,fakturtran,masuk,saldo) VALUES (?,?,?,?,?)",isi);
        db.exc(q);
    }

    private void tambahlimit(){
        boolean status = Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status;
        if (!status){
            int batas = ModulApotek.strToInt(config.getCustom("jual","1"))+1;
            config.setCustom("jual", ModulApotek.intToStr(batas));
        }
    }

    private void tambahlimit1(){
        boolean status = Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status;
        if (!status){
            int batas = ModulApotek.strToInt(config.getCustom("beli","1"))+1;
            config.setCustom("beli", ModulApotek.intToStr(batas));
        }
    }


    public void tambahhutang(){
        Cursor c = db.sq(ModulApotek.selectwhere("tblpelanggan")+ ModulApotek.sWhere("idpelanggan",idpelanggan));
        c.moveToNext();
        double hutang = ModulApotek.strToDouble(ModulApotek.getString(c,"hutang"));

        double kembali= ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eKembali)));

        kembali = kembali *-1;
        hutang+=kembali;

        String[] pp = {ModulApotek.doubleToStr(hutang),idpelanggan} ;
        String qq = ModulApotek.splitParam("UPDATE tblpelanggan SET hutang=? WHERE idpelanggan=?   ",pp) ;
        db.exc(qq);

    }

    public void tambahhutang1(){

        Cursor c = db.sq(ModulApotek.selectwhere("tblsupplier")+ ModulApotek.sWhere("idsupplier",idpelanggan));
        c.moveToNext();
        double hutang = ModulApotek.strToDouble(ModulApotek.getString(c,"utang"));

        double kembali= ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eKembali)));

        kembali = kembali *-1;
        hutang+=kembali;


        String[] pp = {ModulApotek.doubleToStr(hutang),idpelanggan} ;
        String qq = ModulApotek.splitParam("UPDATE tblsupplier SET utang=? WHERE idsupplier=?   ",pp) ;
        db.exc(qq);

    }

    private void simpan(){
        String bayar = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eBayar));
        String kembali = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eKembali));

        if (type.equals("jual")){
            if (!TextUtils.isEmpty(bayar) && !bayar.equals("0") && !(ModulApotek.strToDouble(kembali)<0)){
                smpn();

            }else if (!TextUtils.isEmpty(bayar) && !bayar.equals("0") && (ModulApotek.strToDouble(kembali)<0) && !idpelanggan.equals("1")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin simpan transaksi dengan metode piutang ?")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                smpn();
                                tambahhutang();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Simpan Transaksi");
                alert.show();
            }else{
                ModulApotek.showToast(this,"Pelanggan Belum Terdaftar Tidak Bisa Hutang");
            }
        }else{
            if (!TextUtils.isEmpty(bayar) && !bayar.equals("0") && !(ModulApotek.strToDouble(kembali)<0)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin simpan transaksi ?")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                smpn1();

                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Simpan Transaksi");
                alert.show();



            }else if (!TextUtils.isEmpty(bayar) && !bayar.equals("0") && (ModulApotek.strToDouble(kembali)<0) && !idpelanggan.equals("1")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin simpan transaksi dengan metode hutang ?")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                smpn1();
                                tambahhutang1();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Simpan Transaksi");
                alert.show();
            }else{
                ModulApotek.showToast(this,"Supplier Belum Terdaftar Tidak Bisa Hutang");
            }
        }




    }

    public void smpn1(){

        String bayar = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eBayar));
        String kembali = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eKembali));
        if (db.exc("UPDATE tblorderbeli set bayarbeli="+bayar+",kembalibeli="+kembali+", stat=1 WHERE idorderbeli="+idjual)){
            tambahlimit1();
            transaksi();
            Intent intent = new Intent(Menu_Bayar_Apotek.this,Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    public void smpn(){
        tambahlimit();
        String bayar = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eBayar));
        String kembali = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eKembali));
        if (db.exc("UPDATE tbljual set bayar="+bayar+",kembali="+kembali+" WHERE idjual="+idjual)){
            transaks();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda ingin mencetak struk ini")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Menu_Bayar_Apotek.this, MenuCetakapotek2.class);
                            intent.putExtra("idjual",idjual);
                            intent.putExtra("type","transaksi");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Menu_Bayar_Apotek.this,Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Cetak Struk");
            alert.show();
        }
    }

    public void bayar(View view){
        simpan();
    }
}
