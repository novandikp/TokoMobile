package com.itbrain.aplikasitoko.Laundry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityTransaksiTerimaLaundry extends AppCompatActivity {
    EditText edtJumlahJasa;
    ImageButton btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;
    TextView tvSatuan;
    View v;
    DatabaseLaundry db;

    int year, month, day ;
    Calendar calendar ;

    String faktur="00000000";
    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="",status,updateFaktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_terima_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db=new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali19);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        status = getIntent().getStringExtra("status");
        updateFaktur = getIntent().getStringExtra("faktur");
        if (status.equals("terima")){
            getFaktur();
            ModulLaundry.setText(v, R.id.edtTglTerima, ModulLaundry.getDate("dd/MM/yyyy"));
            ModulLaundry.setText(v, R.id.edtTglSelesai, ModulLaundry.getDate("dd/MM/yyyy"));
            Cursor c1 = db.sq(QueryLaundry.selectwhere("tblpegawai")+ QueryLaundry.sWhere("idpegawai","0"));
            c1.moveToFirst();
            Cursor c2 = db.sq(QueryLaundry.selectwhere("tblpelanggan")+ QueryLaundry.sWhere("idpelanggan","0"));
            c2.moveToFirst();
            tIdpegawai= ModulLaundry.getInt(c1,"idpegawai");
            tnPegawai= ModulLaundry.getString(c1,"pegawai");
            tIdpelanggan= ModulLaundry.getInt(c2,"idpelanggan");
            tnPelanggan= ModulLaundry.getString(c2,"pelanggan");
        }else if (status.equals("update")){
            ModulLaundry.setText(v,R.id.edtNomorFaktur,updateFaktur);
            loadCart();
            String q="SELECT * FROM qlaundry WHERE faktur='"+updateFaktur+"'";
            Cursor c=db.sq(q);
            c.moveToNext();

            ModulLaundry.setText(v,R.id.edtTglTerima, ModulLaundry.dateToNormal(ModulLaundry.getString(c,"tgllaundry")));
            ModulLaundry.setText(v,R.id.edtTglSelesai, ModulLaundry.dateToNormal(ModulLaundry.getString(c,"tglselesai")));
            tIdpegawai= ModulLaundry.getInt(c,"idpegawai");
            tnPegawai= ModulLaundry.getString(c,"pegawai");
            tIdpelanggan= ModulLaundry.getInt(c,"idpelanggan");
            tnPelanggan= ModulLaundry.getString(c,"pelanggan");
            getTotal();
        }

        btnCari();

        btnTglTerima = (ImageButton)findViewById(R.id.ibtnTglTerima);
        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        btnTglSelesai = (ImageButton)findViewById(R.id.ibtnTglSelesai);
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });

        btnAddJ=(ImageButton)findViewById(R.id.ibtnPlus);
        btnAddJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah+1;
                setJumlah(tJumlah,tBiaya);
            }
        });
        btnRemoveJ=(ImageButton)findViewById(R.id.ibtnMinus);
        btnRemoveJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah-1;
                setJumlah(tJumlah,tBiaya);
            }
        });
        edtJumlahJasa=(EditText)findViewById(R.id.edtJumlah);
        edtJumlahJasa.addTextChangedListener(new NumberTextWatcher(edtJumlahJasa,new Locale("in","ID"),2));
    }

    private void getFaktur(){
//        db.exc("INSERT INTO tbllaundry (total) VALUES (0)");
        Cursor last=db.sq("SELECT * FROM tbllaundry");
        if (ModulLaundry.getCount(last)>0){
            last.moveToLast();
            String lastFaktur= ModulLaundry.getString(last,"faktur");
            if (ModulLaundry.getString(last,"total").equals("0")){
                db.exc("DELETE FROM tbllaundry WHERE faktur='"+lastFaktur+"'");
            }
        }
        List<Integer> idLaundry = new ArrayList<Integer>();
        String q="SELECT idlaundry FROM tbllaundry";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idLaundry.add(ModulLaundry.getIntFromColumn(c,0));
            }while (c.moveToNext());
        }
        String tempFaktur="";
        int IdFaktur=0;
        if (ModulLaundry.getCount(c)==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
        }else {
            IdFaktur = idLaundry.get(ModulLaundry.getCount(c)-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }
        ModulLaundry.setText(v,R.id.edtNomorFaktur,tempFaktur);
    }
    private void setEdit(String pegawai,String pelanggan,String jasa,String biaya,String kategori,String satuan){
        ModulLaundry.setText(v,R.id.edtNamaPegawai,pegawai);
        ModulLaundry.setText(v,R.id.edtNamaPelanggan,pelanggan);
        ModulLaundry.setText(v,R.id.edtJasa,jasa);
        ModulLaundry.setText(v,R.id.edtHargaJasa,biaya);
        tvSatuan=(TextView) findViewById(R.id.tvSatuan);
        if (tSatuan.equals("pc")){
            tvSatuan.setText("/Pcs");
            tvSatuan.setVisibility(View.VISIBLE);
        }else if (tSatuan.equals("kg")){
            tvSatuan.setText("/Kg");
            tvSatuan.setVisibility(View.VISIBLE);
        }else if (tSatuan.equals("m2")){
            tvSatuan.setText("/M²");
            tvSatuan.setVisibility(View.VISIBLE);
        }
    }
    private void btnCari(){
        btnCariPegawai=(ImageButton)findViewById(R.id.ibtnNamaPegawai);
        btnCariPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityTransaksiTerimaLaundry.this,Transaksi_Cari_Pegawai.class);
                startActivityForResult(i,1000);
            }
        });
        btnCariPelanggan=(ImageButton)findViewById(R.id.ibtnNamaPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityTransaksiTerimaLaundry.this,Cari_Pelanggan_Laundry.class);
                startActivityForResult(i,2000);
            }
        });
        btnCariJasa=(ImageButton)findViewById(R.id.ibtnJasa);
        btnCariJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityTransaksiTerimaLaundry.this,Cari_Jasa_Laundry.class);
                startActivityForResult(i,3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000){
            tIdpegawai=data.getIntExtra("idpegawai",0);
            tnPegawai=data.getStringExtra("namapegawai");
        }else if (resultCode==2000){
            tIdpelanggan=data.getIntExtra("idpelanggan",0);
            tnPelanggan=data.getStringExtra("namapelanggan");
        }else if (resultCode==3000){
            tIdjasa=data.getIntExtra("idjasa",0);
            tIdkategori=data.getIntExtra("idkategori",0);
            tnJasa=data.getStringExtra("namajasa");
            tKategori=data.getStringExtra("kategorijasa");
            tBiaya=data.getStringExtra("biayajasa");
            tSatuan=data.getStringExtra("satuanjasa");
        }

    }

    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }else if (id==2){
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulLaundry.setText(v, R.id.edtTglTerima, ModulLaundry.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulLaundry.setText(v, R.id.edtTglSelesai, ModulLaundry.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    private void setJumlah(Integer jumlah, String biaya){
        ModulLaundry.setText(v,R.id.edtJumlah,String.valueOf(jumlah));
        if (biaya.equals("")){
            btnAddJ.setVisibility(View.INVISIBLE);
        }else {
            btnAddJ.setVisibility(View.VISIBLE);
        }
        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String kat="";
        tJumlah=0;
        ModulLaundry.setText(v,R.id.edtJumlah,"0");
        kat=tKategori;
        if (!kat.equals("")){
            kat=tKategori+" - "+tnJasa;
        }
        if (!tBiaya.equals("")){
            setJumlah(0,tBiaya);
        }
        setEdit(tnPegawai,tnPelanggan,kat,tBiaya,tKategori,tSatuan);
    }
    private void keluar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Anda yakin ingin keluar?");
        builder.setMessage("Setelah keluar data faktur ini akan hilang");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String faktur= ModulLaundry.getText(v,R.id.edtNomorFaktur);
                db.exc("DELETE FROM tbllaundry WHERE idlaundry="+Integer.valueOf(faktur));
                finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();

    }

    @Override
    public void onBackPressed() {
        if (status.equals("terima")){
            keluar();
        }else if (status.equals("update")){
            finish();
        }
    }

    public String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public void insertTransaksi(View view) {
        String eFaktur = ModulLaundry.getText(v,R.id.edtNomorFaktur);
        String eTglT = ModulLaundry.getText(v,R.id.edtTglTerima);
        String eTglS = ModulLaundry.getText(v,R.id.edtTglSelesai);
        String eNPegawai = ModulLaundry.getText(v,R.id.edtNamaPegawai);
        String eNPelanggan = ModulLaundry.getText(v,R.id.edtNamaPelanggan);
        String eJasa = ModulLaundry.getText(v,R.id.edtJasa);
        String eHarga = ModulLaundry.unNumberFormat(ModulLaundry.getText(v,R.id.edtHargaJasa));
        String eJumlah = ModulLaundry.parseDF(ModulLaundry.getText(v,R.id.edtJumlah));
        String eKet = ModulLaundry.getText(v,R.id.edtKeterangan).replace("\n","  ");
        if (TextUtils.isEmpty(eTglS) || TextUtils.isEmpty(eNPegawai) || TextUtils.isEmpty(eNPelanggan) || TextUtils.isEmpty(eJasa) || TextUtils.isEmpty(eJumlah) || ModulLaundry.strToDouble(eHarga)==0 || ModulLaundry.strToDouble(eJumlah)==0){
            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
        }else {
            String idPelanggan=String.valueOf(tIdpelanggan);
            String idPegawai=String.valueOf(tIdpegawai);
            String idJasa=String.valueOf(tIdjasa);
            Integer idlaundry = Integer.valueOf(eFaktur);
            Double bay = ModulLaundry.strToDouble(eHarga) * ModulLaundry.strToDouble(eJumlah);
            String qLaundryD,qLaundry ;
            String[] detail ={
                    idJasa,
                    String.valueOf(idlaundry),
                    eJumlah,
                    String.valueOf(bay),
                    eKet
            };
            String[] simpan = {
                    String.valueOf(idlaundry),
                    idPelanggan,
                    idPegawai,
                    eFaktur,
                    convertDate(eTglT),
                    convertDate(eTglS)
            } ;

            String q = QueryLaundry.selectwhere("tbllaundry")+ QueryLaundry.sWhere("faktur",eFaktur);
            Cursor c = db.sq(q);
            if (ModulLaundry.getCount(c)==0){
                qLaundry= QueryLaundry.splitParam("INSERT INTO tbllaundry (idlaundry,idpelanggan,idpegawai,faktur,tgllaundry,tglselesai) VALUES (?,?,?,?,?,?)",simpan);
                qLaundryD= QueryLaundry.splitParam("INSERT INTO tbllaundrydetail (idjasa,idlaundry,jumlahlaundry,biayalaundry,keterangan) VALUES (?,?,?,?,?)",detail);
            }
            else {
                qLaundry="UPDATE tbllaundry SET " +
                        "idpelanggan=" +idPelanggan+","+
                        "idpegawai=" +idPegawai+","+
                        "tgllaundry="+convertDate(eTglT)+","+
                        "tglselesai="+convertDate(eTglS)+
                        " WHERE idlaundry=" +String.valueOf(idlaundry);
                qLaundryD= QueryLaundry.splitParam("INSERT INTO tbllaundrydetail (idjasa,idlaundry,jumlahlaundry,biayalaundry,keterangan) VALUES (?,?,?,?,?)",detail);
            }

            if (db.exc(qLaundry)&&db.exc(qLaundryD)){
                Toast.makeText(this, "Sukses!", Toast.LENGTH_SHORT).show();
                getTotal();
                clearText();
                loadCart();
            }
        }
    }
    public void getTotal(){
        double total=0.0;
        int idlaundry=Integer.valueOf(ModulLaundry.getText(v,R.id.edtNomorFaktur));
        Cursor c=db.sq("SELECT SUM(biayalaundry) FROM tbllaundrydetail WHERE idlaundry="+String.valueOf(idlaundry));
        double sum=0.0;
        if (c.moveToFirst()){
            sum = c.getDouble(0);
        }
        total=total+sum;
        totalbayar=String.valueOf(total);
        ModulLaundry.setText(v,R.id.tvTotalBayar,"Rp. "+ ModulLaundry.numberFormat(totalbayar));
    }
    public void loadCart(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterTransaksi(this,arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur= ModulLaundry.getText(v,R.id.edtNomorFaktur);

        String q= QueryLaundry.selectwhere("qcart")+ QueryLaundry.sWhere("faktur",tempFaktur);
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idlaundrydetail")+"__"+
                        ModulLaundry.getString(c,"kategori")+"__"+
                        ModulLaundry.getString(c,"jasa")+"__"+
                        ModulLaundry.getString(c,"jumlahlaundry")+"__"+
                        ModulLaundry.getString(c,"satuan")+"__"+
                        ModulLaundry.removeE(ModulLaundry.getString(c,"biayalaundry"))+"__Ket : "+
                        ModulLaundry.getString(c,"keterangan");
                arrayList.add(campur);
            }
        }else{

        }
        adapter.notifyDataSetChanged();
    }
    private void clearText(){
        ModulLaundry.setText(v,R.id.edtJasa,"");
        ModulLaundry.setText(v,R.id.edtHargaJasa,"");
        ModulLaundry.setText(v,R.id.edtJumlah,"");
        ModulLaundry.setText(v,R.id.edtKeterangan,"");
        setJumlah(0,"");
    }

    public void simpan(View view) {
        String eTglS = ModulLaundry.getText(v,R.id.edtTglSelesai);
        String eNPegawai = ModulLaundry.getText(v,R.id.edtNamaPegawai);
        String eNPelanggan = ModulLaundry.getText(v,R.id.edtNamaPelanggan);
        final String faktur= ModulLaundry.getText(v,R.id.edtNomorFaktur);
        String pelanggan= ModulLaundry.getText(v,R.id.edtNamaPelanggan);

        Cursor c=db.sq("SELECT * FROM qcart WHERE faktur='"+faktur+"'");
        isikeranjang= ModulLaundry.getCount(c);
        if (TextUtils.isEmpty(eTglS) || TextUtils.isEmpty(eNPegawai) || TextUtils.isEmpty(eNPelanggan)||isikeranjang==0){
            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.create()
                    .setTitle("Anda Yakin?");
            builder.setMessage("Anda yakin ingin menyimpan pesanan \n"+faktur+" - "+pelanggan)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (db.exc("UPDATE tbllaundry SET total="+totalbayar+" WHERE faktur='"+faktur+"'")){
                                berhasil();
                            }else {
                                Toast.makeText(ActivityTransaksiTerimaLaundry.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

    }
    private void berhasil(){
        final Intent i = new Intent(this, ActivityTransaksiTerimaLaundry.class);
        final String faktur= ModulLaundry.getText(v,R.id.edtNomorFaktur);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Simpan data berhasil");
        builder.setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityTransaksiTerimaLaundry.this,MenuCetaklaundry.class);
                i.putExtra("faktur",faktur);
                startActivity(i);
            }
        }).setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setCancelable(false)
                .show();
    }
}
class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>{
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterTransaksi(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_terima_laundry,viewGroup,false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");

        String satuan = "";
        if(row[4].equals("pc")){
            satuan="Pcs";
        }else if (row[4].equals("kg")){
            satuan="Kg";
        }else if (row[4].equals("m2")){
            satuan="M²";
        }
        holder.jasa.setText("("+row[1]+") "+row[2]+" - "+row[3].replace(".",",")+" "+satuan);
        holder.jasa.setSelected(true);

        holder.harga.setText("Rp. "+row[5]);
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseLaundry db=new DatabaseLaundry(ctxAdapter);
                AlertDialog.Builder builder=new AlertDialog.Builder(ctxAdapter);
                builder.create();
                builder.setMessage("Anda yakin ingin menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tbllaundrydetail WHERE idlaundrydetail="+row[0];
                                if (db.exc(q)){
                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
                                    ((ActivityTransaksiTerimaLaundry)ctxAdapter).getTotal();
                                    ((ActivityTransaksiTerimaLaundry)ctxAdapter).loadCart();
                                }else {
                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        if (row[6].equals("Ket : ")){
            holder.keterangan.setText("Tanpa Keterangan");
        }else {
            holder.keterangan.setText(row[6]);
        }
        holder.keterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="";
                if (row[6].equals("Ket : ")){
                    message="Tanpa Keterangan";
                }else {
                    message=row[6];
                }
                AlertDialog dialog=new AlertDialog.Builder(ctxAdapter)
                        .setTitle("Keterangan "+row[1]+" - "+row[2])
                        .setMessage(message)
                        .setPositiveButton("Ok",null)
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransaksiViewHolder extends RecyclerView.ViewHolder{
        TextView jasa,jumlah,satuan,harga,keterangan;
        ImageButton hapus;
        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            jasa=(TextView)itemView.findViewById(R.id.tvNamaJasa);
            jumlah=(TextView)itemView.findViewById(R.id.tvJumlahJasa);
            satuan=(TextView)itemView.findViewById(R.id.tvSatuan);
            harga=(TextView)itemView.findViewById(R.id.tvHargaJumlah);
            keterangan=(TextView)itemView.findViewById(R.id.tvKeterangan);
            hapus=(ImageButton)itemView.findViewById(R.id.ibtnHapus);
        }
    }
}
