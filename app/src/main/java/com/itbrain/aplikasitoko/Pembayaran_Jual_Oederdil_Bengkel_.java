package com.itbrain.aplikasitoko;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.Database_Bengkel_;
import com.itbrain.aplikasitoko.ModulBengkel;

public class Pembayaran_Jual_Oederdil_Bengkel_ extends AppCompatActivity {
    Database_Bengkel_ db;
    String idorder,idpelanggan;
    View v;
    ModulBengkel config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran_jual_oederdil_bengkel_);
        db= new Database_Bengkel_(this);
        v=this.findViewById(android.R.id.content);
        config= new ModulBengkel(getSharedPreferences("config",MODE_PRIVATE));
        idorder=getIntent().getStringExtra("idorder");
        setText();
        EditText bayar = findViewById(R.id.eByr);
        bayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                double kembali = ModulBengkel.strToDouble(ModulBengkel.getText(v,R.id.eByr))-ModulBengkel.strToDouble(ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eTtl)));
                ModulBengkel.setText(v,R.id.eKmbl,ModulBengkel.removeE(ModulBengkel.doubleToStr(kembali)));
            }
        });
    }

    protected void setText(){
        String q=ModulBengkel.selectwhere("tblorder")+ModulBengkel.sWhere("idorder",idorder);
        Cursor c= db.sq(q);
        c.moveToNext();
        ModulBengkel.setText(v,R.id.eFtr,ModulBengkel.getString(c,"faktur"));
        ModulBengkel.setText(v,R.id.eTtl,ModulBengkel.removeE(ModulBengkel.getString(c,"total")));
        ModulBengkel.setText(v,R.id.eKmbl,ModulBengkel.removeE(ModulBengkel.getString(c,"kembali")));
        ModulBengkel.setText(v,R.id.eByr, "");
        idpelanggan = ModulBengkel.getString(c,"idpelanggan");
    }

    public void print(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("Apakah anda ingin mencetak struk orderan ini ?")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Pembayaran_Jual_Oederdil_Bengkel_.this,MenuCetak.class);
                        i.putExtra("idorder",idorder);
                        i.putExtra("type","bayar");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Pembayaran_Jual_Oederdil_Bengkel_.this,AplikasiBengkel_MenuTransaksi.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.setTitle("Cetak Struk");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Apakah anda ingin membatalkan pembayaran ini ?")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Pembayaran_Jual_Oederdil_Bengkel_.this,AplikasiBengkel_MenuTransaksi.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.setTitle("Pembatalan pembayaran");
        dialog.show();
    }

    private void tambahlimit(){
        boolean status = AplikasiBengkel_MenuTransaksi.status;
        if (!status){
            int batas = ModulBengkel.strToInt(config.getCustom("bayar","1"))+1;
            config.setCustom("bayar",ModulBengkel.intToStr(batas));
        }
    }

    public void tambahhutang(){

        Cursor c = db.sq(ModulBengkel.selectwhere("tblpelanggan")+ModulBengkel.sWhere("idpelanggan",idpelanggan));
        c.moveToNext();
        double hutang = ModulBengkel.strToDouble(ModulBengkel.getString(c,"hutang"));

        double kembali=ModulBengkel.strToDouble(ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eKmbl)));

        kembali = kembali *-1;
        hutang+=kembali;

        String[] pp = {ModulBengkel.doubleToStr(hutang),idpelanggan} ;
        String qq = ModulBengkel.splitParam("UPDATE tblpelanggan SET hutang=? WHERE idpelanggan=?   ",pp) ;
        db.exc(qq);
    }

    public void alert(){
        String bayar = ModulBengkel.getText(v,R.id.eByr);
        String kembali=ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eKmbl));
        if (ModulBengkel.strToInt(bayar)!=0 && ModulBengkel.strToInt(kembali)>=0) {
            byr();
        }else if (ModulBengkel.strToInt(bayar)!=0 && ModulBengkel.strToInt(bayar)>0 && ModulBengkel.strToInt(kembali)<0 && !idpelanggan.equals("1") ){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Simpan Data").setMessage("Apakah anda yakin simpan data pembayaran dengan metode hutang ?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    byr();
                    tambahhutang();
                }
            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }else{
            ModulBengkel.showToast(this,"Pelanggan Tidak Terdaftar tidak bisa melakukan hutang");
        }

    }

    public void bayar(View view){
        alert();
    }

    public void byr(){
        String bayar = ModulBengkel.getText(v,R.id.eByr);
        String kembali=ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eKmbl));

        String[] pp = {bayar,kembali,"lunas",idorder} ;
        String qq = ModulBengkel.splitParam("UPDATE tblorder SET bayar=?,kembali=?,statusbayar=? WHERE idorder=?   ",pp) ;
        db.exc(qq);
        tambahlimit();
        print();
    }
}
