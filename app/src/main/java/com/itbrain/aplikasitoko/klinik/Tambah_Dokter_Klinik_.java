package com.itbrain.aplikasitoko.klinik;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Tambah_Dokter_Klinik_ extends AppCompatActivity {
    String type;
    DatabaseKlinik db;
    ArrayList arrayList = new ArrayList();
    ModulKlinik config;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tambah_dokter_klinik);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type = getIntent().getStringExtra("type");

        db = new DatabaseKlinik(this);

        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();

                getDokter(a);
            }


        });
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private boolean limit(String item) {
        int batas = ModulKlinik.strToInt(config.getCustom(item, "1"));
        if (batas > 5) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDokter("");
    }

    public void getDokter(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterDokter(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulKlinik.selectwhere("tbldokter") + " iddokter!=1 AND " + ModulKlinik.sLike("dokter", cari) + " ORDER BY dokter ASC";;
        Log.d("SQL", "getJasa: " + q);
        Cursor c = db.sq(q);
        if (c != null) {
            while (c.moveToNext()) {
                String campur = ModulKlinik.getString(c, "iddokter") + "__" + ModulKlinik.getString(c, "dokter") + "__" + ModulKlinik.getString(c, "alamatdokter") + "__" + ModulKlinik.getString(c, "nodokter");
                arrayList.add(campur);
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void tambah(View view) {
        if (MainActivity.status) {
            startActivity(new Intent(Tambah_Dokter_Klinik_.this, MenuTambahDokterKLinik.class));
        } else {
            startActivity(new Intent(this, MenuTambahDokterKLinik.class));
        }

        finish();
    }

    private void tartActivity(Intent intent) {
        throw new RuntimeException("Stub!");
    }
}

class AdapterDokter extends RecyclerView.Adapter<AdapterDokter.ViewHolder>{
    private ArrayList<String> data;
    Context c;



    View v ;

    public AdapterDokter(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pelanggan, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);

        holder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.tvOpt);
                popupMenu.inflate(R.menu.menu_option_klinik);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Intent intent = new Intent(c,MenuUbahDokterKlinik.class);
                                intent.putExtra("id",holder.tvOpt.getTag().toString());
                                c.startActivity(intent);
                                break;
                            case  R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseKlinik db = new DatabaseKlinik(c);;
                                                String id = holder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tbldokter WHERE iddokter="+id ;
                                                if (db.exc(q)){
                                                    ((Tambah_Dokter_Klinik_)c).getDokter("");
                                                }else{
                                                    ModulKlinik.showToast(c,"Gagal menghapus data");
                                                }

//

                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                alert=alertDialog.create();

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
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat,notelp,tvOpt;
        public ViewHolder(View itemView) {
            super(itemView);

            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp=(TextView) itemView.findViewById(R.id.tNo);
            tvOpt=(TextView) itemView.findViewById(R.id.tvOpt);

        }
    }
}
