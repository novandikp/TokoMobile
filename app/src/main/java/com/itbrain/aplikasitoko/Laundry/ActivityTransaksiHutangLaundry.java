package com.itbrain.aplikasitoko.Laundry;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ActivityTransaksiHutangLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_hutang_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db=new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali11);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EditText edtCari = (EditText) findViewById(R.id.dicari2);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cari= ModulLaundry.getText(v,R.id.eCari);
                getHutang(cari);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHutang("");
    }

    private void getHutang(String keyword){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recHutang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList= new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListHutang(this,arrayList);
        recyclerView.setAdapter(adapter);

        String q;
        if (keyword.isEmpty()){
            q="SELECT * FROM tblpelanggan WHERE hutang>0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE hutang>0 AND pelanggan='"+keyword+"'";
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idpelanggan")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"hutang");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
class AdapterListHutang extends RecyclerView.Adapter<AdapterListHutang.HutangViewHolder>{
    private ArrayList<String> data;
    private Context ctx;

    public AdapterListHutang(Context ctx,ArrayList<String> data) {
        this.data = data;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public HutangViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_tranksaksi_hutang_laundry,viewGroup,false);
        return new HutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HutangViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");

        holder.pelanggan.setText(row[1]);
        holder.hutang.setText("Rp. "+ ModulLaundry.removeE(row[2]));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx,ActivityTransaksiHutangBayarLaundry.class);
                i.putExtra("id",row[0]);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HutangViewHolder extends RecyclerView.ViewHolder{
        TextView pelanggan, hutang;
        public HutangViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan=(TextView)itemView.findViewById(R.id.tvNamaPelanggan);
            hutang=(TextView)itemView.findViewById(R.id.tvJumlahHutang);
        }
    }
}
