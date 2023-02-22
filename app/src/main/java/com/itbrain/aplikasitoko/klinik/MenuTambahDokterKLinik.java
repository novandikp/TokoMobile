package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

public class MenuTambahDokterKLinik extends AppCompatActivity {
    DatabaseKlinik db;
    View v;
    ModulKlinik config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DatabaseKlinik(this);
        v= this.findViewById(android.R.id.content);
        setContentView(R.layout.activity_menu_tambah_dokter_klinik);
        config = new ModulKlinik(getSharedPreferences("config",this.MODE_PRIVATE));

        tambahlimit();

        ImageButton imageButton = findViewById(R.id.Kembali);

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
        String nama = ModulKlinik.getText(v,R.id.eNama);
        String alamat = ModulKlinik.getText(v,R.id.eAlamat);
        String no = ModulKlinik.getText(v,R.id.eNo);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(no)){
            String [] isi = {nama,alamat,no};
            String q = ModulKlinik.splitParam("INSERT INTO tbldokter (dokter,alamatdokter,nodokter) VALUES (?,?,?)",isi);
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
        Intent intent = new Intent(MenuTambahDokterKLinik.this,Tambah_Dokter_Klinik_.class);
        startActivity(intent);

        simpan();
    }
}
