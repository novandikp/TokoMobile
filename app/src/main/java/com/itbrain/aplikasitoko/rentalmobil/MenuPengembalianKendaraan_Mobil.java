package com.itbrain.aplikasitoko.rentalmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MenuPengembalianKendaraan_Mobil extends AppCompatActivity {

    private Guideline guideline23;
    private EditText eCari;
    private ConstraintLayout cari;
    private Guideline guideline22;
    private TextView tglAwal;
    private TextView tglAkhir;
    private TextView textView19;
    DatabaseRentalMobil db;
    View v;
    Calendar calendar;
    int year, month, day;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupengembaliankendaraan_mobil);
        guideline23
                = (Guideline) findViewById(R.id.guideline23);
        eCari
                = (EditText) findViewById(R.id.eCari);
        cari
                = (ConstraintLayout) findViewById(R.id.cari);

        guideline22
                = (Guideline) findViewById(R.id.guideline22);
        tglAwal
                = (TextView) findViewById(R.id.tglAwal);
        tglAkhir
                = (TextView) findViewById(R.id.tglAkhir);
        textView19
                = (TextView) findViewById(R.id.textView19);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseRentalMobil(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulRentalMobil.getDate("dd/MM/yyyy");
        tglAwal.setText(tgl);
        tglAkhir.setText(tgl);


        getPesanan("");
        tglAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        tglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString() ;
                arrayList.clear();
                getPesanan(a);
            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        }else if(id == 2){
            return new DatePickerDialog(this, date1, year, month, day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulRentalMobil.setText(v,R.id.tglAwal,ModulRentalMobil.setDatePickerNormal(thn,bln+1,day));
            getPesanan("");
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulRentalMobil.setText(v,R.id.tglAkhir,ModulRentalMobil.setDatePickerNormal(thn,bln+1,day));
        }
    };

    public void getPesanan(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPesananMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("view_rental")+" ( flagrental>0 AND flagrental<3) AND "+ModulRentalMobil.sLike("pelanggan",cari)+" AND "+ModulRentalMobil.sBetween("tglrental",tglAwal.getText().toString(),tglAkhir.getText().toString());
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idrental")+"__"+ModulRentalMobil.getString(c,"faktur") + "__" + ModulRentalMobil.getString(c,"tglrental")+ "__" + ModulRentalMobil.getString(c,"pelanggan")+ "__" + ModulRentalMobil.getString(c,"total");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }


}


class AdapterPilihPesananMobil extends RecyclerView.Adapter<AdapterPilihPesananMobil.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPesananMobil(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_kembali_mobil, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);
        holder.nama.setText("Faktur : "+row[1]);
        holder.nik.setText(row[2]);
        holder.alamat.setText("Nama Pelanggan : "+row[3]);
        holder.notelp.setText("Total : "+ModulRentalMobil.removeE(row[4]));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = holder.cv.getTag().toString();
                Intent i = new Intent(c, MenuDetailPengembalian_Mobil.class);
                i.putExtra("idrental",id);
                c.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,nik;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            nik= (TextView) itemView.findViewById(R.id.t2);
            nama= (TextView) itemView.findViewById(R.id.t1);
            alamat = (TextView) itemView.findViewById(R.id.t3);
            notelp=(TextView) itemView.findViewById(R.id.t4);

        }
    }
}


