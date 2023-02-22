package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Obat extends AppCompatActivity {
    String type;
    ModulApotek config, temp;
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKat = new ArrayList();
    ArrayList arraySat = new ArrayList();
    ArrayList arrayKet = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_obat_apotek);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        config = new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp",this.MODE_PRIVATE));
        v = this.findViewById(android.R.id.content);
        db = new DatabaseApotek(this);
        setText();
    }

    private void tambahlimit() {
        boolean status = Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status;
        if (!status) {
            int batas = ModulApotek.strToInt(config.getCustom("barang", "1")) + 1;
            config.setCustom("barang", ModulApotek.intToStr(batas));
        }
    }
        public boolean onOptionItemSelected(MenuItem item){
            if (item.getItemId() == android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        }
        private void setText(){
            setKategori();
            setKet();
            setSatuan();
        }
        private void tambahBarang(){
            if (arrayKat.isEmpty() || arraySat.isEmpty()){
                Toast.makeText(this, "Tambahkan Satuan dan Kategori nya", Toast.LENGTH_SHORT).show();
                return;
            }
            String obat = ModulApotek.getText(v, R.id.NamaObat);
            String satuan = getSatuan();
            String kategori = getKategori();
            String ketbarang = getKet();
            String kodebarang= ModulApotek.getText(v,R.id.KodeBarang);

        if(!TextUtils.isEmpty(kodebarang) && !TextUtils.isEmpty(obat) &&  !kategori.equals("0") && !satuan.equals("0")){
            Cursor c= db.sq(ModulApotek.selectwhere("tblbarang")+ ModulApotek.sWhere("idbarang",kodebarang));
            if (c.getCount()==0){
                String[] p = {kodebarang,obat,kategori,satuan,ketbarang} ;
                String q = ModulApotek.splitParam("INSERT INTO tblbarang (idbarang,barang,idkategori,idsatuan,ketbarang) values(?,?,?,?,?)",p) ;
                if(db.exc(q)){
                    Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                    tambahlimit();
                    finish();
                } else {
                    Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
                }
            }else{
                ModulApotek.showToast(this,"Kode barang sudah ada");
            }

        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }
        private void setKategori() {
            arrayKat.clear();
            arrayList.clear();
            arrayKat.add("Pilih Kategori");
            arrayList.add("0");
            Spinner spinner = (Spinner) findViewById(R.id.sp_kategori_obat) ;
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKat);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            Cursor c = db.sq(ModulApotek.select("tblkategori"));
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    arrayKat.add(ModulApotek.getString(c,"kategori"));
                    arrayList.add(ModulApotek.getString(c,"idkategori"));
                }
            }
            adapter.notifyDataSetChanged();
            spinner.setSelection(1);
        }

        private void setSatuan() {
            arraySat.clear();
            arrayid.clear();
            arraySat.add("Pilih Satuan");
            arrayid.add("0");
            Spinner spinner = (Spinner) findViewById(R.id.sp_satuan_obat) ;
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySat);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            Cursor c = db.sq(ModulApotek.select("tblsatuan"));
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    String isi= ModulApotek.getString(c,"satuanbesar")+" - "+ ModulApotek.getString(c,"satuankecil");
                    arraySat.add(isi);
                    arrayid.add(ModulApotek.getString(c,"idsatuan"));
                }
            }
            adapter.notifyDataSetChanged();
            spinner.setSelection(1);
        }


        private void setKet(){
            arrayKet.clear();
            arrayKet.add("Titipan");
            arrayKet.add("Kulakan");
            Spinner spinner = (Spinner) findViewById(R.id.sKet_barang) ;
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKet);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        private String getKategori(){
            Spinner spinner = (Spinner) findViewById(R.id.sp_kategori_obat) ;
            return arrayList.get(spinner.getSelectedItemPosition()).toString();
        }

        private String getSatuan(){
            Spinner spinner = (Spinner) findViewById(R.id.sp_satuan_obat) ;
            return arrayid.get(spinner.getSelectedItemPosition()).toString();
        }

        private String getKet(){
            Spinner spinner = (Spinner) findViewById(R.id.sKet_barang) ;
            return spinner.getSelectedItem().toString();
        }

        public void simpan(View view) {
            tambahBarang();
        }
}
