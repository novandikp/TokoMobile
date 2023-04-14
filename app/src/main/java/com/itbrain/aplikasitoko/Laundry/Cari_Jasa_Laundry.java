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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterJasaLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Cari_Jasa_Laundry extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseLaundry db;
    Spinner spinner;
    String keyword = "", kategori = "";
    String a;
    List<String> idKat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_jasa_laundry);
        db=new DatabaseLaundry(this);
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinner = (Spinner) findViewById(R.id.setatus);
        getKategoriData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategori = db.getIdKategori().get(parent.getSelectedItemPosition());
                getJasa(keyword, kategori);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText edtCari = (EditText) findViewById(R.id.dicari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = edtCari.getText().toString();
                getJasa(keyword, kategori);
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
    private void getKategoriData(){
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,db.getKategori());
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(data);
    }
    public  void getJasa(String keyword,String kategori){
        recyclerView = (RecyclerView) findViewById(R.id.recJasaa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListJasaCari(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q;

        if (keyword.isEmpty()){
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa";
            }else {
                q="SELECT * FROM qjasa WHERE idkategori='"+kategori+"'";
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
            }
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idjasa")+"__"+
                        ModulLaundry.getString(c,"idkategori")+"__"+
                        ModulLaundry.getString(c,"kategori")+"__"+
                        ModulLaundry.getString(c,"jasa")+"__"+
                        ModulLaundry.getString(c,"biaya")+"__"+
                        ModulLaundry.getString(c,"satuan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahdata(View view) {
            DialogMasterJasaLaundry dialogMasterJasaLaundry =new DialogMasterJasaLaundry(this,true,null,false);
    }
    public void getList(){
        getKategoriData();
        getJasa(keyword,kategori);
        }
    @Override
    protected void onResume() {
        super.onResume();
        getKategoriData();
        getJasa(keyword,kategori);
        }
}
class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder>{
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterListJasaCari(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.list_transaksi_cari_jasa_laundry,viewGroup,false);
        return new JasaCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.nama.setText(row[3]);
        holder.biaya.setText("Harga : "+ ModulLaundry.removeE(row[4]));
        if(row[5].equals("pc")){
            holder.satuan.setText("/Pcs");
        }else if (row[5].equals("kg")){
            holder.satuan.setText("/Kg");
        }else if (row[5].equals("m2")){
            holder.satuan.setText("/M²");
        }else {
            holder.satuan.setText(row[5]);
        }
        holder.kategori.setText(row[2]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter, ActivityTransaksiTerimaLaundry.class);
                terima.putExtra("idjasa",Integer.valueOf(row[0]));
                terima.putExtra("idkategori",Integer.valueOf(row[1]));
                terima.putExtra("namajasa",row[3]);
                terima.putExtra("kategorijasa",row[2]);
                terima.putExtra("biayajasa",row[4]);
                terima.putExtra("satuanjasa",row[5]);
                ((Cari_Jasa_Laundry)ctxAdapter).setResult(3000,terima);
                ((Cari_Jasa_Laundry)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class JasaCariViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,satuan,kategori;
        public JasaCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaJasa);
            biaya=(TextView)itemView.findViewById(R.id.tvBiayaJasa);
            satuan=(TextView)itemView.findViewById(R.id.tvJenisSatuan);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriJasa);
        }
    }
}