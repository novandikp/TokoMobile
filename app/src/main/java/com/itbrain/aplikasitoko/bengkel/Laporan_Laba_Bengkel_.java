package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Laba_Bengkel_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    ArrayList arrayTeknisi = new ArrayList();
    ArrayList arrayId = new ArrayList();
    Database_Bengkel_ db;
    int year, day, month;
    Calendar calendar;
    View v;
    double total = 0;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_laba_bengkel_);
        ImageButton imageButton = findViewById(R.id.kembali29);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulBengkel.getDate("dd/MM/yyyy");
        ModulBengkel.setText(v, R.id.tglawal3, tgl);
        ModulBengkel.setText(v, R.id.tglakhir3, tgl);
        type = getIntent().getStringExtra("type");
        if (type.equals("laba")) {
//            getSupportActionBar().setTitle("Laporan Laba");
            getLaba("");
            ModulBengkel.setText(v, R.id.tTotal, "Rp." + ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
            final EditText eCari = (EditText) findViewById(R.id.etlab);
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
                    getLaba(a);

                }
            });
        }
    }

    public void getLaba(String cari) {
        arrayList.clear();
        total = 0;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView6);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaba(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qdetailjual") + " idkategori!=1 AND statusbayar='lunas' AND " + ModulBengkel.sBetween("tglorder", ModulBengkel.getText(v, R.id.tglawal3), ModulBengkel.getText(v, R.id.tglakhir3)) + " AND " + ModulBengkel.sLike("pelanggan", cari) + ModulBengkel.sOrderASC("faktur");
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String harga = ModulBengkel.getString(c, "hargajual");
            String idkategori = ModulBengkel.getString(c, "idkategori");
            if (idkategori.equals("1")) {
                double potongan = ModulBengkel.strToDouble(ModulBengkel.getString(c, "stok")) / 100;
                harga = ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga) * potongan);
            }
            total += ModulBengkel.strToDouble(ModulBengkel.getString(c, "laba"));

            String campur = ModulBengkel.getString(c, "idkategori") + "__" + ModulBengkel.getString(c, "faktur") + "__" + ModulBengkel.getString(c, "pelanggan") + "__" + ModulBengkel.getString(c, "nopol") + "__" + ModulBengkel.getString(c, "barang") + "__" + harga + "__" + ModulBengkel.getString(c, "laba");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v, R.id.tglawal3, ModulBengkel.setDatePickerNormal(thn, bln + 1, day));
            if (type.equals("laba")) {
                getLaba(ModulBengkel.getText(v, R.id.etlab));
            }
            ModulBengkel.setText(v, R.id.tTotal, "Rp." + ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v, R.id.tglakhir3, ModulBengkel.setDatePickerNormal(thn, bln + 1, day));
            if (type.equals("laba")) {
                {
                    getLaba(ModulBengkel.getText(v, R.id.etlab));
                }
                ModulBengkel.setText(v, R.id.tTotal, "Rp." + ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
            }
        }

    };


    public void tglakhir(View view) {showDialog(2);}

    public void tglawal(View view) {  showDialog(1);}

    public void export(View view) {
        Intent i = new Intent(Laporan_Laba_Bengkel_.this, MenuExportExcelBengkel.class);
        i.putExtra("type", type);
        startActivity(i);
    };
}

//    public void tglawal(View view) { showDialog(1);
//    }
//
//    public void tglakhir(View view) {showDialog(2);
//    }
//
//    public void export(View view) {
//        Intent i = new Intent(Laporan_Laba_Bengkel_.this, MenuExportExcelBengkel.class);
//        i.putExtra("type", type);
//        startActivity(i);
//
//};
//
//}


class AdapterLaba extends RecyclerView.Adapter<AdapterLaba.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLaba(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_laba_bengkel, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");

        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[4]);
        if (row[6].contains("-")){
            String harga= row[6].replace("-","");
            holder.tvOpt.setText("Rugi : Rp."+ModulBengkel.removeE(harga));
        }else{
            holder.tvOpt.setText("Laba : Rp."+ModulBengkel.removeE(row[6]));
        }






    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tgl);

        }
    }
}


