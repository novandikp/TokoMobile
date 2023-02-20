package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Adapter_Hutang_Apotek extends RecyclerView.Adapter<Adapter_Hutang_Apotek.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public Adapter_Hutang_Apotek(Context a, ArrayList<String> kota) {
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
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No Telp : "+row[3]);
        holder.hutang.setText("Total Hutang : "+ ModulApotek.removeE(row[4]));
        final String type = row[5];
        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(c, Menu_Bayar_Hutang_Apotek.class);
                i.putExtra("idpelanggan",holder.tvOpt.getTag().toString());
                i.putExtra("type",type);
                c.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,hutang,tvOpt;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            hutang = itemView.findViewById(R.id.tHutang);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            cv=itemView.findViewById(R.id.cv);

        }
    }
}