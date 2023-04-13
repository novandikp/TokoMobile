package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.PemanggilMethod;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Kategori_Toko_Sepatu extends AppCompatActivity implements PemanggilMethod {
    ModulTokoSepatu config,temp;
    Context a;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kategori_sepatu);
        recyclerView = findViewById(R.id.recKategori);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);


        getKategori("");

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
                arrayList.clear();
                getKategori(a);

            }
        });
    }

    @Override
    protected void onResume() {
        getKategori("");
        super.onResume();
    }



    public void getKategori(String cari){
        arrayList.clear();
        recyclerView = findViewById(R.id.recKategori);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterKategori(this,arrayList) ;
        recyclerView.setAdapter(adapter);

        Cursor c ;
        if(TextUtils.isEmpty(cari)){
            c = db.sq(ModulTokoSepatu.select("tblkategori")+ModulTokoSepatu.sOrderASC("kategori"));
        } else {
            c = db.sq(ModulTokoSepatu.selectwhere("tblkategori") + ModulTokoSepatu.sLike("kategori",cari)+ModulTokoSepatu.sOrderASC("kategori")) ;
        }

        while(c.moveToNext()){
            String kategori = ModulTokoSepatu.getString(c,"kategori") ;
            String idkategori = ModulTokoSepatu.getString(c,"idkategori") ;
            arrayList.add(idkategori+"__"+kategori);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void getBarang(String cari) {

    }

    @Override
    public void getPelanggan(String cari) {

    }

    @Override
    public void getUkuran() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void tambahKategori(View view) {

        Intent i = new Intent(Menu_Kategori_Toko_Sepatu.this,Menu_Tambah_kategori_Toko_Sepatu.class);
        startActivity(i);

    }
}

class AdapterKategori extends RecyclerView.Adapter<AdapterKategori.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;


    public AdapterKategori(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_kategori_sepatu, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView kategori, tvOpt;

        public ViewHolder(View view) {
            super(view);

            kategori = (TextView) view.findViewById(R.id.tvKat);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");
        viewHolder.tvOpt.setTag(row[0]);
        viewHolder.kategori.setText(row[1]);
        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c, viewHolder.tvOpt);
                popupMenu.inflate(R.menu.menu_option_sepatu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_update:
                                Intent i = new Intent(c, Menu_Ubah_Kategori_Sepatu.class);
                                i.putExtra("id", viewHolder.tvOpt.getTag().toString());
                                c.startActivity(i);
                                break;
                            case R.id.menu_delete:


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);

                                                String id = viewHolder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tblkategori WHERE idkategori=" + id;
                                                if (db.exc(q)) {
                                                    if (c instanceof PemanggilMethod) {
                                                        ((PemanggilMethod) c).getKategori("");
                                                    }
                                                } else {
                                                    Toast.makeText(c, "Data masih dipakai", Toast.LENGTH_SHORT).show();
                                                }

//                                                Intent intent = new Intent(c, MenuKategori.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                c.startActivity(intent);


                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                alert = alertDialog.create();

                                alert.setTitle("Hapus Data");
                                alert.show();

//
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


//        viewHolder.ubah.setTag(row[0]);
//        viewHolder.hapus.setTag(row[0]);
    }
}