package com.itbrain.aplikasitoko.kasir;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityReturnProsesKasir extends AppCompatActivity {
    String idpenjualan,idbarang,faktur,jumlah ;
    View v ;
    FConfigKasir config ;
    DatabaseKasir db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_proses_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE));
        db = new DatabaseKasir(this,config) ;

        idpenjualan = getIntent().getStringExtra("idpenjualan") ;

        ImageButton imageButton = findViewById(R.id.kembali7);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setText() ;
    }

    public void setText(){
        Cursor c = db.sq(FQueryKasir.selectwhere("qpenjualan")+FQueryKasir.sWhere("idpenjualan",idpenjualan)) ;
        c.moveToNext() ;
        idbarang = FFunctionKasir.getString(c,"idbarang:1") ;
        String barang = FFunctionKasir.getString(c,"barang") ;
        jumlah = FFunctionKasir.getString(c,"jumlahjual") ;
        faktur = FFunctionKasir.getString(c,"fakturbayar") ;

        FFunctionKasir.setText(v,R.id.eJumBarang,jumlah) ;
        FFunctionKasir.setText(v,R.id.efaktur,faktur) ;
        FFunctionKasir.setText(v,R.id.eBarang,barang) ;
    }

    public void returnProses(View view){
        String tgl = FFunctionKasir.getDate("yyyyMMdd") ;
        String jum = FFunctionKasir.getText(v,R.id.eJumReturn) ;
        double hasil =  FFunctionKasir.strToDouble(jumlah) - FFunctionKasir.strToDouble(jum);
        if(FFunctionKasir.strToDouble(jum) <= FFunctionKasir.strToDouble(jumlah)){
            if(!TextUtils.isEmpty(jum)){
                String[] p = {idbarang,
                        faktur,
                        tgl,
                        jum} ;
                String q = FQueryKasir.splitParam("INSERT INTO tblreturn (idbarang,fakturbayar,tglreturn,jumlah)VALUES (?,?,?,?)",p) ;
                String q1 = "UPDATE tblpenjualan SET jumlahjual="+FFunctionKasir.quote(FFunctionKasir.doubleToStr(hasil))+" WHERE idpenjualan="+idpenjualan ;
                if(db.exc(q) && db.exc(q1)){
                    Toast.makeText(this, "Return Berhasil", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, Menu_Return_Kasir_.class) ;
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Return Gagal", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mohon isi dengan benar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Jumlah Barang melebihi pemesanan", Toast.LENGTH_SHORT).show();
        }

    }
}
