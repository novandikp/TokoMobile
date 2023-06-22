package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityLaporanMenuKredit extends AppCompatActivity {
    String deviceid, kondisi;
    SharedPreferences getPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_menu_kredit);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    public void pelanggan(View v) {
        Intent i = new Intent(this, ActivityLaporanMasterKredit.class);
        i.putExtra("type", "pelanggan");
        startActivity(i);
    }

    public void barang(View v) {
        Intent i = new Intent(this, ActivityLaporanBarangKredit.class);
        i.putExtra("type", "barang");
        startActivity(i);
    }

    public void laporanpenjualan(View v) {
        Intent i = new Intent(this, ActivityLaporanPenjualanKredit.class);
        i.putExtra("type", "perpelanggan");
        startActivity(i);
    }

    public void laporanpenjualan2(View v) {
        Intent i = new Intent(this, ActivityLaporanPerJenisKredit.class);
        i.putExtra("type", "perjenis");
        startActivity(i);

    }

    public void laporanpenjualan3(View v) {
        Intent i = new Intent(this, ActivityLaporanPerBarangKredit.class);
        i.putExtra("type", "perbarang");
        startActivity(i);
    }

    public void laporanpenjualan4(View v) {
        Intent i = new Intent(this, ActivityLaporanPerKategoriKredit.class);
        i.putExtra("type", "perkategori");
        startActivity(i);
    }

    public void laporanpendapatan(View v) {
        Intent i = new Intent(this, ActivityLaporanPendapatanKredit.class);
        i.putExtra("type", "pendapatan");
        startActivity(i);
    }

    public void laporanreturn(View v) {
        Intent i = new Intent(this, ActivityLaporanReturnKredit.class);
        i.putExtra("type", "return");
        startActivity(i);
    }

    public void laporanlabarugi(View v) {
        Intent i = new Intent(this, ActivityLaporanLabaRugiKredit.class);
        i.putExtra("type", "labarugi");
        startActivity(i);
    }

    public void laporankredit(View v) {
        Intent i = new Intent(this, ActivityLaporanKredit.class);
        startActivity(i);
    }

    public void laporantagihan(View view) {
        Intent i = new Intent(this, ActivityLaporanTagihanKredit.class);
        startActivity(i);
    }

    public void laporankeuangan(View view) {
        ModuleKredit.goToActivity(this, ActivityLaporanKeuanganKredit.class);
    }
}
