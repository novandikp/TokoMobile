////package com.itbrain.aplikasitoko;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import android.app.AlertDialog;
////import android.app.DatePickerDialog;
////import android.app.Dialog;
////import android.content.Context;
////import android.content.DialogInterface;
////import android.content.Intent;
////import android.database.Cursor;
////import android.os.Bundle;
////import android.text.TextUtils;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.Button;
////import android.widget.DatePicker;
////import android.widget.EditText;
////import android.widget.ImageButton;
////import android.widget.ImageView;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.google.android.material.textfield.TextInputEditText;
////
////import java.util.ArrayList;
////import java.util.Calendar;
////import java.util.List;
////
////public class MenuTerimaLaundry extends AppCompatActivity {
////
////    ImageView btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;
////    EditText Faktur,TanggalMulai,TanggalKembali,NamaPelanggan,NamaPegawai,HargaJasa,Jumlah;
////    DatabaseLaundry db;
////    Button Simpan;
////    View v;
////
////    int year, month, day ;
////    Calendar calendar ;
////
////    String faktur="00000000";
////    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
////    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="",status,updateFaktur;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.menuterimalaundry);
////        Simpan = (Button) findViewById(R.id.Simpan);
////        Faktur = (TextInputEditText) findViewById(R.id.edtFaktur);
////        TanggalMulai = (TextInputEditText) findViewById(R.id.TanggalMulai);
////        TanggalKembali = (TextInputEditText) findViewById(R.id.TanggalKembali);
////        NamaPelanggan = (TextInputEditText) findViewById(R.id.NamaPelanggan);
////        NamaPegawai = (TextInputEditText) findViewById(R.id.namaPegawaiLaundry);
////        HargaJasa = (TextInputEditText) findViewById(R.id.HargaJasa);
////        Jumlah = (TextInputEditText) findViewById(R.id.edtJumlah);
////        btnTglTerima = findViewById(R.id.ibtnTglTerima);
////        btnTglSelesai = findViewById(R.id.ibtnTglSelesai);
////        btnCariPegawai = findViewById(R.id.ibtnCariPegawai);
////        btnCariPelanggan = findViewById(R.id.ibtnCariPelanggan);
////        btnCariJasa = findViewById(R.id.ibtnJasa);
////
////        db = new DatabaseLaundry(this);
////        v = this.findViewById(android.R.id.content);
////
////        calendar = Calendar.getInstance();
////        year = calendar.get(Calendar.YEAR);
////        month = calendar.get(Calendar.MONTH);
////        day = calendar.get(Calendar.DAY_OF_MONTH);
////
////        status = getIntent().getStringExtra("status");
////        updateFaktur = getIntent().getStringExtra("faktur");
////        if (status.equals("terima")){
////            getFaktur();
////            Modul.setText(v, R.id.TanggalMulai,Modul.getDate("dd/MM/yyyy"));
////            Modul.setText(v, R.id.TanggalKembali,Modul.getDate("dd/MM/yyyy"));
////            Cursor c1 = db.sq(Query.selectwhere("tblpegawai")+Query.sWhere("idpegawai","0"));
////            c1.moveToFirst();
////            Cursor c2 = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan","0"));
////            c2.moveToFirst();
////            tIdpegawai=Modul.getInt(c1,"idpegawai");
////            tnPegawai=Modul.getString(c1,"pegawai");
////            tIdpelanggan=Modul.getInt(c2,"idpelanggan");
////            tnPelanggan=Modul.getString(c2,"pelanggan");
////        }else if (status.equals("update")){
////            Modul.setText(v,R.id.edtFaktur,updateFaktur);
////            loadCart();
////            String q="SELECT * FROM qlaundry WHERE faktur='"+updateFaktur+"'";
////            Cursor c=db.sq(q);
////            c.moveToNext();
////
////            Modul.setText(v,R.id.TanggalMulai,Modul.dateToNormal(Modul.getString(c,"tgllaundry")));
////            Modul.setText(v,R.id.TanggalKembali,Modul.dateToNormal(Modul.getString(c,"tglselesai")));
////            tIdpegawai=Modul.getInt(c,"idpegawai");
////            tnPegawai=Modul.getString(c,"pegawai");
////            tIdpelanggan=Modul.getInt(c,"idpelanggan");
////            tnPelanggan=Modul.getString(c,"pelanggan");
////            getTotal();
////        }
////
////        btnCari();
////
////        btnTglTerima.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                setDate(1);
////            }
////        });
////        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                setDate(2);
////            }
////        });
////
////        btnAddJ= findViewById(R.id.ibtnPlus);
////        btnAddJ.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                tJumlah=tJumlah+1;
////                setJumlah(tJumlah,tBiaya);
////            }
////        });
////
////        Simpan.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) { add(); }
////        });
////    }
////
////    public void setDate(int i) {
////        showDialog(i);
////    }
////    @Override
////    protected Dialog onCreateDialog(int id) {
////        // TODO Auto-generated method stub
////        if (id == 1) {
////            return new DatePickerDialog(this, dTerima, year, month, day);
////        }else if (id==2){
////            return new DatePickerDialog(this, dSelesai, year, month, day);
////        }
////        return null;
////    }
////
////    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
////        @Override
////        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
////            Modul.setText(v, R.id.TanggalMulai, Modul.setDatePickerNormal(thn,bln+1,day)) ;
////        }
////    };
////    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
////        @Override
////        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
////            Modul.setText(v, R.id.TanggalKembali, Modul.setDatePickerNormal(thn,bln+1,day)) ;
////        }
////    };
////
////    private void setEdit(String pegawai,String pelanggan,String jasa,String biaya,String kategori,String satuan){
////        Modul.setText(v,R.id.namaPegawaiLaundry,pegawai);
////        Modul.setText(v,R.id.NamaPelanggan,pelanggan);
////        Modul.setText(v,R.id.edtJasa,jasa);
////        Modul.setText(v,R.id.Jasa,biaya);
////        TextView tvSatuan=(TextView) findViewById(R.id.tvSatuan);
////        if (tSatuan.equals("pc")){
////            tvSatuan.setText("/Pcs");
////            tvSatuan.setVisibility(View.VISIBLE);
////        }else if (tSatuan.equals("kg")){
////            tvSatuan.setText("/Kg");
////            tvSatuan.setVisibility(View.VISIBLE);
////        }else if (tSatuan.equals("m2")){
////            tvSatuan.setText("/M²");
////            tvSatuan.setVisibility(View.VISIBLE);
////        }
////    }
////
////    private void btnCari(){
////        btnCariPegawai= findViewById(R.id.ibtnCariPegawai);
////        btnCariPegawai.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
////                i.putExtra("cari","pegawai");
////                startActivityForResult(i,1000);
////            }
////        });
////        btnCariPelanggan= findViewById(R.id.ibtnCariPelanggan);
////        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
////                i.putExtra("cari","pelanggan");
////                startActivityForResult(i,2000);
////            }
////        });
////        btnCariJasa= findViewById(R.id.ibtnJasa);
////        btnCariJasa.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent i= new Intent(MenuTerimaLaundry.this,TransaksiCariLaundry.class);
////                i.putExtra("cari","jasa");
////                startActivityForResult(i,3000);
////            }
////        });
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (resultCode==1000){
////            tIdpegawai=data.getIntExtra("idpegawai",0);
////            tnPegawai=data.getStringExtra("namapegawai");
////        }else if (resultCode==2000){
////            tIdpelanggan=data.getIntExtra("idpelanggan",0);
////            tnPelanggan=data.getStringExtra("namapelanggan");
////        }else if (resultCode==3000){
////            tIdjasa=data.getIntExtra("idjasa",0);
////            tIdkategori=data.getIntExtra("idkategori",0);
////            tnJasa=data.getStringExtra("namajasa");
////            tKategori=data.getStringExtra("kategorijasa");
////            tBiaya=data.getStringExtra("biayajasa");
////            tSatuan=data.getStringExtra("satuanjasa");
////        }
////    }
////
////    private void setJumlah(Integer jumlah, String biaya){
////        Modul.setText(v,R.id.edtJumlah,String.valueOf(jumlah));
////        if (biaya.equals("")){
////            btnAddJ.setVisibility(View.INVISIBLE);
////        }else {
////            btnAddJ.setVisibility(View.VISIBLE);
////        }
////        if (jumlah<1){
////            btnRemoveJ.setVisibility(View.INVISIBLE);
////        }else {
////            btnRemoveJ.setVisibility(View.VISIBLE);
////        }
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        String kat="";
////        tJumlah=0;
////        Modul.setText(v,R.id.edtJumlah,"0");
////        kat=tKategori;
////        if (!kat.equals("")){
////            kat=tKategori+" - "+tnJasa;
////        }
////        if (!tBiaya.equals("")){
////            setJumlah(0,tBiaya);
////        }
////        setEdit(tnPegawai,tnPelanggan,kat,tBiaya,tKategori,tSatuan);
////    }
////
////    public void loadCart(){
////        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        recyclerView.setHasFixedSize(true);
////        ArrayList arrayList = new ArrayList();
////        RecyclerView.Adapter adapter=new AdapterTransaksi(this,arrayList);
////        recyclerView.setAdapter(adapter);
////
////        String tempFaktur=Modul.getText(v,R.id.edtFaktur);
////
////        String q=Query.selectwhere("qcart")+Query.sWhere("faktur",tempFaktur);
////        Cursor c = db.sq(q);
////        if (Modul.getCount(c)>0){
////            while (c.moveToNext()){
////                String campur=Modul.getString(c,"idlaundrydetail")+"__"+
////                        Modul.getString(c,"kategori")+"__"+
////                        Modul.getString(c,"jasa")+"__"+
////                        Modul.getString(c,"jumlahlaundry")+"__"+
////                        Modul.getString(c,"satuan")+"__"+
////                        Modul.removeE(Modul.getString(c,"biayalaundry"))+"__Ket : "+
////                        Modul.getString(c,"keterangan");
////                arrayList.add(campur);
////            }
////        }else{
////
////        }
////        adapter.notifyDataSetChanged();
////    }
////
////    public void getTotal(){
////        double total=0.0;
////        int idlaundry=Integer.valueOf(Modul.getText(v,R.id.edtFaktur));
////        Cursor c=db.sq("SELECT SUM(biayalaundry) FROM tbllaundrydetail WHERE idlaundry="+String.valueOf(idlaundry));
////        double sum=0.0;
////        if (c.moveToFirst()){
////            sum = c.getDouble(0);
////        }
////        total=total+sum;
////        totalbayar=String.valueOf(total);
////        Modul.setText(v,R.id.tvTotalbayar,"Rp. "+Modul.removeE(totalbayar));
////    }
////
////    private void getFaktur(){
//////        db.exc("INSERT INTO tbllaundry (total) VALUES (0)");
////        List<Integer> idLaundry = new ArrayList<Integer>();
////        String q="SELECT idlaundry FROM tbllaundry";
////        Cursor c = db.sq(q);
////        if (c.moveToNext()){
////            do {
////                idLaundry.add(Modul.getIntFromCol(c,0));
////            }while (c.moveToNext());
////        }
////        String tempFaktur="";
////        int IdFaktur=0;
////        if (Modul.getCount(c)==0){
////            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
////        }else {
////            IdFaktur = idLaundry.get(Modul.getCount(c)-1)+1;
////            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
////        }
////        Modul.setText(v,R.id.edtFaktur,tempFaktur);
////    }
////
////
////    public void add(){
////        String faktur = Faktur.getText().toString();
////        String tanggalmulai = TanggalMulai.getText().toString();
////        String tanggalkembali = TanggalKembali.getText().toString();
////        String namapelanggan = NamaPelanggan.getText().toString();
////        String namapegawai = NamaPegawai.getText().toString();
////        String hargajasa = HargaJasa.getText().toString();
////        String jumlah = Jumlah.getText().toString();
////        if(TextUtils.isEmpty(faktur)){
////            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
////        }
////        else{
////            db.exc("insert into qproses (faktur,tgllaundry,tglselesai,total,statuslaundry,statusbayar,pelanggan,alamat,notelp) values ('"+ faktur +"','"+ tanggalmulai +"','"+ tanggalkembali +"','"+ namapelanggan +"','"+ namapegawai +"','"+ hargajasa +"','"+ jumlah +"')");
////            finish();
////        }
////    }
////}
////
////class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>{
////    private Context ctxAdapter;
////    private ArrayList<String> data;
////
////    public AdapterTransaksi(Context ctxAdapter, ArrayList<String> data) {
////        this.ctxAdapter = ctxAdapter;
////        this.data = data;
////    }
////
////    @NonNull
////    @Override
////    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_terima_laundry_taruh_keranjang_laundry,viewGroup,false);
////        return new TransaksiViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int i) {
////        final String[] row=data.get(i).split("__");
////
////        String satuan = "";
////        if(row[4].equals("pc")){
////            satuan="Pcs";
////        }else if (row[4].equals("kg")){
////            satuan="Kg";
////        }else if (row[4].equals("m2")){
////            satuan="M²";
////        }
////        holder.jasa.setText("("+row[1]+") "+row[2]+" - "+row[3].replace(".",",")+" "+satuan);
////        holder.jasa.setSelected(true);
////
////        holder.harga.setText("Rp. "+row[5]);
////        holder.hapus.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                final DatabaseLaundry db=new DatabaseLaundry(ctxAdapter);
////                AlertDialog.Builder builder=new AlertDialog.Builder(ctxAdapter);
////                builder.create();
////                builder.setMessage("Anda yakin ingin menghapusnya?")
////                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                String q = "DELETE FROM tbllaundrydetail WHERE idlaundrydetail="+row[0];
////                                if (db.exc(q)){
////                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
////                                    ((MenuTerimaLaundry)ctxAdapter).getTotal();
////                                    ((MenuTerimaLaundry)ctxAdapter).loadCart();
////                                }else {
////                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        })
////                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                            }
////                        })
////                        .show();
////            }
////        });
////        if (row[6].equals("Ket : ")){
////            holder.keterangan.setText("Tanpa Keterangan");
////        }else {
////            holder.keterangan.setText(row[6]);
////        }
////        holder.keterangan.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String message="";
////                if (row[6].equals("Ket : ")){
////                    message="Tanpa Keterangan";
////                }else {
////                    message=row[6];
////                }
////                androidx.appcompat.app.AlertDialog dialog=new androidx.appcompat.app.AlertDialog.Builder(ctxAdapter)
////                        .setTitle("Keterangan "+row[1]+" - "+row[2])
////                        .setMessage(message)
////                        .setPositiveButton("Ok",null)
////                        .create();
////                dialog.show();
////            }
////        });
////
////    }
////
////    @Override
////    public int getItemCount() {
////        return data.size();
////    }
////
////    class TransaksiViewHolder extends RecyclerView.ViewHolder{
////        TextView jasa,jumlah,satuan,harga,keterangan;
////        ImageButton hapus;
////        public TransaksiViewHolder(@NonNull View itemView) {
////            super(itemView);
////            jasa=(TextView)itemView.findViewById(R.id.tvNamaJasa);
////            jumlah=(TextView)itemView.findViewById(R.id.tvJumlahJasa);
////            satuan=(TextView)itemView.findViewById(R.id.tvSatuan);
////            harga=(TextView)itemView.findViewById(R.id.tvHargaJumlah);
////            keterangan=(TextView)itemView.findViewById(R.id.tvKeterangan);
////            hapus=(ImageButton)itemView.findViewById(R.id.ibtnHapus);
////        }
////    }
////}
//
//
//package com.itbrain.aplikasitoko;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.android.material.textfield.TextInputEditText;
//
//import java.util.Calendar;
//import java.util.Locale;
//
//public class MenuTerimaLaundry extends AppCompatActivity {
//    EditText edtJumlahJasa;
//    ImageView btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa;
//    ImageView btnAddJ,btnRemoveJ;
//    TextView tvSatuan;
//    View v;
//    DatabaseLaundry db;
//
//    int year, month, day ;
//    Calendar calendar ;
//
//    String faktur="00000000";
//    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
//    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.menuterimalaundry);
//
//        db=new DatabaseLaundry(this);
//        v = this.findViewById(android.R.id.content);
//
//        calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
////
////        status = getIntent().getStringExtra("status");
////        updateFaktur = getIntent().getStringExtra("faktur");
////        if (status.equals("terima")){
//////            getFaktur();
////            Modul.setText(v, R.id.TanggalMulai,Modul.getDate("dd/MM/yyyy"));
////            Modul.setText(v, R.id.TanggalKembali,Modul.getDate("dd/MM/yyyy"));
////            Cursor c1 = db.sq(Query.selectwhere("tblpegawai")+Query.sWhere("idpegawai","0"));
////            c1.moveToFirst();
////            Cursor c2 = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan","0"));
////            c2.moveToFirst();
////            tIdpegawai=Modul.getInt(c1,"idpegawai");
////            tnPegawai=Modul.getString(c1,"pegawai");
////            tIdpelanggan=Modul.getInt(c2,"idpelanggan");
////            tnPelanggan=Modul.getString(c2,"pelanggan");
////        }else if (status.equals("update")){
////            Modul.setText(v,R.id.edtFaktur,updateFaktur);
//////            loadCart();
////            String q="SELECT * FROM qlaundry WHERE faktur='"+updateFaktur+"'";
////            Cursor c=db.sq(q);
////            c.moveToNext();
////
////            Modul.setText(v,R.id.TanggalMulai,Modul.dateToNormal(Modul.getString(c,"tgllaundry")));
////            Modul.setText(v,R.id.TanggalKembali,Modul.dateToNormal(Modul.getString(c,"tglselesai")));
////            tIdpegawai=Modul.getInt(c,"idpegawai");
////            tnPegawai=Modul.getString(c,"pegawai");
////            tIdpelanggan=Modul.getInt(c,"idpelanggan");
////            tnPelanggan=Modul.getString(c,"pelanggan");
//////            getTotal();
////        }
//
////        btnCari();
//
////        MenuTerimaLaundry.java
////        btnTglTerima = findViewById(R.id.ibtnTglTerima);
////        btnTglTerima.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                setDate(1);
////            }
////        });
////        btnTglSelesai = findViewById(R.id.ibtnTglSelesai);
////        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                setDate(2);
////            }
////        });
////
////        btnAddJ = findViewById(R.id.ibtnPlus);
////        btnAddJ.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                tJumlah=tJumlah+1;
//////                setJumlah(tJumlah,tBiaya);
////            }
////        });
////        btnRemoveJ= findViewById(R.id.ibtnMinus);
////        btnRemoveJ.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                tJumlah=tJumlah-1;
//////                setJumlah(tJumlah,tBiaya);
////            }
////        });
////        edtJumlahJasa=(EditText)findViewById(R.id.edtJumlah);
//////        edtJumlahJasa.addTextChangedListener(new NumberTextWatcher(edtJumlahJasa,new Locale("in","ID"),2));
////    }
////
////}

//package com.itbrain.aplikasitoko;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.appcompat.widget.PopupMenu;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.itbrain.aplikasitoko.DatabaseLaundry;
//import com.itbrain.aplikasitoko.Model.JasaLaundry;
//import com.itbrain.aplikasitoko.Model.Pegawai;
//import com.itbrain.aplikasitoko.Model.PelangganLaundry;
//import com.itbrain.aplikasitoko.Modul;
//import com.itbrain.aplikasitoko.Query;
//import com.itbrain.aplikasitoko.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TransaksiCariLaundry extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    List<Pegawai> DaftarPegawai;
//    List<PelangganLaundry> DaftarPelanggan;
//    List<JasaLaundry> DaftarJasa;
//    DatabaseLaundry db;
//    AdapterListPegawaiCari adapterPegawai;
//    AdapterListPelangganCari adapterPelanggan;
//    AdapterListJasaCari adapterJasa;
//    Spinner spinner;
//    String keyword="",kategori="";
//    String a;
//    List<String> idKat;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.transaksi_cari_laundry);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        db=new DatabaseLaundry(this);
//
//        a=getIntent().getStringExtra("cari");
//        if (a.equals("pegawai")){
//            Modul.btnBack("Cari Pegawai",getSupportActionBar());
//            getPegawai("");
//        }else if (a.equals("pelanggan")){
//            Modul.btnBack("Cari Pelanggan",getSupportActionBar());
//            getPelanggan("");
//        }else if (a.equals("jasa")){
//            Modul.btnBack("Cari Jasa",getSupportActionBar());
//            spinner = (Spinner)findViewById(R.id.spKatCari);
//            getKategoriData();
//            spinner.setVisibility(View.VISIBLE);
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    kategori=db.getIdKategori().get(parent.getSelectedItemPosition());
//                    getJasa(keyword,kategori);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            getJasa(keyword,kategori);
//        }
//
//        final EditText edtCari = (EditText) findViewById(R.id.edtCari);
//        edtCari.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                keyword=edtCari.getText().toString();
//                if (a.equals("pegawai")){
//                    getPegawai(keyword);
//                }else if (a.equals("pelanggan")){
//                    getPelanggan(keyword);
//                }else if (a.equals("jasa")){
//                    getJasa(keyword,kategori);
//                }
//            }
//        });
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==android.R.id.home){
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    private void getKategoriData(){
//        DatabaseLaundry db = new DatabaseLaundry(this);
//        List<String> labels = db.getKategori();
//
//        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
//        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(data);
//    }
//    //--------------------------------------------------------------------------------------------------------//
//    public void getPegawai(String keyword){
//        DaftarPegawai = new ArrayList<>();
//        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapterPegawai = new AdapterListPegawaiCari(this,DaftarPegawai);
//        recyclerView.setAdapter(adapterPegawai);
//        String q;
//        if (TextUtils.isEmpty(keyword)){
//            q="SELECT * FROM tblpegawai";
//        }else {
//            q="SELECT * FROM tblpegawai WHERE pegawai LIKE '%"+keyword+"%'"+ Query.sOrderASC("pegawai");
//        }
//        Cursor c=db.sq(q);
//        if (Modul.getCount(c)>0){
//            while(c.moveToNext()){
//                DaftarPegawai.add(new Pegawai(
//                        Modul.getInt(c,"idpegawai"),
//                        Modul.getString(c,"pegawai"),
//                        Modul.getString(c,"alamatpegawai"),
//                        Modul.getString(c,"notelppegawai")
//                ));
//            }
//        }
//        adapterPegawai.notifyDataSetChanged();
//    }
//    //--------------------------------------------------------------------------------------------------------//
//    public void getPelanggan(String keyword){
//        DaftarPelanggan = new ArrayList<>();
//        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapterPelanggan = new AdapterListPelangganCari(this,DaftarPelanggan);
//        recyclerView.setAdapter(adapterPelanggan);
//
//        String q;
//        if (TextUtils.isEmpty(keyword)){
//            q="SELECT * FROM tblpelanggan";
//        }else {
//            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%'"+Query.sOrderASC("pelanggan");
//        }
//
//        Cursor c = db.sq(q);
//        if (Modul.getCount(c)>0){
//            while(c.moveToNext()){
//                DaftarPelanggan.add(new PelangganLaundry(
//                        Modul.getInt(c,"idpelanggan"),
//                        Modul.getString(c,"pelanggan"),
//                        Modul.getString(c,"alamat"),
//                        Modul.getString(c,"notelp")
//                ));
//            }
//        }
//
//        adapterPelanggan.notifyDataSetChanged();
//    }
//    //--------------------------------------------------------------------------------------------------------//
//    public  void getJasa(String keyword,String kategori){
//        DaftarJasa = new ArrayList<>();
//        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapterJasa = new AdapterListJasaCari(this,DaftarJasa);
//        recyclerView.setAdapter(adapterJasa);
//        String q;
//
//        if (TextUtils.isEmpty(keyword)){
//            if (kategori.equals("0")){
//                q="SELECT * FROM qjasa";
//            }else {
//                q="SELECT * FROM qjasa WHERE idkategori='"+kategori+"'";
//            }
//        }else {
//            if (kategori.equals("0")){
//                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
//            }else {
//                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
//            }
//        }
//        Cursor c=db.sq(q);
//        if (Modul.getCount(c)>0){
//            while(c.moveToNext()){
//                DaftarJasa.add(new JasaLaundry(
//                        Modul.getInt(c,"idjasa"),
//                        Modul.getInt(c,"idkategori"),
////                        Modul.getString(c,"kategori"),
//                        Modul.getString(c,"jasa"),
//                        Modul.removeE(Modul.getString(c,"biaya")),
//                        Modul.getString(c,"satuan")
//                ));
//            }
//        }
//        adapterJasa.notifyDataSetChanged();
//    }
//
//    public void tambahdata(View view) {
//        if (a.equals("pegawai")){
//            startActivity(new Intent(this,MenuDaftarPegawaiLaundry.class));
//        }else if (a.equals("pelanggan")){
//            startActivity(new Intent(this,MenuDaftarPelangganLaundry.class));
//        }else if (a.equals("jasa")){
//            startActivity(new Intent(this,MenuDaftarJasaLaundry.class));
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (a.equals("pegawai")){
//            getPegawai(keyword);
//        }else if (a.equals("pelanggan")){
//            getPelanggan(keyword);
//        }else if (a.equals("jasa")) {
//            getKategoriData();
//            getJasa(keyword,kategori);
//        }
//    }
//}
//class AdapterListPegawaiCari extends RecyclerView.Adapter<AdapterListPegawaiCari.PegawaiCariViewHolder>{
//    private Context ctxAdapter;
//    private List<Pegawai> data;
//
//    public AdapterListPegawaiCari(Context ctxAdapter, List<Pegawai> data) {
//        this.ctxAdapter = ctxAdapter;
//        this.data = data;
//    }
//    @NonNull
//    @Override
//    public PegawaiCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
//        View view = inflater.inflate(R.layout.laundryitemdaftarpegawai,viewGroup,false);
//        return new PegawaiCariViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PegawaiCariViewHolder holder, int i) {
//        final Pegawai getter = data.get(i);
//
//        holder.nama.setText(getter.getPegawai());
//        holder.alamat.setText(getter.getAlamatpegawai());
//        holder.telp.setText(getter.getNotelppegawai());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
//                terima.putExtra("idpegawai",getter.getIdpegawai());
//                terima.putExtra("namapegawai",getter.getPegawai());
//                ((MenuTerimaLaundry)ctxAdapter).setResult(1000,terima);
//                ((MenuTerimaLaundry)ctxAdapter).finish();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    class PegawaiCariViewHolder extends RecyclerView.ViewHolder{
//        private TextView nama,alamat,telp;
//        public PegawaiCariViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nama=(TextView) itemView.findViewById(R.id.namaPegawai);
//            alamat=(TextView) itemView.findViewById(R.id.alamatPegawai);
//            telp=(TextView) itemView.findViewById(R.id.notelpPegawai);
//        }
//    }
//}
//class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder>{
//    private Context ctxAdapter;
//    private List<PelangganLaundry> data;
//
//    public AdapterListPelangganCari(Context ctxAdapter, List<PelangganLaundry> data) {
//        this.ctxAdapter = ctxAdapter;
//        this.data = data;
//    }
//
//    @NonNull
//    @Override
//    public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        final PelangganLaundry getter=data.get(i);
//        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
//        View view=inflater.inflate(R.layout.laundryitemdaftarpelanggan,viewGroup,false);
//        return new PelangganCariViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
//        final PelangganLaundry getter=data.get(i);
//
//        holder.nama.setText(getter.getPelanggan());
//        holder.alamat.setText(getter.getAlamatpelanggan());
//        holder.telp.setText(getter.getNotelppelanggan());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
//                terima.putExtra("idpelanggan",getter.getIdpelanggan());
//                terima.putExtra("namapelanggan",getter.getPelanggan());
//                ((TransaksiCariLaundry)ctxAdapter).setResult(2000,terima);
//                ((TransaksiCariLaundry)ctxAdapter).finish();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    class PelangganCariViewHolder extends RecyclerView.ViewHolder{
//        private TextView nama,alamat,telp;
//        public PelangganCariViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nama= (TextView)itemView.findViewById(R.id.txtNamaPelanggan);
//            alamat= (TextView)itemView.findViewById(R.id.txtAlamatPelanggan);
//            telp= (TextView)itemView.findViewById(R.id.txtNomerPelanggan);
//        }
//    }
//}
//class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder>{
//    private Context ctxAdapter;
//    private List<JasaLaundry> data;
//
//    public AdapterListJasaCari(Context ctxAdapter, List<JasaLaundry> data) {
//        this.ctxAdapter = ctxAdapter;
//        this.data = data;
//    }
//
//    @NonNull
//    @Override
//    public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        final JasaLaundry getter=data.get(i);
//        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
//        View view=inflater.inflate(R.layout.laundryitemdaftarjasa,viewGroup,false);
//        return new JasaCariViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {
//        final JasaLaundry getter=data.get(i);
//        holder.nama.setText(getter.getJasa());
//        holder.biaya.setText("Rp."+getter.getSatuan());
//        if(getter.getSatuan().equals("pc")){
//            holder.satuan.setText("/Pcs");
//        }else if (getter.getSatuan().equals("kg")){
//            holder.satuan.setText("/Kg");
//        } else if (getter.getSatuan().equals("m2")) {
//            holder.satuan.setText("/M²");
//        }
//        holder.kategori.setText(getter.getIdkategori());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
//                terima.putExtra("idjasa",getter.getIdJasa());
//                terima.putExtra("idkategori",getter.getIdkategori());
//                terima.putExtra("namajasa",getter.getJasa());
//                terima.putExtra("kategorijasa",getter.getKategori());
//                terima.putExtra("biayajasa",getter.getBiaya());
//                terima.putExtra("satuanjasa",getter.getSatuan());
//                ((TransaksiCariLaundry)ctxAdapter).setResult(3000,terima);
//                ((TransaksiCariLaundry)ctxAdapter).finish();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    class JasaCariViewHolder extends RecyclerView.ViewHolder{
//        TextView nama,biaya,satuan,kategori;
//        public JasaCariViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nama=(TextView)itemView.findViewById(R.id.edtDaftarKategori);
//            biaya=(TextView)itemView.findViewById(R.id.edtSatuan);
//            satuan=(TextView)itemView.findViewById(R.id.edtjenisSatuan);
//            kategori=(TextView)itemView.findViewById(R.id.tvKategoriJasa);
//        }
//    }
//}


