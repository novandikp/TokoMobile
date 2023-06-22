package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class PosTokoKreditMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postokokreditmenumaster);

        ImageView imageView = findViewById(R.id.kembaliKreditMaster);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void identitasKredit(View v) {
        startActivity(new Intent(this, ActivityIdentitasKredit.class));
    }

    public void kategoriKredit(View view) {
        Intent i = new Intent(this, ActivityMasterListKredit.class);
        i.putExtra("type", "kategori");
        startActivity(i);
    }

    public void barangKredit(View view) {
        Intent i = new Intent(this, ActivityMasterListKredit.class);
        i.putExtra("type", "barang");
        startActivity(i);
    }

    public void pelangganKredit(View view) {
        Intent i = new Intent(this, ActivityMasterListKredit.class);
        i.putExtra("type", "pelanggan");
        startActivity(i);
    }
}
