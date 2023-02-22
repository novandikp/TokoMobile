package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

class Adapter_Obat_Apotek extends RecyclerView.Adapter<Adapter_Obat_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Obat_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_apotek_obat,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");
        viewHolder.tvOpt.setTag(row[0]);
        viewHolder.barang.setText(row[1]);
        String stokkecil = ModulApotek.doubleToStr(ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[7]));
        if (row[2].equals("0")){
            viewHolder.stok.setText("Stok kosong");
        }else{
            viewHolder.stok.setText("Sisa stok: "+ ModulApotek.unchangeComma(row[2])+" "+row[4]+"/"+ ModulApotek.unchangeComma(stokkecil)+" "+row[6]);

        }

        if (row[3].equals("Belum tersedia")){
            viewHolder.harga.setText("Harga Belum Tersedia");
            viewHolder.hargadua.setText("");
        }else{
            viewHolder.harga.setText("Harga Jual Pertama: Rp."+row[3]+"/"+row[4]);
            viewHolder.hargadua.setText("Harga Jual Kedua: Rp."+row[5]+"/"+row[6]);
        }
        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,viewHolder.tvOpt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                String id = viewHolder.tvOpt.getTag().toString();
                                Intent i=new Intent(c,Form_Ubah_Obat_Apotek_.class);
                                i.putExtra("id",id);
                                c.startActivity(i);
                                break;
                            case  R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseApotek db = new DatabaseApotek(c);;
                                                String id = viewHolder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tblbarang WHERE idbarang='"+id+"'" ;
                                                if(db.exc(q)){

                                                }else{
                                                    Toast.makeText(c, "Data gagal dihapus karena sedang dipakai", Toast.LENGTH_SHORT).show();
                                                }

//
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

                                alert.setTitle("Hapus Data");
                                alert.show();


                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barang,stok,harga,hargadua, tvOpt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            barang=itemView.findViewById(R.id.tfaktur);
            stok=itemView.findViewById(R.id.t1);
            harga=itemView.findViewById(R.id.t2);
            hargadua=itemView.findViewById(R.id.t3);
            tvOpt=itemView.findViewById(R.id.tvOpt);
        }
    }
}