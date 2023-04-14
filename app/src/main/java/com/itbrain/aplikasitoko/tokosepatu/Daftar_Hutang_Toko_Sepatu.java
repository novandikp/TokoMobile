package com.itbrain.aplikasitoko.tokosepatu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Daftar_Hutang_Toko_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;

    public static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_daftar_hutang_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Hutang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        getHutang("");

        final EditText eCari = (EditText) findViewById(R.id.eCari) ;
        final String finalType1 = type;
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
                getHutang(a);

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

    @Override
    protected void onResume() {
        super.onResume();
        getHutang("");
    }

    private void getHutang(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recHutang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterHutang(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("tblpelanggan") + "hutang!=0 AND "+ModulTokoSepatu.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"idpelanggan")+"__"+ModulTokoSepatu.getString(c,"pelanggan") + "__" + ModulTokoSepatu.getString(c,"alamat")+ "__" + ModulTokoSepatu.getString(c,"notelp")+ "__" + ModulTokoSepatu.getString(c,"hutang");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
}

class AdapterHutang extends RecyclerView.Adapter<AdapterHutang.ViewHolder>{
    ArrayList <String> data;
    Context c;

    AdapterHutang(Context a, ArrayList<String>kota){
        this.data=kota;
        c=a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_hutang_sepatu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String[] row = data.get(position).split("__");
        holder.cv.setTag(row[0]);
        holder.pelanggan.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);
        holder.hutang.setText(ModulTokoSepatu.removeE(row[4]));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
                String id = holder.cv.getTag().toString();
                Intent i = new Intent(c,Menu_Bayar_Hutang_Sepatu.class);
                i.putExtra("id",id);
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pelanggan,alamat,notelp,hutang;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);

            pelanggan= itemView.findViewById(R.id.tvPelanggan);
            alamat= itemView.findViewById(R.id.tvAlamat);
            notelp= itemView.findViewById(R.id.tvNo);
            hutang= itemView.findViewById(R.id.tvHutang);
            cv=itemView.findViewById(R.id.cvHutang);

        }
    }
}
