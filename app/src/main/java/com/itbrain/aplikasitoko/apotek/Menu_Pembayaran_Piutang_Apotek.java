package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Pembayaran_Piutang_Apotek extends AppCompatActivity {
    ModulApotek config, temp;
    String type;
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pembayaran_piutang_apotek);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        type = getIntent().getStringExtra("type");

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getBayar("");
        final EditText eCari = (EditText) findViewById(R.id.nyari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();
                getBayar(a);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBayar("");
    }
    public void getBayar(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcPiutang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Piutang_Apotek(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("tblpelanggan") + " hutang>0 AND " + ModulApotek.sLike("pelanggan", cari) + ModulApotek.sOrderASC("pelanggan");
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur;
            campur = ModulApotek.getString(c, "idpelanggan") + "__" + ModulApotek.getString(c, "pelanggan") + "__" + ModulApotek.getString(c, "alamat") + "__" + ModulApotek.getString(c, "notelp") + "__" + ModulApotek.getString(c, "hutang") + "__" + type;

            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
}



