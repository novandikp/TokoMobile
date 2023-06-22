package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityPenjualanTunaiKredit extends AppCompatActivity {

    String faktur = "";
    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    int year, month, day;
    Calendar calendar;
    String brg, plgn, tanggal, harga, totalbayar = "";
    RecyclerView recJual;
    List<String> daftarOrderDetail;
    ListPenjualan adapterOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_tunai_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        plgn = temp.getCustom("idpelanggan", "");
        if (TextUtils.isEmpty(plgn)) {
            temp.setCustom("idpelanggan", "1");
        }

        faktur = temp.getCustom("fakturbayar", "");
        if (TextUtils.isEmpty(faktur)) {
            getFaktur();
        } else {
            FFunctionKredit.setText(v, R.id.faktur, faktur);
        }

        Button tgl = findViewById(R.id.cariTanggal);
        tgl.setOnClickListener(v -> setDate(1));

        Button barang = findViewById(R.id.cariBarang);
        barang.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPenjualanTunaiKredit.this, MenuCariBarangKredit.class);
            i.putExtra("type", "barang");
            i.putExtra("owner", ActivityPenjualanTunaiKredit.class);
            startActivity(i);
        });

        Button pelanggan = findViewById(R.id.cariPelanggan);
        pelanggan.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPenjualanTunaiKredit.this, MenuCariPelangganKredit.class);
            i.putExtra("type", "pelanggan");
            i.putExtra("owner", ActivityPenjualanTunaiKredit.class);
            startActivity(i);
        });

        Button btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(view -> {
            String jumlah = FFunctionKredit.getText(v, R.id.jumlah);
            int jum = FFunctionKredit.strToInt(jumlah);
            jum++;
            FFunctionKredit.setText(v, R.id.jumlah, FFunctionKredit.intToStr(jum));

            hitung();
        });

        Button btnMin = findViewById(R.id.btnMin);
        btnMin.setOnClickListener(view -> {
            String jumlah = FFunctionKredit.getText(v, R.id.jumlah);
            int jum = FFunctionKredit.strToInt(jumlah);
            if (jum > 1) {
                jum--;
                FFunctionKredit.setText(v, R.id.jumlah, FFunctionKredit.intToStr(jum));

                hitung();
            }
        });

        String tot = getTotal();
        FFunctionKredit.setText(v, R.id.total, "Rp. " + FFunctionKredit.numberFormat(tot));
        setText();
        cekButtonCari();
        hitung();
        loadList();

    }

    void init() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);

        // RecyclerView stuff
        daftarOrderDetail = new ArrayList<>();
        adapterOrderDetail = new ListPenjualan(this, daftarOrderDetail);
        recJual = findViewById(R.id.recJual);
        recJual.setAdapter(adapterOrderDetail);
        recJual.setLayoutManager(new LinearLayoutManager(this));
        recJual.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        keluar();
    }

    public void hitung() {
        String jumlahbarang = FFunctionKredit.getText(v, R.id.jumlah);
        String harga = FFunctionKredit.getText(v, R.id.harga);

        if (!TextUtils.isEmpty(harga)) {
            double total = FFunctionKredit.strToDouble(totalbayar) + (FFunctionKredit.strToDouble(jumlahbarang) * FFunctionKredit.strToDouble(harga));
            String hasil = FFunctionKredit.doubleToStr(total);
            FFunctionKredit.setText(v, R.id.total, "Rp. " + FFunctionKredit.removeE(hasil));
        }
    }

    public void cekButtonCari() {
        Button btnCariPelanggan = findViewById(R.id.cariPelanggan);
        Button btnCariTanggal = findViewById(R.id.cariTanggal);

        boolean shouldEnabled = recJual.getAdapter().getItemCount() == 0;
        btnCariPelanggan.setEnabled(shouldEnabled);
        btnCariTanggal.setEnabled(shouldEnabled);
    }

    public void selesai(View v) {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblpenjualan") + FQueryKredit.sWhere("fakturbayar", faktur));
        if (c.getCount() > 0) {
            Intent i = new Intent(this, ActivityBayarKredit.class);
            i.putExtra("faktur", faktur);
            startActivity(i);
        } else {
            Toast.makeText(this, "Keranjang Masih Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    public String getTotal() {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblbayar") + FQueryKredit.sWhere("fakturbayar", faktur));
        String total = "";
        if (c.getCount() > 0) {
            c.moveToNext();
            total = removeE(FFunctionKredit.getString(c, "jumlahbayar"));
            totalbayar = total;
            return total;
        } else {
            total = "0";
            totalbayar = "0";
            return total;
        }
    }

    public void setText() {
        FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        brg = FConfigKredit.getCustom("idbarang", "");
        plgn = FConfigKredit.getCustom("idpelanggan", "");
        tanggal = FConfigKredit.getCustom("tanggal", "");

        if (!TextUtils.isEmpty(brg)) {
            getBarang();
        } else {
            FFunctionKredit.setText(v, R.id.barang, "");
        }
        if (!TextUtils.isEmpty(plgn)) {
            getPelanggan();
        } else {
            FFunctionKredit.setText(v, R.id.pelanggan, "");
        }
        if (!TextUtils.isEmpty(tanggal)) {
            FFunctionKredit.setText(v, R.id.tanggal, tanggal);
        } else {
            FFunctionKredit.setText(v, R.id.tanggal, FFunctionKredit.getDate("dd/MM/yyyy"));
        }
        FFunctionKredit.setText(v, R.id.faktur, faktur);
    }

    private void getFaktur() {
        try {
            String q = FQueryKredit.select("tblbayar") + FQueryKredit.sOrderDESC("fakturbayar");
            Cursor c = db.sq(q);
            int count = c.getCount();
            if (count < 1) {
                faktur = nol(8) + "1";
            } else {
                c.moveToNext();
                int last = Integer.parseInt(FFunctionKredit.getString(c, "fakturbayar")); // 2
                int fix = last + 1;
                int leng = String.valueOf(fix).length();
                faktur = nol(9 - leng) + fix;
            }
            temp.setCustom("fakturbayar", faktur);
            FFunctionKredit.setText(v, R.id.faktur, faktur);
        } catch (Exception e) {
            Toast.makeText(this, "Membuat Faktur Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public String nol(int total) {
        String n = "";
        for (int i = 0; i < total; i++) {
            n += "0";
        }
        return n;
    }

    public void simpan(View view) {
        String eTgl = FFunctionKredit.getText(v, R.id.tanggal);
        String eBrg = FFunctionKredit.getText(v, R.id.barang);
        String ePlgn = FFunctionKredit.getText(v, R.id.pelanggan);
        String eHrg = FFunctionKredit.getText(v, R.id.harga);
        String eJmlh = FFunctionKredit.getText(v, R.id.jumlah);
        String keterangan = FFunctionKredit.getText(v, R.id.etKeterangan);

        if (TextUtils.isEmpty(eBrg) || TextUtils.isEmpty(ePlgn) || TextUtils.isEmpty(eHrg) || TextUtils.isEmpty(eJmlh) || FFunctionKredit.strToDouble(eHrg) == 0 || FFunctionKredit.strToDouble(eJmlh) == 0) {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            Double bay = FFunctionKredit.strToDouble(eHrg) * FFunctionKredit.strToDouble(eJmlh);
            String qBayar, qpenj;
            String[] bayar;
            String[] penj = {
                    faktur,
                    convertDate(eTgl),
                    eHrg,
                    eJmlh,
                    brg,
                    "0",
                    labarugi(eHrg),
                    keterangan
            };
            String q = FQueryKredit.selectwhere("tblbayar") + FQueryKredit.sWhere("fakturbayar", faktur);
            Cursor c = db.sq(q);
            if (c.getCount() == 0) {
                bayar = new String[]{faktur, FFunctionKredit.doubleToStr(bay), plgn};
                qBayar = FQueryKredit.splitParam("INSERT INTO tblbayar (fakturbayar,jumlahbayar,idpelanggan) VALUES (?,?,?)", bayar);

                qpenj = FQueryKredit.splitParam("INSERT INTO tblpenjualan (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi, keterangan) VALUES (?,?,?,?,?,?,?,?)", penj);
            } else {
                double hasil = FFunctionKredit.strToDouble(getTotal()) + bay;
                qBayar = "UPDATE tblbayar SET jumlahbayar=" + FFunctionKredit.quote(FFunctionKredit.doubleToStr(hasil)) + " WHERE fakturbayar=" + FFunctionKredit.quote(faktur);
                qpenj = FQueryKredit.splitParam("INSERT INTO tblpenjualan (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi,keterangan) VALUES (?,?,?,?,?,?,?,?)", penj);
            }

            Cursor c2 = db.sq(FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg));
            if (c2.getCount() > 0) {
                c2.moveToNext();
                String stok = FFunctionKredit.getString(c2, "stok");
                if (FFunctionKredit.strToDouble(stok) >= FFunctionKredit.strToDouble(FFunctionKredit.getText(v, R.id.jumlah))) {
                    if (db.exc(qBayar) && db.exc(qpenj)) {
                        Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();

                        FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", ActivityPenjualanTunaiKredit.MODE_PRIVATE));
                        FConfigKredit.setCustom("idbarang", "");
                        FConfigKredit.setCustom("tanggal", "");
                        FFunctionKredit.setText(v, R.id.harga, "");
                        FFunctionKredit.setText(v, R.id.etKeterangan, "");
                        FFunctionKredit.setText(v, R.id.jumlah, "1");

                        loadList();
                        setText();
                        cekButtonCari();
                    } else {
                        Toast.makeText(this, "Pembelian Gagal, Silahkan Restart HP Anda.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Stok Tidak Cukup untuk Pemesanan", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void hapus(View view) {
        String[] id = view.getTag().toString().split("__");
        open(id[0], id[1]);
    }

    public void getBarang() {
        String q = FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg);
        Cursor c = db.sq(q);
        c.moveToNext();

        harga = removeE(FFunctionKredit.getString(c, "hargajual"));
        FFunctionKredit.setText(v, R.id.barang, FFunctionKredit.getString(c, "barang"));
        FFunctionKredit.setText(v, R.id.harga, harga);
    }

    public String removeE(String value) {
        double hasil = FFunctionKredit.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(hasil);
    }

    public void getPelanggan() {
        String q = FQueryKredit.selectwhere("tblpelanggan") + FQueryKredit.sWhere("idpelanggan", plgn);
        Cursor c = db.sq(q);
        c.moveToNext();
        FFunctionKredit.setText(v, R.id.pelanggan, FFunctionKredit.getString(c, "pelanggan"));
    }

    public String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + "-" + a[1] + "-" + a[0];
    }

    public String labarugi(String harga) {
        String q = FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg);
        Cursor c = db.sq(q);
        c.moveToNext();
        double jual = FFunctionKredit.strToDouble(harga);
        double beli = FFunctionKredit.strToDouble(FFunctionKredit.getString(c, "hargabeli"));
        double hasil = jual - beli;
        return FFunctionKredit.doubleToStr(hasil);
    }

    public void loadList() {
        try {
            daftarOrderDetail.clear();

            String q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("fakturbayar", faktur);
            Cursor c = db.sq(q);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    String campur = FFunctionKredit.getString(c, "idpenjualan") + "__" + FFunctionKredit.getString(c, "fakturbayar") + "__" +
                            FFunctionKredit.getString(c, "barang") + "__" + removeE(kali(FFunctionKredit.getString(c, "hargajual:1"), FFunctionKredit.getString(c, "jumlahjual")))
                            + "__" + FFunctionKredit.getString(c, "jumlahjual") + "__" + FFunctionKredit.getString(c, "ketpenjualan") + " ";
                    daftarOrderDetail.add(campur);
                }
            }
            adapterOrderDetail.notifyDataSetChanged();
            String tot = getTotal();
            FFunctionKredit.setText(v, R.id.total, "Rp. " + FFunctionKredit.numberFormat(tot));
        } catch (Exception e) {
            Toast.makeText(this, "Aplikasi Error. Silahkan restart HP Anda. atau silahkan hubungi komputerkit.dev@gmail.com", Toast.LENGTH_SHORT).show();
        }
    }

    public String kali(String a, String b) {
        try {
            return FFunctionKredit.doubleToStr(FFunctionKredit.strToDouble(a) * FFunctionKredit.strToDouble(b));
        } catch (Exception e) {
            return "0";
        }
    }

    //dialog
    public void open(final String id, final String min) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Hapus", (arg0, arg1) -> {
            //yes
            double hasil = FFunctionKredit.strToDouble(getTotal()) - FFunctionKredit.strToDouble(min);
            if (db.exc("UPDATE tblbayar SET jumlahbayar=" + FFunctionKredit.doubleToStr(hasil) + " WHERE fakturbayar=" + FFunctionKredit.quote(faktur)) &&
                    db.exc("DELETE FROM tblpenjualan WHERE idpenjualan=" + id)) {
                loadList();
                cekButtonCari();
            } else {
                Toast.makeText(ActivityPenjualanTunaiKredit.this, "Hapus Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void keluar() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan keluar");
        alertDialogBuilder.setPositiveButton("Iya", (arg0, arg1) -> {
            Intent i = new Intent(ActivityPenjualanTunaiKredit.this, ActivityTransaksiKredit.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        alertDialogBuilder.setNegativeButton("Batal", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //end dialog

    // Date Picker
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
            FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", ActivityPenjualanTunaiKredit.MODE_PRIVATE));
            FConfigKredit.setCustom("tanggal", FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));

            FFunctionKredit.setText(v, R.id.tanggal, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
        }
    };
    //End Date Picker
}

//ADAPTER listpenjualan

class ListPenjualan extends RecyclerView.Adapter<ListPenjualan.ViewHolder> {
    private List<String> data;
    Context c;

    public ListPenjualan(Context a, List<String> daftarOrderDetail) {
        this.data = daftarOrderDetail;
        c = a;
    }

    @Override
    public ListPenjualan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_penjualan_list_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, nma, harga;
        ConstraintLayout hapus;

        public ViewHolder(View view) {
            super(view);

            faktur = view.findViewById(R.id.tBarang);
            nma = view.findViewById(R.id.tbarang);
            harga = view.findViewById(R.id.tjumlah);
            hapus = view.findViewById(R.id.wSampah);
        }
    }

    @Override
    public void onBindViewHolder(ListPenjualan.ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.harga.setText("Rp. " + FFunctionKredit.numberFormat(row[3]) + " \n" + row[5]);
        viewHolder.nma.setText("Jumlah: " + row[4]);
        viewHolder.faktur.setText(row[2]);
        viewHolder.hapus.setTag(row[0] + "__" + row[3]);
    }
}

