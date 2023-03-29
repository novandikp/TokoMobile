package com.itbrain.aplikasitoko.tokosepatu;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Menu_Laporan_List_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arraystat = new ArrayList() ;
    int year,day,month;
    Calendar calendar;
    public static String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_list_sepatu);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        type="";
        type=getIntent().getStringExtra("type");
        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);


        if (type.equals("lappelanggan")) {
            getSupportActionBar().setTitle("Laporan Pelanggan");
            getPelanggan("");
            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
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
                    getPelanggan(a);

                }
            });
        }else if (type.equals("lapbarang")){
            getSupportActionBar().setTitle("Laporan Barang");
            getBarang("");

            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
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
                    getBarang(a);

                }
            });
        }else if (type.equals("hutang")){
            getSupportActionBar().setTitle("Laporan Hutang");
            getHutang("");

            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
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
                    getHutang(a);

                }
            });
        }
        else{
            setContentView(R.layout.activity_menu_laporanlistdua_sepatu);
            Toolbar toolbar1= findViewById(R.id.toolbar);
            setSupportActionBar(toolbar1);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Laporan Penjualan Per Pelanggan");
            setText();
            String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            ModulTokoSepatu.setText(v, R.id.tglAwal,date_v);
            ModulTokoSepatu.setText(v, R.id.tglakhir,date_v);

            TextView tglaw = findViewById(R.id.tglAwal);
            TextView tglak = findViewById(R.id.tglakhir);

            final String finalType2 = type;
            tglaw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(1);

                }
            });

            tglak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(2);

                }
            });


            Spinner spinner = (Spinner) findViewById(R.id.sStatus) ;
            final String finalType = type;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getLaporan("", finalType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            getLaporan("",type);

            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
            final String finalType1 = type;
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
                    getLaporan(a, finalType1);

                }
            });
        }

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
            return new DatePickerDialog(this, date1, year, month, day);
        }else if(id==2){
            return new DatePickerDialog(this, date2, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulTokoSepatu.setText(v,R.id.tglAwal,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
            if (type=="pendapatan"){
                getPendapatan("");
            }else{
                getLaporan("",type);
            }


        }
    };

    private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulTokoSepatu.setText(v,R.id.tglakhir,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
            if (type=="pendapatan"){
                getPendapatan("");
            }else{
                getLaporan("",type);
            }
        }
    };

    public String whereStat(String cari,String search){
        Spinner spinner = (Spinner) findViewById(R.id.sStatus) ;
        String id = spinner.getSelectedItem().toString();
        if(id.equals("Semua")){
            return ModulTokoSepatu.selectwhere("view_orderdetail") + ModulTokoSepatu.sLike(search,cari) +" AND "+ModulTokoSepatu.sBetween("tgljual",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir))+ " ORDER BY idjual ASC";
        } else {
            return ModulTokoSepatu.selectwhere("view_orderdetail")+ModulTokoSepatu.sWhere("status",id) +" AND "+ ModulTokoSepatu.sLike(search,cari) +" AND "+ModulTokoSepatu.sBetween("tgljual",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir)) + " ORDER BY idjual ASC";
        }
    }

    private void setText() {
        arraystat.add("Semua");
        arraystat.add("Tunai");
        arraystat.add("Utang");
        Spinner spinner = (Spinner) findViewById(R.id.sStatus) ;
        ArrayAdapter ok = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraystat);
        ok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ok);

    }

    private void getHutang(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterHutang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tblpelanggan") + "hutang!=0 AND "+ModulTokoSepatu.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idpelanggan")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"alamat")+ "__" + ModulTokoSepatu.getString(c,"notelp")+ "__" + ModulTokoSepatu.getString(c,"hutang");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapPel(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tblpelanggan")  +" idpelanggan != 1 AND "+ModulTokoSepatu.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idpelanggan")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"alamat")+ "__" + ModulTokoSepatu.getString(c,"notelp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapBar(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tblbarang") + ModulTokoSepatu.sLike("barang",cari) + " ORDER BY barang ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idbarang")+"__"+ModulTokoSepatu.getString(c,"barang") + "__" + ModulTokoSepatu.getString(c,"deskripsi")+ "__" + ModulTokoSepatu.getString(c,"stokbarang");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getLaporan(String cari,String search){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapPenjualan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = whereStat(cari,search);
        Cursor c = db.sq(q) ;
        double total =0;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"fakturbayar")+"__"+ModulTokoSepatu.getString(c,"tgljual") + "__" + ModulTokoSepatu.getString(c,"pelanggan")+ "__" + ModulTokoSepatu.getString(c,"barang")+ "__" + ModulTokoSepatu.getString(c,"jumlah")+ "__" + ModulTokoSepatu.getString(c,"hargajual")+ "__" + ModulTokoSepatu.getString(c,"total")+ "__" + ModulTokoSepatu.getString(c,"ukuran")+ "__" + ModulTokoSepatu.getString(c,"idjual");
            arrayList.add(campur);
            double item=ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"jumlah"))*ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"hargajual"));
            total+=item;

        }

        adapter.notifyDataSetChanged();
        ModulTokoSepatu.setText(v,R.id.total,ModulTokoSepatu.removeE(total));
    }

    public void getPendapatan(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tbldetailjual") + ModulTokoSepatu.sLike("pelanggan",cari) +" AND "+ModulTokoSepatu.sBetween("tgljual",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir))+ " ORDER BY idjual ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"iddetailjual")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"tgljual")+ "__" + ModulTokoSepatu.getString(c,"total")+ "__" + ModulTokoSepatu.getString(c,"bayar")+ "__" + ModulTokoSepatu.getString(c,"kembali");
            arrayList.add(campur);

        }

        adapter.notifyDataSetChanged();
    }


    public void export(View view) {
        Intent i = new Intent(Menu_Laporan_List_Sepatu.this,Menu_Export_Excel_Sepatu.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}
class AdapterLapPel extends RecyclerView.Adapter<AdapterLapPel.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapPel(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_lapsatu_sepatu, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");

        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp;

        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);

        }
    }
}

class AdapterLapBar extends RecyclerView.Adapter<AdapterLapBar.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLapBar(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_lapbar_sepatu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi, tvStok;
        ;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDek);
            tvStok = (TextView) view.findViewById(R.id.tvStok);


        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        viewHolder.deskripsi.setText(row[2]);
        viewHolder.tvStok.setText("Jumlah Stok: "+row[3]);
        if (row[0].equals("laporanretur")){
            viewHolder.tvStok.setText(row[3]);
        }
    }
}


class AdapterLapPenjualan extends RecyclerView.Adapter<AdapterLapPenjualan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLapPenjualan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_lappenjualan_sepatu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur,tgljual, namapel, ketbarang;
        private ImageView print;
        ;


        public ViewHolder(View view) {
            super(view);

            faktur=view.findViewById(R.id.faktur);
            tgljual=view.findViewById(R.id.tgljual);
            namapel=view.findViewById(R.id.namapel);
            ketbarang=view.findViewById(R.id.ketBarang);
            print=view.findViewById(R.id.print);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");
        viewHolder.faktur.setText(row[0]);
        viewHolder.tgljual.setText(row[1]);
        viewHolder.namapel.setText(row[2]);
        String total = ModulTokoSepatu.doubleToStr(ModulTokoSepatu.strToDouble(row[4])*ModulTokoSepatu.strToDouble(row[5]));
        String ketBar= row[3]+" ("+ModulTokoSepatu.toUppercase(row[7])+") : "+row[4]+" x "+ModulTokoSepatu.removeE(row[5])+" = "+ModulTokoSepatu.removeE(total);
        viewHolder.ketbarang.setText(ketBar);
        viewHolder.print.setTag(row[8]);
        viewHolder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=viewHolder.print.getTag().toString();
                Intent i = new Intent(c,ActivityTransaksiBayarCetak_Sepatu.class);
                i.putExtra("idjual",id);
                i.putExtra("type","kembali");
                c.startActivity(i);
            }
        });


    }
}



