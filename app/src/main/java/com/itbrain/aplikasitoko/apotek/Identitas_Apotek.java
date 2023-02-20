package com.itbrain.aplikasitoko.apotek;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class Identitas_Apotek extends AppCompatActivity {

    ModulApotek config,temp;
    DatabaseApotek db ;
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
        setContentView(R.layout.identitas_apotek);


        config = new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseApotek(this) ;
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setPelanggan();
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setPelanggan(){
        Cursor c = db.sq("SELECT * FROM tbltoko WHERE idtoko=1") ;
        if(c.getCount() > 0){
            c.moveToNext() ;
            ModulApotek.setText(v, R.id.eNamaToko, ModulApotek.getString(c,"namatoko")) ;
            ModulApotek.setText(v,R.id.eAlamat, ModulApotek.getString(c,"alamattoko")) ;
            ModulApotek.setText(v,R.id.eNotoko, ModulApotek.getString(c,"notoko")) ;
            ModulApotek.setText(v,R.id.eCaption1, ModulApotek.getString(c,"caption1")) ;
            ModulApotek.setText(v,R.id.eCaption2, ModulApotek.getString(c,"caption2")) ;
            ModulApotek.setText(v,R.id.eCaption3, ModulApotek.getString(c,"caption3")) ;
        }

        ModulApotek.setText(v,R.id.expired,config.getCustom("expired","30"));
    }



    private void tambahidentitas() {
        String nama = ModulApotek.getText(v,R.id.eNamaToko) ;
        String alamat = ModulApotek.getText(v,R.id.eAlamat) ;
        String notelp = ModulApotek.getText(v,R.id.eNotoko) ;
        String caption1 = ModulApotek.getText(v,R.id.eCaption1);
        String caption2 = ModulApotek.getText(v,R.id.eCaption2);
        String caption3 = ModulApotek.getText(v,R.id.eCaption3);
        String expired = ModulApotek.getText(v,R.id.expired);


        if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp) && !TextUtils.isEmpty(caption1) && !TextUtils.isEmpty(caption2) && !TextUtils.isEmpty(caption3)){
            String[] p = {nama,alamat,notelp,caption1,caption2,caption3} ;
            String q = ModulApotek.splitParam("INSERT OR REPLACE INTO tbltoko (idtoko,namatoko,alamattoko,notoko,caption1,caption2,caption3) values(1,?,?,?,?,?,?)",p) ;
            if(db.exc(q)){
                config.setCustom("expired",expired);
                ModulApotek.showToast(Identitas_Apotek.this,"Identitas telah tersimpan");
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
        ModulApotek.setText(v,R.id.eNamaToko,"") ;
        ModulApotek.setText(v,R.id.eAlamat,"") ;
        ModulApotek.setText(v,R.id.eNotoko,"") ;
        ModulApotek.setText(v,R.id.eCaption1,"") ;
        ModulApotek.setText(v,R.id.eCaption2,"") ;
        ModulApotek.setText(v,R.id.eCaption3,"") ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == IMAGE_RESULT) {

//                try{
//                    Bundle extras = data.getExtras();
//                    Bitmap imageBitmap = (Bitmap) extras.get("data");
//                    ImageView img = findViewById(R.id.logoToko);
//                    img.setImageBitmap(imageBitmap);
//                }catch (Exception e){
//                    Toast.makeText(ActivityIdentitas.this,e.getMessage(),Toast.LENGTH_LONG);
//                }


            }

        }

    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
}