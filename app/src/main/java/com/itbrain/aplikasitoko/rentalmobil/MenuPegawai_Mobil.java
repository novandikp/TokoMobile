package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

public class MenuPegawai_Mobil extends AppCompatActivity {

    DatabaseRentalMobil db;
    String type,idpegawai;
    private TextInputEditText eNama;
    private TextInputLayout textInputLayout;
    private TextInputEditText eAlamat;
    private TextInputLayout textInputLayout2;
    private TextInputEditText eNoTelp;
    private TextInputLayout textInputLayout3;
    private TextInputEditText eKTP;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupegawai_mobil);

        eNama
                = (TextInputEditText) findViewById(R.id.eNama);
        textInputLayout
                = (TextInputLayout) findViewById(R.id.textInputLayout);
        eAlamat
                = (TextInputEditText) findViewById(R.id.eAlamat);
        textInputLayout2
                = (TextInputLayout) findViewById(R.id.textInputLayout2);
        eNoTelp
                = (TextInputEditText) findViewById(R.id.eNoTelp);
        textInputLayout3
                = (TextInputLayout) findViewById(R.id.textInputLayout3);
        eKTP
                = (TextInputEditText) findViewById(R.id.eKTP);
        button3
                = (Button) findViewById(R.id.button3);
        db=new DatabaseRentalMobil(this);
        type=getIntent().getStringExtra("type");
        idpegawai=getIntent().getStringExtra("id");
        if(idpegawai!=null){
            setText();
        }


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idpegawai!=null){
                    edit();
                }else{
                    simpan();
                }
            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
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

    private void setText(){
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpegawai")+ModulRentalMobil.sWhere("idpegawai",idpegawai));
        c.moveToNext();
        eNama.setText(ModulRentalMobil.getString(c,"pegawai"));
        eAlamat.setText(ModulRentalMobil.getString(c,"alamatpegawai"));
        eNoTelp.setText(ModulRentalMobil.getString(c,"nopegawai"));
        eKTP.setText(ModulRentalMobil.getString(c,"ktppegawai"));
    }

    private void simpan(){
        String nama = eNama.getText().toString();
        String alamat = eAlamat.getText().toString();
        String notelp = eNoTelp.getText().toString();
        String ktp = eKTP.getText().toString();
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp) && !TextUtils.isEmpty(ktp) ){
            String [] isi={nama,alamat,notelp,ktp};
            String q= ModulRentalMobil.splitParam("INSERT INTO tblpegawai (pegawai,alamatpegawai,nopegawai,ktppegawai) VALUES (?,?,?,?)",isi);
            if (db.exc(q)){
                ModulRentalMobil.showToast(this,getString(R.string.toast_simpan));
                finish();
            }else{
                ModulRentalMobil.showToast(this,getString(R.string.gagal_simpan));
            }
        }else{
            ModulRentalMobil.showToast(this,getString(R.string.kurang_lengkap));
        }


    }

    private void edit(){
        String nama = eNama.getText().toString();
        String alamat = eAlamat.getText().toString();
        String notelp = eNoTelp.getText().toString();
        String ktp = eKTP.getText().toString();
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp) && !TextUtils.isEmpty(ktp) ){
            String [] isi={nama,alamat,notelp,ktp,idpegawai};
            String q= ModulRentalMobil.splitParam("UPDATE tblpegawai SET pegawai=?,alamatpegawai=?,nopegawai=?,ktppegawai=? WHERE idpegawai=?  ",isi);
            if (db.exc(q)){
                ModulRentalMobil.showToast(this,getString(R.string.toast_simpan));
                finish();
            }else{
                ModulRentalMobil.showToast(this,getString(R.string.gagal_simpan));
            }
        }else{
            ModulRentalMobil.showToast(this,getString(R.string.kurang_lengkap));
        }


    }
}