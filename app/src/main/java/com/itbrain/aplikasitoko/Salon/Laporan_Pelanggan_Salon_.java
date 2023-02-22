package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.MenuExportExcel;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Pelanggan_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    View v;
    DatabaseSalon db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pelanggan_salon_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        db = new DatabaseSalon(this);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appbar = (Toolbar) findViewById(R.id.toolbar68);
//        setSupportActionBar(appbar);
//        type = getIntent().getStringExtra("type");
//        String title = "judul";

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new AdapterPelanggan(this, arrayList);
        recyclerView.setAdapter(adapter);
        getPelanggan("");

        final EditText eCari = (EditText) findViewById(R.id.editTextTextPersonName24);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString();
                getPelanggan(a);
            }
        });
        // FunctionSalon.btnBack(title, getSupportActionBar());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPelanggan(String cari){
        String hasil = "" ;
        if (TextUtils.isEmpty(cari)){
            hasil="SELECT * FROM tblpelanggan";
        }else {
            hasil="SELECT * FROM tblpelanggan WHERE (pelanggan LIKE '%"+cari+"%' OR alamatpel LIKE '%"+cari+"%' OR telppel LIKE '%"+cari+"%') ORDER BY pelanggan";
        }
        Cursor c = db.sq(hasil) ;
        if(FunctionSalon.getCount(c) > 0){
            FunctionSalon.setText(v,R.id.textView295,"Jumlah Data : "+FunctionSalon.intToStr(FunctionSalon.getCount(c))) ;
            while(c.moveToNext()){
                String nama = FunctionSalon.getString(c,"pelanggan");
                String telp = FunctionSalon.getString(c,"telppel");
                String alamat = FunctionSalon.getString(c,"alamatpel");
                String idpelanggan = FunctionSalon.getString(c,"idpelanggan");

                String campur = nama +"__"+alamat+"__"+telp+"__"+idpelanggan ;
                arrayList.add(campur);
            }
        } else {
            FunctionSalon.setText(v,R.id.textView295,"Jumlah Data : 0") ;
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcelSalon.class) ;
        i.putExtra("type","pelanggan") ;
        startActivity(i);
    }
    class AdapterPelanggan extends RecyclerView.Adapter<AdapterPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterPelanggan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pelanggan_salon, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama, alamat, notelp, opt;
            ConstraintLayout cpelanggan;

            public ViewHolder(View view) {
                super(view);

                nama = (TextView) view.findViewById(R.id.tvPelanggan);
                alamat = (TextView) view.findViewById(R.id.tvAlamat);
                notelp = (TextView) view.findViewById(R.id.tvNoTelp);
                cpelanggan = (ConstraintLayout) view.findViewById(R.id.cPelanggan);
                opt = (TextView) view.findViewById(R.id.tvOpt);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            final String[] row = data.get(i).split("__");

            viewHolder.nama.setText("Nama : "+row[0]);
            viewHolder.alamat.setText("Alamat : "+row[1]);
            viewHolder.notelp.setText("No. Telepon : "+row[2]);
            viewHolder.cpelanggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String notelp = row[2].substring(1);
                    final Dialog dialog = new Dialog(c);
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
                                c.startActivity(sendIntent);
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(c,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    tp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+"+62"+notelp));
                            c.startActivity(intent);
                        }
                    });

                    ps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "+62"+notelp, null)));
                        }
                    });
                    dialog.show();
                }
            });
            viewHolder.opt.setVisibility(View.INVISIBLE);
        }
    }

}