package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Model.Kategori;
import com.itbrain.aplikasitoko.Model.Pegawai;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LaundryMenuUtamaMaster extends AppCompatActivity {

    DatabaseLaundry db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenuutamamaster);
        db = new DatabaseLaundry(this) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        v=this.findViewById(android.R.id.content);
        Cursor c=db.sq(Query.selectwhere("tblidentitas")+Query.sWhere("ididentitas", "1"));
        c.moveToNext();
        Modul.setText(v,R.id.NamaToko,Modul.upperCaseFirst(Modul.getString(c,"namatoko")));
        Modul.setText(v,R.id.AlamatToko,Modul.getString(c,"alamattoko"));
        Modul.setText(v,R.id.NomorToko,Modul.getString(c,"notelptoko"));
//        Modul.setText(v,R.id.Caption1,Modul.getString(c,"caption_1"));
//        Modul.setText(v,R.id.Caption2,Modul.getString(c,"caption_2"));
//        Modul.setText(v,R.id.Caption3,Modul.getString(c,"caption_3"));
    }

public void Master(View view) {
    Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuMaster.class);
    startActivity(intent);
}

    public void Transaksi(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuLaporan.class);
        startActivity(intent);
    }

    public void Utilitas(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, MenuUtilitasLaundry.class);
        startActivity(intent);
    }
}