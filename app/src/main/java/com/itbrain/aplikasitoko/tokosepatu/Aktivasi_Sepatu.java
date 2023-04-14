package com.itbrain.aplikasitoko.tokosepatu;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class Aktivasi_Sepatu extends AppCompatActivity {

    private TextView textView24;
    private TextView textView25;
    private ImageView imageView4;
    private Guideline guideline;
    private TextInputLayout textInputLayout3;
    private TextInputEditText eKodeAplikasi;
    private TextInputLayout textInputLayout4;
    private TextInputEditText eKodeLisensi;
    private TextView textView26;
    private CardView btnAktivasi;
    private TextView textView28;
    private CardView btnWA;
    private TextView textView29;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi_sepatu);
        initView();
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    boolean status = true;


    public void cek() throws UnsupportedEncodingException, NoSuchAlgorithmException {
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
    }

    private void initView() {
        textView24 = (TextView) findViewById(R.id.textView24);
        textView25 = (TextView) findViewById(R.id.textView25);
        //imageView4 = (ImageView) findViewById(R.id.imageView4);
        //guideline = (Guideline) findViewById(R.id.guideline);
        textInputLayout3 = (TextInputLayout) findViewById(R.id.textInputLayout3);
        eKodeAplikasi = (TextInputEditText) findViewById(R.id.eKodeAplikasi);
        textInputLayout4 = (TextInputLayout) findViewById(R.id.textInputLayout4);
        eKodeLisensi = (TextInputEditText) findViewById(R.id.eKodeLisensi);
        textView26 = (TextView) findViewById(R.id.textView26);
        btnAktivasi = (CardView) findViewById(R.id.btnAktivasi);
        textView28 = (TextView) findViewById(R.id.textView28);
        btnWA = (CardView) findViewById(R.id.btnWA);
        textView29 = (TextView) findViewById(R.id.textView29);
        eKodeLisensi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    if (status){
                        status = false;
                        status = true;
                    }
                }catch (Exception e){

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputLayout3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setText(eKodeAplikasi.getText());
                }
                Toast.makeText(getApplicationContext(), "Kode Aplikasi berhasil di copy", Toast.LENGTH_SHORT).show();
            }
        });
        eKodeAplikasi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setText(eKodeAplikasi.getText());
                }
                Toast.makeText(getApplicationContext(), "Kode Aplikasi berhasil di copy", Toast.LENGTH_SHORT).show();
            }
        });

        btnAktivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cek();
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    PackageManager packageManager = getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ "+6281357911226" +"&text=" + URLEncoder.encode("Halooo...\nAplikasi : "+ getResources().getString(R.string.app_name) + " \n dengan kode aplikasi : \n"+ eKodeAplikasi.getText().toString(), "UTF-8");
                    //i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }else {

                        Toast.makeText(Aktivasi_Sepatu.this, "Aplikasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {
                    Toast.makeText(Aktivasi_Sepatu.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
