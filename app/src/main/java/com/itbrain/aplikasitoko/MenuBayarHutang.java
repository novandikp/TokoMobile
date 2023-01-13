package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.bengkel.AplikasiBengkel_MenuTransaksi;
import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.util.Calendar;

public class MenuBayarHutang extends AppCompatActivity {
    Database_Bengkel_ db;
    String idpelanggan;
    View v;
    ModulBengkel config;
    Calendar calendar;
    int year,  day, month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bayar_hutang);
        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        config = new ModulBengkel(getSharedPreferences("config", MODE_PRIVATE));
        idpelanggan = getIntent().getStringExtra("idpelanggan");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        ModulBengkel.setText(v,R.id.eTanggal,ModulBengkel.getDate("dd/MM/yyyy"));

        setText();
        EditText bayar = findViewById(R.id.eBayar);

        bayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double kembali = ModulBengkel.strToDouble(ModulBengkel.getText(v,R.id.eBayar))-ModulBengkel.strToDouble(ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eTotal)));
                String totalkembali = ModulBengkel.removeE(ModulBengkel.doubleToStr(kembali));
                totalkembali=totalkembali.replace("-.","");
                ModulBengkel.setText(v,R.id.eKembali,totalkembali);
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
        String q=ModulBengkel.selectwhere("tblpelanggan")+ModulBengkel.sWhere("idpelanggan",idpelanggan);
        Cursor c= db.sq(q);
        c.moveToNext();
        ModulBengkel.setText(v,R.id.ePelanggan,ModulBengkel.getString(c,"pelanggan"));
        ModulBengkel.setText(v,R.id.eTotal,ModulBengkel.removeE(ModulBengkel.getString(c,"hutang")));
        ModulBengkel.setText(v,R.id.eKembali,"0");
        ModulBengkel.setText(v,R.id.eBayar,"0");
    }

    private void tambahlimit(){
        boolean status = AplikasiBengkel_MenuTransaksi.status;
        if (!status){
            int batas = ModulBengkel.strToInt(config.getCustom("hutang","1"))+1;
            config.setCustom("hutang",ModulBengkel.intToStr(batas));
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
            ModulBengkel.setText(v,R.id.eTanggal,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
        }
    };

    public void alert(){

        String bayar = ModulBengkel.getText(v,R.id.eBayar);

        if (ModulBengkel.strToInt(bayar)!=0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Simpan Data").setMessage("Apakah anda yakin simpan data pembayaran hutang ?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    byr();
                    finish();

                }
            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();



        }else{
            ModulBengkel.showToast(this,"Gagal Simpan");
        }

    }

    public void bayar(View view){
        alert();
    }

    public void byr(){
        String bayar = ModulBengkel.getText(v,R.id.eBayar);
        String tgl = ModulBengkel.getText(v,R.id.eTanggal);
        String hutang;
        String total =ModulBengkel.unNumberFormat(ModulBengkel.getText(v,R.id.eTotal)) ;
        if (ModulBengkel.strToDouble(total)<ModulBengkel.strToDouble(bayar)){
            hutang = total;
        }else{
            hutang = bayar;
        }


        String[] pp = {hutang,tgl,idpelanggan} ;
        String qq = ModulBengkel.splitParam("INSERT INTO tblhutang (bayarhutang,tglbayar,idpelanggan) VALUES (?,?,?) ",pp) ;
        db.exc(qq);
        tambahlimit();

    }


}