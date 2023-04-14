package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Tambah_kategori_Toko_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_kategori_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
    }
    private void tambahkategori() {
        if(!TextUtils.isEmpty(ModulTokoSepatu.getText(v,R.id.eKategori))){
            String [] isikategori = {ModulTokoSepatu.getText(v,R.id.eKategori)};
            if(db.exc(ModulTokoSepatu.splitParam("INSERT INTO tblkategori (kategori) values(?)",isikategori))){
                ModulTokoSepatu.showToast(this,"Data berhasil ditambah");

                finish();

//
//
            } else {
                ModulTokoSepatu.showToast(this,"Gagal masukkan data");
            }
        } else {
            ModulTokoSepatu.showToast(this,"Isi semua data denga benar");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void tambah(View view) {
        tambahkategori();
    }
}