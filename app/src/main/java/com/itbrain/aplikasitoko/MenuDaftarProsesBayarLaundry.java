package com.itbrain.aplikasitoko;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MenuDaftarProsesBayarLaundry extends AppCompatActivity {
    View v;
    DatabaseLaundry db;
    String metodeBayar;
    Integer idpelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menudaftarprosesbayarlaundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String faktur = getIntent().getStringExtra("faktur");
        v=this.findViewById(android.R.id.content);
        db=new DatabaseLaundry(this);
        Modul.btnBack("Menu Bayar",getSupportActionBar());

        Cursor c=db.sq("SELECT * FROM tbllaundry WHERE faktur='"+faktur+"'");
        c.moveToNext();
        Modul.setText(v,R.id.edtFaktur,faktur);
//        Modul.setText(v,R.id.edtJumlahBayar,"0");
        Modul.setText(v,R.id.edtTotalBayar,Modul.removeE(Modul.getString(c,"total")));
        double kembali=0.0-Modul.strToDouble(Modul.unNumberFormat(Modul.getText(v,R.id.edtTotalBayar)));
        Modul.setText(v,R.id.edtKembali,Modul.removeE(Modul.doubleToStr(kembali)));
        idpelanggan=Modul.getInt(c,"idpelanggan");

        Spinner spMetode= (Spinner) findViewById(R.id.SpinnerBayar);
        spMetode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metodeBayar=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TextInputEditText eJumlahBayar=(TextInputEditText)findViewById(R.id.edtBayar);
//        eJumlahBayar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                double kembali=Modul.strToDouble(Modul.getText(v,R.id.edtBayar))-Modul.strToDouble(Modul.unNumberFormat(Modul.getText(v,R.id.edtTotalBayar)));
//                Modul.setText(v,R.id.edtKembali,Modul.removeE(Modul.doubleToStr(kembali)));
//            }
//        });
        EditText edtKembali=(EditText)findViewById(R.id.edtKembali);
        eJumlahBayar.addTextChangedListener(new NumberTextWatcherKembali(eJumlahBayar,new Locale("in","ID"),2,Modul.justRemoveE(Modul.getString(c,"total")),edtKembali));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void bayar(View view) {
        final String faktur=Modul.getText(v,R.id.edtFaktur);
        final String mBayar=metodeBayar;
        String total=Modul.unNumberFormat(Modul.getText(v,R.id.edtTotalBayar));
        String jBayar=Modul.unNumberFormat(Modul.getText(v,R.id.edtBayar));
        final String kembali=Modul.unNumberFormat(Modul.getText(v,R.id.edtKembali));
        String tglbayar=Modul.getDate("yyyyMMdd");

        if (mBayar.equals("Pilih Metode Bayar")){
            Toast.makeText(this, "Pilih metode bayar terlebih dahulu", Toast.LENGTH_SHORT).show();
        }else {
            if (mBayar.equals("Tunai")&&Integer.valueOf(total)>Modul.strToInt(jBayar)){
                Toast.makeText(this, "Uang bayar tidak cukup!", Toast.LENGTH_SHORT).show();
            }else if (mBayar.equals("Hutang")&&Double.valueOf(kembali)>0){
                Toast.makeText(this, "Uang cukup untuk pembayaran tunai", Toast.LENGTH_SHORT).show();
            }else if (idpelanggan==0&&mBayar.equals("Hutang")){
                Toast.makeText(this, "Sistem bayar hutang hanya berlaku untuk pelanggan terdaftar", Toast.LENGTH_SHORT).show();
            }else{
                final String q = "UPDATE tbllaundry SET " +
                        "bayar=" +jBayar+","+
                        "kembali=" +kembali+"," +
                        "tglbayar="+tglbayar+","+
                        "statuslaundry='Selesai'," +
                        "statusbayar='"+mBayar+"'" +
                        " WHERE faktur='"+faktur+"'";
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.create();
                builder.setMessage("Konfirmasi pesanan laundry "+faktur+" selesai?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (db.exc(q)){
                            if (mBayar.equals("Hutang")){
                                pelangganHutang(faktur,kembali);
                            }
                            Toast.makeText(MenuDaftarProsesBayarLaundry.this, "Berhasil", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(MenuDaftarProsesBayarLaundry.this,MenuCetaklaundry.class);;
                            i.putExtra("faktur",faktur);
                            finish();
                            startActivity(i);
                        }else {
                            Toast.makeText(MenuDaftarProsesBayarLaundry.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MenuDaftarProsesBayarLaundry.this, "Lakukan Pembayaran jika proses laundry sudah selesai", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        }
    }
    private void pelangganHutang(String faktur, String hutang){
        Cursor c=db.sq("SELECT * FROM tbllaundry WHERE faktur='"+faktur+"'");
        c.moveToNext();
        String idpelanggan=Modul.getString(c,"idpelanggan");
        db.exc("UPDATE tblpelanggan SET " +
                "hutang=hutang+"+Modul.removeMinus(hutang)+
                " WHERE idpelanggan="+idpelanggan);

    }
}
