package com.itbrain.aplikasitoko.tabungan;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;
//import com.komputerkit.aplikasitabunganplus.dialog.DialogAnggota;
//import com.komputerkit.aplikasitabunganplus.dialog.DialogJenisSimpanan;
//import com.komputerkit.aplikasitabunganplus.dialog.DialogPanggil;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.dialogTabungan.DialogJenisSimpanan;

import java.util.ArrayList;

public class MenuJenisTabungan extends AppCompatActivity {

    private EditText mEdtCari;
    private RecyclerView mRecList;

    DatabaseTabungan db;
    String type, mmKeyword = "";
    //    BillingProcessor bp;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_jenis_tabungan);
//        bp=new BillingProcessor(this,Pref.licenseKey,this);
//        bp.initialize();
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseTabungan(this);
        type = getIntent().getStringExtra("type");
//        ModulTabungan.btnBack(type.equals("anggota") ? R.string.title_master_anggota : R.string.title_master_simpanan, getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliJenis);
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
                mmKeyword = s.toString();
                getList();
            }
        });
        getList();
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
        getSimpanan(mmKeyword);
    }

    private void getSimpanan(String keyword) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListSimpanan(this, arrayList, false);
        mRecList.setAdapter(adapter);
        Cursor c = db.sq(QueryTabungan.selectwhere("tbljenissimpanan") +
                QueryTabungan.sLike("jenis", keyword) + " OR " +
                QueryTabungan.sLike("bunga", keyword));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idjenis") + "__" +
                        ModulTabungan.getString(c, "jenis") + "__" +
                        ModulTabungan.getString(c, "bunga");
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
        if (ModulTabungan.cekLimit(this, "jenis") || (!ModulTabungan.cekLimit(this, "anggota") && status)) {
            DialogJenisSimpanan dialogJenisSimpanan = new DialogJenisSimpanan(this, true, null, true);
        } else {
            beli();
        }
    }


    static class AdapterListSimpanan extends RecyclerView.Adapter<AdapterListSimpanan.ListSimpananVH> {
        Context context;
        ArrayList<String> data;
        Boolean fromLaporan;

        public AdapterListSimpanan(Context context, ArrayList<String> data, Boolean fromLaporan) {
            this.context = context;
            this.data = data;
            this.fromLaporan = fromLaporan;
        }

        @NonNull
        @Override
        public ListSimpananVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_master_simpanan_tabungan, viewGroup, false);
            return new ListSimpananVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ListSimpananVH holder, int i) {
            final String[] row = data.get(i).split("__");
            holder.mTvNamaSimpanan.setText(gettext(R.string.listsimpanan_tv1) + " " + row[1]);
            holder.mTvBunga.setText(gettext(R.string.listsimpanan_tv2) + " " + ModulTabungan.numFormat(row[2]) + "%");
            if (fromLaporan) {
                holder.mTvOpt.setVisibility(View.GONE);
            }

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
                                    DialogJenisSimpanan builder = new DialogJenisSimpanan(context, false, row[0], true);
                                    break;
                                case R.id.menu_delete:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    final DatabaseTabungan db = new DatabaseTabungan(context);
                                    builder1.create();
                                    builder1.setTitle(context.getResources().getString(R.string.confirmdelete) + " " + row[1] + "?");
                                    builder1.setMessage(context.getResources().getString(R.string.alerthapusjenis));
                                    final String q = "DELETE FROM tbljenissimpanan WHERE idjenis=" + row[0];
                                    builder1.setPositiveButton(context.getResources().getString(R.string.hapus), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (db.exc(q)) {
                                                Toast.makeText(context, context.getResources().getString(R.string.berhasil), Toast.LENGTH_SHORT).show();
                                                ((MenuJenisTabungan) context).getList();
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
        public int getItemCount() {
            return data.size();
        }

        class ListSimpananVH extends RecyclerView.ViewHolder {
            private TextView mTvNamaSimpanan, mTvBunga, mTvOpt;

            public ListSimpananVH(@NonNull View itemView) {
                super(itemView);
                mTvNamaSimpanan = (TextView) itemView.findViewById(R.id.tvNamaSimpanan);
                mTvBunga = (TextView) itemView.findViewById(R.id.tvBunga);
                mTvOpt = (TextView) itemView.findViewById(R.id.tvOpt);
            }
        }
    }
}