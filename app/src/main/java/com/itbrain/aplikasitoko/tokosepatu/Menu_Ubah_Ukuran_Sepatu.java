package com.itbrain.aplikasitoko.tokosepatu;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Locale;

public class Menu_Ubah_Ukuran_Sepatu extends AppCompatActivity {
    String id,idbar;
    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;
    boolean stat=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_ukuran_salon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubah Detail Ukuran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id=getIntent().getStringExtra("id");
        idbar=getIntent().getStringExtra("idbar");

        config = new ModulTokoSepatu(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this) ;
        v = this.findViewById(android.R.id.content);

        setBarang();
        setUkuran();
        EditText harga = findViewById(R.id.eHarga);
        harga.setShowSoftInputOnFocus(false);
        harga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hidekeyboard();
                }else{
                    keyboard();
                    hidekeyboarddef(Menu_Ubah_Ukuran_Sepatu.this);
                }
            }
        });
        harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard();
            }
        });
        Locale locale = new Locale("id","id");
        harga.addTextChangedListener(new TextWatcherPengeluaranSepatu(harga,locale,2));
        NumberKeyboard numberKeyboard = findViewById(R.id.key);
        numberKeyboard.setListener(new NumberKeyboardListener() {

            @Override
            public void onNumberClicked(int i) {
                EditText editText;
                editText =findViewById(R.id.eHarga);
                int start =editText.getSelectionStart(); //this is to get the the cursor position

                int total =editText.getText().length();
                String s = ModulTokoSepatu.intToStr(i);
                editText.getText().insert(start, s);
                int selisih=editText.getText().length()-total;
                editText.setSelection(start+selisih);

            }

            @Override
            public void onLeftAuxButtonClicked() {
                EditText editText;
                editText =findViewById(R.id.eHarga);
                int start =editText.getSelectionStart(); //this is to get the the cursor position
                int total =editText.getText().length();
                String s = ",";
                editText.getText().insert(start, s); //this will get the text and insert the String s into   the current position
                int selisih=editText.getText().length()-total;
                editText.setSelection(start+selisih);

            }

            @Override
            public void onRightAuxButtonClicked() {
                EditText bayar;

                bayar =findViewById(R.id.eHarga);
                int posisi = bayar.getSelectionStart();
                int akhir = bayar.getSelectionEnd();
                String isi =bayar.getText().toString();
                if (bayar.getText().length()>0 && posisi>0){
                    bayar.getText().delete(posisi-1,posisi);
                }else if (bayar.getText().length()>0){
                    bayar.getText().delete(bayar.getText().length()-1,bayar.getText().length());
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (stat){
            hidekeyboard();
            stat=false;
        }else {
            super.onBackPressed();
        }

    }

    public void keyboard(){
        NumberKeyboard num = findViewById(R.id.key);
        num.setVisibility(View.VISIBLE);
        stat=true;

    }

    public void hidekeyboard(){
        NumberKeyboard num = findViewById(R.id.key);
        num.setVisibility(View.GONE);

    }

    public void hidekeyboarddef(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBarang(){
        Cursor c = db.sq("SELECT * FROM tblbarang WHERE idbarang="+idbar) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.tvBarang,ModulTokoSepatu.getString(c,"barang")) ;
            ModulTokoSepatu.setText(v,R.id.tvDek,ModulTokoSepatu.getString(c,"deskripsi")) ;
            ModulTokoSepatu.setText(v,R.id.eStokBar,ModulTokoSepatu.getString(c,"stokbarang")) ;

        }
    }

    private void setUkuran(){
        Cursor c = db.sq("SELECT * FROM tblukuran WHERE idukuran="+id) ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulTokoSepatu.setText(v,R.id.eUkuran,ModulTokoSepatu.toUppercase(ModulTokoSepatu.getString(c,"ukuran"))) ;
            ModulTokoSepatu.setText(v,R.id.eStokUk,ModulTokoSepatu.getString(c,"stok")) ;
            ModulTokoSepatu.setText(v,R.id.eHarga,ModulTokoSepatu.unNumberFormat(ModulTokoSepatu.removeE(ModulTokoSepatu.getString(c,"harga")))) ;

        }
    }

    private void tambahUkuran(){
        String ukuran = ModulTokoSepatu.getText(v,R.id.eUkuran);
        String harga = ModulTokoSepatu.getText(v,R.id.eHarga);
        String stok= ModulTokoSepatu.getText(v,R.id.eStokUk);
        String idukur = id;

        if (ModulTokoSepatu.strToDouble(ModulTokoSepatu.unNumberFormat(harga))<1 || ModulTokoSepatu.strToDouble(stok)<1){
            ModulTokoSepatu.showToast(this,"Terdapat pengisian 0");
        }else if(!TextUtils.isEmpty(ukuran) && !TextUtils.isEmpty(harga)  && !TextUtils.isEmpty(stok)){
            String[] p = {ukuran,ModulTokoSepatu.unNumberFormat(harga),stok,idukur} ;
            String q = ModulTokoSepatu.splitParam("UPDATE tblukuran SET ukuran=?,harga=?,stok=? WHERE idukuran=?   ",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil mengubah Barang", Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(this, "Gagal mengubah Barang", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void tambah(View view) {
        tambahUkuran();
    }


}
