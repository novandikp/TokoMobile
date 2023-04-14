package com.itbrain.aplikasitoko.CetakKwitansi;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class UpdateJasa_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    TextInputEditText edtJasa;
    String jasa;
    Integer idjasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatejasa_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);
        edtJasa = (TextInputEditText) findViewById(R.id.edtJasa);

        Bundle extra = getIntent().getExtras();
        idjasa = extra.getInt("idjasa");
        edtJasa.setText(extra.getString("jasa"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnSimpan(View view) {
        jasa = edtJasa.getText().toString();
        if (jasa.equals("")){
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            if (db.updateJasa(idjasa, jasa)){
                Toast.makeText(this, "Berhasil Update", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

