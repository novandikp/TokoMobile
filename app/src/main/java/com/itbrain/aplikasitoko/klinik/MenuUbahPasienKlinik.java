package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuUbahPasienKlinik extends AppCompatActivity {
    DatabaseKlinik db;
    View v;
    ArrayList arraylist= new ArrayList();
    ArrayList arrayKategori= new ArrayList();
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ubah_pasien_klinik);
        db = new DatabaseKlinik(this);
        v= this.findViewById(android.R.id.content);
        id = getIntent().getStringExtra("id");
        setText();
        setDefault();
        final EditText eThn = findViewById(R.id.eThn);
        final EditText eBln = findViewById(R.id.eBln);
        eThn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int tahun = ModulKlinik.strToInt(eThn.getText().toString());
                if (tahun>3){
                    eBln.setText("0");
                    eBln.setEnabled(false);

                }else{
                    eBln.setEnabled(true);
                }
            }
        });

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefault(){
        Cursor c = db.sq(ModulKlinik.selectwhere("tblpasien")+ModulKlinik.sWhere("idpasien",id));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.eNama,ModulKlinik.getString(c,"pasien"));
        ModulKlinik.setText(v,R.id.eAlamat,ModulKlinik.getString(c,"alamat"));
        ModulKlinik.setText(v,R.id.eNo,ModulKlinik.getString(c,"notelp"));
        ModulKlinik.setText(v,R.id.eGoldar,ModulKlinik.getString(c,"goldarah"));
        ModulKlinik.setText(v,R.id.eNobpjs,ModulKlinik.getString(c,"nobpjs"));
        ModulKlinik.setText(v,R.id.eNIK,ModulKlinik.getString(c,"nik"));
        String umur = ModulKlinik.getUmur(ModulKlinik.getString(c,"umur"));
        String [] row=umur.split(" ");
        if (umur.contains("tahun")){
            ModulKlinik.setText(v,R.id.eThn,row[0]);
            ModulKlinik.setText(v,R.id.eBln,"0");
        }else{
            ModulKlinik.setText(v,R.id.eThn,"0");
            ModulKlinik.setText(v,R.id.eBln,row[0]);
        }

        String jk = ModulKlinik.getString(c,"jk");
        Spinner spinner = (Spinner) findViewById(R.id.sKelamin) ;
        if (jk.equals("P")){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(1);
        }
    }

    private void simpan(){
        String nama = ModulKlinik.getText(v,R.id.eNama);
        Spinner spinner = (Spinner) findViewById(R.id.sKelamin) ;
        String kelamin ;
        if (spinner.getSelectedItemPosition()==0){
            kelamin="P";
        }else{
            kelamin="L";
        }
        int tahun = ModulKlinik.strToInt(ModulKlinik.getText(v,R.id.eThn));
        int bulan = ModulKlinik.strToInt(ModulKlinik.getText(v,R.id.eBln));
        String alamat = ModulKlinik.getText(v,R.id.eAlamat);
        String no = ModulKlinik.getText(v,R.id.eNo);
        String goldar = ModulKlinik.getText(v,R.id.eGoldar);
        String nik = ModulKlinik.getText(v,R.id.eNIK);
        String bpjs = ModulKlinik.getText(v,R.id.eNobpjs);
        if (!TextUtils.isEmpty(nama)  && (tahun!=0 || bulan!=0) && !TextUtils.isEmpty(alamat)
                && !TextUtils.isEmpty(no) && !TextUtils.isEmpty(goldar) && !TextUtils.isEmpty(nik) ){
            String isi []={nama,kelamin,ModulKlinik.setUmur(bulan,tahun),alamat,no,goldar,nik,bpjs,id};
            String q = ModulKlinik.splitParam("UPDATE tblpasien SET pasien=?,jk=?,umur=?,alamat=?,notelp=?,goldarah=?,nik=?,nobpjs=? WHERE idpasien=?   ",isi);
            if (db.exc(q)){
                ModulKlinik.showToast(this,"Berhasil menyimpan data");
                finish();
            }else{
                ModulKlinik.showToast(this,"Gagal menyimpan data");
            }
        }else{
            ModulKlinik.showToast(this,"Mohon isi data dengan lengkap dan benar");
        }
    }

    private void setText() {
        arrayKategori.clear();
        arrayKategori.add("Perempuan");
        arrayKategori.add("Laki-Laki");
        Spinner spinner = (Spinner) findViewById(R.id.sKelamin) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    public void simpan(View view) {
        simpan();
    }
}
