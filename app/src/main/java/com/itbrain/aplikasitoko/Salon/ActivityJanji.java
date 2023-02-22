package com.itbrain.aplikasitoko.Salon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityJanji extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listjanji;
    AdapterListJanji adapter;
    List<getterJanji> DaftarJanji;
    ArrayList arrayList = new ArrayList() ;
    DatabaseSalon db;
    View v;

    int year,month, day ;
    String dari, ke;
    Calendar calendar;

    String deviceid;
    SharedPreferences getPrefs ;
    ConfigSalon config;

    static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_janji);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        FunctionSalon.btnBack("Booking",getSupportActionBar());

        db = new DatabaseSalon(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        config = new ConfigSalon(getSharedPreferences("config",this.MODE_PRIVATE));

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //deviceid = FunctionSalon.getDecrypt(getPrefs.getString("deviceid","")) ;

        final EditText eCari = (EditText) findViewById(R.id.eCari) ;
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
                getJanji(a);
            }
        });

        setText();
        getJanji("");
    }

    private boolean limit(String item) {
        int batas = FunctionSalon.strToInt(config.getCustom(item, "1"));
        if (batas>500){
            return false;
        } else {
            return true;
        }
    }

    public void setText(){
        dari = FunctionSalon.setDatePicker(year,month+1,day) ;
        ke = FunctionSalon.setDatePicker(year,month+1,day) ;
        String now = FunctionSalon.setDatePickerNormal(year,month+1,day) ;
        FunctionSalon.setText(v,R.id.eKe,now) ;
        FunctionSalon.setText(v,R.id.eDari,now) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getJanji(String keyword){
        DaftarJanji = new ArrayList<>();
        listjanji = (RecyclerView) findViewById(R.id.listjanji);
        listjanji.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listjanji.setLayoutManager(layoutManager);
        adapter = new AdapterListJanji(this, DaftarJanji);
        listjanji.setAdapter(adapter);
        String q;

        if (TextUtils.isEmpty(keyword)){
            q = QuerySalon.selectwhere("qjanji") + QuerySalon.sWhere("status", "0") + " AND " + QuerySalon.sBetween("tgljanji", dari, ke) + QuerySalon.sOrderDESC("tgljanji");
        } else {
            q = QuerySalon.selectwhere("qjanji") + QuerySalon.sWhere("status", "0") + " AND (" + QuerySalon.sLike("pelanggan",keyword) + " OR " + QuerySalon.sLike("telppel",keyword) + ") AND " + QuerySalon.sBetween("tgljanji",dari,ke);
        }

        Cursor cur = db.sq(q);
        if(FunctionSalon.getCount(cur)>0){
            while (cur.moveToNext()) {
                DaftarJanji.add(new getterJanji(
                        FunctionSalon.getInt(cur,"idjanji"),
                        FunctionSalon.getInt(cur,"idpelanggan"),
                        FunctionSalon.getString(cur,"pelanggan"),
                        FunctionSalon.getString(cur,"tgljanji"),
                        FunctionSalon.getString(cur,"jamjanji"),
                        FunctionSalon.getString(cur,"status"),
                        FunctionSalon.getString(cur,"telppel")
                ));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void filtertgl(){
        getJanji(FunctionSalon.getText(v,R.id.eCari));
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.eDari, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            dari = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.eKe, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            ke = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker

    public void tambah(View view){
        if (status){
            Intent intent = new Intent(this, Form_Booking_Salon.class);
            startActivity(intent);
        } else {
            if (limit("janji")){
                Intent intent = new Intent(this, Form_Booking_Salon.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Anda sudah melebihi batas trial, harap beli yang full version", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Aplikasi_Salon_Menu_Utillitas.class);
            startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJanji("");
    }

    private class AdapterListJanji extends RecyclerView.Adapter<AdapterListJanji.JanjiViewHolder>{
        private Context ctxAdapter;
        private List<getterJanji> data;

        public AdapterListJanji(Context ctx, List<getterJanji> viewData){
            this.ctxAdapter = ctx;
            this.data = viewData;
        }

        @NonNull
        @Override
        public JanjiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_janji_salon,viewGroup,false);
            return new JanjiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final JanjiViewHolder viewHolder, final int i) {
            final getterJanji getter = data.get(i);

            String stat;
            if (getter.getStatus().equals("0")){
                stat = "Belum Datang";
            } else {
                stat = "Sudah Datang";
            }

            viewHolder.pel.setText("Nama : "+getter.getPel());
            viewHolder.tgljanji.setText("Tanggal Booking : "+FunctionSalon.dateToNormal(getter.getTgljanji()));
            viewHolder.jamjanji.setText("Jam Booking : "+FunctionSalon.timeToNormal(getter.getJamjanji()));
            viewHolder.status.setText("No. Telepon : "+getter.getTelp()+"\n"+"Status : "+stat);
            viewHolder.cJanji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String notelp = String.valueOf(getter.getTelp()).substring(1);
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
            viewHolder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(ctxAdapter,viewHolder.opt);
                    popupMenu.inflate(R.menu.option_item_salon);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                    builder.setMessage("Apakah Anda Yakin Ingin Menghapus Booking Ini?");
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseSalon db = new DatabaseSalon(ctxAdapter);
                                            if (db.deleteJanji(getter.getIdjanji())){
                                                data.remove(i);
                                                notifyDataSetChanged();
                                                Toast.makeText(ctxAdapter, "Delete Booking Berhasil", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ctxAdapter, "Gagal Menghapus Booking", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    builder.show();
                                    break;

                                case R.id.menu_status:
                                    AlertDialog.Builder build = new AlertDialog.Builder(ctxAdapter);
                                    build.setMessage("Apakah Anda Yakin Ingin Mengubah Status?");
                                    build.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String stat = "1";
                                            db.exc("UPDATE tbljanji SET status="+stat+" WHERE idjanji="+getter.getIdjanji());
                                            Toast.makeText(ActivityJanji.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                            getJanji("");
                                        }
                                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    build.show();
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

        class JanjiViewHolder extends RecyclerView.ViewHolder{
            TextView pel, tgljanji, jamjanji, status, opt;
            ConstraintLayout cJanji;
            public JanjiViewHolder(@NonNull View itemView) {
                super(itemView);
                pel = (TextView) itemView.findViewById(R.id.tePelanggan);
                tgljanji = (TextView) itemView.findViewById(R.id.teTanggal);
                jamjanji = (TextView) itemView.findViewById(R.id.teJam);
                status = (TextView) itemView.findViewById(R.id.teStatus);
                cJanji = (ConstraintLayout) itemView.findViewById(R.id.cJanji);
                opt=(TextView) itemView.findViewById(R.id.tvOpt);
            }
        }
    }

    static class getterJanji{
        private int idjanji;
        private int idpelanggan;
        private String pel;
        private String tgljanji;
        private String jamjanji;
        private String status;
        private String telp;

        public getterJanji(int idjanji, int idpelanggan, String pel, String tgljanji, String jamjanji, String status, String telp){
            this.idjanji = idjanji;
            this.idpelanggan = idpelanggan;
            this.pel = pel;
            this.tgljanji = tgljanji;
            this.jamjanji = jamjanji;
            this.status = status;
            this.telp = telp;
        }

        public int getIdjanji(){
            return idjanji;
        }

        public int getIdpelanggan(){
            return idpelanggan;
        }

        public String getPel(){
            return pel;
        }

        public String getTgljanji(){
            return tgljanji;
        }

        public String getJamjanji(){
            return jamjanji;
        }

        public String getStatus(){
            return status;
        }

        public String getTelp(){
            return telp;
        }
    }
}

