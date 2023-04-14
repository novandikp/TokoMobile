package com.itbrain.aplikasitoko.tokosepatu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Menu_Retur_Sepatu extends AppCompatActivity {
    String id;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    String hutang,idpelanggan;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    int year,day,month;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_retur_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Retur Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        id=getIntent().getStringExtra("id");
        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);




        String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulTokoSepatu.setText(v, R.id.tgl,date_v);

        setDetailKategori();
        setLimit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDetailKategori(){
        Cursor c = db.sq(ModulTokoSepatu.selectwhere("view_orderdetail")+ModulTokoSepatu.sWhere("iddetailjual",id));
        c.moveToNext();
        hutang= ModulTokoSepatu.getString(c,"hutang");
        idpelanggan = ModulTokoSepatu.getString(c,"idpelanggan");
        String faktur = ModulTokoSepatu.getString(c,"fakturbayar");
        String pelanggan = ModulTokoSepatu.getString(c,"pelanggan");
        String barang = ModulTokoSepatu.getString(c,"barang")+" ("+ModulTokoSepatu.getString(c,"ukuran")+")";
        String jumlah = "Jumlah Barang: "+ModulTokoSepatu.getString(c,"jumlah");
        ModulTokoSepatu.setText(v,R.id.tvPelanggan,faktur);
        ModulTokoSepatu.setText(v,R.id.pelanggan,pelanggan);
        ModulTokoSepatu.setText(v,R.id.barang,barang);
        ModulTokoSepatu.setText(v,R.id.jumlah,jumlah);
        ModulTokoSepatu.setText(v,R.id.retur,ModulTokoSepatu.getString(c,"jumlah"));
    }

    private void setLimit(){
        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tbldetailjual")+ModulTokoSepatu.sWhere("iddetailjual",id));
        c.moveToNext();
        final String limit=ModulTokoSepatu.getString(c,"jumlah");
        final EditText retur = findViewById(R.id.retur);
        retur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ModulTokoSepatu.strToInt(retur.getText().toString())>ModulTokoSepatu.strToInt(limit)||ModulTokoSepatu.strToInt(retur.getText().toString())<1){
                    retur.setText("1");
                }
            }
        });
    }


    private void setHutang(){
        hutang =ModulTokoSepatu.unNumberFormat( ModulTokoSepatu.removeE(hutang));
        String isi []={hutang,idpelanggan};
        String q = ModulTokoSepatu.splitParam("UPDATE tblpelanggan SET hutang =? WHERE idpelanggan=?   ",isi);
        db.exc(q);
    }

    private void getRetur(){
        String tanggal=ModulTokoSepatu.getText(v,R.id.tgl);
        String jumlah = ModulTokoSepatu.getText(v,R.id.retur);
        String iddetail = id;
        if(!TextUtils.isEmpty(iddetail) && !TextUtils.isEmpty(tanggal)  && !TextUtils.isEmpty(jumlah)){
            String[] p = {iddetail,tanggal,jumlah} ;
            String q = ModulTokoSepatu.splitParam("INSERT INTO tblretur (iddetailjual,tglretur,jumlahretur) values(?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                setHutang();
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
        getRetur();
    }

    public void tambah(View view) {
        int hasil= ModulTokoSepatu.strToInt(ModulTokoSepatu.getText(v,R.id.retur))+1;
        ModulTokoSepatu.setText(v,R.id.retur,ModulTokoSepatu.intToStr(hasil));
    }

    public void kurang(View view) {
        int hasil= ModulTokoSepatu.strToInt(ModulTokoSepatu.getText(v,R.id.retur))-1;
        ModulTokoSepatu.setText(v,R.id.retur,ModulTokoSepatu.intToStr(hasil));
    }
}
