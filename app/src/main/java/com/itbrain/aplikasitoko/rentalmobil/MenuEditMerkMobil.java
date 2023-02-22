package com.itbrain.aplikasitoko.rentalmobil;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class MenuEditMerkMobil extends AppCompatActivity {

    String type,idmerk;
    DatabaseRentalMobil db;
    private TextInputEditText eMerk;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menueditmerkmobil);
        eMerk
                = (TextInputEditText) findViewById(R.id.eMerk);
        button2
                = (Button) findViewById(R.id.button2);
        db= new DatabaseRentalMobil(this);
        type=getIntent().getStringExtra("type");
        idmerk=getIntent().getStringExtra("id");
        if(idmerk!=null){
            setText();
        }


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idmerk!=null){
                    edit();
                }else{
                    edit();
                }
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
        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblmerk")+ModulRentalMobil.sWhere("idmerk",idmerk));
        c.moveToNext();
        eMerk.setText(ModulRentalMobil.getString(c,"merk"));
    }



    private void edit(){
        String merk = eMerk.getText().toString();
        if (!TextUtils.isEmpty(merk)){
            String [] isi ={merk,idmerk};
            String q = ModulRentalMobil.splitParam("UPDATE tblmerk SET merk=? WHERE idmerk=?  ",isi);
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

    public void simpan(View view) {
        edit();
    }
}

