package com.itbrain.aplikasitoko.Salon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Menu_Jasa_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listjasa;
    AdapterListJasa adapter;
    List<getterJasa> DaftarJasa;
    ArrayList arrayList = new ArrayList() ;
    DatabaseSalon db;
    View v;
    String deviceid;
    SharedPreferences getPrefs ;
    ConfigSalon config;

    static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jasa_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahJasaSalon);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Jasa_Salon_.this, Form_Tambah_Jasa_Salon_.class);
                startActivity(intent);
            }
        });
        appbar = (Toolbar) findViewById(R.id.toolbar68);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DatabaseSalon(this);
        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config",this.MODE_PRIVATE));

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //deviceid = FunctionSalon.getDecrypt(getPrefs.getString("deviceid","")) ;

        final EditText eCari = (EditText) findViewById(R.id.editTextTextPersonName70) ;
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
                getJasa(a);
            }
        });
    }

    private boolean limit(String item) {
        int batas = FunctionSalon.strToInt(config.getCustom(item, "1"));
        if (batas>5){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    public void getJasa(String keyword){
        DaftarJasa = new ArrayList<>();
        listjasa = (RecyclerView) findViewById(R.id.listjasa);
        listjasa.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listjasa.setLayoutManager(layoutManager);
        adapter = new AdapterListJasa(this, DaftarJasa);
        listjasa.setAdapter(adapter);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tbljasa";
        } else {
            q = "SELECT * FROM tbljasa WHERE jasa LIKE '%"+keyword+"%' ORDER BY jasa";
        }

        Cursor cur = db.sq(q);
        if(FunctionSalon.getCount(cur)>0){
            while (cur.moveToNext()) {
                DaftarJasa.add(new getterJasa(
                        cur.getInt(cur.getColumnIndex("idjasa")),
                        cur.getString(cur.getColumnIndex("jasa")),
                        cur.getInt(cur.getColumnIndex("harga"))
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambah(View view){
        if (status){
            Intent intent = new Intent(this, Form_Tambah_Jasa_Salon_.class);
            startActivity(intent);
        } else {
            if (limit("jasa")){
                Intent intent = new Intent(this, Form_Tambah_Jasa_Salon_.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Anda sudah melebihi batas trial, harap beli yang full version", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Aplikasi_Salon_Menu_Utillitas.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJasa("");
    }

    private class AdapterListJasa extends RecyclerView.Adapter<AdapterListJasa.JasaViewHolder>{
        private Context ctxAdapter;
        private List<getterJasa> data;

        public AdapterListJasa(Context ctx, List<getterJasa> viewData){
            this.ctxAdapter = ctx;
            this.data = viewData;
        }

        @NonNull
        @Override
        public JasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_jasa_salon,viewGroup,false);
            return new JasaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final JasaViewHolder viewHolder, final int i) {
            final getterJasa getter = data.get(i);

            viewHolder.jasa.setText("Jasa : "+getter.getJasa());
            viewHolder.harga.setText("Harga : Rp. "+FunctionSalon.removeE(getter.getHarga()));
            viewHolder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ctxAdapter,viewHolder.opt);
                    popupMenu.inflate(R.menu.menu_option_salon);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_edit:
                                    Intent intent = new Intent(ctxAdapter, Form_Tambah_Jasa_Salon_.class);
                                    intent.putExtra("idjasa", getter.getIdjasa());
                                    intent.putExtra("jasa", getter.getJasa());
                                    intent.putExtra("harga", String.valueOf(getter.getHarga()));
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_Hapus:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                    builder.setMessage("Anda yakin ingin menghapus jasa ini?");
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseSalon db = new DatabaseSalon(ctxAdapter);
                                            if (db.deleteJasa(getter.getIdjasa())){
                                                data.remove(i);
                                                notifyDataSetChanged();
                                                Toast.makeText(ctxAdapter, "Delete Jasa Berhasil", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ctxAdapter, "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
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

        class JasaViewHolder extends RecyclerView.ViewHolder{
            TextView jasa, harga, opt;
            public JasaViewHolder(@NonNull View itemView) {
                super(itemView);
                jasa = (TextView)itemView.findViewById(R.id.tvNamaJasa);
                harga = (TextView)itemView.findViewById(R.id.tvHargaJasa);
                opt = (TextView)itemView.findViewById(R.id.tvOpt);
            }
        }
    }

    static class getterJasa{
        private int idjasa;
        private String jasa;
        private int harga;

        public getterJasa(int idjasa, String jasa, int harga){
            this.idjasa = idjasa;
            this.jasa = jasa;
            this.harga = harga;
        }

        public int getIdjasa(){
            return idjasa;
        }

        public String getJasa(){
            return jasa;
        }

        public int getHarga(){
            return harga;
        }
    }
}