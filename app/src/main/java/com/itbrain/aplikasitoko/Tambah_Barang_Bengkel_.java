package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContentInfo;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.Barang;
import com.itbrain.aplikasitoko.Model.Kategori;
import com.itbrain.aplikasitoko.Model.Pelanggan;

import java.util.ArrayList;
import java.util.List;

public class Tambah_Barang_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Barang_Bengkel_Adapter adapter;
    Spinner kategori;

    ArrayList<Barang> listBarang;
    List<String> listnamakategori;
    List<String> idkategori;
    private EditText cari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang_bengkel_);

        listnamakategori = new ArrayList<>();
        idkategori = new ArrayList<>();

        load();

        selectSpinner();
        Button button = findViewById(R.id.TambahBarang);
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cari = (EditText) findViewById(R.id.eCari);
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectData();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Barang_Bengkel_.this, Form_Tambah_Barang_Bengkel_.class);
                startActivity(intent);
            }
        });
    }

    public void load() {
        db = new Database_Bengkel_(this);

        listBarang = new ArrayList<>();
        adapter = new Barang_Bengkel_Adapter(listBarang,this);

        recyclerView = findViewById(R.id.rcvBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        kategori = findViewById(R.id.spCari);
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

  protected void onResume() {
      super.onResume();

      selectData();
  }

  public void selectSpinner() {
        String sqll = "SELECT * FROM tblkategori WHERE idkategori !=1";

        Cursor cursor = db.sq(sqll);
        if (cursor != null) {
            listnamakategori.add("Semua Kategori");
            idkategori.add("-1");
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

    public void selectData() {

        String sql;
        if (kategori.getSelectedItemPosition() == 0){
            sql =  "SELECT * FROM tblbarang WHERE idkategori !=1 AND barang LIKE '%"+cari.getText().toString()+"%' ";
        }else{
            String idkategoriSelected = idkategori.get(kategori.getSelectedItemPosition());
            sql =  "SELECT * FROM tblbarang WHERE idkategori !=1 AND idkategori="+idkategoriSelected+" AND barang LIKE '%"+cari.getText().toString()+"%' ";
        }
//        Toast.makeText(this, sql, Toast.LENGTH_SHORT).show();
        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listBarang.clear();
            while (cursor.moveToNext()) {
                listBarang.add(new Barang(
                        cursor.getInt(cursor.getColumnIndex("idbarang")),
                        cursor.getString(cursor.getColumnIndex("barang")),
                        cursor.getString(cursor.getColumnIndex("hargabeli")),
                        cursor.getString(cursor.getColumnIndex("stok")),
                        cursor.getString(cursor.getColumnIndex("harga")),
                        cursor.getString(cursor.getColumnIndex("idkategori"))


                ));
            }
            adapter.notifyDataSetChanged();
        }
    }

}

class Barang_Bengkel_Adapter extends RecyclerView.Adapter<Barang_Bengkel_Adapter.ViewHolder>{
    private ArrayList<Barang> barangs;
    private Context context;

    public Barang_Bengkel_Adapter(ArrayList<Barang> barang, Context context) {
        this.barangs = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public Barang_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Barang_Bengkel_Adapter.ViewHolder holder, int position) {
        Barang item = barangs.get(position);

        holder.tvHargaBeli.setText(ModulBengkel.removeE(item.getHargabeli()));
        holder.tvStok.setText(item.getStok());
        holder.tvHarga.setText(ModulBengkel.removeE(item.getHarga()));
        holder.tvBarang.setText(item.getBarang());

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
                                Intent intent = new Intent(context, Form_Tambah_Barang_Bengkel_.class);

                                intent.putExtra("idbarang", item.getIdbarang());
                                intent.putExtra("barang", item.getBarang());
                                intent.putExtra("stok", item.getStok());
                                intent.putExtra("harga", item.getHarga());
                                intent.putExtra("hargabeli", item.getHargabeli());
                                intent.putExtra("idkategori", item.getIdkategori());

                                context.startActivity(intent);
                                break;
                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                                AlertDialog alert;
                                alertDialog.setMessage("Apakah Anda Ingin Menghapus Data Ini?")
                                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Bengkel_ db = new Database_Bengkel_(context);

                                                String id = ""+item.getIdbarang();

                                                String sql = "DELETE FROM tblbarang WHERE idbarang="+id;

                                                if (db.exc(sql)) {
                                                    pesan("Hapus Data");
                                                    barangs.remove(position);
                                                    notifyDataSetChanged();
                                                }else {
                                                    pesan("Data Sedang Digunakan");
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

    public void pesan(String isi) {
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return barangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHargaBeli, tvStok, tvHarga, tvBarang, tvMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvHargaBeli = itemView.findViewById(R.id.tvHargaBeli);
            tvStok = itemView.findViewById(R.id.tvStok);
            tvHarga = itemView.findViewById(R.id.tvHargaJual);
            tvBarang = itemView.findViewById(R.id.tvNamaBarang);
        }
    }
}


