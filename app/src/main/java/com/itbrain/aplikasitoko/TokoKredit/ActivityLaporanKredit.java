package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;
import com.itbrain.aplikasitoko.TokoKredit.DialogInfoKredit;

import java.util.ArrayList;
import java.util.List;

public class ActivityLaporanKredit extends AppCompatActivity {

    FConfigKredit config;
    FKoneksiKredit conn;
    List<ModelKredit> daftarLaporanKredit;
    RecyclerView reDaftarLaporanKredit;
    EditText eCari;
    TextView tvJumlah, tvTotal, tvSaldo;
    AdapterLaporanKredit adapterLaporanKredit;
    View v;
    Spinner spStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kredit);
//        getSupportActionBar().hide();
        init();
//        ModuleKredit.implementBackButton(this);
//        ModuleKredit.removeFocus(this);
        getDaftarLaporanKredit("");

        // Listen to textchange event
        textChangedListenerManager();
        handleSpinnerChange();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void init() {
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        conn = new FKoneksiKredit(this, config);
        daftarLaporanKredit = new ArrayList<>();
        reDaftarLaporanKredit = findViewById(R.id.reDaftarKredit);
        eCari = findViewById(R.id.eCari);
        tvJumlah = findViewById(R.id.tvJumlah);
        tvTotal = findViewById(R.id.tvTotal);
        tvSaldo = findViewById(R.id.tvSaldo);
        v = findViewById(android.R.id.content);
        adapterLaporanKredit = new AdapterLaporanKredit(this, daftarLaporanKredit);
        reDaftarLaporanKredit.setLayoutManager(new LinearLayoutManager(this));
        reDaftarLaporanKredit.setHasFixedSize(true);
        reDaftarLaporanKredit.setAdapter(adapterLaporanKredit);
        spStatus = findViewById(R.id.spStatus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void textChangedListenerManager() {
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getDaftarLaporanKredit(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaftarLaporanKredit("");
    }

    void handleSpinnerChange() {
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDaftarLaporanKredit(eCari.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void getDaftarLaporanKredit(String cari) {
        daftarLaporanKredit.clear();
        String sql = "SELECT * FROM qkredit WHERE (pelanggan LIKE '%" + cari + "%' OR faktur LIKE '%" + cari + "%') AND flag_kredit = " + spStatus.getSelectedItemPosition() + "";
        Cursor res = conn.db.rawQuery(sql, null);
        if (res.getCount() > 0) {
            double jumlahkredit = 0, saldo = 0;
            tvJumlah.setText("" + res.getCount());
            while (res.moveToNext()) {
                Cursor barang = conn.sq("select * from tblbarang where idbarang in (select idbarang from tblpenjualan_kredit where fakturbayar = '" + res.getString(res.getColumnIndex("faktur")) + "')");
                barang.moveToFirst();
                daftarLaporanKredit.add(new ModelKredit(
                        res.getString(res.getColumnIndex("faktur")),
                        res.getString(res.getColumnIndex("tglkredit")),
                        res.getString(res.getColumnIndex("pelanggan")),
                        res.getDouble(res.getColumnIndex("angsuran")),
                        res.getDouble(res.getColumnIndex("lamakredit")),
                        res.getDouble(res.getColumnIndex("jumlahkredit"))
//                        barang.getDouble(barang.getColumnIndex("hargajual"))
                ));
                jumlahkredit += res.getDouble(res.getColumnIndex("angsuran")) * res.getDouble(res.getColumnIndex("lamakredit"));
                saldo += res.getDouble(res.getColumnIndex("saldokredit"));
            }
            tvTotal.setText(ModuleKredit.numFormat(jumlahkredit));
            tvSaldo.setText(ModuleKredit.numFormat(saldo));
        } else {
            tvJumlah.setText("0");
            tvTotal.setText("0");
            tvSaldo.setText("0");
        }
        adapterLaporanKredit.notifyDataSetChanged();
    }

    public void showInfo(View itemView) {
        Bundle args = new Bundle();
        args.putString("faktur", itemView.getTag().toString());
        DialogInfoKredit DialogInfoKredit = new DialogInfoKredit();
        DialogInfoKredit.setArguments(args);
        DialogInfoKredit.show(getSupportFragmentManager(), "Nothing");
    }

    public void cetak(View itemView) {
        String faktur = itemView.getTag().toString();
        Intent i = new Intent(this, ActivityCetakKredit.class);
        i.putExtra("faktur", faktur);
        i.putExtra("owner", ActivityLaporanKredit.class);
        startActivity(i);
    }

    public void dateDari(View view) {
        showDialog(1);
    }

    public void dateKe(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", "kredit");
        startActivity(i);
    }
}

class AdapterLaporanKredit extends RecyclerView.Adapter<AdapterLaporanKredit.DaftarLaporanKreditViewHolder> {

    private Context ctx;
    private List<ModelKredit> daftarLaporanKredit;

    AdapterLaporanKredit(Context ctx, List<ModelKredit> daftarLaporanKredit) {
        this.ctx = ctx;
        this.daftarLaporanKredit = daftarLaporanKredit;
    }

    @Override
    public DaftarLaporanKreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_laporan_kredit, parent, false);
        return new DaftarLaporanKreditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DaftarLaporanKreditViewHolder holder, int position) {
        ModelKredit row = daftarLaporanKredit.get(position);
        holder.tvFaktur.setText(row.getFaktur());
        holder.tvPelanggan.setText("Pelanggan : " + row.getPelanggan());
        holder.tvTanggal.setText("Tgl Kredit : " + FFunctionKredit.getDateString(row.getTglkredit()));
        holder.tvJumlah.setText("Jumlah Kredit : " + ModuleKredit.numFormat(row.getHargaBarang()));
        holder.bInfo.setTag(row.getFaktur());
        holder.bCetak.setTag(row.getFaktur());
    }

    @Override
    public int getItemCount() {
        return daftarLaporanKredit.size();
    }

    class DaftarLaporanKreditViewHolder extends RecyclerView.ViewHolder {

        TextView tvFaktur, tvTanggal, tvJumlah, tvPelanggan;
        ImageView bInfo, bCetak;
        CardView cvItem;

        DaftarLaporanKreditViewHolder(View itemView) {
            super(itemView);
            tvFaktur = itemView.findViewById(R.id.tvFaktur);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvPelanggan = itemView.findViewById(R.id.tvPelanggan);
            bInfo = itemView.findViewById(R.id.bInfo);
            bCetak = itemView.findViewById(R.id.bCetak);
            cvItem = itemView.findViewById(R.id.cvItem);
        }

    }

}

