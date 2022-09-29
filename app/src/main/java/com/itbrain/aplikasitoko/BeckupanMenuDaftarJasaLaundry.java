//package com.itbrain.aplikasitoko;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.itbrain.aplikasitoko.Model.JasaLaundry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MenuDaftarJasaLaundry extends AppCompatActivity {
//    Spinner SpinnerKategori;
//    ArrayList<JasaLaundry> datajasa;
//    ArrayList<String> listkategori;
//    ArrayList<String> listidkategori;
//    RecyclerView recyclerView;
//    JasaLaundryAdapter adapter;
//    DatabaseLaundry db;
//    EditText pencarian;
////    implements PopupMenu.OnMenuItemClickListener
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.menu_daftar_jasa_laundry);
//        pencarian = findViewById(R.id.Pencarian);
//        SpinnerKategori = findViewById(R.id.SpinnerKategori);
//        recyclerView = findViewById(R.id.DaftarJasa);
//        listidkategori = new ArrayList<>();
//        listkategori = new ArrayList<>();
//        datajasa = new ArrayList<>();
//        db = new DatabaseLaundry(this);
//        getKategori();
//        getKategoriData();
//        adapter = new JasaLaundryAdapter(datajasa,this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//        pencarian.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                getData();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        SpinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                db.getIdKategori().get(position);
//                getData();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    public  void getJasa(String keyword,String kategori){
//        datajasa = new ArrayList<>();
//        recyclerView = (RecyclerView) findViewById(R.id.DaftarJasa);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new JasaLaundryAdapter(this,datajasa,Boolean.TRUE);
//        recyclerView.setAdapter(adapter);
//        String q;
//
//        if (TextUtils.isEmpty(keyword)){
//            if (kategori.equals("0")){
//                q="SELECT * FROM qjasa";
//            }else {
//                q="SELECT * FROM qjasa WHERE idkategori="+kategori;
//            }
//        }else {
//            if (kategori.equals("0")){
//                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
//            }else {
//                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori="+kategori;
//            }
//        }
//        Cursor c=db.sq(q);
//        if (Modul.getCount(c)>0){
//            while(c.moveToNext()){
//                datajasa.add(new JasaLaundry(
//                        Modul.getInt(c,"idjasa"),
//                        Modul.getInt(c,"idkategori"),
//                        Modul.getString(c,"kategori"),
//                        Modul.getString(c,"jasa"),
//                        Modul.getString(c,"satuan")
//                ));
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
//
////    public void popMenu(View v){
////        datakategori.remove(recyclerView.getAdapter().toString());
////        adapter = new KategoriLaundryAdapter(datakategori,this);
////        PopupMenu popupMenu = new PopupMenu(this, v);
////        PopupMenu.OnMenuItemClickListener popMenu;
////        popupMenu.inflate(R.menu.option_item);
////        popupMenu.show();
////    }
//
//
////    public boolean onMenuItemClick(MenuItem menuItem) {
////        switch (menuItem.getItemId()){
////            case R.id.listItem:
////                Toast.makeText(this, "Item 1 di Klik", Toast.LENGTH_SHORT).show();
////            case R.id.listItem2:
////                Toast.makeText(this, "Item 2 di Klik", Toast.LENGTH_SHORT).show();
////        }
////        return false;
////    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData();
//    }
//
////    public void dummy(){
////        datakategori.clear();
////        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
////        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
////        datakategori.add(new Kategori(1, cursor.getString(cursor.getColumnIndex("pegawai")), cursor.getString(cursor.getColumnIndex("alamatpegawai")), "cucibasah"));
////    }
//
//    public void getData(){
//        Cursor cursor = db.sq("select * from tbljasa where idjasa != 0 and jasa like '%"+ pencarian.getText().toString() +"%'");
//        if(cursor!=null){
//            datajasa.clear();
//            while(cursor.moveToNext()){
//                datajasa.add(new JasaLaundry(cursor.getInt(cursor.getColumnIndex("idjasa")),cursor.getInt(cursor.getColumnIndex("idkategori")),cursor.getString(cursor.getColumnIndex("jasa")),cursor.getString(cursor.getColumnIndex("biaya")), cursor.getString(cursor.getColumnIndex("satuan"))));
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    public void getKategori() {
//        Cursor cursor = db.sq("select * from tblkategori where idkategori != 0 and kategori like '%"+ pencarian.getText().toString() +"%'");
//        if(cursor !=null ){
//            listkategori.clear();
//            listidkategori.clear();
//            listkategori.add("Semua Kategori");
//            while(cursor.moveToNext()){
//                listkategori.add(cursor.getString(cursor.getColumnIndex("kategori")));
//                listidkategori.add(cursor.getString(cursor.getColumnIndex("idkategori")));
//            }
//        }
//
//        ArrayAdapter adapterspinner=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listkategori);
//        SpinnerKategori.setAdapter(adapterspinner);
//    }
//
//    private void getKategoriData(){
//        DatabaseLaundry db = new DatabaseLaundry(this);
//        List<String> labels = db.getKategori();
//
//        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
//        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SpinnerKategori.setAdapter(data);
//    }
//
//    public void Simpan(View view) {
//        Intent intent = new Intent(MenuDaftarJasaLaundry.this, MenuUbahJasaLaundry.class);
//        startActivity(intent);
//    }
//
//    public void Kembali(View view) {
//        Intent intent = new Intent(MenuDaftarJasaLaundry.this, LaundryMenuMaster.class);
//        startActivity(intent);
//    }
//}
//
//class JasaLaundryAdapter extends RecyclerView.Adapter<JasaLaundryAdapter.ViewHolder>{
//
//    ArrayList<JasaLaundry>jasas;
//    Context context;
//
//    public JasaLaundryAdapter(ArrayList<JasaLaundry> jasas, Context context) {
//        this.jasas = jasas;
//        this.context = context;
//    }
//
//    public JasaLaundryAdapter(MenuDaftarJasaLaundry menuDaftarJasaLaundry, ArrayList<JasaLaundry> datajasa, Boolean aTrue) {
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laundryitemdaftarjasa,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder,int Adapter) {
//        final JasaLaundry jasa = jasas.get(Adapter);
//        holder.edtJasa.setText(jasa.getJasa());
//        holder.edtSatuan.setText(jasa.getSatuan());
//        holder.optMuncul.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(context,view);
//                popupMenu.inflate(R.menu.option_item);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()){
//                            case R.id.ubah:
//                                Intent intent = new Intent(context,MenuUbahJasaLaundry.class);
//                                intent.putExtra("idjasa",jasa.getIdJasa());
//                                intent.putExtra("jasa",jasa.getJasa());
//                                intent.putExtra("satuan",jasa.getSatuan());
//                                intent.putExtra("idkategori",jasa.getIdKategori());
//                                context.startActivity(intent);
//                                break;
//                            case R.id.hapus:
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        DatabaseLaundry db = new DatabaseLaundry(context);
//                                        if (db.deleteJasa(jasa.getIdJasa())){
//                                            jasas.remove(Adapter);
//                                            notifyItemChanged(Adapter);
//                                            Toast.makeText(context, "Delete "+ jasa.getJasa()+" berhasil", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            Toast.makeText(context, "Gagal menghapus "+ jasa.getJasa(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                });
//                                builder.setTitle("Hapus "+jasa.getJasa());
//                                builder.setMessage("Anda yakin ingin menghapus "+jasa.getJasa());
//                                builder.show();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return jasas.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView edtJasa,edtSatuan;
//        ImageView optMuncul;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            edtJasa = itemView.findViewById(R.id.edtDaftarKategori);
//            edtSatuan = itemView.findViewById(R.id.edtSatuan);
//            optMuncul = itemView.findViewById(R.id.optMuncul);
//        }
//    }
//}