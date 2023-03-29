package com.itbrain.aplikasitoko.tokosepatu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Menu_Bayar_Hutang_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    String id;
    int year,day,month;
    Calendar calendar;
    double hutang;
    boolean stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bayar_hutang_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bayar Hutang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        id=getIntent().getStringExtra("id");
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulTokoSepatu.setText(v, R.id.tgl,date_v);
        setPelanggan();
        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tblpelanggan")+ModulTokoSepatu.sWhere("idpelanggan",id));
        c.moveToNext();
        String hutang=ModulTokoSepatu.getString(c,"hutang");
        final EditText pembayaran = (EditText) findViewById(R.id.bayar) ;
        final EditText kembali = findViewById(R.id.kembali);
        final EditText tgl = findViewById(R.id.tgl);
        Locale locale= new Locale("id","id");

        tgl.setFocusable(false);

        kembali.setFocusable(false);
        tgl.setShowSoftInputOnFocus(false);
        pembayaran.setShowSoftInputOnFocus(false);
        pembayaran.addTextChangedListener(new TextWatcherHutang_sepatu(pembayaran,hutang,kembali,locale,2));

        pembayaran.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyboard();
            }
        });

        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard();
            }
        });

        NumberKeyboard numberKeyboard = findViewById(R.id.key);
        numberKeyboard.setListener(new NumberKeyboardListener() {

            @Override
            public void onNumberClicked(int i) {
                EditText editText;
                editText =findViewById(R.id.bayar);
                int start =editText.getSelectionStart(); //this is to get the the cursor position

                int total =editText.getText().length();
                String s = ModulTokoSepatu.intToStr(i);
                editText.getText().insert(start, s);
                int selisih=editText.getText().length()-total;
                editText.setSelection(start+selisih);

            }

            @Override
            public void onLeftAuxButtonClicked() {
                EditText editText;
                editText =findViewById(R.id.bayar);
                int start =editText.getSelectionStart(); //this is to get the the cursor position
                int total =editText.getText().length();
                String s = ",";
                editText.getText().insert(start, s); //this will get the text and insert the String s into   the current position
                int selisih=editText.getText().length()-total;
                editText.setSelection(start+selisih);

            }

            @Override
            public void onRightAuxButtonClicked() {
                EditText bayar;

                bayar =findViewById(R.id.bayar);
                int posisi = bayar.getSelectionStart();
                int akhir = bayar.getSelectionEnd();
                String isi =bayar.getText().toString();
                if (bayar.getText().length()>0 && posisi>0){
                    bayar.getText().delete(posisi-1,posisi);
                }else if (bayar.getText().length()>0){
                    bayar.getText().delete(bayar.getText().length()-1,bayar.getText().length());
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (stat){
            hidekeyboard();
            stat=false;
        }else {
            super.onBackPressed();
        }

    }

    public void keyboard(){
        NumberKeyboard num = findViewById(R.id.key);
        num.setVisibility(View.VISIBLE);
        stat=true;

    }

    public void hidekeyboard(){
        NumberKeyboard num = findViewById(R.id.key);
        num.setVisibility(View.GONE);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPelanggan(){
        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tblpelanggan")+ModulTokoSepatu.sWhere("idpelanggan",id));
        c.moveToNext();
        ModulTokoSepatu.setText(v,R.id.tvPelanggan,ModulTokoSepatu.getString(c,"pelanggan"));
        ModulTokoSepatu.setText(v,R.id.tvHutang,ModulTokoSepatu.removeE(ModulTokoSepatu.getString(c,"hutang")));
        hutang=ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"hutang"));
    }

    public void transaksi(String faktur){

        String tgl= ModulTokoSepatu.getText(v,R.id.tgl);
        double saldo;
        String masuk;
        Cursor c= db.sq(ModulTokoSepatu.select("tbltransaksi"));
        if (c.getCount()==0){
            saldo=0;
        }else{
            c.moveToLast();
            saldo=ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"saldo"));
        }
        if (hutang<ModulTokoSepatu.strToDouble(ModulTokoSepatu.getText(v,R.id.bayar))){
            masuk=ModulTokoSepatu.doubleToStr(hutang);
        }else{
            masuk= ModulTokoSepatu.unNumberFormat(ModulTokoSepatu.getText(v,R.id.bayar));
        }

        saldo+=ModulTokoSepatu.strToDouble(masuk);
        String[] p = {tgl,faktur,"Pelunasan hutang",masuk,ModulTokoSepatu.doubleToStr(saldo)} ;
        ;
        String q =ModulTokoSepatu.splitParam("INSERT INTO tbltransaksi (tgltransaksi,fakturtran,kettransaksi,masuk,saldo) VALUES (?,?,?,?,?)",p) ;
        db.exc(q);
    }

    private void getBayarHutang(){
        String tanggal=ModulTokoSepatu.getText(v,R.id.tgl);
        String bayar = ModulTokoSepatu.getText(v,R.id.bayar);
        String pelanggan = id;
        if(!TextUtils.isEmpty(pelanggan) && !TextUtils.isEmpty(tanggal)  && !TextUtils.isEmpty(bayar)){
            String[] p = {pelanggan,tanggal,ModulTokoSepatu.unNumberFormat(bayar)} ;
            String q = ModulTokoSepatu.splitParam("INSERT INTO tblbayarhutang (idpelanggan,tglbayar,jumlahbayar) values(?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                Cursor c= db.sq("SELECT * FROM tblbayarhutang");
                c.moveToLast();
                String faktur="00000000";
                faktur=faktur.substring(0,faktur.length()-ModulTokoSepatu.getString(c,"idbayarhutang").length())+ModulTokoSepatu.getString(c,"idbayarhutang") ;
                transaksi(faktur);
                finish();
            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }
    public void kalender(View view) {
        showDialog(1);
    }
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulTokoSepatu.setText(v,R.id.tgl,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
        }
    };



    public void simpan(View view) {
        getBayarHutang();
    }
}
