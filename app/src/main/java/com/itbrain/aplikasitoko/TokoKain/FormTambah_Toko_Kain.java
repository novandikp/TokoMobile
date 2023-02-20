package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;

import java.util.List;
import java.util.Locale;

public class FormTambah_Toko_Kain extends AppCompatActivity {
    Spinner spKat;
    Button btnSimpan;
    TextInputEditText edtNama,edtBiaya;
    String Nama,Biaya;
    Integer idKat,idKain;
    List<String> getIdKat;
    DatabaseTokoKain db;
    View v;
    int spKatSelection=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_kain);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {

        } else {

        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton imageButton = findViewById(R.id.Kembaligg);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            db = new DatabaseTokoKain(this);

            btnSimpan = (Button) findViewById(R.id.btnTambahh);
            edtNama = (TextInputEditText) findViewById(R.id.edtNamaJasa);
            edtBiaya = (TextInputEditText) findViewById(R.id.edtBiaya);
            edtBiaya.addTextChangedListener(new NumberTextWatcher(edtBiaya, new Locale("in", "ID"), 2));
            spKat = (Spinner) findViewById(R.id.spKategorii);
            v = this.findViewById(android.R.id.content);

            getIdKat = db.getIdKategori();
            getIdKat.remove(0);


            spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String label = parent.getItemAtPosition(position).toString();
                    idKat = KumFunTokoKain.strToInt(getIdKat.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (extra == null) {
                //Insert
                idKain = null;
            } else {
                idKain = extra.getInt("idkain");
                edtNama.setText(extra.getString("kain"));
                edtBiaya.setText(extra.getString("biaya"));
                String qcount = "SELECT idkategori FROM tblkategori WHERE idkategori<" + String.valueOf(extra.getInt("idkategori")) + " ORDER BY idkategori ASC";
                Cursor cKat = db.sq(qcount);
                spKatSelection = cKat.getCount();
                spKat.setSelection(cKat.getCount());
                btnSimpan.setText("Ubah");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getIdKat = db.getIdKategori();
        getIdKat.remove(0);


        spKat = (Spinner) findViewById(R.id.spKategorii);
        getKategoriData();
        spKat.setSelection(spKatSelection);
        spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                idKat = KumFunTokoKain.strToInt(getIdKat.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void getKategoriData(){
        DatabaseTokoKain db = new DatabaseTokoKain(this);
        List<String> labels = db.getKategori();
        labels.remove(0);

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKat.setAdapter(data);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void tambahkategori(View view) {
        startActivity(new Intent(this, Form_Tambah_Kategori_Toko_Kain.class));
    }
        public void tambahkain(View view) {
            Nama = edtNama.getText().toString();
            Biaya = KumFunTokoKain.unNumberFormat(edtBiaya.getText().toString());

        if (TextUtils.isEmpty(Nama) || TextUtils.isEmpty(Biaya) ){
            Toast.makeText(FormTambah_Toko_Kain.this, "Isi data terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }else {
            if (idKat==(null)){
                Toast.makeText(FormTambah_Toko_Kain.this, "Pilih kategori terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }else {
                if (idKain==null){
                    if (db.insertToKain(idKat,Nama,Biaya)){
                        Toast.makeText(FormTambah_Toko_Kain.this, "Tambah data berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(FormTambah_Toko_Kain.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (db.updateKain(idKain,idKat,Nama,Biaya)){
                        Toast.makeText(FormTambah_Toko_Kain.this, "Tambah data berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(FormTambah_Toko_Kain.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
}


