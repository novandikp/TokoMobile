package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class MenuJasa_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    RecyclerView listjasa;
    AdapterListJasa adapter;
    List<getterJasa> DaftarJasa;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menujasa_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);
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
                getJasa(a);
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

    public void btnTambahJasa(View view) {
        Intent intent=new Intent(this, TambahJasa_Kwitansi.class);
        startActivity(intent);
    }


    public void getJasa(String keyword){
        DaftarJasa = new ArrayList<>();
        listjasa = (RecyclerView) findViewById(R.id.listJasa);
        listjasa.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listjasa.setLayoutManager(layoutManager);
        adapter = new AdapterListJasa(this,DaftarJasa);
        listjasa.setAdapter(adapter);
        String q;
        Cursor c ;
        c = db.sq("SELECT * FROM tbljasa ORDER BY jasa ASC");

        if(c.getCount() > 0){
            while(c.moveToNext()){
                String idjasa = c.getString(c.getColumnIndex("idjasa")) ;
                String jasa = c.getString(c.getColumnIndex("jasa")) ;
                arrayList.add(idjasa+"__"+jasa);
            }
        }
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tbljasa";
        }else {
            q="SELECT * FROM tbljasa WHERE jasa LIKE '%"+keyword+"%' ORDER BY jasa";
        }
        Cursor cur=db.sq(q);
        while(cur.moveToNext()){
            DaftarJasa.add(new getterJasa(
                    cur.getInt(cur.getColumnIndex("idjasa")),
                    cur.getString(cur.getColumnIndex("jasa"))
            ));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJasa("");
    }

    class AdapterListJasa extends RecyclerView.Adapter<AdapterListJasa.JasaViewHolder>{
        private Context ctxAdapter;
        private List<getterJasa> data;

        public AdapterListJasa(Context ctx, List<getterJasa> viewData) {
            this.ctxAdapter = ctx;
            this.data = viewData;
        }

        @NonNull
        @Override
        public JasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.itemjasa_kwitansi,viewGroup,false);
            return new JasaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final JasaViewHolder holder, final int i) {
            final getterJasa getter = data.get(i);
            holder.jasa.setText(getter.getJasa());
            holder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(ctxAdapter,holder.opt);
                    popupMenu.inflate(R.menu.option_item_kwitansi);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_update:
                                    Intent intent = new Intent(ctxAdapter, UpdateJasa_Kwitansi.class);
                                    intent.putExtra("idjasa",getter.getIdJasa());
                                    intent.putExtra("jasa",getter.getJasa());
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseCetakKwitansi db = new DatabaseCetakKwitansi(ctxAdapter);
                                            if (db.deleteJasa(getter.getIdJasa())){
                                                data.remove(i);
                                                notifyDataSetChanged();
                                                Toast.makeText(ctxAdapter, "Delete jasa "+getter.getJasa()+" berhasil", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.setTitle("Hapus "+getter.getJasa())
                                            .setMessage("Anda yakin ingin menghapus "+getter.getJasa()+" dari data jasa");
                                    builder.show();
                                    break;
                                default:
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

        class JasaViewHolder extends RecyclerView.ViewHolder{
            TextView jasa,opt;
            public JasaViewHolder(@NonNull View itemView) {
                super(itemView);
                jasa=(TextView)itemView.findViewById(R.id.tvJasa);
                opt=(TextView)itemView.findViewById(R.id.tvOpt);
            }
        }
    }
    static class getterJasa{
        private int idJasa;
        private String jasa;

        public getterJasa(int idJasa, String jasa) {
            this.idJasa = idJasa;
            this.jasa = jasa;
        }

        public int getIdJasa() {
            return idJasa;
        }

        public String getJasa() {
            return jasa;
        }
    }
}


