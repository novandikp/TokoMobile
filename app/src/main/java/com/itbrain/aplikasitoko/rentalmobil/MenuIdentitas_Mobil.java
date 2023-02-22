package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuIdentitas_Mobil extends AppCompatActivity {


    ModulRentalMobil config,temp;
    DatabaseRentalMobil db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    ArrayList arrayid = new ArrayList() ;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitas_mobil);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        config = new ModulRentalMobil(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulRentalMobil(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseRentalMobil(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setPelanggan();
      
    }
    private void setPelanggan(){

        Cursor c = db.sq("SELECT * FROM tbltoko WHERE idtoko=1") ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulRentalMobil.setText(v,R.id.eNamaToko,ModulRentalMobil.getString(c,"namatoko")) ;
            ModulRentalMobil.setText(v,R.id.eAlamatToko,ModulRentalMobil.getString(c,"alamattoko")) ;
            ModulRentalMobil.setText(v,R.id.eNoToko,ModulRentalMobil.getString(c,"notoko")) ;
            ModulRentalMobil.setText(v,R.id.eCaption1,ModulRentalMobil.getString(c,"caption1")) ;
            ModulRentalMobil.setText(v,R.id.eCaption2,ModulRentalMobil.getString(c,"caption2")) ;
            ModulRentalMobil.setText(v,R.id.eCaption3,ModulRentalMobil.getString(c,"caption3")) ;
        }
    }


    private void tambahidentitas() {
        String nama = ModulRentalMobil.getText(v,R.id.eNamaToko) ;
        String alamat = ModulRentalMobil.getText(v,R.id.eAlamatToko) ;
        String notelp = ModulRentalMobil.getText(v,R.id.eNoToko) ;
        String caption1 = ModulRentalMobil.getText(v,R.id.eCaption1);
        String caption2 = ModulRentalMobil.getText(v,R.id.eCaption2);
        String caption3 = ModulRentalMobil.getText(v,R.id.eCaption3);


        if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp) && !TextUtils.isEmpty(caption1) && !TextUtils.isEmpty(caption2) && !TextUtils.isEmpty(caption3)){
            String[] p = {nama,alamat,notelp,caption1,caption2,caption3} ;
            String q = ModulRentalMobil.splitParam("INSERT OR REPLACE INTO tbltoko (idtoko,namatoko,alamattoko,notoko,caption1,caption2,caption3) values(1,?,?,?,?,?,?)",p) ;
            if(db.exc(q)){
                ModulRentalMobil.showToast(MenuIdentitas_Mobil.this,"Identitas telah tersimpan");
            } else {
                Toast.makeText(this, "Gagal Mengubah Identitas", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void simpan(View view) {

        tambahidentitas();
    }

    public void reset(View view) {
        ModulRentalMobil.setText(v,R.id.eNamaToko,"") ;
        ModulRentalMobil.setText(v,R.id.eAlamatToko,"") ;
        ModulRentalMobil.setText(v,R.id.eNoToko,"") ;
        ModulRentalMobil.setText(v,R.id.eCaption1,"") ;
        ModulRentalMobil.setText(v,R.id.eCaption2,"") ;
        ModulRentalMobil.setText(v,R.id.eCaption3,"") ;
    }

  

    public void gantiFoto(View view) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , IMAGE_RESULT);//one can be replaced with any action code
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }



}