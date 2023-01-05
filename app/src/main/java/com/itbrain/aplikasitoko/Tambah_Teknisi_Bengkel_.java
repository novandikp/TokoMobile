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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.Barang;
import com.itbrain.aplikasitoko.Model.Teknisi;

import java.util.ArrayList;

public class Tambah_Teknisi_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Teknisi_Bengkel_Adapter adapter;

    ArrayList<Teknisi> listTeknisi;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_teknisi_bengkel_);

        load();

        Button button = findViewById(R.id.TambahTeknisi);
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
                Intent intent = new Intent(Tambah_Teknisi_Bengkel_.this, FormTambahTeknisi.class);
                startActivity(intent);
            }
        });

    }

    public  void load() {
        db = new Database_Bengkel_(this);

        listTeknisi = new ArrayList<>();
        adapter = new Teknisi_Bengkel_Adapter(listTeknisi, this);

        recyclerView = findViewById(R.id.rcvTeknisi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT * FROM tblteknisi WHERE idteknisi !=1 AND teknisi LIKE '%"+cari.getText().toString()+"%'";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            listTeknisi.clear();
            while (cursor.moveToNext()) {
                listTeknisi.add(new Teknisi(
                        cursor.getInt(cursor.getColumnIndex("idteknisi")),
                        cursor.getString(cursor.getColumnIndex("teknisi")),
                        cursor.getString(cursor.getColumnIndex("alamatteknisi")),
                        cursor.getString(cursor.getColumnIndex("noteknisi"))
                ));
            }
            adapter.notifyDataSetChanged();
        }
    }
}

class Teknisi_Bengkel_Adapter extends RecyclerView.Adapter<Teknisi_Bengkel_Adapter.ViewHolder> {
    private ArrayList<Teknisi> teknisi;
    private Context context;

    public Teknisi_Bengkel_Adapter(ArrayList<Teknisi> teknisi, Context context) {
        this.teknisi = teknisi;
        this.context = context;
    }

    @NonNull
    @Override
    public Teknisi_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bengkel_teknisi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Teknisi_Bengkel_Adapter.ViewHolder holder, int position) {
        Teknisi item = teknisi.get(position);

        holder.tvTeknisi.setText(item.getTeknisi());
        holder.tvAlamat.setText(item.getAlamatteknisi());
        holder.tvNomor.setText(item.getNoteknisi());

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
                                Intent intent = new Intent(context, FormTambahTeknisi.class);

                                intent.putExtra("idteknisi", item.getIdteknisi());
                                intent.putExtra("teknisi", item.getTeknisi());
                                intent.putExtra("alamatteknisi", item.getAlamatteknisi());
                                intent.putExtra("noteknisi", item.getNoteknisi());

                                context.startActivity(intent);
                                break;

                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                                AlertDialog alert;
                                alertDialog.setMessage("Apakah Anda Ingin Menghapus Pesan Ini?")
                                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Database_Bengkel_ db = new Database_Bengkel_(context);

                                                String id = ""+item.getIdteknisi();
                                                String sql = "DELETE FROM tblteknisi WHERE idteknisi="+id;

                                                if (db.exc(sql)) {
                                                    pesan("Hapus Data");
                                                    teknisi.remove(position);
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
        return teknisi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeknisi, tvAlamat, tvNomor, tvMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTeknisi = itemView.findViewById(R.id.tvNamaTeknisi);
            tvAlamat = itemView.findViewById(R.id.tvAlamatTeknisi);
            tvNomor = itemView.findViewById(R.id.tvNoTeknisi);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }
}