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

public class MenuDaftarJasaLaundry extends AppCompatActivity {
    ArrayList<JasaLaundry> datajasa;
    RecyclerView DaftarJasa;
    JasaLaundryAdapater adapter;
    DatabaseLaundry db;
//    implements PopupMenu.OnMenuItemClickListener



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_jasa_laundry);
        DaftarJasa = findViewById(R.id.DaftarJasa);
        datajasa = new ArrayList<>();
        db = new DatabaseLaundry(this);

        DaftarJasa.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JasaLaundryAdapater(datajasa,this);
        DaftarJasa.setAdapter(adapter);
    }
    public void UbahJasa(View view) {
        Intent intent = new Intent(MenuDaftarJasaLaundry.this, MenuUbahJasaLaundry.class);
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
        Cursor cursor = db.sq("select * from tbljasa where jasa,biaya,satuan != 0");
        if(cursor!=null){
            datajasa.clear();
            while(cursor.moveToNext()){
                datajasa.add(new JasaLaundry(cursor.getInt(cursor.getColumnIndex("idjasa")), cursor.getString(cursor.getColumnIndex("jasa")), cursor.getDouble(cursor.getColumnIndex("biaya"))) {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                    }

                    @Override
                    public int getItemCount() {
                        return 0;
                    }
                });
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void Simpan(View view) {
        Intent intent = new Intent(MenuDaftarJasaLaundry.this, MenuPelangganLaundry.class);
        startActivity(intent);
    }
}

class JasaLaundryAdapater extends RecyclerView.Adapter<JasaLaundryAdapater.ViewHolder>{

    ArrayList<JasaLaundry>Jasa;
    Context context;

    public JasaLaundryAdapater(ArrayList<JasaLaundry> Jasa, Context context) {
        this.Jasa = Jasa;
        this.context = context;
//        Toast.makeText(context, ""+Pelanggan.size(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laundryitemdaftarjasa,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int Position) {
        final JasaLaundry jasa = Jasa.get(Position);
        holder.jasa.setText(jasa.getJasa());
        holder.biaya.setText((int) jasa.getBiaya());
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
                                intent.putExtra("idjasa",jasa.getIdjasa());
                                intent.putExtra("jasa",jasa.getJasa());
                                intent.putExtra("biaya",jasa.getBiaya());
                                context.startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LaundryDatabase db = new LaundryDatabase(context);
                                        if (db.deleteJasa(jasa.getIdjasa())){
                                            Jasa.remove(Position);
                                            notifyItemChanged(Position);
                                            Toast.makeText(context, "Delete Pelanggan"+ jasa.getJasa()+" berhasil", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+jasa.getJasa());
                                builder.setMessage("Anda yakin ingin menghapus "+jasa.getJasa()+" dari data Pelanggan");
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
    public int getItemCount() { return Jasa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jasa,biaya;
        ImageView optMuncul;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jasa = itemView.findViewById(R.id.txtjasa);
            biaya = itemView.findViewById(R.id.txtbiaya);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }
}