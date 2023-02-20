package com.itbrain.aplikasitoko.klinik;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Menu_pemeriksaan_klinik extends AppCompatActivity {
    DatabaseKlinik db;
    String idjual;
    View v;
    ArrayList arrayList = new ArrayList();
    int year, day, month;
    Calendar calendar;
    boolean stat = false;
    String idpelanggan, idbarang, idkategori = "default", iddokter = "default";
    String umur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pemeriksaan_klinik);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        db = new DatabaseKlinik(this);

        ImageButton imageButton = findViewById(R.id.Keluar);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        v = this.findViewById(android.R.id.content);

        String type=getIntent().getStringExtra("type");
        if (type.equals("transaksi")){
            idjual=getIdOrder();
            refresh();
        }else{
            idjual=getIntent().getStringExtra("idorder");
            setText();
        }

        String tgl = ModulKlinik.getDate("dd/MM/yyyy");
        ModulKlinik.setText(v, R.id.eTanggal, tgl);
        getBarang("");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (idbarang != null) {
            Cursor c = db.sq(ModulKlinik.selectwhere("tbljasa") + ModulKlinik.sWhere("idjasa", idbarang));
            c.moveToNext();
            ModulKlinik.setText(v, R.id.eHarga, ModulKlinik.unNumberFormat(ModulKlinik.removeE(ModulKlinik.getString(c, "harga"))));
            ModulKlinik.setText(v, R.id.eBarang, ModulKlinik.getString(c, "jasa"));
        }
        if (idpelanggan != null) {
            Cursor c = db.sq(ModulKlinik.selectwhere("tblpasien") + ModulKlinik.sWhere("idpasien", idpelanggan));
            c.moveToNext();
            ModulKlinik.setText(v, R.id.ePelanggan, ModulKlinik.getString(c, "pasien"));
            umur = ModulKlinik.getUmur(ModulKlinik.getString(c, "umur"));

        }
        if (iddokter != null) {
            Cursor c = db.sq(ModulKlinik.selectwhere("tbldokter") + ModulKlinik.sWhere("iddokter", "1"));
            c.moveToNext();
            ModulKlinik.setText(v, R.id.eTeknisi, ModulKlinik.getString(c, "dokter"));
            iddokter = "1";
        } else {
            Cursor c = db.sq(ModulKlinik.selectwhere("tbldokter") + ModulKlinik.sWhere("iddokter", iddokter));
            c.moveToNext();
            ModulKlinik.setText(v, R.id.eTeknisi, ModulKlinik.getString(c, "dokter"));
        }
    }

    public String getStok() {
        Cursor c = db.sq(ModulKlinik.selectwhere("tblbarang") + ModulKlinik.sWhere("idbarang", idbarang));
        c.moveToNext();
        String stokya = ModulKlinik.getString(c, "stok");
        return stokya;
    }

    public void getBarang(String cari) {
        setTotal();
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPenjualan(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("view_detailperiksa") + ModulKlinik.sWhere("idperiksa", idjual) + " AND " + ModulKlinik.sLike("jasa", cari) + " ORDER BY jasa ASC";
        ;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur = ModulKlinik.getString(c, "iddetailperiksa") + "__" + ModulKlinik.getString(c, "jasa") + "__" + ModulKlinik.getString(c, "harga") + "__" + ModulKlinik.getString(c, "dokter") + "__" + ModulKlinik.getString(c, "keterangan");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahstok(View view) {
        int stok = ModulKlinik.strToInt(ModulKlinik.getText(v, R.id.eStok)) + 1;
        ModulKlinik.setText(v, R.id.eStok, ModulKlinik.intToStr(stok));
    }

    public void kurangstok(View view) {
        int stok = ModulKlinik.strToInt(ModulKlinik.getText(v, R.id.eStok)) - 1;
        ModulKlinik.setText(v, R.id.eStok, ModulKlinik.intToStr(stok));
    }

    public void setTotal() {
        String q = ModulKlinik.selectwhere("view_periksa") + ModulKlinik.sWhere("idperiksa", idjual);
        ;
        Cursor c = db.sq(q);
        c.moveToNext();
        ModulKlinik.setText(v, R.id.eTotal, ModulKlinik.removeE(ModulKlinik.getString(c, "total")));
    }

    public void refresh() {
        idjual = getIdOrder();
        if (idjual.equals("belum")) {
            db.exc("INSERT INTO tblperiksa (idpasien) values (1);");
            idjual = getIdOrder();
            refresh();
        } else {
            setText();
            getBarang("");
        }
    }

    public void bayar(View view) {
        String kondisi = cek();
        if (kondisi.equals("belum")) {
            ModulKlinik.showToast(this, "Keranjang Masih Kosong");
        } else {
            finish();
            Intent i = new Intent(Menu_pemeriksaan_klinik.this, MenuBayarKLinik.class);
            i.putExtra("idorder", idjual);
            startActivity(i);
        }

    }

    public void setText() {
        Cursor c = db.sq(ModulKlinik.selectwhere("view_periksa") + ModulKlinik.sWhere("idperiksa", idjual));
        c.moveToNext();
        String faktur = "00000000";
        ModulKlinik.setText(v, R.id.eFaktur, faktur.substring(0, faktur.length() - ModulKlinik.getString(c, "idperiksa").length()) + ModulKlinik.getString(c, "idperiksa"));
        ModulKlinik.setText(v, R.id.ePelanggan, ModulKlinik.getString(c, "pasien"));
        idpelanggan = ModulKlinik.getString(c, "idpasien");
        umur = ModulKlinik.getUmur(ModulKlinik.getString(c, "umur"));


    }

    public String cek() {
        String kondisi = "belum";
        Cursor c = db.sq("SELECT * FROM tbldetailperiksa WHERE idperiksa=" + idjual);
        while (c.moveToNext()) {
            kondisi = ModulKlinik.getString(c, "idperiksa");
        }
        return kondisi;
    }

    public String getIdOrder() {
        idjual = "belum";
        Cursor c = db.sq("SELECT * FROM tblperiksa WHERE flagperiksa =0");
        while (c.moveToNext()) {
            idjual = ModulKlinik.getString(c, "idperiksa");
        }
        return idjual;
    }

    public void kalendar(View view) {
        showDialog(1);
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v, R.id.eTanggal, ModulKlinik.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    public void pelanggan(View view) {
        Intent i = new Intent(Menu_pemeriksaan_klinik.this, MenuPilihKlinik.class);
        i.putExtra("type", "pelanggan");
        startActivityForResult(i, 100);
    }

    public void barang(View view) {
        Intent i = new Intent(Menu_pemeriksaan_klinik.this, MenuPilihKlinik.class);
        i.putExtra("type", "jasa");
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");
        } else if (resultCode == 200) {
            idbarang = data.getStringExtra("idbarang");
            idkategori = data.getStringExtra("idkategori");

        } else if (resultCode == 300) {
            iddokter = data.getStringExtra("idteknisi");
        }
    }
    private void simpan(){
        String barang = ModulKlinik.getText(v,R.id.eBarang);

        String harga = ModulKlinik.unNumberFormat(ModulKlinik.getText(v,R.id.eHarga));
        String faktur=ModulKlinik.getText(v,R.id.eFaktur);
        String tgl = ModulKlinik.getText(v,R.id.eTanggal);
        String keterangan= TextUtils.isEmpty(ModulKlinik.getText(v,R.id.eKet)) ? "Tanpa Keterangan" : ModulKlinik.getText(v,R.id.eKet);
        if(!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(harga) ){
            String[] pp = {faktur,tgl,idpelanggan,umur,idjual} ;
            String qq = ModulKlinik.splitParam("UPDATE tblperiksa SET fakturperiksa=?,tglperiksa=?,idpasien=?,umurperiksa=? WHERE idperiksa=?   ",pp) ;
            db.exc(qq);
            String[] p={idjual,iddokter,idbarang,harga,keterangan};

            String q = ModulKlinik.splitParam("INSERT INTO tbldetailperiksa (idperiksa,iddokter,idjasa,biaya,keterangan) values(?,?,?,?,?)",p) ;
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
        EditText barang,harga,jumlah;
        barang=findViewById(R.id.eBarang);
        harga=findViewById(R.id.eHarga);
        jumlah=findViewById(R.id.eStok);
        barang.getText().clear();
        harga.getText().clear();
        jumlah.getText().clear();


    }


    public void simpan(View view) {
        simpan();
        reset();
        getBarang("");
        setText();
    }



    public void ganti(View view) {
        String kondisi=cek();
        if (kondisi.equals("belum")){
            ModulKlinik.showToast(this,"Keranjang Masih Kosong");
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menyimpan pesanan ini dan mengganti pesanan lain ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String qq ="UPDATE tblperiksa SET flagperiksa=1 WHERE idperiksa="+idjual;
                            db.exc(qq);
                            refresh();

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Ganti Pesanan");
            alert.show();
        }

    }


    public void teknisi(View view) {
        Intent i = new Intent(Menu_pemeriksaan_klinik.this,MenuPilihKlinik.class);
        i.putExtra("type","teknisi");
        startActivityForResult(i,300);
    }


    public void kembali(View view) {
        Intent intent = new Intent(Menu_pemeriksaan_klinik.this,AplikasiKlinik_Menu_Transaksi.class);
        startActivity(intent);
    }
}

class MasterPenjualan extends RecyclerView.Adapter<MasterPenjualan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPenjualan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_klinik, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi,tvHarga,dokter;
        private ImageView tvOpt;


        public ViewHolder(View view) {
            super(view);

            dokter = view.findViewById(R.id.tvDokter);
            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
            tvOpt = (ImageView) view.findViewById(R.id.hapus);

            tvHarga=view.findViewById(R.id.tvHarga);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        viewHolder.deskripsi.setText("Biaya : "+ModulKlinik.removeE(row[2]));
        viewHolder.dokter.setText("Ditangani oleh: "+row[3]);
        viewHolder.tvHarga.setText(row[4]);
        viewHolder.tvOpt.setTag(row[0]);
        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = viewHolder.tvOpt.getTag().toString();
                                DatabaseKlinik db = new DatabaseKlinik(c);
                                String q = "DELETE FROM tbldetailperiksa WHERE iddetailperiksa="+id ;
                                db.exc(q);
                                ((Menu_pemeriksaan_klinik)c).getBarang("");

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
}