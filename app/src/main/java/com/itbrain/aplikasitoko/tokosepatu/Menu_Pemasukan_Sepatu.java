package com.itbrain.aplikasitoko.tokosepatu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Menu_Pemasukan_Sepatu extends AppCompatActivity {
    String saldo;
    DatabaseTokoSepatu db;
    View v;
    int year,day,month;
    Calendar calendar;
    boolean stat=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pemasukan_sepatu);
        db= new DatabaseTokoSepatu(this);
        v= this.findViewById(android.R.id.content);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu Pemasukan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulTokoSepatu.setText(v, R.id.tgl,date_v);
        setSaldo();
        Locale local = new Locale("id","id");
        final EditText pengeluaran = findViewById(R.id.masuk);
        pengeluaran.setShowSoftInputOnFocus(false);
        pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard();
            }
        });
        pengeluaran.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hidekeyboard();
                }else{
                    keyboard();
                    hidekeyboarddef(Menu_Pemasukan_Sepatu.this);
                }

            }
        });
        pengeluaran.addTextChangedListener(new TextWatcherPengeluaran_Sepatu(pengeluaran,local,2));
        NumberKeyboard numberKeyboard = findViewById(R.id.key);
        numberKeyboard.setListener(new NumberKeyboardListener() {

            @Override
            public void onNumberClicked(int i) {
                EditText editText;
                editText =findViewById(R.id.masuk);
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
                editText =findViewById(R.id.masuk);
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

                bayar =findViewById(R.id.masuk);
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

    public void hidekeyboarddef(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSaldo(){
        Cursor c= db.sq(ModulTokoSepatu.select("tbltransaksi"));
        if (c.getCount()==0){
            saldo="0";
        }else{
            c.moveToLast();
            saldo=ModulTokoSepatu.getString(c,"saldo");
        }
        ModulTokoSepatu.setText(v,R.id.textView24,"Rp."+ModulTokoSepatu.removeE(saldo));
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

    private void clear(){
        ModulTokoSepatu.setText(v,R.id.faktur,"");
        ModulTokoSepatu.setText(v,R.id.ket,"");
        ModulTokoSepatu.setText(v,R.id.masuk,"");

    }

    public void simpan(View view){
        simpantran();
    }

    private void simpantran(){
        String tgl=ModulTokoSepatu.getText(v,R.id.tgl);
        String faktur=ModulTokoSepatu.getText(v,R.id.faktur);
        String keterangan=ModulTokoSepatu.getText(v,R.id.ket);
        String masuk=ModulTokoSepatu.getText(v,R.id.masuk);

        if (!faktur.isEmpty() && !keterangan.isEmpty() && !masuk.isEmpty()){
            double sisasaldo = ModulTokoSepatu.strToDouble(saldo)+ModulTokoSepatu.strToDouble(ModulTokoSepatu.unNumberFormat(masuk));
            String [] p ={tgl,faktur,keterangan,ModulTokoSepatu.unNumberFormat(masuk),ModulTokoSepatu.doubleToStr(sisasaldo)};
            final String q= ModulTokoSepatu.splitParam("INSERT INTO tbltransaksi (tgltransaksi,fakturtran,kettransaksi,masuk,saldo) VALUES (?,?,?,?,?)",p);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;

            alertDialog.setMessage("Apakah anda yakin menyimpan transaksi ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (db.exc(q)){

                                clear();
                                setSaldo();

                            }
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Simpan Transaksi");
            alert.show();
        }else{
            ModulTokoSepatu.showToast(this,"Isi data dengan benar");
        }



    }
}
