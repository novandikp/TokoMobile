package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.Model.Kategori;

import java.util.ArrayList;

public class MenuUbahJasaLaundry extends AppCompatActivity {

    Button Simpan;
    EditText edtJasa,edtBiaya;
    DatabaseLaundry db;
//    String Biaya,Jasa;
    ArrayList<String> listkategori,listidkategori;
    Spinner SpinnerKategori,SpinnerSatuan;
    Integer idKat,idJasa;
    int spKatSelection=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.menuubahjasalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        edtJasa = (EditText) findViewById(R.id.edtJasa);
        edtBiaya = (EditText) findViewById(R.id.edtBiaya);
        db = new DatabaseLaundry(this);
        SpinnerSatuan = findViewById(R.id.spinnersatuan);
        getKategori();
        if(isUpdate()){
            edtJasa.setText(getIntent().getStringExtra("jasa"));
            edtBiaya.setText(getIntent().getStringExtra("satuan"));
            SpinnerKategori.setSelection(listidkategori.indexOf(""+getIntent().getIntExtra("idkategori",0)));
//            Toast.makeText(this, "Update"+getIntent().getIntExtra("idkategori",0), Toast.LENGTH_SHORT).show();
        }

        Bundle extra = getIntent().getExtras();
        if (extra==null){
            //Insert
            idJasa=null;
        }else {
            idJasa = extra.getInt("idjasa");
            edtJasa.setText(extra.getString("jasa"));
            edtBiaya.setText(extra.getString("biaya"));
            if (extra.getString("satuan").equals("pc")){
                SpinnerSatuan.setSelection(0);
            }else if (extra.getString("satuan").equals("kg")){
                SpinnerSatuan.setSelection(1);
            }else if (extra.getString("satuan").equals("m2")){
                SpinnerSatuan.setSelection(2);
            }
            String qcount = "SELECT idkategori FROM tblkategori WHERE idkategori<"+String.valueOf(extra.getInt("idkategori"))+" ORDER BY idkategori ASC";
            Cursor cKat=db.sq(qcount);
            spKatSelection=cKat.getCount();
            SpinnerKategori.setSelection(cKat.getCount());
        }

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdate()){
                    update();

                }else{
                    add();
                }

            }
        });
    }

    public void Kembali(View view) {
        Intent intent = new Intent(MenuUbahJasaLaundry.this, MenuDaftarJasaLaundry.class);
        startActivity(intent);
    }

    public void getKategori() {
        SpinnerKategori = findViewById(R.id.spinnerkategori);
        listidkategori = new ArrayList<>();
        listkategori = new ArrayList<>();
        Cursor cursor=db.sq("select * from tblkategori");
        if(cursor !=null ){
            listkategori.clear();
            listidkategori.clear();
            listkategori.add("Pilih Kategori");
            listidkategori.add("-1");
            while(cursor.moveToNext()){
                listidkategori.add(cursor.getString(cursor.getColumnIndex("idkategori")));
                listkategori.add(cursor.getString(cursor.getColumnIndex("kategori")));
            }
        }
        ArrayAdapter adapterspinner=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listkategori);
        SpinnerKategori.setAdapter(adapterspinner);
//        Toast.makeText(this
//                ,""+listidkategori.size(), Toast.LENGTH_SHORT).show();
    }

    public boolean isUpdate(){
        return getIntent().getIntExtra("idjasa",-1) > -1;
    }
    public void add(){
        String jasa = edtJasa.getText().toString();
        String biaya = edtBiaya.getText().toString();
        String idkat = listidkategori.get(SpinnerKategori.getSelectedItemPosition());
//        String Biaya = Modul.unNumberFormat(edtBiaya.getText().toString());
        if(TextUtils.isEmpty(jasa) || idkat.equals("-1")){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else{
            db.exc("insert into tbljasa (jasa,satuan,idkategori) values ('"+ jasa +"','"+ biaya +"','"+idkat +"')");
            finish();
//            Toast.makeText(this, "Nambah", Toast.LENGTH_SHORT).show();
        }
    }

    public void update(){
        String idjasa = String.valueOf(getIntent().getIntExtra("idjasa",-1));
        String jasa = edtJasa.getText().toString();
        String biaya = edtBiaya.getText().toString();
        String idkat = listidkategori.get(SpinnerKategori.getSelectedItemPosition());
        if(TextUtils.isEmpty(jasa) || idkat.equals("-1")){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }else{
            db.exc("UPDATE tbljasa SET jasa='"+ jasa +"',satuan='"+ biaya +"',idkategori='"+ idkat +"' where idjasa='"+ idjasa +"'");
            finish();
        }
    }
}