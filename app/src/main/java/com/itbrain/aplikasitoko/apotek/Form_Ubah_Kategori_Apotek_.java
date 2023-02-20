package com.itbrain.aplikasitoko.apotek;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class Form_Ubah_Kategori_Apotek_ extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_ubah_kategori_apotek_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        v = this.findViewById(android.R.id.content);
        db = new DatabaseApotek(this);
        id = getIntent().getStringExtra("id");
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
        Cursor c = db.sq("SELECT * FROM tblkategori WHERE idkategori="+id) ;
        if(c.getCount() > 0) {
            c.moveToNext();
            ModulApotek.setText(v, R.id.eKategori, ModulApotek.getString(c, "kategori"));
        }
    }


    private void tambahKategori(){
        String kategori= ModulApotek.getText(v,R.id.eKategori);
        if (!TextUtils.isEmpty(kategori)){
            String [] isi={kategori,id};
            db.exc(ModulApotek.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=?   ",new String[]{ModulApotek.getText(v,R.id.eKategori),id}));
            finish();
        }else{
            ModulApotek.showToast(this,"Mohon Masukkan data dengan benar");
        }

    }

    public void tambah(View view){
        tambahKategori();
    }
}
