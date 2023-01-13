package com.itbrain.aplikasitoko.restoran;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.Model.MenuRestoran;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;
import com.itbrain.aplikasitoko.ModulBengkel;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Tambah_Isi_Menu_Restoran_ extends AppCompatActivity {

    String type,mmKeyword="";
    Database_Restoran db;
    RecyclerView recyclerView;
    Menu_Restoran_Adapter adapter;
    Spinner kategori;

    View v;

    ArrayList<MenuRestoran> listMenu;
    List<String> listnamakategori;
    List<String> idkategori;
    private EditText eCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_isi_menu_restoran_);

        listnamakategori = new ArrayList<>();
        idkategori = new ArrayList<>();
        v = this.findViewById(android.R.id.content);

        load();

        selectSpinner();

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.tambahMenu);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(Tambah_Isi_Menu_Restoran_.this, Form_Tambah_Menu_Restoran_.class);
                    startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mmKeyword=editable.toString();
               selectSpinner();
            }
        });
    }

    public String getIdKategoriAtPosition(int position){
        return getIdKategori().get(position);
    }

    public List<String> getIdKategori(){
        List<String> labels = new ArrayList<String>();
        String q= Query.select("tblkategori");
        Cursor c = db.sq(q);
        if (c.getCount()>0){
            labels.add("0");
            while (c.moveToNext()){
                String data= ModulBengkel.getString(c,"idkategori");
                labels.add(data);
            }
        }else {
            labels.add("0");
        }
        return labels;
    }

    public void load() {
        db = new Database_Restoran(this);

        listMenu = new ArrayList<>();
        adapter = new Menu_Restoran_Adapter(listMenu, this);

        recyclerView = findViewById(R.id.rcvMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        kategori = findViewById(R.id.spCari);
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectData(ModulRestoran.getText(v,R.id.eCari), getIdKategoriAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public List<String> getKategori(){
        List<String> labels = new ArrayList<String>();
        String q=Query.select("tblkategori");
        Cursor c = db.sq(q);
        if (c.getCount()>0){
            labels.add(ModulRestoran.getResString(this,R.string.semuakategori));
            while (c.moveToNext()){
                String data=ModulRestoran.getString(c,"kategori");
                labels.add(data);
            }
        }else {
            labels.add(getResources().getString(R.string.kosong));
        }
        return labels;
    }

    protected void onResume() {
        super.onResume();

        selectData(mmKeyword, "");
    }

    public void selectSpinner() {
        String sqll = "SELECT * FROM tblkategori WHERE idkategori";

        Cursor cursor = db.sq(sqll);
        if (cursor != null) {
            listnamakategori.add("Semua Kategori");
            idkategori.add("1");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listnamakategori);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kategori.setAdapter(adapter);
            while (cursor.moveToNext()) {
                listnamakategori.add(cursor.getString(cursor.getColumnIndex("kategori")));
                idkategori.add(cursor.getString(cursor.getColumnIndex("idkategori")));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void selectData(String keyword, String kategori) {
//        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rcvMenu);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);

        ArrayList arrayList=new ArrayList();
//        RecyclerView.Adapter adapter = new Menu_Restoran_Adapter(this, arrayList,getIdKategori(),getIdKategori());

        String sql = "";
        if (keyword.isEmpty()) {
            if (kategori.equals("0")){
                sql="SELECT * FROM qmakanan";
            }else {
                sql="SELECT * FROM qmakanan WHERE idkategori='"+kategori+"'";
            }
        } else {
            if (kategori.equals("0")) {
                sql="SELECT * FROM qmakanan WHERE makanan LIKE '%"+keyword+"%'";
            }else {
                sql="SELECT * FROM qmakanan WHERE makanan LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'  ";
            }
        }

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listMenu.clear();
            while (cursor.moveToNext()) {
                listMenu.add(new MenuRestoran(
                        cursor.getInt(cursor.getColumnIndex("idmakanan")),
                        cursor.getString(cursor.getColumnIndex("makanan")),
                        cursor.getString(cursor.getColumnIndex("harga")),
                        cursor.getString(cursor.getColumnIndex("idkategori")),
                        cursor.getString(cursor.getColumnIndex("kategori"))
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }

}

class Menu_Restoran_Adapter extends RecyclerView.Adapter<Menu_Restoran_Adapter.ViewHolder> {

    private ArrayList<MenuRestoran> barang;
    private Context context;
    List<String> kategori;
    List<String> idkategori;

    public Menu_Restoran_Adapter(ArrayList<MenuRestoran> barang, Context context) {
        this.barang = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public Menu_Restoran_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_makanan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Menu_Restoran_Adapter.ViewHolder holder, int position) {
        MenuRestoran item = barang.get(position);

        holder.tvMakanan.setText(item.getMenuRestoran());
        holder.tvHarga.setText(ModulRestoran.removeE(item.getHarga()));
        holder.tvNamaKategori.setText(item.getKategori());

        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.tvMenu);
                popupMenu.inflate(R.menu.menu_option);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(context, Form_Tambah_Menu_Restoran_.class);

                                intent.putExtra("idmakanan", item.getIdmakanan());
                                intent.putExtra("makanan", item.getMakanan());
                                intent.putExtra("harga", item.getHarga());
                                intent.putExtra("idkategori", item.getIdkategori());

                                context.startActivity(intent);
                                break;

                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                                AlertDialog alert;
                                alertDialog.setMessage("Apakah Andah Ingin Menghapus Data Ini?")
                                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Restoran db = new Database_Restoran(context);

                                                String id = ""+item.getIdmakanan();
                                                String sql = "DELETE FROM tblmakanan WHERE idmakanan="+id;

                                                if (db.exc(sql)) {
                                                    Toast.makeText(context, "Hapus Data", Toast.LENGTH_SHORT).show();
                                                    barang.remove(position);
                                                    notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(context, "Gagal Hapus", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                alert = alertDialog.create();
                                alert.setTitle("Hapus Data");
                                alert.show();

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
        return barang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMakanan, tvMenu, tvHarga, tvNamaKategori;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMakanan = itemView.findViewById(R.id.tvNamaMakanan);
            tvMenu = itemView.findViewById(R.id.tvOpt);
            tvHarga = itemView.findViewById(R.id.tvHargaMakanan);
            tvNamaKategori = itemView.findViewById(R.id.tvKategoriMakanan);
        }
    }
}