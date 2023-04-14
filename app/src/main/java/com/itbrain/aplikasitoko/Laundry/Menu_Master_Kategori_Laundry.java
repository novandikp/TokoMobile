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

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterKategoriLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Master_Kategori_Laundry extends AppCompatActivity {
    String type, cari = "";
    DatabaseLaundry db;
    String idkat = "0";
    Spinner spKat;
    SharedPreferences getPrefs, pref2, pref3;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kategori_laundry);
        db = new DatabaseLaundry(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type = getIntent().getStringExtra("type");
        pref2 = getSharedPreferences("id", MODE_PRIVATE);
        edit = pref2.edit();
        ImageButton i = findViewById(R.id.Kembali);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EditText edtCari = (EditText) findViewById(R.id.eCari);
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
    protected void onResume() {
        super.onResume();
        getList(cari);
    }

    public void tambahdata(View view) {
        DialogMasterKategoriLaundry dialogMasterKategoriLaundry = new DialogMasterKategoriLaundry(this, true, null);
    }

    public void getList(String keyword) {

        getKategori(keyword);
    }

    private void getKategori(String keyword) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcKatt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListKategori(this, arrayList, true);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (keyword.isEmpty()) {
            q = QueryLaundry.select("tblkategori");
        } else {
            q = QueryLaundry.selectwhere("tblkategori") + QueryLaundry.sLike("kategori", keyword);
        }
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = ModulLaundry.getString(c, "idkategori") + "__" +
                        ModulLaundry.getString(c, "kategori");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
    class AdapterListKategori extends RecyclerView.Adapter<com.itbrain.aplikasitoko.Laundry.AdapterListKategori.ListKategoriViewHolder> {
        Context context;
        ArrayList<String> data;
        Boolean showOpt;
        DatabaseLaundry db;

        public AdapterListKategori(Context context, ArrayList<String> data, Boolean showOpt) {
            this.context = context;
            this.data = data;
            this.showOpt = showOpt;
        }

        @NonNull
        @Override
        public com.itbrain.aplikasitoko.Laundry.AdapterListKategori.ListKategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_master_kategori_laundry, viewGroup, false);
            db = new DatabaseLaundry(context);
            return new com.itbrain.aplikasitoko.Laundry.AdapterListKategori.ListKategoriViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.itbrain.aplikasitoko.Laundry.AdapterListKategori.ListKategoriViewHolder holder, final int i) {
            final String[] row = data.get(i).split("__");
            holder.kategori.setText(row[1]);
            holder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.opt);
                    popupMenu.inflate(R.menu.option_item_laundry);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_update:
                                    DialogMasterKategoriLaundry dialogMasterKategoriLaundry = new DialogMasterKategoriLaundry(context, false, row[0]);
                                    break;
                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String q = QueryLaundry.deleteFrom("tblkategori", "idkategori", row[0]);
                                            if (db.exc(q)) {
                                                data.remove(i);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "Delete kategori " + row[1] + " berhasil", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.setTitle("Hapus " + row[1])
                                            .setMessage("Anda yakin ingin menghapus " + row[1] + " dari data kategori");
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

        class ListKategoriViewHolder extends RecyclerView.ViewHolder {
            TextView kategori, opt;

            public ListKategoriViewHolder(@NonNull View itemView) {
                super(itemView);
                kategori = (TextView) itemView.findViewById(R.id.tvNamaKategori);
                opt = (TextView) itemView.findViewById(R.id.tvOpt);

            }
        }
    }

