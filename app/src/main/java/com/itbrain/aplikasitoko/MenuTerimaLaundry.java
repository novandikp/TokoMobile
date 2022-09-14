package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MenuTerimaLaundry extends AppCompatActivity {
    EditText Faktur,TanggalMulai,TanggalKembali,NamaPelanggan,NamaPegawai,HargaJasa,Jumlah;
    DatabaseLaundry db;
    Button Simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuterimalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        Faktur = (TextInputEditText) findViewById(R.id.Faktur);
        TanggalMulai = (TextInputEditText) findViewById(R.id.TanggalMulai);
        TanggalKembali = (TextInputEditText) findViewById(R.id.TanggalKembali);
        NamaPelanggan = (TextInputEditText) findViewById(R.id.NamaPelanggan);
        NamaPegawai = (TextInputEditText) findViewById(R.id.namaPegawaiLaundry);
        HargaJasa = (TextInputEditText) findViewById(R.id.HargaJasa);
        Jumlah = (TextInputEditText) findViewById(R.id.Jumlah);
        db = new DatabaseLaundry(this);
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }
    public void add(){
        String faktur = Faktur.getText().toString();
        String tanggalmulai = TanggalMulai.getText().toString();
        String tanggalkembali = TanggalKembali.getText().toString();
        String namapelanggan = NamaPelanggan.getText().toString();
        String namapegawai = NamaPegawai.getText().toString();
        String hargajasa = HargaJasa.getText().toString();
        String jumlah = Jumlah.getText().toString();
        if(TextUtils.isEmpty(faktur)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into qproses (faktur,tgllaundry,tglselesai,total,statuslaundry,statusbayar,pelanggan,alamat,notelp) values ('"+ faktur +"','"+ tanggalmulai +"','"+ tanggalkembali +"','"+ namapelanggan +"','"+ namapegawai +"','"+ hargajasa +"','"+ jumlah +"')");
            finish();
        }
    }
}