package com.itbrain.aplikasitoko.bengkel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MenuPilihBengkel extends AppCompatActivity {
    ModulBengkel config,temp;
    Database_Bengkel_ db ;
    View v ;
    ArrayList arrayList = new  ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ConstraintLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pilih);

        ImageButton imageButton = findViewById(R.id.kembali23);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        config = new ModulBengkel(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulBengkel(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new Database_Bengkel_(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setText();

        final String type= getIntent().getStringExtra("type");
        if (type.equals("pelanggan")){
         //   getSupportActionBar().setTitle("Pilih Pelanggan");
            getPelanggan("");
        }else if(type.equals("barang")){
         //   getSupportActionBar().setTitle("Pilih Barang");
            getBarang("");
        }else if(type.equals("jasa")){
            spinner=findViewById(R.id.spinner);
            spinner.setVisibility(View.VISIBLE);
//            getSupportActionBar().setTitle("Pilih Jasa/Barang");
            getJasa("");
            Spinner sp = (Spinner) findViewById(R.id.sKat) ;
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getJasa("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if(type.equals("teknisi")){
         //   getSupportActionBar().setTitle("Pilih Teknisi");
            getTeknisi("");
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
                    getBarang(a);
                }else if(type.equals("jasa")){
                    getJasa(a);
                }else if(type.equals("teknisi")){
                    getTeknisi(a);
                }
            }
        });

    }

    public void getTeknisi(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihTeknisi(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("tblteknisi") +" idteknisi!=1 and "+ModulBengkel.sLike("teknisi",cari) + " ORDER BY teknisi ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idteknisi")+"__"+ModulBengkel.getString(c,"teknisi") + "__" + ModulBengkel.getString(c,"alamatteknisi")+ "__" + ModulBengkel.getString(c,"noteknisi");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public String whereKat(String cari) {
        Spinner sp = (Spinner) findViewById(R.id.sKat);
        String id = ModulBengkel.intToStr(sp.getSelectedItemPosition());
        if (id.equals("0")) {
            return ModulBengkel.selectwhere("qbarang") +  ModulBengkel.sLike("barang", cari) + " ORDER BY barang ASC";
        } else if (id.equals("1")) {
            return ModulBengkel.selectwhere("qbarang") + "idkategori==1 AND " + ModulBengkel.sLike("barang", cari) + " ORDER BY barang ASC";
        } else  {
            return ModulBengkel.selectwhere("qbarang") + "idkategori!=1 AND " + ModulBengkel.sLike("barang", cari) + " ORDER BY barang ASC";
        }
    }

    public void getJasa(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPilihJasa(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(whereKat(cari)) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idbarang")+"__"+ModulBengkel.getString(c,"barang") + "__" + ModulBengkel.getString(c,"stok")+ "__" + ModulBengkel.getString(c,"harga")+"__"+ModulBengkel.getString(c,"idkategori");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void getPelanggan(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPilihPelanggan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("tblpelanggan") +" idpelanggan!=1 and " +ModulBengkel.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idpelanggan")+"__"+ModulBengkel.getString(c,"pelanggan") + "__" + ModulBengkel.getString(c,"alamat")+ "__" + ModulBengkel.getString(c,"notelp");
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
        String q = ModulBengkel.selectwhere("tblbarang") +"idkategori!=1 AND "+ ModulBengkel.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idbarang")+"__"+ModulBengkel.getString(c,"barang") + "__" + ModulBengkel.getString(c,"stok")+ "__" + ModulBengkel.getString(c,"harga")+ "__" + ModulBengkel.getString(c,"hargabeli");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

     private void setText() {
         arrayKategori.clear();
         arrayKategori.add("Semua");
         arrayKategori.add("Jasa");
         arrayKategori.add("Onderdil");
         Spinner sp = (Spinner) findViewById(R.id.sKat) ;
         ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKategori);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         sp.setAdapter(adapter);
         adapter.notifyDataSetChanged();
         sp.setSelection(1);

     }


}

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
                Intent i =new Intent(c,MenuPenjualanBengkel.class);
                i.putExtra("idpelanggan",holder.tvOpt.getTag().toString());
                ((MenuPilihBengkel)c).setResult(100,i);
                ((MenuPilihBengkel)c).finish();

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_barang, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang,deskripsi, tvOpt,tvHarga;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cvBarang);
            tvHarga=view.findViewById(R.id.tvHarga);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        viewHolder.deskripsi.setText("Sisa Stok : "+row[2]);
        viewHolder.tvHarga.setText("Harga : "+ModulBengkel.removeE(row[3]));
        viewHolder.tvOpt.setTag(row[0]);
        viewHolder.cvBarang.setTag(row[0]);
        viewHolder.tvOpt.setVisibility(View.GONE);
        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ModulBengkel.strToInt(row[2])==0){
                    ModulBengkel.showToast(c,"Stok Telah Habis");
                }else{
                    Intent i =new Intent(c,MenuPenjualanBengkel.class);
                    i.putExtra("idbarang",viewHolder.tvOpt.getTag().toString());
                    ((MenuPilihBengkel)c).setResult(200,i);
                    ((MenuPilihBengkel)c).finish();
                }

            }
        });



    }
}

class AdapterPilihTeknisi extends RecyclerView.Adapter<AdapterPilihTeknisi.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterPilihTeknisi(Context a, ArrayList<String> kota) {
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
                Intent i =new Intent(c,MenuPenjualanBengkel.class);
                i.putExtra("idteknisi",holder.tvOpt.getTag().toString());
                ((MenuPilihBengkel)c).setResult(300,i);
                ((MenuPilihBengkel)c).finish();

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
            cv=itemView.findViewById(R.id.cv);
            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);

        }
    }
}

class MasterPilihJasa extends RecyclerView.Adapter<MasterPilihJasa.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public MasterPilihJasa(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_barang, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang, deskripsi, tvOpt, tvHarga;
        private CardView cvBarang;


        public ViewHolder(View view) {
            super(view);


            barang = (TextView) view.findViewById(R.id.tvBarang);
            deskripsi = (TextView) view.findViewById(R.id.tvDeskripsi);
            tvOpt = (TextView) view.findViewById(R.id.tvOpt);
            cvBarang = view.findViewById(R.id.cvBarang);
            tvHarga = view.findViewById(R.id.tvHarga);

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[1]);
        if (row[4].equals("1")) {
            viewHolder.deskripsi.setVisibility(View.GONE);
        } else {
            viewHolder.deskripsi.setText("Stok :" + row[2]);
        }
        viewHolder.deskripsi.setTag(row[2]);
        viewHolder.tvHarga.setText("Harga : " + ModulBengkel.removeE(row[3]));
        viewHolder.tvOpt.setTag(row[4]);
        viewHolder.cvBarang.setTag(row[0]);
        viewHolder.cvBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idkategori = viewHolder.tvOpt.getTag().toString();
                String stok = viewHolder.deskripsi.getTag().toString();
                if (idkategori.equals("1")) {
                    Intent i = new Intent(c, MenuPenjualanBengkel.class);
                    i.putExtra("idbarang", viewHolder.cvBarang.getTag().toString());
                    i.putExtra("idkategori", viewHolder.tvOpt.getTag().toString());
                    ((MenuPilihBengkel) c).setResult(200, i);
                    ((MenuPilihBengkel) c).finish();
                } else {
                    if (stok.equals("0")) {
                        ModulBengkel.showToast(c, "Stok telah habis");
                    } else {
                        Intent i = new Intent(c, Menu_Servis_Bengkel_.class);
                        i.putExtra("idbarang", viewHolder.cvBarang.getTag().toString());
                        i.putExtra("idkategori", viewHolder.tvOpt.getTag().toString());
                        ((MenuPilihBengkel) c).setResult(200, i);
                        ((MenuPilihBengkel) c).finish();
                    }
                }

            }
        });


        viewHolder.tvOpt.setVisibility(View.GONE);

    }
}



