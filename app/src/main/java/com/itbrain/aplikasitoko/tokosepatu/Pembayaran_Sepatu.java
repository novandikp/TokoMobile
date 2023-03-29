package com.itbrain.aplikasitoko.tokosepatu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.itbrain.aplikasitoko.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Pembayaran_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config, temp;
    DatabaseTokoSepatu db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    ArrayList arraystat = new ArrayList();
    String id;
    int year,day,month;
    Calendar calendar;
    String faktur,status;
    String pel;
    double saldo;
    boolean stat=false;
    public static String edit="bayar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_sepatu);
        db = new DatabaseTokoSepatu(this);
        v = this.findViewById(android.R.id.content);
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        id=getIntent().getStringExtra("id");
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pembayaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulTokoSepatu.setText(v, R.id.tgl,date_v);


        getIsi();
        pembayaran();
        setText();

        NumberKeyboard numberKeyboard = findViewById(R.id.key);
        numberKeyboard.setListener(new NumberKeyboardListener() {

            @Override
            public void onNumberClicked(int i) {
                EditText editText;
                if (edit.equals("bayar")){
                    editText =findViewById(R.id.bayar);
                }else{
                    editText =findViewById(R.id.diskon);
                }

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
                if (edit.equals("bayar")){
                    editText =findViewById(R.id.bayar);
                }else{
                    editText =findViewById(R.id.diskon);
                }
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
                if (edit.equals("bayar")){
                    bayar =findViewById(R.id.bayar);
                }else{
                    bayar =findViewById(R.id.diskon);
                }

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
    protected void onResume() {
        super.onResume();
        getIsi();

    }

    public void getIsi(){
        Cursor c = db.sq("SELECT * FROM qjual WHERE idjual="+id) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            faktur="00000000";
            ModulTokoSepatu.setText(v,R.id.pel,ModulTokoSepatu.getString(c,"pelanggan"));
            ModulTokoSepatu.setText(v,R.id.faktur,faktur.substring(0,faktur.length()-ModulTokoSepatu.getString(c,"idjual").length())+ModulTokoSepatu.getString(c,"idjual")) ;
            ModulTokoSepatu.setText(v,R.id.total,ModulTokoSepatu.setCurrency(ModulTokoSepatu.getString(c,"total"))) ;
            ModulTokoSepatu.setText(v,R.id.bayar,ModulTokoSepatu.getString(c,"bayar"));
            ModulTokoSepatu.setText(v,R.id.diskon,"0");


        }
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

    public void transaksi(){
        String faktur = ModulTokoSepatu.getText(v,R.id.faktur);
        String tgl= ModulTokoSepatu.getText(v,R.id.tgl);

        String masuk;
        Cursor c= db.sq(ModulTokoSepatu.select("tbltransaksi"));
        if (c.getCount()==0){
            saldo=0;
        }else{
            c.moveToLast();
            saldo=ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"saldo"));
        }
        String kondisi=getStatus();
        if (kondisi.equals("Tunai")){
            masuk=ModulTokoSepatu.unNumberFormat(ModulTokoSepatu.getText(v,R.id.total));
        }else{
            masuk= ModulTokoSepatu.unNumberFormat(ModulTokoSepatu.getText(v,R.id.bayar));
        }

        saldo+=ModulTokoSepatu.strToDouble(masuk);
        String[] p = {tgl,faktur,"Penjualan",masuk,ModulTokoSepatu.doubleToStr(saldo)} ;
        ;
        String q =ModulTokoSepatu.splitParam("INSERT INTO tbltransaksi (tgltransaksi,fakturtran,kettransaksi,masuk,saldo) VALUES (?,?,?,?,?)",p) ;
        db.exc(q);
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

    public void pembayaran(){
        final EditText pembayaran= findViewById(R.id.bayar);
        final EditText total = findViewById(R.id.total);
        final EditText diskon = findViewById(R.id.diskon);
        final EditText kembali = findViewById(R.id.kembali);
        Cursor c = db.sq("SELECT * FROM qjual WHERE idjual="+id) ;
        c.moveToNext();
        String harga=ModulTokoSepatu.getString(c,"total");
        Locale locale = new Locale("id","id");
        pembayaran.setShowSoftInputOnFocus(false);
        diskon.setShowSoftInputOnFocus(false);
        diskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit="diskon";
                keyboard();
            }
        });
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit="bayar";
                keyboard();
            }
        });

        pembayaran.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit="bayar";
                keyboard();
            }
        });

        diskon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit="diskon";
                keyboard();
            }
        });

        diskon.addTextChangedListener(new TextWatcherDiskon_Sepatu(diskon,harga,total,locale,2,this));

//        diskon.addTextChangedListener(new TextWatcher() {
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
//                Cursor c = db.sq("SELECT * FROM qjual WHERE idjual="+id) ;
//                c.moveToNext();
//                double harga= ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"total"));
//                if(ModulTokoSepatu.strToDouble(diskon.getText().toString())>harga){
//                    ModulTokoSepatu.showToast(Pembayaran.this,"Potongan harga lebih besar daripada total pembelian");
//                    diskon.setText("0");
//                }else{
//                    harga= harga-ModulTokoSepatu.strToDouble(diskon.getText().toString());
//                    ModulTokoSepatu.setText(v,R.id.total,ModulTokoSepatu.setCurrency(harga));
//                }
//
//            }
//        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double kembali= ModulTokoSepatu.strToDouble(ModulTokoSepatu.unNumberFormat(pembayaran.getText().toString()))-ModulTokoSepatu.strToDouble(ModulTokoSepatu.unNumberFormat(total.getText().toString()));
                String bali = ModulTokoSepatu.setCurrency(kembali);

                ModulTokoSepatu.setText(v,R.id.kembali,bali);
            }
        });

        pembayaran.addTextChangedListener(new TextWatcherJual_Sepatu(pembayaran,total,kembali,locale,2));
//        pembayaran.addTextChangedListener(new TextWatcher() {
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
//                double kembali= ModulTokoSepatu.strToDouble(pembayaran.getText().toString())-ModulTokoSepatu.strToDouble(ModulTokoSepatu.unNumberFormat(total.getText().toString()));
//                String bali = ModulTokoSepatu.setCurrency(kembali);
//
//                ModulTokoSepatu.setText(v,R.id.kembali,bali);
//            }
//        });
    }

    private void setCurrency(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (!s.toString().equals(current)) {
                    edt.removeTextChangedListener(this);

//                    Locale local = new Locale("id", "id");
//                    String replaceable = String.format("[Rp,.\\s]",
//                            NumberFormat.getCurrencyInstance().getCurrency()
//                                    .getSymbol(local));
//                    String cleanString = s.toString().replaceAll(replaceable,
//                            ".");
//
//                    double parsed;
//                    try {
//                        parsed = Double.parseDouble(cleanString);
//                    } catch (NumberFormatException e) {
//                        parsed = 0.00;
//                    }
//
                    double parse =ModulTokoSepatu.strToDouble(s.toString());
//                    NumberFormat formatter = NumberFormat
//                            .getInstance(new Locale("id","id"));
//                    formatter.setMaximumFractionDigits(2);
//                    formatter.setParseIntegerOnly(false);
//                    String formatted = formatter.format((parse));
//
//                    String replace = String.format("[Rp\\s]",
//                            NumberFormat.getCurrencyInstance().getCurrency()
//                                    .getSymbol(local));
//                    String clean = formatted.replaceAll(replace, "");
                    NumberFormat format =NumberFormat.getInstance(new Locale("id","id"));
                    String formatted=format.format(parse);
                    current = formatted;
                    edt.setText(formatted);
                    edt.setSelection(formatted.length());
                    edt.addTextChangedListener(this);
                }
            }
        });
    }

    private void setText() {
        ModulTokoSepatu.setText(v,R.id.diskon,"0");
        arraystat.add("Tunai");
        arraystat.add("Utang");
        Spinner spinner = (Spinner) findViewById(R.id.spinnerStat) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraystat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private String getStatus() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerStat) ;
        String text = spinner.getSelectedItem().toString();
        return text;
    }

    public void kalendar(View view) {
        setDate(1);
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


    private void tambahJual(){
        String faktur = ModulTokoSepatu.getText(v,R.id.faktur);
        String status = getStatus();
        String tgl= ModulTokoSepatu.getText(v,R.id.tgl);
        String bayar= ModulTokoSepatu.getText(v,R.id.bayar);
        String kembali= ModulTokoSepatu.getText(v,R.id.kembali);
        String potongan = ModulTokoSepatu.getText(v,R.id.diskon);




        if(!TextUtils.isEmpty(tgl) && !TextUtils.isEmpty(bayar)  && !TextUtils.isEmpty(kembali)){
            String[] p = {faktur,status,tgl,ModulTokoSepatu.unNumberFormat(bayar),ModulTokoSepatu.unNumberFormat(kembali),ModulTokoSepatu.unNumberFormat(potongan),id} ;
            ;
            String q =ModulTokoSepatu.splitParam("UPDATE tbljual SET fakturbayar=?,status=?,tgljual=?,bayar=?,kembali=?,potongan=? WHERE idjual=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    public void selesai(){
        tambahJual();
        transaksi();
        String freetran = config.getCustom("transaksi","1");
        final String profile = config.getCustom("profil","");
        int tran =ModulTokoSepatu.strToInt(freetran)+1;
        freetran=ModulTokoSepatu.intToStr(tran);
        config.setCustom("transaksi",freetran);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog alert;
        final String finalFreetran = freetran;
        alertDialog.setMessage("Apakah anda ingin mencetak struk ")
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(Pembayaran_Sepatu.this,ActivityTransaksiBayarCetak_Sepatu.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.putExtra("idjual",id);
                        in.putExtra("type","tes");
                        startActivity(in);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent in = new Intent(Pembayaran_Sepatu.this,Aplikasi_Menu_Transaksi_Toko_Sepatu.class);
                        startActivity(in);

                    }
                });
        alert=alertDialog.create();

        alert.setTitle("Cetak Struk");
        alert.show();
    }



    public void tambah(View view) {
        String bayar = ModulTokoSepatu.getText(v,R.id.bayar);
        String kembali = ModulTokoSepatu.unNumberFormat(ModulTokoSepatu.getText(v,R.id.kembali));
        if (getStatus().equals("Tunai")){
            if (ModulTokoSepatu.strToInt(kembali)<0||bayar.equals("0")){
                ModulTokoSepatu.showToast(this,"Uang masih belum cukup");
            }else{
                selesai();
            }
        }else{
            if(ModulTokoSepatu.getText(v, R.id.pel).equals("Kosong")){
                ModulTokoSepatu.showToast(this,"Pelanggan Kosong tidak bisa hutang");
            }else{
                if (ModulTokoSepatu.strToInt(kembali)>0){
                    ModulTokoSepatu.showToast(this,"Uang Masih Cukup");
                }else if (bayar.equals("0")){
                    ModulTokoSepatu.showToast(this,"Isi Pembayaran");
                }else{

                    selesai();
                }
            }

        }
    }

    public void cari(View view) {
        Intent i = new Intent (Pembayaran_Sepatu.this,Menu_Penjualan_Kategori_Sepatu.class);
        i.putExtra("type","pelanggan");
        startActivityForResult(i,1000);
    }


}
