package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.itbrain.aplikasitoko.R;

import java.io.File;
import java.util.ArrayList;

public class ActivityUtilitasRestoreLaundry extends AppCompatActivity {
    ListView listView;
    View v;
    String dirOut, dirIn;
    SharedPreferences getPref;
    SharedPreferences.Editor editPref;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_restore_laundry);

        v = this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali18);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPref = getSharedPreferences("dir", MODE_PRIVATE);
        dirOut = getPref.getString("dirRestore", Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Laundry & Keuangan/");
        if (Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString() + "/";
            String codename = this.getPackageName();
            ModulLaundry.setText(v, R.id.viewPathRestore, "Internal Storage/Android/data/" + codename + "/files/backup/");
            //only api 21 above
        } else {
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulLaundry.setText(v, R.id.viewPathRestore, "Internal Storage/Download");
            //only api 21 down
        }
        dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
        listView = (ListView) findViewById(R.id.listRestore);
        editPref = getPref.edit();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void readFile() {
        ArrayList arrayList = new ArrayList();
        ArrayAdapter arrayAdapter = new AdapterBackup(this, R.layout.list_item_restore_laundry, R.id.wadah, arrayList);
        listView.setAdapter(arrayAdapter);


        File dir = new File(dirOut);
        File[] isi = dir.listFiles(); // ini penting
        if (isi.length > 0) {
            for (int i = 0; i < isi.length; i++) {
                String nama = isi[i].getName();
                try {
                    String hasil = nama.substring(PrefLaundry.db.length());
                    if (nama.substring(0, (nama.length() - 16)).equals(PrefLaundry.db)) {
                        arrayList.add("Tgl : " + hasil + "__" + nama);
                    }
                } catch (Exception e) {

                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void u(final String db) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin untuk merestore data " + db + " ?");
        alertDialogBuilder.setPositiveButton("Restore",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (ModulLaundry.deleteFile(dirIn + PrefLaundry.db)) {
                            if (ModulLaundry.copyFile(dirOut, dirIn, db)) {
                                File a = new File(dirIn + db);
                                File b = new File(dirIn + PrefLaundry.db);
                                a.renameTo(b);
                                Toast.makeText(ActivityUtilitasRestoreLaundry.this, "Restore Berhasil", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ActivityUtilitasRestoreLaundry.this, LaundryMenuUtamaMaster.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Toast.makeText(ActivityUtilitasRestoreLaundry.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ActivityUtilitasRestoreLaundry.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Restore Data");
        alertDialog.show();
    }

    public void restore(View v) {
        String key = v.getTag().toString();
        try {
            u(key);
        } catch (Exception e) {
            Toast.makeText(this, "Restore Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void hapus(View v) {
        String db = v.getTag().toString();
        String path = dirOut;
        if (ModulLaundry.deleteFile(path + db)) {
            Toast.makeText(this, "Hapus Backup Berhasil", Toast.LENGTH_SHORT).show();
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

    public AdapterBackup(Context context, int vg, int id, ArrayList<String> records){
        super(context,vg, id, records);
        this.context=context;
        groupid=vg;
        this.records=records;

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);

        String[] row_items = records.get(position).split("__");

        TextView nama= (TextView) itemView.findViewById(R.id.text);
        Button b = (Button) itemView.findViewById(R.id.hapus) ;
        ConstraintLayout wadah = (ConstraintLayout) itemView.findViewById(R.id.wadah) ;

        nama.setText(row_items[0]);
        b.setTag(row_items[1]);
        wadah.setTag(row_items[1]);

        return itemView;
    }
}
