package com.itbrain.aplikasitoko.rentalmobil;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuEditKendaraanMobil extends AppCompatActivity {

    private View v;
    private TextInputLayout textInputLayout4;
    private TextInputLayout textInputLayout5;
    private TextInputLayout textInputLayout6;
    private Spinner spinner2;
    private Guideline guideline2;
    private TextView textView;
    private Button button4;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    DatabaseRentalMobil db;
    private TextInputEditText eKendaraan;
    private TextInputEditText ePlat;
    private TextInputEditText eTahun;
    private TextInputEditText eHarga,eHargaJam;
    String type,idkendaraan;
    ModulRentalMobil config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menueditkendaraanmobil);
        textInputLayout4
                = (TextInputLayout) findViewById(R.id.textInputLayout4);
        textInputLayout5
                = (TextInputLayout) findViewById(R.id.textInputLayout5);
        textInputLayout6
                = (TextInputLayout) findViewById(R.id.textInputLayout6);
        spinner2
                = (Spinner) findViewById(R.id.spinner2);
        guideline2
                = (Guideline) findViewById(R.id.guideline2);
        textView
                = (TextView) findViewById(R.id.textView);
        button4
                = (Button) findViewById(R.id.button4);
        eKendaraan = findViewById(R.id.eKendaraan);
        ePlat = findViewById(R.id.ePlat);
        eHarga = findViewById(R.id.eHarga);
        eHargaJam = findViewById(R.id.eHargaJam);
        eTahun = findViewById(R.id.eTahun);
        db = new DatabaseRentalMobil(this);
        v = findViewById(android.R.id.content);
        config = new ModulRentalMobil(getSharedPreferences("config",MODE_PRIVATE));
        setSpinner();
        type=getIntent().getStringExtra("type");
        idkendaraan=getIntent().getStringExtra("id");
        if(idkendaraan!=null){
            setText();
        }

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idkendaraan!=null){
                    edit();
                }else{
                    edit();
                }
            }
        });


        eHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double harga = ModulRentalMobil.strToDouble(eHarga.getText().toString());
                ModulRentalMobil.setText(v,R.id.eHargaJam,ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(Math.round(harga/24))));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        eHargaJam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double harga = ModulRentalMobil.strToDouble(eHargaJam.getText().toString());
                ModulRentalMobil.setText(v,R.id.eHargaMenit,ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(Math.round(harga/60))));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
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

    private void setText(){
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblkendaraan")+ModulRentalMobil.sWhere("idkendaraan",idkendaraan));
        if (c.getCount()>0){
            c.moveToNext();
            eKendaraan.setText(ModulRentalMobil.getString(c,"mobil"));
            eTahun.setText(ModulRentalMobil.getString(c,"tahunkeluaran"));
            ePlat.setText(ModulRentalMobil.getString(c,"plat"));
            ModulRentalMobil.setText(v,R.id.eHargaJam,ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(ModulRentalMobil.getString(c,"hargajam"))));
            ModulRentalMobil.setText(v,R.id.eHargaMenit,ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE((ModulRentalMobil.getString(c,"hargamenit")))));
            eHarga.setText(ModulRentalMobil.unNumberFormat(ModulRentalMobil.removeE(ModulRentalMobil.getString(c,"harga"))));
            Cursor b = db.sq(ModulRentalMobil.selectwhere("tblmerk")+"idmerk<="+ModulRentalMobil.getString(c,"idmerk"));
            spinner2.setSelection(b.getCount());
        }

    }

    private void setSpinner() {
        arrayList.add("Pilih Merk");
        arrayid.add("0");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        Cursor c = db.sq(ModulRentalMobil.select("tblmerk")) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arrayList.add(ModulRentalMobil.getString(c,"merk"));
                arrayid.add(ModulRentalMobil.getString(c,"idmerk"));
            }
        }
        adapter.notifyDataSetChanged();
        spinner2.setSelection(1);
    }

    private String getMerk(){
        return arrayid.get(spinner2.getSelectedItemPosition()).toString();
    }

    private int getPosisiMerk(String id){
        int x =0;
        while (arrayid.get(x)!=id){
            x++;
        }
        return x;
    }


    private void edit(){
        String kendaraan = eKendaraan.getText().toString();
        String tahun = eTahun.getText().toString();
        String plat = ePlat.getText().toString();
        String harga = eHarga.getText().toString();
        String hargaJam = ModulRentalMobil.getText(v,R.id.eHargaJam);
        String hargaMenit = ModulRentalMobil.getText(v,R.id.eHargaMenit);
        String merk =getMerk();
        if (!TextUtils.isEmpty(kendaraan) && !TextUtils.isEmpty(tahun) && !TextUtils.isEmpty(plat) && !TextUtils.isEmpty(harga) && !merk.equals("0") ){
            String [] isi={kendaraan,tahun,plat,harga,merk,hargaJam,hargaMenit,idkendaraan};
            String q= ModulRentalMobil.splitParam("UPDATE tblkendaraan SET mobil=?,tahunkeluaran=?,plat=?,harga=?,idmerk=?,hargajam=?,hargamenit=? WHERE idkendaraan=?   ",isi);
            if (db.exc(q)){
                ModulRentalMobil.showToast(this,getString(R.string.toast_edit));
                finish();
            }else{
                ModulRentalMobil.showToast(this,getString(R.string.gagal_simpan));
            }
        }else{
            ModulRentalMobil.showToast(this,getString(R.string.kurang_lengkap));
        }
    }
}
