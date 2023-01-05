package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.Util.Penjualan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Menu_Transaksi_Bengkel_ extends AppCompatActivity implements Penjualan{

    Database_Bengkel_ db;
    String idjual;
    double hargabeli;
    ImageButton imvTanggal, ibKurang, ibTambah;
    Button btnSimpan;
    TextInputEditText tvTanggal;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    int CARI_PELANGGAN = 0;
    int CARI_BARANG = 1;
    TextView tvNamaPelanggan, tvNamaBarang;
    String idpelanggan, idbarang;
    View v;
    ArrayList arrayList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_bengkel_);
        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        idjual = getIdOrder();
        refresh();

        ImageButton imageButton = findViewById(R.id.Kembali);
        imvTanggal = findViewById(R.id.imvTanggal);
        tvTanggal = findViewById(R.id.etTanggal);
        tvNamaPelanggan = findViewById(R.id.namaPelanggan);
        tvNamaBarang = findViewById(R.id.tvNamaBarang);
        ibKurang = findViewById(R.id.ibKurang);
        ibTambah = findViewById(R.id.ibTambah);
        btnSimpan = findViewById(R.id.btnSimpan);

        tvTanggal.setText(getDayToday());

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        getBarang("");

        final TextInputEditText stok = findViewById(R.id.eStok);
        stok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ModulBengkel.strToInt(stok.getText().toString())>ModulBengkel.strToInt(getStok())) {
                    stok.setText(getStok());
                } else if (ModulBengkel.strToInt(stok.getText().toString())<0) {
                    stok.setText("0");
                }
            }
        });


        tvTanggal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        imvTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });


        ibKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stok = ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))-1;

                if (stok == -2) {
                    stok = ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))+1;
                }
                ModulBengkel.setText(v,R.id.eStok, ModulBengkel.intToStr(stok));
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvNamaPelanggan==null || tvNamaBarang==null) {
                    pesan("Mohon isi dengan benar!");
                } else {
                    simpan();
                    reset();
                    getBarang("");
                }
            }
        });

        ibTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stok = ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))+1;
                ModulBengkel.setText(v,R.id.eStok, ModulBengkel.intToStr(stok));
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button button = findViewById(R.id.bayar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String kondisi = cek();
               if (kondisi.equals("belum")) {
                    pesan("Keranjang Masih Kosong!");
               } else {
                    finish();
                   Intent intent = new Intent(Menu_Transaksi_Bengkel_.this, Pembayaran_Jual_Oederdil_Bengkel_.class);
                   intent.putExtra("idorder", idjual);
                   startActivity(intent);
               }
            }
        });
    }

    protected void onResume() {
        super.onResume();
            if(idbarang!=null){
                Cursor cursor =db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang", idbarang));
                cursor.moveToNext();
                ModulBengkel.setText(v,R.id.eStok,"1");
                ModulBengkel.setText(v,R.id.tiHargaJual, ModulBengkel.unNumberFormat(ModulBengkel.removeE(ModulBengkel.getString(cursor, "harga"))));
                ModulBengkel.setText(v,R.id.tvNamaBarang, ModulBengkel.getString(cursor, "barang"));
                hargabeli = ModulBengkel.strToDouble(ModulBengkel.getString(cursor, "hargabeli"));

            }

            if (idpelanggan!=null) {
                Cursor cursor = db.sq(ModulBengkel.selectwhere("tblpelanggan")+ModulBengkel.sWhere("idpelanggan", idpelanggan));
                cursor.moveToNext();
                ModulBengkel.setText(v,R.id.namaPelanggan, ModulBengkel.getString(cursor, "pelanggan"));
            }


    }

    public String getStok() {
        Cursor cursor = db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang", idbarang));
        cursor.moveToNext();
        String stokya = ModulBengkel.getString(cursor, "stok");
        return stokya;
    }

    public void getBarang(String cari) {
        setTotal();
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.keranjang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPenjualan(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qdetailjual") +ModulBengkel.sWhere("idorder",idjual)+" AND "+ ModulBengkel.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor cursor = db.sq(q) ;
        while (cursor.moveToNext()) {
            String campur = ModulBengkel.getString(cursor,"iddetailorder")+"__"+ModulBengkel.getString(cursor,"barang") + "__" + ModulBengkel.getString(cursor,"jumlah")+ "__" + ModulBengkel.getString(cursor,"hargajual")+"__"+ModulBengkel.getString(cursor,"idkategori")+"__"+ModulBengkel.getString(cursor,"teknisi");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();

    }

    public void setTotal() {
        String sql = ModulBengkel.selectwhere("tblorder") +ModulBengkel.sWhere("idorder",idjual);
        Cursor cursor = db.sq(sql);
        cursor.moveToNext();
        ModulBengkel.setText(v,R.id.tvTotal, ""+ModulBengkel.removeE(ModulBengkel.getString(cursor, "total")));

    }

    public void refresh() {
        idjual = getIdOrder();
        if (idjual.equals("belum")) {
            db.exc("INSERT INTO tblorder (idpelanggan,nopol) values (1,'kosong');");
            idjual= getIdOrder();
            refresh();
        }else {
            setText();
            getBarang("");
        }
    }

    public void setText() {
        Cursor c = db.sq(ModulBengkel.selectwhere("qorder")+ModulBengkel.sWhere("idorder",idjual));
        c.moveToNext();
        String faktur = "00000000";
        ModulBengkel.setText(v,R.id.tvFaktur,faktur.substring(0,faktur.length() - ModulBengkel.getString(c,"idorder").length())+ModulBengkel.getString(c,"idorder")) ;
        ModulBengkel.setText(v,R.id.namaPelanggan,ModulBengkel.getString(c,"pelanggan"));
        idpelanggan=ModulBengkel.getString(c,"idpelanggan");
    }

    public String cek() {
        String kondisi="belum";
        Cursor c = db.sq("SELECT * FROM tbldetailorder WHERE idorder="+idjual);
        while(c.moveToNext()){
            kondisi=ModulBengkel.getString(c,"idorder");
        }
        return kondisi;
    }

    public String getIdOrder() {
        idjual="belum";
        Cursor c = db.sq("SELECT * FROM tblorder WHERE statusbayar IS NULL");
        while(c.moveToNext()){
            idjual=ModulBengkel.getString(c,"idorder");
        }
        return idjual;
    }



    private String getDayToday() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String today = dateFormat.format(date);
        return today;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                tvTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void PilihPelanggan(View view) {
        Intent intent = new Intent(Menu_Transaksi_Bengkel_.this, Pilih_Pelanggan_Bengkel_.class);
        intent.putExtra("type", "pelanggan");
        startActivityForResult(intent,CARI_PELANGGAN);
    }

    public void PilihBarang(View view) {
        Intent intent = new Intent(Menu_Transaksi_Bengkel_.this, Pilih_Barang_Bengkel_.class);
        startActivityForResult(intent, CARI_BARANG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CARI_PELANGGAN && resultCode == RESULT_OK) {
            tvNamaPelanggan.setText(data.getStringExtra("pelanggan"));
            tvNamaPelanggan.setTextColor(Color.parseColor("#000000"));
            idpelanggan=data.getStringExtra("idpelanggan");
        }
        if (requestCode == CARI_BARANG && resultCode == RESULT_OK) {
            tvNamaBarang.setText(data.getStringExtra("barang"));
            tvNamaBarang.setTextColor(Color.parseColor("#000000"));
            idbarang = "" + data.getIntExtra("idbarang", 1);
        }
        if (requestCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");
//            idpelanggan = "" + data.getIntExtra("idpelanggan", 0);
        } else if (requestCode == 200) {
            idbarang = data.getStringExtra("idbarang");
        }
    }

    private double tambahlaba(){
        String harga = ModulBengkel.getText(v,R.id.tiHargaJual);
        String stok = ModulBengkel.getText(v,R.id.eStok);

        double hargajual = ModulBengkel.strToDouble(harga) * ModulBengkel.strToDouble(stok);
        hargabeli = hargabeli*ModulBengkel.strToDouble(stok);
        return hargajual-hargabeli;
    }


    private void simpan(){
        String barang = ModulBengkel.getText(v,R.id.tvNamaBarang);
        String deskripsi = ModulBengkel.getText(v,R.id.eStok);
        String harga = ModulBengkel.getText(v,R.id.tiHargaJual);
        String faktur=ModulBengkel.getText(v,R.id.tvFaktur);
        String tgl = ModulBengkel.getText(v,R.id.etTanggal);
        String laba = ModulBengkel.doubleToStr(tambahlaba());
        Boolean status;
        if (ModulBengkel.strToInt(deskripsi)==0 || ModulBengkel.strToInt(harga)<=0) {
            status=false;
        }else{
            status=true;
        }

        if(!TextUtils.isEmpty(barang)  && !TextUtils.isEmpty(harga) && status && !TextUtils.isEmpty(deskripsi) ){
            String[] pp = {idpelanggan,faktur,tgl,idjual} ;
            String qq = ModulBengkel.splitParam("UPDATE tblorder SET idpelanggan=?,faktur=?,tglorder=? WHERE idorder=?   ",pp) ;
            db.exc(qq);
            String[] p = {idbarang,deskripsi,harga,"1",idjual,laba} ;
            String q = ModulBengkel.splitParam("INSERT INTO tbldetailorder (idbarang,jumlah,hargajual,idteknisi,idorder,laba) values(?,?,?,?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    public void reset(){
        EditText harga,jumlah, barang;
        barang = findViewById(R.id.tvNamaBarang);
        harga = findViewById(R.id.tiHargaJual);
        jumlah = findViewById(R.id.eStok);
        barang.getText().clear();
        harga.getText().clear();
        jumlah.getText().clear();
    }

    public void pesan(String isi) {
        Toast.makeText(this, isi, Toast.LENGTH_SHORT).show();
    }

    public void simpan(View view) {
            simpan();
            reset();
            getBarang("");
    }

    public void ganti(View view) {
        String kondisi = cek();
        if (kondisi.equals("belum")) {
            ModulBengkel.showToast(this, "Keranjang Masih Kosong!");
        } else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menyimpan pesanan ini dan mengganti pesanan lain?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String sql ="UPDATE tblorder SET statusbayar='belum' WHERE idorder="+idjual;
                            db.exc(sql);
                            refresh();

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            alert = alertDialog.create();
            alert.setTitle("Ganti Pesanan");
            alert.show();
        }
    }
}


//class MasterPenjualan extends RecyclerView.Adapter<MasterPenjualan.ViewHolder> {
//    private ArrayList<String> data;
//    Context c;
//
//    public MasterPenjualan(Context a, ArrayList<String> kota) {
//        this.data = kota;
//        c = a;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView barang,deskripsi,tvHarga;
//        private ImageView tvOpt;
//
//
//        public ViewHolder(@NonNull View view) {
//            super(view);
//
//            barang = (TextView) view.findViewById(R.id.tvBarang);
//            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
//            tvOpt = (ImageView) view.findViewById(R.id.hapus);
//
//            tvHarga=view.findViewById(R.id.tvHarga);
//
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
//        String[] row = data.get(i).split("__");
//
//        viewHolder.barang.setText(row[1]);
//        if (row[4].equals("1")){
//            viewHolder.deskripsi.setText("Teknisi :"+row[5]);
//            viewHolder.tvHarga.setText(ModulBengkel.removeE(row[3]));
//            viewHolder.tvOpt.setTag(row[0]);
//        }else{
//            viewHolder.deskripsi.setText("Jumlah : "+row[2]);
//            double harga = ModulBengkel.strToDouble(row[3])*ModulBengkel.strToDouble(row[2]);
//            viewHolder.tvHarga.setText(ModulBengkel.removeE(harga));
//            viewHolder.tvOpt.setTag(row[0]);
//        }
//        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
//                AlertDialog alert;
//                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
//                        .setCancelable(false)
//                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String id = viewHolder.tvOpt.getTag().toString();
//                                Database_Bengkel_ db = new Database_Bengkel_(c);
//                                String q = "DELETE FROM tbldetailorder WHERE iddetailorder="+id ;
//                                db.exc(q);
//                                ((Penjualan)c).getBarang("");
//
//                            }
//                        })
//                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        });
//                alert=alertDialog.create();
//
//                alert.setTitle("Hapus Data");
//                alert.show();
//            }
//        });
//
//    }
//}

//class MasterPenjualan extends RecyclerView.Adapter<MasterPenjualan.ViewHolder> {
//    private ArrayList<String> data;
//    Context c;
//
//    public MasterPenjualan(Context a, ArrayList<String> kota) {
//        this.data = kota;
//        c = a;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView barang,deskripsi,tvHarga;
//        private ImageView tvOpt;
//
//
//        public ViewHolder(@NonNull View view) {
//            super(view);
//
//
//            barang = (TextView) view.findViewById(R.id.tvBarang);
//            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
//            tvOpt = (ImageView) view.findViewById(R.id.hapus);
//
//            tvHarga=view.findViewById(R.id.tvHarga);
//
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
//        String[] row = data.get(i).split("__");
//
//        viewHolder.barang.setText(row[1]);
//        if (row[4].equals("1")){
//            viewHolder.deskripsi.setText("Teknisi :"+row[5]);
//            viewHolder.tvHarga.setText(ModulBengkel.removeE(row[3]));
//            viewHolder.tvOpt.setTag(row[0]);
//        }else{
//            viewHolder.deskripsi.setText("Jumlah : "+row[2]);
//            double harga = ModulBengkel.strToDouble(row[3])*ModulBengkel.strToDouble(row[2]);
//            viewHolder.tvHarga.setText(ModulBengkel.removeE(harga));
//            viewHolder.tvOpt.setTag(row[0]);
//        }
//        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
//                AlertDialog alert;
//                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
//                        .setCancelable(false)
//                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String id = viewHolder.tvOpt.getTag().toString();
//                                Database_Bengkel_ db = new Database_Bengkel_(c);
//                                String q = "DELETE FROM tbldetailorder WHERE iddetailorder="+id ;
//                                db.exc(q);
//                                ((Penjualan)c).getBarang("");
//
//                            }
//                        })
//                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        });
//                alert=alertDialog.create();
//
//                alert.setTitle("Hapus Data");
//                alert.show();
//            }
//        });
//
//
//
//
//    }
//}



