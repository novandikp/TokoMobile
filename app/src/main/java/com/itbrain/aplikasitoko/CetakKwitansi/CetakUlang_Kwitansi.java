package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CetakUlang_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db ;
    ArrayList arrayList = new ArrayList() ;
    String dari, ke ;
    Calendar calendar ;
    int year,month, day ;
    Button eDari, eKe;
    EditText eCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakulang_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this) ;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        eKe = (Button) findViewById(R.id.eKe);
        eDari = (Button) findViewById(R.id.eDari);

        eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                loadList(eCari.getText().toString());
            }
        });
        setText();
        loadList("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        dari = LaporanTransaksi_Kwitansi.setDatePicker(year,month+1,day) ;
        ke = LaporanTransaksi_Kwitansi.setDatePicker(year,month+1,day) ;
        String now = MenuCetakTransaksi_Kwitansi.setDatePickerNormal(year,month+1,day) ;
        eKe.setText(now);
        eDari.setText(now);
    }

    public void loadList(String cari){
        String q  ;
        if(TextUtils.isEmpty(cari)){
            q = "SELECT * FROM vtransaksi WHERE status>0 AND " + LaporanTransaksi_Kwitansi.sBetween("tgltransaksi",dari,ke) + " LIMIT 30";
        } else {
            q = "SELECT * FROM vtransaksi WHERE status>0 AND (jasatransaksi LIKE '%"+cari+"%' OR penerima LIKE '%"+cari+"%')  AND " + LaporanTransaksi_Kwitansi.sBetween("tgltransaksi",dari,ke) + " LIMIT 30";
        }
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanTransaksi(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String pelanggan = c.getString(c.getColumnIndex("penerima"));
                String jasatransaksi = c.getString(c.getColumnIndex("jasatransaksi"));
                String faktur = c.getString(c.getColumnIndex("faktur"));
                String jumlah = c.getString(c.getColumnIndex("jumlah"));
                String harga = c.getString(c.getColumnIndex("harga"));
                double total = Double.parseDouble(harga)*Double.parseDouble(jumlah) ;
                String keter = c.getString(c.getColumnIndex("keterangan"));
                String ket;
                if (keter.equals("")){
                    ket = " -";
                } else {
                    ket = keter;
                }

                String campur = faktur+"__"+jasatransaksi+"__"+pelanggan+"__"+CariJasa_Kwitansi.removeE(jumlah)+" x "+CariJasa_Kwitansi.removeE(harga)+" = "+CariJasa_Kwitansi.removeE(String.valueOf(total)) +"__" + LaporanTransaksi_Kwitansi.dateToNormal(c.getString(c.getColumnIndex("tgltransaksi")))+"__"+ket;
                arrayList.add(campur);
            }
        } else {
        }
        adapter.notifyDataSetChanged();
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filtertgl(){
        loadList(eCari.getText().toString());
    }

    //start date time picker
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
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            eDari.setText(MenuCetakTransaksi_Kwitansi.setDatePickerNormal(thn,bln+1,day));
            dari =  LaporanTransaksi_Kwitansi.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            eKe.setText(MenuCetakTransaksi_Kwitansi.setDatePickerNormal(thn,bln+1,day));
            ke = LaporanTransaksi_Kwitansi.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker
}