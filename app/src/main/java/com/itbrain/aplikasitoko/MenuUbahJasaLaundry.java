package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MenuUbahJasaLaundry extends AppCompatActivity {
    EditText NamaJasa,BiayaJasa;
    Button Simpan;
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuubahjasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        NamaJasa = (TextInputEditText) findViewById(R.id.NamaJasa);
        BiayaJasa = findViewById(R.id.BiayaJasa);
        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }

    public void add(){
        String namajasa = NamaJasa.getText().toString();
        String biayajasa = BiayaJasa.getText().toString();
        if(TextUtils.isEmpty(namajasa)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(biayajasa)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into tbljasa (jasa,biaya) values ('"+ namajasa +"','"+ biayajasa +"')");
            finish();
        }
    }
}