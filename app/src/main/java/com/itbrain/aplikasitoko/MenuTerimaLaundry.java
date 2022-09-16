package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenuTerimaLaundry extends AppCompatActivity {

    ImageView btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;
    EditText Faktur,TanggalMulai,TanggalKembali,NamaPelanggan,NamaPegawai,HargaJasa,Jumlah;
    DatabaseLaundry db;
    Button Simpan;
    View v;

    int year, month, day ;
    Calendar calendar ;

    String faktur="00000000";
    int tIdpegawai,tIdpelanggan,tIdjasa,tIdkategori,tJumlah=0,isikeranjang=0;
    String tnPegawai,tnPelanggan,tnJasa="",tBiaya="",tKategori="",tSatuan="",totalbayar="",status,updateFaktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuterimalaundry);
        Simpan = (Button) findViewById(R.id.Simpan);
        Faktur = (TextInputEditText) findViewById(R.id.edtFaktur);
        TanggalMulai = (TextInputEditText) findViewById(R.id.TanggalMulai);
        TanggalKembali = (TextInputEditText) findViewById(R.id.TanggalKembali);
        NamaPelanggan = (TextInputEditText) findViewById(R.id.NamaPelanggan);
        NamaPegawai = (TextInputEditText) findViewById(R.id.namaPegawaiLaundry);
        HargaJasa = (TextInputEditText) findViewById(R.id.HargaJasa);
        Jumlah = (TextInputEditText) findViewById(R.id.Jumlah);
        btnTglTerima = findViewById(R.id.ibtnTglTerima);
        btnTglSelesai = findViewById(R.id.ibtnTglSelesai);
        btnCariPegawai = findViewById(R.id.ibtnCariPegawai);
        btnCariPelanggan = findViewById(R.id.ibtnCariPelanggan);
        btnCariJasa = findViewById(R.id.ibtnJasa);


//        ImageButton btnTglTerima,btnTglSelesai,btnCariPelanggan,btnCariPegawai,btnCariJasa,btnAddJ,btnRemoveJ;


        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        status = getIntent().getStringExtra("status");
        updateFaktur = getIntent().getStringExtra("faktur");
        if (status.equals("terima")){
            getFaktur();
            Modul.setText(v, R.id.TanggalMulai,Modul.getDate("dd/MM/yyyy"));
            Modul.setText(v, R.id.TanggalKembali,Modul.getDate("dd/MM/yyyy"));
            Cursor c1 = db.sq(Query.selectwhere("tblpegawai")+Query.sWhere("idpegawai","0"));
            c1.moveToFirst();
            Cursor c2 = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan","0"));
            c2.moveToFirst();
            tIdpegawai=Modul.getInt(c1,"idpegawai");
            tnPegawai=Modul.getString(c1,"pegawai");
            tIdpelanggan=Modul.getInt(c2,"idpelanggan");
            tnPelanggan=Modul.getString(c2,"pelanggan");
        }else if (status.equals("update")){
            Modul.setText(v,R.id.edtFaktur,updateFaktur);
            loadCart();
            String q="SELECT * FROM qlaundry WHERE faktur='"+updateFaktur+"'";
            Cursor c=db.sq(q);
            c.moveToNext();

            Modul.setText(v,R.id.TanggalMulai,Modul.dateToNormal(Modul.getString(c,"tgllaundry")));
            Modul.setText(v,R.id.TanggalKembali,Modul.dateToNormal(Modul.getString(c,"tglselesai")));
            tIdpegawai=Modul.getInt(c,"idpegawai");
            tnPegawai=Modul.getString(c,"pegawai");
            tIdpelanggan=Modul.getInt(c,"idpelanggan");
            tnPelanggan=Modul.getString(c,"pelanggan");
            getTotal();
        }

        btnCari();

        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { add(); }
        });
    }

    public void setDate(int i) {
        showDialog(i);
    }

    private void btnCari(){
        btnCariPegawai= findViewById(R.id.ibtnCariPegawai);
        btnCariPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MenuTerimaLaundry.this,ActivityTransaksiCari.class);
                i.putExtra("cari","pegawai");
                startActivityForResult(i,1000);
            }
        });
        btnCariPelanggan=(ImageButton)findViewById(R.id.ibtnNamaPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityTransaksiTerima.this,ActivityTransaksiCari.class);
                i.putExtra("cari","pelanggan");
                startActivityForResult(i,2000);
            }
        });
        btnCariJasa=(ImageButton)findViewById(R.id.ibtnJasa);
        btnCariJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityTransaksiTerima.this,ActivityTransaksiCari.class);
                i.putExtra("cari","jasa");
                startActivityForResult(i,3000);
            }
        });
    }

    public void loadCart(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterTransaksi(this,arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur=Modul.getText(v,R.id.edtFaktur);

        String q=Query.selectwhere("qcart")+Query.sWhere("faktur",tempFaktur);
        Cursor c = db.sq(q);
        if (Modul.getCount(c)>0){
            while (c.moveToNext()){
                String campur=Modul.getString(c,"idlaundrydetail")+"__"+
                        Modul.getString(c,"kategori")+"__"+
                        Modul.getString(c,"jasa")+"__"+
                        Modul.getString(c,"jumlahlaundry")+"__"+
                        Modul.getString(c,"satuan")+"__"+
                        Modul.removeE(Modul.getString(c,"biayalaundry"))+"__Ket : "+
                        Modul.getString(c,"keterangan");
                arrayList.add(campur);
            }
        }else{

        }
        adapter.notifyDataSetChanged();
    }

    public void getTotal(){
        double total=0.0;
        int idlaundry=Integer.valueOf(Modul.getText(v,R.id.edtFaktur));
        Cursor c=db.sq("SELECT SUM(biayalaundry) FROM tbllaundrydetail WHERE idlaundry="+String.valueOf(idlaundry));
        double sum=0.0;
        if (c.moveToFirst()){
            sum = c.getDouble(0);
        }
        total=total+sum;
        totalbayar=String.valueOf(total);
        Modul.setText(v,R.id.tvTotalbayar,"Rp. "+Modul.removeE(totalbayar));
    }

    private void getFaktur(){
//        db.exc("INSERT INTO tbllaundry (total) VALUES (0)");
        List<Integer> idLaundry = new ArrayList<Integer>();
        String q="SELECT idlaundry FROM tbllaundry";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idLaundry.add(Modul.getIntFromCol(c,0));
            }while (c.moveToNext());
        }
        String tempFaktur="";
        int IdFaktur=0;
        if (Modul.getCount(c)==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
        }else {
            IdFaktur = idLaundry.get(Modul.getCount(c)-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }
        Modul.setText(v,R.id.edtFaktur,tempFaktur);
    }


    public void add(){
        String faktur = Faktur.getText().toString();
        String tanggalmulai = TanggalMulai.getText().toString();
        String tanggalkembali = TanggalKembali.getText().toString();
        String namapelanggan = NamaPelanggan.getText().toString();
        String namapegawai = NamaPegawai.getText().toString();
        String hargajasa = HargaJasa.getText().toString();
        String jumlah = Jumlah.getText().toString();
        if(TextUtils.isEmpty(faktur)){
            Toast.makeText(this, "Mohon Isi Dulu", Toast.LENGTH_SHORT).show();
        }
        else{
            db.exc("insert into qproses (faktur,tgllaundry,tglselesai,total,statuslaundry,statusbayar,pelanggan,alamat,notelp) values ('"+ faktur +"','"+ tanggalmulai +"','"+ tanggalkembali +"','"+ namapelanggan +"','"+ namapegawai +"','"+ hargajasa +"','"+ jumlah +"')");
            finish();
        }
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_terima_laundry_taruh_keranjang_laundry,viewGroup,false);
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
                                    ((MenuTerimaLaundry)ctxAdapter).getTotal();
                                    ((MenuTerimaLaundry)ctxAdapter).loadCart();
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
                androidx.appcompat.app.AlertDialog dialog=new androidx.appcompat.app.AlertDialog.Builder(ctxAdapter)
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