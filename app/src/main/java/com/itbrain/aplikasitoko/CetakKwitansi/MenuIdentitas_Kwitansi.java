package com.itbrain.aplikasitoko.CetakKwitansi;

import android.database.Cursor;
import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuIdentitas_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    TextInputEditText edt1,edt2,edt3,edt4,edt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitas_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);

        edt1 = (TextInputEditText)findViewById(R.id.edtNamaId);
        edt2 = (TextInputEditText)findViewById(R.id.edtAlamatId);
        edt3 = (TextInputEditText)findViewById(R.id.edtKotaId);
        edt4 = (TextInputEditText)findViewById(R.id.edtPegawaiId);
        edt5 = (TextInputEditText)findViewById(R.id.edtNoTlpId);

        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        Cursor c = db.sq("SELECT * FROM tblidentitas WHERE ididentitas=1 ");
        if(c.getCount() == 1){
            c.moveToNext();
            edt1.setText(c.getString(c.getColumnIndex("nama")));
            edt2.setText(c.getString(c.getColumnIndex("alamatid")));
            edt3.setText(c.getString(c.getColumnIndex("telp")));
            edt4.setText(c.getString(c.getColumnIndex("cap1")));
            edt5.setText(c.getString(c.getColumnIndex("cap2")));

        }
    }

    public void hapus(View view) {
        edt1.setText("");edt2.setText("");edt3.setText("");edt4.setText("");edt5.setText("");
        edt1.getText().clear();edt2.getText().clear();edt3.getText().clear();edt4.getText().clear();edt5.getText().clear();
    }

    public void simpan(View view) {
        String nama = edt1.getText().toString();
        String alamat = edt2.getText().toString();
        String telepon = edt3.getText().toString();
        String cap1 = edt4.getText().toString();
        String cap2 = edt5.getText().toString();


        if (edt1.getText().toString().isEmpty() || edt2.getText().toString().isEmpty() || edt3.getText().toString().isEmpty() || edt4.getText().toString().isEmpty() || edt5.getText().toString().isEmpty()){
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
                if (db.exc("UPDATE tblidentitas SET nama='"+nama+"',alamatid='"+alamat+"',telp='"+telepon+"',cap1='"+cap1+"',cap2='"+cap2+"' WHERE ididentitas=1")){
                    Toast.makeText(this, "Identitas Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
