package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Form_Tambah_kategori_Apotek_ extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_kategori_apotek_);
        v=this.findViewById(android.R.id.content);
        db=new DatabaseApotek(this);


            ImageButton imageButton = findViewById(R.id.Kembali);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }

            });
        }

    private void tambahKategori(){
        String kategori= ModulApotek.getText(v,R.id.eKategori);
        if (!TextUtils.isEmpty(kategori)){
            String [] isi={kategori};
            db.exc(ModulApotek.splitParam("INSERT INTO tblkategori (kategori) VALUES (?)",isi));
            finish();
        }else{
            ModulApotek.showToast(this,"Mohon Masukkan data dengan benar");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void tambah(View view){
        tambahKategori();

    }
}

