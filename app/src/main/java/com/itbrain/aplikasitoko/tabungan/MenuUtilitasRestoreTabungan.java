package com.itbrain.aplikasitoko.tabungan;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

//import com.github.isabsent.filepicker.SimpleFilePickerDialog;

import com.itbrain.aplikasitoko.R;

import java.io.File;
import java.util.ArrayList;

public class MenuUtilitasRestoreTabungan extends AppCompatActivity{
    ListView listView;
    View v;
    String dirOut,dirIn;
    SharedPreferences getPref;
    SharedPreferences.Editor editPref;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilitas_restore_tabungan);
//        ModulTabungan.btnBack("Restore",getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliUtilitasRestoreTabungan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        v = this.findViewById(android.R.id.content);
        getPref=getSharedPreferences("dir",MODE_PRIVATE);
        if(Build.VERSION.SDK_INT >= 29) {
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v,R.id.viewPathRestore,"Internal Storage/Download");
            //only api 21 above
        }else{
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v,R.id.viewPathRestore,"Internal Storage/Download");
            //only api 21 down
        }
        dirIn = "/data/data/com.itbrain.aplikasitoko/databases/" ;
        listView = (ListView) findViewById(R.id.listRestore) ;
        editPref=getPref.edit();
        try {
            File file = new File(dirOut) ;
            if(!file.exists()){
                file.mkdirs() ;
            }
            readFile();
        } catch (Exception e){
            Toast.makeText(this, "Fetch Data gagal", Toast.LENGTH_SHORT).show();
        }
    }
    public void browse(View view) {

//    }
//
//    @Override
//    public void showListItemDialog(String title, String folderPath, SimpleFilePickerDialog.CompositeMode mode, String dialogTag) {
//        SimpleFilePickerDialog.build(folderPath,mode)
//                .title(title)
//                .neut(getResources().getString(R.string.batal))
//                .neg(getResources().getString(R.string.buka))
//                .pos(getResources().getString(R.string.pilihfolder))
//                .show(this,dialogTag);
//    }
//
//    @Override
//    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
//        switch (dialogTag){
//            case "PICK_FOLDER":
//                if (extras.containsKey(SimpleFilePickerDialog.SELECTED_SINGLE_PATH)){
//                    String selectedSinglePath = extras.getString(SimpleFilePickerDialog.SELECTED_SINGLE_PATH);
//                    if(!selectedSinglePath.isEmpty()){
//                        editPref.putString("dirRestore",selectedSinglePath+"/");
//                        editPref.apply();
//                        this.dirOut = getPref.getString("dirRestore",Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Aplikasi Tabungan Plus Keuangan/");
//                        ModulTabungan.setText(v,R.id.viewPathRestore,dirOut);
//                        readFile();
//                    }
//                }
//                break;
//        }
//        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void readFile() {
        ArrayList arrayList = new ArrayList() ;
        ArrayAdapter arrayAdapter = new AdapterBackup(this,R.layout.list_item_utilitas_restore_tabungan,R.id.wadah,arrayList) ;
        listView.setAdapter(arrayAdapter);


        File dir = new File(dirOut) ;
        File[] isi = dir.listFiles() ; // ini penting
        if(isi.length > 0){
            for(int i = 0 ; i < isi.length ; i++){
                String nama = isi[i].getName() ;
                try {
                    String hasil = nama.substring(PrefTabungan.db.length()) ;
                    if(nama.substring(0,(nama.length()-16)).equals(PrefTabungan.db)){
                        arrayList.add("Tgl : " + hasil+"__"+nama);
                    }
                }catch (Exception e){

                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
    public void restore(View v){
        String key = v.getTag().toString() ;
        try {
            u(key) ;
        }catch (Exception e){
            Toast.makeText(this, "Restore Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void hapus(View v){
        String db = v.getTag().toString() ;
        String path = dirOut ;
        if(ModulTabungan.deleteFile(path+db)){
            Toast.makeText(this, "Hapus Backup Berhasil", Toast.LENGTH_SHORT).show();
            readFile();
        } else {
            Toast.makeText(this, "Hapus Backup Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void u(final String db){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin untuk merestore data " + db+" ?");
        alertDialogBuilder.setPositiveButton("Restore",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(ModulTabungan.deleteFile(dirIn+PrefTabungan.db)){
                            if(ModulTabungan.copyFile(dirOut,dirIn,db)){
                                File a = new File(dirIn+db) ;
                                File b = new File(dirIn+PrefTabungan.db) ;
                                a.renameTo(b) ;
                                Toast.makeText(MenuUtilitasRestoreTabungan.this, "Restore Berhasil, Aplikasi terestart", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MenuUtilitasRestoreTabungan.this, MainActivityTabungan.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Toast.makeText(MenuUtilitasRestoreTabungan.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MenuUtilitasRestoreTabungan.this, "Restore Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Restore Data");
        alertDialog.show();
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


