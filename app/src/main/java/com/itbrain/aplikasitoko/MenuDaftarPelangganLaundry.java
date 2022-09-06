package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.JasaLaundry;
import com.itbrain.aplikasitoko.Model.PelangganLaundry;

import java.util.ArrayList;

public class MenuDaftarPelangganLaundry extends AppCompatActivity {
    ArrayList<PelangganLaundry> datapelanggan;
    RecyclerView DaftarPelanggan;
    PelangganLaundryAdapater adapter;
    DatabaseLaundry db;
//    implements PopupMenu.OnMenuItemClickListener



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_pelanggan_laundry);
        DaftarPelanggan = findViewById(R.id.DaftarPelanggan);
        datapelanggan = new ArrayList<>();
        db = new DatabaseLaundry(this);

        DaftarPelanggan.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PelangganLaundryAdapater(datapelanggan,this);
        DaftarPelanggan.setAdapter(adapter);
    }
    public void UbahPelanggan(View view) {
        Intent intent = new Intent(MenuDaftarPelangganLaundry.this, MenuPelangganLaundry.class);
        startActivity(intent);
    }

//    public void popMenu(View v){
//        datapelanggan.remove(recyclerView.getAdapter().toString());
//        adapter = new PelangganLaundryAdapater(datapelanggan,this);
//        PopupMenu popupMenu = new PopupMenu(this, v);
//        PopupMenu.OnMenuItemClickListener popMenu;
//        popupMenu.inflate(R.menu.option_item);
//        popupMenu.show();
//    }


//    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.listItem:
//                Toast.makeText(this, "Item 1 di Klik", Toast.LENGTH_SHORT).show();
//            case R.id.listItem2:
//                Toast.makeText(this, "Item 2 di Klik", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

//    public void dummy(){
//        datapelanggan.clear();
//        datapelanggan.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//        datapelanggan.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//        datapelanggan.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//    }

    public void getData(){
        Cursor cursor = db.sq("select * from tblpelanggan where idpelanggan != 0");
        if(cursor!=null){
            datapelanggan.clear();
            while(cursor.moveToNext()){
                datapelanggan.add(new PelangganLaundry(cursor.getInt(cursor.getColumnIndex("idpelanggan")),cursor.getString(cursor.getColumnIndex("pelanggan")),cursor.getString(cursor.getColumnIndex("alamat")),cursor.getString(cursor.getColumnIndex("notelp")),cursor.getDouble(cursor.getColumnIndex("hutang"))));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void Simpan(View view) {
        Intent intent = new Intent(MenuDaftarPelangganLaundry.this, MenuPelangganLaundry.class);
        startActivity(intent);
    }
}

class PelangganLaundryAdapater extends RecyclerView.Adapter<PelangganLaundryAdapater.ViewHolder>{

    ArrayList<PelangganLaundry>Pelanggan;
    Context context;

    public PelangganLaundryAdapater(ArrayList<PelangganLaundry> Pelanggan, Context context) {
        this.Pelanggan = Pelanggan;
        this.context = context;
//        Toast.makeText(context, ""+Pelanggan.size(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laundryitemdaftarpelanggan,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int Position) {
        final PelangganLaundry pelanggan = Pelanggan.get(Position);
        holder.namaPelanggan.setText(pelanggan.getPelanggan());
        holder.alamatPelanggan.setText(pelanggan.getAlamatpelanggan());
        holder.noTelpPelanggan.setText(pelanggan.getNotelppelanggan());
        holder.Hutang.setText(""+(Double)pelanggan.getHutang());
        holder.optMuncul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.inflate(R.menu.option_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.ubah:
//                                context.startActivity(new Intent(context, MenuPelangganLaundry.class).putExtra("idpelanggan",pelanggan.getIdpelanggan()));
//                                ((MenuDaftarPelangganLaundry)context).finish();
                                LaundryDatabase db = new LaundryDatabase(context);
                                Intent intent = new Intent(context,MenuPelangganLaundry.class);
                                intent.putExtra("idpelanggan",pelanggan.getIdpelanggan());
                                intent.putExtra("pelanggan",pelanggan.getPelanggan());
                                intent.putExtra("alamatpelanggan",pelanggan.getAlamatpelanggan());
                                intent.putExtra("notelppelanggan",pelanggan.getNotelppelanggan());
                                intent.putExtra("hutang",pelanggan.getHutang());
                                context.startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LaundryDatabase db = new LaundryDatabase(context);
                                        if (db.deletePelanggan(pelanggan.getIdpelanggan())){
                                            Pelanggan.remove(Position);
                                            notifyItemChanged(Position);
                                            Toast.makeText(context, "Delete Pelanggan"+ pelanggan.getPelanggan()+" berhasil", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+pelanggan.getPelanggan());
                                builder.setMessage("Anda yakin ingin menghapus "+pelanggan.getPelanggan()+" dari data Pelanggan");
                                  builder.show();
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
    public int getItemCount() { return Pelanggan.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaPelanggan,alamatPelanggan,noTelpPelanggan,Hutang;
        ImageView optMuncul;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPelanggan = itemView.findViewById(R.id.txtNamaPelanggan);
            alamatPelanggan = itemView.findViewById(R.id.txtAlamatPelanggan);
            noTelpPelanggan = itemView.findViewById(R.id.txtNomerPelanggan);
            Hutang = itemView.findViewById(R.id.txtHutang);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }
}