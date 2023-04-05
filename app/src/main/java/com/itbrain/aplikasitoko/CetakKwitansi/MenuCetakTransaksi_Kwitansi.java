package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuCetakTransaksi_Kwitansi extends AppCompatActivity {

    int tJumlah = 0, year, month, day, tIdjasa, tIdpelanggan, isikeranjang = 0;
    String faktur = "00000000", tJasa, tPelanggan, plgn;
    Calendar calendar;
    DatabaseCetakKwitansi db;
    ConfigKwitansi temp;

    TextView btnMin, btnPlus;
    TextInputEditText edtTgl, edtFaktur, edtPelanggan, edtJasa, edtHarga, edtJumlah, edtKeterangan;
    ImageView btnTgl, btnCariPelanggan, btnCariJasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucetaktransaksi_kwitansi);

        ImageButton imageButton = findViewById(R.id.imageButton31);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        temp = new ConfigKwitansi(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseCetakKwitansi(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        edtPelanggan = (TextInputEditText) findViewById(R.id.edtPelanggan);
        edtJasa = (TextInputEditText) findViewById(R.id.edtJasa);
        edtFaktur = (TextInputEditText) findViewById(R.id.edtFaktur);
        edtHarga = (TextInputEditText) findViewById(R.id.edtHarga);
        edtJumlah = (TextInputEditText) findViewById(R.id.edtJumlah);
        edtKeterangan = (TextInputEditText) findViewById(R.id.edtKet);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        edtTgl = (TextInputEditText) findViewById(R.id.edtTgl);
        edtTgl.setText(date);

        btnTgl = (ImageView) findViewById(R.id.ivTgl);
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        cek();
        loadCart();
        btnCari();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cek(){
        Cursor c = db.sq("SELECT * FROM tbltransaksi WHERE status=0");
        if (c.getCount()>0){
            c.moveToLast();
            @SuppressLint("Range") String faktur = c.getString(c.getColumnIndex("faktur"));
            edtFaktur.setText(faktur);
        } else {
            getFaktur();
        }
    }

    private void getFaktur(){
        List<Integer> idtrans = new ArrayList<Integer>();
        String q="SELECT idtransaksi FROM tbltransaksi";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idtrans.add(c.getInt(0));
            }while (c.moveToNext());
        }
        String tempFaktur="";
        int IdFaktur=0;
        if (c.getCount()==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
        }else {
            IdFaktur = idtrans.get(c.getCount()-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }
        edtFaktur.setText(tempFaktur);
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
            edtTgl.setText(setDatePickerNormal(thn,bln+1,day));
        }
    };

    private void btnCari(){
        btnCariPelanggan = (ImageView) findViewById(R.id.ivPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MenuCetakTransaksi_Kwitansi.this, ProsesCari_Kwitansi.class);
                i.putExtra("cari","pelanggan");
                startActivityForResult(i,1);
            }
        });
        btnCariJasa = (ImageView) findViewById(R.id.ivJasa);
        btnCariJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MenuCetakTransaksi_Kwitansi.this, ProsesCari_Kwitansi.class);
                i.putExtra("cari","jasa");
                startActivityForResult(i,2);
            }
        });

        btnPlus=(TextView)findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah = tJumlah+1;
                setJumlah(tJumlah);
            }
        });
        btnMin=(TextView)findViewById(R.id.btnMin);
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah = tJumlah-1;
                setJumlah(tJumlah);
            }
        });
    }

    private void setEdit(String jasa){
        plgn = temp.getCustom("idpelanggan","") ;

        if (TextUtils.isEmpty(plgn)) {
            temp.setCustom("idpelanggan", "0");
            plgn = "0";
            edtPelanggan.setText(plgn);
        }

        if (!TextUtils.isEmpty(plgn)){
            getPelanggan(String.valueOf(tIdpelanggan));
        } else {
            edtPelanggan.setText("");
        }

        edtJasa.setText(jasa);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            tIdpelanggan = data.getIntExtra("idpelanggan",0);
            tPelanggan = data.getStringExtra("pelanggan");
            getPelanggan(String.valueOf(tIdpelanggan));
        }else if (resultCode==2){
            tIdjasa = data.getIntExtra("idjasa",0);
            tJasa = data.getStringExtra("jasa");
            getJasa(String.valueOf(tIdjasa));
        }
    }

    @SuppressLint("Range")
    public void getPelanggan(String idpelanggan){
        String q = "SELECT * FROM tblpelanggan WHERE idpelanggan='"+idpelanggan+"'";
        Cursor c = db.sq(q) ;
        c.moveToNext() ;

        edtPelanggan = (TextInputEditText) findViewById(R.id.edtPelanggan);
        edtPelanggan.setText(c.getString(c.getColumnIndex("pelanggan")));
    }

    @SuppressLint("Range")
    public void getJasa(String idjasa){
        String q = "SELECT * FROM tbljasa WHERE idjasa='"+idjasa+"'";
        Cursor c = db.sq(q) ;
        c.moveToNext() ;

        edtJasa = (TextInputEditText) findViewById(R.id.edtJasa);
        edtJasa.setText(c.getString(c.getColumnIndex("jasa")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtJumlah.setText("0");
        tJumlah=0;
        setEdit(tJasa);
        if (!edtHarga.equals("")){
            setJumlah(0);
        }
    }

    private void setJumlah(Integer jumlah){
        edtJumlah.setText(String.valueOf(jumlah));
        if (jumlah<1){
            btnMin.setVisibility(View.INVISIBLE);
        }else {
            btnMin.setVisibility(View.VISIBLE);
        }
    }

    public void btnsimpan(View view){
        String eFaktur = edtFaktur.getText().toString();
        String eTgl = edtTgl.getText().toString();
        String eNPelanggan = edtPelanggan.getText().toString();
        String eJasa = edtJasa.getText().toString();
        String eHarga = edtHarga.getText().toString();
        String eJumlah = edtJumlah.getText().toString();
        String eKet = edtKeterangan.getText().toString();
        if (TextUtils.isEmpty(eJasa) || TextUtils.isEmpty(eNPelanggan) || ConfigKwitansi.toDouble(eHarga)==0 || ConfigKwitansi.toDouble(eJumlah)==0) {
            Toast.makeText(this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }else {
            String idPelanggan = String.valueOf(tIdpelanggan);
            String idjasa = String.valueOf(tIdjasa);
            Integer idtransaksi = Integer.valueOf(eFaktur);
//            Double bay = Config.toDouble(eHarga) * Config.toDouble(eJumlah);
            String harga="";
            String qTransD,qTrans ;

            String q = "SELECT * FROM tbltransaksi WHERE faktur='"+eFaktur+"'";
            Cursor c = db.sq(q);
            if (c.getCount()==0){
                qTrans = "INSERT INTO tbltransaksi (faktur,tgltransaksi,total,idpelanggan,penerima) VALUES ('"+eFaktur+"','"+convertDate(eTgl)+"','"+harga+"','"+idPelanggan+"','"+eNPelanggan+"')";
                qTransD = "INSERT INTO tbltransaksidetail (idjasa,idtransaksi,harga,jumlah,keterangan,jasatransaksi) VALUES ('"+idjasa+"','"+String.valueOf(idtransaksi)+"','"+eHarga+"','"+eJumlah+"','"+eKet+"','"+eJasa+"')";
            }else {
                qTrans = "UPDATE tbltransaksi SET " +
                        "idpelanggan='" +idPelanggan+"',"+
                        "penerima='" +eNPelanggan+"',"+
                        "tgltransaksi='"+convertDate(eTgl)+"',"+
                        "faktur='"+eFaktur+"'"+
                        " WHERE idtransaksi=" +String.valueOf(idtransaksi);
                qTransD = "INSERT INTO tbltransaksidetail (idjasa,idtransaksi,harga,jumlah,keterangan,jasatransaksi) VALUES ('"+idjasa+"','"+String.valueOf(idtransaksi)+"','"+eHarga+"','"+eJumlah+"','"+eKet+"','"+eJasa+"')";
            }

//            db.exc(qTrans);

            if (db.exc(qTrans)&&db.exc(qTransD)){
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                clearText();
                loadCart();
            }else if (db.exc(qTransD)) {
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                clearText();
                loadCart();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void btnPreview(View view) {
        final String faktur = edtFaktur.getText().toString();
        Cursor c= db.sq("SELECT * FROM vtransaksi WHERE faktur='"+faktur+"'");
        c.moveToNext();
        isikeranjang = c.getCount();
        if (isikeranjang==0){
            Toast.makeText(this, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuCetakTransaksi_Kwitansi.this);
            builder.create()
                    .setTitle("Anda Yakin?");
            builder.setMessage("Anda yakin ingin menyimpan pesanan ini?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String q = "UPDATE tbltransaksi SET status=1 WHERE faktur='"+faktur+"'";
                            db.exc(q);
                            Intent i = new Intent(MenuCetakTransaksi_Kwitansi.this, MenuCetak_Kwitansi.class);
                            i.putExtra("faktur", faktur);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        }

    }

    public void loadCart(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recTrans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterTrans(this,arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur = edtFaktur.getText().toString();

        String q= "SELECT * FROM vtransaksi WHERE faktur='"+tempFaktur+"'";
        Cursor c = db.sq(q);
        if (c.getCount()>0){
            while (c.moveToNext()){
                @SuppressLint("Range") String campur =
                        c.getString(c.getColumnIndex("idtransaksidetail"))+"__"+
                                c.getString(c.getColumnIndex("keterangan"))+"__"+
                                c.getString(c.getColumnIndex("penerima"))+"__"+
                                c.getString(c.getColumnIndex("jumlah"))+"__"+
                                c.getString(c.getColumnIndex("harga"))+"__"+
                                c.getString(c.getColumnIndex("jasatransaksi"));
                arrayList.add(campur);
            }
        }else{

        }
        adapter.notifyDataSetChanged();

    }

    private void clearText(){
        edtJasa.setText("");
        edtHarga.setText("");
        edtJumlah.setText("0");
        tJumlah=0;
        edtKeterangan.setText("");
    }

    public String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return hri+"/"+bln+"/"+String.valueOf(year);
    }
}

class AdapterTrans extends RecyclerView.Adapter<AdapterTrans.TransViewHolder>{
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterTrans(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaksi_kwitansi,viewGroup,false);
        return new TransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        String ket;

        if (row[1].equals("")){
            ket = "-";
        } else {
            ket = row[1];
        }

        holder.pelanggan.setText(row[2]);
        holder.jasa.setText(row[5]);
        holder.harga.setText(row[3]+" x "+ ProsesCari_Kwitansi.removeE(row[4]));
        holder.keterangan.setText("Keterangan : "+ket);

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseCetakKwitansi db=new DatabaseCetakKwitansi(ctxAdapter);
                AlertDialog.Builder builder=new AlertDialog.Builder(ctxAdapter);
                builder.create();
                builder.setMessage("Anda yakin ingin menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tbltransaksidetail WHERE idtransaksidetail="+row[0];
                                if (db.exc(q)){
                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
                                    ((MenuCetakTransaksi_Kwitansi)ctxAdapter).loadCart();
                                }else {
                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransViewHolder extends RecyclerView.ViewHolder{
        TextView pelanggan,jasa,harga,keterangan;
        ImageView hapus;
        public TransViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan = (TextView)itemView.findViewById(R.id.tPelanggan);
            jasa = (TextView)itemView.findViewById(R.id.tJasa);
            harga = (TextView)itemView.findViewById(R.id.tHarga);
            keterangan = (TextView)itemView.findViewById(R.id.tKeterangan);
            hapus = (ImageView) itemView.findViewById(R.id.ivDelete);
        }
    }
}



