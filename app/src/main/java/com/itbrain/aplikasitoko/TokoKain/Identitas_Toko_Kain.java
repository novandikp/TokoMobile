package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class Identitas_Toko_Kain extends AppCompatActivity {

    View v;
    DatabaseTokoKain db;

    Button btnClear;

    String tnToko, tnAlamat, tnNomer, tnCap1, tncap2, tncap3;
    TextInputEditText etToko;
    TextInputEditText etAlamat;
    TextInputEditText etNomer;
    TextInputEditText etcap1;
    TextInputEditText etcap2;
    TextInputEditText etcap3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.identitas_toko_kain);
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            db = new DatabaseTokoKain(this);
            v = this.findViewById(android.R.id.content);
            etToko = findViewById(R.id.etNamaToko);
            etAlamat = findViewById(R.id.etAlamatToko);
            etNomer = findViewById(R.id.etNomerToko);
            etcap1 = findViewById(R.id.etCap1);
            etcap2 = findViewById(R.id.etCap2);
            etcap3 = findViewById(R.id.etCap3);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


            Cursor c = db.sq(FQueryTokoKain.select("tblidentitas"));
            if (c.getCount() == 1) {
                setText();
            }

            btnClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    etToko.setText("");
                    etAlamat.setText("");
                    etNomer.setText("");
                    etcap1.setText("");
                    etcap2.setText("");
                    etcap3.setText("");
                    etToko.getText().clear();
                    etAlamat.getText().clear();
                    etNomer.getText().clear();
                    etcap1.getText().clear();
                    etcap2.getText().clear();
                    etcap3.getText().clear();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText() {
        Cursor c = db.sq(FQueryTokoKain.selectwhere("tblidentitas") + FQueryTokoKain.sWhere("id", "1"));
        c.moveToNext();
        etToko.setText(KumFunTokoKain.getString(c, "namatoko"));
        etAlamat.setText(KumFunTokoKain.getString(c, "alamattoko"));
        etNomer.setText(KumFunTokoKain.getString(c, "nomerptoko"));
        etcap1.setText( KumFunTokoKain.getString(c, "caption_1"));
        etcap2.setText(KumFunTokoKain.getString(c, "caption_2"));
        etcap3.setText(KumFunTokoKain.getString(c, "caption_3"));

    }

    public void simpan(View view) {
        String etoko = etToko.getText().toString();
        String ealamat = etAlamat.getText().toString();
        String enomer = etNomer.getText().toString();
        String ecap1 = etcap1.getText().toString();
        String ecap2 = etcap2.getText().toString();
        String ecap3 = etcap3.getText().toString();
        if (TextUtils.isEmpty(etoko)) {
            Toast.makeText(this, "Masukkan toko", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ealamat)) {
            Toast.makeText(this, "Halo alamat", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(enomer)) {
            Toast.makeText(this, "Halo nomer", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ecap1)) {
            Toast.makeText(this, "Halo cap 1", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ecap2)) {
            Toast.makeText(this, "Halo cap 2", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ecap3)) {
            Toast.makeText(this, "Halo cap 3", Toast.LENGTH_SHORT).show();
        }
//            Toast.makeText(this, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();

        else {
        Cursor c = db.sq(FQueryTokoKain.select("tblidentitas"));
        String[] p = {"1",
               etoko,
               ealamat,
               enomer,
                ecap1,
                ecap2,
                ecap3
        };
        String[] p1 = {
                etoko,
                ealamat,
                enomer,
                ecap1,
                ecap2,
                ecap3,
                "1"
        };

        String q = "";
        if (c.getCount() == 1) {
            q = "UPDATE tblidentitas SET namatoko='"+etoko+"' , alamattoko='"+ealamat+"' ,notelptoko='"+enomer+"' ,caption_1='"+ecap1+"' , caption_2='"+ecap2+"' , caption_3='"+ecap3+"' WHERE id='"+"1"+"'  ";
        } else {
            q = FQueryTokoKain.splitParam("INSERT INTO tblidentitas VALUES(?,?,?,?,?,?,?)", p);
        }
        if (db.exc(q)) {
            Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
        }
    }

}

    public void hapus(View view) {
        KumFunTokoKain.setText(v, R.id.etNamaToko, ""  );
        etToko.setText(tnToko);
        KumFunTokoKain.setText(v, R.id.etAlamatToko, "");
        etAlamat.setText(tnAlamat);
        KumFunTokoKain.setText(v, R.id.etNomerToko, "" );
        etNomer.setText(tnNomer);
        KumFunTokoKain.setText(v, R.id.etCap1, "");
        etcap1.setText(tnCap1);
        KumFunTokoKain.setText(v, R.id.etCap2, "");
        etcap2.setText(tncap2);
        KumFunTokoKain.setText(v, R.id.etCap3, "");
        etcap3.setText(tncap3);

    }

}



