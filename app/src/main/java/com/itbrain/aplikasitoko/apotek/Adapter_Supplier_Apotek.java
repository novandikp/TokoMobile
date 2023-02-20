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


class Adapter_Supplier_Apotek extends RecyclerView.Adapter<Adapter_Supplier_Apotek.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public Adapter_Supplier_Apotek(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier_apotek, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.tvOpt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Intent intent = new Intent(c, Form_Ubah_Supplier_Apotek.class);
                                intent.putExtra("id",holder.tvOpt.getTag().toString());
                                c.startActivity(intent);
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
                                                String id = holder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tblsupplier WHERE idsupplier="+id ;
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
        TextView nama,alamat,notelp,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.nama);
            alamat = (TextView) itemView.findViewById(R.id.epelangggan);
            notelp=(TextView) itemView.findViewById(R.id.notelp);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt3);

        }
    }
}