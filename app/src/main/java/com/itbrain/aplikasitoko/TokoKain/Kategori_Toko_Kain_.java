package com.itbrain.aplikasitoko.TokoKain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Kategori_Toko_Kain_ extends AppCompatActivity {

    RecyclerView recyclerView;
    List<getterKategori> DaftarKategori;
    AdapterListKategori adapter;
    DatabaseTokoKain db;
    View v ;
    ArrayList arrayList = new ArrayList() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_toko_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.kembali3);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {

            db = new DatabaseTokoKain(this);
            v = this.findViewById(android.R.id.content);

            final EditText edtCari = (EditText)findViewById(R.id.etPencarian);
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
                    getKategori(keyword);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getKategori("");
    }

    public void getKategori(String keyword){
        DaftarKategori = new ArrayList<>();
        recyclerView = findViewById(R.id.rcvList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterListKategori(this,DaftarKategori);
        recyclerView.setAdapter(adapter);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblkategori WHERE NOT (kategori = '---Pilih Kategori---')";
        }else {
            q="SELECT * FROM tblkategori WHERE idkategori>0 AND kategori LIKE '%"+keyword+"%' ORDER BY kategori ";
        }
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            while (c.moveToNext()){
                DaftarKategori.add(new getterKategori(
                        c.getInt(c.getColumnIndex("idkategori")),
                        c.getString(c.getColumnIndex("kategori"))
                ));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void Tambahh(View view) {
        Intent i = new Intent(this, Form_Tambah_Kategori_Toko_Kain.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
class AdapterListKategori extends RecyclerView.Adapter<AdapterListKategori.KategoriViewHolder>{
    private Context ctxAdapter;
    private List<getterKategori> data;

    public AdapterListKategori(Context ctx, List<getterKategori> viewData) {
        this.ctxAdapter = ctx;
        this.data = viewData;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.list_kategori_toko_kain,viewGroup,false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KategoriViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        final getterKategori getter = data.get(i);
        holder.kategori.setText(getter.getNamaKategori());
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                popupMenu.inflate(R.menu.menu_option_toko_kain);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                Intent intent = new Intent(ctxAdapter, edit_kategori_kain.class);
                                intent.putExtra("idkategori",getter.getIdKategori());
                                intent.putExtra("kategori",getter.getNamaKategori());
                                ctxAdapter.startActivity(intent);
                                break;
                            case R.id.menu_hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseTokoKain db = new DatabaseTokoKain(ctxAdapter);
                                        if (db.deleteKategori(getter.getIdKategori())){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctxAdapter, "Delete kategori "+getter.getNamaKategori()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+getter.getNamaKategori())
                                        .setMessage("Anda yakin ingin menghapus "+getter.getNamaKategori()+" dari data kategori");
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

    class KategoriViewHolder extends RecyclerView.ViewHolder{
        TextView kategori,opt;
        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            kategori=itemView.findViewById(R.id.tvNamaKategori);
            opt=itemView.findViewById(R.id.tvOpt);
        }
    }
}
class getterKategori{
    private int idKategori;
    private String namaKategori;

    public getterKategori(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

}