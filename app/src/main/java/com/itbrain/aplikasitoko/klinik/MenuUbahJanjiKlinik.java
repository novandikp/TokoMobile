package com.itbrain.aplikasitoko.klinik;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

import java.util.Calendar;

public class MenuUbahJanjiKlinik extends AppCompatActivity {
    int day,month,year,hour,minute;
    Calendar calendar;
    DatabaseKlinik db;
    View v;
    String idpelanggan,iddokter;
    String type,idjanji;
    int hari,bulan,tahun,jamq,menit;
    ModulKlinik config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ubah_janji_klinik);

        config = new ModulKlinik(getSharedPreferences("config", this.MODE_PRIVATE));
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        tahun = year;
        hari = day;
        bulan = month;
        jamq = hour;
        menit = minute;
        db = new DatabaseKlinik(this);
        v = this.findViewById(android.R.id.content);
        String tgl = ModulKlinik.getDate("dd/MM/yyyy");
        type = getIntent().getStringExtra("type");
        if (!type.equals("tambah")) {
            idjanji = getIntent().getStringExtra("idjanji");
            setText();
        } else {
            ModulKlinik.setText(v, R.id.eTanggal, tgl);
            setDefault();
        }

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
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

    private void setDefault(){
        iddokter="1";
        idpelanggan="1";
        Cursor dokter = db.sq(ModulKlinik.selectwhere("tbldokter")+ModulKlinik.sWhere("iddokter","1"));
        dokter.moveToNext();
        Cursor pasien = db.sq(ModulKlinik.selectwhere("tblpasien")+ModulKlinik.sWhere("idpasien","1"));
        pasien.moveToNext();
        ModulKlinik.setText(v,R.id.ePasien,ModulKlinik.getString(pasien,"pasien"));
        ModulKlinik.setText(v,R.id.eDokter,ModulKlinik.getString(dokter,"dokter"));

    }

    private void setText(){
        Cursor c = db.sq(ModulKlinik.selectwhere("view_janji")+ModulKlinik.sWhere("idjanji",idjanji));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.eJam,ModulKlinik.getString(c,"jamjanji"));
        ModulKlinik.setText(v,R.id.eTanggal,ModulKlinik.getString(c,"tgljanji"));
        ModulKlinik.setText(v,R.id.ePasien,ModulKlinik.getString(c,"pasien"));
        ModulKlinik.setText(v,R.id.eDokter,ModulKlinik.getString(c,"dokter"));
        ModulKlinik.setText(v,R.id.eKeperluan,ModulKlinik.getString(c,"keperluan"));
        idpelanggan=ModulKlinik.getString(c,"idpasien");
        iddokter=ModulKlinik.getString(c,"iddokter");
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
        }else if(id==2){
            return new TimePickerDialog(this, date1, hour, minute,android.text.format.DateFormat.is24HourFormat(this));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.eTanggal,ModulKlinik.setDatePickerNormal(thn,bln+1,day));
            tahun=thn;bulan=bln;hari=day;
        }
    };

    private TimePickerDialog.OnTimeSetListener date1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ModulKlinik.setText(v,R.id.eJam,ModulKlinik.setTimePickerNormal(hourOfDay,minute));
            jamq=hourOfDay;menit=minute;
        }
    };

    public void tanggal(View view) {
        showDialog(1);
    }

    public void jam(View view) {
        showDialog(2);
    }

    public void pasien(View view){
        Intent i = new Intent(this,MenuPilihDuaKlinik.class);
        i.putExtra("type","pelanggan");
        startActivityForResult(i,100);
    }

    public void dokter(View view){
        Intent i = new Intent(this,MenuPilihDuaKlinik.class);
        i.putExtra("type","teknisi");
        startActivityForResult(i,300);
    }

    private void tambahlimit(){
        boolean status = MainActivity.status;
        if (!status){
            int batas = ModulKlinik.strToInt(config.getCustom("janji","1"))+1;
            config.setCustom("janji",ModulKlinik.intToStr(batas));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");
            setPasien();
        } else if (resultCode == 300) {
            iddokter = data.getStringExtra("idteknisi");
            setDokter();
        }
    }
    public void setDokter(){
        Cursor c = db.sq(ModulKlinik.selectwhere("tbldokter")+ModulKlinik.sWhere("iddokter",iddokter));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.eDokter,ModulKlinik.getString(c,"dokter"));
    }
    public void setPasien(){
        Cursor c = db.sq(ModulKlinik.selectwhere("tblpasien")+ModulKlinik.sWhere("idpasien",idpelanggan));
        c.moveToNext();
        ModulKlinik.setText(v,R.id.ePasien,ModulKlinik.getString(c,"pasien"));
    }

    private void simpan(){
        String dokter = ModulKlinik.getText(v,R.id.eDokter);
        String pasien = ModulKlinik.getText(v,R.id.ePasien);
        String keperluan = ModulKlinik.getText(v,R.id.eKeperluan);
        String jam = ModulKlinik.getText(v,R.id.eJam);
        String tgl = ModulKlinik.getText(v,R.id.eTanggal);

        if (!TextUtils.isEmpty(dokter)&& !TextUtils.isEmpty(pasien)&& !TextUtils.isEmpty(keperluan) && !TextUtils.isEmpty(jam) && !TextUtils.isEmpty(tgl)){
            String isi [] ={iddokter,idpelanggan,tgl,jam,keperluan};
            String q = ModulKlinik.splitParam("INSERT INTO tbljanji (iddokter,idpasien,tgljanji,jamjanji,keperluan) VALUES (?,?,?,?,?)",isi);
            if (db.exc(q)){
                ModulKlinik.showToast(this,"Berhasil menyimpan data");
                tambahlimit();
                finish();
                startActivity(new Intent(this,Tambah_Janji_Klinik.class));
            }else{
                ModulKlinik.showToast(this,"Gagal menyimpan data");
            }

        }else{
            ModulKlinik.showToast(this,"Isi data dengan lengkap dan benar");
        }
    }

    private void ubah(){
        String dokter = ModulKlinik.getText(v,R.id.eDokter);
        String pasien = ModulKlinik.getText(v,R.id.ePasien);
        String keperluan = ModulKlinik.getText(v,R.id.eKeperluan);
        String jam = ModulKlinik.getText(v,R.id.eJam);
        String tgl = ModulKlinik.getText(v,R.id.eTanggal);

        if (!TextUtils.isEmpty(dokter)&& !TextUtils.isEmpty(pasien)&& !TextUtils.isEmpty(keperluan) && !TextUtils.isEmpty(jam) && !TextUtils.isEmpty(tgl)){
            String isi [] ={iddokter,idpelanggan,tgl,jam,keperluan,idjanji};
            String q = ModulKlinik.splitParam("UPDATE tbljanji SET iddokter=?,idpasien=?,tgljanji=?,jamjanji=?,keperluan=? WHERE idjanji=?   ",isi);
            if (db.exc(q)){
                ModulKlinik.showToast(this,"Berhasil menyimpan data");
                finish();
            }else{
                ModulKlinik.showToast(this,"Gagal menyimpan data");
            }

        }else{
            ModulKlinik.showToast(this,"Isi data dengan lengkap dan benar");
        }
    }

    public void simpan(View view) {
        if(type.equals("tambah")){
            simpan();
        }else{
            ubah();
        }

    }
}