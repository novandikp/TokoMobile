package com.itbrain.aplikasitoko.apotek;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

import java.util.Calendar;

public class Menu_Bayar_Hutang_Apotek extends AppCompatActivity {
    DatabaseApotek db;
    String idpelanggan,type;
    View v;
    ModulApotek config;
    Calendar calendar;
    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bayar_hutang_apotek);
        ImageButton imageButton = findViewById(R.id.kembali5);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db= new DatabaseApotek(this);
        v=this.findViewById(android.R.id.content);
        config= new ModulApotek(getSharedPreferences("config",MODE_PRIVATE));
        idpelanggan=getIntent().getStringExtra("idpelanggan");

        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");
        ModulApotek.setText(v,R.id.eTanggal, ModulApotek.getDate("dd/MM/yyyy"));

        setText();
        EditText bayar = findViewById(R.id.eBayar);

        bayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double kembali = ModulApotek.strToDouble(ModulApotek.getText(v,R.id.eBayar))- ModulApotek.strToDouble(ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)));
                String totalkembali = ModulApotek.removeE(ModulApotek.doubleToStr(kembali));
                totalkembali=totalkembali.replace("-.","");
                ModulApotek.setText(v,R.id.eKembali,totalkembali);
                TextInputLayout ekembali = findViewById(R.id.textInputLayout20);
                if (kembali<0){
                    ekembali.setHint("Sisa Hutang");
                }else{
                    ekembali.setHint("Kembalian");
                }
            }
        });

    }

    protected void setText(){

            String q= ModulApotek.selectwhere("tblsupplier")+ ModulApotek.sWhere("idsupplier",idpelanggan);
            Cursor c= db.sq(q);
            c.moveToNext();
            ModulApotek.setText(v,R.id.ePelanggan, ModulApotek.getString(c,"supplier"));
            ModulApotek.setText(v,R.id.eTotal, ModulApotek.removeE(ModulApotek.getString(c,"utang")));
            ModulApotek.setText(v,R.id.eKembali,"0");
            ModulApotek.setText(v,R.id.eBayar,"0");



    }


    public void transaks1(){
        double saldo;
        Cursor c= db.sq(ModulApotek.select("tbltransaksi"));
        if (c.getCount()>0){
            c.moveToLast();
            saldo= ModulApotek.strToDouble(ModulApotek.getString(c,"saldo"));
        }else{
            saldo=0;
        }

        String tgl = ModulApotek.getText(v,R.id.eTanggal);


        String hutang;
        String total = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)) ;
        if (ModulApotek.strToDouble(total)< ModulApotek.strToDouble(ModulApotek.getText(v,R.id.eBayar))){
            hutang = total;
            saldo -= ModulApotek.strToDouble(hutang);
        }else{
            hutang = ModulApotek.getText(v,R.id.eBayar);
            saldo -= ModulApotek.strToDouble(hutang);
        }
        Cursor b = db.sq(ModulApotek.select("tblutang"));
        int id = b.getCount();
        String faktur="00000000";
        faktur =faktur.substring(0,faktur.length()- ModulApotek.intToStr(id).length())+ ModulApotek.intToStr(id);
        String [] isi={tgl,"Pelunasan Hutang "+ ModulApotek.getText(v,R.id.ePelanggan),faktur,hutang, ModulApotek.doubleToStr(saldo)};
        String q= ModulApotek.splitParam("INSERT INTO tbltransaksi (tgltransaksi,kettransaksi,fakturtran,keluar,saldo) VALUES (?,?,?,?,?)",isi);
        db.exc(q);
    }




    private void tambahlimit(){
        boolean status = Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status;
        if (!status){
            int batas = ModulApotek.strToInt(config.getCustom("hutang","1"))+1;
            config.setCustom("hutang", ModulApotek.intToStr(batas));
        }


    }

    public void calendar(View view) {
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
            ModulApotek.setText(v,R.id.eTanggal, ModulApotek.setDatePickerNormal(thn,bln+1,day));
        }
    };



    public void alert(){

        String bayar = ModulApotek.getText(v,R.id.eBayar);

        if (ModulApotek.strToInt(bayar)!=0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Simpan Data").setMessage("Apakah anda yakin simpan data pembayaran hutang ?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                        byr1();



                    finish();

                }
            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();



        }else{
            ModulApotek.showToast(this,"Gagal Simpan");
        }

    }


    public void bayar(View view){
        alert();
    }



    public void byr1(){
        String bayar = ModulApotek.getText(v,R.id.eBayar);
        String tgl = ModulApotek.getText(v,R.id.eTanggal);
        String hutang;
        String total = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eTotal)) ;
        if (ModulApotek.strToDouble(total)< ModulApotek.strToDouble(bayar)){
            hutang = total;
        }else{
            hutang = bayar;
        }


        String[] pp = {hutang,tgl,idpelanggan} ;
        String qq = ModulApotek.splitParam("INSERT INTO tblutang (bayarhutang,tglbayar,idsupplier) VALUES (?,?,?) ",pp) ;
        db.exc(qq);
        transaks1();
        tambahlimit();

    }




}
