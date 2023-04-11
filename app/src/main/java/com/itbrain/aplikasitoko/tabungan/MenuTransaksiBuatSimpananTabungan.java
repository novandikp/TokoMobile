package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.util.List;

public class MenuTransaksiBuatSimpananTabungan extends AppCompatActivity {
    DatabaseTabungan db;
    private TextInputEditText mEdtPelanggan;
    private TextInputEditText mEdtSimpanan;
    private Spinner mSpJenisSimpanan;
    private Button mBtnSimpan;
    private ImageButton mIbtnCari;

    List<String> getJenis;
    List<String> getIdJenis;

    String idanggota,anggota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_buat_simpanan_tabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ModulTabungan.btnBack(R.string.menutransaksi_box1,getSupportActionBar());
        db = new DatabaseTabungan(this);
        initView();
        idanggota="0";
        anggota=getResources().getString(R.string.belumpilih);

        ImageButton imageButton = findViewById(R.id.kembaliBuatSimpanan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getJenis=db.getJenis();
        getIdJenis=db.getIdJenis();
        if (getJenis.get(0).equals(ModulTabungan.getResString(this,R.string.menutransaksisimpanan_sp1))){
            getJenis.remove(0);
            getIdJenis.remove(0);
        }

        // spinner
        ArrayAdapter<String> dataAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getJenis);
        dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpJenisSimpanan.setAdapter(dataAdapterKategori);

        //form 1
        mIbtnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MenuTransaksiBuatSimpananTabungan.this, MenuTransaksiCariTabungan.class).putExtra("type","anggota"),1000);
            }
        });

        //form 2
        mEdtSimpanan.requestFocus();

        if (getIntent().getBooleanExtra("update",false)){
            Cursor c=db.sq(QueryTabungan.selectwhere("qsimpanan")+QueryTabungan.sWhere("idsimpanan",getIntent().getStringExtra("idsimpanan")));
            if (c.getCount()>0){
                c.moveToFirst();
                Cursor idjenis=db.sq(QueryTabungan.selectwhere("tbljenissimpanan")+" idjenis<"+ModulTabungan.getString(c,"idjenis"));
                mSpJenisSimpanan.setSelection(idjenis.getCount());
                idanggota=ModulTabungan.getString(c,"idanggota");
                anggota=ModulTabungan.getString(c,"namaanggota");
                mEdtPelanggan.setText(anggota);
                mEdtSimpanan.setText(ModulTabungan.getString(c,"simpanan"));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000){
            idanggota=data.getStringExtra("id");
            anggota=data.getStringExtra("nama");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEdtPelanggan.setText(anggota);
    }

    private void initView() {
        mEdtPelanggan = (TextInputEditText) findViewById(R.id.edtPelanggan);
        mEdtSimpanan = (TextInputEditText) findViewById(R.id.edtSimpanan);
        mSpJenisSimpanan = (Spinner) findViewById(R.id.spJenisSimpanan);
        mBtnSimpan = (Button) findViewById(R.id.btnSimpan);
        mIbtnCari = (ImageButton) findViewById(R.id.ibtnCari);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void simpan(View view) {
        String[] qBaru={
                idanggota,
                getIdJenis.get(mSpJenisSimpanan.getSelectedItemPosition()),
                mEdtSimpanan.getText().toString()
        };
        String q=QueryTabungan.splitParam("INSERT INTO tblsimpanan (idanggota,idjenis,simpanan) VALUES (?,?,?)",qBaru);
        if (qBaru[0].equals("0")||qBaru[2].isEmpty()){
            Toast.makeText(this, R.string.formnull, Toast.LENGTH_SHORT).show();
        }else {
            if (db.exc(q)){
                Toast.makeText(this, R.string.berhasil, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
