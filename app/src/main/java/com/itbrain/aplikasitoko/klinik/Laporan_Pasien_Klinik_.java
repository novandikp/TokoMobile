package com.itbrain.aplikasitoko.klinik;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Pasien_Klinik_ extends AppCompatActivity {
    DatabaseKlinik db;
    ArrayList arrayList = new ArrayList();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pasien_klinik_);
        db = new DatabaseKlinik(this);
        type = getIntent().getStringExtra("type");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        if (type.equals("pasien")) {
            getPasien("");
//        }
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                    getPasien(a);

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

    public void getPasien(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPasien(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulKlinik.selectwhere("tblpasien") +" idpasien!=1 AND "+ModulKlinik.sLike("pasien",cari) + " ORDER BY pasien ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idpasien")+"__"+ModulKlinik.getString(c,"pasien") + "__" + ModulKlinik.getString(c,"alamat")+ "__" + ModulKlinik.getString(c,"notelp")+ "__" + ModulKlinik.getUmur(ModulKlinik.getString(c,"umur")) + "__" + ModulKlinik.getString(c,"jk");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
    public void export(View view) {
        Intent i= new Intent(this,MenuExportExcelKlinik.class);
        i.putExtra("type","pasien");
        startActivity(i);
    }
    }

class AdapterLaporanPasien extends RecyclerView.Adapter<AdapterLaporanPasien.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public AdapterLaporanPasien(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlapasien, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.umur.setText("Umur : "+row[4]);
        holder.nama.setText(row[1]);
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No Telp : "+row[3]);
        if (row[5].equals("P")){
            holder.kelamin.setText("Jenis Kelamin : Perempuan");
        }else{
            holder.kelamin.setText("Jenis Kelamin : Laki-laki");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, notelp, umur,kelamin;

        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp = (TextView) itemView.findViewById(R.id.tNo);
            umur = (TextView) itemView.findViewById(R.id.tvUmur);
            kelamin = (TextView) itemView.findViewById(R.id.tvKelamin);

        }
    }

}

