package com.itbrain.aplikasitoko.tabungan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MenuTransaksiKeuanganTabungan extends AppCompatActivity {
    View v;
    DatabaseTabungan db;
    String cari="",dateawal,datesampai,saldo;
    Integer kat=0;

    int year, month, day;
    Calendar calendar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_keuangan_tabungan);
        v=this.findViewById(android.R.id.content);
        db=new DatabaseTabungan(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        ModulTabungan.btnBack(ModulTabungan.getResString(this,R.string.title_transaksi_keuangan),getSupportActionBar());
        //tanggal
        ConstraintLayout cTanggal=(ConstraintLayout)findViewById(R.id.cTanggal);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //        button tgl
        dateawal=ModulTabungan.getDate("yyyyMMdd");
        Cursor dop=db.sq(QueryTabungan.select("tblkeuangan"));
        if (dop.getCount()>0){
            dop.moveToFirst();
            dateawal=ModulTabungan.getString(dop,"tgltransaksi");
        }

        datesampai=ModulTabungan.getDate("yyyyMMdd");
        ModulTabungan.setText(v,R.id.btnTglAwal,ModulTabungan.dateToNormal(dateawal));
        ModulTabungan.setText(v,R.id.btnTglSampai,ModulTabungan.getDate("dd/MM/yyyy"));
        Button btnTglTerima = (Button)findViewById(R.id.btnTglAwal);
        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        Button btnTglSelesai = (Button)findViewById(R.id.btnTglSampai);
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });

        refreshList();
        EditText edtCari=v.findViewById(R.id.edtCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cari=s.toString();
                getKeuangan(cari,kat,dateawal+"__"+datesampai);
            }
        });
        RadioGroup groupKeuangan=(RadioGroup)findViewById(R.id.rgKeuangan);
        groupKeuangan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rBtnSemua){
                    kat=0;
                    getKeuangan(cari,kat,dateawal+"__"+datesampai);
                }else if (checkedId==R.id.rBtnPemasukan){
                    kat=1;
                    getKeuangan(cari,kat,dateawal+"__"+datesampai);
                }else if (checkedId==R.id.rBtnPengeluaran) {
                    kat=2;
                    getKeuangan(cari,kat,dateawal+"__"+datesampai);
                }
            }
        });

        ImageButton imageButton = findViewById(R.id.kembaliDetail);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void refreshList(){
        getKeuangan(cari,kat,dateawal+"__"+datesampai);
        Cursor cSaldo=db.sq(QueryTabungan.select("tblkeuangan"));
        cSaldo.moveToLast();
        if (cSaldo.getCount()>0){
            saldo=ModulTabungan.removeE(ModulTabungan.getString(cSaldo,"saldo"));
        }else {
            saldo="0";
        }
        ModulTabungan.setText(v,R.id.tvSaldo,saldo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        ModulTabungan.setText(v,R.id.tvSaldo,saldo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void getKeuangan(String keyword,Integer kategori,String tgl){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListKeuangan(this,arrayList,false);
        recyclerView.setAdapter(adapter);
        String[] tanggal=tgl.split("__");
        String q="";
        if (keyword.isEmpty()){
            if (kategori==0){
                q=QueryTabungan.selectwhere("tblkeuangan")+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1]);
            }else if (kategori==1){
                q=QueryTabungan.selectwhere("tblkeuangan")+"masuk>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }else {
                q=QueryTabungan.selectwhere("tblkeuangan")+"keluar>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }
        }else {
            if (kategori==0){
                q=QueryTabungan.selectwhere("tblkeuangan")+QueryTabungan.sLike("faktur",keyword)+" AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }else if (kategori==1){
                q=QueryTabungan.selectwhere("tblkeuangan")+"masuk>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+QueryTabungan.sLike("faktur",keyword);
            }else {
                q=QueryTabungan.selectwhere("tblkeuangan")+"keluar>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+QueryTabungan.sLike("faktur",keyword);
            }
        }
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur= ModulTabungan.getString(c,"idtransaksi")+"__"+
                        ModulTabungan.getString(c,"tgltransaksi")+"__"+
                        ModulTabungan.getString(c,"faktur")+"__"+
                        ModulTabungan.getString(c,"keterangantransaksi")+"__"+
                        ModulTabungan.getString(c,"masuk")+"__"+
                        ModulTabungan.getString(c,"keluar")+"__"+
                        ModulTabungan.getString(c,"saldo");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahData(View view) {
        menuKeuangan();
    }
    private void menuKeuangan(){
        final View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_transaksi_keuangan_tabungan,null);
        AlertDialog dialog= new AlertDialog.Builder(this,R.style.CustomDialog)
                .setView(dialogView)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                CardView cvPemasukan=dialogView.findViewById(R.id.cvPemasukan);
                cvPemasukan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(MenuTransaksiKeuanganTabungan.this,MenuTransaksiKeuanganTambahTabungan.class).putExtra("type","pemasukan"));
                    }
                });
                CardView cvPengeluaran=dialogView.findViewById(R.id.cvPengeluaran);
                cvPengeluaran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(MenuTransaksiKeuanganTabungan.this,MenuTransaksiKeuanganKeluarTabungan.class).putExtra("type","pengeluaran"));
                    }
                });
            }
        });
        dialog.show();
    }

    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }else if (id==2){
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            dateawal=ModulTabungan.convertDate(ModulTabungan.setDatePickerNormal(thn,bln+1,day));
            ModulTabungan.setText(v, R.id.btnTglAwal, ModulTabungan.setDatePickerNormal(thn,bln+1,day)) ;
            getKeuangan(cari,kat,dateawal+"__"+datesampai);
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            datesampai=ModulTabungan.convertDate(ModulTabungan.setDatePickerNormal(thn,bln+1,day));
            ModulTabungan.setText(v, R.id.btnTglSampai, ModulTabungan.setDatePickerNormal(thn,bln+1,day)) ;
            getKeuangan(cari,kat,dateawal+"__"+datesampai);
        }
    };

}
class AdapterListKeuangan extends RecyclerView.Adapter<AdapterListKeuangan.ListKeuanganViewHolder>{
    Context context;
    ArrayList<String> data;
    DatabaseTabungan db;
    String idtransaksi;
    String tStatus;
    boolean laporan;

    public AdapterListKeuangan(Context context, ArrayList<String> data, Boolean laporan) {
        this.context = context;
        this.data = data;
        this.laporan = laporan;
    }

    @NonNull
    @Override
    public ListKeuanganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_transaksi_keuangan_tabungan,viewGroup,false);
        db=new DatabaseTabungan(context);
        Cursor c=db.sq(QueryTabungan.select("tblkeuangan"));
        c.moveToLast();
        idtransaksi=ModulTabungan.getString(c,"idtransaksi");
        return new ListKeuanganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListKeuanganViewHolder holder, final int i) {
        final String[] row=data.get(i).split("__");
        if (!row[4].equals("0")||row[5].equals("0")){
            holder.status.setText(R.string.pemasukan);
            holder.status.setTextColor(Color.parseColor("#2e8800"));
            holder.uang.setText(ModulTabungan.getResString(context,R.string.masuk)+" "+ModulTabungan.removeE(row[4]));
            tStatus=ModulTabungan.getResString(context,R.string.pemasukan);
        }else {
            holder.status.setText(R.string.pengeluaran);
            holder.status.setTextColor(Color.parseColor("#0e81d1"));
            holder.uang.setText(ModulTabungan.getResString(context,R.string.masuk)+" "+ModulTabungan.removeE(row[5]));
            tStatus=ModulTabungan.getResString(context,R.string.pengeluaran);
        }
        holder.tanggal.setText("- "+ModulTabungan.dateToNormal(row[1]));
        holder.faktur.setText(ModulTabungan.getResString(context,R.string.cetak_line2)+" "+row[2]);
        holder.saldo.setText(ModulTabungan.getResString(context,R.string.saldo)+" "+ModulTabungan.removeE(row[6]));
        if (idtransaksi.equals(row[0])  &&!laporan){
            if (!row[2].contains("TABUNGAN-") && !row[3].contains("Simpanan :")) {
                holder.hapus.setVisibility(View.VISIBLE);
            }
        }
        if (row[3].isEmpty()){
            holder.keterangan.setText(ModulTabungan.getResString(context,R.string.keterangan)+" : "+ModulTabungan.getResString(context,R.string.tanpaketerangan));
        }else {
            holder.keterangan.setText(ModulTabungan.getResString(context,R.string.keterangan)+" : "+row[3]);
        }
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(context)
                        .setTitle(ModulTabungan.getResString(context,R.string.hapus)+" "+tStatus+" "+row[2])
                        .setMessage(ModulTabungan.getResString(context,R.string.confirmdelete)+" "+tStatus+" "+row[2])
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q="DELETE FROM tblkeuangan WHERE idtransaksi="+row[0];
                                if (db.exc(q)){
                                    Toast.makeText(context, R.string.berhasil, Toast.LENGTH_SHORT).show();
                                    ((MenuTransaksiKeuanganTabungan)context).refreshList();
                                }
                            }
                        })
                        .setNegativeButton(R.string.batal, null)
                        .create();
                dialog.show();
            }
        });

        holder.keterangan.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListKeuanganViewHolder extends RecyclerView.ViewHolder{
        TextView status,tanggal,faktur,keterangan,uang,saldo;
        ImageButton hapus;
        public ListKeuanganViewHolder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.tvStatus);
            tanggal=itemView.findViewById(R.id.tvTanggal);
            faktur=itemView.findViewById(R.id.tvFaktur);
            keterangan=itemView.findViewById(R.id.tvKeterangan);
            uang=itemView.findViewById(R.id.tvUang);
            saldo=itemView.findViewById(R.id.tvSaldo);
            hapus=itemView.findViewById(R.id.ibtnDelete);
        }
    }
}
