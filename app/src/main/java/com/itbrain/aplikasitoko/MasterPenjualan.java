package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Util.Penjualan;
import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.util.ArrayList;

public class MasterPenjualan extends RecyclerView.Adapter<MasterPenjualan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPenjualan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_penjualan_, viewGroup, false);
//        return new ViewHolder(view);
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi,tvHarga;
        private ImageView tvOpt;


        public ViewHolder(@NonNull View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
            tvOpt = (ImageView) view.findViewById(R.id.hapus);

            tvHarga=view.findViewById(R.id.tvHarga);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        if (row[4].equals("1")){
            viewHolder.deskripsi.setText("Teknisi :"+row[5]);
            viewHolder.tvHarga.setText(ModulBengkel.removeE(row[3]));
            viewHolder.tvOpt.setTag(row[0]);
        }else{
            viewHolder.deskripsi.setText("Jumlah : "+row[2]);
            double harga = ModulBengkel.strToDouble(row[3])*ModulBengkel.strToDouble(row[2]);
            viewHolder.tvHarga.setText(ModulBengkel.removeE(harga));
            viewHolder.tvOpt.setTag(row[0]);
        }
        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = viewHolder.tvOpt.getTag().toString();
                                Database_Bengkel_ db = new Database_Bengkel_(c);
                                String q = "DELETE FROM tbldetailorder WHERE iddetailorder="+id ;
                                db.exc(q);
                                ((Penjualan)c).getBarang("");

                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Hapus Data");
                alert.show();
            }
        });




    }
}