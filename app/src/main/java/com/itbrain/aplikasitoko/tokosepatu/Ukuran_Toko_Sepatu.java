package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.PemanggilMethod;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Ukuran_Toko_Sepatu extends AppCompatActivity implements PemanggilMethod {
    public static String idbarang;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ukuran_sepatu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idbarang= getIntent().getStringExtra("idbar");

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);

        getBarang("");
        TextView tv = findViewById(R.id.tvBarang);
        TextView tvdes = findViewById(R.id.tvDek);
        ConstraintLayout cl= findViewById(R.id.constraintLayout3);
//        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
//        tv.setTransitionName("judul");
//        tvdes.setTransitionName("subjudul");
        cl.setTransitionName("kotak");

        getUkuran();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        getBarang("");
        getUkuran();
        super.onResume();
    }



    @Override
    public void getKategori(String cari) {

    }

    @Override
    public void getBarang(String cari) {
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang="+idbarang) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.tvBarang,ModulTokoSepatu.getString(c,"barang")) ;
            ModulTokoSepatu.setText(v,R.id.tvDek,ModulTokoSepatu.getString(c,"deskripsi")) ;
            ModulTokoSepatu.setText(v,R.id.eStokBar,ModulTokoSepatu.getString(c,"stokbarang")) ;

        }
    }

    @Override
    public void getPelanggan(String cari) {

    }

    public void getUkuran() {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recUkur) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterUkuran(this,arrayList) ;
        recyclerView.setAdapter(adapter);

        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tblukuran")+ModulTokoSepatu.sWhere("idbarang",idbarang) + " ORDER BY idukuran ASC") ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idukuran")+"__"+ModulTokoSepatu.getString(c,"ukuran") + "__" + ModulTokoSepatu.getString(c,"stok")+ "__" + ModulTokoSepatu.getString(c,"harga");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }


    public void tambah(View view) {
        Intent i = new Intent(Ukuran_Toko_Sepatu.this,Menu_Tambah_Ukuran_Sepatu.class);
        i.putExtra("idbar",idbarang);
        startActivity(i);

    }
}


class AdapterUkuran extends RecyclerView.Adapter<AdapterUkuran.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterUkuran(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_ukuran_salon, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ukuran,harga,stokuk,tvOpt;



        public ViewHolder(View view) {
            super(view);


            ukuran = (TextView) view.findViewById(R.id.tvUkuran);
            harga = (TextView) view.findViewById(R.id.tvHargaUk);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            stokuk = view.findViewById(R.id.tvStokUk);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.ukuran.setText(ModulTokoSepatu.toUppercase(row[1]));
        viewHolder.stokuk.setText(row[2]);
        viewHolder.harga.setText(ModulTokoSepatu.removeE(row[3]));

        viewHolder.tvOpt.setTag(row[0]);


        viewHolder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,viewHolder.tvOpt);
                popupMenu.inflate(R.menu.menu_option_sepatu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:

                                Intent intent = new Intent(c,Menu_Ubah_Ukuran_Sepatu.class);
                                intent.putExtra("id",viewHolder.tvOpt.getTag().toString());
                                intent.putExtra("idbar",Ukuran_Toko_Sepatu.idbarang);
                                c.startActivity(intent);
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

                                                String q = "DELETE FROM tblukuran WHERE idukuran="+id ;

                                                if(db.exc(q)){
                                                    if (c instanceof PemanggilMethod){
                                                        ((PemanggilMethod)c).getUkuran();
                                                        ((PemanggilMethod)c).getBarang("");
                                                    }
                                                }else {
                                                    Toast.makeText(c, "Data masih dipakai", Toast.LENGTH_SHORT).show();
                                                }

//



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
}




