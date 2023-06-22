package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Penjualan_Bengkel_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    Database_Bengkel_ db;
    int year, day, month;
    Calendar calendar;
    View v;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_penjualan_bengkel1);
        ImageButton imageButton = findViewById(R.id.Kembali1);

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
        ModulBengkel.setText(v, R.id.tglawal, tgl);
        ModulBengkel.setText(v, R.id.tglakhir, tgl);
        type = getIntent().getStringExtra("type");
        if (type.equals("jual")) {
//            getSupportActionBar().setTitle("Laporan Penjualan");
            getJual("");
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
                    getJual(a);

                }
            });
        }
    }
    public void getJual(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapJual(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qdetailjual")+ " statusbayar='lunas' AND idkategori!=1 AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.tglawal),ModulBengkel.getText(v,R.id.tglakhir))+" AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("faktur");
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idorder")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"jumlah")+ "__" + ModulBengkel.getString(c,"barang")+"__" + ModulBengkel.getString(c,"hargajual");
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
            ModulBengkel.setText(v,R.id.tglawal,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
            if (type.equals("jual")){
                getJual("");
            }
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.tglakhir,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
            if (type.equals("jual")){
                getJual("");
            }
        }
    };

    public void tglawal(View view) {
        showDialog(1);
    }

    public void tglakhir(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i= new Intent(Laporan_Penjualan_Bengkel_.this,MenuExportExcelBengkel.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}
class AdapterLapJual extends RecyclerView.Adapter<AdapterLapJual.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapJual(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(ModulBengkel.upperCaseFirst(row[4]));
        holder.tvOpt.setText("Rp."+ModulBengkel.removeE(row[5])+" x "+row[3]+"="+"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(ModulBengkel.strToDouble(row[5])*ModulBengkel.strToDouble(row[3]))));
        holder.print.setVisibility(View.VISIBLE);
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,MenuCetakBengkel.class);
                i.putExtra("idorder",holder.cv.getTag().toString());
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
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        ImageView print;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tgl);
            print=itemView.findViewById(R.id.print);

        }
    }
}