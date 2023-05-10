package com.itbrain.aplikasitoko.tabungan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;
//import com.komputerkit.aplikasitabunganplus.util.NumberTextWatcher;

import java.util.Calendar;
import java.util.Locale;

public class MenuTransaksiKeuanganTambahTabungan extends AppCompatActivity {
    String type,fakturKeuangan="",tempFaktur,date,saldo="";
    DatabaseTabungan db;
    View v;
    int year, month, day;
    Calendar calendar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_keuangan_tambah_tabungan);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String judul="";
        type=getIntent().getStringExtra("type");
        if (type.equals("pemasukan")){
            judul=ModulTabungan.getResString(this,R.string.pemasukan);
        }else {
            judul=ModulTabungan.getResString(this,R.string.pengeluaran);
        }
//        ModulTabungan.btnBack(judul,getSupportActionBar());
        db=new DatabaseTabungan(this);
        v=this.findViewById(android.R.id.content);
        Cursor cKeuangan=db.sq(QueryTabungan.select("tblkeuangan"));
        cKeuangan.moveToLast();
        TextInputLayout tilBayar=(TextInputLayout) findViewById(R.id.textInputLayout28);
        if (type.equals("pemasukan")){
            tempFaktur=ModulTabungan.getResString(this,R.string.fakturmasuk);
            tilBayar.setHint(ModulTabungan.getResString(this,R.string.pemasukan));
        }else {
            tempFaktur=ModulTabungan.getResString(this,R.string.fakturkeluar);
            tilBayar.setHint(ModulTabungan.getResString(this,R.string.pengeluaran));
        }
        Cursor cFaktur=db.sq(QueryTabungan.selectwhere("tblkeuangan")+QueryTabungan.sLike("faktur",tempFaktur));
        if (cFaktur.getCount()>0){
            if (cFaktur.getCount()<10){
                fakturKeuangan=tempFaktur+"0"+String.valueOf(cFaktur.getCount()+1);
            }else {
                fakturKeuangan=tempFaktur+String.valueOf(cFaktur.getCount()+1);
            }
        }else {
            fakturKeuangan=tempFaktur+"01";
        }

        ImageButton imageButton = findViewById(R.id.kembaliPemasukan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText edtFaktur=(EditText)findViewById(R.id.edtFaktur);
        edtFaktur.setText(fakturKeuangan);
        final ImageButton ibtnGenerate=(ImageButton) findViewById(R.id.ibtnGenerate);
        ibtnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFaktur.setText("");
            }
        });
        edtFaktur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length()>0){
                    ibtnGenerate.setVisibility(View.VISIBLE);
                }else {
                    ibtnGenerate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    ibtnGenerate.setVisibility(View.VISIBLE);
                }else {
                    ibtnGenerate.setVisibility(View.GONE);
                }
            }
        });
        Cursor cSaldo=db.sq(QueryTabungan.select("tblkeuangan"));
        cSaldo.moveToLast();
        if (cSaldo.getCount()>0){
            saldo=ModulTabungan.removeE(ModulTabungan.getString(cSaldo,"saldo"));
        }else {
            saldo="0";
        }
        ModulTabungan.setText(v,R.id.tvSaldo,saldo);

        //tanggal
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //        button tgl
        date=ModulTabungan.getDate("yyyyMMdd");

        date=ModulTabungan.getDate("yyyyMMdd");
        ModulTabungan.setText(v,R.id.edtTanggal,ModulTabungan.dateToNormal(date));
        ImageButton ibtnTanggal=(ImageButton)findViewById(R.id.ibtnTanggal);
        ibtnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });
        EditText edtTanggal=(EditText) findViewById(R.id.edtTanggal);
        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(0);
            }
        });

        TextInputEditText edtKeuangan=(TextInputEditText)findViewById(R.id.edtJenisKeuangan);
        edtKeuangan.addTextChangedListener(new NumberTextWatcher(edtKeuangan,new Locale("in","ID"),2));
        edtKeuangan.requestFocus();
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
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        return new DatePickerDialog(this, dTanggal, year, month, day);
    }
    private DatePickerDialog.OnDateSetListener dTanggal = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            date=ModulTabungan.convertDate(ModulTabungan.setDatePickerNormal(thn,bln+1,day));
            ModulTabungan.setText(v, R.id.edtTanggal, ModulTabungan.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    public void tambah(View view) {
        final String[] q = {""};
        boolean dataBenar=false;
        if (type.equals("pemasukan")){
            if (ModulTabungan.getText(v,R.id.edtFaktur).isEmpty()||ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)).isEmpty()||ModulTabungan.strToDouble(ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)))<0){
                dataBenar=false;
            }else {
                double tempSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)))+ModulTabungan.strToDouble(ModulTabungan.parseDF(saldo));
                q[0] ="INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (" +
                        "'"+ModulTabungan.convertDate(ModulTabungan.getText(v,R.id.edtTanggal))+"',"+
                        "'"+ModulTabungan.getText(v,R.id.edtFaktur)+"',"+
                        "'"+ModulTabungan.getText(v,R.id.edtKeterangan)+"',"+
                        "'"+ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan))+"',"+
                        "'"+ModulTabungan.doubleToStr(tempSaldo)+"'"+
                        ")";
                dataBenar=true;
            }
        }else {
            if (ModulTabungan.getText(v,R.id.edtFaktur).isEmpty()||ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)).isEmpty()||ModulTabungan.strToDouble(ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)))>ModulTabungan.strToDouble(ModulTabungan.parseDF(saldo))){
                dataBenar=false;
            }else {
                double tempSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(saldo))-ModulTabungan.strToDouble(ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan)));
                q[0] ="INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,keluar,saldo) VALUES (" +
                        "'"+ModulTabungan.convertDate(ModulTabungan.getText(v,R.id.edtTanggal))+"',"+
                        "'"+ModulTabungan.getText(v,R.id.edtFaktur)+"',"+
                        "'"+ModulTabungan.getText(v,R.id.edtKeterangan)+"',"+
                        "'"+ModulTabungan.parseDF(ModulTabungan.getText(v,R.id.edtJenisKeuangan))+"',"+
                        "'"+ModulTabungan.doubleToStr(tempSaldo)+"'"+
                        ")";
                dataBenar=true;
            }
        }
        if (!dataBenar){
            Toast.makeText(MenuTransaksiKeuanganTambahTabungan.this, R.string.datasalah, Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog dialog =new AlertDialog.Builder(this)
                    .setTitle(R.string.titleSimpan)
                    .setMessage(R.string.konfirmasidatabenar)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (db.exc(q[0])){
                                Toast.makeText(MenuTransaksiKeuanganTambahTabungan.this, R.string.successAdd, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton(R.string.batal,null)
                    .create();
            dialog.show();
        }
    }
}
