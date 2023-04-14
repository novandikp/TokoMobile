package com.itbrain.aplikasitoko.Laundry;

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

import java.util.Calendar;
import java.util.Locale;

public class ActivityKeuanganTambah_laundry extends AppCompatActivity {
    String type,fakturKeuangan="",tempFaktur,date,saldo="";
    DatabaseLaundry db;
    View v;
    int year, month, day;
    Calendar calendar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keuangan_tambah_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String judul="";
        type=getIntent().getStringExtra("type");
        if (type.equals("pemasukan")){
            judul="Pemasukan";
        }
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali14);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Cursor cKeuangan=db.sq(QueryLaundry.select("tblkeuangan"));
        cKeuangan.moveToLast();
        TextInputLayout tilBayar=(TextInputLayout) findViewById(R.id.textInputLayout28);
            tempFaktur="UANG-MASUK-";
            tilBayar.setHint("Pemasukan");
        Cursor cFaktur=db.sq(QueryLaundry.selectwhere("tblkeuangan")+ QueryLaundry.sLike("faktur",tempFaktur));
        if (ModulLaundry.getCount(cFaktur)>0){
            if (ModulLaundry.getCount(cFaktur)<10){
                fakturKeuangan=tempFaktur+"0"+String.valueOf(ModulLaundry.getCount(cFaktur)+1);
            }else {
                fakturKeuangan=tempFaktur+String.valueOf(ModulLaundry.getCount(cFaktur)+1);
            }
        }else {
            fakturKeuangan=tempFaktur+"01";
        }
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
        Cursor cSaldo=db.sq(QueryLaundry.select("tblkeuangan"));
        cSaldo.moveToLast();
        if (ModulLaundry.getCount(cSaldo)>0){
            saldo= ModulLaundry.removeE(ModulLaundry.getString(cSaldo,"saldo"));
        }else {
            saldo="0";
        }
        ModulLaundry.setText(v,R.id.tvSaldo,saldo);

        //tanggal
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //        button tgl
        date= ModulLaundry.getDate("yyyyMMdd");

        date= ModulLaundry.getDate("yyyyMMdd");
        ModulLaundry.setText(v,R.id.edtTanggal, ModulLaundry.dateToNormal(date));
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
            date= ModulLaundry.convertDate(ModulLaundry.setDatePickerNormal(thn,bln+1,day));
            ModulLaundry.setText(v, R.id.edtTanggal, ModulLaundry.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    public void tambah(View view) {
        final String[] q = {""};
        boolean dataBenar=false;
        if (type.equals("pemasukan")){
            if (ModulLaundry.getText(v,R.id.edtFaktur).isEmpty()|| ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtJenisKeuangan)).isEmpty()|| ModulLaundry.strToDouble(ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtJenisKeuangan)))<0){
                dataBenar=false;
            }else {
                double tempSaldo= ModulLaundry.strToDouble(ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJenisKeuangan)))+ ModulLaundry.strToDouble(ModulLaundry.parseDF(saldo));
                q[0] ="INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (" +
                        "'"+ ModulLaundry.convertDate(ModulLaundry.getText(v,R.id.edtTanggal))+"',"+
                        "'"+ ModulLaundry.getText(v,R.id.edtFaktur)+"',"+
                        "'"+ ModulLaundry.getText(v,R.id.edtKeterangan)+"',"+
                        "'"+ ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJenisKeuangan))+"',"+
                        "'"+ ModulLaundry.doubleToStr(tempSaldo)+"'"+
                        ")";
                dataBenar=true;
            }
        }else {
            if (ModulLaundry.getText(v,R.id.edtFaktur).isEmpty()|| ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtJenisKeuangan)).isEmpty()|| ModulLaundry.strToDouble(ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJenisKeuangan)))> ModulLaundry.strToDouble(ModulLaundry.parseDF(saldo))){
                dataBenar=false;
            }else {
                double tempSaldo= ModulLaundry.strToDouble(ModulLaundry.parseDF(saldo))- ModulLaundry.strToDouble(ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJenisKeuangan)));
                q[0] ="INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,keluar,saldo) VALUES (" +
                        "'"+ ModulLaundry.convertDate(ModulLaundry.getText(v,R.id.edtTanggal))+"',"+
                        "'"+ ModulLaundry.getText(v,R.id.edtFaktur)+"',"+
                        "'"+ ModulLaundry.getText(v,R.id.edtKeterangan)+"',"+
                        "'"+ ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJenisKeuangan))+"',"+
                        "'"+ ModulLaundry.doubleToStr(tempSaldo)+"'"+
                        ")";
                dataBenar=true;
            }
        }
        if (!dataBenar){
            Toast.makeText(ActivityKeuanganTambah_laundry.this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog dialog =new AlertDialog.Builder(this)
                    .setTitle("Simpan Transaksi")
                    .setMessage("Konfirmasi data yang anda masukkan sudah benar")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (db.exc(q[0])){
                                Toast.makeText(ActivityKeuanganTambah_laundry.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("Batal",null)
                    .create();
            dialog.show();
        }
    }
}
