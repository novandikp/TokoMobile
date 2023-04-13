package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.List;

public class MenuUtama_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    TextView tvNama, tvAlamat;
    CardView pro;
    static boolean status;
    static final Integer WRITE_EXST = 0x3;
    //CekInApp cekInApp;
    String produkid = "com.komputerkit.easyprintreceipt.full";
    //    String produkid="android.test.purchased";
    String produkid2 = "com.komputerkit.easyprintreceiptnew.full";
    //IAPHelper iapHelper;
    String belisku = produkid;
    private List<String> skuList = Arrays.asList(produkid, produkid2);
    //HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
    SharedPreferences getPrefs;
    SharedPreferences.Editor edit;
    String deviceid;
    static String base64Encode = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmo0sBB98yZ7bcXu99+WwnVj4IG221AhGAWgL3MAcKsS6LL6bXnnN3vN/NTZzmhkUciTJnVVQtbS9lY5soWeNSlwAwH9xJtwuyd4Ae+pvBqEhH4ApKg2wwK9GRlysUk6Sq7i/OY8/ek7W9BMeg+1FXxZaPe9NdxRpPTry79bk81J9PV3CUtr8vkP6AhoixtcJnjR55GJ/kvGix5icq/L/kRxq0NqW5xARFwUs25Nxc2kqpyBc5tXfb35uw7mn/azabt0OdA3q/cWrsVfhqZQ61zolh8hJoaAVT2NBq39a1M4zxDdn1L9OM3180RI+5IbHuL/jfm3Wmqh+xIw9B52RVwIDAQAB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuutama_kwitansi);

        tvNama = findViewById(R.id.textView478);
        tvAlamat = findViewById(R.id.textView479);

    }

    protected void onResume() {
        super.onResume();

        load();
    }

    @SuppressLint("Range")
    public void selectData() {
        String sql = "SELECT nama, alamatid FROM tblidentitas";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                tvNama.setText(cursor.getString(cursor.getColumnIndex("nama")));
                tvAlamat.setText(cursor.getString(cursor.getColumnIndex("alamatid")));
            }
        }
    }

    public void load() {
        db = new DatabaseCetakKwitansi(this);

        selectData();
    }



    public void cvMaster(View view) {
        Intent intent=new Intent(this, MenuMaster_Kwitansi.class);
        startActivity(intent);
    }

    public void cvTransaksi(View view) {
        Intent intent=new Intent(this, MenuTransaksi_Kwitansi.class);
        startActivity(intent);
    }

    public void cvLaporan(View view) {
        Intent intent=new Intent(this, MenuLaporan_Kwitansi.class);
        startActivity(intent);
    }

    public void cvUtilitas(View view) {
        Intent intent=new Intent(this, MenuUtilitas_Kwitansi.class);
        startActivity(intent);
    }

    private void keluar(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.create();
        alert.setMessage("Yakin Ingin Keluar Dari Kwitansi ?");
        alert.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        keluar();
    }
}