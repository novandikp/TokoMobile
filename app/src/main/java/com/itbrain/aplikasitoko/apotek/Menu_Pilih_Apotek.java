package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Pilih_Apotek extends AppCompatActivity {
    ModulApotek config,temp;
    DatabaseApotek db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ConstraintLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pilih_apotek);


        config = new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseApotek(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton imageButton = findViewById(R.id.kembali10);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
        final String type= getIntent().getStringExtra("type");
        if (type.equals("pelanggan")){
            getPelanggan("");
        }else if(type.equals("barang")){
            getoBarang("");
        }else if(type.equals("barang1")){
            getoBarang1("");
        }else if(type.equals("supplier")){
            getSupplier("");
        }




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
                if (type.equals("pelanggan")){
                    getPelanggan(a);
                }else if(type.equals("barang")){
                    getoBarang(a);
                }else if(type.equals("barang1")){
                    getoBarang1(a);
                }else if(type.equals("supplier")){
                    getSupplier(a);
                }
            }
        });

    }

    public void getSupplier(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihSupplier(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("tblsupplier") +" idsupplier!=1 and "+ ModulApotek.sLike("supplier",cari) + " ORDER BY supplier ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulApotek.getString(c,"idsupplier")+"__"+ ModulApotek.getString(c,"supplier") + "__" + ModulApotek.getString(c,"alamatsupplier")+ "__" + ModulApotek.getString(c,"nosupplier");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public String whereKat(String cari) {
        Spinner sp = (Spinner) findViewById(R.id.sKat);
        String id = ModulApotek.intToStr(sp.getSelectedItemPosition());
        if (id.equals("0")) {
            return ModulApotek.selectwhere("qbarang") +  ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        } else if (id.equals("1")) {
            return ModulApotek.selectwhere("qbarang") + "idkategori==1 AND " + ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        } else  {
            return ModulApotek.selectwhere("qbarang") + "idkategori!=1 AND " + ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        }
    }



    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPelanggan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("tblpelanggan") +" idpelanggan!=1 and " + ModulApotek.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulApotek.getString(c,"idpelanggan")+"__"+ ModulApotek.getString(c,"pelanggan") + "__" + ModulApotek.getString(c,"alamat")+ "__" + ModulApotek.getString(c,"notelp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPilihBarang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("tblbarang") + ModulApotek.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulApotek.getString(c,"idbarang")+"__"+ ModulApotek.getString(c,"barang") + "__" + ModulApotek.getString(c,"stok")+ "__" + "pembelian";
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getoBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPilihBarang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qbarang") + ModulApotek.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String idbarang = ModulApotek.getString(c,"idbarang");
            String hargasatu="", hargadua="", satuankecil="" , satuanbesar="",stok;
            satuanbesar= ModulApotek.getString(c,"satuanbesar");

            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbarang",idbarang)+"ORDER BY idbelidetail");
            try{
                if (b.getCount()>0){
                    b.moveToLast();
                    hargasatu= ModulApotek.getString(b,"harga_jual_satu");
                    hargadua= ModulApotek.getString(b,"harga_jual_dua");
                }else{
                    hargasatu="pembelian";
                }
            }catch (Exception  e){
                hargasatu="pembelian";
            }

            String campur = ModulApotek.getString(c,"idbarang")+"__"+ ModulApotek.getString(c,"barang") + "__" + ModulApotek.getString(c,"stok")+ "__" + hargasatu+"__"+satuanbesar+"__"+hargadua+ "__" + ModulApotek.getString(c,"satuankecil")+ "__" + ModulApotek.getString(c,"nilai");
            arrayList.add(campur);


        }

        adapter.notifyDataSetChanged();
    }

    public void getoBarang1(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPilihBarangJual(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qbarang") + ModulApotek.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String idbarang = ModulApotek.getString(c,"idbarang");
            String hargasatu="", hargadua="", satuankecil="" , satuanbesar="",stok;
            satuanbesar= ModulApotek.getString(c,"satuanbesar");

            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbarang",idbarang)+"ORDER BY idbelidetail");
            try{
                if (b.getCount()>0){
                    b.moveToLast();
                    hargasatu= ModulApotek.getString(b,"harga_jual_satu");
                    hargadua= ModulApotek.getString(b,"harga_jual_dua");
                }else{
                    hargasatu="pembelian";
                }
            }catch (Exception  e){
                hargasatu="pembelian";
            }

            String campur = ModulApotek.getString(c,"idbarang")+"__"+ ModulApotek.getString(c,"barang") + "__" + ModulApotek.getString(c,"stok")+ "__" + hargasatu+"__"+satuanbesar+"__"+hargadua+ "__" + ModulApotek.getString(c,"satuankecil")+ "__" + ModulApotek.getString(c,"nilai");
            arrayList.add(campur);


        }

        adapter.notifyDataSetChanged();}}


    /*private void setText() {
        arrayKategori.clear();
        arrayKategori.add("Semua");
        arrayKategori.add("Jasa");
        arrayKategori.add("Onderdil");
        Spinner sp = (Spinner) findViewById(R.id.sKat) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        sp.setSelection(1); */





class AdapterPilihPelanggan extends RecyclerView.Adapter<AdapterPilihPelanggan.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihPelanggan(Context a, ArrayList<String> kota) {
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
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c,Menu_Pembelian_Apotek_.class);
                i.putExtra("idpelanggan",holder.tvOpt.getTag().toString());
                ((Menu_Pilih_Apotek)c).setResult(100,i);
                ((Menu_Pilih_Apotek)c).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);
            cv=itemView.findViewById(R.id.cv);

        }
    }
}

class MasterPilihBarang extends RecyclerView.Adapter<MasterPilihBarang.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPilihBarang(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_apotek_obat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi, tvOpt,tvHarga,tvhargadua;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tfaktur);
            deskripsi = (TextView) view.findViewById(R.id.t1);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cv);
            tvhargadua=view.findViewById(R.id.t3);
            tvHarga=view.findViewById(R.id.t2);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        String stokkecil = ModulApotek.doubleToStr(ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[7]));
        if (row[2].equals("0")){
            viewHolder.deskripsi.setText("Stok Kosong");
        }else{
            viewHolder.deskripsi.setText("Sisa Stok : "+ ModulApotek.unchangeComma(row[2])+" "+row[4]+" / "+ ModulApotek.unchangeComma(stokkecil)+" "+row[6]);
        }
        if (row[3].equals("pembelian")){
            viewHolder.tvHarga.setText("Harga belum tersedia");
            viewHolder.tvhargadua.setText("");
        }else{
            viewHolder.tvHarga.setText("Harga Jual Pertama : Rp."+ ModulApotek.removeE(row[3])+"/"+row[4]);
            viewHolder.tvhargadua.setText("Harga Jual Kedua : Rp."+ ModulApotek.removeE(row[5])+"/"+row[6]);
        }

       viewHolder.tvOpt.setTag(row[0]);
        viewHolder.cvBarang.setTag(row[0]);
        viewHolder.tvOpt.setVisibility(View.GONE);
        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(c,Menu_Pembelian_Apotek_.class);
                    i.putExtra("idbarang",viewHolder.tvOpt.getTag().toString());
                    ((Menu_Pilih_Apotek)c).setResult(200,i);
                    ((Menu_Pilih_Apotek)c).finish();
// if (Modul.strToInt(row[2])==0){
//                    Modul.showToast(c,"Stok Telah Habis");
//                }else{
//                    Intent i =new Intent(c,MenuPembelian.class);
//                    i.putExtra("idbarang",viewHolder.tvOpt.getTag().toString());
//                    ((MenuPilih)c).setResult(200,i);
//                    ((MenuPilih)c).finish();
//                }

            }
        });



    }
}
class MasterPilihBarangJual extends RecyclerView.Adapter<MasterPilihBarangJual.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPilihBarangJual(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_apotek_obat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi, tvOpt,tvHarga,tvhargadua;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tfaktur);
            deskripsi = (TextView) view.findViewById(R.id.t1);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cv);
            tvhargadua=view.findViewById(R.id.t3);
            tvHarga=view.findViewById(R.id.t2);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        String stokkecil = ModulApotek.doubleToStr(ModulApotek.strToDouble(row[2])* ModulApotek.strToDouble(row[7]));
        if (row[2].equals("0")){
            viewHolder.deskripsi.setText("Stok Kosong");
        }else{
            viewHolder.deskripsi.setText("Sisa Stok : "+ ModulApotek.unchangeComma(row[2])+" "+row[4]+" / "+ ModulApotek.unchangeComma(stokkecil)+" "+row[6]);
        }
        if (row[3].equals("pembelian")){
            viewHolder.tvHarga.setText("Harga belum tersedia");
            viewHolder.tvhargadua.setText("");
        }else{
            viewHolder.tvHarga.setText("Harga Jual Pertama : Rp."+ ModulApotek.removeE(row[3])+"/"+row[4]);
            viewHolder.tvhargadua.setText("Harga Jual Kedua : Rp."+ ModulApotek.removeE(row[5])+"/"+row[6]);
        }

       viewHolder.tvOpt.setTag(row[0]);
        viewHolder.cvBarang.setTag(row[0]);
        viewHolder.tvOpt.setVisibility(View.GONE);
        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(row[3].equals("pembelian") || row[2].equals("0")){
                    Toast.makeText(c, "Barang masih kosong", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i =new Intent(c,Menu_Pembelian_Apotek_.class);
                    i.putExtra("idbarang",viewHolder.tvOpt.getTag().toString());
                    ((Menu_Pilih_Apotek)c).setResult(200,i);
                    ((Menu_Pilih_Apotek)c).finish();
                }

// if (Modul.strToInt(row[2])==0){
//                    Modul.showToast(c,"Stok Telah Habis");
//                }else{
//                    Intent i =new Intent(c,MenuPembelian.class);
//                    i.putExtra("idbarang",viewHolder.tvOpt.getTag().toString());
//                    ((MenuPilih)c).setResult(200,i);
//                    ((MenuPilih)c).finish();
//                }

            }
        });



    }
}

class AdapterPilihSupplier extends RecyclerView.Adapter<AdapterPilihSupplier.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public AdapterPilihSupplier(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier_apotek, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, Menu_Pembelian_Apotek_.class);
                i.putExtra("idsupplier", holder.tvOpt.getTag().toString());
                ((Menu_Pilih_Apotek) c).setResult(100, i);
                ((Menu_Pilih_Apotek) c).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, notelp, tvOpt;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cvSupp);
            nama = (TextView) itemView.findViewById(R.id.nama);
            alamat = (TextView) itemView.findViewById(R.id.epelangggan);
            notelp = (TextView) itemView.findViewById(R.id.notelp);
            tvOpt = (TextView) itemView.findViewById(R.id.tvOpt3);

        }
    }
}
