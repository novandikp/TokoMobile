package com.itbrain.aplikasitoko.Laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterPelangganLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuPelangganLaundry extends AppCompatActivity {
    String type, cari = "";
    DatabaseLaundry db;
    String idkat = "0";
    Spinner spKat;
    SharedPreferences getPrefs, pref2, pref3;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pelanggan_laundry);
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
    protected void onResume(){
        super.onResume();
        getList(cari);
    }

    public void tambahdata(View view) {
                DialogMasterPelangganLaundry dialogMasterPelangganLaundry =new DialogMasterPelangganLaundry(this,true,null,true);
    }

    public void getList(String keyword){
            getPelanggan(keyword);
    }
    private void getPelanggan(String keyword){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recFelanggan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListPelanggan(this,arrayList,true);
        recyclerView.setAdapter(adapter);
        String q="";
        if (keyword.isEmpty()){
            q= QueryLaundry.selectwhere("tblpelanggan")+"idpelanggan>0";
        }else {
            q= QueryLaundry.selectwhere("tblpelanggan")+ QueryLaundry.sLike("pelanggan",keyword)+" AND idpelanggan>0";
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idpelanggan")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"alamat")+"__"+
                        ModulLaundry.getString(c,"notelp");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
class AdapterListPelanggan extends RecyclerView.Adapter<AdapterListPelanggan.ListPelangganViewHolder> {
    Context context;
    ArrayList<String> data;
    Boolean showOpt;
    DatabaseLaundry db;

    public AdapterListPelanggan(Context context, ArrayList<String> data, Boolean showOpt) {
        this.context = context;
        this.data = data;
        this.showOpt = showOpt;
    }

    @NonNull
    @Override
    public ListPelangganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_master_pelanggan_laundry, viewGroup, false);
        db = new DatabaseLaundry(context);
        return new ListPelangganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListPelangganViewHolder holder, final int i) {
        final String[] row = data.get(i).split("__");
        holder.nama.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.telp.setText(ModulLaundry.getCurrentCountryCode(context) + "-" + ModulLaundry.justRemoveE(row[3]));
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.opt);
                popupMenu.inflate(R.menu.option_item_laundry);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_update:
                                DialogMasterPelangganLaundry dialogMasterPelangganLaundry = new DialogMasterPelangganLaundry(context, false, row[0], true);
                                break;
                            case R.id.menu_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String q = QueryLaundry.deleteFrom("tblpelanggan", "idpelanggan", row[0]);
                                        if (db.exc(q)) {
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Delete pelanggan " + row[1] + " berhasil", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus " + row[1]);
                                builder.setMessage("Anda yakin ingin menghapus " + row[1] + " dari data pelanggan");
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
        if (!showOpt) {
            holder.opt.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListPelangganViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, telp, opt;

        public ListPelangganViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.tvNamaPelanggan);
            alamat = (TextView) itemView.findViewById(R.id.tvAlamatPelanggan);
            telp = (TextView) itemView.findViewById(R.id.tvTelpPelanggan);
            opt = (TextView) itemView.findViewById(R.id.tvOpt);
        }
    }
}
