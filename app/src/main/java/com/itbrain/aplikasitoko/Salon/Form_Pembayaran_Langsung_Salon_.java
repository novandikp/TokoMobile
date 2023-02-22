package com.itbrain.aplikasitoko.Salon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Form_Pembayaran_Langsung_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    DatabaseSalon db;
    View v;
    Calendar calendar;
    String faktur="00000000", tnPelanggan, tnJasa="", tnHarga="";
    int year, month, day, tJumlah=0, tIdpelanggan, tIdJasa, isikeranjang=0;
    ImageButton btnTglPembayaran, btnCariPelanggan, btnCariJasa, btnAddJ, btnRemoveJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_pembayaran_langsung_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appbar = (Toolbar) findViewById(R.id.toolbar68);

        db=new DatabaseSalon(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        cek();
        loadCart();
        btnCari();
        getTotal();

        String date_n=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        FunctionSalon.setText(v, R.id.edtTglPembayaran,date_n);
        btnTglPembayaran= (ImageButton)findViewById(R.id.ibtnTglPembayaran);
        btnTglPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cek(){
        Cursor c = db.sq(QuerySalon.selectwhere("qorder")+QuerySalon.sWhere("bayar", "0"));
        if (FunctionSalon.getCount(c)>0){
            c.moveToLast();
            String faktur = FunctionSalon.getString(c, "faktur");
            FunctionSalon.setText(v, R.id.edtNomorFaktur, faktur);
        } else {
            getFaktur();
        }
    }

    private void getFaktur(){
        List<Integer> idorder = new ArrayList<Integer>();
        String q="SELECT idorder FROM tblorder";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idorder.add(c.getInt(0));
            }while (c.moveToNext());
        }
        String tempFaktur="";
        int IdFaktur=0;
        if (FunctionSalon.getCount(c)==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
        }else {
            IdFaktur = idorder.get(FunctionSalon.getCount(c)-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }
        FunctionSalon.setText(v,R.id.edtNomorFaktur,tempFaktur);
    }

    private void btnCari(){
        btnCariPelanggan=(ImageButton)findViewById(R.id.ibtnNamaPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Form_Pembayaran_Langsung_Salon_.this, ActivityPembayaranCariSalon.class);
                i.putExtra("cari","pelanggan");
                startActivityForResult(i,1000);
            }
        });
        btnCariJasa=(ImageButton)findViewById(R.id.ibtnNamaJasa);
        btnCariJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Form_Pembayaran_Langsung_Salon_.this, ActivityPembayaranCariSalon.class);
                i.putExtra("cari","jasa");
                startActivityForResult(i,2000);
            }
        });
        btnAddJ=(ImageButton)findViewById(R.id.ibtnPlus);
        btnAddJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah+1;
                setJumlah(tJumlah, tnHarga);
            }
        });
        btnRemoveJ=(ImageButton)findViewById(R.id.ibtnMinus);
        btnRemoveJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah-1;
                setJumlah(tJumlah, tnHarga);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000){
            tIdpelanggan=data.getIntExtra("idpelanggan",0);
            tnPelanggan=data.getStringExtra("pelanggan");
            getPelanggan(FunctionSalon.intToStr(tIdpelanggan));
        }else if (resultCode==2000){
            tIdJasa=data.getIntExtra("idjasa",0);
            tnJasa=data.getStringExtra("jasa");
            tnHarga=data.getStringExtra("harga");
            getJasa(FunctionSalon.intToStr(tIdJasa));
        }
    }

    public void getPelanggan(String idpelanggan){
        String q = QuerySalon.selectwhere("tblpelanggan") + QuerySalon.sWhere("idpelanggan", idpelanggan) ;
        Cursor c = db.sq(q) ;
        c.moveToNext() ;
        FunctionSalon.setText(v,R.id.edtNamaPelanggan,FunctionSalon.getString(c, "pelanggan")) ;
    }

    public void getJasa(String idjasa){
        String q = QuerySalon.selectwhere("tbljasa") + QuerySalon.sWhere("idjasa", idjasa) ;
        Cursor c = db.sq(q) ;
        c.moveToNext() ;
        FunctionSalon.setText(v,R.id.edtNamaJasa,FunctionSalon.getString(c, "jasa")) ;
        FunctionSalon.setText(v,R.id.edtHargaJasa,FunctionSalon.removeE(FunctionSalon.getString(c,"harga"))) ;
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.edtTglPembayaran, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    private void setJumlah(Integer jumlah, String biaya){
        FunctionSalon.setText(v,R.id.edtJumlah,String.valueOf(jumlah));
        if (biaya.equals("")){
            btnAddJ.setVisibility(View.INVISIBLE);
        }else {
            btnAddJ.setVisibility(View.VISIBLE);
        }
        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tJumlah=0;
        FunctionSalon.setText(v,R.id.edtJumlah,"0");
        if (!tnHarga.equals("")){
            setJumlah(0,tnHarga);
        }
    }

    private void keluar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Anda yakin ingin keluar?")
                .setMessage("Jika anda keluar, data di faktur ini akan di hapus");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String faktur =FunctionSalon.getText(v, R.id.edtNomorFaktur);
                String q = "DELETE FROM tblorderdetail WHERE idorder="+faktur;
                db.exc(q);
                Intent i = new Intent(Form_Pembayaran_Langsung_Salon_.this, Aplikasi_Salon_Menu_Transaksi.class) ;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(i);
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        keluar();
    }

    public String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public void insertTransaksi(View view) {
        String eFaktur = FunctionSalon.getText(v,R.id.edtNomorFaktur);
        String eTglP = FunctionSalon.getText(v,R.id.edtTglPembayaran);
        String eNPelanggan = FunctionSalon.getText(v,R.id.edtNamaPelanggan);
        String eNJasa= FunctionSalon.getText(v,R.id.edtNamaJasa);
        String eHarga = FunctionSalon.unNumberFormat(FunctionSalon.getText(v,R.id.edtHargaJasa));
        String eJumlah = FunctionSalon.getText(v,R.id.edtJumlah);
        if (TextUtils.isEmpty(eNJasa) || TextUtils.isEmpty(eNPelanggan) || FunctionSalon.strToDouble(eHarga)==0 || FunctionSalon.strToDouble(eJumlah)==0) {
            Toast.makeText(this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }else {
            String idPelanggan = String.valueOf(tIdpelanggan);
            String idjasa = String.valueOf(tIdJasa);
            Integer idorder = Integer.valueOf(eFaktur);
            String qOrderD,qOrder ;
            String harga="", tipe="0";
            String[] simpan = {
                    idPelanggan,
                    eFaktur,
                    convertDate(eTglP),
                    FunctionSalon.removeE(harga),
                    tipe
            } ;
            String[] detail ={
                    String.valueOf(idorder),
                    idjasa,
                    eHarga,
                    eJumlah
            };
            String q = QuerySalon.selectwhere("tblorder")+QuerySalon.sWhere("faktur",eFaktur);
            Cursor c = db.sq(q);
            if (FunctionSalon.getCount(c)==0){
                qOrder = QuerySalon.splitParam("INSERT INTO tblorder (idpelanggan,faktur,tglorder,total,tipe) VALUES (?,?,?,?,?)",simpan);
                qOrderD = QuerySalon.splitParam("INSERT INTO tblorderdetail (idorder,idjasa,hargajual,jumlah) VALUES (?,?,?,?)",detail);
            }else {
                qOrder="UPDATE tblorder SET " +
                        "idpelanggan=" + idPelanggan + "," +
                        "tglorder="+ convertDate(eTglP) +
                        " WHERE idorder=" + String.valueOf(idorder);
                qOrderD = QuerySalon.splitParam("INSERT INTO tblorderdetail (idorder,idjasa,hargajual,jumlah) VALUES (?,?,?,?)",detail);
            }
            if (db.exc(qOrder) && db.exc(qOrderD)){
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                getTotal();
                clearText();
                loadCart();
//                    } else if (db.exc(qOrderD)) {
//                        Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
//                        getTotal();
//                        clearText();
//                        loadCart();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getTotal(){
        double total=0.0;
        int idorder=Integer.valueOf(FunctionSalon.getText(v,R.id.edtNomorFaktur));
        Cursor c=db.sq("SELECT SUM(hargajual*jumlah) FROM tblorderdetail WHERE idorder="+String.valueOf(idorder));
        double sum=0.0;
        if (c.moveToFirst()){
            sum = c.getDouble(0);
        }
        total=total+sum;
        FunctionSalon.setText(v,R.id.tvTotalBayar,"Rp. "+(FunctionSalon.removeE(total)));
    }

    public void loadCart(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterPembayaranNonBookingSalon(this,arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur=FunctionSalon.getText(v,R.id.edtNomorFaktur);

        String q=QuerySalon.selectwhere("qorderdetail")+QuerySalon.sWhere("faktur",tempFaktur);
        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c)>0){
            while (c.moveToNext()){
                String campur=FunctionSalon.getString(c,"idorderdetail")+"__"+
                        FunctionSalon.getString(c, "pelanggan") + "__" +
                        FunctionSalon.getString(c, "jasa") + "__" +
                        FunctionSalon.getString(c, "jumlah") + "__" +
                        FunctionSalon.getString(c, "hargajual");
                arrayList.add(campur);
            }
        }else{
        }
        adapter.notifyDataSetChanged();
    }

    private void clearText(){
        FunctionSalon.setText(v,R.id.edtNamaJasa,"");
        FunctionSalon.setText(v,R.id.edtHargaJasa,"");
        FunctionSalon.setText(v,R.id.edtJumlah,"");
        setJumlah(0,"");
    }

    public void simpan(View view) {
        String faktur=FunctionSalon.getText(v,R.id.edtNomorFaktur);
        Cursor c= db.sq("SELECT * FROM qorderdetail WHERE faktur='"+faktur+"'");
        c.moveToNext();
        isikeranjang=FunctionSalon.getCount(c);
        final Intent i = new Intent(this, ActivityPembayaranProsesSalon.class);
        i.putExtra("faktur", faktur);
        if (isikeranjang==0){
            Toast.makeText(this, "Masukkan Data Dengan Benar", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.create();
            builder.setMessage("Apakah Anda Yakin Ingin Menyimpan Pesanan Ini?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
}

class AdapterPembayaranNonBookingSalon extends RecyclerView.Adapter<AdapterPembayaranNonBookingSalon.PembayaranViewHolder> {
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterPembayaranNonBookingSalon(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PembayaranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pembayaran_salon,viewGroup,false);
        return new PembayaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PembayaranViewHolder viewHolder, int i) {
        final String[] row=data.get(i).split("__");

        viewHolder.pelanggan.setText(row[1]);
        viewHolder.jasa.setText(row[2]);
        viewHolder.satuanjasa.setText(row[3]+"  x");
        viewHolder.hargajasa.setText(FunctionSalon.removeE(row[4]));

        viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseSalon db = new DatabaseSalon(ctxAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                builder.create();
                builder.setMessage("Apakah Anda Yakin Ingin Menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tblorderdetail WHERE idorderdetail="+row[0];
                                if (db.exc(q)){
                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
                                    ((Form_Pembayaran_Langsung_Salon_)ctxAdapter).getTotal();
                                    ((Form_Pembayaran_Langsung_Salon_)ctxAdapter).loadCart();
                                }else {
                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PembayaranViewHolder extends RecyclerView.ViewHolder {
        TextView pelanggan, jasa, satuanjasa, hargajasa;
        ImageButton hapus;

        public PembayaranViewHolder(@NonNull View itemView) {
            super(itemView);

            pelanggan = (TextView) itemView.findViewById(R.id.tvNamaPelanggan);
            jasa = (TextView) itemView.findViewById(R.id.tvNamaJasa);
            satuanjasa = (TextView) itemView.findViewById(R.id.tvSatuanJasa);
            hargajasa = (TextView) itemView.findViewById(R.id.tvHargaJasa);
            hapus = (ImageButton) itemView.findViewById(R.id.ibtnHapus);
        }
    }
}