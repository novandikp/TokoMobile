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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityBayarTagihanKredit extends AppCompatActivity implements DialogBayarTagihanKredit.ListenerDialogTagihan {

    FConfigKredit config;
    FKoneksiKredit conn;
    List<ModelKredit> daftarKredit;
    RecyclerView reDaftarKredit;
    EditText eCari;
    TextView tvJumlah, tvTotal, tvSaldo;
    AdapterKredit adapterKredit;
    View v;

    void init() {
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        conn = new FKoneksiKredit(this, config);
        daftarKredit = new ArrayList<>();
        reDaftarKredit = findViewById(R.id.reDaftarKredit);
        eCari = findViewById(R.id.eCari);
        tvJumlah = findViewById(R.id.tvJumlah);
        tvTotal = findViewById(R.id.tvTotal);
        tvSaldo = findViewById(R.id.tvSaldo);
        v = findViewById(android.R.id.content);
        adapterKredit = new AdapterKredit(this, daftarKredit);
        reDaftarKredit.setLayoutManager(new LinearLayoutManager(this));
        reDaftarKredit.setHasFixedSize(true);
        reDaftarKredit.setAdapter(adapterKredit);
    }

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
        setContentView(R.layout.activity_bayar_tagihan_kredit);
        init();
//        ModuleKredit.implementBackButton(this);
        ModuleKredit.removeFocus(this);
        getDaftarKredit("");

        // Listen to textchange event
        textChangedListenerManager();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void textChangedListenerManager() {
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getDaftarKredit(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaftarKredit("");
    }

    void getDaftarKredit(String cari) {
        daftarKredit.clear();
        String sql = "SELECT * FROM qkredit WHERE (pelanggan LIKE '%" + cari + "%' OR faktur LIKE '%" + cari + "%') AND flag_kredit = 0";
        Cursor res = conn.db.rawQuery(sql, null);
        if (res.getCount() > 0) {
            Double jumlahkredit = 0.0, saldo = 0.0;
            tvJumlah.setText("" + res.getCount());
            while (res.moveToNext()) {
                Cursor barang = conn.sq("select * from tblbarang where idbarang in (select idbarang from tblpenjualan_kredit where fakturbayar = '" + res.getString(res.getColumnIndex("faktur")) + "')");
                barang.moveToFirst();
                daftarKredit.add(new ModelKredit(
                        res.getString(res.getColumnIndex("faktur")),
                        res.getString(res.getColumnIndex("tglkredit")),
                        res.getString(res.getColumnIndex("pelanggan")),
                        res.getDouble(res.getColumnIndex("angsuran")),
                        res.getDouble(res.getColumnIndex("lamakredit")),
                        res.getDouble(res.getColumnIndex("jumlahkredit"))
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
        adapterKredit.notifyDataSetChanged();
    }

    public void showInfo(View itemView) {
        Bundle args = new Bundle();
        args.putString("faktur", itemView.getTag().toString());
        DialogInfoKredit dialogInfo = new DialogInfoKredit();
        dialogInfo.setArguments(args);
        dialogInfo.show(getSupportFragmentManager(), "Nothing");
    }

    public void bayarTagihan(View itemView) {
        Bundle args = new Bundle();
        args.putString("faktur", itemView.getTag().toString());
        DialogBayarTagihanKredit dialogBayarTagihan = new DialogBayarTagihanKredit();
        dialogBayarTagihan.setArguments(args);
        dialogBayarTagihan.show(getSupportFragmentManager(), "Nothing");
    }

    @Override
    public void onCreditPaid(String faktur) {
        getDaftarKredit("");

        Intent i = new Intent(this, ActivityCetakTagihanKredit.class);
        i.putExtra("faktur", faktur);
        startActivity(i);
    }
}

class AdapterKredit extends RecyclerView.Adapter<AdapterKredit.DaftarKreditViewHolder> {

    private Context ctx;
    private List<ModelKredit> daftarKredit;

    AdapterKredit(Context ctx, List<ModelKredit> daftarKredit) {
        this.ctx = ctx;
        this.daftarKredit = daftarKredit;
    }

    @Override
    public DaftarKreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_kredit, parent, false);
        return new DaftarKreditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DaftarKreditViewHolder holder, int position) {
        ModelKredit row = daftarKredit.get(position);
        holder.tvFaktur.setText(row.getFaktur());
        holder.tvPelanggan.setText("Pelanggan : " + row.getPelanggan());
        holder.tvTanggal.setText("Tgl Kredit : " + FFunctionKredit.getDateString(row.getTglkredit()));
        holder.tvJumlah.setText("Jumlah Kredit : " + ModuleKredit.numFormat(row.getHargaBarang()));
        holder.bInfo.setTag(row.getFaktur());
        holder.cvItem.setTag(row.getFaktur());
    }

    @Override
    public int getItemCount() {
        return daftarKredit.size();
    }

    class DaftarKreditViewHolder extends RecyclerView.ViewHolder {

        TextView tvFaktur, tvTanggal, tvJumlah, tvPelanggan;
        ImageView bInfo;
        CardView cvItem;

        DaftarKreditViewHolder(View itemView) {
            super(itemView);
            tvFaktur = itemView.findViewById(R.id.tvFaktur);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvPelanggan = itemView.findViewById(R.id.tvPelanggan);
            bInfo = itemView.findViewById(R.id.bInfo);
            cvItem = itemView.findViewById(R.id.cvItem);
        }

    }

}