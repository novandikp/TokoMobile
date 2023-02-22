package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Identitas_Salon_ extends AppCompatActivity {
    Toolbar appbar;
    DatabaseSalon db;
    View v;
    TextInputEditText edt1, edt2, edt3, edt4, edt5, edt6;
    Button btnClear;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_identitas_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = new DatabaseSalon(this);
        v = this.findViewById(android.R.id.content);

        btnClear = findViewById(R.id.btnClear);
        edt1 = (TextInputEditText) findViewById(R.id.eNama);
        edt2 = (TextInputEditText) findViewById(R.id.eAlamat);
        edt3 = (TextInputEditText) findViewById(R.id.eTelp);
        edt4 = (TextInputEditText) findViewById(R.id.cap1);
        edt5 = (TextInputEditText) findViewById(R.id.cap2);
        edt6 = (TextInputEditText) findViewById(R.id.cap3);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText("");
                edt2.setText("");
                edt3.setText("");
                edt4.setText("");
                edt5.setText("");
                edt6.setText("");
                edt1.getText().clear();
                edt2.getText().clear();
                edt3.getText().clear();
                edt4.getText().clear();
                edt5.getText().clear();
                edt6.getText().clear();
            }
        });

        setText();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText() {
        Cursor c = db.sq(Query.selectwhere("tblidentitas") + Query.sWhere("ididentitas", "1"));
        if (FunctionSalon.getCount(c) == 1) {
            c.moveToNext();
            FunctionSalon.setText(v, R.id.eNama, FunctionSalon.getString(c, "nama"));
            FunctionSalon.setText(v, R.id.eAlamat, FunctionSalon.getString(c, "alamat"));
            FunctionSalon.setText(v, R.id.eTelp, FunctionSalon.getString(c, "telp"));
            FunctionSalon.setText(v, R.id.cap1, FunctionSalon.getString(c, "cap1"));
            FunctionSalon.setText(v, R.id.cap2, FunctionSalon.getString(c, "cap2"));
            FunctionSalon.setText(v, R.id.cap3, FunctionSalon.getString(c, "cap3"));
        }

    }

    public void simpan(View view) {
        Cursor c = db.sq(Query.select("tblidentitas"));
        String[] p = {"1", FunctionSalon.getText(v, R.id.eNama),
                FunctionSalon.getText(v, R.id.eAlamat),
                FunctionSalon.getText(v, R.id.eTelp),
                FunctionSalon.getText(v, R.id.cap1),
                FunctionSalon.getText(v, R.id.cap2),
                FunctionSalon.getText(v, R.id.cap3)
        };
        String[] p1 = {FunctionSalon.getText(v, R.id.eNama),
                FunctionSalon.getText(v, R.id.eAlamat),
                FunctionSalon.getText(v, R.id.eTelp),
                FunctionSalon.getText(v, R.id.cap1),
                FunctionSalon.getText(v, R.id.cap2),
                FunctionSalon.getText(v, R.id.cap3),
                "1"
        };
        String nama = FunctionSalon.getText(v, R.id.eNama);
        String alamat = FunctionSalon.getText(v, R.id.eAlamat);
        String telepon = FunctionSalon.getText(v, R.id.eTelp);
        String cap1 = FunctionSalon.getText(v, R.id.cap1);
        String cap2 = FunctionSalon.getText(v, R.id.cap2);
        String cap3 = FunctionSalon.getText(v, R.id.cap3);

        String q = "";
        if (nama.equals("") || alamat.equals("") || telepon.equals("") || cap1.equals("") || cap2.equals("") || cap3.equals("")) {
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            if (FunctionSalon.getCount(c) == 1) {
                q = Query.splitParam("UPDATE tblidentitas SET nama=? , alamat=? ,telp=? ,cap1=? , cap2=? , cap3=? WHERE ididentitas=?   ", p1);
            } else {
                q = Query.splitParam("INSERT INTO tblidentitas VALUES(?,?,?,?,?,?,?)", p);
            }
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show();
            }
        }
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


