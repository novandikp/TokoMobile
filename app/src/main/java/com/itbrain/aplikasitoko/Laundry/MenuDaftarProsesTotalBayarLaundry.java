package com.itbrain.aplikasitoko.Laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.Laundry.dialog.DialogTransaksiBayarLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuDaftarProsesTotalBayarLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;
    String faktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_proses_total_bayar_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        faktur = getIntent().getStringExtra("faktur");
        loadcartproses();
        setTextProses();
        ImageView imageView = findViewById(R.id.kembali12);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTextProses() {
        String q = QueryLaundry.selectwhere("qlaundry") + QueryLaundry.sWhere("faktur", faktur);
        Cursor c = db.sq(q);
        c.moveToNext();
        ModulLaundry.setText(v, R.id.edtNomorFaktur, faktur);
        ModulLaundry.setText(v, R.id.edtTglTerima, ModulLaundry.dateToNormal(ModulLaundry.getString(c, "tgllaundry")));
        ModulLaundry.setText(v, R.id.edtTglSelesai, ModulLaundry.dateToNormal(ModulLaundry.getString(c, "tglselesai")));
        ModulLaundry.setText(v, R.id.edtNamaPegawai, ModulLaundry.getString(c, "pegawai"));
        ModulLaundry.setText(v, R.id.edtNamaPelanggan, ModulLaundry.getString(c, "pelanggan"));
        ModulLaundry.setText(v, R.id.tvTotalBayar, "Rp. " + ModulLaundry.removeE(ModulLaundry.getString(c, "total")));
        String q2 = QueryLaundry.selectwhere("qproses") + QueryLaundry.sWhere("faktur", faktur);
        Cursor c2 = db.sq(q2);
        c2.moveToNext();
        ModulLaundry.setText(v, R.id.edtAlamatPelanggan, ModulLaundry.getString(c2, "alamat"));
        ModulLaundry.setText(v, R.id.edtNoTelpPelanggan, ModulLaundry.getString(c2, "notelp"));
        TextInputLayout alamat, notelp;
        alamat = (TextInputLayout) findViewById(R.id.textInputLayout26);
        notelp = (TextInputLayout) findViewById(R.id.textInputLayout27);
        if (ModulLaundry.getInt(c, "idpelanggan") == 0) {
            alamat.setVisibility(View.GONE);
            notelp.setVisibility(View.GONE);
        }
    }

    public void loadcartproses() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListProsesBayar(this, arrayList);
        recyclerView.setAdapter(adapter);

        String q = QueryLaundry.selectwhere("qcart") + QueryLaundry.sWhere("faktur", faktur);
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = ModulLaundry.getString(c, "idlaundrydetail") + "__" +
                        ModulLaundry.getString(c, "kategori") + "__" +
                        ModulLaundry.getString(c, "jasa") + "__" +
                        ModulLaundry.getString(c, "jumlahlaundry") + "__" +
                        ModulLaundry.getString(c, "satuan") + "__" +
                        ModulLaundry.getString(c, "biayalaundry") + "__Ket : " +
                        ModulLaundry.getString(c, "keterangan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Tersalin", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyNama(View view) {
        setClipboard(this, ModulLaundry.getText(v, R.id.edtNamaPelanggan));
    }

    public void copyAlamat(View view) {
        setClipboard(this, ModulLaundry.getText(v, R.id.edtAlamatPelanggan));
    }

    public void copyNoTelp(View view) {
        setClipboard(this, ModulLaundry.getText(v, R.id.edtNoTelpPelanggan));
    }
    public void bayar(View view) {
//        Intent i = new Intent(this,ActivityTransaksiBayar.class);
//        i.putExtra("faktur",faktur);
//        startActivity(i);
        DialogTransaksiBayarLaundry dialogTransaksiBayarLaundry =new DialogTransaksiBayarLaundry(this,faktur);
    }

    class AdapterListProsesBayar extends RecyclerView.Adapter<AdapterListProsesBayar.ProsesBayarViewHolder> {
        private ArrayList<String> data = new ArrayList<>();
        private Context ctx;

        public AdapterListProsesBayar(Context ctx, ArrayList<String> data) {
            this.data = data;
            this.ctx = ctx;
        }

        @NonNull
        @Override
        public ProsesBayarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_terima_laundry, viewGroup, false);
            return new ProsesBayarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProsesBayarViewHolder holder, int i) {
            final String[] row = data.get(i).split("__");

            String satuan = "";
            if (row[4].equals("pc")) {
                satuan = "Pcs";
            } else if (row[4].equals("kg")) {
                satuan = "Kg";
            } else if (row[4].equals("m2")) {
                satuan = "M²";
            }
            holder.jasa.setText("(" + row[1] + ") " + row[2] + " - " + row[3].replace(".", ",") + " " + satuan);
            holder.jasa.setSelected(true);

            holder.harga.setText("Rp. " + ModulLaundry.removeE(row[5]));
            holder.harga.setSelected(true);
            holder.hapus.setVisibility(View.GONE);
            if (row[6].equals("Ket : ")) {
                holder.keterangan.setText("Tanpa Keterangan");
            } else {
                holder.keterangan.setText(row[6]);
            }
            holder.keterangan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "";
                    if (row[6].equals("Ket : ")) {
                        message = "Tanpa Keterangan";
                    } else {
                        message = row[6];
                    }
                    AlertDialog dialog = new AlertDialog.Builder(ctx)
                            .setTitle("Keterangan " + row[1] + " - " + row[2])
                            .setMessage(message)
                            .setPositiveButton("Ok", null)
                            .create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ProsesBayarViewHolder extends RecyclerView.ViewHolder {
            TextView jasa, jumlah, satuan, harga, keterangan;
            ImageButton hapus;

            public ProsesBayarViewHolder(@NonNull View itemView) {
                super(itemView);
                jasa = (TextView) itemView.findViewById(R.id.tvNamaJasa);
                jumlah = (TextView) itemView.findViewById(R.id.tvJumlahJasa);
                satuan = (TextView) itemView.findViewById(R.id.tvSatuan);
                harga = (TextView) itemView.findViewById(R.id.tvHargaJumlah);
                keterangan = (TextView) itemView.findViewById(R.id.tvKeterangan);
                hapus = (ImageButton) itemView.findViewById(R.id.ibtnHapus);
            }
        }
    }
}
