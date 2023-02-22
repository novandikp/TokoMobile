package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class MenuTransaksi_Mobil extends AppCompatActivity {

    ModulRentalMobil config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menutransaksi_mobil);
        config = new ModulRentalMobil(getSharedPreferences("config",MODE_PRIVATE));

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MenuTransaksi_Mobil.this, MenuUtama_Mobil.class);
               startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Imageview() {
        Intent i = new Intent(this, MenuUtama_Mobil.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void rental(View view) {
       {
            if (limit("rental")) {
                Intent i = new Intent(MenuTransaksi_Mobil.this, MenuRental_Mobil.class);
                i.putExtra("type", "transaksi");
                startActivity(i);
            } else {
                Toast.makeText(this, "Limit telah terlampui", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MenuUtilitas_Mobil.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }
    }

    public void kembali(View view) {
       {
            if (limit("kembali")){
                Intent i = new Intent(this, MenuPengembalianKendaraan_Mobil.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "Limit telah terlampui", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MenuUtilitas_Mobil.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }

    }

    private boolean limit(String item) {
        int batas =ModulRentalMobil.strToInt(config.getCustom(item,"1"));
        if (batas>10){
            return false;
        }else{
            return true;
        }
    }

}
