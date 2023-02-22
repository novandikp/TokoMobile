package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Master_Qart_Apotek extends RecyclerView.Adapter<Master_Qart_Apotek.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public Master_Qart_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart_apotek, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,jumlah,total,no,batch;
        private ImageView tvOpt;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tfaktur);
            jumlah = (TextView) view.findViewById(R.id.tJumlah);
            tvOpt = (ImageView) view.findViewById(R.id.tvOpt);
            batch = (TextView) view.findViewById(R.id.tBatch);

            total=view.findViewById(R.id.tTotal);
            no=view.findViewById(R.id.no);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");
        viewHolder.no.setText(ModulApotek.intToStr(i+1));
        viewHolder.barang.setText(row[1]);
        String jumlah= ModulApotek.unchangeComma(row[2])+" "+row[4]+" x Rp."+ ModulApotek.removeE(row[3]);
        double total = ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[3]);
        viewHolder.jumlah.setText(jumlah);
        viewHolder.batch.setText(row[9]);
        viewHolder.total.setText("Total : Rp."+ ModulApotek.removeE(total));
        viewHolder.tvOpt.setTag(row[0]);

        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin untuk menghapus item ini")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseApotek db = new DatabaseApotek(c);;
                                    String id = viewHolder.tvOpt.getTag().toString();
                                    String type=row[8];
                                    String q;
                                    if (type.equals("penjualan")){
                                        q= "DELETE FROM tbldetailjual WHERE iddetailjual="+id ;
                                    }else if (type.equals("pembelian")){
                                        q= "DELETE FROM tblbelidetail WHERE idbelidetail="+id ;
                                    }else{
                                        q="";
                                    }

                                    if (db.exc(q)){
                                        String jumlah=row[2];
                                        double konver = ModulApotek.strToDouble(jumlah)/ ModulApotek.strToDouble(row[5]);
                                        if (row[4].equals(row[6])){
                                            jumlah= ModulApotek.doubleToStr(konver);
                                        }

                                        if(type.equals("penjualan")){
                                            db.exc("UPDATE tblbarang SET stok=stok+"+jumlah+" WHERE idbarang='"+row[7]+"'");

                                        }else {
                                            db.exc("UPDATE tblbarang SET stok=stok-"+jumlah+" WHERE idbarang='"+row[7]+"'");

                                        }
                                    }
                                    if (c instanceof PemanggilMethod_apotek){
                                        ((PemanggilMethod_apotek)c).getdata("");
                                    }


                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Hapus item");
                alert.show();
            }
        });


    }
}
