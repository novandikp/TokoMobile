package com.itbrain.aplikasitoko.TokoKredit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.util.Calendar;

public class ActivityPengeluaranKredit extends AppCompatActivity {

    FKoneksiKredit conn;
    FConfigKredit config;
    EditText eFaktur, eKet, eJumlah, eSaldo, eTanggal;
    LinearLayout tilSaldoWrapper;
    Integer idkategori;
    TextView tvCharFaktur, tvCharKet, tvCharJumlah;
    String type, tanggal = "";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_kredit);
        init();
        setTitle(type.equals("pemasukan") ? "Pemasukan" : "Pengeluaran");
//        ModuleKredit.implementBackButton(this);
        ModuleKredit.removeFocus(this);
        handleTextChange();
        updateSaldo();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void handleTextChange() {
        eFaktur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharFaktur.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eKet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharKet.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharJumlah.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void init() {
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        conn = new FKoneksiKredit(this, config);
        eFaktur = findViewById(R.id.eFaktur);
        eKet = findViewById(R.id.eKet);
        eJumlah = findViewById(R.id.eJumlah);
        eSaldo = findViewById(R.id.eSaldo);
        eTanggal = findViewById(R.id.eTanggal);
        tilSaldoWrapper = findViewById(R.id.tilSaldoWrapper);
        tvCharFaktur = findViewById(R.id.tvCharFaktur);
        tvCharKet = findViewById(R.id.tvCharKet);
        tvCharJumlah = findViewById(R.id.tvCharJumlah);
        type = getIntent().getStringExtra("type");
//        tilSaldoWrapper.setVisibility(type.equals("pengeluaran") ? View.VISIBLE : View.GONE);
    }

    void updateSaldo() {
        Cursor c = conn.sq("SELECT * FROM tblkeuangan ORDER BY no_transaksi DESC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            eSaldo.setText(ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("saldo"))));
        } else {
            eSaldo.setText(ModuleKredit.numFormat(0));
        }
    }

    public void tambahPemasukanPengeluaran(View view) {
        if (!ModuleKredit.notEmpty(new EditText[]{eJumlah, eFaktur, eKet, eTanggal}) || Double.parseDouble(eJumlah.getText().toString()) == 0) {
            ModuleKredit.info(this, "Harap isi field dengan benar");
            return;
        }

        // Cek faktur sama
//        Cursor c = conn.sq("SELECT * FROM tblkeuangan WHERE faktur = '" + eFaktur.getText().toString() + "'");
//        if (c.getCount() > 0) {
//            ModuleKredit.info(this, "Faktur sudah terdaftar, masukkan faktur lain");
//            return;
//        }

        Cursor tblkeuangan = conn.sq("SELECT * FROM tblkeuangan ORDER BY no_transaksi DESC");
        if (tblkeuangan.getCount() == 0 && type.equals("pengeluaran")) {
            ModuleKredit.info(this, "Belum ada pemasukan");
            return;
        } else if (tblkeuangan.getCount() > 0 && type.equals("pengeluaran")) {
            tblkeuangan.moveToFirst();
            double jumlah = Double.parseDouble(eJumlah.getText().toString());
            double saldoTerakhir = tblkeuangan.getDouble(tblkeuangan.getColumnIndex("saldo"));
            if (jumlah > saldoTerakhir) {
                ModuleKredit.info(this, "Saldo tidak cukup");
                return;
            }
        }

        int no_transaksi = 1;
        double jumlah = Double.parseDouble(eJumlah.getText().toString());
        double saldoBaru = jumlah;
        if (tblkeuangan.getCount() > 0) {
            tblkeuangan.moveToFirst();
            no_transaksi = tblkeuangan.getInt(tblkeuangan.getColumnIndex("no_transaksi")) + 1;

            double saldoterakhir = tblkeuangan.getDouble(tblkeuangan.getColumnIndex("saldo"));
            saldoBaru = type.equals("pemasukan") ? saldoterakhir + jumlah : saldoterakhir - jumlah;
        }
        double masuk, keluar;
        masuk = type.equals("pemasukan") ? jumlah : 0;
        keluar = type.equals("pengeluaran") ? jumlah : 0;

        String sql = "INSERT INTO tblkeuangan(no_transaksi, tglkeuangan, faktur, keterangan, masuk, keluar, saldo) " +
                "VALUES(" + no_transaksi + ", '" + tanggal + "', '" + eFaktur.getText().toString() + "', '" + eKet.getText().toString() + "', " + masuk + ", " + keluar + ", " + saldoBaru + " )";

        if (conn.exc(sql)) {
            ModuleKredit.info(this, "Berhasil mencatat " + type);
            resetForm();
            updateSaldo();
        }
    }

    void resetForm() {
        eFaktur.getText().clear();
        eKet.getText().clear();
        eJumlah.getText().clear();
        eTanggal.getText().clear();
    }

    public void showDate(View view) {
        showDialog(0x9);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = (arg0, year, month, dayOfMonth) -> {
        String currentMonth, currentDay;
        if ((month + 1) < 10) {
            currentMonth = "0" + (month + 1);
        } else {
            currentMonth = "" + (month + 1);
        }

        if (dayOfMonth < 10) {
            currentDay = "0" + dayOfMonth;
        } else {
            currentDay = "" + dayOfMonth;
        }

        tanggal = year + "-" + currentMonth + "-" + currentDay;
        eTanggal.setText(ModuleKredit.getDateString(tanggal, "dd MMM yyyy"));
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0x9) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        return super.onCreateDialog(id);
    }

}
