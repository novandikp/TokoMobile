package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Laporan_Booking_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    View v;
    ConfigSalon config;
    DatabaseSalon db;
    ArrayList arrayList = new ArrayList();
    String dari="", ke="", type;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_booking_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appbar = (Toolbar) findViewById(R.id.toolBar);
        String title = "judul";

        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseSalon(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");

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
//                if (type.equals("janji")) {
                    arrayList.clear();
                    loadList(eCari.getText().toString());
                }
            //}
        });

        List<String> categories = new ArrayList<>();
//        if (type.equals("janji")) {
            title = ("Laporan Booking");
            loadList("");
            categories = new ArrayList<String>();
            categories.add("Semua");
            categories.add("Sudah Datang");
            categories.add("Belum Datang");
//        }

        Spinner spinner = (Spinner) findViewById(R.id.Spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
//        FunctionSalon.btnBack(title, getSupportActionBar());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(type.equals("janji")){
                    loadList(FunctionSalon.getText(v,R.id.eCari));
                }
//            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        dari = FunctionSalon.setDatePicker(year,month+1,day) ;
        ke = FunctionSalon.setDatePicker(year,month+1,day) ;
        String now = FunctionSalon.setDatePickerNormal(year,month+1,day) ;
        FunctionSalon.setText(v,R.id.eKe,now) ;
        FunctionSalon.setText(v,R.id.eDari,now) ;
    }

    public void loadList(String cari) {
        String item = FunctionSalon.getSpinnerItem(v, R.id.Spinner);
        String status = "";
        String q = "";
        if (item.equals("Semua")) {
            q = QuerySalon.selectwhere("qjanji")+QuerySalon.sLike("(pelanggan",cari) + " OR " +QuerySalon.sLike("telppel",cari) + ") AND "+QuerySalon.sBetween("tgljanji",dari,ke) + QuerySalon.sOrderASC("tgljanji");
        } else if (item.equals("Sudah Datang")) {
            status = "1";
            q = QuerySalon.selectwhere("qjanji")+QuerySalon.sWhere("status",status)+" AND ("+QuerySalon.sLike("pelanggan",cari) + " OR " +QuerySalon.sLike("telppel",cari) + ") AND "+QuerySalon.sBetween("tgljanji",dari,ke) + QuerySalon.sOrderASC("tgljanji");
        } else if (item.equals("Belum Datang")) {
            status = "0";
            q = QuerySalon.selectwhere("qjanji")+QuerySalon.sWhere("status",status)+" AND ("+QuerySalon.sLike("pelanggan",cari) + " OR " +QuerySalon.sLike("telppel",cari) + ") AND "+QuerySalon.sBetween("tgljanji",dari,ke) + QuerySalon.sOrderASC("tgljanji");
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanJanji(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0) {
            while (c.moveToNext()) {
                String idjanji = FunctionSalon.getString(c, "idjanji");
                String pel = FunctionSalon.getString(c, "pelanggan");
                String tgljanji = FunctionSalon.getString(c, "tgljanji");
                String jamjanji = FunctionSalon.getString(c, "jamjanji");
                String stat = FunctionSalon.getString(c, "status");
                String idpelanggan = FunctionSalon.getString(c, "idpelanggan");
                String telp = FunctionSalon.getString(c, "telppel");

                String campur = idjanji + "__" + pel + "__" + FunctionSalon.dateToNormal(tgljanji) + "__" + FunctionSalon.timeToNormal(jamjanji) + "__" + stat + "__" + idpelanggan + "__" + telp;
                arrayList.add(campur);
            }
            FunctionSalon.setText(v, R.id.tValue1, "");
            FunctionSalon.setText(v, R.id.tValue2, "");
            FunctionSalon.setText(v, R.id.tValue3, "");
            FunctionSalon.setText(v, R.id.tjumlah, "");
            FunctionSalon.setText(v, R.id.tjumlah2, "Jumlah Data : " + FunctionSalon.intToStr(FunctionSalon.getCount(c)));
        } else {
            FunctionSalon.setText(v, R.id.tValue1, "");
            FunctionSalon.setText(v, R.id.tValue2, "");
            FunctionSalon.setText(v, R.id.tValue3, "");
            FunctionSalon.setText(v, R.id.tjumlah, "");
            FunctionSalon.setText(v, R.id.tjumlah2, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcelSalon.class);
        i.putExtra("type","janji") ;
        startActivity(i);
    }
    @Override
    protected void onResume() {
        super.onResume();

//        if(type.equals("janji")){
            loadList("");
//        }
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filter(){
//        if(type.equals("janji")){
            loadList(FunctionSalon.getText(v,R.id.eCari));
        }
    //}

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
            FunctionSalon.setText(v, R.id.eDari, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            dari = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.eKe, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            ke = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };
    //end date time picker
}



class AdapterLaporanJanji extends RecyclerView.Adapter<AdapterLaporanJanji.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanJanji(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_janji_salon, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, nama, jam, status, opt;
        ConstraintLayout cjanji;

        public ViewHolder(View view) {
            super(view);

            tanggal = (TextView) view.findViewById(R.id.teTanggal);
            nama = (TextView) view.findViewById(R.id.tePelanggan);
            jam = (TextView) view.findViewById(R.id.teJam);
            status = (TextView) view.findViewById(R.id.teStatus);
            opt = (TextView) view.findViewById(R.id.tvOpt);
            cjanji = (ConstraintLayout) view.findViewById(R.id.cJanji);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final String[] row = data.get(i).split("__");
        String stat;
        if (row[4].equals("0")){
            stat = "Belum Datang";
        } else {
            stat = "Sudah Datang";
        }

        viewHolder.nama.setText("Nama : "+row[1]);
        viewHolder.tanggal.setText("Tanggal Booking : "+row[2]);
        viewHolder.jam.setText("Jam Booking : "+row[3]);
        viewHolder.status.setText("No. Telepon : "+row[6]+"\n"+"Status : "+stat);
        viewHolder.opt.setVisibility(View.INVISIBLE);
        viewHolder.cjanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String notelp = row[6].substring(1);
                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_list_salon);

                ConstraintLayout wa = (ConstraintLayout) dialog.findViewById(R.id.clWA);
                ConstraintLayout tp = (ConstraintLayout) dialog.findViewById(R.id.clTP);
                ConstraintLayout ps = (ConstraintLayout) dialog.findViewById(R.id.clPS);

                wa.setEnabled(true);
                tp.setEnabled(true);
                ps.setEnabled(true);

                wa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Intent sendIntent =new Intent("android.intent.action.MAIN");
                            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                            sendIntent.putExtra("jid", "62"+notelp +"@s.whatsapp.net");
                            sendIntent.setPackage("com.whatsapp");
                            c.startActivity(sendIntent);
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(c,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+"+62"+notelp));
                        c.startActivity(intent);
                    }
                });

                ps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "+62"+notelp, null)));
                    }
                });
                dialog.show();
            }
        });
    }
}