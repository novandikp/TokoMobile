package com.itbrain.aplikasitoko;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

public class MenuUbahJasaLaundry extends AppCompatActivity {

    Spinner spKat,spSat;
    Button btnSimpan;
    TextInputEditText edtNama;
    EditText edtBiaya;
    String Nama,Satuan,Biaya;
    Integer idKat,idJasa;
    List<String> getIdKat;
    DatabaseLaundry db;
    int spKatSelection=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menuubahjasalaundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);

        btnSimpan = (Button) findViewById(R.id.Simpan);
        edtNama = (TextInputEditText) findViewById(R.id.edtJasa);
        edtBiaya = findViewById(R.id.edtBiaya);
        edtBiaya.addTextChangedListener(new NumberTextWatcher(edtBiaya,new Locale("in","ID"),2));
        spKat = (Spinner) findViewById(R.id.spinnerkategori);
        spSat = (Spinner) findViewById(R.id.spinnersatuan);

        getIdKat = db.getIdKategori();
        getIdKat.remove(0);
        getKategoriData();
//        spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String label = parent.getItemAtPosition(position).toString();
//                idKat = Modul.strToInt(getIdKat.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        spSat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String label = parent.getItemAtPosition(position).toString();
//                if (label.equals("Piece (Pc)")){
//                    Satuan = "pc";
//                }else if (label.equals("Kilo Gram(Kg)")){
//                    Satuan = "kg";
//                }else if (label.equals("Meter Persegi(M²)")){
//                    Satuan = "m2";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Bundle extra = getIntent().getExtras();
        if (extra==null){
            //Insert
            idJasa=null;
        }else {
            idJasa = extra.getInt("idjasa");
            edtNama.setText(extra.getString("jasa"));
            edtBiaya.setText(extra.getString("biaya"));
            if (extra.getString("satuan").equals("pc")){
                spSat.setSelection(0);
            }else if (extra.getString("satuan").equals("kg")){
                spSat.setSelection(1);
            }else if (extra.getString("satuan").equals("m2")){
                spSat.setSelection(2);
            }
            String qcount = "SELECT idkategori FROM tblkategori WHERE idkategori<"+String.valueOf(extra.getInt("idkategori"))+" ORDER BY idkategori ASC";
            Cursor cKat=db.sq(qcount);
            spKatSelection=cKat.getCount();
            spKat.setSelection(cKat.getCount());
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nama = edtNama.getText().toString();
                Biaya = Modul.unNumberFormat(edtBiaya.getText().toString());
//                Integer idKat = 0;
                if (TextUtils.isEmpty(Nama)||TextUtils.isEmpty(Biaya)||Modul.strToDouble(Biaya)<=0){
                    Toast.makeText(MenuUbahJasaLaundry.this, "Isi data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }else {
                    if (idKat.equals(0)){
                        Toast.makeText(MenuUbahJasaLaundry.this, "Pilih kategori terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }else {
                        if (idJasa==null){
                            if (db.insertToJasa(idKat,Nama,Biaya,Satuan)){
                                Toast.makeText(MenuUbahJasaLaundry.this, "Tambah data berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(MenuUbahJasaLaundry.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (db.updateJasa(idJasa,idKat,Nama,Biaya,Satuan)){
                                Toast.makeText(MenuUbahJasaLaundry.this, "Tambah data berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(MenuUbahJasaLaundry.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
        });
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuUbahJasaLaundry.this, MenuDaftarJasaLaundry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIdKat = db.getIdKategori();
        getIdKat.remove(0);
        getKategoriData();
        spKat.setSelection(spKatSelection);
        spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                idKat = Modul.strToInt(getIdKat.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                if (label.equals("Piece (Pc)")){
                    Satuan = "pc";
                }else if (label.equals("Kilo Gram(Kg)")){
                    Satuan = "kg";
                }else if (label.equals("Meter Persegi(M²)")){
                    Satuan = "m2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getKategoriData(){
        DatabaseLaundry db = new DatabaseLaundry(this);
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

    public void tambah(View view) {
        startActivity(new Intent(this,MenuKategoriJasaLaundry.class));
    }
}
