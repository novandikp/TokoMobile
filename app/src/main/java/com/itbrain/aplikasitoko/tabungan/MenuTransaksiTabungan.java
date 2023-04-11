package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class MenuTransaksiTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_tabungan);
        ModulTabungan.btnBack(R.string.title_transaksi,getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliTransaksi);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    public void buatSimpanan(View view) {
        startActivity(new Intent(this,MenuTransaksiSimpananTabungan.class));
    }

    public void prosesSimpan(View view) {
        startActivity(new Intent(this, MenuTransaksiProsesSimpanTabungan.class).putExtra("type","simpan"));
    }

    public void prosesAmbil(View view) {
        startActivity(new Intent(this,MenuTransaksiProsesAmbilTabungan.class).putExtra("type","ambil"));
    }

    public void menuKeuangan(View view) {
        startActivity(new Intent(this,MenuTransaksiKeuanganTabungan.class));
    }
}
