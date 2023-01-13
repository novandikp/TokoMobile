package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.util.ArrayList;


public class MenuLaporanSatu extends AppCompatActivity {

    ArrayList arrayList = new ArrayList();
    ArrayList arrayStat = new ArrayList();
    Database_Bengkel_ db;
    String type;
    TextView tvHead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_satu);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tvHead = findViewById(R.id.tvHead);
        db = new Database_Bengkel_(this);
        type = getIntent().getStringExtra("type");
        if (type.equals("pelanggan")) {
            ConstraintLayout cl = findViewById(R.id.status);
            cl.setVisibility(View.VISIBLE);
            setStatus();
//            getSupportActionBar().setTitle("Laporan Pelanggan");
            tvHead.setText("Laporan Pelanggan");
            getPelanggan("");
            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
            eCari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String a = eCari.getText().toString() ;
                    arrayList.clear();
                    getPelanggan(a);                }
            });

            Spinner spinner = (Spinner) findViewById(R.id.sStatus) ;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getPelanggan(eCari.getText().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (type.equals("teknisi")) {
            tvHead.setText("Laporan Teknisi");
//            getSupportActionBar().setTitle("Laporan Teknisi");
            getTeknisi("");
            final EditText eCari = (EditText) findViewById(R.id.eCari) ;
            eCari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String a = eCari.getText().toString() ;
                    arrayList.clear();
                    getTeknisi(a);

                }
            });
        }
    }

    public void setStatus() {
        arrayStat.clear();
        Spinner spinner = (Spinner) findViewById(R.id.sStatus);
        arrayStat.add("Semua");
        arrayStat.add("Tidak Berhutang");
        arrayStat.add("Berhutang");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayStat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner.setSelection(0);
    }

    public String wherePelanggan(String a) {
        Spinner sTeknisi = findViewById(R.id.sStatus);
        int id = sTeknisi.getSelectedItemPosition();
        if (id == 0) {
            return ModulBengkel.selectwhere("tblpelanggan")  +" idpelanggan!=1 AND "+ModulBengkel.sLike("pelanggan",a) + " ORDER BY pelanggan ASC";
        } else if(id==1){
            return ModulBengkel.selectwhere("tblpelanggan")  +" idpelanggan!=1 AND hutang =0  AND "+ModulBengkel.sLike("pelanggan",a) + " ORDER BY pelanggan ASC";

        }else{
            return ModulBengkel.selectwhere("tblpelanggan")  +" idpelanggan!=1 AND hutang >0  AND "+ModulBengkel.sLike("pelanggan",a) + " ORDER BY pelanggan ASC";
        }
    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapPelanggan(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = wherePelanggan(cari);
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur = ModulBengkel.getString(c,"idpelanggan")+"__"+ModulBengkel.getString(c,"pelanggan") + "__" + ModulBengkel.getString(c,"alamat")+ "__" + ModulBengkel.getString(c,"notelp")+ "__" + ModulBengkel.getString(c,"hutang");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();
    }

    public void getTeknisi(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapTeknisi(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("tblteknisi")  +" idteknisi!=1 AND "+ModulBengkel.sLike("teknisi",cari) + " ORDER BY teknisi ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idteknisi")+"__"+ModulBengkel.getString(c,"teknisi") + "__" + ModulBengkel.getString(c,"alamatteknisi")+ "__" + ModulBengkel.getString(c,"noteknisi");
            arrayList.add(campur);
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i= new Intent(MenuLaporanSatu.this,MenuLaporanSatu.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}


class AdapterLapPelanggan extends RecyclerView.Adapter<AdapterLapPelanggan.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapPelanggan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hutang, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setVisibility(View.GONE);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No HP : "+row[3]);
        holder.hutang.setText("Hutang : Rp "+ModulBengkel.removeE(row[4]));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt,hutang;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            hutang=(TextView) itemView.findViewById(R.id.tHutang);

        }
    }
}


class AdapterLapTeknisi extends RecyclerView.Adapter<AdapterLapTeknisi.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapTeknisi(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pelanggan, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setVisibility(View.GONE);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No HP : "+row[3]);



        holder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.tvOpt);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Intent intent = new Intent(c,FormTambahTeknisi.class);
                                intent.putExtra("id",holder.tvOpt.getTag().toString());
                                c.startActivity(intent);
                                break;
                            case  R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Bengkel_ db = new Database_Bengkel_(c);;
                                                String id = holder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tblpelanggan WHERE idteknisi="+id ;
                                                db.exc(q);
//
                                                if (c instanceof PemanggilMethod){
                                                    ((PemanggilMethod)c).getPelanggan("");
                                                }




                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                alert=alertDialog.create();

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
        TextView nama,alamat,notelp,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);


        }
    }
}
