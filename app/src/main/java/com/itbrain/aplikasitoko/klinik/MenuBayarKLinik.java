package com.itbrain.aplikasitoko.klinik;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

public class MenuBayarKLinik extends AppCompatActivity {
    DatabaseKlinik db;
    String idorder;
    View v;
    ModulKlinik config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bayar_klinik);
        db= new DatabaseKlinik(this);
        v=this.findViewById(android.R.id.content);
        config= new ModulKlinik(getSharedPreferences("config",MODE_PRIVATE));
        idorder=getIntent().getStringExtra("idorder");
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
                double kembali = ModulKlinik.strToDouble(ModulKlinik.getText(v,R.id.eBayar))-ModulKlinik.strToDouble(ModulKlinik.unNumberFormat(ModulKlinik.getText(v,R.id.eTotal)));
                ModulKlinik.setText(v,R.id.eKembali,ModulKlinik.removeE(ModulKlinik.doubleToStr(kembali)));
            }
        });

    }

    protected void setText(){
        String q=ModulKlinik.selectwhere("tblperiksa")+ModulKlinik.sWhere("idperiksa",idorder);
        Cursor c= db.sq(q);
        c.moveToNext();
        ModulKlinik.setText(v,R.id.eFaktur,ModulKlinik.getString(c,"fakturperiksa"));
        ModulKlinik.setText(v,R.id.eTotal,ModulKlinik.removeE(ModulKlinik.getString(c,"total")));
        ModulKlinik.setText(v,R.id.eKembali,ModulKlinik.removeE(ModulKlinik.getString(c,"kembali")));
        ModulKlinik.setText(v,R.id.eBayar,"0");
    }

    public void print(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("Apakah anda ingin mencetak struk orderan ini ?")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MenuBayarKLinik.this,MenuCetakKLinik.class);
                        i.putExtra("idorder",idorder);
                        i.putExtra("type","bayar");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MenuBayarKLinik.this,AplikasiKlinik_Menu_Transaksi.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.setTitle("Cetak Struk");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("Apakah anda ingin membatalkan pembayaran ini ?")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MenuBayarKLinik.this,AplikasiKlinik_Menu_Transaksi.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.setTitle("Pembatalan pembayaran");
        dialog.show();
    }

    private void tambahlimit(){
        boolean status = MainActivity.status;
        if (!status){
            int batas = ModulKlinik.strToInt(config.getCustom("periksa","1"))+1;
            config.setCustom("periksa",ModulKlinik.intToStr(batas));
        }

    }

    public void bayar(View view){
        String bayar = ModulKlinik.getText(v,R.id.eBayar);
        String kembali=ModulKlinik.unNumberFormat(ModulKlinik.getText(v,R.id.eKembali));

        if (ModulKlinik.strToInt(bayar)!=0 && ModulKlinik.strToInt(kembali)>=0){
            String[] pp = {bayar,kembali,"2",idorder} ;
            String qq = ModulKlinik.splitParam("UPDATE tblperiksa SET bayar=?,kembali=?,flagperiksa=? WHERE idperiksa=?   ",pp) ;
            db.exc(qq);
            tambahlimit();
            print();

        }else{
            ModulKlinik.showToast(this,"Uang Pembayaran masih belum cukup");
        }
    }
}
