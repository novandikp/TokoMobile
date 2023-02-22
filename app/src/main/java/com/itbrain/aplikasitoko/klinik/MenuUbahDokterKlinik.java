package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class MenuUbahDokterKlinik extends AppCompatActivity {
    DatabaseKlinik db;
    View v;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DatabaseKlinik(this);
        v= this.findViewById(android.R.id.content);
        setContentView(R.layout.activity_menu_tambah_dokter_klinik);
        id = getIntent().getStringExtra("id");

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void setText(){
        Cursor c = db.sq(ModulKlinik.selectwhere("tbldokter")+ModulKlinik.sWhere("iddokter",id));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.eNama,ModulKlinik.getString(c,"dokter"));
        ModulKlinik.setText(v,R.id.eAlamat,ModulKlinik.getString(c,"alamatdokter"));
        ModulKlinik.setText(v,R.id.eNo,ModulKlinik.getString(c,"nodokter"));
    }

    private void simpan(){
        String nama = ModulKlinik.getText(v,R.id.eNama);
        String alamat = ModulKlinik.getText(v,R.id.eAlamat);
        String no = ModulKlinik.getText(v,R.id.eNo);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(no)){
            String [] isi = {nama,alamat,no,id};
            String q = ModulKlinik.splitParam("UPDATE tbldokter SET dokter=?,alamatdokter=?,nodokter=? WHERE iddokter=?   ",isi);
            if (db.exc(q)){
                ModulKlinik.showToast(this,"Berhasi menyimpan data");
                finish();
            }else{
                ModulKlinik.showToast(this,"Gagal Menyimpan data");
            }
        }else{
            ModulKlinik.showToast(this,"Mohon isi data dengan lengkap dan benar");
        }

    }

    public void simpan(View view) {
        simpan();
    }


}
