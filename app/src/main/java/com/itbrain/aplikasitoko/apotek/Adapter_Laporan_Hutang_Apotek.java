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


class Adapter_Laporan_Hutang_Apotek extends RecyclerView.Adapter<Adapter_Laporan_Hutang_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Hutang_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_detailbarang_apotek, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setVisibility(View.GONE);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText("Total Pembayaran : "+ ModulApotek.removeE(row[3]));




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tfaktur);
            alamat = (TextView) itemView.findViewById(R.id.t3);
            notelp=(TextView) itemView.findViewById(R.id.t1);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);


        }
    }
}
