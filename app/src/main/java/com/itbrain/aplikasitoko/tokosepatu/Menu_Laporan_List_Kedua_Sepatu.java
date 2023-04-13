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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Menu_Laporan_List_Kedua_Sepatu  extends AppCompatActivity {
        ModulTokoSepatu config,temp;
        DatabaseTokoSepatu db ;
        View v ;
        ArrayList arrayList = new ArrayList() ;
        ArrayList arraystat = new ArrayList() ;
        int year,day,month;
        Calendar calendar;
        String type;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_laporan_kedua_sepatu);
            Toolbar toolbar= findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
            temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
            db = new DatabaseTokoSepatu(this) ;
            v = this.findViewById(android.R.id.content);
            calendar=Calendar.getInstance();
            year=calendar.get(Calendar.YEAR);
            month=calendar.get(Calendar.MONTH);
            day=calendar.get(Calendar.DAY_OF_MONTH);



            String date_v= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            ModulTokoSepatu.setText(v, R.id.tglAwal,date_v);
            ModulTokoSepatu.setText(v, R.id.tglakhir,date_v);

            TextView tglaw = findViewById(R.id.tglAwal);
            TextView tglak = findViewById(R.id.tglakhir);




            type=getIntent().getStringExtra("type");

            if (type.equals("pendapatan")){
                getSupportActionBar().setTitle("Laporan Pendapatan");
                getPendapatan("");

                final EditText eCari = (EditText) findViewById(R.id.eCari) ;

                eCari.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        {

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String a = eCari.getText().toString() ;
                        arrayList.clear();
                        getPendapatan(a);

                    }
                });
            }else if(type.equals("dethutang")){
                getSupportActionBar().setTitle("Laporan Detail Hutang");
                getDetailHutang("");

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
                        getDetailHutang(a);

                    }
                });
            }else if (type.equals("retur")){
                getSupportActionBar().setTitle("Laporan Return");
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
            }else if (type.equals("pemasukan")){
                getSupportActionBar().setTitle("Laporan Pemasukan");
                getPemasukan("");

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
                        getPemasukan(a);

                    }
                });
            }else if (type.equals("pengeluaran")){
                getSupportActionBar().setTitle("Laporan Pengeluaran");
                getPengeluaran("");

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
                        getPengeluaran(a);

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
                if (type.equals("pendapatan")){
                    getPendapatan("");
                }else if(type.equals("dethutang")){
                    getDetailHutang("");
                }else if(type.equals("retur")){
                    getBarang("");
                }else if(type.equals("pemasukan")){
                    getPemasukan("");
                }else if(type.equals("pengeluaran")){
                    getPengeluaran("");
                }

            }
        };

        private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
                ModulTokoSepatu.setText(v,R.id.tglakhir,ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day));
                if (type.equals("pendapatan")){
                    getPendapatan("");
                }else if(type.equals("dethutang")){
                    getDetailHutang("");
                }else if (type.equals("retur")){
                    getBarang("");
                }else if(type.equals("pemasukan")){
                    getPemasukan("");
                }else if(type.equals("pengeluaran")){
                    getPengeluaran("");
                }

            }
        };

        public void getBarang(String cari) {
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new AdapterLapBar(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulTokoSepatu.selectwhere("qretur") + ModulTokoSepatu.sLike("barang",cari) +" AND "+ModulTokoSepatu.sBetween("tglretur",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir))+ " ORDER BY barang ASC";
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                String barang = ModulTokoSepatu.getString(c,"barang")+" ("+ModulTokoSepatu.getString(c,"ukuran")+")";
                String jumlah = "Jumlah Retur : "+ModulTokoSepatu.getString(c,"jumlahretur");
                String campur = "laporanretur"+"__"+barang + "__" + ModulTokoSepatu.getString(c,"tglretur")+ "__" + jumlah;
                arrayList.add(campur);
            }

            adapter.notifyDataSetChanged();
        }


        public void getPendapatan(String cari){
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulTokoSepatu.selectwhere("view_orderdetail") + ModulTokoSepatu.sLike("pelanggan",cari) +" AND "+ModulTokoSepatu.sBetween("tgljual",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir))+ " ORDER BY idjual ASC";;
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                String campur = ModulTokoSepatu.getString(c,"iddetailjual")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"tgljual")+ "__" + ModulTokoSepatu.getString(c,"total")+ "__" + ModulTokoSepatu.getString(c,"bayar")+ "__" + ModulTokoSepatu.getString(c,"kembali");
                arrayList.add(campur);

            }

            adapter.notifyDataSetChanged();
        }

        public void getPemasukan(String cari){
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new AdapterDompetdua(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("keluar","0") +" AND "+ ModulTokoSepatu.sLike("kettransaksi",cari) +" AND "+ModulTokoSepatu.sBetween("tgltransaksi",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir));
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                String campur = ModulTokoSepatu.getString(c,"idtransaksi")+"__"+ModulTokoSepatu.getString(c,"tgltransaksi") + "__" + ModulTokoSepatu.getString(c,"fakturtran") + "__" + ModulTokoSepatu.getString(c,"kettransaksi") + "__" + ModulTokoSepatu.getString(c,"masuk")+ "__" + ModulTokoSepatu.getString(c,"keluar")+"__"+"pemasukan";
                arrayList.add(campur);

            }

            adapter.notifyDataSetChanged();
        }

        public void getPengeluaran(String cari){
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new AdapterDompetdua(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("masuk","0") +" AND "+ ModulTokoSepatu.sLike("kettransaksi",cari) +" AND "+ModulTokoSepatu.sBetween("tgltransaksi",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir));
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                String campur = ModulTokoSepatu.getString(c,"idtransaksi")+"__"+ModulTokoSepatu.getString(c,"tgltransaksi") + "__" + ModulTokoSepatu.getString(c,"fakturtran") + "__" + ModulTokoSepatu.getString(c,"kettransaksi") + "__" + ModulTokoSepatu.getString(c,"masuk")+ "__" + ModulTokoSepatu.getString(c,"keluar")+"__"+"pengeluaran";
                arrayList.add(campur);

            }

            adapter.notifyDataSetChanged();
        }

        public void getDetailHutang(String cari) {
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLapPel) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new AdapterLapPel(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulTokoSepatu.selectwhere("qjual")+ModulTokoSepatu.sWhere("status","Utang")+" AND " + ModulTokoSepatu.sLike("pelanggan",cari) +" AND "+ModulTokoSepatu.sBetween("tgljual",ModulTokoSepatu.getText(v,R.id.tglAwal),ModulTokoSepatu.getText(v,R.id.tglakhir))+ " ORDER BY pelanggan ASC";
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                double hutang =ModulTokoSepatu.strToDouble(ModulTokoSepatu.getString(c,"kembali"))*-1;
                String hasil="Hutang : "+ ModulTokoSepatu.removeE(hutang);

                String campur = ModulTokoSepatu.getString(c,"idjual")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + hasil + "__" + ModulTokoSepatu.getString(c,"tgljual");
                arrayList.add(campur);
            }

            adapter.notifyDataSetChanged();
        }

        public void hapus(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ImageView hps = findViewById(R.id.hapus);
                            String id = hps.getTag().toString();

                            String q = "DELETE FROM tbldetailjual WHERE iddetailjual="+id ;
                            db.exc(q);
//
                            getPendapatan("");




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

        public void export(View view) {
            Intent i = new Intent(Menu_Laporan_List_Kedua_Sepatu.this,Menu_Export_Excel_Sepatu.class);
            i.putExtra("type",type);
            startActivity(i);
        }

        public void tglawal(View view){
            showDialog(1);
        }

        public void tglakhir(View view){
            showDialog(2);
        }




    }

    class AdapterPendapatan extends RecyclerView.Adapter<AdapterPendapatan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterPendapatan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_pendapatan_sepatu, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama,tgljual,total,bayar,kembali;
            private ImageView hapus;


            public ViewHolder(View view) {
                super(view);
                nama=view.findViewById(R.id.NamaPelanggan);
                tgljual=view.findViewById(R.id.tgljual);
                total=view.findViewById(R.id.total);
                bayar=view.findViewById(R.id.Bayar);
                kembali=view.findViewById(R.id.kembali);
                hapus=view.findViewById(R.id.hapus);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");
            viewHolder.hapus.setTag(row[0]);
            viewHolder.nama.setText(row[1]);
            viewHolder.tgljual.setText(row[2]);
            viewHolder.total.setTag("Total = "+row[3]);
            viewHolder.bayar.setText("Bayar = "+ModulTokoSepatu.removeE(row[4]));
            if (ModulTokoSepatu.strToInt(row[5])<0){
                row[5]="0";
            }
            viewHolder.kembali.setText("Kembali = "+ModulTokoSepatu.removeE(row[5]));

        }
    }

    class AdapterDompetdua extends RecyclerView.Adapter<AdapterDompetdua.ViewHolder>{
        ArrayList <String> data;
        Context c;

        public  AdapterDompetdua(Context a, ArrayList<String>kota){
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
            holder.no.setText("No Transaksi : "+ModulTokoSepatu.intToStr(position+1));
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
                                    if (row[6].equals("pemasukan")){
                                        ((Menu_Laporan_List_Kedua_Sepatu)c).getPemasukan("");
                                    }else{
                                        ((Menu_Laporan_List_Kedua_Sepatu)c).getPengeluaran("");
                                    }

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
