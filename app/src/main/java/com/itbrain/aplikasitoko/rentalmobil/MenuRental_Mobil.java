package com.itbrain.aplikasitoko.rentalmobil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuRental_Mobil extends AppCompatActivity {

    DatabaseRentalMobil db;
    String idjual;
    View v;
    ArrayList arrayList = new ArrayList();
    int year, day, month, hour, minute;
    int year1, day1, month1, hour2, minute2;
    Calendar calendar, calend, calaw;
    boolean stat = false;
    String idpelanggan, idbarang, idkategori = "default", idpegawai;
    String umur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rental_mobil);
        calend = Calendar.getInstance();
        calaw = Calendar.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year1 = calendar.get(Calendar.YEAR);
        month1 = calendar.get(Calendar.MONTH);
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        hour2 = calendar.get(Calendar.HOUR_OF_DAY);
        minute2 = calendar.get(Calendar.MINUTE);
        db = new DatabaseRentalMobil(this);
        v = this.findViewById(android.R.id.content);
        String type = getIntent().getStringExtra("type");
        if (type.equals("transaksi")) {
            idjual = getIdOrder();
            refresh();
        } else {
            idjual = getIntent().getStringExtra("idorder");
            setText();
        }

        String tgl = ModulRentalMobil.getDate("dd/MM/yyyy HH:mm");
        ModulRentalMobil.setText(v, R.id.eTanggal, tgl);
        ModulRentalMobil.setText(v, R.id.eTgl, tgl);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            Cursor c = db.sq(ModulRentalMobil.selectwhere("tblkendaraan") + ModulRentalMobil.sWhere("idkendaraan", idbarang));
            c.moveToNext();
            ModulRentalMobil.setText(v, R.id.eHarga, ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(ModulRentalMobil.getString(c, "harga"))));
            ModulRentalMobil.setText(v, R.id.eHargaJam, ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(ModulRentalMobil.getString(c, "hargajam"))));
            ModulRentalMobil.setText(v, R.id.eHargaMenit, ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(ModulRentalMobil.getString(c, "hargamenit"))));
            ModulRentalMobil.setText(v, R.id.eBarang, ModulRentalMobil.getString(c, "mobil"));
        }
        if (idpelanggan != null) {
            Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpelanggan") + ModulRentalMobil.sWhere("idpelanggan", idpelanggan));
            c.moveToNext();
            ModulRentalMobil.setText(v, R.id.ePelanggan, ModulRentalMobil.getString(c, "pelanggan"));
        }
        if (idpegawai != null) {
            Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpegawai") + ModulRentalMobil.sWhere("idpegawai", idpegawai));
            c.moveToNext();
            ModulRentalMobil.setText(v, R.id.ePegawai, ModulRentalMobil.getString(c, "pegawai"));
        }

    }

    public String getStok() {
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblbarang") + ModulRentalMobil.sWhere("idbarang", idbarang));
        c.moveToNext();
        String stokya = ModulRentalMobil.getString(c, "stok");
        return stokya;
    }

    public void getBarang(String cari) {
        setTotal();
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPenjualanMobil(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulRentalMobil.selectwhere("view_rentaldetail") + ModulRentalMobil.sWhere("idrental", idjual) + " AND " + ModulRentalMobil.sLike("mobil", cari) + " ORDER BY mobil ASC";
        ;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur = ModulRentalMobil.getString(c, "idrentaldetail") + "__" + ModulRentalMobil.getString(c, "mobil") + "__" + ModulRentalMobil.getString(c, "plat") + "__" + ModulRentalMobil.getString(c, "tahunkeluaran") + "__"
                    + ModulRentalMobil.getString(c, "tglmulai") + "__" + ModulRentalMobil.getString(c, "tglselesai") + "__" + ModulRentalMobil.getString(c, "jumlahhari") + "__" + ModulRentalMobil.getString(c, "hargarental") + "__" + ModulRentalMobil.getString(c, "jumlahjam") + "__" + ModulRentalMobil.getString(c, "hargarentaljam") + "__" + ModulRentalMobil.getString(c, "jumlahmenit") + "__" + ModulRentalMobil.getString(c, "hargarentalmenit");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void tambahstok(View view) {
//        int stok =ModulRentalMobil.strToInt(ModulRentalMobil.getText(v,R.id.eStok))+1;
//        ModulRentalMobil.setText(v,R.id.eStok,ModulRentalMobil.intToStr(stok));
    }

    public void kurangstok(View view) {
//        int stok =ModulRentalMobil.strToInt(ModulRentalMobil.getText(v,R.id.eStok))-1;
//        ModulRentalMobil.setText(v,R.id.eStok,ModulRentalMobil.intToStr(stok));
    }

    public void setTotal() {
        String q = ModulRentalMobil.selectwhere("tblrental") + ModulRentalMobil.sWhere("idrental", idjual);
        ;
        Cursor c = db.sq(q);
        c.moveToNext();
        ModulRentalMobil.setText(v, R.id.eTotal, ModulRentalMobil.removeE(ModulRentalMobil.getString(c, "total")));
    }

    public void refresh() {
        idjual = getIdOrder();
        if (idjual.equals("belum")) {
            db.exc("INSERT INTO tblrental (idpelanggan,idpegawai) values (1,1);");
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
            ModulRentalMobil.showToast(this, getString(R.string.kosong));
        } else {

            Intent i = new Intent(this, MenuDP_Mobil.class);
            i.putExtra("idrental", idjual);
            startActivity(i);
        }
    }

    public void setText() {
        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rental") + ModulRentalMobil.sWhere("idrental", idjual));
        c.moveToNext();
        String faktur = "00000000";
        ModulRentalMobil.setText(v, R.id.eFaktur, faktur.substring(0, faktur.length() - ModulRentalMobil.getString(c, "idrental").length()) + ModulRentalMobil.getString(c, "idrental"));
        ModulRentalMobil.setText(v, R.id.ePelanggan, ModulRentalMobil.getString(c, "pelanggan"));
        ModulRentalMobil.setText(v, R.id.ePegawai, ModulRentalMobil.getString(c, "pegawai"));
        idpelanggan = ModulRentalMobil.getString(c, "idpelanggan");
        idpegawai = ModulRentalMobil.getString(c, "idpegawai");

    }

    public String cek() {
        String kondisi = "belum";
        Cursor c = db.sq("SELECT * FROM tblrentaldetail WHERE idrental=" + idjual);
        while (c.moveToNext()) {
            kondisi = ModulRentalMobil.getString(c, "idrental");
        }
        return kondisi;
    }

    public String getIdOrder() {
        idjual = "belum";
        Cursor c = db.sq("SELECT * FROM tblrental WHERE flagrental =0");
        while (c.moveToNext()) {
            idjual = ModulRentalMobil.getString(c, "idrental");
        }
        return idjual;
    }

    public void kalendar(View view) {
        showDialog(1);
    }

    public void kalender(View view) {
        showDialog(2);
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
        } else if (id == 2) {
            return new DatePickerDialog(this, date1, year1, month1, day1);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            new TimePickerDialog(MenuRental_Mobil.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    ModulRentalMobil.setText(v, R.id.eTanggal, ModulRentalMobil.setDatePickerNormal(thn, bln + 1, day) + " " + ModulRentalMobil.setTimePickerNormal(hour, minute));
                    calaw.set(thn, bln, day, hour, minute);
                    MenuRental_Mobil.this.minute = minute;
                    MenuRental_Mobil.this.hour = hour;
                }
            }, hour, minute, true).show();

        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            new TimePickerDialog(MenuRental_Mobil.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    ModulRentalMobil.setText(v, R.id.eTgl, ModulRentalMobil.setDatePickerNormal(thn, bln + 1, day) + " " + ModulRentalMobil.setTimePickerNormal(hour, minute));
                    calend.set(thn, bln, day, hour, minute);
                    MenuRental_Mobil.this.minute2 = minute;
                    MenuRental_Mobil.this.hour2 = hour;
                }
            }, hour2, minute2, true).show();

        }
    };

    public void pelanggan(View view) {
        Intent i = new Intent(this, MenuPilih_Mobil.class);
        i.putExtra("type", "pelanggan");
        startActivityForResult(i, 100);
    }

    public void barang(View view) {
        Intent i = new Intent(this, MenuPilih_Mobil.class);
        i.putExtra("type", "jasa");
        startActivityForResult(i, 200);
    }

    private void simpan() {
        String barang = ModulRentalMobil.getText(v, R.id.eBarang);
        String kmmulai = ModulRentalMobil.getText(v, R.id.eKM);
        String harga = ModulRentalMobil.unNumberFormat(ModulRentalMobil.getText(v, R.id.eHarga));
        String hargaJam = ModulRentalMobil.unNumberFormat(ModulRentalMobil.getText(v, R.id.eHargaJam));
        String hargaMenit = ModulRentalMobil.unNumberFormat(ModulRentalMobil.getText(v, R.id.eHargaMenit));
        String faktur = ModulRentalMobil.getText(v, R.id.eFaktur);
        String tglawal = ModulRentalMobil.getText(v, R.id.eTanggal);
        String tglakhir = ModulRentalMobil.getText(v, R.id.eTgl);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            calaw.setTime(sdf.parse(tglawal));
            calend.setTime(sdf.parse(tglakhir));
        } catch (Exception e) {

        }
        String jumlahari = ModulRentalMobil.intToStr(ModulRentalMobil.daysBetween(calaw.getTime(), calend.getTime()));
        if (ModulRentalMobil.minuteBetween(calaw.getTime(), calend.getTime()) == 0) {
            Toast.makeText(this, "Tanggal Mulai tidak boleh sama dengan tanggal pengembalian", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(harga) && !TextUtils.isEmpty(kmmulai) && ModulRentalMobil.minuteBetween(calaw.getTime(), calend.getTime()) > 0) {

            int menit = ModulRentalMobil.minuteBetween(calaw.getTime(), calend.getTime());
            int jam = ModulRentalMobil.getHour(menit);
            menit = ModulRentalMobil.getSisaMinute(menit);
            int hari = ModulRentalMobil.getHari(jam);
            jam = ModulRentalMobil.getSisaHour(jam);

            String[] pp = {faktur, idpelanggan, idpegawai, idjual};
            String qq = ModulRentalMobil.splitParam("UPDATE tblrental SET faktur=?,idpelanggan=?,idpegawai=? WHERE idrental=?   ", pp);
            db.exc(qq);
            String[] p = {idjual, idbarang, tglawal, tglakhir, harga, ModulRentalMobil.intToStr(hari), kmmulai, hargaJam, ModulRentalMobil.intToStr(jam), hargaMenit, ModulRentalMobil.intToStr(menit)};

            String q = ModulRentalMobil.splitParam("INSERT INTO tblrentaldetail (idrental,idkendaraan,tglmulai,tglselesai,hargarental,jumlahhari,kmmulai,hargarentaljam,jumlahjam,hargarentalmenit,jumlahmenit) values(?,?,?,?,?,?,?,?,?,?,?)", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                reset();
                getBarang("");
                setText();

            } else {
                Toast.makeText(this, "Gagal Menambah " + ", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");
        } else if (resultCode == 200) {
            idbarang = data.getStringExtra("idbarang");

        } else if (resultCode == 300) {
            idpegawai = data.getStringExtra("idteknisi");
        }
    }

    public void reset() {
        EditText barang, harga, jumlah, hargaJam, hargaMenit;
        barang = findViewById(R.id.eBarang);
        harga = findViewById(R.id.eHarga);
        jumlah = findViewById(R.id.eKM);
        hargaJam = findViewById(R.id.eHargaJam);
        hargaMenit = findViewById(R.id.eHargaMenit);
        barang.getText().clear();
        harga.getText().clear();
        jumlah.getText().clear();
        hargaJam.getText().clear();
        hargaMenit.getText().clear();
        idbarang = null;

    }

    public void simpan(View view) {
        simpan();

    }

    public void ganti(View view) {
        String kondisi = cek();
        if (kondisi.equals("belum")) {
            ModulRentalMobil.showToast(this, "Keranjang Masih Kosong");
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menyimpan pesanan ini dan mengganti pesanan lain ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String qq = "UPDATE tblperiksa SET flagperiksa=1 WHERE idperiksa=" + idjual;
                            db.exc(qq);
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

    public void teknisi(View view) {
        Intent i = new Intent(this, MenuPilih_Mobil.class);
        i.putExtra("type", "teknisi");
        startActivityForResult(i, 300);
    }

}


    class MasterPenjualanMobil extends RecyclerView.Adapter<MasterPenjualanMobil.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public MasterPenjualanMobil(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_mobil, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mobil, deskripsi, tanggal, harga;
            private ImageView tvOpt;


            public ViewHolder(View view) {
                super(view);

                mobil = view.findViewById(R.id.t1);

                deskripsi = (TextView) view.findViewById(R.id.t2);
                tanggal = (TextView) view.findViewById(R.id.t3);
                harga = view.findViewById(R.id.t4);
                tvOpt = (ImageView) view.findViewById(R.id.tvOpt);


            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.mobil.setText(row[1]);
            viewHolder.deskripsi.setText(row[2] + " - " + row[3]);
            viewHolder.tanggal.setText(row[4] + " - " + row[5]);
            double total = ModulRentalMobil.strToDouble(row[6]) * ModulRentalMobil.strToDouble(row[7]) + ModulRentalMobil.strToDouble(row[8]) * ModulRentalMobil.strToDouble(row[9]) + ModulRentalMobil.strToDouble(row[10]) * ModulRentalMobil.strToDouble(row[11]);
            StringBuilder sb = new StringBuilder();
            if (ModulRentalMobil.strToDouble(row[6]) > 0) {
                sb.append(row[6] + " hari ");
            }
            if (ModulRentalMobil.strToDouble(row[8]) > 0) {
                sb.append(row[8] + " jam ");
            }
            if (ModulRentalMobil.strToDouble(row[10]) > 0) {
                sb.append(row[10] + " menit ");
            }
            sb.append("= " + ModulRentalMobil.removeE(total));
            viewHolder.harga.setText(sb.toString());
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
                                    DatabaseRentalMobil db = new DatabaseRentalMobil(c);
                                    String q = "DELETE FROM tblrentaldetail WHERE idrentaldetail=" + id;
                                    db.exc(q);
                                    ((MenuRental_Mobil) c).getBarang("");


                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    alert = alertDialog.create();

                    alert.setTitle("Hapus Data");
                    alert.show();
                }
            });


        }
    }

