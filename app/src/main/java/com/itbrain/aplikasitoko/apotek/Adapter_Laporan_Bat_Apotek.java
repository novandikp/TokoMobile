package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;


class Adapter_Laporan_Bat_Apotek extends RecyclerView.Adapter<Adapter_Laporan_Bat_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Laporan_Bat_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expired, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.cv.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Tanggal expired : "+row[8]);
        String stokkecil = ModulApotek.doubleToStr(ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[7]));
        if (row[2].equals("0")) {
            holder.expired.setText("Stok kosong");
        }else{
            holder.expired.setText("Sisa Stok: "+ ModulApotek.unchangeComma(row[2]) +" "+row[6]+" / "+ ModulApotek.unchangeComma(stokkecil)+" "+row[5]);
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
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.cv.getTag().toString();
                ((Laporan_Obat_Expired_Apotek_)c).openDialog(id);
            }
        });

//

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt,expired;
        CardView cv ;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tfaktur);
            alamat = (TextView) itemView.findViewById(R.id.t1);
            notelp=(TextView) itemView.findViewById(R.id.t2);
            tvOpt=(TextView) itemView.findViewById(R.id.t3);
            expired=(TextView) itemView.findViewById(R.id.t5);
        }
    }

}
