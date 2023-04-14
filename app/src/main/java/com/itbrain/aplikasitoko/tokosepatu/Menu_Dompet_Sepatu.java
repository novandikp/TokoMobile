package com.itbrain.aplikasitoko.tokosepatu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class Menu_Dompet_Sepatu extends AppCompatActivity {
    DatabaseTokoSepatu db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arraystat = new ArrayList();
    String saldo;
    int day,month,year;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dompet_sepatu);
        db= new DatabaseTokoSepatu(this);
        v= this.findViewById(android.R.id.content);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Laporan Keuangan");
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ModulTokoSepatu.setText(v, R.id.tglawal,date_v);
        ModulTokoSepatu.setText(v, R.id.tglakhir,date_v);
        setText();
        getDompet("");
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
                getDompet(a);
            }
        });

        Spinner sp = findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDompet("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setText() {
        arraystat.add("Semua");
        arraystat.add("Pemasukan");
        arraystat.add("Pengeluaran");
        Spinner spinner = (Spinner) findViewById(R.id.spinner) ;
        ArrayAdapter ok = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraystat);
        ok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ok);

    }

    public String whereStat(String cari){
        Spinner spinner = (Spinner) findViewById(R.id.spinner) ;
        int id = spinner.getSelectedItemPosition();
        if(id==0){
            return  ModulTokoSepatu.selectwhere("tbltransaksi")+" ("+ModulTokoSepatu.sLike("fakturtran",cari)+" OR "+ModulTokoSepatu.sLike("kettransaksi",cari)+") AND "+ModulTokoSepatu.sBetween("tgltransaksi",ModulTokoSepatu.getText(v,R.id.tglawal),ModulTokoSepatu.getText(v,R.id.tglakhir));
        }else if (id==1){
            return  ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("keluar","0")+ " AND ("+ModulTokoSepatu.sLike("fakturtran",cari)+" OR "+ModulTokoSepatu.sLike("kettransaksi",cari)+")"+" AND "+ModulTokoSepatu.sBetween("tgltransaksi",ModulTokoSepatu.getText(v,R.id.tglawal),ModulTokoSepatu.getText(v,R.id.tglakhir));
        }else{
            return  ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("masuk","0")+ " AND ("+ModulTokoSepatu.sLike("fakturtran",cari)+" OR "+ModulTokoSepatu.sLike("kettransaksi",cari)+")"+" AND "+ModulTokoSepatu.sBetween("tgltransaksi",ModulTokoSepatu.getText(v,R.id.tglawal),ModulTokoSepatu.getText(v,R.id.tglakhir));

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDompet(String id) {

        arrayList.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dompet) ;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterDompet(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String campur="";
        Cursor c = db.sq(whereStat(id));
        if (c.getCount()>0){
            while(c.moveToNext()){
                campur = ModulTokoSepatu.getString(c,"idtransaksi")+"__"+ModulTokoSepatu.getString(c,"tgltransaksi") + "__" + ModulTokoSepatu.getString(c,"fakturtran") + "__" + ModulTokoSepatu.getString(c,"kettransaksi") + "__" + ModulTokoSepatu.getString(c,"masuk")+ "__" + ModulTokoSepatu.getString(c,"keluar");

                arrayList.add(campur);
            }
        }

        adapter.notifyDataSetChanged();
        setSaldo();

    }

    public void setSaldo(){
        Cursor c= db.sq(ModulTokoSepatu.select("tbltransaksi"));
        if (c.getCount()==0){
            saldo="0";
        }else{
            c.moveToLast();
            saldo=ModulTokoSepatu.getString(c,"saldo");
        }
        ModulTokoSepatu.setText(v,R.id.saldo,"Saldo: "+ModulTokoSepatu.removeE(saldo));
    }

    public void export(View view) {
        Intent i = new Intent(this,Menu_Export_Excel_Sepatu.class);
        i.putExtra("type","keuangan");
        startActivity(i);
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
            ModulTokoSepatu.setText(v,R.id.tglawal,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
            getDompet("");

        }
    };

    private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulTokoSepatu.setText(v,R.id.tglakhir,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
            getDompet("");

        }
    };

    public void tglawal(View view){
        showDialog(1);
    }

    public void tglakhir(View view){
        showDialog(2);
    }
}

class AdapterDompet extends RecyclerView.Adapter<AdapterDompet.ViewHolder>{
    ArrayList <String> data;
    Context c;

    public  AdapterDompet(Context a, ArrayList<String>kota){
        this.data=kota;
        c=a;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi_sepatu,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String[] row = data.get(position).split("__");
        holder.hapus.setTag(row[0]);
        holder.no.setText("No Transaksi : "+row[0]);
        holder.tgl.setText(row[1]);
        holder.faktur.setText(row[2]);
        holder.ket.setText(row[3]);

        final DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
        Cursor b= db.sq(ModulTokoSepatu.select("tbltransaksi"));
        b.moveToLast();
        String idlast= ModulTokoSepatu.getString(b,"idtransaksi");
        if (row[0].equals(idlast)){
            holder.hapus.setVisibility(View.VISIBLE);
        }
        if (row[4].equals("0")){
            holder.uang.setText("Keluar : "+ModulTokoSepatu.removeE(row[5]));
        }else{
            holder.uang.setText("Masuk : "+ModulTokoSepatu.removeE(row[4]));
        }
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = holder.hapus.getTag().toString();


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.exc("DELETE FROM tbltransaksi where idtransaksi="+id);
                                ((Menu_Dompet_Sepatu)c).getDompet("");
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Hapus Data");
                alert.show();



            }
        });






    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no,tgl,faktur,ket,uang;
        ImageView hapus;
        public ViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.no);
            tgl= itemView.findViewById(R.id.tgl);
            faktur = itemView.findViewById(R.id.faktur);
            ket=itemView.findViewById(R.id.ket);
            uang=itemView.findViewById(R.id.uang);
            hapus= itemView.findViewById(R.id.imageView2);



        }
    }
}
