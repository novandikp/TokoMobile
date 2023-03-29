package com.itbrain.aplikasitoko.tokosepatu;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Ubah_Barang_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config, temp;
    DatabaseTokoSepatu db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_barang_sepatu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id=getIntent().getStringExtra("id");
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this);
        v = this.findViewById(android.R.id.content);
        setText();
        setBarang();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBarang(){
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang="+id) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.eBarang,ModulTokoSepatu.getString(c,"barang")) ;
            ModulTokoSepatu.setText(v,R.id.eDeskripsi,ModulTokoSepatu.getString(c,"deskripsi")) ;
            Spinner spinner = (Spinner) findViewById(R.id.sKategori) ;

            int posisi ;
            try{
                posisi =arrayid.indexOf(ModulTokoSepatu.getString(c,"idkategori"));
            }catch (Exception e){
                posisi=0;
            }

            spinner.setSelection(posisi);
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
    }


    private void tambahbarang() {
        String barang = ModulTokoSepatu.getText(v,R.id.eBarang) ;
        String deskripsi = ModulTokoSepatu.getText(v,R.id.eDeskripsi) ;
        String kategori = getKategori() ;


        if(!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(deskripsi)  && !kategori.equals("0")){
            String[] p = {barang,kategori,deskripsi,id} ;
            String q = ModulTokoSepatu.splitParam("UPDATE tblbarang SET barang=?,idkategori=?,deskripsi=? WHERE idbarang=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(this, "Gagal mengubah Barang", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    private String getKategori() {
        Spinner spinner = (Spinner) findViewById(R.id.sKategori) ;
        return arrayid.get(spinner.getSelectedItemPosition()).toString();
    }

    public void tambah(View view) {
        tambahbarang();
    }
}

