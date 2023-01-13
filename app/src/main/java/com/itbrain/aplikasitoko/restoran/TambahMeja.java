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

import com.itbrain.aplikasitoko.Model.Kategori;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class TambahMeja extends AppCompatActivity {

    Database_Restoran db;
    RecyclerView recyclerView;
    Meja_Restoran_Adapter adapter;

    ArrayList<Kategori> listMeja;
    private EditText eCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_meja);

        db = new Database_Restoran(this);
        listMeja = new ArrayList<>();
        adapter = new Meja_Restoran_Adapter(listMeja, this);

        recyclerView = findViewById(R.id.rcvMeja);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button button = findViewById(R.id.tambahMeja);
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TambahMeja.this, Form_Tambah_Meja_Restoran_.class);
                startActivity(intent);
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

    protected void onResume () {
        super.onResume();
        selectData();
    }

    public void selectData(){
        String sql = "SELECT * FROM tblmeja WHERE idmeja AND meja LIKE '%"+eCari.getText().toString()+"%' ";

        Cursor c = db.sq(sql);
        if (c != null) {
            listMeja.clear();
            while (c.moveToNext()) {
                listMeja.add(new Kategori(
                        c.getInt(c.getColumnIndex("idmeja")),
                        c.getString(c.getColumnIndex("meja"))
                ));
            }
        adapter.notifyDataSetChanged();
        }
    }
}

class Meja_Restoran_Adapter extends RecyclerView.Adapter<Meja_Restoran_Adapter.ViewHolder> {

    ArrayList<Kategori> meja;
    private Context context;

    public Meja_Restoran_Adapter(ArrayList<Kategori> meja, Context context) {
        this.meja = meja;
        this.context = context;
    }

    @NonNull
    @Override
    public Meja_Restoran_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori_bengkel_, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Meja_Restoran_Adapter.ViewHolder holder, int position) {
        Kategori item = meja.get(position);
        holder.tvKategori.setText(item.getKategori());

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
                                Intent intent = new Intent(context,Form_Tambah_Meja_Restoran_.class);

                                intent.putExtra("idmeja", item.getIdkategori());
                                intent.putExtra("meja", item.getKategori());

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

                                                String id = ""+item.getIdkategori();
                                                String sql = "DELETE FROM tblmeja WHERE idmeja="+id;

                                                if (db.exc(sql)) {
                                                    Toast.makeText(context, "Hapus Data", Toast.LENGTH_SHORT).show();
                                                    meja.remove(position);
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
        return meja.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKategori, tvMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }
}