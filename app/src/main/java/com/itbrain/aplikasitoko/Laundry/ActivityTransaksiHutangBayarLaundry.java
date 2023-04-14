package com.itbrain.aplikasitoko.Laundry;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.util.Locale;

public class ActivityTransaksiHutangBayarLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;
    String idpelanggan,totalHutang,namaPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_hutang_bayar_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);

        idpelanggan=getIntent().getStringExtra("id");
        getPelanggan();

        ImageButton i = findViewById(R.id.kembali17);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextInputEditText edtBayar=(TextInputEditText)findViewById(R.id.edtBayar);
        Cursor cKembali=db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+idpelanggan);
        cKembali.moveToNext();
        EditText edtKembali=(EditText) findViewById(R.id.edtKembali);
        edtBayar.addTextChangedListener(new NumberTextWatcherKembali(edtBayar,new Locale("in","ID"),2, ModulLaundry.justRemoveE(ModulLaundry.getString(cKembali,"hutang")),edtKembali));
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

    private void getPelanggan(){
        Cursor c=db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+idpelanggan);
        c.moveToNext();
        ModulLaundry.setText(v,R.id.tvNama, ModulLaundry.getString(c,"pelanggan"));
        namaPelanggan= ModulLaundry.getString(c,"pelanggan");
        ModulLaundry.setText(v,R.id.tvNoTelp, ModulLaundry.getString(c,"notelp"));
        ModulLaundry.setText(v,R.id.tvAlamat, ModulLaundry.getString(c,"alamat"));
        ModulLaundry.setText(v,R.id.tvHutang,"Rp. "+ ModulLaundry.removeE(ModulLaundry.getString(c,"hutang")));
        totalHutang= ModulLaundry.getString(c,"hutang");
    }

    public void bayar(View view) {
        String nominal= ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtBayar));
        String hutang=totalHutang;
        String kembali= ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtKembali));
        String[] input={idpelanggan, ModulLaundry.getDate("yyyyMMdd"),hutang,nominal,nominal,kembali};
        String q= QueryLaundry.splitParam("INSERT INTO tblbayarhutang (idpelanggan,tglbayar,hutang,bayar,bayarhutang,kembali) VALUES (?,?,?,?,?,?)",input);
        String[] input2={idpelanggan, ModulLaundry.getDate("yyyyMMdd"),hutang,nominal,hutang,kembali};
        String q2= QueryLaundry.splitParam("INSERT INTO tblbayarhutang (idpelanggan,tglbayar,hutang,bayar,bayarhutang,kembali) VALUES (?,?,?,?,?,?)",input2);

        Cursor cKeuangan=db.sq(QueryLaundry.select("tblkeuangan"));
        cKeuangan.moveToLast();
        Cursor cFaktur=db.sq(QueryLaundry.selectwhere("tblkeuangan")+ QueryLaundry.sLike("faktur","BYR-HTNG-"));
        String fakturKeuangan="BYR-HTNG-01";
        if (ModulLaundry.getCount(cFaktur)>0){
            if (ModulLaundry.getCount(cFaktur)<10){
                fakturKeuangan="BYR-HTNG-0"+String.valueOf(ModulLaundry.getCount(cFaktur)+1);
            }else {
                fakturKeuangan="BYR-HTNG-"+String.valueOf(ModulLaundry.getCount(cFaktur)+1);
            }
        }
        double saldo=0;
        final String[] pKeuangan = new String[5];

        if (nominal.isEmpty()){
            Toast.makeText(this, "Isi nominal bayar", Toast.LENGTH_SHORT).show();
        }else if(Integer.valueOf(nominal)>Integer.valueOf(hutang)){
            if (ModulLaundry.getCount(cKeuangan)>0){
                saldo= ModulLaundry.strToDouble(hutang)+ ModulLaundry.getDouble(cKeuangan,"saldo");
            }else {
                saldo= ModulLaundry.strToDouble(hutang);
            }
            pKeuangan[0]= ModulLaundry.getDate("yyyyMMdd");
            pKeuangan[1]=fakturKeuangan;
            pKeuangan[2]="Pembayaran Hutang - "+namaPelanggan;
            pKeuangan[3]=hutang;
            pKeuangan[4]=String.valueOf(saldo);
            if (db.exc(q2)){
                if (db.exc(QueryLaundry.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

                }else {
                    Toast.makeText(this, "Gagal Menambahkan Data Keuangan", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }else if (Integer.valueOf(nominal)<=Integer.valueOf(hutang)){
            if (ModulLaundry.getCount(cKeuangan)>0){
                saldo= ModulLaundry.strToDouble(nominal)+ ModulLaundry.getDouble(cKeuangan,"saldo");
            }else {
                saldo= ModulLaundry.strToDouble(nominal);
            }
            pKeuangan[0]= ModulLaundry.getDate("yyyyMMdd");
            pKeuangan[1]=fakturKeuangan;
            pKeuangan[2]="Pembayaran Hutang - "+namaPelanggan;
            pKeuangan[3]=nominal;
            pKeuangan[4]=String.valueOf(saldo);
            if (db.exc(q)){
                if (db.exc(QueryLaundry.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

                }else {
                    Toast.makeText(this, "Gagal Menambahkan Data Keuangan", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
