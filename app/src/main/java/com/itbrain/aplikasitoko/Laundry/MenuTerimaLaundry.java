package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.Calendar;

public class MenuTerimaLaundry extends AppCompatActivity {
    EditText edtJumlahJasa;
    ImageButton btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;
    TextView tvSatuan;
    View v;
    DatabaseLaundry db;

    int year, month, day ;
    Calendar calendar ;

    String faktur="00000000";
    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="",status,updateFaktur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuterimalaundry);
    }
}