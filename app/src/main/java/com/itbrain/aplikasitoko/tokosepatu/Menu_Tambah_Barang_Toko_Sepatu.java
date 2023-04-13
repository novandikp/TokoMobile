package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Tambah_Barang_Toko_Sepatu extends AppCompatActivity {
    String type ;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_barang_sepatu);
        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setText();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void tambahBarang(){
        String barang = ModulTokoSepatu.getText(v,R.id.eBarang);
        String deskripsi = ModulTokoSepatu.getText(v,R.id.eDeskripsi);
        String kategori = getKategori();


        if(!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(deskripsi)  && !kategori.equals("0")){
            String[] p = {barang,kategori,deskripsi} ;
            String q = ModulTokoSepatu.splitParam("INSERT INTO tblbarang (barang,idkategori,deskripsi) values(?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                String freetran = config.getCustom("barang","1");
                int tran =ModulTokoSepatu.strToInt(freetran)+1;
                freetran=ModulTokoSepatu.intToStr(tran);
                config.setCustom("barang",freetran);
                String profile = config.getCustom("profil","");
                if (Splash_Activity_Sepatu.status){
                    finish();
                }else{
                    if(ModulTokoSepatu.strToInt(freetran)>5){
                        Intent i = new Intent(Menu_Tambah_Barang_Toko_Sepatu.this,Aplikasi_Menu_Master_Toko_Sepatu.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                        startActivity(i);
                    }else{
                        finish();
                    }
                }
            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    private void setText() {
        arrayList.add("Pilih Kategori");
        arrayid.add("0");
        Spinner spinner = (Spinner) findViewById(R.id.sKategori) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(ModulTokoSepatu.select("tblkategori")) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arrayList.add(ModulTokoSepatu.getString(c,"kategori"));
                arrayid.add(ModulTokoSepatu.getString(c,"idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
        spinner.setSelection(1);
    }



    private String getKategori(){
        Spinner spinner = (Spinner) findViewById(R.id.sKategori) ;
        return arrayid.get(spinner.getSelectedItemPosition()).toString();
    }


    public void tambah(View view) {
        tambahBarang();
    }
}
