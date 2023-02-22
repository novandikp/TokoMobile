package com.itbrain.aplikasitoko.apotek;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Ubah_Supplier_Apotek extends AppCompatActivity {
    ModulApotek config,temp;
    DatabaseApotek db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_ubah_supplier_apotek);

        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        config = new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseApotek(this) ;
        v = this.findViewById(android.R.id.content);
        id=getIntent().getStringExtra("id");
        setText();
    }

    private void setText(){
        Cursor c = db.sq("SELECT * FROM tblsupplier WHERE idsupplier="+id) ;
        if(c.getCount() > 0) {
            c.moveToNext();
            ModulApotek.setText(v,R.id.nama, ModulApotek.getString(c,"supplier"));
            ModulApotek.setText(v,R.id.epelangggan, ModulApotek.getString(c,"alamatsupplier"));
            ModulApotek.setText(v,R.id.telpsupllier, ModulApotek.getString(c,"nosupplier"));
        }
    }

    private void tambahpelanggan() {
        String nama = ModulApotek.getText(v,R.id.nama) ;
        String alamat = ModulApotek.getText(v,R.id.epelangggan) ;
        String notelp = ModulApotek.getText(v,R.id.telpsupllier) ;


        if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp)){
            String[] p = {ModulApotek.upperCaseFirst(nama), ModulApotek.upperCaseFirst(alamat),notelp,id} ;
            String q = ModulApotek.splitParam("UPDATE tblsupplier SET supplier=?,alamatsupplier=?,nosupplier=? WHERE idsupplier=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void button27(View view) {
        tambahpelanggan();
    }
}