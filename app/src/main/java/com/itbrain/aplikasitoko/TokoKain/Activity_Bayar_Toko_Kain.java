package com.itbrain.aplikasitoko.TokoKain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;

import java.util.Locale;

public class Activity_Bayar_Toko_Kain extends AppCompatActivity {

    View v;
    DatabaseTokoKain db;


    TextInputEditText etFaktur;
    TextInputEditText etTotalbayar;
    TextInputEditText etKembali;
    TextInputEditText etJumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        ImageButton imageButton = findViewById(R.id.kembalicoy);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            etFaktur = findViewById(R.id.viewFaktur);
            etTotalbayar = findViewById(R.id.totalbayar);
            etKembali = findViewById(R.id.kembaliyahh);
            etJumlah = findViewById(R.id.jumlahbayar);

            String faktur = getIntent().getStringExtra("faktur");

            v=this.findViewById(android.R.id.content);
            db=new DatabaseTokoKain(this);
//            KumFunTokoKain.btnBack("Bayar",getSupportActionBar());

            Cursor c=db.sq("SELECT * FROM tblorder WHERE faktur='"+faktur+"'");
            Toast.makeText(this, ""+c.getCount(), Toast.LENGTH_SHORT).show();
            c.moveToNext();
            KumFunTokoKain.setText(v,R.id.viewFaktur,faktur);
            etFaktur.setText(faktur);
            KumFunTokoKain.setText(v,R.id.totalbayar,KumFunTokoKain.removeE(KumFunTokoKain.getString(c,"total")));
            int kembali=0-KumFunTokoKain.strToInt(KumFunTokoKain.unNumberFormat(KumFunTokoKain.getText(v,R.id.totalbayar)));
            etTotalbayar.setText(KumFunTokoKain.removeE(KumFunTokoKain.getString(c,"total")));
            KumFunTokoKain.setText(v,R.id.kembaliyahh,KumFunTokoKain.removeE(KumFunTokoKain.doubleToStr(kembali)));
            etKembali.setText(KumFunTokoKain.removeE(KumFunTokoKain.doubleToStr(kembali)));
      //    KumFunTokoKain.setText(v,R.id.edtjumlahbayar,"0");



            final EditText eJumlahBayar=findViewById(R.id.jumlahbayar);
            eJumlahBayar.addTextChangedListener(new NumberTextWatcher(eJumlahBayar,new Locale("in","ID"),2));
            eJumlahBayar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int kembali = KumFunTokoKain.strToInt(KumFunTokoKain.unNumberFormat(etJumlah.getText().toString()))-KumFunTokoKain.strToInt(KumFunTokoKain.unNumberFormat(etTotalbayar.getText().toString()));
                    etKembali.setText(KumFunTokoKain.removeE(KumFunTokoKain.intToStr(kembali)));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void bayar(View view) {
        final String faktur = KumFunTokoKain.getText(v, R.id.viewFaktur);
        String total = etTotalbayar.getText().toString();
        String jBayar = etJumlah.getText().toString();
        final String kembali = etKembali.getText().toString();
        String tglbayar = KumFunTokoKain.getDate("yyyyMMdd");

        if (TextUtils.isEmpty(total)) {
            Toast.makeText(this, "Masukkan toko", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(jBayar)) {
            Toast.makeText(this, "Masukkan bayar", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(kembali)) {
            Toast.makeText(this, "Masukkan kembali", Toast.LENGTH_SHORT).show();
        }

        double hasil = KumFunTokoKain.strToDouble(total);
        double bayar = KumFunTokoKain.strToDouble(jBayar);
        double kembalik = KumFunTokoKain.strToDouble(kembali);
        if (hasil>bayar) {
                Toast.makeText(this, "Uang bayar tidak cukup!", Toast.LENGTH_SHORT).show();
        }else {
            final String q = "UPDATE tblorder SET " +
                    "bayar=" + bayar + "," +
                    "kembali=" + kembalik + "," +
                    "tglbayar=" + tglbayar + "" +
                    " WHERE faktur='" + faktur + "'";
            Log.d("sqlupdate", "bayar: "+q);
            if (db.exc(q)){
                Toast.makeText(Activity_Bayar_Toko_Kain.this, "Berhasil", Toast.LENGTH_SHORT).show();
                open();
            }else {
                Toast.makeText(Activity_Bayar_Toko_Kain.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void open(){
        final String faktur = KumFunTokoKain.getText(v, R.id.viewFaktur);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Ingin Cetak struk ?");
        builder.setPositiveButton("cetak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Activity_Bayar_Toko_Kain.this, Activity_Bayar_Cetak_Toko_Kain.class) ;
                i.putExtra("faktur",faktur) ;
                startActivity(i);
            }
        });
        builder.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Activity_Bayar_Toko_Kain.this, Aplikasi_TokoKain_Menu_Utama_Toko_Kain.class) ;
                startActivity(i);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
