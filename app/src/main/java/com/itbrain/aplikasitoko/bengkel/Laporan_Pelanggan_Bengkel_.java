package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Pelanggan_Bengkel_ extends AppCompatActivity {

    ArrayList arrayList = new ArrayList();
    ArrayList arrayStat = new ArrayList();
    Database_Bengkel_ db;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pelanggan_bengkel_);
        ImageButton imageButton = findViewById(R.id.kembali22);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new Database_Bengkel_(this);
        type = getIntent().getStringExtra("type");
       if (type.equals("pelanggan")) {
            setStatus();
            getPelanggan("");
            final EditText eCari = (EditText) findViewById(R.id.eCari);
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
                    getPelanggan(a);
                }
            });
            Spinner spinner = (Spinner) findViewById(R.id.spinner) ;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getPelanggan(eCari.getText().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private void setStatus() {
        arrayStat.clear();
        Spinner spinner = (Spinner) findViewById(R.id.spinner) ;
        arrayStat.add("Semua");
        arrayStat.add("Tidak Berhutang");
        arrayStat.add("Berhutang");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayStat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public String wherePelanggan(String a) {
        Spinner sTeknisi = findViewById(R.id.spinner);
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvR) ;
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

    public void export(View view) {
        Intent i= new Intent(Laporan_Pelanggan_Bengkel_.this,MenuExportExcelBengkel.class);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bengkel_hutang, parent, false);


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
            hutang=itemView.findViewById(R.id.tHutang);

        }
    }
}