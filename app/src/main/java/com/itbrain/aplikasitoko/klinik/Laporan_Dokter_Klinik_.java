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

public class Laporan_Dokter_Klinik_ extends AppCompatActivity {
    DatabaseKlinik db;
    ArrayList arrayList = new ArrayList();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_dokter_klinik_);
        db = new DatabaseKlinik(this);
        type = getIntent().getStringExtra("type");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDokter("");

        final EditText eCari = (EditText) findViewById(R.id.eCari);
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

                getDokter(a);


            }
        });
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDokter(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapDokter(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("tbldokter") + " iddokter!=1 AND " + ModulKlinik.sLike("dokter", cari) + " ORDER BY dokter ASC";
        ;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur = ModulKlinik.getString(c, "iddokter") + "__" + ModulKlinik.getString(c, "dokter") + "__" + ModulKlinik.getString(c, "alamatdokter") + "__" + ModulKlinik.getString(c, "nodokter");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
    public void export(View view) {
        Intent i= new Intent(this,MenuExportExcelKlinik.class);
        i.putExtra("type","dokter");
        startActivity(i);
    }

}

class AdapterLapDokter extends RecyclerView.Adapter<AdapterLapDokter.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterLapDokter(Context a, ArrayList<String> kota) {
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
        holder.alamat.setText("Alamat : "+row[2]);
        holder.notelp.setText("No Telp : "+row[3]);
        holder.tvOpt.setVisibility(View.GONE);
//
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
