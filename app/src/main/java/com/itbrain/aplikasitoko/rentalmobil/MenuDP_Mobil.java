package com.itbrain.aplikasitoko.rentalmobil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

import java.util.Calendar;

public class MenuDP_Mobil extends AppCompatActivity {

    String idrental;
    private TextView tFaktur;
    private TextView tPelanggan;
    private TextView ePegawai;
    private TextView eTotal;
    private CardView cardView;
    private Button button7;
    private TextInputEditText eTanggal;
    private TextInputLayout textInputLayout8;
    private ImageView imageView2;
    private TextInputEditText eDP;
    DatabaseRentalMobil db;
    Calendar calendar;
    int year, month, day;
    View v;
    String total;
    ModulRentalMobil config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dp_mobil);
        config = new ModulRentalMobil(getSharedPreferences("config", MODE_PRIVATE));
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        v = this.findViewById(android.R.id.content);
        tFaktur
                = (TextView) findViewById(R.id.tFaktur);
        tPelanggan
                = (TextView) findViewById(R.id.tPelanggan);
        ePegawai
                = (TextView) findViewById(R.id.ePegawai);
        eTotal
                = (TextView) findViewById(R.id.eTotal);
        cardView
                = (CardView) findViewById(R.id.cardView);
        button7
                = (Button) findViewById(R.id.button7);
        eTanggal
                = (TextInputEditText) findViewById(R.id.eTanggal);
        textInputLayout8
                = (TextInputLayout) findViewById(R.id.textInputLayout8);
        imageView2
                = (ImageView) findViewById(R.id.imageView2);
        eDP
                = (TextInputEditText) findViewById(R.id.eDP);
        idrental = getIntent().getStringExtra("idrental");
        db = new DatabaseRentalMobil(this);
        setText();

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setText(){
        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rental")+ModulRentalMobil.sWhere("idrental",idrental));
        c.moveToNext();
        tFaktur.setText("Faktur : "+ModulRentalMobil.getString(c,"faktur"));
        tPelanggan.setText("Nama Pelanggan : " +ModulRentalMobil.getString(c,"pelanggan"));
        ePegawai.setText("Nama Pegawai : "+ModulRentalMobil.getString(c,"pegawai"));
        eTotal.setText("Total : "+ ModulRentalMobil.removeE(ModulRentalMobil.getString(c,"total")));
        total=ModulRentalMobil.getString(c,"total");
        eTanggal.setEnabled(false);
        String tgl = ModulRentalMobil.getDate("dd/MM/yyyy");
        eTanggal.setText(tgl);
    }

    private void simpan(){
        String tanggal = eTanggal.getText().toString();
        String dp = eDP.getText().toString();
        String flagrental;

        if (!TextUtils.isEmpty(dp)){
            if(ModulRentalMobil.strToDouble(dp)==ModulRentalMobil.strToDouble(total)){
                flagrental="2";
            }else{
                flagrental="1";
            }
            final String [] isi ={dp,tanggal,flagrental,idrental};
            if (ModulRentalMobil.strToDouble(dp)>ModulRentalMobil.strToDouble(total)){
                ModulRentalMobil.showToast(this,getString(R.string.lebih_dp));
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.judul_konfirmasi)).setMessage(getString(R.string.subjudul_konfirmasi)).setPositiveButton(getString(R.string.iya), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String q = ModulRentalMobil.splitParam("UPDATE tblrental SET dp=?,tglrental=?, flagrental=? WHERE idrental=?   ",isi);
                        if (db.exc(q)){
                            ModulRentalMobil.showToast(MenuDP_Mobil.this,getString(R.string.toast_simpan));
                            print();
                        }
                    }
                }).setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

            }



        }else{
            ModulRentalMobil.showToast(this,getString(R.string.kurang_lengkap));
        }

    }

    public void kalendar(View view) {
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

            ModulRentalMobil.setText(v,R.id.eTanggal,ModulRentalMobil.setDatePickerNormal(thn,bln+1,day));

        }
    };

    private void print(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.judul_cetak)).setMessage(getString(R.string.subjudul_cetak)).setPositiveButton(getString(R.string.iya), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent i = new Intent(MenuDP_Mobil.this, MenuCetak_Mobil.class);
                i.putExtra("idorder",idrental);
                i.putExtra("type","dp");
                startActivity(i);
            }
        }).setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MenuDP_Mobil.this, MenuTransaksi_Mobil.class);
                startActivity(i);
            }
        }).show();
    }

    public void Konfirmasi(View view) {
        simpan();
    }
}
