package com.itbrain.aplikasitoko.bengkel;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.itbrain.aplikasitoko.PemanggilMethod;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Teknisi_Bengkel_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    ArrayList arrayStat = new ArrayList();
    Database_Bengkel_ db;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_teknisi_bengkel_);
        ImageButton imageButton = findViewById(R.id.kembali26);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db= new Database_Bengkel_(this);
        type=getIntent().getStringExtra("type");
        if(type.equals("teknisi")){
//            getSupportActionBar().setTitle("Laporan Teknisi");
            getTeknisi("");
            final EditText eCari = (EditText) findViewById(R.id.ecaritek) ;
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
                    getTeknisi(a);

                }
            });
        }
    }
    public void getTeknisi(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcTekni) ;
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
        Intent i= new Intent(Laporan_Teknisi_Bengkel_.this,MenuExportExcelBengkel.class);
        i.putExtra("type",type);
        startActivity(i);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bengkel_teknisi, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvMenu.setVisibility(View.GONE);
        holder.tvTeknisi.setText(row[1]);
        holder.tvAlamat.setText("Alamat : "+row[2]);
        holder.tvNomor.setText("No HP : "+row[3]);



        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.tvMenu);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Intent intent = new Intent(c,FormUbahTeknisibengkel.class);
                                intent.putExtra("id",holder.tvMenu.getTag().toString());
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
                                                String id = holder.tvMenu.getTag().toString();

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
        TextView tvTeknisi, tvAlamat, tvNomor, tvMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTeknisi = itemView.findViewById(R.id.tvNamaTeknisi);
            tvAlamat = itemView.findViewById(R.id.tvAlamatTeknisi);
            tvNomor = itemView.findViewById(R.id.tvNoTeknisi);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }
}
