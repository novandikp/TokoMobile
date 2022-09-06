package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuIdentitasLaundry extends AppCompatActivity {
    Button Simpan;
    EditText namatoko,alamattoko,notelptoko;
    Intent intent;
    SharedPreferences sharedPreferences;
    DatabaseLaundry db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitaslaundry);
        Simpan = findViewById(R.id.Simpan);
        namatoko = findViewById(R.id.NamaToko);
        alamattoko = findViewById(R.id.AlamatToko);
        notelptoko = findViewById(R.id.NotelpToko);
//        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//        sharedPreferences.contains("NamaToko");
//        sharedPreferences.contains("AlamatToko");
//        sharedPreferences.contains("NotelpToko");

//        Simpan.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String NamaToko = namatoko.getText().toString();
//                String AlamatToko = alamattoko.getText().toString();
//                String NotelpToko = notelptoko.getText().toString();
//
//                if (NamaToko.equals("") && AlamatToko.equals("") && NotelpToko.equals("")) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("NamaToko", NamaToko);
//                    editor.putString("AlamatToko", AlamatToko);
//                    editor.putString("NotelpToko", NotelpToko);
//                    editor.apply();
//                    intent = new Intent(MenuIdentitasLaundry.this, LaundryMenuUtamaMaster.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(MenuIdentitasLaundry.this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


















//        NamaToko=findViewById(R.id.NamaToko);
//        AlamatToko=findViewById(R.id.AlamatToko);
//        NomorTelepon=findViewById(R.id.NotelpToko);
//    }
//
//    public void buttonSenderPressed(View v){
//        Intent intent = new Intent(MenuIdentitasLaundry.this,LaundryMenuUtamaMaster.class);
//        intent.putExtra("key_nama", NamaToko.getText().toString());
//        intent.putExtra("key_alamat", AlamatToko.getText().toString());
//        intent.putExtra("key_telp", NomorTelepon.getText().toString());
//        startActivity(intent);
//        finish();
//    }
//    public void buttonSenderPressed(View view) {
//        Intent intent = new Intent( MenuIdentitasLaundry.this, LaundryMenuMaster.class);
//        intent.putExtra("key_kirim", NamaToko.getText().toString());
//        startActivity(intent);
//        finish();
//    }


//        Simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    String nama = NamaToko.getText().toString();
//                    if (NamaToko != null && nama != ""){
//                        Intent i = new Intent(MenuIdentitasLaundry.this, LaundryMenuUtamaMaster.class);
//                        i.putExtra(key_name, nama);
//                        startActivity(i);
//
//                    } else {
//                        Toast.makeText(getApplication(), "YOU NEED TO FILL YOUR NAME",Toast.LENGTH_SHORT);
//                    }
//
//                } catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(getApplication(), "ERROR, TRY AGAIN !",Toast.LENGTH_SHORT);
//                }
//
//            }
//        });
//}
//        Simpan = findViewById(R.id.Simpan);
//        NamaToko = findViewById(R.id.NamaToko);
//        AlamatToko = findViewById(R.id.AlamatToko);
//        NomorTelepon = findViewById(R.id.NotelpToko);
//        Caption1 = findViewById(R.id.Caption1);
//        Caption2 = findViewById(R.id.Caption2);
//        Caption3 = findViewById(R.id.Caption3);
////        db = new DatabaseLaundry(this);
//        Simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                Intent i = new Intent(MenuIdentitasLaundry.this,LaundryMenuUtamaMaster.class);
//                getIntent().putExtra("FOO", txtnamatoko);
////                String CaptionSatu = Caption1.getText().toString();
////                String CaptionDua = Caption2.getText().toString();
////                String CaptionTiga = Caption3.getText().toString();
//                startActivity(i);
//                finish();
//            }
////            { add(); }
//        });
//    }
//    public void add(){
//        if(TextUtils.isEmpty(txtnamatoko)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(txtalamattoko)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(txtnomortelp)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(CaptionSatu)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(CaptionDua)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(CaptionTiga)){
//            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            db.exc("insert into tblidentitas (namatoko,alamattoko,notelptoko,caption_1,caption_2,caption_3) values ('"+ txtnamatoko +"','"+ txtalamattoko +"','"+ txtnomortelp +"','"+ CaptionSatu +"','"+ CaptionDua +"','"+ CaptionTiga +"')");
//            finish();
//        }
//    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuIdentitasLaundry.this, LaundryMenuMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}