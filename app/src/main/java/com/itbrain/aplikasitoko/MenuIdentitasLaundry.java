package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuIdentitasLaundry extends AppCompatActivity {

    View v;
    DatabaseLaundry db;
    EditText nama,alamat,telp,Caption1,Caption2,Caption3;
    Button btnclear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menuidentitaslaundry);
        db=new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);

        Cursor c = db.sq(Query.select("tblidentitas")) ;
        if(Modul.getCount(c)==1){
            setText() ;
        }
        btnclear = findViewById(R.id.btnclear);
        nama = findViewById(R.id.Nama);
        alamat = findViewById(R.id.Alamat);
        telp = findViewById(R.id.Telp);
        Caption1 = findViewById(R.id.Caption1);
        Caption2 = findViewById(R.id.Caption2);
        Caption3 = findViewById(R.id.Caption3);

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.exc("delete from tblidentitas where ididentitas =0");
            }
        });
    }

    public void Keluar(View view) {
        Intent intent = new Intent(MenuIdentitasLaundry.this, LaundryMenuMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void setText(){
        Cursor c = db.sq(Query.selectwhere("tblidentitas")+Query.sWhere("ididentitas","1")) ;
        c.moveToNext();
        Modul.setText(v,R.id.Nama,Modul.getString(c,"namatoko")) ;
        Modul.setText(v,R.id.Alamat,Modul.getString(c,"alamattoko")) ;
        Modul.setText(v,R.id.Telp,Modul.getString(c,"notelptoko")) ;
        Modul.setText(v,R.id.Caption1,Modul.getString(c,"caption_1")) ;
        Modul.setText(v,R.id.Caption2,Modul.getString(c,"caption_2")) ;
        Modul.setText(v,R.id.Caption3,Modul.getString(c,"caption_3")) ;

    }

    public void simpanIdentitas(View view) {
        if(Modul.getText(v,R.id.Nama).isEmpty()||
                Modul.getText(v,R.id.Alamat).isEmpty()||
                Modul.getText(v,R.id.Telp).isEmpty()||
                Modul.getText(v,R.id.Caption1).isEmpty()||
                Modul.getText(v,R.id.Caption2).isEmpty()||
                Modul.getText(v,R.id.Caption3).isEmpty()){
            Toast.makeText(this, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();
        }else {
            Cursor c = db.sq(Query.select("tblidentitas"));
            String Nama = nama.getText().toString();
            String Alamat = alamat.getText().toString();
            String Telp = telp.getText().toString();
            String Caption_1 = Caption1.getText().toString();
            String Caption_2 = Caption2.getText().toString();
            String Caption_3 = Caption3.getText().toString();
            String[] p = {"1", Modul.getText(v, R.id.Nama),
                    Modul.getText(v, R.id.Alamat),
                    Modul.getText(v, R.id.Telp),
                    Modul.getText(v, R.id.Caption1),
                    Modul.getText(v, R.id.Caption2),
                    Modul.getText(v, R.id.Caption3)
            };
            String[] p1 = {Modul.getText(v, R.id.Nama),
                    Modul.getText(v, R.id.Alamat),
                    Modul.getText(v, R.id.Telp),
                    Modul.getText(v, R.id.Caption1),
                    Modul.getText(v, R.id.Caption2),
                    Modul.getText(v, R.id.Caption3),
                    "1"
            };

            String q = "";
            if (Modul.getCount(c) == 1) {
                String ididentitas = String.valueOf(getIntent().getIntExtra("ididentitas",1));
                q = Query.splitParam("UPDATE tblidentitas SET namatoko='"+ Nama +"',alamattoko='"+ Alamat +"',notelptoko='"+ Telp +"',caption_1='"+ Caption_1 +"',caption_2='"+ Caption_2 +"',caption_3='"+ Caption_3 +"' where ididentitas = '"+ ididentitas +"'",p1);
//                q = Query.splitParam("UPDATE tblidentitas SET namatoko='"+ Nama +"',alamattoko='"+ Alamat +"' where ididentitas = '1'",p1);
            } else {
                q = Query.splitParam("INSERT INTO tblidentitas (namatoko,alamattoko,notelptoko,caption_1,caption_2,caption_3) values ('"+ Nama +"','"+ Alamat +"','"+ Telp +"','"+ Caption_1 +"','"+ Caption_2 +"','"+ Caption_3 +"')" ,p);
            }
//            Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}