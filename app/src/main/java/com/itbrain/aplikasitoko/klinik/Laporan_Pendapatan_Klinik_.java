package com.itbrain.aplikasitoko.klinik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Pendapatan_Klinik_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    ArrayList arrayTeknisi = new ArrayList();
    DatabaseKlinik db;
    int year,day,month;
    Calendar calendar;
    View v;
    double total=0;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pendapatan_klinik_);
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
        type = getIntent().getStringExtra("type");
//        if (type.equals("pendapatan")) {
            getPendapatan("");

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
                    getPendapatan(a);

                }
            });
//        }
        }

    public void getPendapatan(String cari) {
        arrayList.clear();
        total=0;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterBagiHasil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("view_detailperiksa")+ " flagperiksa=2 AND "+ModulKlinik.sBetween("tglperiksa",ModulKlinik.getText(v,R.id.tglawal),ModulKlinik.getText(v,R.id.tglakhir))+" AND "+ModulKlinik.sLike("pasien",cari)+ModulKlinik.sOrderASC("fakturperiksa");
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String harga = ModulKlinik.getString(c,"biaya");
            if (!ModulKlinik.getString(c,"iddokter").equals("1")){
                double potongan = ModulKlinik.strToDouble(ModulKlinik.getString(c,"bagihasil"))/100;
                harga=ModulKlinik.doubleToStr(ModulKlinik.strToDouble(harga)*potongan);
            }

            total+=ModulKlinik.strToDouble(harga);

            String campur = ModulKlinik.getString(c,"idperiksa")+"__"+ModulKlinik.getString(c,"fakturperiksa") + "__" + ModulKlinik.getString(c,"tglperiksa")+ "__" + ModulKlinik.getString(c,"pasien")+ "__" + ModulKlinik.getString(c,"jasa")+"__" + ModulKlinik.getString(c,"keterangan")+"__" + harga+"__pendapatan";
            arrayList.add(campur);
        }
        ModulKlinik.setText(v,R.id.tTotal,ModulKlinik.removeE(ModulKlinik.doubleToStr(total)));
        adapter.notifyDataSetChanged();
    }

    private void setText() {
        arrayTeknisi.clear();
        Spinner spinner = (Spinner) findViewById(R.id.sTeknisi) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayTeknisi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(ModulKlinik.select("tbldokter"));
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arrayTeknisi.add(ModulKlinik.getString(c,"dokter"));
            }
        }else{
            arrayTeknisi.add("Belum ada Data");
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
            ModulKlinik.setText(v, R.id.tglawal, ModulKlinik.setDatePickerNormal(thn, bln + 1, day));

            getPendapatan("");

            ModulKlinik.setText(v,R.id.tTotal,"Rp."+ModulKlinik.removeE(ModulKlinik.doubleToStr(total)));
    }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.tglakhir,ModulKlinik.setDatePickerNormal(thn,bln+1,day));

                getPendapatan("");

            ModulKlinik.setText(v,R.id.tTotal,ModulKlinik.removeE(ModulKlinik.doubleToStr(total)));
        }
    };

    public void tglawal(View view) {
        showDialog(1);
    }

    public void tglakhir(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i= new Intent(Laporan_Pendapatan_Klinik_.this,MenuExportExcelKlinik.class);
        i.putExtra("type","pendapatan");
        startActivity(i);
    }
}

class AdapterPendapatan extends RecyclerView.Adapter<AdapterPendapatan.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPendapatan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pendapatan_klinik, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.tvOpt.setText(row[2]);
        holder.alamat.setText("Nama Pasien : "+row[3]);
        holder.notelp.setText(row[4]);
        holder.Ket.setText("Keterangan : "+ row[5]);
        holder.Penda.setText("Pendapatan : "+ModulKlinik.removeE(row[6]));
        if (row[7].equals("dokter")){
            holder.print.setVisibility(View.GONE);
        }else{
            holder.print.setVisibility(View.VISIBLE);
        }

        holder.print.setTag(row[0]);
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = holder.print.getTag().toString();
                Intent i = new Intent(c,MenuCetakKLinik.class);
                i.putExtra("idorder",id);
                i.putExtra("type","laporan");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt,Ket,Penda;
        CardView cv;
        ImageView print;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tgl);
            Ket = itemView.findViewById(R.id.tvKet);
            Penda = itemView.findViewById(R.id.tvPendapatan);
            print = itemView.findViewById(R.id.print);


        }
    }
}