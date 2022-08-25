package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MenuKategoriJasaLaundry extends AppCompatActivity {

    Button Simpan;
    EditText edtKategori;

    private final String KEY_NAME = "NAMA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menukategorijasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        edtKategori = (EditText) findViewById(R.id.edtKategori);

        edtKategori.setOnClickListener(view -> {
            try {
                String nama = Simpan.getText().toString();
                if (nama != null && nama !=""){
                    Intent intent = new Intent(MenuKategoriJasaLaundry.this, LaundryItemDaftarKategori.class);
                    intent.putExtra(KEY_NAME, nama);
                    startActivity(intent);
                } else {
                    Toast.makeText(MenuKategoriJasaLaundry.this, "Tolong Diisi Slurr", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception){
                exception.printStackTrace();
                Toast.makeText(MenuKategoriJasaLaundry.this, "Erorr, Coba Lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}