package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Adapter_Laporan_Dua_Apotek extends RecyclerView.Adapter<Adapter_Laporan_Dua_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Dua_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_lapdua, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.tgl.setText(row[2]);
        holder.alamat.setText(row[3]);
        holder.notelp.setText(row[4]);
        double total= ModulApotek.strToDouble(row[5])* ModulApotek.strToDouble(row[7]);
        holder.jum.setText(ModulApotek.unchangeComma(row[5])+" "+row[6]+" x "+ ModulApotek.removeE(row[7])+" = "+ ModulApotek.removeE(total));
        holder.print.setTag(row[0]);
        holder.batch.setText("batch number : "+row[8]);
        if (row[0].equals("PEMBELIAN")){
            holder.print.setVisibility(View.GONE);

        }else{
            holder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String id = holder.print.getTag().toString();
                        Intent in = new Intent(c,MenuCetakapotek2.class);
                        in.putExtra("idjual",id);
                         in.putExtra("type","laporan");
                         c.startActivity(in);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tgl,jum,batch;
        ImageView print;
        public ViewHolder(View itemView) {
            super(itemView);
            tgl=itemView.findViewById(R.id.tanggalan);
            jum = itemView.findViewById(R.id.jum);
            nama= (TextView) itemView.findViewById(R.id.fakturnya);
            alamat = (TextView) itemView.findViewById(R.id.t100);
            notelp=(TextView) itemView.findViewById(R.id.t200);
            batch=(TextView) itemView.findViewById(R.id.batch);
            print=itemView.findViewById(R.id.print);

        }
    }
}


