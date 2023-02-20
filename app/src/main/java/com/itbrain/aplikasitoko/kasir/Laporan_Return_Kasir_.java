package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Return_Kasir_ extends AppCompatActivity {

    String type ;
    View v ;
    FConfigKasir config ;
    DatabaseKasir db ;
    ArrayList arrayList = new ArrayList() ;
    String dari, ke ;
    Calendar calendar ;
    int year,month, day ;

    String deviceid ;
    SharedPreferences getPrefs ;
    boolean bHapus = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_return_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE));
        db = new DatabaseKasir(this,config) ;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type") ;

        type = "return";

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.carik) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString() ;
                    getReturn(a);
            }
        });

        setText() ;
            setTitle("Laporan Return") ;
            getReturn("");
    }

    public void exportLaporanReturn(View view){
        Intent i = new Intent(this, ActivityExportExcelKasir.class) ;
        i.putExtra("type",type) ;
        startActivity(i);
    }

    public void setText(){
        dari = FFunctionKasir.setDatePicker(year,month+1,day) ;
        ke = FFunctionKasir.setDatePicker(year,month+1,day) ;
        String now = FFunctionKasir.setDatePickerNormal(year,month+1,day) ;
        FFunctionKasir.setText(v,R.id.nang,now) ;
        FFunctionKasir.setText(v,R.id.prom,now) ;
    }

    public void getReturn(String cari){
        arrayList.clear();
        String q = "" ;
        if(TextUtils.isEmpty(cari)){
            q = FQueryKasir.selectwhere("qreturn")+FQueryKasir.sBetween("tglreturn",dari,ke) ;
        } else {
            q = FQueryKasir.selectwhere("qreturn")+FQueryKasir.sLike("barang",cari) + " AND "+FQueryKasir.sBetween("tglreturn",dari,ke) ;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.erece) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(FFunctionKasir.getCount(c) > 0){
            int total = 0;
            while(c.moveToNext()){
                String nama = FFunctionKasir.getString(c,"barang") ;
                String tgl = FFunctionKasir.getString(c,"tglreturn") ;
                String jumlah = FFunctionKasir.getString(c,"jumlah") ;

                String campur =  FFunctionKasir.getCampur(nama,"Jumlah Barang : "+jumlah , FFunctionKasir.dateToNormal(tgl));
                arrayList.add(campur);
                total+=FFunctionKasir.strToInt(jumlah) ;
            }
            FFunctionKasir.setText(v,R.id.judat,"Jumlah Data : "+String.valueOf(FFunctionKasir.getCount(c))) ;
            FFunctionKasir.setText(v,R.id.tCaption,"Return :") ;
            FFunctionKasir.setText(v,R.id.t100, FFunctionKasir.intToStr(total)+" Barang") ;
            FFunctionKasir.setText(v,R.id.t200, "") ;
        } else {
            FFunctionKasir.setText(v,R.id.tCaption,"Return :") ;
            FFunctionKasir.setText(v,R.id.t100, "0 Barang") ;
            FFunctionKasir.setText(v,R.id.t200, "") ;
            FFunctionKasir.setText(v,R.id.judat,"Jumlah Data : 0") ;
        }
        adapter.notifyDataSetChanged();
    }

    public void prom(View view){
        setDate(1);
    }
    public void nang(View view){
        setDate(2);
    }

    public void submit(){
        String a = FFunctionKasir.getText(v,R.id.carik) ;
            getReturn(a);
    }

    public void open(final String faktur){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus transaksi ini?");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        if(!faktur.equals("tidak")){
                            Cursor c = db.sq(FQueryKasir.selectwhere("tblpenjualan") + FQueryKasir.sWhere("fakturbayar",faktur)) ;
                            if(FFunctionKasir.getCount(c) > 0){
                                while(c.moveToNext()){
                                    String id = FFunctionKasir.getString(c,"idpenjualan") ;
                                    db.exc("DELETE FROM tblpenjualan WHERE idpenjualan='"+id+"'") ;
                                }
                            }
                            db.exc("DELETE FROM tblbayar WHERE fakturbayar='"+faktur+"'") ;
                            Toast.makeText(Laporan_Return_Kasir_.this, "Hapus Berhasil", Toast.LENGTH_SHORT).show();
                            //getPendapatan("");
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

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
            FFunctionKasir.setText(v, R.id.prom, FFunctionKasir.setDatePickerNormal(thn,bln+1,day)) ;
            dari = FFunctionKasir.setDatePicker(thn,bln+1,day) ;
            submit() ;
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.nang, FFunctionKasir.setDatePickerNormal(thn,bln+1,day)) ;
            ke = FFunctionKasir.setDatePicker(thn,bln+1,day) ;
            submit() ;
        }
    };
    //end date time picker

    public void delete(View view){
        String faktur = view.getTag().toString() ;
        open(faktur);
    }


    class AdapterPendapatan extends RecyclerView.Adapter<AdapterPendapatan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterPendapatan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterPendapatan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_transaksi_kasir_pendapatan, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView faktur, nma, jumlah;

            public ViewHolder(View view) {
                super(view);

                nma = (TextView) view.findViewById(R.id.tHitung);
                faktur = (TextView) view.findViewById(R.id.tBarang);
                jumlah = (TextView) view.findViewById(R.id.tNama);
            }
        }

        @Override
        public void onBindViewHolder(AdapterPendapatan.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.jumlah.setText(row[2]);
            viewHolder.nma.setText(row[1]);
            viewHolder.faktur.setText(row[0]);
        }
    }
}