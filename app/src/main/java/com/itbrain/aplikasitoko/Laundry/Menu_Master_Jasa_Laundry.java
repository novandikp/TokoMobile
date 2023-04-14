package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterJasaLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Master_Jasa_Laundry extends AppCompatActivity {
    String type, cari = "";
    DatabaseLaundry db;
    String idkat = "0";
    Spinner spKat;
    SharedPreferences getPrefs, pref2, pref3;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jasa_laundrys);
        db = new DatabaseLaundry(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type = getIntent().getStringExtra("type");
        pref2 = getSharedPreferences("id", MODE_PRIVATE);
        edit = pref2.edit();
        EditText edtCari = (EditText) findViewById(R.id.dicari);
        spKat = (Spinner) findViewById(R.id.setatus);
        ImageButton i = findViewById(R.id.Kembali);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cari = s.toString();
                getList(cari);
            }
        });
        spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idkat = db.getIdKategori().get(position);
                getJasa(cari, idkat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    @Override
    protected void onResume(){
        super.onResume();
        getList(cari);
    }

    public void tambahdata(View view) {
                DialogMasterJasaLaundry dialogMasterJasaLaundry =new DialogMasterJasaLaundry(this,true,null,true);
            }
    public void getList(String keyword){
            getJasa(keyword,idkat);
            TextView tvkat=(TextView)findViewById(R.id.tvKat);
            tvkat.setVisibility(View.VISIBLE);
            spKat.setVisibility(View.VISIBLE);
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,db.getKategori());
            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spKat.setAdapter(data);
        }
    private void getJasa(String keyword,String idkategori){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recJasaa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListJasa(this,arrayList,true);
        recyclerView.setAdapter(adapter);
        String q="";
        if (keyword.isEmpty()){
            if (idkategori.equals("0")){
                q= QueryLaundry.select("qjasa");
            }else {
                q= QueryLaundry.selectwhere("qjasa")+ QueryLaundry.sWhere("idkategori",idkategori);
            }
        }else {
            if (idkategori.equals("0")){
                q= QueryLaundry.selectwhere("qjasa")+ QueryLaundry.sLike("jasa",keyword);
            }else {
                q= QueryLaundry.selectwhere("qjasa")+ QueryLaundry.sLike("jasa",keyword)+" AND "+ QueryLaundry.sWhere("idkategori",idkategori);
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

}
class AdapterListJasa extends RecyclerView.Adapter<AdapterListJasa.ListJasaViewHolder>{
    Context context;
    ArrayList<String> data;
    Boolean showOpt;
    DatabaseLaundry db;

    public AdapterListJasa(Context context, ArrayList<String> data, Boolean showOpt) {
        this.context = context;
        this.data = data;
        this.showOpt = showOpt;
    }

    @NonNull
    @Override
    public ListJasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_master_jasa_laundry,viewGroup,false);
        db=new DatabaseLaundry(context);
        return new ListJasaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListJasaViewHolder holder, final int i) {
        final String[] row=data.get(i).split("__");
        holder.nama.setText(row[3]);
        holder.biaya.setText("Biaya : "+ ModulLaundry.removeE(row[4]));
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
        if (showOpt){
            holder.opt.setVisibility(View.VISIBLE);
        }else {
            holder.opt.setVisibility(View.GONE);
        }
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.opt);
                popupMenu.inflate(R.menu.option_item_laundry);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                DialogMasterJasaLaundry dialogMasterJasaLaundry =new DialogMasterJasaLaundry(context,false,row[0],true);
                                break;
                            case R.id.menu_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String q= QueryLaundry.deleteFrom("tbljasa","idjasa",row[0]);
                                        if (db.exc(q)){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Delete jasa "+row[3]+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+row[3]);
                                builder.setMessage("Anda yakin ingin menghapus "+row[3]+" dari data jasa");
                                builder.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListJasaViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,satuan,kategori,opt;
        public ListJasaViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaJasa);
            biaya=(TextView)itemView.findViewById(R.id.tvBiayaJasa);
            satuan=(TextView)itemView.findViewById(R.id.tvJenisSatuan);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriJasa);
            opt=(TextView)itemView.findViewById(R.id.tvOpt);
        }
    }
}