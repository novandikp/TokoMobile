package com.itbrain.aplikasitoko.tabungan;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.dialogTabungan.DialogAnggota;

import java.util.ArrayList;

public class MenuTransaksiCariTabungan extends AppCompatActivity {
    DatabaseTabungan db;
    String type,mmKeyword="";
    private EditText mEdtCari;
    private RecyclerView mRecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_cari_tabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type=getIntent().getStringExtra("type");
        ModulTabungan.btnBack(type.equals("anggota")?R.string.title_transaksi_cari_anggota:R.string.title_transaksi_cari_simpanan, getSupportActionBar());
        db = new DatabaseTabungan(this);
        initView();

        ImageButton imageButton = findViewById(R.id.kembaliCari);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEdtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmKeyword=s.toString();
                getList();
            }
        });

        getList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getList(){
        if (type.equals("anggota")){
            getAnggota(mmKeyword);
        }else if (type.equals("simpanan")){
            getSimpanan(mmKeyword);
        }
    }
    private void getAnggota(String keyword){
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new MenuAnggotaTabungan.AdapterListAnggota(this,arrayList,false,true);
        mRecList.setAdapter(adapter);
        Cursor c=db.sq(QueryTabungan.selectwhere("tblanggota")+QueryTabungan.sLike("namaanggota",keyword)+" OR "+QueryTabungan.sLike("alamatanggota",keyword)+" OR "+QueryTabungan.sLike("notelpanggota",keyword));
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur=ModulTabungan.getString(c,"idanggota")+"__"+
                        ModulTabungan.getString(c,"namaanggota")+"__"+
                        ModulTabungan.getString(c,"alamatanggota")+"__"+
                        ModulTabungan.getString(c,"notelpanggota");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void getSimpanan(String keyword){
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterTransaksiSimpanan(this,arrayList,true,getIntent().getBooleanExtra("fromLaporan",false));
        mRecList.setAdapter(adapter);
        Cursor c=db.sq(QueryTabungan.selectwhere("qsimpanan")+QueryTabungan.sLike("namaanggota",keyword)+" OR " +
                QueryTabungan.sLike("jenis",keyword)+" OR "+QueryTabungan.sLike("simpanan",keyword));
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur=ModulTabungan.getString(c,"idsimpanan")+"__"+
                        ModulTabungan.getString(c,"idanggota")+"__"+
                        ModulTabungan.getString(c,"namaanggota")+"__"+
                        ModulTabungan.getString(c,"idjenis")+"__"+
                        ModulTabungan.getString(c,"jenis")+"__"+
                        ModulTabungan.getString(c,"bunga")+"__"+
                        ModulTabungan.getString(c,"simpanan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahdata(View view) {
        if (type.equals("anggota")){
            DialogAnggota dialogAnggota=new DialogAnggota(this,true,null,false);
        }else if (type.equals("simpanan")){
            startActivity(new Intent(this,MenuTransaksiBuatSimpananTabungan.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCari);
        mRecList = (RecyclerView) findViewById(R.id.recList);
    }
}
class AdapterTransaksiSimpanan extends RecyclerView.Adapter<AdapterTransaksiSimpanan.TransaksiSimpananVH>{
    Context context;
    ArrayList<String> data;
    DatabaseTabungan db;
    Boolean fromCari,fromLaporan;

    public AdapterTransaksiSimpanan(Context context, ArrayList<String> data,Boolean fromCari,Boolean fromLaporan) {
        this.context = context;
        this.data = data;
        this.fromCari = fromCari;
        this.fromLaporan = fromLaporan;
    }

    @NonNull
    @Override
    public TransaksiSimpananVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_transaksi_simpanan_tabungan,viewGroup,false);
        db=new DatabaseTabungan(context);
        return new TransaksiSimpananVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransaksiSimpananVH holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.mTvNama.setText(gettext(R.string.listtransaksisimpanan_tv1)+" "+row[6]);
        holder.mTvAnggota.setText(gettext(R.string.listtransaksisimpanan_tv3)+" "+row[2]);
        holder.mTvJenis.setText(gettext(R.string.listtransaksisimpanan_tv2)+" "+row[4]);
        holder.mTvBunga.setText(gettext(R.string.menutransaksiproses_tv3)+" "+ModulTabungan.numFormat(row[5])+"%");

        Cursor c=db.sq(QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("idsimpanan",row[0]));
        c.moveToLast();
        String saldo=c.getCount()>0 ? ModulTabungan.getString(c,"saldo") : "0";
        holder.mTvSaldo.setText(gettext(R.string.listtransaksisimpanan_tv4)+" "+ModulTabungan.removeE(saldo));

        if (fromCari && fromLaporan){
            holder.mCLayoutSimpanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MenuLaporanKeuanganTabungan.class);
                    intent.putExtra("idsimpanan",row[0]);
                    intent.putExtra("namasimpanan",row[6]);
                    ((MenuTransaksiCariTabungan)context).setResult(3000,intent);
                    ((MenuTransaksiCariTabungan)context).finish();
                }
            });
        } else if (fromCari){
            holder.mCLayoutSimpanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MenuTransaksiProsesSimpanTabungan.class);
                    intent.putExtra("idsimpanan",row[0]);
                    intent.putExtra("namasimpanan",row[6]);
                    ((MenuTransaksiCariTabungan)context).setResult(2000,intent);
                    ((MenuTransaksiCariTabungan)context).finish();
                }
            });
        }else if (!fromLaporan&&!fromCari){
            holder.mTvOpt.setVisibility(View.VISIBLE);
            holder.mTvOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context,holder.mTvOpt);
                    popupMenu.inflate(R.menu.option_menu_tabungan);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menu_update:
                                    context.startActivity(new Intent(context,MenuTransaksiBuatSimpananTabungan.class).putExtra("idsimpanan",row[0]).putExtra("update",true));
                                    break;
                                case R.id.menu_delete:
                                    AlertDialog.Builder builder1=new AlertDialog.Builder(context);
                                    final DatabaseTabungan db=new DatabaseTabungan(context);
                                    builder1.create();
                                    builder1.setTitle(context.getResources().getString(R.string.confirmdelete)+" "+row[6]+"?");
                                    builder1.setMessage(context.getResources().getString(R.string.alerthapus));
                                    final String q="DELETE FROM tblsimpanan WHERE idsimpanan="+row[0];
                                    builder1.setPositiveButton(context.getResources().getString(R.string.hapus), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (db.exc(q)){
                                                Toast.makeText(context, context.getResources().getString(R.string.berhasil), Toast.LENGTH_SHORT).show();
                                                ((MenuTransaksiSimpananTabungan)context).onResume();
                                            }else {
                                                Toast.makeText(context, context.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder1.setNegativeButton(context.getResources().getString(R.string.batal), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder1.show();
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
    }
    private String gettext(int string){
        return context.getResources().getString(string);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransaksiSimpananVH extends RecyclerView.ViewHolder{
        private TextView mTvNama,mTvJenis,mTvAnggota,mTvSaldo,mTvBunga;
        private ConstraintLayout mCLayoutSimpanan;
        private TextView mTvOpt;

        public TransaksiSimpananVH(@NonNull View itemView) {
            super(itemView);
            mCLayoutSimpanan = (ConstraintLayout) itemView.findViewById(R.id.cLayoutSimpanan);
            mTvNama = (TextView) itemView.findViewById(R.id.tvNama);
            mTvJenis = (TextView) itemView.findViewById(R.id.tvJenis);
            mTvAnggota = (TextView) itemView.findViewById(R.id.tvAnggota);
            mTvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);
            mTvBunga = (TextView) itemView.findViewById(R.id.tvBunga);
            mTvOpt = (TextView) itemView.findViewById(R.id.tvOpt);

        }
    }
}
