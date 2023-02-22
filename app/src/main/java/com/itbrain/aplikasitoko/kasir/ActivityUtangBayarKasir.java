package com.itbrain.aplikasitoko.kasir;

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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityUtangBayarKasir extends AppCompatActivity {

    String faktur,hutang,kembali,bayar ;
    View v ;
    FConfigKasir config;
    DatabaseKasir db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utang_bayar_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE));
        db = new DatabaseKasir(this,config) ;
        faktur = getIntent().getStringExtra("faktur") ;

        ImageButton imageButton = findViewById(R.id.kembali5);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                double masuk = FFunctionKasir.strToDouble(in.getText().toString()) ;
                double jum = FFunctionKasir.strToDouble(hutang) ;
                double back = masuk - jum ;
                kembali = FFunctionKasir.doubleToStr(back) ;
                FFunctionKasir.setText(v,R.id.eKembali, FFunctionKasir.numberFormat(FFunctionKasir.doubleToStr(back))) ;
            }
        });

        setText() ;


    }

    public void setText(){
        Cursor c = db.sq(FQueryKasir.selectwhere("tblbayar")+FQueryKasir.sWhere("fakturbayar",faktur)) ;
        if(FFunctionKasir.getCount(c) > 0){
            c.moveToNext();
            hutang = FFunctionKasir.getString(c,"kembali") ;

            FFunctionKasir.setText(v,R.id.efaktur,faktur);
            FFunctionKasir.setText(v,R.id.eHutang,FFunctionKasir.removeE(hutang)) ;
        } else {
            Intent i = new Intent(this, Menu_Bayar_Hutang_Kasir.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }
    }
    public void bayar(View view){
        double dHutang = FFunctionKasir.strToDouble(hutang) ;
        double dBayar = FFunctionKasir.strToDouble(bayar) ;
        if(dHutang <= dBayar){
            Cursor c = db.sq(FQueryKasir.selectwhere("tblbayar")+FQueryKasir.sWhere("fakturbayar",faktur)) ;
            c.moveToNext() ;
            double jumlahbayar = FFunctionKasir.strToDouble(FFunctionKasir.getString(c,"bayar")) ;
            double bayar = jumlahbayar+dBayar ;
            String tgl = FFunctionKasir.getDate("yyyyMMdd") ;

            String[] p = {  tgl,
                            FFunctionKasir.doubleToStr(bayar),
                            kembali,
                            FFunctionKasir.getText(v,R.id.eKeterangan),
                            faktur} ;
            String q = FQueryKasir.splitParam("UPDATE tblbayar SET tglbayar=?,bayar=?,kembali=?,keterangan=?,flagbayar=1 WHERE fakturbayar=?   ",p) ;
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
                        Intent i = new Intent(ActivityUtangBayarKasir.this, ActivityCetakUtangKasir.class) ;
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                        i.putExtra("fakturbayar",faktur) ;
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityUtangBayarKasir.this, Menu_Bayar_Hutang_Kasir.class) ;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
