 package com.itbrain.aplikasitoko;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.util.ArrayList;
import java.util.Calendar;

 public class MenuLaporanTiga extends AppCompatActivity {
     ArrayList arrayList = new ArrayList();
     ArrayList arrayTeknisi = new ArrayList();
     ArrayList arrayId = new ArrayList();
     Database_Bengkel_ db;
     int year,day,month;
     Calendar calendar;
     View v;
     double total=0;
     String type;
     TextView tvHead;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_menu_laporan_tiga);
         tvHead = findViewById(R.id.tvHead);
         this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         db=new Database_Bengkel_(this);
         v = this.findViewById(android.R.id.content);
         calendar=Calendar.getInstance();
         year=calendar.get(Calendar.YEAR);
         month=calendar.get(Calendar.MONTH);
         day=calendar.get(Calendar.DAY_OF_MONTH);
         String tgl   = ModulBengkel.getDate("dd/MM/yyyy") ;
         ModulBengkel.setText(v,R.id.tglawal,tgl);
         ModulBengkel.setText(v,R.id.tglakhir,tgl);
         type = getIntent().getStringExtra("type");
         if (type.equals("pendapatan")){
//             getSupportActionBar().setTitle("Laporan Pendapatan");
             tvHead.setText("Laporan Pendapatan");
             ConstraintLayout cr=findViewById(R.id.status);
             cr.setVisibility(View.GONE);
             getPendapatan("");
             ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
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
                     getPendapatan(a);

                 }
             });
         }else if(type.equals("servisteknisi")){
//             getSupportActionBar().setTitle("Laporan Pendapatan Teknisi");
             tvHead.setText("Laporan Pendapatan Teknisi");
             ConstraintLayout cr=findViewById(R.id.cari);
             cr.setVisibility(View.GONE);
             setText();

             Spinner spinner = (Spinner) findViewById(R.id.sTeknisi) ;
             spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     getPendapatanTeknisi();
                     ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });


         }else{
//             getSupportActionBar().setTitle("Laporan Laba");
             tvHead.setText("Laporan Laba");
             ConstraintLayout cr=findViewById(R.id.status);
             cr.setVisibility(View.GONE);
             getLaba("");
             ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
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
                     getLaba(a);

                 }
             });
         }
     }

     public void getPendapatan(String cari) {
         arrayList.clear();
         total=0;
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setHasFixedSize(true);
         RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
         recyclerView.setAdapter(adapter);
         String q = ModulBengkel.selectwhere("qdetailjual")+ " statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.tglawal),ModulBengkel.getText(v,R.id.tglakhir))+" AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("faktur");
         Cursor c = db.sq(q) ;
         while(c.moveToNext()){
             String harga = ModulBengkel.getString(c,"hargajual");
             String idkategori=ModulBengkel.getString(c,"idkategori");
             if (idkategori.equals("1")){
                 double potongan = ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"))/100;
                 harga=ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga)*potongan);
             }
             total+=ModulBengkel.strToDouble(harga);

             String campur = ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"nopol")+ "__" + ModulBengkel.getString(c,"barang")+"__" + harga+"__" + ModulBengkel.getString(c,"jumlah");
             arrayList.add(campur);
         }

         adapter.notifyDataSetChanged();
     }

     public void getLaba(String cari) {
         arrayList.clear();
         total=0;
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setHasFixedSize(true);
         RecyclerView.Adapter adapter = new AdapterLaba(this,arrayList) ;
         recyclerView.setAdapter(adapter);
         String q = ModulBengkel.selectwhere("qdetailjual")+ " idkategori!=1 AND statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.tglawal),ModulBengkel.getText(v,R.id.tglakhir))+" AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("faktur");
         Cursor c = db.sq(q) ;
         while(c.moveToNext()){
             String harga = ModulBengkel.getString(c,"hargajual");
             String idkategori=ModulBengkel.getString(c,"idkategori");
             if (idkategori.equals("1")){
                 double potongan = ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"))/100;
                 harga=ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga)*potongan);
             }
             total+=ModulBengkel.strToDouble(ModulBengkel.getString(c,"laba"));

             String campur = ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"nopol")+ "__" + ModulBengkel.getString(c,"barang")+"__" + harga+"__" + ModulBengkel.getString(c,"laba");
             arrayList.add(campur);
         }

         adapter.notifyDataSetChanged();
     }


     public void getPendapatanTeknisi() {
         arrayList.clear();
         total=0;
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setHasFixedSize(true);
         RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
         recyclerView.setAdapter(adapter);
         String q = whereTeknisi();
         Cursor c = db.sq(q) ;
         while(c.moveToNext()){
             String harga = ModulBengkel.getString(c,"hargajual");
             double pendapatan=100-ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"));
             pendapatan=pendapatan/100;
             harga=ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga)*pendapatan);

             total+=ModulBengkel.strToDouble(harga);

             String campur = ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"nopol")+ "__" + ModulBengkel.getString(c,"barang")+"__" + harga+"__" + ModulBengkel.getString(c,"jumlah");
             arrayList.add(campur);
         }

         adapter.notifyDataSetChanged();
     }


     public void getPembayaranHutang() {
         arrayList.clear();
         total=0;
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setHasFixedSize(true);
         RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
         recyclerView.setAdapter(adapter);
         String q = whereTeknisi();
         Cursor c = db.sq(q) ;
         while(c.moveToNext()){
             String harga = ModulBengkel.getString(c,"hargajual");
             double pendapatan=100-ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"));
             pendapatan=pendapatan/100;
             harga=ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga)*pendapatan);

             total+=ModulBengkel.strToDouble(harga);

             String campur = ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"nopol")+ "__" + ModulBengkel.getString(c,"barang")+"__" + harga+"__" + ModulBengkel.getString(c,"jumlah");
             arrayList.add(campur);
         }

         adapter.notifyDataSetChanged();
     }


     public String whereTeknisi(){
         Spinner sTeknisi = findViewById(R.id.sTeknisi);
         String idteknisi = ModulBengkel.intToStr(sTeknisi.getSelectedItemPosition()+1);
         idteknisi=ModulBengkel.selectwhere("qdetailjual")+" idteknisi="+idteknisi+" AND idkategori==1 AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.tglawal),ModulBengkel.getText(v,R.id.tglakhir));
         return idteknisi;
     }

     public String wherePelanggan(){
         Spinner sTeknisi = findViewById(R.id.sTeknisi);
         String idteknisi = arrayId.get(sTeknisi.getSelectedItemPosition()).toString();
         idteknisi=ModulBengkel.selectwhere("tblhutang")+" idpelanggan="+idteknisi+" "+ModulBengkel.sBetween("tglbayar",ModulBengkel.getText(v,R.id.tglawal),ModulBengkel.getText(v,R.id.tglakhir));
         return idteknisi;
     }

     private void setText() {
         arrayTeknisi.clear();
         Spinner spinner = (Spinner) findViewById(R.id.sTeknisi) ;
         ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayTeknisi);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         Cursor c = db.sq(ModulBengkel.select("tblteknisi"));
         if(c.getCount() > 0){
             while(c.moveToNext()){
                 arrayTeknisi.add(ModulBengkel.getString(c,"teknisi"));
             }
         }
         adapter.notifyDataSetChanged();
     }

     private void setPelanggan() {
         arrayTeknisi.clear();
         Spinner spinner = (Spinner) findViewById(R.id.sTeknisi) ;
         ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayTeknisi);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         Cursor c = db.sq(ModulBengkel.selectwhere("tblpelanggan")+" hutang > 0 ");
         if(c.getCount() > 0){
             while(c.moveToNext()){
                 arrayTeknisi.add(ModulBengkel.getString(c,"pelanggan"));
                 arrayId.add(ModulBengkel.getString(c,"idpelanggan"));
             }
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
             if (type.equals("pendapatan")){
                 getPendapatan("");
             }else if (type.equals("servisteknisi")){
                 getPendapatanTeknisi();
             }else{
                 getLaba(ModulBengkel.getText(v,R.id.eCari));
             }
             ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
         }
     };

     private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
         @Override
         public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
             ModulBengkel.setText(v,R.id.tglakhir,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
             if (type.equals("pendapatan")){
                 getPendapatan("");
             }else if (type.equals("servisteknisi")){
                 getPendapatanTeknisi();
             }else{
                 getLaba(ModulBengkel.getText(v,R.id.eCari));
             }
             ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
         }
     };

     public void tglawal(View view) {
         showDialog(1);
     }

     public void tglakhir(View view) {
         showDialog(2);
     }

     public void export(View view) {
         Intent i= new Intent(MenuLaporanTiga.this,MenuExportExcel.class);
         i.putExtra("type",type);
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
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);


         return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
         String[] row = data.get(i).split("__");
         if (row[0].equals("1")){
             holder.nama.setText(row[1]);
             holder.alamat.setText(row[2]+"("+row[3]+")");
             holder.notelp.setText(row[4]);
             holder.tvOpt.setText("Rp."+ModulBengkel.removeE(row[5]));
         }else{
             holder.nama.setText(row[1]);
             holder.alamat.setText(row[2]);
             holder.notelp.setText(ModulBengkel.upperCaseFirst(row[4]));
             holder.tvOpt.setText(ModulBengkel.removeE(row[5])+" x "+row[6]+"="+"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(ModulBengkel.strToDouble(row[5])*ModulBengkel.strToDouble(row[6]))));
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
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);


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