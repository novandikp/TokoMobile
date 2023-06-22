package com.itbrain.aplikasitoko.TokoKredit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.itbrain.aplikasitoko.TokoKredit.ModelTagihanKredit;

import java.util.ArrayList;
import java.util.List;

public class ActivityLaporanTagihanKredit extends AppCompatActivity {

    FConfigKredit config;
    FKoneksiKredit conn;
    List<ModelTagihanKredit> daftarLaporanTagihan;
    RecyclerView reDaftarTagihan;
    //    TextView tvJumlah, tvTotal;
    AdapterLaporanTagihan adapterLaporanTagihan;
    Spinner spStatus;
    String faktur;
    EditText eFaktur;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_tagihan_kredit);
//        getSupportActionBar().hide();
        init();
//        ModuleKredit.implementBackButton(this);
//        ModuleKredit.removeFocus(this);
        getDaftarLaporanTagihan();

        // Listen to textchange event
        handleSpinnerChange();
        cekFaktur();

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
        reDaftarTagihan = findViewById(R.id.reDaftarTagihan);
//        tvJumlah = findViewById(R.id.tvJumlah);
//        tvTotal = findViewById(R.id.tvTotal);
        eFaktur = findViewById(R.id.eFaktur);
        faktur = getIntent().getStringExtra("faktur");
        spStatus = findViewById(R.id.spStatus);
        spStatus.setEnabled(faktur != null);

        //RecyclerView stufff
        daftarLaporanTagihan = new ArrayList<>();
        adapterLaporanTagihan = new AdapterLaporanTagihan(this, daftarLaporanTagihan);
        reDaftarTagihan.setLayoutManager(new LinearLayoutManager(this));
        reDaftarTagihan.setHasFixedSize(true);
        reDaftarTagihan.setAdapter(adapterLaporanTagihan);
    }

    void cekFaktur() {
        if (getIntent().getStringExtra("faktur") != null) {
            eFaktur.setText(getIntent().getStringExtra("faktur"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaftarLaporanTagihan();
    }

    void handleSpinnerChange() {
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDaftarLaporanTagihan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("Range")
    void getDaftarLaporanTagihan() {
        int flag_tagihan = spStatus.getSelectedItemPosition();

        daftarLaporanTagihan.clear();
        String sql = "SELECT * FROM qtagihan WHERE faktur = '" + faktur + "' AND flag_tagihan = " + flag_tagihan + " ORDER BY cicilan DESC";
        Cursor res = conn.db.rawQuery(sql, null);
        if (res.getCount() > 0) {
            double jumlah = 0;
//            tvJumlah.setText("" + res.getCount());
            while (res.moveToNext()) {
                daftarLaporanTagihan.add(new ModelTagihanKredit(
                        res.getInt(res.getColumnIndex("idtagihan")),
                        res.getInt(res.getColumnIndex("cicilan")),
                        res.getString(res.getColumnIndex("tglbayar")),
                        res.getString(res.getColumnIndex("tgltempo")),
                        res.getDouble(res.getColumnIndex("jumlahangsuran")),
                        res.getInt(res.getColumnIndex("flag_tagihan"))
                ));
                jumlah += res.getDouble(res.getColumnIndex("jumlahangsuran"));
            }
//            tvTotal.setText(ModuleKredit.numFormat(jumlah));
        } else {
//            tvJumlah.setText("0");
//            tvTotal.setText("0");
        }
        adapterLaporanTagihan.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", "tagihan");
        startActivity(i);
    }

    public void cariFaktur(View v) {
        ModuleKredit.goToActivity(this, ActivityDaftarKredit.class);
    }

    public void hapusCicilan(View v) {
        ModuleKredit.yesNoDialog(this, "Peringatan", "Yakin ingin hapus tagihan ?", (dialog, which) -> {
            String updateTagihan = "UPDATE tbltagihan SET tglbayar = null, bayardenda = null, jumlahterlambat = null, flag_tagihan = 0 WHERE idtagihan = " + v.getTag().toString() + "";
            String updateKredit = "UPDATE tblkredit SET saldokredit = saldokredit + angsuran, flag_kredit = 0 WHERE faktur = '" + faktur + "'";
            Log.i("info", updateTagihan + "\n" + updateKredit);
            if (conn.exc(updateTagihan) && conn.exc(updateKredit)) {
                ModuleKredit.info(this, "Berhasil hapus tagihan");
                getDaftarLaporanTagihan();
            }
        }, (dialog, which) -> dialog.dismiss());
    }

}

class AdapterLaporanTagihan extends RecyclerView.Adapter<AdapterLaporanTagihan.DaftarLaporanTagihanViewHolder> {

    private Context ctx;
    private List<ModelTagihanKredit> daftarLaporanTagihan;

    AdapterLaporanTagihan(Context ctx, List<ModelTagihanKredit> daftarLaporanTagihan) {
        this.ctx = ctx;
        this.daftarLaporanTagihan = daftarLaporanTagihan;
    }

    @Override
    public DaftarLaporanTagihanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_tagihan_kredit, parent, false);
        return new DaftarLaporanTagihanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DaftarLaporanTagihanViewHolder holder, int position) {
        ModelTagihanKredit row = daftarLaporanTagihan.get(position);
        holder.tvCicilan.setText(row.getCicilanke() + "");
        holder.tvAngsuran.setText(ModuleKredit.numFormat(row.getAngsuran()));
        holder.tvTglTempo.setText(ModuleKredit.getDateString(row.getTgltempo(), "dd MMM yyyy"));
        holder.tvTglBayar.setText(row.getTglbayar() == null ? "Belum dibayar" : ModuleKredit.getDateString(row.getTglbayar(), "dd MMM yyyy"));
        holder.bHapus.setTag(row.getIdtagihan());
        if (row.getFlag_tagihan() != 1 || position != 0) {
            holder.bHapus.setVisibility(View.INVISIBLE);
        } else {
            holder.bHapus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return daftarLaporanTagihan.size();
    }

    class DaftarLaporanTagihanViewHolder extends RecyclerView.ViewHolder {

        TextView tvCicilan, tvAngsuran, tvTglTempo, tvTglBayar;
        CardView cvItem;
        ImageView bHapus;

        DaftarLaporanTagihanViewHolder(View itemView) {
            super(itemView);
            tvCicilan = itemView.findViewById(R.id.tvCicilan);
            tvAngsuran = itemView.findViewById(R.id.tvAngsuran);
            tvTglTempo = itemView.findViewById(R.id.tvTglTempo);
            tvTglBayar = itemView.findViewById(R.id.tvTglBayar);
            cvItem = itemView.findViewById(R.id.cvItem);
            bHapus = itemView.findViewById(R.id.bHapus);
        }

    }

}

