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

public class MenuLaporanBagRental_Mobil extends AppCompatActivity {

    private Guideline guideline23;
    private EditText eCari;
    private ConstraintLayout cari;
    private Guideline guideline22;
    private TextView tglAwal;
    private TextView tglAkhir;
    private TextView textView19;
    DatabaseRentalMobil db ;
    View v;
    Calendar calendar;
    int year,month,day;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanbagrental_mobil);

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

        getRental("");
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
                getRental(a);
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
            getRental("");
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulRentalMobil.setText(v,R.id.tglAkhir,ModulRentalMobil.setDatePickerNormal(thn,bln+1,day));
        }
    };
    
    public void getRental(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapRentalMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("view_rentaldetail")+" flagrental>0 AND "+ModulRentalMobil.sLike("pelanggan",cari)+" AND "+ModulRentalMobil.sBetween("tglrental",tglAwal.getText().toString(),tglAkhir.getText().toString());
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idrentaldetail")+"__"+ModulRentalMobil.getString(c,"mobil") + "__" + ModulRentalMobil.getString(c,"plat")+ "__" + ModulRentalMobil.getString(c,"tahunkeluaran")+ "__"
                    + ModulRentalMobil.getString(c,"tglmulai") +"__"+ModulRentalMobil.getString(c,"tglselesai") +"__" + ModulRentalMobil.getString(c,"jumlahhari") +"__"+ModulRentalMobil.getString(c,"hargarental")+"__"+ModulRentalMobil.getString(c,"pelanggan");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, MenuExportExcel_Mobil.class);
        i.putExtra("type","rental");
        startActivity(i);
    }
}

class AdapterLapRentalMobil extends RecyclerView.Adapter<AdapterLapRentalMobil.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapRentalMobil(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_rental_mobil, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.nik.setText(row[2]+" - "+row[3]);
        holder.alamat.setText(row[4]+" - "+row[5]);
        double total = ModulRentalMobil.strToDouble(row[6])*ModulRentalMobil.strToDouble(row[7]);
        holder.notelp.setText(row[6]+" hari x "+ModulRentalMobil.removeE(row[7])+" = "+ModulRentalMobil.removeE(total));
        holder.pelanggan.setText("Pelanggan : "+row[8]);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,nik,pelanggan;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            nik= (TextView) itemView.findViewById(R.id.t2);
            nama= (TextView) itemView.findViewById(R.id.t1);
            alamat = (TextView) itemView.findViewById(R.id.t3);
            notelp=(TextView) itemView.findViewById(R.id.t4);
            pelanggan = itemView.findViewById(R.id.tPelanggan);

        }
    }
}
