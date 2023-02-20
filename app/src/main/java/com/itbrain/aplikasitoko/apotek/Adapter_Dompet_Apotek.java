package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Adapter_Dompet_Apotek extends RecyclerView.Adapter<Adapter_Dompet_Apotek.ViewHolder> {
    ArrayList<String> data;
    Context c;

    public Adapter_Dompet_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi_apotek, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String[] row = data.get(position).split("__");
        holder.hapus.setTag(row[0]);
        holder.no.setText("No Transaksi : " + row[0]);
        holder.tgl.setText(row[1]);
        holder.faktur.setText(row[2]);
        holder.ket.setText(row[3]);

        final DatabaseApotek db = new DatabaseApotek(c);
        Cursor b = db.sq(ModulApotek.select("tbltransaksi"));
        b.moveToLast();
        String idlast = ModulApotek.getString(b, "idtransaksi");
        if (row[0].equals(idlast)) {
            holder.hapus.setVisibility(View.VISIBLE);
        }
        if (row[4].equals("0")) {
            holder.uang.setText("Keluar : " + ModulApotek.removeE(row[5]));
        } else {
            holder.uang.setText("Masuk : " + ModulApotek.removeE(row[4]));
        }
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = holder.hapus.getTag().toString();


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.exc("DELETE FROM tbltransaksi where idtransaksi=" + id);
                                ((Laporan_Keuangan_Apotek_) c).getDompet("");
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert = alertDialog.create();

                alert.setTitle("Hapus Data");
                alert.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView no, tgl, faktur, ket, uang;
        ImageView hapus;

        public ViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.no);
            tgl = itemView.findViewById(R.id.tgl);
            faktur = itemView.findViewById(R.id.faktur);
            ket = itemView.findViewById(R.id.ket);
            uang = itemView.findViewById(R.id.uang);
            hapus = itemView.findViewById(R.id.imageView2);


        }
    }
}