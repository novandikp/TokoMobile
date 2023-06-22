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

public class ActivityDaftarKredit extends AppCompatActivity {

    FConfigKredit config;
    FKoneksiKredit conn;
    List<ModelKredit> daftarDaftarKredit;
    RecyclerView reDaftarDaftarKredit;
    EditText eCari;
    AdapterDaftarKredit adapterDaftarKredit;
    View v;
    Spinner spStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kredit);
        init();
//        ModuleKredit.implementBackButton(this);
//        ModuleKredit.removeFocus(this);
        getDaftarDaftarKredit("");

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
        daftarDaftarKredit = new ArrayList<>();
        reDaftarDaftarKredit = findViewById(R.id.reDaftarKredit);
        eCari = findViewById(R.id.eCari);
        v = findViewById(android.R.id.content);
        adapterDaftarKredit = new AdapterDaftarKredit(this, daftarDaftarKredit);
        reDaftarDaftarKredit.setLayoutManager(new LinearLayoutManager(this));
        reDaftarDaftarKredit.setHasFixedSize(true);
        reDaftarDaftarKredit.setAdapter(adapterDaftarKredit);
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
                getDaftarDaftarKredit(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaftarDaftarKredit("");
    }

    void handleSpinnerChange() {
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDaftarDaftarKredit(eCari.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void getDaftarDaftarKredit(String cari) {
        daftarDaftarKredit.clear();
        String sql = "SELECT * FROM qkredit WHERE (pelanggan LIKE '%" + cari + "%' OR faktur LIKE '%" + cari + "%') AND flag_kredit = " + spStatus.getSelectedItemPosition() + "";
        Cursor res = conn.db.rawQuery(sql, null);
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                Cursor barang = conn.sq("select * from tblbarang where idbarang in (select idbarang from tblpenjualan_kredit where fakturbayar = '" + res.getString(res.getColumnIndex("faktur")) + "')");
                barang.moveToFirst();
                daftarDaftarKredit.add(new ModelKredit(
                        res.getString(res.getColumnIndex("faktur")),
                        res.getString(res.getColumnIndex("tglkredit")),
                        res.getString(res.getColumnIndex("pelanggan")),
                        res.getDouble(res.getColumnIndex("angsuran")),
                        res.getDouble(res.getColumnIndex("lamakredit")),
                        barang.getDouble(barang.getColumnIndex("hargajual"))
                ));
            }
        }
        adapterDaftarKredit.notifyDataSetChanged();
    }

    public void showInfo(View itemView) {
        Bundle args = new Bundle();
        args.putString("faktur", itemView.getTag().toString());
        DialogInfoKredit dialogInfo = new DialogInfoKredit();
        dialogInfo.setArguments(args);
        dialogInfo.show(getSupportFragmentManager(), "Nothing");
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

class AdapterDaftarKredit extends RecyclerView.Adapter<AdapterDaftarKredit.DaftarDaftarKreditViewHolder> {

    private Context ctx;
    private List<ModelKredit> daftarDaftarKredit;

    AdapterDaftarKredit(Context ctx, List<ModelKredit> daftarDaftarKredit) {
        this.ctx = ctx;
        this.daftarDaftarKredit = daftarDaftarKredit;
    }

    @Override
    public DaftarDaftarKreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_laporan_kredit, parent, false);
        return new DaftarDaftarKreditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DaftarDaftarKreditViewHolder holder, int position) {
        ModelKredit row = daftarDaftarKredit.get(position);
        holder.tvFaktur.setText(row.getFaktur());
        holder.tvPelanggan.setText("Pelanggan : " + row.getPelanggan());
        holder.tvTanggal.setText("Tgl Kredit : " + FFunctionKredit.getDateString(row.getTglkredit()));
        holder.tvJumlah.setText("Jumlah Kredit : " + ModuleKredit.numFormat(row.getHargaBarang()));
        holder.bInfo.setTag(row.getFaktur());
        holder.bCetak.setVisibility(View.GONE);
        holder.cvItem.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ActivityLaporanTagihanKredit.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("faktur", row.getFaktur());
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return daftarDaftarKredit.size();
    }

    class DaftarDaftarKreditViewHolder extends RecyclerView.ViewHolder {

        TextView tvFaktur, tvTanggal, tvJumlah, tvPelanggan;
        ImageView bInfo, bCetak;
        CardView cvItem;

        DaftarDaftarKreditViewHolder(View itemView) {
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

