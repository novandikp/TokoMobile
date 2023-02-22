package com.itbrain.aplikasitoko.restoran;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class AdapterListKategori extends RecyclerView.Adapter<AdapterListKategori.KategoriViewHolder> {
    Context ctx;
    ArrayList<String> data;

    public AdapterListKategori(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_restoran, viewGroup, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KategoriViewHolder holder, int i) {
        final String[] row = data.get(i).split("__");
        holder.kategori.setText(row[1]);
        if (row[2].equals("1")) {
            holder.opt.setVisibility(View.GONE);
            holder.jumlahtransaksi.setVisibility(View.VISIBLE);
            Database_Restoran db = new Database_Restoran(ctx);
            Cursor c = db.sq("SELECT DISTINCT faktur, idkategori FROM qorder WHERE idkategori=" + row[0]);
//            holder.jumlahtransaksi.setText(ModulRestoran.getResString(ctx,R.string.jumlahtransaksi)+" "+c.getCount());
        }
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(ctx, holder.opt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_update:
//                                DialogKategori builder=new DialogKategori(ctx,false,row[0]);
                                break;
                            case R.id.menu_hapus:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                                final Database_Restoran db = new Database_Restoran(ctx);
                                builder1.create();
//                                builder1.setTitle(ctx.getResources().getString(R.string.confirmdelete)+" "+row[1]+"?");
//                                builder1.setMessage(ctx.getResources().getString(R.string.alerthapus));
                                builder1.setTitle("Hapus Data");
                                builder1.setMessage("Apakah Anda Ingin Menghapus Data Ini?");
                                final String cek = Query.selectwhere("tblmakanan") + Query.sWhere("idkategori", row[0]);
                                final String q = "DELETE FROM tblkategori WHERE idkategori=" + row[0];
                                builder1.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (ModulRestoran.getCount(db.sq(cek)) == 0) {
                                            db.exc(q);
                                            Toast.makeText(ctx, "Berhasil", Toast.LENGTH_SHORT).show();
                                            ((Tambah_Kategori_Menu_Restoran_) ctx).getList();
                                        } else {
                                            Toast.makeText(ctx, ctx.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder1.setNegativeButton(ctx.getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder1.show();
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

    class KategoriViewHolder extends RecyclerView.ViewHolder {
        TextView kategori, opt, jumlahtransaksi;

        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            kategori = (TextView) itemView.findViewById(R.id.tvNamaKategori);
            opt = (TextView) itemView.findViewById(R.id.tvOpt);
            jumlahtransaksi = (TextView) itemView.findViewById(R.id.tvJumlahTransaksi);
        }
    }
}
