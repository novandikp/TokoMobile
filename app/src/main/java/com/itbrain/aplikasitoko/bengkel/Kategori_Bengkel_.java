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

import com.itbrain.aplikasitoko.Model.Kategori;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Kategori_Bengkel_ extends AppCompatActivity {

    Database_Bengkel_ db;
    RecyclerView recyclerView;
    Kategori_Bengkel_Adapter adapter;

    ArrayList<Kategori> listKategori;
    String idkategori, etKategori;
    private EditText cari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategori_bengkel_);

        load();

        Button button = findViewById(R.id.TambahKategoriBengkel);
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
                Intent intent = new Intent(Kategori_Bengkel_.this, Tambah_Kategori_Bengkel_.class);
                startActivity(intent);
            }




        });
    }

    public void load() {
        db = new Database_Bengkel_(this);

        listKategori = new ArrayList<>();
        adapter = new Kategori_Bengkel_Adapter(listKategori, this);

        recyclerView = findViewById(R.id.rcKategori);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {
        String sql = "SELECT * FROM tblkategori WHERE idkategori !=1 AND kategori LIKE '%"+cari.getText().toString()+"%' ";

        Cursor cursor = db.sq(sql);
        if(cursor !=null) {
            listKategori.clear();
            while (cursor.moveToNext()) {
                listKategori.add(new Kategori(
                        cursor.getInt(cursor.getColumnIndex("idkategori")),
                        cursor.getString(cursor.getColumnIndex("kategori"))
                ));
            }
            adapter.notifyDataSetChanged();
        }
    }

}

class Kategori_Bengkel_Adapter extends RecyclerView.Adapter<Kategori_Bengkel_Adapter.ViewHolder> {

    private ArrayList<Kategori> kategori;
    private Context context;

    public Kategori_Bengkel_Adapter(ArrayList<Kategori> kategori, Context context) {
        this.kategori = kategori;
        this.context = context;
    }


    @NonNull
    @Override
    public Kategori_Bengkel_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori_bengkel_, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Kategori_Bengkel_Adapter.ViewHolder holder, int position) {
        Kategori item = kategori.get(position);
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
                                Intent intent = new Intent(context, Tambah_Kategori_Bengkel_.class);

                                intent.putExtra("idkategori",item.getIdkategori());
                                intent.putExtra("kategori", item.getKategori());
                                context.startActivity(intent);
                                break;
                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

                                AlertDialog alert;
                                alertdialog.setMessage("Apakah Anda Ingin Menghapus Data Ini?")
                                        .setIcon(R.drawable.ic_baseline_delete_forever_24)
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Database_Bengkel_ db = new Database_Bengkel_(context);

                                        String id = ""+item.getIdkategori();
                                        String  sql = "DELETE FROM tblkategori WHERE idkategori="+id;

                                        if (db.exc(sql)) {
                                            Toast.makeText(context, "Hapus Data", Toast.LENGTH_SHORT).show();
                                            kategori.remove(position);
                                            notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(context, "Kategori Tidak Bisa Dihapus, Karena Telah Dipakai!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });

                                alert = alertdialog.create();

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
        return kategori.size();
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