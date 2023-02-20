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

class Adapter_Laporan_Satu_Apotek extends RecyclerView.Adapter<Adapter_Laporan_Satu_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Satu_Apotek(Context a, ArrayList<String> kota) {
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
        if (row[0].equals("barang")){
            holder.nama.setText(row[1]);
            if (row[2].equals("0")) {
                holder.alamat.setText("Stok kosong");
            }else{
                holder.alamat.setText("Sisa Stok: "+ row[2]+" "+row[5]);
            }

            if(row[3].equals("belum")){
                holder.notelp.setText("Harga belum tersedia");
            }else{
                String hargasatu = "Rp."+ ModulApotek.removeE(row[3])+"/"+row[5];
                String hargadua = "Rp."+ ModulApotek.removeE(row[4])+"/"+row[6];
                holder.notelp.setText(hargasatu+"\n\n"+hargadua);
            }
        }else if(row[0].equals("laba")){
            double laba = ModulApotek.strToDouble(row[3])* ModulApotek.strToDouble(row[4]);
            holder.nama.setText(row[1]);
            holder.alamat.setText(row[2]);
            holder.notelp.setText("Rp."+ ModulApotek.sederhana(laba));
        } else{
            holder.nama.setText(row[1]);
            holder.alamat.setText(row[2]);
            holder.notelp.setText(row[3]);
        }



//

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


