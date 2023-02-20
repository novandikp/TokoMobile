package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;
import java.util.ArrayList;

public class Restore_Data_Kasir_ extends AppCompatActivity {

    ListView listView;
    FConfigKasir config;
    String dirOut, dirIn, browse;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore_data_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        v = this.findViewById(android.R.id.content);

        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString() + "/";
            String codename = this.getPackageName();
            FFunctionKasir.setText(v, R.id.ePath, "Internal Storage/Android/data/" + codename + "/files/backup/");
            //only api 21 above
        } else {
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FFunctionKasir.setText(v, R.id.ePath, "Internal Storage/Download");
            //only api 21 down
        }

        dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
        listView = (ListView) findViewById(R.id.listViewRestore);
        try {
            File file = new File(dirOut);
            if (!file.exists()) {
                file.mkdirs();
            }
            readFile();
        } catch (Exception e) {
            Toast.makeText(this, "Fetch Data gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void setText() {
        FFunctionKasir.setText(v, R.id.ePath, "Internal Storage/Download/");
    }

    public void readFile() {
        ArrayList arrayList = new ArrayList();
        ArrayAdapter arrayAdapter = new AdapterBackup(this, R.layout.item_view_restore, R.id.wadahRestore, arrayList);
        listView.setAdapter(arrayAdapter);
        FConfigKasir config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));

        File dir = new File(dirOut);
        File[] isi = dir.listFiles(); // ini penting
        if (isi.length > 0) {
            for (int i = 0; i < isi.length; i++) {
                String nama = isi[i].getName();
                try {
                    String hasil = nama.substring(config.getDb().length());
                    if (nama.substring(0, (nama.length() - 16)).equals(config.getDb())) {
                        arrayList.add("Tgl : " + hasil + "__" + nama);
                    }
                } catch (Exception e) {

                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void open(final String db) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Restore Database " + db);
        alertDialogBuilder.setPositiveButton("Restore",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (FFunctionKasir.deleteFile(dirIn + config.getDb())) {
                            if (FFunctionKasir.copyFile(dirOut, dirIn, db)) {
                                File a = new File(dirIn + db);
                                File b = new File(dirIn + config.getDb());
                                a.renameTo(b);

                                Toast.makeText(Restore_Data_Kasir_.this, "Restore Berhasil, Silahkan Restart Aplikasi", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Restore_Data_Kasir_.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Restore_Data_Kasir_.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void restore(View v) {
        String key = v.getTag().toString();
        try {
            open(key);
        } catch (Exception e) {
            Toast.makeText(this, "Restore Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void hapus(View v) {
        String db = v.getTag().toString();
        String path = Environment.getExternalStorageDirectory().toString() + "/Download/";
        if (FFunctionKasir.deleteFile(path + db)) {
            Toast.makeText(this, "Hapus Backup Berhasil" + db, Toast.LENGTH_SHORT).show();
            readFile();
        } else {
            Toast.makeText(this, "Hapus Backup Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}

    class AdapterBackup extends ArrayAdapter<String> {

        int groupid;
        ArrayList<String> records;
        Context context;

        public AdapterBackup(Context context, int vg, int id, ArrayList<String> records) {
            super(context, vg, id, records);
            this.context = context;
            groupid = vg;
            this.records = records;

        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(groupid, parent, false);

            String[] row_items = records.get(position).split("__");

            TextView nama = (TextView) itemView.findViewById(R.id.text);
            Button b = (Button) itemView.findViewById(R.id.hapusRestore);
            ConstraintLayout wadah = (ConstraintLayout) itemView.findViewById(R.id.wadahRestore);

            nama.setText(row_items[0]);
            b.setTag(row_items[1]);
            wadah.setTag(row_items[1]);

            return itemView;

        }
    }

