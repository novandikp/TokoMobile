package com.itbrain.aplikasitoko.tokosepatu;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Ubah_Pelanggan_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config, temp;
    DatabaseTokoSepatu db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_pelanggan_sepatu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Pelanggan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id=getIntent().getStringExtra("id");
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this);
        v = this.findViewById(android.R.id.content);

        setPelanggan();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPelanggan(){
        Cursor c = db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+id) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.ePelanggan,ModulTokoSepatu.getString(c,"pelanggan")) ;
            ModulTokoSepatu.setText(v,R.id.eAlamat,ModulTokoSepatu.getString(c,"alamat")) ;
            ModulTokoSepatu.setText(v,R.id.eNoTelp,ModulTokoSepatu.getString(c,"notelp")) ;
        }
    }

    private void tambahPelanggan(){
        String nama = ModulTokoSepatu.getText(v,R.id.ePelanggan) ;
        String alamat = ModulTokoSepatu.getText(v,R.id.eAlamat) ;
        String notelp = ModulTokoSepatu.getText(v,R.id.eNoTelp) ;


        if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp)){
            String[] p = {nama,alamat,notelp,id} ;
            String q =ModulTokoSepatu.splitParam("UPDATE tblpelanggan SET pelanggan=?,alamat=?,notelp=? WHERE idpelanggan=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil mengubah ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal mengubah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void simpan(View view) {
        tambahPelanggan();
    }
}