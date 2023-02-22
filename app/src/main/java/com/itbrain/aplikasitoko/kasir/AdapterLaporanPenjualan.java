package com.itbrain.aplikasitoko.kasir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class AdapterLaporanPenjualan extends RecyclerView.Adapter<AdapterLaporanPenjualan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanPenjualan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_penjualan_item_kasir, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, nma, jumlah, tanggal;
        ConstraintLayout print ;;

        public ViewHolder(View view) {
            super(view);

            nma = (TextView) view.findViewById(R.id.tHitung);
            tanggal = (TextView) view.findViewById(R.id.tTanggal);
            faktur = (TextView) view.findViewById(R.id.tNama);
            jumlah = (TextView) view.findViewById(R.id.tBarang);
            print = (ConstraintLayout) view.findViewById(R.id.wHapus);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.jumlah.setText(row[2] + "\t : "+row[3]);
        viewHolder.nma.setText(row[1]);
        viewHolder.tanggal.setText(row[4]);
        viewHolder.faktur.setText(row[0]);
        viewHolder.print.setTag(row[0]);
    }

}

