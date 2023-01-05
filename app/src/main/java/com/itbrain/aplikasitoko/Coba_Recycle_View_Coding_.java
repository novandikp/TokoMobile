package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Coba_Recycle_View_Coding_ extends AppCompatActivity {


    RecyclerView recyclerView;
    Siswa_Adapter adapter;
    List<Siswa> siswaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba_recycle_view_coding_);

        load();
        isiData();
    }

    public void load(){
        recyclerView = findViewById(R.id.rcvSiswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void isiData() {
        siswaList = new ArrayList<Siswa>();
        siswaList.add(new Siswa("Joni", "Surabaya"));
        siswaList.add(new Siswa("Handoko", "Surabaya"));
        siswaList.add(new Siswa("Siti", "Surabaya"));
        siswaList.add(new Siswa("Rafi", "Surabaya"));
        siswaList.add(new Siswa("Herdi", "Surabaya"));
        siswaList.add(new Siswa("Joni", "Surabaya"));
        siswaList.add(new Siswa("Joni", "Surabaya"));

        adapter = new Siswa_Adapter(this, siswaList);
        recyclerView.setAdapter(adapter);
    }


    public void btnTambah(View view) {
        siswaList.add(new Siswa("Jono", "Mojokerto"));
        adapter.notifyDataSetChanged();
    }
}