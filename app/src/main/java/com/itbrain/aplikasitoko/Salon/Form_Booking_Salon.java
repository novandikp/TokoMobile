package com.itbrain.aplikasitoko.Salon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Form_Booking_Salon extends AppCompatActivity {

    Toolbar appbar;
    Button bSimpan;
    Calendar calendar;
    DatabaseSalon db;
    TextInputEditText edtTglJanji, edtJamJanji, edtNamaPelanggan;
    ImageView btnCariPelanggan, btnTglJanji, btnJamJanji;
    int tIdpelanggan, year, month, day, hour, minute;
    Integer idjanji, idpelanggan;
    String tgljanji, jamjanji, pelanggan, tnPelanggan, deviceid;
    ConfigSalon config;
    SharedPreferences getPrefs ;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_booking_salon);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //deviceid = FunctionSalon.getDecrypt(getPrefs.getString("deviceid","")) ;
        config = new ConfigSalon(getSharedPreferences("config",this.MODE_PRIVATE));
        db = new DatabaseSalon(this);

        bSimpan = (Button) findViewById(R.id.button35);
        edtTglJanji = (TextInputEditText) findViewById(R.id.edtTglJanji);
        edtJamJanji = (TextInputEditText) findViewById(R.id.edtJamJanji);
        edtNamaPelanggan = (TextInputEditText) findViewById(R.id.edtNamaPelanggan);

        String date_n = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        //FunctionSalon.setText(v, R.id.edtTglJanji,date_n);

        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        //FunctionSalon.setText(v, R.id.edtJamJanji,time);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            //Insert
            idjanji = null;
            idpelanggan = null;
        } else {
            idjanji = extra.getInt("idjanji");
            idpelanggan = extra.getInt("idpelanggan");
            edtTglJanji.setText(FunctionSalon.dateToNormal(extra.getString("tgljanji")));
            edtJamJanji.setText(FunctionSalon.timeToNormal(extra.getString("jamjanji")));
            edtNamaPelanggan.setText(extra.getString("pelanggan"));
        }

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgljanji = edtTglJanji.getText().toString();
                jamjanji = edtJamJanji.getText().toString();
                pelanggan = edtNamaPelanggan.getText().toString();
                String status="0";

                if (tgljanji.equals("") || jamjanji.equals("") || pelanggan.equals("")) {
                    Toast.makeText(Form_Booking_Salon.this, "Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseSalon db = new DatabaseSalon(Form_Booking_Salon.this);
                    if (idjanji == (null) && idpelanggan == (null)){
                        if (db.insertJanji(String.valueOf(tIdpelanggan), convertDate(tgljanji), convertTime(jamjanji), status)) {
                            Toast.makeText(Form_Booking_Salon.this, "Tambah Booking berhasil", Toast.LENGTH_SHORT).show();
                            tambahlimit();
                            finish();
                        } else {
                            Toast.makeText(Form_Booking_Salon.this, "Tambah data gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (db.updateJanji(idjanji, String.valueOf(idpelanggan), convertDate(tgljanji), convertTime(jamjanji), status)) {
                            Toast.makeText(Form_Booking_Salon.this, "Berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Form_Booking_Salon.this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
      btnCari();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDate(int i) {
        showDialog(i);
    }

    public void setTime(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        } else {
            return new TimePickerDialog(this, dTime, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
        }
    }
    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.edtTglJanji, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    private TimePickerDialog.OnTimeSetListener dTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            FunctionSalon.setText(v, R.id.edtJamJanji, FunctionSalon.setTimePickerNormal(hourOfDay, minute));
        }
    };

    private void btnCari() {

        btnJamJanji = (ImageView) findViewById(R.id.jamboking);
        btnJamJanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(2);
            }
        });

        btnTglJanji = (ImageView)findViewById(R.id.tglboking);
        btnTglJanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        btnCariPelanggan = (ImageView) findViewById(R.id.pell);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Form_Booking_Salon.this, ActivityPembayaranCariSalon.class);
                i.putExtra("cari", "pelanggan");
                startActivityForResult(i, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000) {
            tIdpelanggan = data.getIntExtra("idpelanggan", 0);
            tnPelanggan = data.getStringExtra("pelanggan");
            getPelanggan(FunctionSalon.intToStr(tIdpelanggan));
        }
    }

    public void getPelanggan(String idpelanggan){
        String q = QuerySalon.selectwhere("tblpelanggan") + QuerySalon.sWhere("idpelanggan", idpelanggan) ;
        Cursor c = db.sq(q) ;
        c.moveToNext() ;
        FunctionSalon.setText(v,R.id.edtNamaPelanggan,FunctionSalon.getString(c, "pelanggan")) ;
    }

    public String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public String convertTime(String time){
        String[] a = time.split(":");
        return a[0]+a[1];
    }

    private void tambahlimit(){
        boolean status = Aplikasi_Salon_Menu_Transaksi.status;
        if (!status){
            int batas = FunctionSalon.strToInt(config.getCustom("janji", "1"))+1;
            config.setCustom("janji", FunctionSalon.intToStr(batas));
        }
    }
    public String id(){
        return String.valueOf(idjanji);
    }
}