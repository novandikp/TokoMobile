package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuDaftarJasaLaundry extends AppCompatActivity {

    Spinner spinnerKategori;
    RecyclerView recyclerView;
    List<getterJasa> DaftarJasa;
    AdapterListJasa adapter;
    DatabaseLaundry db;
    String kat;

    Boolean inAppStatus;
    Cursor countMaster,countTransaksi;
//    BillingProcessor bp;
    boolean status;
    SharedPreferences.Editor edit;
    SharedPreferences pref2;
//    IAPHelper iapHelper;

//    String belisku = Modul.produkid;
//    private List<String> skuList = Arrays.asList(Modul.produkid,Modul.produkid2);
//    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.menu_daftar_jasa_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new DatabaseLaundry(this);
        Modul.btnBack("Daftar Jasa",getSupportActionBar());
//        bp = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg8Gb+Bp63mo57C8j4/ip/Z1F9NjS+6p4WM4wz043FiVLJYI4ddPx3g5uNZbNgmOHhWj6jYvvoaly+yPOiDkawugOjmOdd4RU6V3xRxwvHmQYd2doftngfqK0+mG4ueBA8TkuUS85TbVFLzYTPpuS78HBS/Q3QFcm1gyqEdKmYuRhT0I7P/7gHwny72XGImKdjc/rKzH3ClXg+E51UQ97+4GZ1uHM3CzLg9gYTHkyf/1Xd2C+XQz2vbPFpbM7Dq4o9HiBYh1eGkf9Ex1WW7MJf3BRmTwQJsSz+x4aWz2lCFxGJoLVaUxx25XY2z7FFZYl49OVsYxvqGEjIa3WpLnQiQIDAQAB", this);
//        bp = BillingProcessor.newBillingProcessor(this, null, this);
//        bp.initialize();
//        iapHelper = new IAPHelper(this,this,skuList);
//        checkPurchase();

        spinnerKategori = (Spinner) findViewById(R.id.SpinnerKategori);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kat=db.getIdKategori().get(position);
                getJasa("",kat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getKategoriData();
        final EditText edtCari = (EditText)findViewById(R.id.Pencarian);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = edtCari.getText().toString();
                getJasa(keyword,kat);
            }
        });




    }

//    public void checkPurchase(){
//
//        if (ActivitySplash.status){
//            status =true;
//
//
//        }else {
//            status=false;
//
//        }
//
//
//    }

//    private void DialogBeli() {
//        startActivity(new Intent(this, Aktivasi.class));
//    }

    public  void getJasa(String keyword,String kategori){
        DaftarJasa = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.DaftarJasa);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterListJasa(this,DaftarJasa,Boolean.TRUE);
        recyclerView.setAdapter(adapter);
        String q;

        if (TextUtils.isEmpty(keyword)){
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa";
            }else {
                q="SELECT * FROM qjasa WHERE idkategori="+kategori;
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori="+kategori;
            }
        }
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarJasa.add(new getterJasa(
                        Modul.getInt(c,"idjasa"),
                        Modul.getInt(c,"idkategori"),
                        Modul.getString(c,"kategori"),
                        Modul.getString(c,"jasa"),
                        Modul.getString(c,"biaya"),
                        Modul.getString(c,"satuan")
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void getKategoriData(){
        DatabaseLaundry db = new DatabaseLaundry(this);
        List<String> labels = db.getKategori();

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(data);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuDaftarJasaLaundry.this, LaundryMenuMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJasa("","0");
        spinnerKategori.setSelection(0);
        countMaster=db.sq("SELECT * FROM tbljasa");
        countTransaksi=db.sq("SELECT * FROM tbllaundry WHERE total>0");

        if (countMaster.getCount()<10&&countTransaksi.getCount()<10){
            inAppStatus=true;
        }else {
            inAppStatus=false;
        }
    }

    public void Tambah(View view) {
        Cursor kategori  = db.sq(Query.select("tblkategori"));

        if (Modul.getCount(kategori) == 0 ){
            Toast.makeText(this, "Kategori masih kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inAppStatus || (status && !inAppStatus)){
            Intent i = new Intent(this,MenuUbahJasaLaundry.class);
            startActivity(i);
        }else {
//            DialogBeli();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        edit.putString(Modul.encrypt("status"),Modul.encrypt("premium#!"+ Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID)));
//        edit.apply();
//        Intent i = new Intent(this,ActivityMasterJasa.class);
//        startActivity(i);
//        Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, @Nullable Throwable error) {
//
//    }
//
//    @Override
//    public void onBillingInitialized() {
//
//    }
//
//    private void launch(String sku){
//        if(!skuDetailsHashMap.isEmpty())
//            iapHelper.launchBillingFLow(skuDetailsHashMap.get(sku));
//    }
//
//    @Override
//    public void onSkuListResponse(HashMap<String, SkuDetails> skuDetails) {
//        skuDetailsHashMap = skuDetails;
//    }
//
//    @Override
//    public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {
//        if (purchasedItems != null) {
//            for (Purchase purchase : purchasedItems) {
//                //Update UI and backend according to purchased items if required
//                // Like in this project I am updating UI for purchased items
//                String sku = purchase.getSku();
//                if(sku.equals(Modul.produkid)){
//                    belisku = Modul.produkid2;
//                }else if(sku.equals(Modul.produkid2)){
//
//                    edit.putString(Modul.encrypt("status"),Modul.encrypt("premium#!"+ Settings.Secure.getString(this.getContentResolver(),
//                            Settings.Secure.ANDROID_ID)));
//                    edit.apply();
//                    checkPurchase();
//
//
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public void onPurchaseCompleted(Purchase purchase) {
//        edit.putString(Modul.encrypt("status"),Modul.encrypt("premium#!"+ Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID)));
//        edit.apply();
//        checkPurchase();
//        Intent i = new Intent(this,ActivityMasterJasa.class);
//        startActivity(i);
//        Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (iapHelper != null)
//            iapHelper.endConnection();
//    }
//
//    private void updatePurchase(Purchase purchase){
//        String sku = purchase.getSku();
//
//        edit.putString(Modul.encrypt("status"),Modul.encrypt("premium#!"+ Settings.Secure.getString(this.getContentResolver(),
//                Settings.Secure.ANDROID_ID)));
//        edit.apply();
//        Intent i = new Intent(this,ActivityMasterJasa.class);
//        startActivity(i);
//        Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();
//
//
//    }
}
class AdapterListJasa extends RecyclerView.Adapter<AdapterListJasa.JasaViewHolder>{
    Context ctxAdapter;
    List<getterJasa> data;
    Boolean showopt;

    public  AdapterListJasa(Context ctx, List<getterJasa> viewData, Boolean showopt) {
        this.ctxAdapter = ctx;
        this.data = viewData;
        this.showopt=showopt;
    }

    @NonNull
    @Override
    public JasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.laundryitemdaftarjasa,viewGroup,false);
        return new JasaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JasaViewHolder holder, final int i) {
        final getterJasa getter = data.get(i);
        holder.nama.setText(getter.getJasa());
        holder.biaya.setText("Rp."+Modul.removeE(getter.getBiaya()));
        String pc = "pc";
        String kg = "kg";
        String m2 = "m2";

        if(pc.equals(getter.getSatuan())){
            holder.satuan.setText("/Pcs");
        }else if (kg.equals(getter.getSatuan())){
            holder.satuan.setText("/Kg");
        }else if (m2.equals(getter.getSatuan())){
            holder.satuan.setText("/M²");
        }
        holder.kategori.setText(getter.getKategori());
        if (showopt){
            holder.opt.setVisibility(View.VISIBLE);
        }else {
            holder.opt.setVisibility(View.GONE);
        }
        holder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                popupMenu.inflate(R.menu.option_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.ubah:
                                Intent intent = new Intent(ctxAdapter,MenuUbahJasaLaundry.class);
                                intent.putExtra("idjasa",getter.getIdJasa());
                                intent.putExtra("idkategori",getter.getIdKategori());
                                intent.putExtra("jasa",getter.getJasa());
                                intent.putExtra("biaya",Modul.removeE(getter.getBiaya()));
                                intent.putExtra("satuan",getter.getSatuan());
                                intent.putExtra("kategori",getter.getKategori());
                                ctxAdapter.startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseLaundry db = new DatabaseLaundry(ctxAdapter);
                                        if (db.deleteJasa(getter.getIdJasa())){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctxAdapter, "Delete jasa "+getter.getJasa()+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctxAdapter, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+getter.getJasa());
                                builder.setMessage("Anda yakin ingin menghapus "+getter.getJasa()+" dari data jasa");
                                builder.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class JasaViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,satuan,kategori;
        ImageView opt;
        public JasaViewHolder(@NonNull View itemView) {
            super(itemView);
            nama= itemView.findViewById(R.id.edtDaftarKategori);
            biaya= itemView.findViewById(R.id.edtSatuan);
            satuan= itemView.findViewById(R.id.edtjenisSatuan);
            kategori= itemView.findViewById(R.id.tvKategoriJasa);
            opt = itemView.findViewById(R.id.optMuncul);
        }
    }
}
class getterJasa{
    private int idJasa,idKategori;
    private String jasa,satuan,kategori,biaya;

    public getterJasa(int idJasa, int idKategori, String kategori, String jasa, String biaya, String satuan) {
        this.idJasa = idJasa;
        this.idKategori = idKategori;
        this.kategori = kategori;
        this.jasa = jasa;
        this.biaya = biaya;
        this.satuan = satuan;

    }

    public int getIdJasa() {
        return idJasa;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public String getBiaya() {
        return biaya;
    }

    public String getJasa() {
        return jasa;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getKategori() {
        return kategori;
    }




}