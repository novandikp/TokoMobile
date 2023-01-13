package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.util.ArrayList;

public
class AdapterLapBayar extends RecyclerView.Adapter<AdapterLapBayar.ViewHolder>{
    private ArrayList<String> data;
    Context c;
    View v ;

    public AdapterLapBayar(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText("Total :"+"Rp."+ ModulBengkel.removeE(row[3]));
        holder.tvOpt.setText("Bayar :"+"Rp."+ModulBengkel.removeE(row[4]));
        holder.print.setVisibility(View.VISIBLE);
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,MenuCetak.class);
                i.putExtra("idorder",holder.cv.getTag().toString());
                i.putExtra("type","laporan");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        ImageView print;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tgl);
            print=itemView.findViewById(R.id.print);

        }
    }
}
