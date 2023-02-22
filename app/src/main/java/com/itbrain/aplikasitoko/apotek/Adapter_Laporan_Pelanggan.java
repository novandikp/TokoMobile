package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Adapter_Laporan_Pelanggan extends RecyclerView.Adapter<Adapter_Laporan_Pelanggan.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Pelanggan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hutang, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setVisibility(View.GONE);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No HP : "+row[3]);
        holder.hutang.setText("Hutang : Rp "+ ModulApotek.removeE(row[4]));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt,hutang;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            hutang=(TextView) itemView.findViewById(R.id.tHutang);

        }
    }
}


