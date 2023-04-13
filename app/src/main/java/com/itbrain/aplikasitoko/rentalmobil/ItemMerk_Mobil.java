package com.itbrain.aplikasitoko.rentalmobil;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ItemMerk_Mobil extends AppCompatActivity {

    DatabaseRentalMobil db;
    String type;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    ArrayList arraykat = new ArrayList();
    ModulRentalMobil config;
    public String cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemmerk_mobil);
        config = new ModulRentalMobil(getSharedPreferences("config", MODE_PRIVATE));
        db = new DatabaseRentalMobil(this);

        {
            getMerk("");
            setSpinner();


        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();
                {
                    getMerk(a);
                }
            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSpinner() {
        arraykat.add("Semua");
        arrayid.add("0");
        Spinner sp = findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraykat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        Cursor c = db.sq(ModulRentalMobil.select("tblmerk")) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arraykat.add(ModulRentalMobil.getString(c,"merk"));
                arrayid.add(ModulRentalMobil.getString(c,"idmerk"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMerk("");
    }


    public void getMerk(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterMerkMobil(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulRentalMobil.selectwhere("tblmerk")+ModulRentalMobil.sLike("merk",cari) + " ORDER BY merk ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulRentalMobil.getString(c,"idmerk")+"__"+ModulRentalMobil.getString(c,"merk");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void tambah(View view){
        Intent intent = new Intent(this, MobilMenuMerk.class);
        intent.putExtra("type","tambah");
        startActivity(intent);
    }
}

    class AdapterMerkMobil extends RecyclerView.Adapter<AdapterMerkMobil.ViewHolder> {
        private ArrayList<String> data;
        Context c;


        View v;

        public AdapterMerkMobil(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_masterdua_mobil, parent, false);


            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
            String[] row = data.get(i).split("__");
            holder.tvOpt.setTag(row[0]);
            holder.nama.setText(row[1]);


            holder.tvOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(c, holder.tvOpt);
                    popupMenu.inflate(R.menu.menu_option_mobil);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    Intent intent = new Intent(c, MenuEditMerkMobil.class);
                                    intent.putExtra("id", holder.tvOpt.getTag().toString());
                                    c.startActivity(intent);
                                    break;
                                case R.id.menu_Hapus:
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                    AlertDialog alert;
                                    alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                            .setCancelable(false)
                                            .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    DatabaseRentalMobil db = new DatabaseRentalMobil(c);
                                                    ;
                                                    String id = holder.tvOpt.getTag().toString();

                                                    String q = "DELETE FROM tblmerk WHERE idmerk=" + id;
                                                    if (db.exc(q)) {
                                                        ((ItemMerk_Mobil) c).getMerk("");
                                                    } else {
                                                        ModulRentalMobil.showToast(c, c.getString(R.string.gagal_hapus));
                                                    }

//

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

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nama, tvOpt;

            public ViewHolder(View itemView) {
                super(itemView);
                nama = itemView.findViewById(R.id.t1);

                tvOpt = itemView.findViewById(R.id.tvOpt);
            }
        }
    }








