package com.itbrain.aplikasitoko.tokosepatu;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Ubah_Kategori_Sepatu extends AppCompatActivity {
    String type ;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_kategori_sepatu);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        id = getIntent().getStringExtra("id");
        setKategori();

//        if(true){
//            setContentView(R.layout.activity_menu_tambah_kategori);
//            id = getIntent().getStringExtra("id") ;
//            setTitle("Ubah Kategori");
//            setKategori() ;
//        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setKategori() {
        Cursor c = db.sq("SELECT * FROM tblkategori WHERE idkategori="+id) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.eKategori,ModulTokoSepatu.getString(c,"kategori")) ;
        }
    }

    public void tambah(View view){
        tambahkategori();
    }

    private void tambahkategori() {
        if(!TextUtils.isEmpty(ModulTokoSepatu.getText(v,R.id.eKategori))){
            if(db.exc(ModulTokoSepatu.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=?   ",new String[]{ModulTokoSepatu.getText(v,R.id.eKategori),id}))){
                ModulTokoSepatu.showToast(this,"Berhasil");
                finish();
            } else {
                ModulTokoSepatu.showToast(this,"Gagal");
            }
        } else {
            ModulTokoSepatu.showToast(this,"Mohon isi data dengan benar");
        }
    }

}
