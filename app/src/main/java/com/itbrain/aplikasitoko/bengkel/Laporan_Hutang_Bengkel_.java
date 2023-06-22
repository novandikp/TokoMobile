package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.itbrain.aplikasitoko.PemanggilMethod;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Hutang_Bengkel_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    Database_Bengkel_ db;
    int year,day,month;
    Calendar calendar;
    View v;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_hutang_bengkel_);
        ImageButton imageButton = findViewById(R.id.kembali28);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db= new Database_Bengkel_(this);
        v=this.findViewById(android.R.id.content);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        String tgl   = ModulBengkel.getDate("dd/MM/yyyy") ;
        ModulBengkel.setText(v,R.id.tglawal2,tgl);
        ModulBengkel.setText(v,R.id.tglakhir2,tgl);
        type = getIntent().getStringExtra("type");
        if(type.equals("hutang")){
//            getSupportActionBar().setTitle("Laporan Hutang");
            getHutang("");
            final EditText eCari = (EditText) findViewById(R.id.etLapPel) ;
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
    }
    public void getHutang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rechut) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapHutang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qhutang")  +ModulBengkel.sBetween("tglbayar",ModulBengkel.getText(v,R.id.tglawal2),ModulBengkel.getText(v,R.id.tglakhir2))+" AND "+ModulBengkel.sLike("pelanggan",cari);
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idhutang")+"__"+ModulBengkel.getString(c,"pelanggan") + "__" + ModulBengkel.getString(c,"tglbayar")+ "__" + ModulBengkel.getString(c,"bayarhutang");
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
            ModulBengkel.setText(v,R.id.tglawal2,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
            if (type.equals("hutang")){
                getHutang(ModulBengkel.getText(v,R.id.etLapPel));
            }
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.tglakhir2,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
            if (type.equals("hutang")){
                getHutang(ModulBengkel.getText(v,R.id.etLapPel));
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
        Intent i= new Intent(Laporan_Hutang_Bengkel_.this,MenuExportExcelBengkel.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}


class AdapterLapHutang extends RecyclerView.Adapter<AdapterLapHutang.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapHutang(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_hutang_bengkel, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setVisibility(View.GONE);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText("Total Pembayaran : "+ModulBengkel.removeE(row[3]));



        holder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.tvOpt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                Intent intent = new Intent(c,FormUbahTeknisibengkel.class);
                                intent.putExtra("id",holder.tvOpt.getTag().toString());
                                c.startActivity(intent);
                                break;
                            case  R.id.menu_delete:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Bengkel_ db = new Database_Bengkel_(c);;
                                                String id = holder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tblpelanggan WHERE idteknisi="+id ;
                                                db.exc(q);
//
                                                if (c instanceof PemanggilMethod){
                                                    ((PemanggilMethod)c).getPelanggan("");
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


                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);


        }
    }
}