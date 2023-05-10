package com.itbrain.aplikasitoko.tabungan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.dialogTabungan.DialogAnggota;
import com.itbrain.aplikasitoko.dialogTabungan.DialogPanggil;
//import com.itbrain.aplikasitoko.dialog.DialogPanggil;

import java.util.ArrayList;

public class MenuAnggotaTabungan extends AppCompatActivity {

    private EditText mEdtCari;
    private RecyclerView mRecList;

    DatabaseTabungan db;
    String type, mmKeyword = "";
    //BillingProcessor bp;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_anggota_tabungan);
//      bp=new BillingProcessor(this,Pref.licenseKey,this);
//      bp.initialize();
//      checkPurchase();
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseTabungan(this);
        type = getIntent().getStringExtra("type");
        ModulTabungan.btnBack(type.equals("anggota") ? R.string.title_master_anggota : R.string.title_master_simpanan, getSupportActionBar());

        mEdtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmKeyword = s.toString();
                getList();
            }
        });
        getList();

        ImageButton imageButton = findViewById(R.id.kembaliAnggota);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void beli() {
        ModulTabungan modul = new ModulTabungan(this);
        modul.inApp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void getList() {
        getAnggota(mmKeyword);
    }

    private void getAnggota(String keyword) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListAnggota(this, arrayList, false, false);
        mRecList.setAdapter(adapter);
        Cursor c = db.sq(QueryTabungan.selectwhere("tblanggota") + QueryTabungan.sLike("namaanggota", keyword) + " OR " + QueryTabungan.sLike("alamatanggota", keyword) + " OR " + QueryTabungan.sLike("notelpanggota", keyword));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idanggota") + "__" +
                        ModulTabungan.getString(c, "namaanggota") + "__" +
                        ModulTabungan.getString(c, "alamatanggota") + "__" +
                        ModulTabungan.getString(c, "notelpanggota");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCari);
        mRecList = (RecyclerView) findViewById(R.id.recList);
    }

    public void tambahData(View view) {
        if (ModulTabungan.cekLimit(this, "anggota") || (!ModulTabungan.cekLimit(this, "anggota") && status)) {
            DialogAnggota dialogAnggota = new DialogAnggota(this, true, null, true);
        } else {
            beli();
        }
    }

    static class AdapterListAnggota extends RecyclerView.Adapter<AdapterListAnggota.ListAnggotaVH> {
        Context context;
        ArrayList<String> data;
        Boolean fromLaporan, fromCari;

        public AdapterListAnggota(Context context, ArrayList<String> data, Boolean fromLaporan, Boolean fromCari) {
            this.context = context;
            this.data = data;
            this.fromLaporan = fromLaporan;
            this.fromCari = fromCari;
        }

        @NonNull
        @Override
        public ListAnggotaVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_master_anggota_tabungan, viewGroup, false);
            return new ListAnggotaVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ListAnggotaVH holder, int i) {
            final String[] row = data.get(i).split("__");
            holder.mTvNamaPelanggan.setText(gettext(R.string.listpelanggan_tv1) + " " + row[1]);
            holder.mTvAlamatPelanggan.setText(gettext(R.string.listpelanggan_tv2) + " " + row[2]);
            holder.mTvTelpPelanggan.setText(gettext(R.string.listpelanggan_tv3) + " +" + ModulTabungan.getCurrentCountryCode(context) + ModulTabungan.justRemoveE(row[3]));
            if (fromCari) {
                holder.mTvOpt.setVisibility(View.GONE);
                holder.mBtnCall.setVisibility(View.GONE);
                holder.mTvJumlahTransaksi.setVisibility(View.VISIBLE);

                holder.mTvJumlahTransaksi.setText(R.string.pilih);
                holder.mTvJumlahTransaksi.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccentDark)));
                holder.mListMasterPelanggan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent=new Intent(context,MenuTransaksiBuatSimpananTabungan.class);
                    intent.putExtra("id",row[0]);
                    intent.putExtra("nama",row[1]);
                    ((MenuTransaksiCariTabungan)context).setResult(1000,intent);
                    ((MenuTransaksiCariTabungan)context).finish();
                    }
                });
            } else if (fromLaporan) {
                holder.mTvOpt.setVisibility(View.GONE);
                holder.mBtnCall.setVisibility(View.GONE);
                holder.mTvJumlahTransaksi.setVisibility(View.GONE);
            }
            holder.mBtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogPanggil dialogPanggil = new DialogPanggil(context, row[0]);
                }
            });

            holder.mTvOpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.mTvOpt);
                    popupMenu.inflate(R.menu.option_menu_tabungan);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_update:
                                    DialogAnggota builder = new DialogAnggota(context, false, row[0], true);
                                    break;
                                case R.id.menu_delete:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    final DatabaseTabungan db = new DatabaseTabungan(context);
                                    builder1.create();
                                    builder1.setTitle(context.getResources().getString(R.string.confirmdelete) + " " + row[1] + "?");
                                    builder1.setMessage(context.getResources().getString(R.string.alerthapuspelanggan));
                                    final String q = "DELETE FROM tblanggota WHERE idanggota=" + row[0];
                                    builder1.setPositiveButton(context.getResources().getString(R.string.hapus), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (db.exc(q)) {
                                                Toast.makeText(context, context.getResources().getString(R.string.berhasil), Toast.LENGTH_SHORT).show();
                                                ((MenuAnggotaTabungan) context).getList();
                                            } else {
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

        private String gettext(@StringRes int string) {
            return context.getResources().getString(string);
        }

        @Override
        public int getItemCount()  {
            return data.size();
        }

        class ListAnggotaVH extends RecyclerView.ViewHolder {
            TextView mTvNamaPelanggan, mTvAlamatPelanggan, mTvTelpPelanggan, mTvOpt, mTvJumlahTransaksi;
            Button mBtnCall;
            ConstraintLayout mListMasterPelanggan;

            public ListAnggotaVH(@NonNull View itemView) {
                super(itemView);
                mTvNamaPelanggan = (TextView) itemView.findViewById(R.id.tvNamaPelanggan);
                mTvAlamatPelanggan = (TextView) itemView.findViewById(R.id.tvAlamatPelanggan);
                mTvTelpPelanggan = (TextView) itemView.findViewById(R.id.tvTelpPelanggan);
                mTvOpt = (TextView) itemView.findViewById(R.id.tvOpt);
                mTvJumlahTransaksi = (TextView) itemView.findViewById(R.id.tvJumlahTransaksi);
                mBtnCall = (Button) itemView.findViewById(R.id.btnCall);
                mListMasterPelanggan = (ConstraintLayout) itemView.findViewById(R.id.listMasterPelanggan);
            }
        }
    }
}