package com.itbrain.aplikasitoko.klinik;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Periksa_Klinik_ extends AppCompatActivity {
    DatabaseKlinik db;
    ArrayList arrayList = new ArrayList();
    View v;
    int year,month,day;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_periksa_klinik);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DatabaseKlinik(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulKlinik.getDate("dd/MM/yyyy");
        ModulKlinik.setText(v, R.id.tglawal, tgl);
        ModulKlinik.setText(v, R.id.tglakhir, tgl);
        getPeriksa("");

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();
                getPeriksa(a);

            }
        });
    }
    public void getPeriksa(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPeriksa(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulKlinik.selectwhere("view_detailperiksa") +ModulKlinik.sLike("pasien",cari)+" AND " +ModulKlinik.sBetween("tglperiksa",ModulKlinik.getText(v,R.id.tglawal),ModulKlinik.getText(v,R.id.tglakhir))+ " ORDER BY pasien ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idperiksa")+"__"+ModulKlinik.getString(c,"pasien") + "__" + ModulKlinik.getString(c,"tglperiksa")+ "__" + ModulKlinik.getString(c,"umurperiksa")+ "__" + ModulKlinik.getString(c,"jasa") + "__" + ModulKlinik.getString(c,"keterangan")+ "__" + ModulKlinik.getString(c,"dokter");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
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
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.tglawal,ModulKlinik.setDatePickerNormal(thn,bln+1,day));
            getPeriksa("");
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.tglakhir,ModulKlinik.setDatePickerNormal(thn,bln+1,day));
            getPeriksa("");
        }
    };

    public void tglawal(View view) {
        showDialog(1);
    }

    public void tglakhir(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i= new Intent(this,MenuExportExcelKlinik.class);
        i.putExtra("type","periksa");
        startActivity(i);
    }
}
class AdapterLaporanPeriksa extends RecyclerView.Adapter<AdapterLaporanPeriksa.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public AdapterLaporanPeriksa(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_periksa, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");

        holder.nama.setText(row[1]);
        holder.kelamin.setText(row[2]);
        holder.umur.setText("Umur Periksa : "+row[3]);
        holder.alamat.setText(row[4]);
        holder.notelp.setText("Keterangan : "+row[5]);
        holder.dokter.setText("Diperiksa oleh : "+row[6]);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, notelp, umur,kelamin,dokter;

        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp = (TextView) itemView.findViewById(R.id.tNo);
            umur = (TextView) itemView.findViewById(R.id.tvUmur);
            kelamin = (TextView) itemView.findViewById(R.id.tvKelamin);
            dokter = itemView.findViewById(R.id.tvDokter);

        }
    }
    

}