package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MenuLaporanMobilBagRental extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanmobilbagrental);
    }

    public void ExportExcel(View view) {
        Intent intent = new Intent(MenuLaporanMobilBagRental.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}