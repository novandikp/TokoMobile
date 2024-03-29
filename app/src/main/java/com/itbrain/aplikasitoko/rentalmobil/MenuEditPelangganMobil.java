package com.itbrain.aplikasitoko.rentalmobil;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

public class MenuEditPelangganMobil extends AppCompatActivity {

    private TextInputEditText eNama;
    private TextInputLayout textInputLayout;
    private TextInputEditText eAlamat;
    private TextInputLayout textInputLayout2;
    private TextInputEditText eNoTelp;
    private TextInputLayout textInputLayout3;
    private TextInputEditText eKTP;
    private Button button3;
    DatabaseRentalMobil db;
    View v;
    String type, idpelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menueditpelangganmobil);

        eNama
                = (TextInputEditText) findViewById(R.id.tNama);
        textInputLayout
                = (TextInputLayout) findViewById(R.id.textInputLayout);
        eAlamat
                = (TextInputEditText) findViewById(R.id.tAlamat);
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

        v = this.findViewById(android.R.id.content);
        db = new DatabaseRentalMobil(this);
        type = getIntent().getStringExtra("type");
        idpelanggan=getIntent().getStringExtra("id");
        if(idpelanggan!=null){
            setText();
        }



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idpelanggan!=null){
                    edit();
                }else{
                    edit();
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


    private void setText() {
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpelanggan") + ModulRentalMobil.sWhere("idpelanggan", idpelanggan));
        c.moveToNext();
        eNama.setText(ModulRentalMobil.getString(c, "pelanggan"));
        eAlamat.setText(ModulRentalMobil.getString(c, "alamat"));
        eNoTelp.setText(ModulRentalMobil.getString(c, "notelp"));
        eKTP.setText(ModulRentalMobil.getString(c, "noktp"));
    }



    private void edit() {
        String nama = eNama.getText().toString();
        String alamat = eAlamat.getText().toString();
        String notelp = eNoTelp.getText().toString();
        String ktp = eKTP.getText().toString();
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp) && !TextUtils.isEmpty(ktp)) {
            String[] isi = {nama, alamat, notelp, ktp, idpelanggan};
            String q = ModulRentalMobil.splitParam("UPDATE tblpelanggan SET pelanggan=?,alamat=?,notelp=?,noktp=? WHERE idpelanggan=?   ", isi);
            if (db.exc(q)) {
                ModulRentalMobil.showToast(this, getString(R.string.toast_edit));
                finish();
            } else {
                ModulRentalMobil.showToast(this, getString(R.string.gagal_simpan));
            }
        } else {
            ModulRentalMobil.showToast(this, getString(R.string.kurang_lengkap));
        }
    }
}

