package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityUtangBayarKredit extends AppCompatActivity {

    String faktur,hutang,kembali,bayar ;
    View v ;
    FConfigKredit config;
    FKoneksiKredit db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utang_bayar_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config",this.MODE_PRIVATE));
        db = new FKoneksiKredit(this,config) ;
        faktur = getIntent().getStringExtra("faktur") ;

        final EditText in = (EditText) findViewById(R.id.eBayar) ;
        in.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bayar = in.getText().toString() ;
                double masuk = FFunctionKredit.strToDouble(in.getText().toString()) ;
                double jum = FFunctionKredit.strToDouble(hutang) ;
                double back = masuk - jum ;
                kembali = FFunctionKredit.doubleToStr(back) ;
                FFunctionKredit.setText(v,R.id.eKembali, FFunctionKredit.numberFormat(FFunctionKredit.doubleToStr(back))) ;
            }
        });

        setText() ;
    }

    public void setText(){
        Cursor c = db.sq(FQueryKredit.selectwhere("tblbayar")+FQueryKredit.sWhere("fakturbayar",faktur)) ;
        if(c.getCount() > 0){
            c.moveToNext();
            hutang = FFunctionKredit.getString(c,"kembali") ;

            FFunctionKredit.setText(v,R.id.efaktur,faktur);
            FFunctionKredit.setText(v,R.id.eHutang,FFunctionKredit.removeE(hutang)) ;
        } else {
            Intent i = new Intent(this, ActivityUtangKredit.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }
    }
    public void bayar(View view){
        double dHutang = FFunctionKredit.strToDouble(hutang) ;
        double dBayar = FFunctionKredit.strToDouble(bayar) ;
        if(dHutang <= dBayar){
            Cursor c = db.sq(FQueryKredit.selectwhere("tblbayar")+FQueryKredit.sWhere("fakturbayar",faktur)) ;
            c.moveToNext() ;
            double jumlahbayar = FFunctionKredit.strToDouble(FFunctionKredit.getString(c,"bayar")) ;
            double bayar = jumlahbayar+dBayar ;
            String tgl = FFunctionKredit.getDate("yyyyMMdd") ;

            String[] p = {  tgl,
                            FFunctionKredit.doubleToStr(bayar),
                            kembali,
                            FFunctionKredit.getText(v,R.id.eKeterangan),
                            faktur} ;
            String q = FQueryKredit.splitParam("UPDATE tblbayar SET tglbayar=?,bayar=?,kembali=?,keterangan=?,flagbayar=1 WHERE fakturbayar=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Pembayaran Hutang Berhasil", Toast.LENGTH_SHORT).show();
                open() ;
            }
        } else {
            Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
        }
    }

    public void open(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda ingin untuk cetak Struk ?");
        alertDialogBuilder.setPositiveButton("Cetak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        Intent i = new Intent(ActivityUtangBayarKredit.this, ActivityCetakUtangKredit.class) ;
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                        i.putExtra("fakturbayar",faktur) ;
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityUtangBayarKredit.this, ActivityPenjualanTunaiKredit.class) ;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
