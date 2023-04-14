package com.itbrain.aplikasitoko.tokosepatu;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKain.DatabaseTokoKain;

import java.util.ArrayList;

public class Menu_Daftar_Retur_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_daftar_retur_sepatu);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Barang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);
        getBarang("");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBarang("");
    }

    private void getBarang(String cari){
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recRetur) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterRetur(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulTokoSepatu.selectwhere("view_orderdetail") + "flagretur=0 AND "+ModulTokoSepatu.sLike("pelanggan",cari) +" AND fakturbayar IS NOT NULL "+ " ORDER BY idjual ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulTokoSepatu.getString(c,"iddetailjual")+"__"+ModulTokoSepatu.getString(c,"fakturbayar") + "__" + ModulTokoSepatu.getString(c,"pelanggan")+ "__" + ModulTokoSepatu.getString(c,"barang")+ "__" + ModulTokoSepatu.getString(c,"ukuran")+ "__" + ModulTokoSepatu.getString(c,"jumlah");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }


}

class AdapterRetur extends RecyclerView.Adapter<AdapterRetur.ViewHolder>{
    ArrayList <String> data;
    Context c;

    AdapterRetur(Context a, ArrayList<String>kota){
        this.data=kota;
        c=a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_retur_sepatu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String[] row = data.get(position).split("__");
        holder.cv.setTag(row[0]);
        holder.faktur.setText(row[1]);
        holder.pelanggan.setText(row[2]);
        String barang = row[3]+" ("+row[4]+")";
        holder.barang.setText(barang);
        holder.jumlah.setText("Jumlah barang: "+row[5]);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseTokoSepatu db = new DatabaseTokoSepatu(c);
                String id = holder.cv.getTag().toString();
                Intent i = new Intent(c,Menu_Retur_Sepatu.class);
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
        TextView faktur,pelanggan,barang,jumlah;
        CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            faktur= itemView.findViewById(R.id.faktur);
            pelanggan= itemView.findViewById(R.id.pelanggan);
            barang= itemView.findViewById(R.id.barang);
            jumlah= itemView.findViewById(R.id.jumlah);
            cv=itemView.findViewById(R.id.cvRetur);

        }
    }
}
