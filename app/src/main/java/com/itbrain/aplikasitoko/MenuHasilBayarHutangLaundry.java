package com.itbrain.aplikasitoko;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MenuHasilBayarHutangLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;
    String idpelanggan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_hasil_bayar_hutang_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);
        Modul.btnBack("Menu Bayar Hutang",getSupportActionBar());
        idpelanggan=getIntent().getStringExtra("id");
        getPelanggan();

        TextInputEditText edtBayar=(TextInputEditText)findViewById(R.id.edtJumlahBayar);
//        edtBayar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                double kembali=Modul.strToDouble(Modul.getText(v,R.id.edtBayar))-Modul.strToDouble(Modul.removeRp(Modul.unNumberFormat(Modul.getText(v,R.id.tvHutang))));
//                Modul.setText(v,R.id.edtKembalian,Modul.removeE(Modul.doubleToStr(kembali)));
//            }
//        });
        Cursor cKembali=db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+idpelanggan);
        cKembali.moveToNext();
        EditText edtKembalian=(EditText) findViewById(R.id.edtKembalian);
        edtBayar.addTextChangedListener(new NumberTextWatcherKembali(edtBayar,new Locale("in",""),2,Modul.justRemoveE(Modul.getString(cKembali,"hutang")),edtKembalian));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getPelanggan(){
        Cursor c=db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+idpelanggan);
        c.moveToNext();
        Modul.setText(v,R.id.txtNama,Modul.getString(c,"pelanggan"));
        Modul.setText(v,R.id.txtNoTelp,Modul.getString(c,"notelp"));
        Modul.setText(v,R.id.txtAlamat,Modul.getString(c,"alamat"));
        Modul.setText(v,R.id.txtHutang,"Rp. "+Modul.removeE(Modul.getString(c,"hutang")));
    }

    public void bayar(View view) {
        String nominal=Modul.unNumberFormat(Modul.getText(v,R.id.edtJumlahBayar));
        String hutang=Modul.removeRp(Modul.unNumberFormat(Modul.getText(v,R.id.txtHutang)));
        String kembali=Modul.unNumberFormat(Modul.getText(v,R.id.edtKembalian));
        String[] input={idpelanggan,Modul.getDate("yyyyMMdd"),hutang,nominal,nominal,kembali,Modul.getText(v,R.id.edtKeterangan)};
        String q=Query.splitParam("INSERT INTO tblbayarhutang (idpelanggan,tglbayar,hutang,bayar,bayarhutang,kembali,keteranganhutang) VALUES (?,?,?,?,?,?,?)",input);
        String[] input2={idpelanggan,Modul.getDate("yyyyMMdd"),hutang,nominal,hutang,kembali,Modul.getText(v,R.id.edtKeterangan)};
        String q2=Query.splitParam("INSERT INTO tblbayarhutang (idpelanggan,tglbayar,hutang,bayar,bayarhutang,kembali,keteranganhutang) VALUES (?,?,?,?,?,?,?)",input2);

        if (nominal.isEmpty()){
            Toast.makeText(this, "Isi nominal bayar", Toast.LENGTH_SHORT).show();
        }else if(Modul.strToInt(nominal)>Modul.strToInt(hutang)){
            if (db.exc(q2)){
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }else if (Modul.strToInt(nominal)<=Modul.strToInt(hutang)){
            if (db.exc(q)){
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
