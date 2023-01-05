package com.itbrain.aplikasitoko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class Siswa_Adapter extends RecyclerView.Adapter<Siswa_Adapter.ViewHolder> {

    private Context context;
    private List<Siswa> siswaList;

    public Siswa_Adapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coba_recycleview_, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        Siswa siswa = siswaList.get(i);
        holder.tvNama.setText(siswa.getNama());
        holder.tvAlamat.setText(siswa.getAlamat());
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, siswa.getNama() + " Alamat : " + siswa.getAlamat(), Toast.LENGTH_SHORT).show();
            }
        });

 */
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMenu);
                popupMenu.inflate(R.menu.menu_option);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.menu_edit:
                                Toast.makeText(context, "Simpan Data " + siswa.getNama(), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menu_Hapus:
                               siswaList.remove(i);
                               notifyDataSetChanged();
                                Toast.makeText(context, siswa.getNama() + " Sudah Di Hapus", Toast.LENGTH_SHORT).show();
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
        return siswaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvAlamat, btnMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }
}