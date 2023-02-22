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

class Adapter_Laporan_Obat extends RecyclerView.Adapter<Adapter_Laporan_Obat.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Obat(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_lapbar, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");

        if (row[0].equals("barang")){
            holder.nama.setText(row[1]);
            String stokkecil = ModulApotek.doubleToStr(ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[7]));
            if (row[2].equals("0")) {
                holder.alamat.setText("Stok kosong");
            }else{
                holder.alamat.setText("Sisa Stok: "+ ModulApotek.unchangeComma(row[2]) +" "+row[6]+" / "+ ModulApotek.unchangeComma(stokkecil)+" "+row[5]);
            }

            if(row[3].equals("belum")){
                holder.notelp.setText("Harga belum tersedia");
                holder.tvOpt.setText("");
            }else{
                String hargasatu = "Rp."+ ModulApotek.removeE(row[3])+"/"+row[6];
                String hargadua = "Rp."+ ModulApotek.removeE(row[4])+"/"+row[5];
                holder.notelp.setText(hargasatu);
                holder.tvOpt.setText(hargadua);
            }
        }else if(row[0].equals("laba")){
            double laba = ModulApotek.strToDouble(row[3])* ModulApotek.strToDouble(row[4]);
            holder.nama.setText(row[1]);
            holder.alamat.setText(row[2]);
            holder.notelp.setText(ModulApotek.sederhana(laba));
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

            nama= (TextView) itemView.findViewById(R.id.etfaktur);
            alamat = (TextView) itemView.findViewById(R.id.t10);
            notelp=(TextView) itemView.findViewById(R.id.t20);
            tvOpt=(TextView) itemView.findViewById(R.id.t30);

        }
    }
}


