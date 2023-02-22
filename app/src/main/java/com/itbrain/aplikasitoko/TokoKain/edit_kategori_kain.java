package com.itbrain.aplikasitoko.TokoKain;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class edit_kategori_kain extends AppCompatActivity {


    Button btnSimpan;
    TextInputEditText edtKategori;
    String Kategori;
    Integer idKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_kategori_kain);

        Bundle extra = getIntent().getExtras();
        if (extra==null) {

        } else {

        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton imageButton = findViewById(R.id.Kembaliokkkk);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            btnSimpan=(Button) findViewById(R.id.btnTambahh);
            edtKategori=(TextInputEditText)findViewById(R.id.edtKategori);

            if (extra==null){
                //Insert
                idKategori=null;
            }else {
                idKategori = extra.getInt("idkategori");
                edtKategori.setText(extra.getString("kategori"));
                btnSimpan.setText("Ubah");
            }

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Kategori=edtKategori.getText().toString();
                    if (Kategori.equals("")){
                        Toast.makeText(edit_kategori_kain.this, "Isi data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseTokoKain db=new DatabaseTokoKain(edit_kategori_kain.this);
                        if (idKategori==null){
                            if (db.insertToKategori(Kategori)){
                                Toast.makeText(edit_kategori_kain.this, "Tambah kategori berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(edit_kategori_kain.this, "Tambah data gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (db.updateKategori(idKategori,Kategori)){
                                Toast.makeText(edit_kategori_kain.this, "Update kategori berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(edit_kategori_kain.this, "Update data gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
