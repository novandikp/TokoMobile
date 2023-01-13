package com.itbrain.aplikasitoko.restoran;

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
import com.itbrain.aplikasitoko.R;


import java.util.ArrayList;

public class Pelanggan_Restoran_ extends AppCompatActivity {

    Database_Restoran db;
    RecyclerView recyclerView;
    Pelanggan_Restoran_Adapter adapter;

    ArrayList<Pelanggan>  listPelanggan;
    private EditText eCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pelanggan_restoran_);

       db = new Database_Restoran(this);
       listPelanggan = new ArrayList<>();
       adapter = new Pelanggan_Restoran_Adapter(listPelanggan,this);

       recyclerView = findViewById(R.id.rcvPelanggan);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.setAdapter(adapter);

        Button button = findViewById(R.id.tambahPelangganRestoran);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pelanggan_Restoran_.this, Form_Tambah_Pelanggan_Restoran.class);
//                Intent intent = new Intent(Pelanggan_Restoran_.this, DialogPelanggan.class);
                startActivity(intent);
            }
        });

        ImageButton imageButton = findViewById(R.id.Kembali);

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
                selectData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT * FROM tblpelanggan WHERE idpelanggan AND namapelanggan LIKE '%"+eCari.getText().toString()+"%' ";

        Cursor c = db.sq(sql);
        if (c != null) {
            listPelanggan.clear();
            while (c.moveToNext()) {
                listPelanggan.add(new Pelanggan (
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("notelppelanggan")),
                        c.getString(c.getColumnIndex("namapelanggan")),
                        c.getString(c.getColumnIndex("alamatpelanggan"))
                ));
            }
            adapter.notifyDataSetChanged();
        }
    }
}

class Pelanggan_Restoran_Adapter extends RecyclerView.Adapter<Pelanggan_Restoran_Adapter.ViewHolder> {

    private ArrayList<Pelanggan> pelanggan;
    private Context context;

    public Pelanggan_Restoran_Adapter(ArrayList<Pelanggan> pelanggan, Context context) {
        this.pelanggan = pelanggan;
        this.context = context;
    }

    @NonNull
    @Override
    public Pelanggan_Restoran_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelanggan_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Pelanggan_Restoran_Adapter.ViewHolder holder, int position) {
        Pelanggan item = pelanggan.get(position);
        holder.tvPelanggan.setText(item.getPelanggan());
        holder.tvAlamat.setText(item.getAlamat());
        holder.tvNotelp.setText(item.getNotelp());
        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "ini menu", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context, holder.tvMenu);
                popupMenu.inflate(R.menu.menu_option);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(context,Form_Tambah_Pelanggan_Restoran.class);

                                intent.putExtra("idpelanggan", item.getIdpelanggan());
                                intent.putExtra("namapelanggan", item.getPelanggan());
                                intent.putExtra("alamatpelanggan", item.getAlamat());
                                intent.putExtra("notelpelanggan", item.getNotelp());

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
                                                Database_Restoran db = new Database_Restoran(context);

                                                String id = ""+item.getIdpelanggan();
                                                String sql = "DELETE FROM tblpelanggan WHERE idpelanggan="+id;

                                                if (db.exc(sql)) {
                                                    Toast.makeText(context, "Hapus Data", Toast.LENGTH_SHORT).show();
                                                    pelanggan.remove(position);
                                                    notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(context, "Data Tidak Bisa Dihapus", Toast.LENGTH_SHORT).show();
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