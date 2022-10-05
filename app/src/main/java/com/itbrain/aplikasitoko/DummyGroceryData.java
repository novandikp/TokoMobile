package com.itbrain.aplikasitoko;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DummyGroceryData extends AppCompatActivity {
    public List<View> groceryList() {
        setContentView(R.layout.backupan_main_app);
        View tabungan = findViewById(R.id.Tabungan);
        View rental = findViewById(R.id.RentalMobil);
        View kredit = findViewById(R.id.RentalMobil);
        View antrian = findViewById(R.id.RentalMobil);
        View kwintansi = findViewById(R.id.RentalMobil);
        View tokosepatu = findViewById(R.id.RentalMobil);
        View laundry = findViewById(R.id.RentalMobil);



        List<View> groceryList = new ArrayList<>();

        groceryList.add(tabungan);
        groceryList.add(rental);
        groceryList.add(kredit);
        groceryList.add(antrian);
        groceryList.add(kwintansi);
        groceryList.add(tokosepatu);
        groceryList.add(laundry);

        return groceryList;
    }
}
