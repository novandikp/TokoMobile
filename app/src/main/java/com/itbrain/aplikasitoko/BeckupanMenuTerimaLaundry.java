//package com.itbrain.aplikasitoko;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//public class MenuTerimaLaundry extends AppCompatActivity {
//    EditText edtJumlahJasa;
//    ImageView btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;
//    TextView tvSatuan;
//    View v;
//    DatabaseLaundry db;
//
//    int year, month, day ;
//    Calendar calendar ;
//
//    String faktur="00000000";
//    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
//    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="",status,updateFaktur;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Modul.btnBack("Menu Terima",getSupportActionBar());
//        setContentView(R.layout.menuterimalaundry);
//
//        db=new DatabaseLaundry(this);
//        v = this.findViewById(android.R.id.content);
//
//        calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        updateFaktur = getIntent().getStringExtra("faktur");
//        status = getIntent().getStringExtra("status");
//        String status = "terima";
//        if (status.equals("terima")){
//            getFaktur();
//            Modul.setText(v, R.id.TanggalMulai,Modul.getDate("dd/MM/yyyy"));
//            Modul.setText(v, R.id.TanggalKembali,Modul.getDate("dd/MM/yyyy"));
//            Cursor c1 = db.sq(Query.selectwhere("tblpegawai")+Query.sWhere("idpegawai","0"));
//            c1.moveToFirst();
//            Cursor c2 = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan","0"));
//            c2.moveToFirst();
//            tIdpegawai=Modul.getInt(c1,"idpegawai");
//            tnPegawai=Modul.getString(c1,"pegawai");
//            tIdpelanggan=Modul.getInt(c2,"idpelanggan");
//            tnPelanggan=Modul.getString(c2,"pelanggan");
//        }else if (status.equals("update")){
//            Modul.setText(v,R.id.edtFaktur,updateFaktur);
//            loadCart();
//            String q="SELECT * FROM qlaundry WHERE faktur='"+updateFaktur+"'";
//            Cursor c=db.sq(q);
//            c.moveToNext();
//
//            Modul.setText(v,R.id.TanggalMulai,Modul.dateToNormal(Modul.getString(c,"tgllaundry")));
//            Modul.setText(v,R.id.TanggalKembali,Modul.dateToNormal(Modul.getString(c,"tglselesai")));
//            tIdpegawai=Modul.getInt(c,"idpegawai");
//            tnPegawai=Modul.getString(c,"pegawai");
//            tIdpelanggan=Modul.getInt(c,"idpelanggan");
//            tnPelanggan=Modul.getString(c,"pelanggan");
//            getTotal();
//        }
//
//        btnCari();
//
//        btnTglTerima = findViewById(R.id.ibtnTglTerima);
//        btnTglTerima.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDate(1);
//            }
//        });
//        btnTglSelesai = findViewById(R.id.ibtnTglSelesai);
//        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDate(2);
//            }
//        });
//
//        btnAddJ= findViewById(R.id.ibtnPlus);
//        btnAddJ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tJumlah=tJumlah+1;
//                setJumlah(tJumlah,tBiaya);
//            }
//        });
//        btnRemoveJ= findViewById(R.id.ibtnMinus);
//        btnRemoveJ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tJumlah=tJumlah-1;
//                setJumlah(tJumlah,tBiaya);
//            }
//        });
//        edtJumlahJasa=(EditText)findViewById(R.id.edtJumlah);
//        edtJumlahJasa.addTextChangedListener(new NumberTextWatcher(edtJumlahJasa,new Locale("in","ID"),2));
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        Intent intent = new Intent (MenuTerimaLaundry.this, LaundryMenuTransaksi.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        return true;
//    }
//
//    private void getFaktur(){
////        db.exc("INSERT INTO tbllaundry (total) VALUES (0)");
//        List<Integer> idLaundry = new ArrayList<Integer>();
//        String q="SELECT idlaundry FROM tbllaundry";
//        Cursor c = db.sq(q);
//        if (c.moveToNext()){
//            do {
//                idLaundry.add(Modul.getIntFromCol(c,0));
//            }while (c.moveToNext());
//        }
//        String tempFaktur="";
//        int IdFaktur=0;
//        if (Modul.getCount(c)==0){
//            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
//        }else {
//            IdFaktur = idLaundry.get(Modul.getCount(c)-1)+1;
//            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
//        }
//        Modul.setText(v,R.id.edtFaktur,tempFaktur);
//    }
//    private void setEdit(String pegawai,String pelanggan,String jasa,String biaya,String kategori,String satuan){
//        Modul.setText(v,R.id.namaPegawaiLaundry,pegawai);
//        Modul.setText(v,R.id.NamaPelanggan,pelanggan);
//        Modul.setText(v,R.id.Jasa,jasa);
//        Modul.setText(v,R.id.HargaJasa,biaya);
//        tvSatuan=(TextView) findViewById(R.id.tvSatuan);
//        if (tSatuan.equals("pc")){
//            tvSatuan.setText("/Pcs");
////            tvSatuan.setVisibility(View.VISIBLE);
//        }else if (tSatuan.equals("kg")){
//            tvSatuan.setText("/Kg");
////            tvSatuan.setVisibility(View.VISIBLE);
//        }else if (tSatuan.equals("m2")){
//            tvSatuan.setText("/M²");
////            tvSatuan.setVisibility(View.VISIBLE);
//        }
//    }
//    private void btnCari(){
//        btnCariPegawai= findViewById(R.id.ibtnCariPegawai);
//        btnCariPegawai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
//                i.putExtra("cari","pegawai");
//                startActivityForResult(i,1000);
//            }
//        });
//        btnCariPelanggan= findViewById(R.id.ibtnCariPelanggan);
//        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
//                i.putExtra("cari","pelanggan");
//                startActivityForResult(i,2000);
//            }
//        });
//        btnCariJasa= findViewById(R.id.ibtnJasa);
//        btnCariJasa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
//                i.putExtra("cari","jasa");
//                startActivityForResult(i,3000);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==1000){
//            tIdpegawai=data.getIntExtra("idpegawai",0);
//            tnPegawai=data.getStringExtra("namapegawai");
//        }else if (resultCode==2000){
//            tIdpelanggan=data.getIntExtra("idpelanggan",0);
//            tnPelanggan=data.getStringExtra("namapelanggan");
//        }else if (resultCode==3000){
//            tIdjasa=data.getIntExtra("idjasa",0);
//            tIdkategori=data.getIntExtra("idkategori",0);
//            tnJasa=data.getStringExtra("namajasa");
//            tKategori=data.getStringExtra("kategorijasa");
//            tBiaya=data.getStringExtra("biayajasa");
//            tSatuan=data.getStringExtra("satuanjasa");
//        }
//
//    }
//
//    public void setDate(int i) {
//        showDialog(i);
//    }
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        // TODO Auto-generated method stub
//        if (id == 1) {
//            return new DatePickerDialog(this, dTerima, year, month, day);
//        }else if (id==2){
//            return new DatePickerDialog(this, dSelesai, year, month, day);
//        }
//        return null;
//    }
//    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
//            Modul.setText(v, R.id.TanggalMulai, Modul.setDatePickerNormal(thn,bln+1,day)) ;
//        }
//    };
//    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
//            Modul.setText(v, R.id.TanggalKembali, Modul.setDatePickerNormal(thn,bln+1,day)) ;
//        }
//    };
//
//    private void setJumlah(Integer jumlah, String biaya){
//        Modul.setText(v,R.id.edtJumlah,String.valueOf(jumlah));
//        if (biaya.equals("")){
////            btnAddJ.setVisibility(View.INVISIBLE);
//        }else {
////            btnAddJ.setVisibility(View.VISIBLE);
//        }
//        if (jumlah<1){
////            btnRemoveJ.setVisibility(View.INVISIBLE);
//        }else {
////            btnRemoveJ.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        String kat="";
//        tJumlah=0;
//        Modul.setText(v,R.id.edtJumlah,"0");
//        kat=tKategori;
//        if (!kat.equals("")){
//            kat=tKategori+" - "+tnJasa;
//        }
//        if (!tBiaya.equals("")){
//            setJumlah(0,tBiaya);
//        }
//        setEdit(tnPegawai,tnPelanggan,kat,tBiaya,tKategori,tSatuan);
//    }
//    private void keluar(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.create();
//        builder.setTitle("Anda yakin ingin keluar?");
//        builder.setMessage("Setelah keluar data faktur ini akan hilang");
//        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String faktur=Modul.getText(v,R.id.edtFaktur);
//                db.exc("DELETE FROM tbllaundry WHERE idlaundry="+Integer.valueOf(faktur));
//                finish();
//            }
//        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        }).show();
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        String status = "terima";
//        if (status.equals("terima")){
//            keluar();
//        }
//        else if (status.equals("update")){
//            finish();
//        }
//    }
//
//    public String convertDate(String date){
//        String[] a = date.split("/") ;
//        return a[2]+a[1]+a[0];
//    }
//
//    public void insertTransaksi(View view) {
//        String eFaktur = Modul.getText(v,R.id.edtFaktur);
//        String eTglT = Modul.getText(v,R.id.TanggalMulai);
//        String eTglS = Modul.getText(v,R.id.TanggalKembali);
//        String eNPegawai = Modul.getText(v,R.id.namaPegawaiLaundry);
//        String eNPelanggan = Modul.getText(v,R.id.NamaPelanggan);
//        String eJasa = Modul.getText(v,R.id.edtJasa);
//        String eHarga = Modul.unNumberFormat(Modul.getText(v,R.id.HargaJasa));
//        String eJumlah = Modul.unNumberFormat(Modul.getText(v,R.id.edtJumlah));
//        String eKet = Modul.getText(v,R.id.edtKeterangan).replace("\n","  ");
//        if (TextUtils.isEmpty(eTglS) || TextUtils.isEmpty(eNPegawai) || TextUtils.isEmpty(eNPelanggan) || TextUtils.isEmpty(eJasa) || TextUtils.isEmpty(eJumlah) || Modul.strToDouble(eHarga)==0 || Modul.strToDouble(eJumlah)==0){
//            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
//        }else {
//            String idPelanggan=String.valueOf(tIdpelanggan);
//            String idPegawai=String.valueOf(tIdpegawai);
//            String idJasa=String.valueOf(tIdjasa);
//            Integer idlaundry = Integer.valueOf(eFaktur);
//            Double bay = Modul.strToDouble(eHarga) * Modul.strToDouble(eJumlah);
//            String qLaundryD,qLaundry ;
//            String[] detail ={
//                    idJasa,
//                    String.valueOf(idlaundry),
//                    eJumlah,
//                    String.valueOf(bay),
//                    eKet
//            };
//            String[] simpan = {
//                    String.valueOf(idlaundry),
//                    idPelanggan,
//                    idPegawai,
//                    eFaktur,
//                    convertDate(eTglT),
//                    convertDate(eTglS)
//            } ;
//
//            String q = Query.selectwhere("tbllaundry")+Query.sWhere("faktur",eFaktur);
//            Cursor c = db.sq(q);
//            if (Modul.getCount(c)==0){
//                qLaundry= Query.splitParam("INSERT INTO tbllaundry (idlaundry,idpelanggan,idpegawai,faktur,tgllaundry,tglselesai) VALUES (?,?,?,?,?,?)",simpan);
//                qLaundryD=Query.splitParam("INSERT INTO tbllaundrydetail (idjasa,idlaundry,jumlahlaundry,biayalaundry,keterangan) VALUES (?,?,?,?,?)",detail);
//            }
//            else {
//                qLaundry="UPDATE tbllaundry SET " +
//                        "idpelanggan=" +idPelanggan+","+
//                        "idpegawai=" +idPegawai+","+
//                        "tgllaundry="+convertDate(eTglT)+","+
//                        "tglselesai="+convertDate(eTglS)+
//                        " WHERE idlaundry=" +String.valueOf(idlaundry);
//                qLaundryD=Query.splitParam("INSERT INTO tbllaundrydetail (idjasa,idlaundry,jumlahlaundry,biayalaundry,keterangan) VALUES (?,?,?,?,?)",detail);
//            }
//
//            if (db.exc(qLaundry)&&db.exc(qLaundryD)){
//                Toast.makeText(this, "Sukses!", Toast.LENGTH_SHORT).show();
//                getTotal();
//                clearText();
//                loadCart();
//            }
//        }
//    }
//    public void getTotal(){
//        double total=0.0;
//        int idlaundry=Integer.valueOf(Modul.getText(v,R.id.edtFaktur));
//        Cursor c=db.sq("SELECT SUM(biayalaundry) FROM tbllaundrydetail WHERE idlaundry="+String.valueOf(idlaundry));
//        double sum=0.0;
//        if (c.moveToFirst()){
//            sum = c.getDouble(0);
//        }
//        total=total+sum;
//        totalbayar=String.valueOf(total);
//        Modul.setText(v,R.id.tvTotalbayar,"Rp. "+Modul.removeE(totalbayar));
//    }
//    public void loadCart(){
//        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        ArrayList arrayList = new ArrayList();
//        RecyclerView.Adapter adapter=new AdapterTransaksi(this,arrayList);
//        recyclerView.setAdapter(adapter);
//
//        String tempFaktur=Modul.getText(v,R.id.edtFaktur);
//
//        String q=Query.selectwhere("qcart")+Query.sWhere("faktur",tempFaktur);
//        Cursor c = db.sq(q);
//        if (Modul.getCount(c)>0){
//            while (c.moveToNext()){
//                String campur=Modul.getString(c,"idlaundrydetail")+"__"+
//                        Modul.getString(c,"kategori")+"__"+
//                        Modul.getString(c,"jasa")+"__"+
//                        Modul.getString(c,"jumlahlaundry")+"__"+
//                        Modul.getString(c,"satuan")+"__"+
//                        Modul.removeE(Modul.getString(c,"biayalaundry"))+"__Ket : "+
//                        Modul.getString(c,"keterangan");
//                arrayList.add(campur);
//            }
//        }else{
//
//        }
//        adapter.notifyDataSetChanged();
//    }
//    private void clearText(){
//        Modul.setText(v,R.id.edtJasa,"");
//        Modul.setText(v,R.id.HargaJasa,"");
//        Modul.setText(v,R.id.edtJumlah,"");
//        Modul.setText(v,R.id.edtKeterangan,"");
//        setJumlah(0,"");
//    }
//
//    public void simpan(View view) {
//        String eTglS = Modul.getText(v,R.id.TanggalKembali);
//        String eNPegawai = Modul.getText(v,R.id.namaPegawaiLaundry);
//        String eNPelanggan = Modul.getText(v,R.id.NamaPelanggan);
//        final String faktur=Modul.getText(v,R.id.edtFaktur);
//        String pelanggan=Modul.getText(v,R.id.NamaPelanggan);
//
//        Cursor c=db.sq("SELECT * FROM qcart WHERE faktur='"+faktur+"'");
//        isikeranjang=Modul.getCount(c);
//        if (TextUtils.isEmpty(eTglS) || TextUtils.isEmpty(eNPegawai) || TextUtils.isEmpty(eNPelanggan)||isikeranjang==0){
//            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
//        }else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.create()
//                    .setTitle("Anda Yakin?");
//            builder.setMessage("Anda yakin ingin menyimpan pesanan \n"+faktur+" - "+pelanggan)
//                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (db.exc("UPDATE tbllaundry SET total="+totalbayar+" WHERE faktur='"+faktur+"'")){
//                                berhasil();
//                            }else {
//                                Toast.makeText(MenuTerimaLaundry.this, "Gagal", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    })
//                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        }
//
//    }
//    private void berhasil(){
//        final Intent i = new Intent(this,TransaksiCariLaundry.class);
//        final String faktur=Modul.getText(v,R.id.edtFaktur);
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.create();
//        builder.setMessage("Simpan data berhasil");
//        builder.setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                        i.putExtra("faktur",faktur);
//                        startActivity(i);
//                    }
//                }).setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setCancelable(false)
//                .show();
//    }
//}
//class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>{
//    private Context ctxAdapter;
//    private ArrayList<String> data;
//
//    public AdapterTransaksi(Context ctxAdapter, ArrayList<String> data) {
//        this.ctxAdapter = ctxAdapter;
//        this.data = data;
//    }
//
//    @NonNull
//    @Override
//    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_terima_laundry_taruh_keranjang_laundry,viewGroup,false);
//        return new TransaksiViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int i) {
//        final String[] row=data.get(i).split("__");
//
//        String satuan = "";
//        if(row[4].equals("pc")){
//            satuan="Pcs";
//        }else if (row[4].equals("kg")){
//            satuan="Kg";
//        }else if (row[4].equals("m2")){
//            satuan="M²";
//        }
//        holder.jasa.setText("("+row[1]+") "+row[2]+" - "+row[3].replace(".",",")+" "+satuan);
//        holder.jasa.setSelected(true);
//
//        holder.harga.setText("Rp. "+row[5]);
//        holder.hapus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final LaundryDatabase db=new LaundryDatabase(ctxAdapter);
//                AlertDialog.Builder builder=new AlertDialog.Builder(ctxAdapter);
//                builder.create();
//                builder.setMessage("Anda yakin ingin menghapusnya?")
//                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String q = "DELETE FROM tbllaundrydetail WHERE idlaundrydetail="+row[0];
//                                if (db.exc(q)){
//                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
//                                    ((MenuTerimaLaundry)ctxAdapter).getTotal();
//                                    ((MenuTerimaLaundry)ctxAdapter).loadCart();
//                                }else {
//                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
//            }
//        });
//        if (row[6].equals("Ket : ")){
//            holder.keterangan.setText("Tanpa Keterangan");
//        }else {
//            holder.keterangan.setText(row[6]);
//        }
//        holder.keterangan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message="";
//                if (row[6].equals("Ket : ")){
//                    message="Tanpa Keterangan";
//                }else {
//                    message=row[6];
//                }
//                androidx.appcompat.app.AlertDialog dialog=new androidx.appcompat.app.AlertDialog.Builder(ctxAdapter)
//                        .setTitle("Keterangan "+row[1]+" - "+row[2])
//                        .setMessage(message)
//                        .setPositiveButton("Ok",null)
//                        .create();
//                dialog.show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    class TransaksiViewHolder extends RecyclerView.ViewHolder{
//        TextView jasa,jumlah,satuan,harga,keterangan;
//        ImageButton hapus;
//        public TransaksiViewHolder(@NonNull View itemView) {
//            super(itemView);
//            jasa=(TextView)itemView.findViewById(R.id.tvNamaJasa);
//            jumlah=(TextView)itemView.findViewById(R.id.tvJumlahJasa);
//            satuan=(TextView)itemView.findViewById(R.id.tvSatuan);
//            harga=(TextView)itemView.findViewById(R.id.tvHargaJumlah);
//            keterangan=(TextView)itemView.findViewById(R.id.tvKeterangan);
//            hapus=(ImageButton)itemView.findViewById(R.id.ibtnHapus);
//        }
//    }
//}
