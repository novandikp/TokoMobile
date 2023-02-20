package com.itbrain.aplikasitoko.TokoKain;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;


public class TambahKain_Toko_Kain extends AppCompatActivity {
    Spinner spinnerKategori;
    RecyclerView recyclerView;
    List<getterKain> DaftarKain;
    AdapterListKain adapter;
    DatabaseTokoKain db;
    String kat;

    Boolean inAppStatus;
    Cursor countMaster, countTransaksi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseTokoKain(this);

        spinnerKategori = (Spinner) findViewById(R.id.spKategoriKain);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kat = db.getIdKategori().get(position);
                getKain("", kat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getKategoriData();
        final EditText edtCari = (EditText) findViewById(R.id.etCari);
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
                getKain(keyword, kat);
            }
        });
    }

    public void getKain(String keyword, String kategori) {
        DaftarKain = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rcvListKain);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterListKain(this, DaftarKain, Boolean.TRUE);
        recyclerView.setAdapter(adapter);
        String q;

        if (TextUtils.isEmpty(keyword)) {
            if (kategori.equals("0")) {
                q = "SELECT * FROM qkain";
            } else {
                q = "SELECT * FROM qkain WHERE idkategori=" + kategori;
            }
        } else {
            if (kategori.equals("0")) {
                q = "SELECT * FROM qkain WHERE kain LIKE '%" + keyword + "%'";
            } else {
                q = "SELECT * FROM qkain WHERE kain LIKE '%" + keyword + "%' AND idkategori=" + kategori;
            }
        }
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            while(c.moveToNext()){
                DaftarKain.add(new getterKain(
                        c.getInt(c.getColumnIndex("idkain")),
                        c.getInt(c.getColumnIndex("idkategori")),
                        c.getString(c.getColumnIndex("kategori")),
                        c.getString(c.getColumnIndex("kain")),
                        c.getString(c.getColumnIndex("biaya"))
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void getKategoriData(){
        DatabaseTokoKain db = new DatabaseTokoKain(this);
        List<String> labels = db.getKategori();

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getKain("","0");
        spinnerKategori.setSelection(0);
        countMaster=db.sq("SELECT * FROM tblkain");
        countTransaksi=db.sq("SELECT * FROM tblorder WHERE total>0");

//        if (countMaster.getCount()<6&&countTransaksi.getCount()<6){
//            inAppStatus=true;
//        }else {
//            inAppStatus=false;
//        }
    }

    public void tambahData(View view) {
//        if (inAppStatus){
        Intent i = new Intent(this, FormTambah_Toko_Kain.class);
        startActivity(i);
//        }else {
//            bp.purchase(this,"versipro");
//        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
class AdapterListKain extends RecyclerView.Adapter<AdapterListKain.KainViewHolder> {
    Context ctxAdapter;
    List<getterKain> data;
    Boolean showopt;

    public AdapterListKain(Context ctx, List<getterKain> viewData, Boolean showopt) {
        this.ctxAdapter = ctx;
        this.data = viewData;
        this.showopt = showopt;
    }

    @NonNull
    @Override
    public KainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.list_kain_toko_kain, viewGroup, false);
        return new KainViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final KainViewHolder holder, final int i) {
        final getterKain getter = data.get(i);
        holder.nama.setText(getter.getKain());
        holder.biaya.setText("Rp."+KumFunTokoKain.removeE(getter.getBiaya()));
        holder.kategori.setText(getter.getKategori());
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
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                Intent intent = new Intent(ctxAdapter, FormTambah_Toko_Kain.class);
                                intent.putExtra("idkain",getter.getIdKain());
                                intent.putExtra("idkategori",getter.getIdKategori());
                                intent.putExtra("kain",getter.getKain());
                                intent.putExtra("biaya",KumFunTokoKain.removeE(getter.getBiaya()));
                                intent.putExtra("kategori",getter.getKategori());
                                ctxAdapter.startActivity(intent);
                                break;
                            case R.id.menu_hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseTokoKain db = new DatabaseTokoKain(ctxAdapter);
                                        if (db.deleteKain(getter.getIdKain())){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctxAdapter, "Delete kain "+getter.getKain()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+getter.getKain());
                                builder.setMessage("Anda yakin ingin menghapus "+getter.getKain()+" dari data kain");
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
        return data.size();
    }

    class KainViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,kategori,opt;
        public KainViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaKain);
            biaya=(TextView)itemView.findViewById(R.id.tvBiayaKain);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriKain);
            opt=(TextView)itemView.findViewById(R.id.tvOpt);
        }
    }
}
class getterKain{
    private int idKain,idKategori;
    private String kain,kategori,biaya;

    public getterKain(int idKain, int idKategori, String kategori, String kain, String biaya) {
        this.idKain = idKain;
        this.idKategori = idKategori;
        this.kategori = kategori;
        this.kain = kain;
        this.biaya = biaya;

    }

    public int getIdKain() {
        return idKain;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public String getBiaya() {
        return biaya;
    }

    public String getKain() {
        return kain;
    }

    public String getKategori() {
        return kategori;
    }
}


