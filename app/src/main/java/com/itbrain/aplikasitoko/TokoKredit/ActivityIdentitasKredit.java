package com.itbrain.aplikasitoko.TokoKredit;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ActivityIdentitasKredit extends AppCompatActivity {

    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    EditText enama, eAlamat, eTelp, eCap1, eCap2, eCap3;
    TextView tvlength, tvlength2, tvlength3, tvlength4, tvlength5, tvlength6;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identitassatukredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pembatas();

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);

        Cursor c = db.sq(FQueryKredit.select("tblidentitas"));
        if (c.getCount() == 1) {
            setText();
        }

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
        String def ="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";
        String strBase64 = shre.getString("image_data",def);
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView img = findViewById(R.id.logoToko);
        img.setImageBitmap(decodedByte);

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void pembatas() {
        enama = findViewById(R.id.tNama);
        eAlamat = findViewById(R.id.tAlamat);
        eTelp = findViewById(R.id.tTelp);
        eCap1 = findViewById(R.id.cap1);
        eCap2 = findViewById(R.id.cap2);
        eCap3 = findViewById(R.id.cap3);

        tvlength = findViewById(R.id.tvRpm);
        tvlength2 = findViewById(R.id.tvRpm2);
        tvlength3 = findViewById(R.id.tvRpm3);
        tvlength4 = findViewById(R.id.tvRpm4);
        tvlength5 = findViewById(R.id.tvRpm5);
        tvlength6 = findViewById(R.id.tvRpm6);

        enama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength.setText(enama.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eAlamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength2.setText(eAlamat.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eTelp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength3.setText(eTelp.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eCap1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength4.setText(eCap1.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eCap2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength5.setText(eCap2.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eCap3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvlength6.setText(eCap3.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void setText() {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblidentitas") + FQueryKredit.sWhere("id", "1"));
        c.moveToNext();
        FFunctionKredit.setText(v, R.id.tNama, FFunctionKredit.getString(c, "nama"));
        FFunctionKredit.setText(v, R.id.tAlamat, FFunctionKredit.getString(c, "alamat"));
        FFunctionKredit.setText(v, R.id.tTelp, FFunctionKredit.getString(c, "telp"));
        FFunctionKredit.setText(v, R.id.cap1, FFunctionKredit.getString(c, "caption1"));
        FFunctionKredit.setText(v, R.id.cap2, FFunctionKredit.getString(c, "caption2"));
        FFunctionKredit.setText(v, R.id.cap3, FFunctionKredit.getString(c, "caption3"));
    }

    public void selesai(View view) {
        if (!ModuleKredit.notEmpty(new EditText[]{enama, eAlamat, eTelp, eCap1, eCap2, eCap3})) {
            ModuleKredit.info(this, "Harap isi semua field");
            return;
        }

        Cursor c = db.sq(FQueryKredit.select("tblidentitas"));
        String[] p = {"1", FFunctionKredit.getText(v, R.id.tNama),
                FFunctionKredit.getText(v, R.id.tAlamat),
                FFunctionKredit.getText(v, R.id.tTelp),
                FFunctionKredit.getText(v, R.id.cap1),
                FFunctionKredit.getText(v, R.id.cap2),
                FFunctionKredit.getText(v, R.id.cap3)
        };
        String[] p1 = {FFunctionKredit.getText(v, R.id.tNama),
                FFunctionKredit.getText(v, R.id.tAlamat),
                FFunctionKredit.getText(v, R.id.tTelp),
                FFunctionKredit.getText(v, R.id.cap1),
                FFunctionKredit.getText(v, R.id.cap2),
                FFunctionKredit.getText(v, R.id.cap3),
                "1"
        };

        String q = "";
        if (c.getCount() == 1) {
            q = FQueryKredit.splitParam("UPDATE tblidentitas SET nama=? , alamat=? ,telp=? ,caption1=? , caption2=? , caption3=? WHERE id = ? ", p1);
        } else {
            q = FQueryKredit.splitParam("INSERT INTO tblidentitas VALUES(?,?,?,?,?,?,?)", p);
        }
        if (db.exc(q)) {
            Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(View view) {
        EditText[] daftarField = {enama, eAlamat, eTelp, eCap1, eCap2, eCap3};
        for (EditText e : daftarField) {
            e.getText().clear();
        }
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

                String filePath = getImageFilePath(data);

                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

                    ImageView img = findViewById(R.id.logoToko);
                    img.setImageBitmap(selectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.WEBP, 100, baos);
                    byte[] b = baos.toByteArray();

                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


                    SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit=shre.edit();
                    edit.putString("image_data",encodedImage);
                    edit.commit();
                }
            }

        }

    }




    public void gantiFoto(View view) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , IMAGE_RESULT);//one can be replaced with any action code
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