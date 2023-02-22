package com.itbrain.aplikasitoko.TokoKain;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Form_Tambah_Pelanggan_Toko_Kain_ extends AppCompatActivity {

    Button btnSimpan;
    TextInputEditText edtNama,edtAlamat,edtNotelp;
    private String Nama,Alamat,NoTelp;
    Integer idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_pelanggan_toko_kain);
        Bundle extra = getIntent().getExtras();
        if (extra==null) {

        } else {

        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton imageButton = findViewById(R.id.kembaliiop);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            btnSimpan=(Button)findViewById(R.id.btnTambah);
            edtNama=(TextInputEditText)findViewById(R.id.edtNamaPelanggan);
            edtAlamat=(TextInputEditText)findViewById(R.id.edtAlamatPelanggan);
            edtNotelp=(TextInputEditText)findViewById(R.id.edtTeleponPelanggan);

            if (extra==null){
                //Insert
                idPelanggan=null;
            }else {
                idPelanggan = extra.getInt("idpelanggan");
                edtNama.setText(extra.getString("namapelanggan"));
                edtAlamat.setText(extra.getString("alamatpelanggan"));
                edtNotelp.setText(extra.getString("telppelanggan"));
                btnSimpan.setText("Ubah");
            }

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Nama=edtNama.getText().toString();
                    Alamat=edtAlamat.getText().toString();
                    NoTelp=edtNotelp.getText().toString();

                    if (Nama.equals("")||Alamat.equals("")||NoTelp.equals("")){
                        Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Isi semua data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }else {
                        if (!NoTelp.matches("[0-9]+")){
                            Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Isi nomor telepon dengan benar", Toast.LENGTH_SHORT).show();
                        }else {
                            DatabaseTokoKain db=new DatabaseTokoKain(Form_Tambah_Pelanggan_Toko_Kain_.this);
                            if (idPelanggan==null){
                                if (db.insertToPelanggan(Nama,Alamat,NoTelp)){
                                    Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if (db.updatePelanggan(idPelanggan,Nama,Alamat,NoTelp)){
                                    Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Berhasil memperbaharui data", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(Form_Tambah_Pelanggan_Toko_Kain_.this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                                }
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
