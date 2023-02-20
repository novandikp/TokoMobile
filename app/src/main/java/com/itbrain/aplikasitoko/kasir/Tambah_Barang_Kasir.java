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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Tambah_Barang_Kasir extends AppCompatActivity {

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
        setContentView(R.layout.tambah_barang_kasir);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);

        setText();

        Spinner spinner = (Spinner) findViewById(R.id.sBabi) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBarang("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahBarang);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Barang_Kasir.this, Form_Tambah_Barang_Kasir_.class);
                startActivity(intent);
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.sPencarianBarang) ;
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
        super.onResume();
        getBarang("");
    }

    private void setText() {
        arrayKategori.add("Semua");
        arrayid.add("0");
        Spinner spinner = (Spinner) findViewById(R.id.sBabi) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori")) ;
        if(FFunctionKasir.getCount(c) > 0){
            while(c.moveToNext()){
                arrayKategori.add(FFunctionKasir.getString(c,"kategori"));
                arrayid.add(FFunctionKasir.getString(c,"idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvBarang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterBarang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = whereKat(cari) ;
        Cursor c = db.sq(q) ;
        if(FFunctionKasir.getCount(c) > 0){
            FFunctionKasir.setText(v,R.id.tJumlahBarang,"Jumlah Data : "+FFunctionKasir.intToStr(FFunctionKasir.getCount(c))) ;
            while(c.moveToNext()){
                String campur = FFunctionKasir.getString(c,"idbarang")+"__"+FFunctionKasir.getString(c,"barang") + "__" + FFunctionKasir.getString(c,"stok")+ "__" + FFunctionKasir.removeE(FFunctionKasir.getString(c,"hargajual"));
                arrayList.add(campur);
            }
        } else {
            FFunctionKasir.setText(v,R.id.tJumlahBarang,"Jumlah Data : 0") ;
        }
        adapter.notifyDataSetChanged();
    }

    public String whereKat(String cari){
        Spinner spinner = (Spinner) findViewById(R.id.sBabi) ;
        String id = arrayid.get(spinner.getSelectedItemPosition()).toString() ;
        if(id.equals("0")){
            return FQueryKasir.selectwhere("tblbarang") + FQueryKasir.sLike("barang",cari) + " ORDER BY barang ASC";
        } else {
            return FQueryKasir.selectwhere("tblbarang")+FQueryKasir.sWhere("idkategori",id)+" AND "+FQueryKasir.sLike("barang",cari) + " ORDER BY barang ASC";
        }
    }

    public void mbohBarang(View view){
        Intent i = new Intent(this, Form_Tambah_Barang_Kasir_.class) ;
        startActivity(i);
    }

    public  boolean cekKondisi(String type,String id){
            Cursor c = db.sq("SELECT * FROM tblpenjualan WHERE idbarang = "+id);
            if(FFunctionKasir.getCount(c)==0){
                return true;
            }else{
                return false;
            }
    }

    public void hapusBarang(final View view){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String q = "" ;
                        String id = view.getTag().toString() ;
                            if(cekKondisi(type,id)){
                                q = "DELETE FROM tblbarang WHERE idbarang="+id ;
                                db.exc(q) ;
                                getBarang("");
                            }else {
                                Toast.makeText(Tambah_Barang_Kasir.this, "Data masih dipakai", Toast.LENGTH_SHORT).show();
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

    public void Wubahbarang(View view){
        Intent i = new Intent(this, ActivityUbahBarangKasir.class) ;
        i.putExtra("type",type) ;
        i.putExtra("id",view.getTag().toString()) ;
        startActivity(i);

    }

    private boolean cekBarang (String id){
        Cursor c = db.sq(FQueryKasir.selectwhere("tblbarang") + FQueryKasir.sWhere("idkategori", id));
        if (FFunctionKasir.getCount(c) > 0) {
            return false;
        } else if (FFunctionKasir.getCount(c) == 0) {
            return true;
        } else {
            return false;
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
        public MasterBarang.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_kasir_master_list_barang, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView barang,stok, harga;
            ConstraintLayout ubah,hapus ;

            public ViewHolder(View view) {
                super(view);

                barang = (TextView) view.findViewById(R.id.barang);
                harga = (TextView) view.findViewById(R.id.tHarga);
                stok = (TextView) view.findViewById(R.id.stok);
                ubah = (ConstraintLayout) view.findViewById(R.id.wHapusBarang) ;
                hapus = (ConstraintLayout) view.findViewById(R.id.wUbahBarang) ;
            }
        }

        @Override
        public void onBindViewHolder(MasterBarang.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.barang.setText(row[1]);
            viewHolder.stok.setText("Sisa Stok : "+row[2]);
            viewHolder.harga.setText("Harga : Rp. "+row[3]);
            viewHolder.ubah.setTag(row[0]);
            viewHolder.hapus.setTag(row[0]);
        }
    }


}