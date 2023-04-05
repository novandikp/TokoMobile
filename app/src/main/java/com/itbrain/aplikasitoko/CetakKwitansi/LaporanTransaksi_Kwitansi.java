package com.itbrain.aplikasitoko.CetakKwitansi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class LaporanTransaksi_Kwitansi extends AppCompatActivity {

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
        setContentView(R.layout.laporantransaksi_kwitansi);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        String q = "" ;
        if(TextUtils.isEmpty(cari)){
            q = "SELECT * FROM vtransaksi WHERE status>0 AND " + sBetween("tgltransaksi",dari,ke) + " LIMIT 30";
        } else {
            q = "SELECT * FROM vtransaksi WHERE status>0 AND (jasatransaksi LIKE '%"+cari+"%' OR penerima LIKE '%"+cari+"%')  AND " + sBetween("tgltransaksi",dari,ke) + " LIMIT 30";
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
                @SuppressLint("Range") String pelanggan = c.getString(c.getColumnIndex("penerima"));
                @SuppressLint("Range") String jasatransaksi = c.getString(c.getColumnIndex("jasatransaksi"));
                @SuppressLint("Range") String faktur = c.getString(c.getColumnIndex("faktur"));
                @SuppressLint("Range") String jumlah = c.getString(c.getColumnIndex("jumlah"));
                @SuppressLint("Range") String harga = c.getString(c.getColumnIndex("harga"));
                double total = Double.parseDouble(harga)*Double.parseDouble(jumlah) ;
                @SuppressLint("Range") String keter = c.getString(c.getColumnIndex("keterangan"));
                String ket;
                if (keter.equals("")){
                    ket = " -";
                } else {
                    ket = keter;
                }

                @SuppressLint("Range") String campur = faktur+"__"+jasatransaksi+"__"+pelanggan+"__"+ CariPelanggan_Kwitansi.removeE(jumlah)+" x "+ CariPelanggan_Kwitansi.removeE(harga)+" = "+ CariPelanggan_Kwitansi.removeE(String.valueOf(total)) +"__" +dateToNormal(c.getString(c.getColumnIndex("tgltransaksi")))+"__"+ket;
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
            dari =  setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            eKe.setText(MenuCetakTransaksi_Kwitansi.setDatePickerNormal(thn,bln+1,day));
            ke = setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker

    public void export(View view){
        Intent i = new Intent(this, ExportExcel_Kwitansi.class);
        i.putExtra("type","transaksi") ;
        startActivity(i);
    }

    public static String sBetween(String key, String v1, String v2){
        return key+" BETWEEN "+ quote(v1)+" AND "+ quote(v2) ;
    }

    public static String quote(String w){
        return "\'" + addSlashes(w) + "\'" ;
    }

    public static String addSlashes(String s) {
        s = s.replaceAll("\\\\", "\\\\\\\\");
        s = s.replaceAll("\\n", "\\\\n");
        s = s.replaceAll("\\r", "\\\\r");
        s = s.replaceAll("\\00", "\\\\0");
        s = s.replaceAll("'", "\"");
        return s;
    }

    public static String dateToNormal(String date){
        try {
            String b1 = date.substring(4) ;
            String b2 = b1.substring(2) ;

            String m = b1.substring(0,2) ;
            String d = b2.substring(0,2) ;
            String y = date.substring(0,4) ;
            return d+"/"+m+"/"+y ;
        }catch (Exception e){
            return "ini tanggal" ;
        }
    }

    public static String setDatePicker(int year , int month, int day) {
        String bln,hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return String.valueOf(year) + bln+hri;
    }
}

class AdapterLaporanTransaksi extends RecyclerView.Adapter<AdapterLaporanTransaksi.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanTransaksi(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_laporan_transaksi_kwitansi, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, nma, jumlah, tanggal;
        ConstraintLayout print;

        public ViewHolder(View view) {
            super(view);

            faktur = (TextView) view.findViewById(R.id.tNama);
            tanggal = (TextView) view.findViewById(R.id.tTanggal);
            nma = (TextView) view.findViewById(R.id.tHitung);
            jumlah = (TextView) view.findViewById(R.id.tBarang);
            print = (ConstraintLayout) view.findViewById(R.id.wPrinter);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.jumlah.setText(row[3] + "\nKeterangan : " +row[5]);
        viewHolder.nma.setText("Pelanggan : " + row[2]+"\nJasa : "+row[1]);
        viewHolder.tanggal.setText(row[4]);
        viewHolder.faktur.setText(row[0]);
        viewHolder.print.setTag(row[0]);

        viewHolder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, MenuCetak_Kwitansi.class);
                intent.putExtra("faktur", row[0]);
                c.startActivity(intent);
            }
        });
    }
}

