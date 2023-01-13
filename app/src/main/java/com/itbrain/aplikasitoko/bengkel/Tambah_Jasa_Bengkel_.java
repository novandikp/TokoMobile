package com.itbrain.aplikasitoko.bengkel;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.Jasa;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Tambah_Jasa_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Jasa_Bengkel_Adapter adapter;

    ArrayList<Jasa> listJasa;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_jasa_bengkel_);

        load();

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

        Button button = findViewById(R.id.TambahJasa);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Jasa_Bengkel_.this, Form_Tambah_Jasa_Bengkel_.class);
                startActivity(intent);
            }
        });
    }

    public void load() {
        db = new Database_Bengkel_(this);

        listJasa = new ArrayList<>();
        adapter = new Jasa_Bengkel_Adapter(listJasa, this);

        recyclerView = findViewById(R.id.rcvJasa_Bengkel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT idbarang, barang, harga, hargabeli FROM tblbarang WHERE idkategori = 1 AND barang LIKE '%"+cari.getText().toString()+"%'";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listJasa.clear();
            while (cursor.moveToNext()) {
                listJasa.add(new Jasa(
                        cursor.getInt(cursor.getColumnIndex("idbarang")),
                        cursor.getString(cursor.getColumnIndex("barang")),
                        cursor.getString(cursor.getColumnIndex("harga")),
                        cursor.getString(cursor.getColumnIndex("hargabeli"))
                ));
            }
            adapter.notifyDataSetChanged();
        }

    }
}

class Jasa_Bengkel_Adapter extends RecyclerView.Adapter<Jasa_Bengkel_Adapter.ViewHolder> {
    private ArrayList<Jasa> jasa;
    private Context context;

    public Jasa_Bengkel_Adapter(ArrayList<Jasa> jasa, Context context) {
        this.jasa = jasa;
        this.context = context;
    }

    @NonNull
    @Override
    public Jasa_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jasa_bengkel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Jasa_Bengkel_Adapter.ViewHolder holder, int position) {
        Jasa item = jasa.get(position);

        holder.tvNama.setText(item.getBarang());
        holder.tvHarga.setText(ModulBengkel.removeE(item.getHarga()));
//        holder.tvPendapatan.setText(item.getHargabeli());

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
                               Intent intent = new Intent(context, Form_Tambah_Jasa_Bengkel_.class);

                               intent.putExtra("idbarang", item.getIdbarang());
                               intent.putExtra("barang", item.getBarang());
                               intent.putExtra("harga", item.getHarga());
                               intent.putExtra("hargabeli", item.getHargabeli());

                               context.startActivity(intent);
                                break;

                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                                AlertDialog alert;
                                alertDialog.setMessage("Apakah Anda Ingin Menghapus Data Ini?")
                                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Bengkel_ db = new Database_Bengkel_(context);

                                                String id = ""+item.getIdbarang();
                                                String sql = "DELETE FROM tblbarang WHERE idbarang="+id;

                                                if (db.exc(sql)) {
                                                    pesan("Hapus Data");
                                                    jasa.remove(position);
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

    public void pesan (String isi) {
        Toast.makeText(context, isi, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return jasa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaJasa);
            tvHarga = itemView.findViewById(R.id.tvHargaJasa);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }
}