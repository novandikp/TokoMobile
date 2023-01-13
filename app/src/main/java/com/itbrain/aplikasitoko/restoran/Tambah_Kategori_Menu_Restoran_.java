package com.itbrain.aplikasitoko.restoran;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.FPref;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Tambah_Kategori_Menu_Restoran_ extends AppCompatActivity {

    String type, mmKeyword="";
    View view;
    Database_Restoran db;
    FPref config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kategori_menu_restoran_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config= new FPref(getSharedPreferences("id", MODE_PRIVATE));
        type=getIntent().getStringExtra("type");
        String title="";

        view = this.findViewById(android.R.id.content);
        db = new Database_Restoran(this);
        getList();
        EditText eCari = (EditText)findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmKeyword=s.toString();
                getList();
            }
        });

    }

    public List<String> getKategori() {
        List<String> label = new ArrayList<String>();
        String q = Query.select("tblkategori");
        Cursor cursor = db.sq(q);
        if (cursor.getCount()>0) {
            label.add(ModulRestoran.getResString(this,R.string.semuakategori));
            while (cursor.moveToNext()) {
                String data =ModulRestoran.getString(cursor,"kategori");
                label.add(data);
            }
        }else {
            label.add(getResources().getString(R.string.kosong));
        }
        return label;
    }

    public List<String> getIdKategori() {
        List<String> label = new ArrayList<String>();
        String q = Query.select("tblkategori");
        Cursor cursor = db.sq(q);
        if (cursor.getCount()>0) {
            label.add("0");
            while (cursor.moveToNext()) {
                String data = ModulRestoran.getString(cursor, "idkategori");
                label.add(data);
            }
        } else {
            label.add("0");
        }
        return label;
    }

//    public String getIdKategoriAtPosition(int position) {
//        return getIdKategori().get(position);
//    }

    public void kembali(View view) {
        finish();
    }

    public void tambahdata(View view) {
        Intent intent = new Intent(Tambah_Kategori_Menu_Restoran_.this, Form_Tambah_Kategori_Menu_Restoran_.class);
        startActivity(intent);
    }
    public void getList() {
        getKategori(mmKeyword);
    }

    private void getKategori(String keyword) {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rcvKategori);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListKategori(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q="";
        if (keyword.isEmpty()){
            q=Query.selectwhere("tblkategori")+"idkategori>0";
        }else {
            q=Query.selectwhere("tblkategori")+Query.sLike("kategori",keyword)+" AND idkategori>0";
        }
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur=ModulRestoran.getString(c,"idkategori")+"__"+
                        ModulRestoran.getString(c,"kategori")+"__"+
                        "0";
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
}

class AdapterListKategori extends RecyclerView.Adapter<AdapterListKategori.KategoriViewHolder>{
    Context ctx;
    ArrayList<String> data;

    public AdapterListKategori(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_restoran,viewGroup,false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KategoriViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.kategori.setText(row[1]);
        if (row[2].equals("1")){
            holder.opt.setVisibility(View.GONE);
            holder.jumlahtransaksi.setVisibility(View.VISIBLE);
            Database_Restoran db=new Database_Restoran(ctx);
            Cursor c=db.sq("SELECT DISTINCT faktur, idkategori FROM qorder WHERE idkategori="+row[0]);
//            holder.jumlahtransaksi.setText(ModulRestoran.getResString(ctx,R.string.jumlahtransaksi)+" "+c.getCount());
        }
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(ctx,holder.opt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.menu_edit:
//                                DialogKategori builder=new DialogKategori(ctx,false,row[0]);
                                break;
                            case R.id.menu_Hapus:
                                AlertDialog.Builder builder1=new AlertDialog.Builder(ctx);
                                final Database_Restoran db=new Database_Restoran(ctx);
                                builder1.create();
//                                builder1.setTitle(ctx.getResources().getString(R.string.confirmdelete)+" "+row[1]+"?");
//                                builder1.setMessage(ctx.getResources().getString(R.string.alerthapus));
                                builder1.setTitle("Hapus Data");
                                builder1.setMessage("Apakah Anda Ingin Menghapus Data Ini?");
                                final String cek = Query.selectwhere("tblmakanan")+Query.sWhere("idkategori",row[0]);
                                final String q="DELETE FROM tblkategori WHERE idkategori="+row[0];
                               builder1.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       if (ModulRestoran.getCount(db.sq(cek))==0){
                                           db.exc(q);
                                           Toast.makeText(ctx, "Berhasil", Toast.LENGTH_SHORT).show();
                                           ((Tambah_Kategori_Menu_Restoran_)ctx).getList();
                                       }else {
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

    class KategoriViewHolder extends RecyclerView.ViewHolder{
        TextView kategori,opt,jumlahtransaksi;
        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            kategori=(TextView)itemView.findViewById(R.id.tvNamaKategori);
            opt=(TextView)itemView.findViewById(R.id.tvOpt);
            jumlahtransaksi=(TextView)itemView.findViewById(R.id.tvJumlahTransaksi);
        }
    }
}