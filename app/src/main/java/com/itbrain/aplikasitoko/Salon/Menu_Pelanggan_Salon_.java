package com.itbrain.aplikasitoko.Salon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Menu_Pelanggan_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listpelanggan;
    AdapterListPelanggan adapter;
    List<getterPelanggan> DaftarPelanggan;
    View v;
    ArrayList arrayList = new ArrayList() ;
    String type;
    DatabaseSalon db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pelanggan_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahPelangganSalon);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Menu_Pelanggan_Salon_.this, Form_Tambah_Pelanggan_Salon_.class);
                startActivity(intent);
            }
        });

        appbar = (Toolbar) findViewById(R.id.toolbar68);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DatabaseSalon(this);
        v = this.findViewById(android.R.id.content);
        type = getIntent().getStringExtra("type") ;

        final EditText eCari = (EditText) findViewById(R.id.editTextTextPersonName70) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString() ;
                getPelanggan(a);
            }
        });
    }

    @SuppressLint("Range")
    public void getPelanggan(String keyword){
        DaftarPelanggan = new ArrayList<>();
        listpelanggan = (RecyclerView) findViewById(R.id.lispelanggan);
        listpelanggan.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listpelanggan.setLayoutManager(layoutManager);
        adapter = new AdapterListPelanggan(this,DaftarPelanggan);
        listpelanggan.setAdapter(adapter);
        String q;

        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan";
        }else {
            q="SELECT * FROM tblpelanggan WHERE (pelanggan LIKE '%"+keyword+"%' OR alamatpel LIKE '%"+keyword+"%' OR telppel LIKE '%"+keyword+"%') ORDER BY pelanggan";
        }

        Cursor cur=db.sq(q);
        if(FunctionSalon.getCount(cur)>0){
            while(cur.moveToNext()){
                DaftarPelanggan.add(new getterPelanggan(
                        cur.getInt(cur.getColumnIndex("idpelanggan")),
                        cur.getString(cur.getColumnIndex("pelanggan")),
                        cur.getString(cur.getColumnIndex("alamatpel")),
                        cur.getString(cur.getColumnIndex("telppel"))
                ));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void tambah(View view){
        Intent intent = new Intent(this, Form_Tambah_Pelanggan_Salon_.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan("");
    }

    class AdapterListPelanggan extends RecyclerView.Adapter<AdapterListPelanggan.PelangganViewHolder>{
        private Context ctxAdapter;
        private List<getterPelanggan> data;

        public AdapterListPelanggan(Context ctx, List<getterPelanggan> viewData) {
            this.ctxAdapter = ctx;
            this.data = viewData;
        }

        @NonNull
        @Override
        public PelangganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_pelanggan_salon,viewGroup,false);
            return new PelangganViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PelangganViewHolder holder, final int i) {
            final getterPelanggan getter = data.get(i);
            holder.pelanggan.setText("Nama : "+getter.getPelanggan());
            holder.alamat.setText("Alamat : "+getter.getAlamat());
            holder.notelp.setText("No. Telepon : "+String.valueOf(getter.getNoTelp()));
            holder.cpelanggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String notelp = getter.getNoTelp().substring(1);
                    final Dialog dialog = new Dialog(ctxAdapter);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_list_salon);

                    ConstraintLayout wa = (ConstraintLayout) dialog.findViewById(R.id.clWA);
                    ConstraintLayout tp = (ConstraintLayout) dialog.findViewById(R.id.clTP);
                    ConstraintLayout ps = (ConstraintLayout) dialog.findViewById(R.id.clPS);

                    wa.setEnabled(true);
                    tp.setEnabled(true);
                    ps.setEnabled(true);

                    wa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                Intent sendIntent =new Intent("android.intent.action.MAIN");
                                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.setType("text/plain");
                                sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                                sendIntent.putExtra("jid", "62"+notelp +"@s.whatsapp.net");
                                sendIntent.setPackage("com.whatsapp");
                                ctxAdapter.startActivity(sendIntent);
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(ctxAdapter,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    tp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+"+62"+notelp));
                            ctxAdapter.startActivity(intent);
                        }
                    });

                    ps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ctxAdapter.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "+62"+notelp, null)));
                        }
                    });
                    dialog.show();
                }
            });
            holder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                    popupMenu.inflate(R.menu.menu_option_salon);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_edit:
                                    Intent intent = new Intent(ctxAdapter,Edit_Pelanggan_Salon.class);
                                    intent.putExtra("idpelanggan",getter.getIdPelanggan());
                                    intent.putExtra("pelanggan",getter.getPelanggan());
                                    intent.putExtra("alamatpel",getter.getAlamat());
                                    intent.putExtra("telppel",String.valueOf(getter.getNoTelp()));
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_Hapus:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                    builder.setMessage("Anda yakin ingin menghapus pelanggan ini?");
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseSalon db = new DatabaseSalon(ctxAdapter);
                                            if (db.deletePelanggan(getter.getIdPelanggan())){
                                                data.remove(i);
                                                notifyDataSetChanged();
                                                Toast.makeText(ctxAdapter, "Delete Pelanggan Berhasil", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
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

        class PelangganViewHolder extends RecyclerView.ViewHolder{
            TextView pelanggan, alamat, notelp, opt;
            ConstraintLayout cpelanggan;
            public PelangganViewHolder(@NonNull View itemView) {
                super(itemView);
                pelanggan=(TextView)itemView.findViewById(R.id.tvPelanggan);
                alamat=(TextView)itemView.findViewById(R.id.tvAlamat);
                notelp=(TextView)itemView.findViewById(R.id.tvNoTelp);
                cpelanggan = (ConstraintLayout) itemView.findViewById(R.id.cPelanggan);
                opt=(TextView)itemView.findViewById(R.id.tvOpt);
            }
        }
    }
    static class getterPelanggan {
        private int idPelanggan;
        private String pelanggan;
        private String alamat;
        private String notelp;

        public getterPelanggan(int idPelanggan, String pelanggan, String alamat, String notelp) {
            this.idPelanggan = idPelanggan;
            this.pelanggan = pelanggan;
            this.alamat = alamat;
            this.notelp = notelp;
        }

        public int getIdPelanggan() {
            return idPelanggan;
        }

        public String getPelanggan() {
            return pelanggan;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getNoTelp() {
            return notelp;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}