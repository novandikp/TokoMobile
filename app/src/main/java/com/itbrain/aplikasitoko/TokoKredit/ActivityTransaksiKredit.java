package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityTransaksiKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_kredit);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void penjualan(View v) {
        startActivity(new Intent(this, ActivityPenjualanTunaiKredit.class));
    }
    public void returnpenj(View v) {
        startActivity(new Intent(this, ActivityReturnKredit.class));
    }

    public void penjualanKredit(View view) {
        startActivity(new Intent(this, ActivityPenjualanKredit.class));
    }

    public void bayarAngsuran(View view) {
        startActivity(new Intent(this, ActivityBayarTagihanKredit.class));
    }

    public void pemasukan(View view) {
        Intent i = new Intent(this, ActivityPemasukanKredit.class);
        i.putExtra("type", "pemasukan");
        startActivity(i);
    }

    public void pengeluaran(View view) {
        Intent i = new Intent(this, ActivityPengeluaranKredit.class);
        i.putExtra("type", "pengeluaran");
        startActivity(i);
    }
}
