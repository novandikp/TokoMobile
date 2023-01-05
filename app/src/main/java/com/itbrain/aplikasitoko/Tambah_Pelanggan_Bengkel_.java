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

import com.itbrain.aplikasitoko.Model.Pelanggan;

import java.util.ArrayList;

public class Tambah_Pelanggan_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Pelanggan_Bengkel_Adapter adapter;

    ArrayList<Pelanggan> listPelanggan;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_pelanggan_bengkel_);

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

        Button tambahPelanggan = findViewById(R.id.TambahPelanggan);

        tambahPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Pelanggan_Bengkel_.this, Form_Tambah_Pelanggan_Bengkel_.class);
                startActivity(intent);
            }
        });
    }

    public void load() {
        db = new Database_Bengkel_(this);

        listPelanggan = new ArrayList<>();
        adapter = new Pelanggan_Bengkel_Adapter(listPelanggan, this);

        recyclerView = findViewById(R.id.rcvPelanggan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

protected void onResume() {
    super.onResume();
    selectData();
}

    public void selectData() {
        String sql = "SELECT * FROM tblpelanggan WHERE idpelanggan !=1 AND pelanggan LIKE '%"+cari.getText().toString()+"%'";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listPelanggan.clear();
            while (cursor.moveToNext()) {
                listPelanggan.add(new Pelanggan(
                        cursor.getInt(cursor.getColumnIndex("idpelanggan")),
                        cursor.getString(cursor.getColumnIndex("notelp")),
                        cursor.getString(cursor.getColumnIndex("pelanggan")),
                        cursor.getString(cursor.getColumnIndex("alamat"))
                ));
            }
            adapter.notifyDataSetChanged();
        }
    }
}

class Pelanggan_Bengkel_Adapter extends RecyclerView.Adapter<Pelanggan_Bengkel_Adapter.ViewHolder>{

    private ArrayList<Pelanggan> pelanggan;
    private Context context;

    public Pelanggan_Bengkel_Adapter(ArrayList<Pelanggan> pelanggan, Context context) {
        this.pelanggan = pelanggan;
        this.context = context;
    }

    @NonNull
    @Override
    public Pelanggan_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelanggan_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Pelanggan_Bengkel_Adapter.ViewHolder holder, int position) {
        Pelanggan item = pelanggan.get(position);

        holder.tvPelanggan.setText(item.getPelanggan());
        holder.tvAlamat.setText(item.getAlamat());
        holder.tvNotelp.setText(item.getNotelp());

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
                                Intent intent = new Intent(context, Form_Tambah_Pelanggan_Bengkel_.class);

                                intent.putExtra("idpelanggan", item.getIdpelanggan());
                                intent.putExtra("pelanggan", item.getPelanggan());
                                intent.putExtra("alamat", item.getAlamat());
                                intent.putExtra("notelp", item.getNotelp());

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

                                                String id = ""+item.getIdpelanggan();
                                                String sql = "DELETE FROM tblpelanggan WHERE idpelanggan="+id;

                                                if (db.exc(sql)) {
                                                    pesan("Hapus Data");
                                                    pelanggan.remove(position);
                                                    notifyDataSetChanged();
                                                } else {
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
        return pelanggan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPelanggan, tvAlamat, tvNotelp, tvMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPelanggan = itemView.findViewById(R.id.tvNamaPelanggan);
            tvAlamat = itemView.findViewById(R.id.tvAlamatPelanggan);
            tvNotelp = itemView.findViewById(R.id.tvNomorPelanggan);
            tvMenu = itemView.findViewById(R.id.tvMenu);

        }
    }
}

