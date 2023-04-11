package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;

public class MenuMasterTabungan extends AppCompatActivity {
    String tab="";
    //BillingProcessor bp;
    boolean status;
    PrefTabungan config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_master_tabungan);
        ModulTabungan.btnBack(R.string.title_master,getSupportActionBar());
        config=new PrefTabungan(getSharedPreferences("id",MODE_PRIVATE));
//        bp=new BillingProcessor(this,Pref.licenseKey,this);
//        bp.initialize();
        //checkPurchase();

        ImageButton imageButton = findViewById(R.id.kembaliMaster);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkPurchase();
    }

    public void menuIdentitas(View view) {
        startActivity(new Intent(this, MenuMasterIdentitasTabungan.class));
    }

    public void menuPelanggan(View view) {
        tab="pelanggan";
        if (ModulTabungan.cekLimit(this,"anggota")|| (!ModulTabungan.cekLimit(this,"anggota")&& status)){
            startActivity(new Intent(this, MenuAnggotaTabungan.class).putExtra("type","anggota"));
        }else {
            beli();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void menuJenis(View view) {
        tab="jenis";
        if (ModulTabungan.cekLimit(this,"jenis") || (!ModulTabungan.cekLimit(this,"jenis")&& status)){
            startActivity(new Intent(this, MenuJenisTabungan.class).putExtra("type","simpanan"));
        }else {
            beli();
        }
    }

    public void beli(){
        ModulTabungan modul = new ModulTabungan(this);
        modul.inApp();
    }

//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        config.setCustom(ModulTabungan.getEncrypt("status"),ModulTabungan.getEncrypt("premium#!"+ Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID)));
//        if (tab.equals("anggota")){
//            startActivity(new Intent(this,MenuMasterList.class).putExtra("type","anggota"));
//        }else if (tab.equals("jenis")){
//            startActivity(new Intent(this,MenuMasterList.class).putExtra("type","simpanan"));
//        }
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, @Nullable Throwable error) {
//
//    }
//
//    @Override
//    public void onBillingInitialized() {
//
//    }
}
