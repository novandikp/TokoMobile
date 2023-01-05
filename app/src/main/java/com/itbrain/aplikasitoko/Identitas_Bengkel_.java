package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.Barang;

public class Identitas_Bengkel_ extends AppCompatActivity {

    EditText etNama, etAlamat, etNoTelp, etCaptionOne, etCaptionTwo, etCaptionTree;
    Database_Bengkel_ db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identitas_bengkel_);

        etNama = findViewById(R.id.etNamaBengkel);
        etAlamat = findViewById(R.id.etAlamatBengkel);
        etNoTelp = findViewById(R.id.etNomorTelp);
        etCaptionOne = findViewById(R.id.tvCaptionOne);
        etCaptionTwo = findViewById(R.id.tvCaptionTwo);
        etCaptionTree = findViewById(R.id.tvCaptionTree);


        load();


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void selectData() {
        String sql = "SELECT * FROM tbltoko WHERE idtoko = 1";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                etNama.setText(cursor.getString(cursor.getColumnIndex("namatoko")));
                etAlamat.setText(cursor.getString(cursor.getColumnIndex("alamattoko")));
                etNoTelp.setText(cursor.getString(cursor.getColumnIndex("notoko")));
                etCaptionOne.setText(cursor.getString(cursor.getColumnIndex("caption1")));
                etCaptionTwo.setText(cursor.getString(cursor.getColumnIndex("caption2")));
                etCaptionTree.setText(cursor.getString(cursor.getColumnIndex("caption3")));
            }
        }
    }

    public void load() {
        db = new Database_Bengkel_(this);

        selectData();
    }

    public void hapus(View view) {
        etNama.setText("");
        etAlamat.setText("");
        etNoTelp.setText("");
        etCaptionOne.setText("");
        etCaptionTwo.setText("");
        etCaptionTree.setText("");
    }

    public void simpan(View view) {
        String namabengkel = etNama.getText().toString();
        String alamatbengkel = etAlamat.getText().toString();
        String nobengkel = etNoTelp.getText().toString();
        String captionone = etCaptionOne.getText().toString();
        String captiontwo = etCaptionTwo.getText().toString();
        String captiontree = etCaptionTree.getText().toString();



        if (etNama.getText().toString().isEmpty() || etAlamat.getText().toString().isEmpty() || etNoTelp.getText().toString().isEmpty() || etCaptionOne.getText().toString().isEmpty() || etCaptionTwo.getText().toString().isEmpty() || etCaptionTree.getText().toString().isEmpty()) {
            pesan("Data Wajib Di Isi!");
        } else {
            String sql = "UPDATE tbltoko SET namatoko = '"+namabengkel+"', alamattoko = '"+alamatbengkel+"', notoko = "+nobengkel+", caption1 = '"+captionone+"', caption2 = '"+captiontwo+"', caption3 = '"+captiontree+"' WHERE idtoko = 1";
            db.exc(sql);
            pesan("Simpan Identitas");
            finish();
        }
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }
}