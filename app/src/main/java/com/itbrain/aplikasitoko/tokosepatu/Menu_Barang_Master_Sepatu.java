package com.itbrain.aplikasitoko.tokosepatu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.PemanggilMethod;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKain.Aplikasi_Toko_Kain_Menu_Utilitas_Toko_Kain;

import java.util.ArrayList;

public class Menu_Barang_Master_Sepatu extends AppCompatActivity implements PemanggilMethodSepatu{
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    ArrayList arrayid = new  ArrayList();
    ArrayList arrayKategori = new  ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_barang_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String kode= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmG1ls2hwPhy9jxjxjeN5ztzvWFXfYbXD1fAY2l0wW9WEjGH3fdimmbLfFoUXhwsu2/H6tWeOvS5Aj5YmeL0og/G9pFIvK6DSvQuLYCTKtevI1zWhbnj5oeUL/uqGgmt4tLie2kt/TsmgrIrQQ3hVYJOM6CfdG8ztzAU9nMJ9v7mU0SdbO7nQ/17LUpat00Liw7xWluAGtHbIGDZWN/vgOtbPKYFPbGwLwJcBsVM8hFO03OgBk5TkJ0R7SQ9oiXkAabF0/Ma/VEl+6Tiyb0GD1mkQXO547RHaU8U1o0ov15U91bn7sEfjMOdo+f+dzaXgyTmqX7tLjyWeu2cqeGR/CwIDAQAB";


        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        setText();
        getBarang("");


        Spinner spinner = (Spinner) findViewById(R.id.sKat) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBarang("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                getBarang(a);
            }
        });


    }

    @Override
    protected void onResume() {
        setText();
        getBarang("");
        super.onResume();
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(MenuBarang.this,MenuMaster.class);
//        startActivity(i);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String whereKat(String cari){
        Spinner spinner = (Spinner) findViewById(R.id.sKat) ;
        String id = arrayid.get(spinner.getSelectedItemPosition()).toString() ;
        if(id.equals("0")){
            return ModulTokoSepatu.selectwhere("tblbarang") + ModulTokoSepatu.sLike("barang",cari) + " ORDER BY barang ASC";
        } else {
            return ModulTokoSepatu.selectwhere("tblbarang")+ModulTokoSepatu.sWhere("idkategori",id)+" AND "+ModulTokoSepatu.sLike("barang",cari) + " ORDER BY barang ASC";
        }
    }


    private void setText() {
        arrayKategori.clear();
        arrayKategori.add("Semua");
        arrayid.add("0");
        Spinner spinner = (Spinner) findViewById(R.id.sKat) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(ModulTokoSepatu.select("tblkategori")) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arrayKategori.add(ModulTokoSepatu.getString(c,"kategori"));
                arrayid.add(ModulTokoSepatu.getString(c,"idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void getKategori(String cari) {

    }

    public void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recBarang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterBarang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = whereKat(cari);
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idbarang")+"__"+ModulTokoSepatu.getString(c,"barang") + "__" + ModulTokoSepatu.getString(c,"deskripsi");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void getPelanggan(String cari) {

    }

    @Override
    public void getUkuran() {

    }



    public void tambah(View view) {
        String freetran = config.getCustom("barang","1");
        String profile = config.getCustom("profil","");
        if (Splash_Activity_Sepatu.status){
            Intent i = new Intent(Menu_Barang_Master_Sepatu.this,Menu_Tambah_Barang_Toko_Sepatu.class);
            startActivity(i);

        }else{
            if(ModulTokoSepatu.strToInt(freetran)>5){
                Toast.makeText(this, "Limit telah terlampui", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Aplikasi_Toko_Kain_Menu_Utilitas_Toko_Kain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(Menu_Barang_Master_Sepatu.this,Menu_Tambah_Barang_Toko_Sepatu.class);
                startActivity(i);
            }
        }

    }

    private void DialogBeli() {
        final String produk="com.komputerkit.tokosepatuplus.full";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_inapp_sepatu, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        CardView beli = dialogView.findViewById(R.id.beli);
        CardView tidak = dialogView.findViewById(R.id.cancel);
        CardView petunjuk = dialogView.findViewById(R.id.petunjuk);
        final AlertDialog dialogi = dialog.create();


        petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ixy-dd2jfsc")));
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();
            }
        });

        dialogi.show();
    }

}
class MasterBarang extends RecyclerView.Adapter<MasterBarang.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterBarang(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_barang_sepatu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi, tvOpt;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDek);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cvBarang);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        viewHolder.deskripsi.setText(row[2]);
        viewHolder.tvOpt.setTag(row[0]);
        viewHolder.cvBarang.setTag(row[0]);


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
                                Intent intent = new Intent(c,Menu_Ubah_Barang_Sepatu.class);
                                intent.putExtra("id",viewHolder.tvOpt.getTag().toString());
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

                                                String q = "DELETE FROM tblbarang WHERE idbarang="+id ;
                                                if(db.exc(q)){
                                                    if (c instanceof PemanggilMethod){
                                                        ((PemanggilMethod)c).getBarang("");
                                                    }
                                                }else{
                                                    Toast.makeText(c, "Data masih dipakai", Toast.LENGTH_SHORT).show();
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



                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.barang.setTransitionName("judul");
                viewHolder.deskripsi.setTransitionName("subjudul");
                viewHolder.cvBarang.setTransitionName("kotak");
                Pair<View, String> pair1= Pair.create((View)viewHolder.barang,viewHolder.barang.getTransitionName());
                Pair<View, String> pair2= Pair.create((View)viewHolder.deskripsi,viewHolder.deskripsi.getTransitionName());
                Pair<View, String> pair3= Pair.create((View)viewHolder.cvBarang,viewHolder.cvBarang.getTransitionName());

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity
                        )c,pair3);
                Intent intent = new Intent(c,Ukuran_Toko_Sepatu.class);
                intent.putExtra("idbar",viewHolder.cvBarang.getTag().toString());
                c.startActivity(intent,optionsCompat.toBundle());

            }
        });
    }
}
