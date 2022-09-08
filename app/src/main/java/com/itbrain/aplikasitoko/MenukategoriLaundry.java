package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.itbrain.aplikasitoko.Model.Kategori;

import java.util.ArrayList;

public class MenukategoriLaundry extends AppCompatActivity {
    ArrayList<Kategori> datakategori;
    RecyclerView recyclerView;
    KategoriLaundryAdapter adapter;
    DatabaseLaundry db;
//    implements PopupMenu.OnMenuItemClickListener



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kategori_laundry);
        recyclerView = findViewById(R.id.DaftarKategori);
        datakategori = new ArrayList<>();
        db = new DatabaseLaundry(this);
        adapter = new KategoriLaundryAdapter(datakategori,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

//    public void popMenu(View v){
//        datakategori.remove(recyclerView.getAdapter().toString());
//        adapter = new KategoriLaundryAdapter(datakategori,this);
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
//        datakategori.clear();
//        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
//        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
//        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
//    }


    @SuppressLint("Range")
    public void getData(){
        Cursor cursor = db.sq("select * from tblkategori where idkategori != 0");
        if(cursor!=null){
            datakategori.clear();
            while(cursor.moveToNext()){
                datakategori.add(new Kategori(cursor.getInt(cursor.getColumnIndex("idkategori")), cursor.getString(cursor.getColumnIndex("kategori"))));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void Simpan(View view) {
        Intent intent = new Intent(MenukategoriLaundry.this, MenuKategoriJasaLaundry.class);
        startActivity(intent);
    }
}

class KategoriLaundryAdapter extends RecyclerView.Adapter<KategoriLaundryAdapter.ViewHolder>{

    ArrayList<Kategori>kategoris;
    Context context;

    public KategoriLaundryAdapter(ArrayList<Kategori> kategoris, Context context) {
        this.kategoris = kategoris;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laundryitemdaftarkategori,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int Adapter) {
        final Kategori kategori = kategoris.get(Adapter);
        holder.edtKategori.setText(kategori.getKategori());
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
                                Intent intent = new Intent(context,MenuKategoriJasaLaundry.class);
                                intent.putExtra("idkategori",kategori.getIdkategori());
                                intent.putExtra("kategori",kategori.getKategori());
                               context.startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LaundryDatabase db = new LaundryDatabase(context);
                                        if (db.deleteKategori(kategori.getIdkategori())){
                                            kategoris.remove(Adapter);
                                            notifyItemChanged(Adapter);
                                            Toast.makeText(context, "Delete kategori "+ kategori.getKategori()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+kategori.getKategori());
                                builder.setMessage("Anda yakin ingin menghapus "+kategori.getKategori()+" dari data jasa");
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
    public int getItemCount() {
        return kategoris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView edtKategori;
        ImageView optMuncul;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edtKategori = itemView.findViewById(R.id.edtDaftarPegawai);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }

}