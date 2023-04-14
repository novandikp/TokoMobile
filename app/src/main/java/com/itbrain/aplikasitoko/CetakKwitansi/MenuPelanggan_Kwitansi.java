package com.itbrain.aplikasitoko.CetakKwitansi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class MenuPelanggan_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    RecyclerView listpelanggan;
    AdapterListPelanggan adapter;
    List<getterPelanggan> DaftarPelanggan;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupelanggan_kwitansi);

        ImageButton imageButton = findViewById(R.id.imageButton31);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);

        final EditText eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString() ;
                getPelanggan(a);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnTambah(View view) {
        Intent intent=new Intent(this, TambahPelanggan_Kwitansi.class);
        startActivity(intent);
    }

    public void getPelanggan(String keyword){
        DaftarPelanggan = new ArrayList<>();
        listpelanggan = (RecyclerView) findViewById(R.id.listPelanggan);
        listpelanggan.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listpelanggan.setLayoutManager(layoutManager);
        adapter = new AdapterListPelanggan(this,DaftarPelanggan);
        listpelanggan.setAdapter(adapter);
        String q;
        Cursor c ;
        c = db.sq("SELECT * FROM tblpelanggan ORDER BY pelanggan ASC");

        if(c.getCount() > 0){
            while(c.moveToNext()){
                String idpelanggan = c.getString(c.getColumnIndex("idpelanggan")) ;
                String pelanggan = c.getString(c.getColumnIndex("pelanggan")) ;
                arrayList.add(idpelanggan+"__"+pelanggan);
            }
        }
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%' AND idpelanggan>0 ORDER BY pelanggan";
        }
        Cursor cur=db.sq(q);
        while(cur.moveToNext()){
            DaftarPelanggan.add(new getterPelanggan(
                    cur.getInt(cur.getColumnIndex("idpelanggan")),
                    cur.getString(cur.getColumnIndex("pelanggan")),
                    cur.getString(cur.getColumnIndex("alamat")),
                    cur.getString(cur.getColumnIndex("notelp"))
            ));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan("");
    }
}

class AdapterListPelanggan extends RecyclerView.Adapter<AdapterListPelanggan.PelangganViewHolder>{
    private Context ctxAdapter;
    private List<getterPelanggan> data;

    public AdapterListPelanggan(Context ctx, List<getterPelanggan> viewData) {
        this.ctxAdapter = ctx;
        this.data = viewData;
    }

    @NonNull
    @Override
    public PelangganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.item_pelanggan_kwitansi,viewGroup,false);
        return new PelangganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PelangganViewHolder holder, final int i) {
        final getterPelanggan getter = data.get(i);
        holder.pelanggan.setText(getter.getPelanggan());
        holder.alamat.setText(getter.getAlamat());
        holder.telp.setText(getter.getTelp());
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                popupMenu.inflate(R.menu.option_item_kwitansi);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                Intent intent = new Intent(ctxAdapter, UpdatePelanggan_Kwitansi.class);
                                intent.putExtra("idpelanggan",getter.getIdPelanggan());
                                intent.putExtra("pelanggan",getter.getPelanggan());
                                intent.putExtra("alamat",getter.getAlamat());
                                intent.putExtra("notelp",getter.getTelp());
                                ctxAdapter.startActivity(intent);
                                break;

                            case R.id.menu_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseCetakKwitansi db = new DatabaseCetakKwitansi(ctxAdapter);
                                        if (db.deletePelanggan(getter.getIdPelanggan())){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctxAdapter, "Delete pelanggan "+getter.getPelanggan()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+getter.getPelanggan())
                                        .setMessage("Anda yakin ingin menghapus "+getter.getPelanggan()+" dari data pelanggan");
                                builder.show();
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

    class PelangganViewHolder extends RecyclerView.ViewHolder{
        TextView pelanggan,alamat,telp,opt;
        public PelangganViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan=(TextView)itemView.findViewById(R.id.tvNama);
            alamat=(TextView)itemView.findViewById(R.id.tvAlamat);
            telp=(TextView)itemView.findViewById(R.id.tvTelp);
            opt=(TextView)itemView.findViewById(R.id.tvOpt);
        }
    }
}

class getterPelanggan{
    private int idPelanggan;
    private String pelanggan;
    private String alamat;
    private String telp;

    public getterPelanggan(int idPelanggan, String Pelanggan, String alamat, String telp) {
        this.idPelanggan = idPelanggan;
        this.pelanggan = Pelanggan;
        this.alamat = alamat;
        this.telp = telp;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelp() {
        return telp;
    }
}