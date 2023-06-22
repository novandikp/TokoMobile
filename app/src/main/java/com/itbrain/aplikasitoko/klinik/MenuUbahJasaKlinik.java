package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class MenuUbahJasaKlinik extends AppCompatActivity {
    DatabaseKlinik db;
    View v;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ubah_jasa_klinik);
        db= new DatabaseKlinik(this);
        v = this.findViewById(android.R.id.content);
        id = getIntent().getStringExtra("id");

        ImageButton imageButton = findViewById(R.id.Kembaliiii);

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
        Cursor c = db.sq(ModulKlinik.selectwhere("tbljasa")+ModulKlinik.sWhere("idjasa",id));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.tNama,ModulKlinik.getString(c,"jasa"));
        ModulKlinik.setText(v,R.id.eBiaya,ModulKlinik.unNumberFormat(ModulKlinik.removeE(ModulKlinik.getString(c,"harga"))));
        ModulKlinik.setText(v,R.id.eBagiHasil,ModulKlinik.getString(c,"bagihasil"));
    }

    private void simpan(){
        String jasa = ModulKlinik.getText(v,R.id.tNama);
        String harga = ModulKlinik.getText(v,R.id.eBiaya);
        String bagihasil= ModulKlinik.getText(v,R.id.eBagiHasil);
        if (!TextUtils.isEmpty(jasa) && !TextUtils.isEmpty(harga) && !TextUtils.isEmpty(bagihasil) && ModulKlinik.strToInt(bagihasil)<101){
            String [] isi = {jasa,harga,bagihasil,id};
            String q = ModulKlinik.splitParam("UPDATE tbljasa SET jasa=?,harga=?,bagihasil=? WHERE idjasa=?   ",isi);
            if (db.exc(q)){
                ModulKlinik.showToast(this,"Berhasil menyimpan data");
                finish();
            }else{
                ModulKlinik.showToast(this,"Gagal menyimpan data");
            }
        }else{
            ModulKlinik.showToast(this,"Mohon isi data dengan lengkap dan benar");
        }
    }

    public void simpan(View view) {
        simpan();
    }
}
