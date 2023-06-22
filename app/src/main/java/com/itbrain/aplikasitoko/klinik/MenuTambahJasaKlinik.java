package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

public class MenuTambahJasaKlinik extends AppCompatActivity {
    DatabaseKlinik db;
    View v;
    ModulKlinik config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_jasa_klinik);
        db= new DatabaseKlinik(this);
        v = this.findViewById(android.R.id.content);
        config = new ModulKlinik(getSharedPreferences("config",this.MODE_PRIVATE));

        tambahlimit();

        ImageButton imageButton = findViewById(R.id.Kembaliiii);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void tambahlimit(){
        boolean status = MainActivity.status;
        if (!status) {
            int batas = ModulKlinik.strToInt(config.getCustom("jasa", "1")) + 1;
            config.setCustom("jasa", ModulKlinik.intToStr(batas));
        }

    }


    private void simpan(){
        String jasa = ModulKlinik.getText(v,R.id.tNama);
        String harga = ModulKlinik.getText(v,R.id.eBiaya);
        String bagihasil= ModulKlinik.getText(v,R.id.eBagiHasil);
        if (!TextUtils.isEmpty(jasa) && !TextUtils.isEmpty(harga) && !TextUtils.isEmpty(bagihasil) && ModulKlinik.strToInt(bagihasil)<101){
            String [] isi = {jasa,harga,bagihasil};
            String q = ModulKlinik.splitParam("INSERT INTO tbljasa (jasa,harga,bagihasil) VALUES (?,?,?)",isi);
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
        Intent intent = new Intent(MenuTambahJasaKlinik.this,Tambah_Jasa_Klinik_.class);
        startActivity(intent);

        simpan();
    }

}
