package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Tambah_Pelanggan_Toko_Kasir_ extends AppCompatActivity {

    String type;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_pelanggan_toko_kasir_);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);

        setText();

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.tambahPelanggan2);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Pelanggan_Toko_Kasir_.this, Form_Tambah_Pelanggan_Toko_Kasir_.class);
                startActivity(intent);
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.sPencarianPelanggan);
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
                getPelanggan(a);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan("");
    }

    private void setText() {
        arrayKategori.add("Semua");
        //Spinner spinner = (Spinner) findViewById(R.id.sPelanggan2) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori")) ;
        if(FFunctionKasir.getCount(c) > 0){
            while(c.moveToNext()){
                arrayKategori.add(FFunctionKasir.getString(c,"kategori"));
                arrayid.add(FFunctionKasir.getString(c,"idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvPelanggan2) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPelanggan(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = "" ;
        if(TextUtils.isEmpty(cari)){
            q = FQueryKasir.select("tblpelanggan") +" EXCEPT "+FQueryKasir.selectwhere("tblpelanggan")+FQueryKasir.sWhere("idpelanggan","1") + FQueryKasir.sOrderASC("pelanggan");
        } else {
            q = FQueryKasir.selectwhere("tblpelanggan") + FQueryKasir.sLike("pelanggan",cari) +" EXCEPT "+FQueryKasir.selectwhere("tblpelanggan")+FQueryKasir.sWhere("idpelanggan","1")  +FQueryKasir.sOrderASC("pelanggan");
        }
        Cursor c = db.sq(q) ;
        if(FFunctionKasir.getCount(c) > 0){
            FFunctionKasir.setText(v,R.id.tJumlahPelanggan2,"Jumlah Data : "+FFunctionKasir.intToStr(FFunctionKasir.getCount(c))) ;
            while(c.moveToNext()){
                String campur =  FFunctionKasir.getString(c,"idpelanggan")+"__"+FFunctionKasir.getString(c,"pelanggan") + "__" + FFunctionKasir.getString(c,"alamat")+ "__" + FFunctionKasir.getString(c,"telp");
                arrayList.add(campur);
            }
        } else {
            FFunctionKasir.setText(v,R.id.tJumlahPelanggan2,"Jumlah Data : 0") ;
        }
        adapter.notifyDataSetChanged();
    }

    public  boolean cekKondisi(String type,String id) {
        //if(type.equals("pelanggan")){
        Cursor c = db.sq("SELECT * FROM tblbayar WHERE idpelanggan = " + id);
        if (FFunctionKasir.getCount(c) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void wUbahPelanggan(View view){
        Intent i = new Intent(this, ActivityUbahPelangganKasir.class) ;
        i.putExtra("type",type) ;
        i.putExtra("id",view.getTag().toString()) ;
        startActivity(i);
    }

    public void hapusPelanggan(final View view){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String q = "" ;
                        String id = view.getTag().toString() ;
                        //if(type.equals("pelanggan")){

                            if(cekKondisi(type,id)){
                                q = "DELETE FROM tblpelanggan WHERE idpelanggan="+id ;
                                db.exc(q) ;
                                getPelanggan("");
                            }else {
                                Toast.makeText(Tambah_Pelanggan_Toko_Kasir_.this, "Data masih dipakai", Toast.LENGTH_SHORT).show();
                            }

                        }
                });

        alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class MasterPelanggan extends RecyclerView.Adapter<MasterPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public MasterPelanggan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public MasterPelanggan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_kasir_master_list_pelanggan, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama, alamat, telp;
            ConstraintLayout ubah, hapus;

            public ViewHolder(View view) {
                super(view);

                telp = (TextView) view.findViewById(R.id.telp);
                alamat = (TextView) view.findViewById(R.id.alamat);
                nama = (TextView) view.findViewById(R.id.nama);
                ubah = (ConstraintLayout) view.findViewById(R.id.wHapusPelanggan);
                hapus = (ConstraintLayout) view.findViewById(R.id.wUbahPelanggan);
            }
        }

        @Override
        public void onBindViewHolder(MasterPelanggan.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.nama.setText("Nama :"+row[1]);
            viewHolder.alamat.setText("Alamat pelanggan :"+row[2]);
            viewHolder.telp.setText("Nomor telepon :"+row[3]);
            viewHolder.ubah.setTag(row[0]);
            viewHolder.hapus.setTag(row[0]);
        }
    }
}