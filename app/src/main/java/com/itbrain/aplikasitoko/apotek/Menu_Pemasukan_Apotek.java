package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Menu_Pemasukan_Apotek extends AppCompatActivity {
    String saldo;
    DatabaseApotek db;
    View v;
    int year,day,month;
    Calendar calendar;
    boolean stat=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pemasukan_apotek);
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        String date_v = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulApotek.setText(v, R.id.tgl, date_v);
        setSaldo();
        Locale local = new Locale("id", "id");
        final EditText pengeluaran = findViewById(R.id.masuk);
        pengeluaran.addTextChangedListener(new TextWatcherPengeluaran_Apotek(pengeluaran, local, 2));

    }
        public void setSaldo () {
            Cursor c = db.sq(ModulApotek.select("tbltransaksi"));
            if (c.getCount() == 0) {
                saldo = "0";
            } else {
                c.moveToLast();
                saldo = ModulApotek.getString(c, "saldo");
            }
            ModulApotek.setText(v, R.id.saldo, "Rp." + ModulApotek.removeE(saldo));
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
            ModulApotek.setText(v,R.id.fakkur, ModulApotek.setDatePickerNormal(thn,bln+1,day));
        }
    };

    private void clear(){
        ModulApotek.setText(v,R.id.efakturrr,"");
        ModulApotek.setText(v,R.id.ket,"");
        ModulApotek.setText(v,R.id.masuk,"");

    }

    public void simpan(View view){
        simpantran();
    }

    private void simpantran(){
        String tgl= ModulApotek.getText(v,R.id.fakkur);
        String faktur= ModulApotek.getText(v,R.id.efakturrr);
        String keterangan= ModulApotek.getText(v,R.id.ket);
        String masuk= ModulApotek.getText(v,R.id.masuk);

        if (!faktur.isEmpty() && !keterangan.isEmpty() && !masuk.isEmpty()){
            double sisasaldo = ModulApotek.strToDouble(saldo)+ ModulApotek.strToDouble(ModulApotek.unNumberFormat(masuk));
            String [] p ={tgl,faktur,keterangan, ModulApotek.unNumberFormat(masuk), ModulApotek.doubleToStr(sisasaldo)};
            final String q= ModulApotek.splitParam("INSERT INTO tbltransaksi (tgltransaksi,fakturtran,kettransaksi,masuk,saldo) VALUES (?,?,?,?,?)",p);

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
            ModulApotek.showToast(this,"Isi data dengan benar");
        }



    }
}