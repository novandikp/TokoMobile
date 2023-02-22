package com.itbrain.aplikasitoko.rentalmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MenuDetailPengembalian_Mobil extends AppCompatActivity {

    Calendar calendar;
    int year, month, day, hour, minute;
    private TextView t3;
    private TextView t2;
    private TextView t1;
    private TextView t4;
    private ImageView imageView8;
    private CardView cv;
    private Button button9;
    private RecyclerView recList;
    DatabaseRentalMobil db;
    String idrental, tanggal;
    ArrayList arrayList = new ArrayList();
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menudetailpengembalian_mobil);

        tanggal = ModulRentalMobil.getDate("dd/MM/yyyy HH:mm");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        t3
                = (TextView) findViewById(R.id.t3);
        t2
                = (TextView) findViewById(R.id.t2);
        t1
                = (TextView) findViewById(R.id.t1);
        t4
                = (TextView) findViewById(R.id.t4);
        cv
                = (CardView) findViewById(R.id.cv);
        button9
                = (Button) findViewById(R.id.button9);
        Button button13
                = (Button) findViewById(R.id.button13);
        recList
                = (RecyclerView) findViewById(R.id.recList);
        db = new DatabaseRentalMobil(this);
        v = this.findViewById(android.R.id.content);
        idrental = getIntent().getStringExtra("idrental");
        getPesanan();

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cek()) {
                    Intent i = new Intent(MenuDetailPengembalian_Mobil.this, MobilMenuBayar.class);
                    i.putExtra("idrental", idrental);
                    startActivity(i);
                } else {
                    ModulRentalMobil.showToast(MenuDetailPengembalian_Mobil.this, getString(R.string.belum_kembali));
                }
            }
        });

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuDetailPengembalian_Mobil.this, MenuCetak_Mobil.class);
                i.putExtra("idorder", idrental);
                i.putExtra("type", "dp");
                startActivity(i);
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

    private boolean cek() {
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblrentaldetail") + ModulRentalMobil.sWhere("idrental", idrental) + " AND " + ModulRentalMobil.sWhere("flagkembali", "0"));
        if (c.getCount() == 0) {
            return true;
        } else {
            return false;
        }

    }

    private void setText() {
        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rental") + ModulRentalMobil.sWhere("idrental", idrental));
        c.moveToNext();
        t1.setText("Faktur : " + ModulRentalMobil.getString(c, "faktur"));
        t2.setText(ModulRentalMobil.getString(c, "tglrental"));
        t3.setText("Nama Pelanggan : " + ModulRentalMobil.getString(c, "pelanggan"));
        t4.setText("Total : " + ModulRentalMobil.removeE(ModulRentalMobil.getString(c, "total")));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void buatDialog(final String id) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.dialog_kembali_mobil, null);
        final TextView mobil, tgl, harga;
        final TextInputEditText eTgl, ebiaya;
        Button konfirmasi;
        ImageView kalendar;
        mobil = mview.findViewById(R.id.tMobil);
        harga = mview.findViewById(R.id.tHarga);
        tgl = mview.findViewById(R.id.tTanggal);
        eTgl = mview.findViewById(R.id.eTanggal);
        konfirmasi = mview.findViewById(R.id.konfirmasi);
        kalendar = mview.findViewById(R.id.kalendar);
        ebiaya = mview.findViewById(R.id.eBiaya);
        ebiaya.setText("0");
        eTgl.setEnabled(false);
        final Calendar calaw, calak;
        calaw = Calendar.getInstance();
        calak = Calendar.getInstance();
        Calendar calKembali = Calendar.getInstance();
        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rentaldetail") + ModulRentalMobil.sWhere("idrentaldetail", id));
        c.moveToNext();
        final int jumlahhari = ModulRentalMobil.strToInt(ModulRentalMobil.getString(c, "jumlahhari"));
        mobil.setText(ModulRentalMobil.getString(c, "mobil") + " - " + ModulRentalMobil.getString(c, "plat"));
        tgl.setText(ModulRentalMobil.getString(c, "tglmulai") + " - " + ModulRentalMobil.getString(c, "tglselesai"));
        eTgl.setText(tanggal);

        ebiaya.setEnabled(false);
        String tglAwal = ModulRentalMobil.getString(c, "tglmulai");
        String tglSelesai = ModulRentalMobil.getString(c, "tglselesai");
        Date tglAw, tglSel;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            tglAw = sdf.parse(tglAwal);
            calaw.setTime(tglAw);
            tglSel = sdf.parse(tglSelesai);
            calKembali.setTime(tglSel);

        } catch (ParseException e) {

        }
        final String hari = ModulRentalMobil.getString(c, "jumlahhari");
        String jumlahhhari = ModulRentalMobil.getString(c, "jumlahhari");
        String jumlahjam = ModulRentalMobil.getString(c, "jumlahjam");
        String jumlahmenit = ModulRentalMobil.getString(c, "jumlahmenit");
        String hargarental = ModulRentalMobil.getString(c, "hargarental");
        String hargarentaljam = ModulRentalMobil.getString(c, "hargarentaljam");
        String hargarentalmenit = ModulRentalMobil.getString(c, "hargarentalmenit");
        double total = ModulRentalMobil.strToDouble(jumlahhhari) * ModulRentalMobil.strToDouble(hargarental) + ModulRentalMobil.strToDouble(jumlahjam) * ModulRentalMobil.strToDouble(hargarentaljam) + ModulRentalMobil.strToDouble(jumlahmenit) * ModulRentalMobil.strToDouble(hargarentalmenit);
        StringBuilder sb = new StringBuilder();
        if (ModulRentalMobil.strToDouble(jumlahhhari) > 0) {
            sb.append(jumlahhhari + " hari ");
        }
        if (ModulRentalMobil.strToDouble(jumlahjam) > 0) {
            sb.append(jumlahjam + " jam ");
        }
        if (ModulRentalMobil.strToDouble(jumlahmenit) > 0) {
            sb.append(jumlahmenit + " menit ");
        }
        final String tglselesai = ModulRentalMobil.getString(c, "tglselesai");
        final double hargarent = ModulRentalMobil.strToDouble(ModulRentalMobil.getString(c, "hargarental"));
//        double total = hargarent * ModulRentalMobil.strToDouble(hari);
        harga.setText(sb.toString() + " = " + ModulRentalMobil.removeE(total));
        kalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog datepicker = new DatePickerDialog(MenuDetailPengembalian_Mobil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        new TimePickerDialog(MenuDetailPengembalian_Mobil.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                eTgl.setText(ModulRentalMobil.setDatePickerNormal(year, month + 1, dayOfMonth) + " " + ModulRentalMobil.setTimePickerNormal(i, i1));
                                calak.set(year, month, dayOfMonth, i, i1);
                                if (ModulRentalMobil.minuteBetween(calKembali.getTime(), calak.getTime()) > 0) {
                                    double total = ModulRentalMobil.getSubtotal(calKembali.getTime(), calak.getTime(), ModulRentalMobil.strToDouble(hargarental), ModulRentalMobil.strToDouble(hargarentaljam), ModulRentalMobil.strToDouble(hargarentalmenit));
                                    ebiaya.setText(ModulRentalMobil.removeE(total));
                                } else {
                                    ebiaya.setText("0");
                                }
                                hour = i;
                                minute = i1;
                            }
                        }, hour, minute, true).show();
                        MenuDetailPengembalian_Mobil.this.year = year;
                        MenuDetailPengembalian_Mobil.this.month = month;
                        MenuDetailPengembalian_Mobil.this.day = dayOfMonth;
                    }
                }, year, month, day);
                datepicker.show();
            }
        });

        alert.setView(mview);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idrental = id;

                int menit = ModulRentalMobil.minuteBetween(calaw.getTime(),calak.getTime());
                int jam =  ModulRentalMobil.getHour(menit);
                menit =  ModulRentalMobil.getSisaMinute(menit);
                int hari =  ModulRentalMobil.getHari(jam);
                jam =  ModulRentalMobil.getSisaHour(jam);
                String tglkembali = eTgl.getText().toString();
                String [] isi;
                if (ModulRentalMobil.minuteBetween(calKembali.getTime(),calak.getTime())>0){
                    isi= new String[]{ModulRentalMobil.intToStr(hari),ModulRentalMobil.intToStr(jam),ModulRentalMobil.intToStr(menit), tglkembali, "1", idrental};
                }else{
                    isi= new String[]{jumlahhhari,jumlahjam,jumlahmenit, tglselesai, "1", idrental};
                }
                String q = ModulRentalMobil.splitParam("UPDATE tblrentaldetail SET jumlahhari=?, jumlahjam=? , jumlahmenit=?, tglselesai=? ,flagkembali=? WHERE idrentaldetail=?  ",isi);
                if (db.exc(q)){
                    alertDialog.dismiss();
                    getPesanan();
                }else{
                    ModulRentalMobil.showToast(MenuDetailPengembalian_Mobil.this,"gagal");
                }

            }
        });

        
    }

    public void getPesanan(){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPesananDetailMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("view_rentaldetail")+ModulRentalMobil.sWhere("idrental",idrental);
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idrentaldetail")+"__"+ModulRentalMobil.getString(c,"mobil") + "__" + ModulRentalMobil.getString(c,"plat")+ "__" + ModulRentalMobil.getString(c,"tahunkeluaran")+ "__"
                    + ModulRentalMobil.getString(c,"tglmulai") +"__"+ModulRentalMobil.getString(c,"tglselesai") +"__" + ModulRentalMobil.getString(c,"jumlahhari") +"__"+ModulRentalMobil.getString(c,"hargarental")+"__"+ ModulRentalMobil.getString(c,"flagkembali") +"__" + ModulRentalMobil.getString(c,"jumlahjam") +"__"+ModulRentalMobil.getString(c,"hargarentaljam") +"__" + ModulRentalMobil.getString(c,"jumlahmenit") +"__"+ModulRentalMobil.getString(c,"hargarentalmenit") ;
            arrayList.add(campur);
        }

        if (cek()){
            button9.setBackground(getDrawable(R.color.tombol1));
        }else{
            button9.setBackground(getDrawable(R.color.off));
        }
        setText();
        adapter.notifyDataSetChanged();
    }
}


class AdapterPilihPesananDetailMobil extends RecyclerView.Adapter<AdapterPilihPesananDetailMobil.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPesananDetailMobil(Context a, ArrayList<String> kota) {
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
        final String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);

        holder.nama.setText(row[1]);
        holder.nik.setText(row[2]+" - "+row[3]);
        holder.alamat.setText(row[4]+" - "+row[5]);
//        double total = ModulRentalMobil.strToDouble(row[6])*ModulRentalMobil.strToDouble(row[7]);
        double total = ModulRentalMobil.strToDouble(row[6])*ModulRentalMobil.strToDouble(row[7]) +  ModulRentalMobil.strToDouble(row[9])*ModulRentalMobil.strToDouble(row[10])+ ModulRentalMobil.strToDouble(row[11])*ModulRentalMobil.strToDouble(row[12]);
        StringBuilder sb = new StringBuilder();
        if(ModulRentalMobil.strToDouble(row[6])>0){
            sb.append(row[6]+" hari ");
        }
        if(ModulRentalMobil.strToDouble(row[9])>0){
            sb.append(row[9]+" jam ");
        }
        if(ModulRentalMobil.strToDouble(row[11])>0){
            sb.append(row[11]+" menit ");
        }
        holder.notelp.setText(sb.toString()+" = "+ModulRentalMobil.removeE(total));
        if (row[8].equals("1")){
            holder.gmbr.setImageResource(R.drawable.centang);
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (row[8].equals("0")) {
                    String id = holder.cv.getTag().toString();
                    ((MenuDetailPengembalian_Mobil)c).buatDialog(id);
                }


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
        ImageView gmbr;
        public ViewHolder(View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            nik= (TextView) itemView.findViewById(R.id.t2);
            nama= (TextView) itemView.findViewById(R.id.t1);
            alamat = (TextView) itemView.findViewById(R.id.t3);
            notelp=(TextView) itemView.findViewById(R.id.t4);
            gmbr=itemView.findViewById(R.id.imageView8);

        }
    }
}
