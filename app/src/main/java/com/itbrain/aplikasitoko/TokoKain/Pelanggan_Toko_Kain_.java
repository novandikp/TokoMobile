package com.itbrain.aplikasitoko.TokoKain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Pelanggan_Toko_Kain_ extends AppCompatActivity {

    RecyclerView recyclerView;
    List<getterPelanggan> DaftarPelanggan;
    AdapterListPelanggan adapter;
    DatabaseTokoKain db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pelanggan_toko_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.Kembalii);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            db=new DatabaseTokoKain(this);

            final EditText edtCari = (EditText)findViewById(R.id.edtCari);
            edtCari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String keyword = edtCari.getText().toString();
                    getPelanggan(keyword);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getPelanggan(String keyword){
        DaftarPelanggan = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListPelanggan);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterListPelanggan(this,DaftarPelanggan,Boolean.TRUE);
        recyclerView.setAdapter(adapter);

        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE namapelanggan LIKE '%"+keyword+"%' AND idpelanggan>0 "+FQueryTokoKain.sOrderASC("namapelanggan");
        }

        Cursor c = db.sq(q);
        if (c.getCount()>0){
            while(c.moveToNext()){
                DaftarPelanggan.add(new getterPelanggan(
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("namapelanggan")),
                        c.getString(c.getColumnIndex("alamatpelanggan")),
                        c.getString(c.getColumnIndex("telppelanggan"))
                ));
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan("");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickTambah(View view) {
        Intent i = new Intent(this,Form_Tambah_Pelanggan_Toko_Kain_.class);
        startActivity(i);
    }
}
class AdapterListPelanggan extends RecyclerView.Adapter<AdapterListPelanggan.PelangganViewHolder>{
    private Context ctxAdapter;
    private List<getterPelanggan> data;
    private Boolean showopt;

    public AdapterListPelanggan (Context ctx,List<getterPelanggan> viewData, Boolean showopt){
        this.data = viewData;
        ctxAdapter=ctx;
        this.showopt=showopt;
    }

    @NonNull
    @Override
    public PelangganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view =inflater.inflate(R.layout.list_pelanggan_toko_kain,viewGroup,false);
        return new PelangganViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final PelangganViewHolder holder, final int i) {
        final getterPelanggan getter = data.get(i);

        holder.nama.setText(getter.getNamaPelanggan());
        holder.alamat.setText(getter.getAlamatPelanggan());
        holder.telp.setText(getter.getNotelpPelanggan());
        if (showopt){
            holder.opt.setVisibility(View.VISIBLE);
        }else {
            holder.opt.setVisibility(View.GONE);
        }
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                popupMenu.inflate(R.menu.menu_option_toko_kain);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.menu_update:
                                Intent intent = new Intent(ctxAdapter,Form_Tambah_Pelanggan_Toko_Kain_.class);
                                intent.putExtra("idpelanggan",getter.getIdPelanggan());
                                intent.putExtra("namapelanggan",getter.getNamaPelanggan());
                                intent.putExtra("alamatpelanggan",getter.getAlamatPelanggan());
                                intent.putExtra("telppelanggan",getter.getNotelpPelanggan());
                                ctxAdapter.startActivity(intent);
                                break;
                            case R.id.menu_hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseTokoKain db = new DatabaseTokoKain(ctxAdapter);
                                        if (db.deletePelanggan(getter.getIdPelanggan())){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctxAdapter, "Delete pelanggan "+getter.getNamaPelanggan()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+getter.getNamaPelanggan());
                                builder.setMessage("Anda yakin ingin menghapus "+getter.getNamaPelanggan()+" dari data pelanggan");
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
        private TextView nama,alamat,telp,opt;
        public PelangganViewHolder(@NonNull View itemView) {
            super(itemView);
            nama= (TextView)itemView.findViewById(R.id.tvNamaPelanggan);
            alamat= (TextView)itemView.findViewById(R.id.tvAlamatPelanggan);
            telp= (TextView)itemView.findViewById(R.id.tvTelpPelanggan);
            opt= (TextView)itemView.findViewById(R.id.tvOpt);
        }

    }
}
class getterPelanggan{
    private int idPelanggan;
    private String namaPelanggan,alamatPelanggan,notelpPelanggan;

    public getterPelanggan(int idPelanggan, String namaPelanggan, String alamatPelanggan, String notelpPelanggan) {
        this.idPelanggan = idPelanggan;
        this.namaPelanggan = namaPelanggan;
        this.alamatPelanggan = alamatPelanggan;
        this.notelpPelanggan = notelpPelanggan;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getAlamatPelanggan() {
        return alamatPelanggan;
    }

    public String getNotelpPelanggan() {
        return notelpPelanggan;
    }
}



