package com.itbrain.aplikasitoko.klinik;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Tambah_Janji_Klinik extends AppCompatActivity {
    DatabaseKlinik db;
    ArrayList arrayList = new ArrayList();
    View v;
    int year,month,day;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_janji_klinik);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseKlinik(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulKlinik.getDate("dd/MM/yyyy");
        ModulKlinik.setText(v, R.id.tglawal, tgl);
        ModulKlinik.setText(v, R.id.tglakhir, tgl);
        getPeriksa("");

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
                arrayList.clear();
                getPeriksa(a);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getPeriksa("");
    }
    public void getPeriksa(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recPel) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterJanji(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q= ModulKlinik.selectwhere("view_janji") +ModulKlinik.sLike("pasien",cari)+" AND " +ModulKlinik.sBetween("tgljanji",ModulKlinik.getText(v,R.id.tglawal),ModulKlinik.getText(v,R.id.tglakhir));;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulKlinik.getString(c,"idjanji")+"__"+ModulKlinik.getString(c,"tgljanji") + "__" + ModulKlinik.getString(c,"jamjanji")+ "__" + ModulKlinik.getString(c,"pasien")+ "__" + ModulKlinik.getString(c,"dokter") + "__" + ModulKlinik.getString(c,"keperluan");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

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
            return new DatePickerDialog(this, date, year, month, day);
        }else if(id==2){
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.tglawal,ModulKlinik.setDatePickerNormal(thn,bln+1,day));
            getPeriksa("");
        }
    };
    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulKlinik.setText(v,R.id.tglakhir,ModulKlinik.setDatePickerNormal(thn,bln+1,day));
            getPeriksa("");
        }
    };
    public void tglawal(View view) {
        showDialog(1);
    }

    public void tglakhir(View view) {
        showDialog(2);
    }
    public void tambah(View view){
        finish();
        Intent i = new Intent(this, MenuTambahJanjiKlinik.class);
        i.putExtra("type","tambah");
        startActivity(i);
    }
}
class AdapterJanji extends RecyclerView.Adapter<AdapterJanji.ViewHolder> {
    private ArrayList<String> data;
    Context c;


    View v;

    public AdapterJanji(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewjanjiklinik, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        String[] row = data.get(i).split("__");
        holder.tvOpt.setTag(row[0]);
        holder.nama.setText(row[1] + "  "+row[2]);
        holder.alamat.setText("Pasien : "+row[3]);
        holder.notelp.setText("Dokter : "+row[4]);
        holder.keperluan.setText("Keperluan : "+row[5]);
        holder.tvOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c, holder.tvOpt);
                popupMenu.inflate(R.menu.menu_option_klinik);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(c, MenuTambahJanjiKlinik.class);
                                intent.putExtra("type","ubah");
                                intent.putExtra("idjanji",holder.tvOpt.getTag().toString());
                                c.startActivity(intent);
                                break;
                            case R.id.menu_Hapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                                AlertDialog alert;
                                alertDialog.setMessage("Apakah anda yakin untuk menghapus data ini")
                                        .setCancelable(false)
                                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DatabaseKlinik db = new DatabaseKlinik(c);
                                                ;
                                                String id = holder.tvOpt.getTag().toString();

                                                String q = "DELETE FROM tbljanji WHERE idjanji=" + id;
                                                if (db.exc(q)) {
//                                                    int code = Modul.strToInt(holder.tvOpt.getTag().toString());
//                                                    Intent intent = new Intent(c, ToolNotification.class);
//                                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                                    AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
//                                                    alarmManager.cancel(pendingIntent);
                                                    ((Tambah_Janji_Klinik) c).getPeriksa("");
                                                } else {
                                                    ModulKlinik.showToast(c, "Gagal menghapus data");
                                                }

//

                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, notelp, tvOpt,keperluan;

        public ViewHolder(View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.tNamaPel);
            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
            notelp = (TextView) itemView.findViewById(R.id.tNo);
            tvOpt = (TextView) itemView.findViewById(R.id.tvOpt);
            keperluan = itemView.findViewById(R.id.tvKeperluan);
        }
    }

}

