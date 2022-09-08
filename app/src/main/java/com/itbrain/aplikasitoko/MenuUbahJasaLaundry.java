package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.Model.Kategori;

import java.util.ArrayList;

public class MenuUbahJasaLaundry extends AppCompatActivity {

    Button Simpan;
    EditText edtJasa,edtBiaya;
    DatabaseLaundry db;
    ArrayList<String> listkategori,listidkategori;
    Spinner SpinnerKategori,SpinnerSatuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuubahjasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        edtJasa = (EditText) findViewById(R.id.edtJasa);
        edtBiaya = (EditText) findViewById(R.id.edtBiaya);
        db = new DatabaseLaundry(this);
        getKategori();
        SpinnerKategori = findViewById(R.id.spinnerkategori);
        SpinnerSatuan = findViewById(R.id.spinnersatuan);
        listidkategori = new ArrayList<>();
        listkategori = new ArrayList<>();
        if(isUpdate()){
            edtJasa.setText(getIntent().getStringExtra("jasa"));
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
        }
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdate()){

                }else{
                    add();
                }
            }
        });
    }

    public void getKategori() {
        SpinnerKategori = findViewById(R.id.spinnerkategori);
        listidkategori = new ArrayList<>();
        listkategori = new ArrayList<>();
        Cursor cursor=db.sq("select * from tblkategori");
        if(cursor !=null ){
            listkategori.clear();
            listidkategori.clear();
            listkategori.add("Semua Kategori");
            listidkategori.add("-1");
            while(cursor.moveToNext()){
                listkategori.add(cursor.getString(cursor.getColumnIndex("kategori")));
                listidkategori.add(cursor.getString(cursor.getColumnIndex("idkategori")));
            }
        }
        ArrayAdapter adapterspinner=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listkategori);
        SpinnerKategori.setAdapter(adapterspinner);
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idjasa",-1) > -1;
    }
    public void add(){
        String jasa = edtJasa.getText().toString();
        String biaya = edtBiaya.getText().toString();
        if(TextUtils.isEmpty(jasa)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else{
            db.exc("insert into tbljasa (jasa,satuan) values ('"+ jasa +"','"+ biaya +"')");
            finish();
        }
    }
}