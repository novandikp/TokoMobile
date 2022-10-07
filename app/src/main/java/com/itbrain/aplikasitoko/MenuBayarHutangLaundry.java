package com.itbrain.aplikasitoko;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuBayarHutangLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bayar_hutang_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        Modul.btnBack("Daftar Hutang",getSupportActionBar());

        EditText edtCari = (EditText) findViewById(R.id.edtCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cari=Modul.getText(v,R.id.edtCari);
                getHutang(cari);
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
        if (Modul.getCount(c)>0){
            while (c.moveToNext()){
                String campur=Modul.getString(c,"idpelanggan")+"__"+
                        Modul.getString(c,"pelanggan")+"__"+
                        Modul.getString(c,"hutang");
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
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itembayarhutanglaundry,viewGroup,false);
        return new HutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HutangViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");

        holder.pelanggan.setText(row[1]);
        holder.hutang.setText("Rp. "+Modul.removeE(row[2]));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx,MenuHasilBayarHutangLaundry.class);
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
            pelanggan=(TextView)itemView.findViewById(R.id.txtNamaPelanggan);
            hutang=(TextView)itemView.findViewById(R.id.txtJumlah);
        }
    }
}
