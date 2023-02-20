package com.itbrain.aplikasitoko.TokoKain;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Transaksi_Toko_Kain extends AppCompatActivity {

    DatabaseTokoKain db;
    Cursor countMaster,countTransaksi;
    SharedPreferences pref,pref2;
    SharedPreferences.Editor edit;
    String status="";
    String prefid="",prefstats="",android_id,infoTransaction;
    Boolean inAppStatus=false;

    EditText edtJumlahPanjang, edtJumlahLebar;
    Button btnTglOrder, btnCariPelanggan, btnAddK, btnRemoveK, btnAddK1, btnRemoveK1, btnCariKain;
    Spinner sKain;
    TextView tvSatuan;
    View v;

    int year, month, day;
    Calendar calendar;

    String faktur = "00000000";
    int tIdpelanggan, tIdKain, tIdkategori, isikeranjang = 0;
    double tPanjang = .0, tLebar = .0;
    String tnPelanggan, tnKain = "", tBiaya = "", tKategori = "", totalbayar = "", updateFaktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_kain);

        ImageButton imageButton = findViewById(R.id.kembaliitran);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            db=new DatabaseTokoKain(this);
            pref=getSharedPreferences("id",MODE_PRIVATE);
            edit=pref.edit();
            pref2=getSharedPreferences("dataVersion",MODE_PRIVATE);

            android_id = pref2.getString(KumFunTokoKain.encrypt("deviceId"),"");
            infoTransaction = pref.getString(KumFunTokoKain.encrypt("status"),"");

            if (!infoTransaction.isEmpty()){
                String transaction[]=KumFunTokoKain.decrypt(infoTransaction).split("#!");
                prefstats=transaction[0];
                prefid=transaction[1];
            }
            if (android_id.equals(prefid)&&prefstats.equals("premium")){
                status=prefstats;
            }else {
                status="";
            }
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
            }
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            db = new DatabaseTokoKain(this);
            v = this.findViewById(android.R.id.content);
//        setTextProses();

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            status = getIntent().getStringExtra("status");
            updateFaktur = getIntent().getStringExtra("faktur");
            if (status.equals("terima")) {
                cek();
                KumFunTokoKain.setText(v, R.id.tanggal, KumFunTokoKain.getDate("dd/MM/yyyy"));
                Cursor c = db.sq(FQueryTokoKain.selectwhere("tblpelanggan") + FQueryTokoKain.sWhere("idpelanggan", "0"));
                c.moveToFirst();
                tIdpelanggan = KumFunTokoKain.getInt(c, "idpelanggan");
                tnPelanggan = KumFunTokoKain.getString(c, "namapelanggan");
                loadCart();
                getTotal();
            } else if (status.equals("update")) {
                KumFunTokoKain.setText(v, R.id.faktur, updateFaktur);
                loadCart();
                String q = "SELECT * FROM qorder WHERE faktur='" + updateFaktur + "'";
                Cursor c = db.sq(q);
                c.moveToNext();

                KumFunTokoKain.setText(v, R.id.tanggal, KumFunTokoKain.dateToNormal(KumFunTokoKain.getString(c, "tglorder")));
                tIdpelanggan = KumFunTokoKain.getInt(c, "idpelanggan");
                tnPelanggan = KumFunTokoKain.getString(c, "namapelanggan");
                getTotal();
            }

            btnCari();

            btnTglOrder = findViewById(R.id.cariTanggal);
            btnTglOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(1);
                }
            });
            btnAddK = findViewById(R.id.btnPlus);
            btnAddK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tPanjang = tPanjang + .5;
                    setPanjang(tPanjang, tBiaya);
                }
            });
            btnRemoveK = findViewById(R.id.btnMin);
            btnRemoveK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tPanjang = tPanjang - .5;
                    setPanjang(tPanjang, tBiaya);
                }
            });
            edtJumlahPanjang = findViewById(R.id.etJumlahPanjang);


            btnAddK1 = findViewById(R.id.btnPlus1);
            btnAddK1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tLebar = tLebar + .5;
                    setLebar(tLebar, tBiaya);
                }
            });
            btnRemoveK1 = findViewById(R.id.btnMin1);
            btnRemoveK1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tLebar = tLebar - .5;
                    setLebar(tLebar, tBiaya);
                }
            });
            edtJumlahLebar = (EditText) findViewById(R.id.etJumlahLebar);
            TextInputEditText harga = findViewById(R.id.harga);
            harga.addTextChangedListener(new NumberTextWatcher(harga,new Locale("in","ID"),2));
        } catch (Exception e){
            Log.d("SQLUPDATE", "onCreate: "+e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cek(){
        Cursor c = db.sq("SELECT * FROM tblorder WHERE bayar IS NULL");
        if (c.getCount()>0){
            c.moveToLast();
            String faktur = c.getString(c.getColumnIndex("faktur"));
            KumFunTokoKain.setText(v, R.id.faktur,faktur);
        } else {
            getFaktur();
        }
    }

    private void getFaktur() {
//        db.exc("INSERT INTO tblorder (total) VALUES (0)");
        List<Integer> idOrder = new ArrayList<Integer>();
        String q = "SELECT idorder FROM tblorder";
        Cursor c = db.sq(q);
        if (c.moveToNext()) {
            do {
                idOrder.add(c.getInt(0));
            } while (c.moveToNext());
        }
        String tempFaktur = "";
        int IdFaktur = 0;
        if (c.getCount() == 0) {
            tempFaktur = faktur.substring(0, faktur.length() - 1) + "1";
        } else {
            IdFaktur = idOrder.get(c.getCount() - 1) + 1;
            tempFaktur = faktur.substring(0, faktur.length() - String.valueOf(IdFaktur).length()) + String.valueOf(IdFaktur);
        }
        KumFunTokoKain.setText(v, R.id.faktur, tempFaktur);
    }

    private void setEdit(String pelanggan, String kain, String biaya, String kategori) {
        KumFunTokoKain.setText(v, R.id.pelanggan, pelanggan);
        KumFunTokoKain.setText(v, R.id.etNamaKain, kain);
        KumFunTokoKain.setText(v, R.id.harga, biaya);
    }

    private void btnCari() {
        btnCariPelanggan = findViewById(R.id.cariPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaksi_Toko_Kain.this, Activity_Penjualan_Cari_Toko_Kain.class);
                i.putExtra("cari", "pelanggan");
                startActivityForResult(i, 1000);
            }
        });
        btnCariKain = findViewById(R.id.cariKain);
        btnCariKain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaksi_Toko_Kain.this, Activity_Penjualan_Cari_Toko_Kain.class);
                i.putExtra("cari", "kain");
                startActivityForResult(i, 2000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            tIdpelanggan = data.getIntExtra("idpelanggan", 0);
            tnPelanggan = data.getStringExtra("namapelanggan");
        } else if (resultCode == 2000) {
            tIdKain = data.getIntExtra("idkain", 0);
            tIdkategori = data.getIntExtra("idkategori", 0);
            tnKain = data.getStringExtra("namakain");
            tKategori = data.getStringExtra("kategorikain");
            tBiaya = data.getStringExtra("biayakain");
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
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            KumFunTokoKain.setText(v, R.id.tanggal, KumFunTokoKain.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private void setPanjang(Double panjang, String biaya) {
        KumFunTokoKain.setText(v, R.id.etJumlahPanjang, String.valueOf(panjang));
        if (biaya.equals("")) {
            btnAddK.setVisibility(View.INVISIBLE);
        } else {
            btnAddK.setVisibility(View.VISIBLE);
        }
        if (panjang < 1.0) {
            btnRemoveK.setVisibility(View.INVISIBLE);
        } else {
            btnRemoveK.setVisibility(View.VISIBLE);
        }
    }

    private void setLebar(Double lebar, String biaya) {
        KumFunTokoKain.setText(v, R.id.etJumlahLebar, String.valueOf(lebar));
        if (biaya.equals("")) {
            btnAddK1.setVisibility(View.INVISIBLE);
        } else {
            btnAddK1.setVisibility(View.VISIBLE);
        }

        if (lebar < 1.0) {
            btnRemoveK1.setVisibility(View.INVISIBLE);
        } else {
            btnRemoveK1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String kat = "";
        tPanjang = 0;
        tLebar = 0;
        v = this.findViewById(android.R.id.content);
        KumFunTokoKain.setText(v, R.id.etJumlahPanjang, "0");
        KumFunTokoKain.setText(v, R.id.etJumlahLebar, "0");
        kat = tKategori;
        if (!kat.equals("")) {
            kat = tKategori + " - " + tnKain;
        }
        if (!tBiaya.equals("")) {
            setPanjang(0.0, tBiaya);
            setLebar(0.0, tBiaya);
        }

        setEdit(tnPelanggan, kat, tBiaya, tKategori);

        countMaster=db.sq("SELECT * FROM tblkain");
        countTransaksi=db.sq("SELECT * FROM tblorder WHERE total>0");

        if (countMaster.getCount()<6&&countTransaksi.getCount()<6){
            inAppStatus=true;
        }else {
            inAppStatus=false;
        }
    }

    public String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + a[1] + a[0];
    }


    public void simpan(View view) {
        String eFaktur = KumFunTokoKain.getText(v, R.id.faktur);
        String eTglO = KumFunTokoKain.getText(v, R.id.tanggal);
        String eNPelanggan = KumFunTokoKain.getText(v, R.id.pelanggan);
        String eKain = KumFunTokoKain.getText(v, R.id.etNamaKain);
        String eHarga = KumFunTokoKain.unNumberFormat(KumFunTokoKain.getText(v, R.id.harga));
        String eJumlahP = KumFunTokoKain.getText(v, R.id.etJumlahPanjang);
        String eJumlahL = KumFunTokoKain.getText(v, R.id.etJumlahLebar);
        String jumlah = KumFunTokoKain.getText(v, R.id.etJumlah);
        String eKet = KumFunTokoKain.getText(v, R.id.etKeterangan).replace("\n", "  ");
        if (TextUtils.isEmpty(eTglO) ||
                TextUtils.isEmpty(eNPelanggan) || TextUtils.isEmpty(eKain) || TextUtils.isEmpty(eJumlahP) || TextUtils.isEmpty(eJumlahL) || KumFunTokoKain.strToDouble(eHarga) == 0 || KumFunTokoKain.strToDouble(eJumlahP) == 0 || KumFunTokoKain.strToDouble(eJumlahL) == 0 || KumFunTokoKain.strToDouble(jumlah) == 0) {
            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
        } else {
            String idPelanggan = String.valueOf(tIdpelanggan);
            String idKain = String.valueOf(tIdKain);
            Integer idorder = Integer.valueOf(eFaktur);
            Double bay = ((KumFunTokoKain.strToDouble(eJumlahP) * KumFunTokoKain.strToDouble(eJumlahL)) * KumFunTokoKain.strToDouble(eHarga)) * KumFunTokoKain.strToDouble(jumlah);
            String qOrderD, qOrder;
            String[] detail = {
                    idKain,
                    String.valueOf(idorder),
                    eJumlahP,
                    eJumlahL,
                    jumlah,
                    String.valueOf(bay),
                    eKet
            };
            String[] simpan = {
                    String.valueOf(idorder),
                    idPelanggan,
                    eFaktur,
                    convertDate(eTglO)
            };

            String q = FQueryTokoKain.selectwhere("tblorder") + FQueryTokoKain.sWhere("faktur", eFaktur);
            Cursor c = db.sq(q);
            if (c.getCount() == 0) {
                Toast.makeText(this, "halo", Toast.LENGTH_SHORT).show();
                qOrder = FQueryTokoKain.splitParam("INSERT INTO tblorder (idorder,idpelanggan,faktur,tglorder) VALUES (?,?,?,?)", simpan);
                qOrderD = FQueryTokoKain.splitParam("INSERT INTO tblorderdetail (idkain,idorder,panjang,lebar,jumlah,hargaakhir,keterangan) VALUES (?,?,?,?,?,?,?)", detail);
            } else {
                qOrder = "UPDATE tblorder SET " +
                        "idpelanggan=" + idPelanggan + "," +
                        "tglorder=" + convertDate(eTglO) +
                        " WHERE idorder=" + String.valueOf(idorder);
                qOrderD = FQueryTokoKain.splitParam("INSERT INTO tblorderdetail (idkain,idorder,panjang,lebar,jumlah,hargaakhir,keterangan) VALUES (?,?,?,?,?,?,?)", detail);
            }
            String kain = "UPDATE tblkain SET biaya = '"+eHarga+"' WHERE idkain = '"+String.valueOf(tIdKain)+"'";
            Log.d("SQLUPDATE", "simpan: "+qOrder);
            Log.d("SQLUPDATE", "simpan: "+qOrderD);

            if (db.exc(qOrder) && db.exc(qOrderD) && db.exc(kain)) {
                Toast.makeText(this, "Sukses!", Toast.LENGTH_SHORT).show();
                getTotal();
                clearText();
                loadCart();
            }
        }
    }

    public void jumlah(){
        final Button plus = findViewById(R.id.btnPlusBayar);
        final Button min = findViewById(R.id.btnMinBayar);
        final EditText etJumlah = findViewById(R.id.etJumlah);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = KumFunTokoKain.strToInt(KumFunTokoKain.getText(v, R.id.etJumlah)) + 1;
                etJumlah.setText(String.valueOf(jumlah));
                min.setVisibility(View.VISIBLE);
            }
        });
        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jumlah = KumFunTokoKain.strToInt(KumFunTokoKain.getText(v, R.id.etJumlah)) - 1;
                etJumlah.setText(String.valueOf(jumlah));
                if (KumFunTokoKain.strToInt(KumFunTokoKain.getText(v, R.id.etJumlah)) == 1) {
                    min.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void getTotal() {
        jumlah();
        int total = 0;
        int idorder = Integer.valueOf(KumFunTokoKain.getText(v, R.id.faktur));
        Cursor c = db.sq("SELECT SUM(hargaakhir) FROM tblorderdetail WHERE idorder=" + String.valueOf(idorder));
        int sum = 0;
        if (c.moveToFirst()) {
            sum = c.getInt(0);
        }
        total = total + sum;
        totalbayar = String.valueOf(total);
        KumFunTokoKain.setText(v, R.id.total, "Rp. " + KumFunTokoKain.numberFormat(totalbayar));
    }

    public void loadCart() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recJual);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterTransaksi(this, arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur = KumFunTokoKain.getText(v, R.id.faktur);

        String q = FQueryTokoKain.selectwhere("qorder") + FQueryTokoKain.sWhere("faktur", tempFaktur);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = KumFunTokoKain.getString(c, "idorderdetail") + "__" +
                        KumFunTokoKain.getString(c, "kategori") + "__" +
                        KumFunTokoKain.getString(c, "kain") + "__" +
                        KumFunTokoKain.getString(c, "panjang") + "__" +
                        KumFunTokoKain.getString(c, "lebar") + "__" +
                        KumFunTokoKain.getString(c, "jumlah") + "__" +
                        KumFunTokoKain.removeE(KumFunTokoKain.getString(c, "hargaakhir")) + "__Ket : " +
                        KumFunTokoKain.getString(c, "keterangan");
                arrayList.add(campur);
            }
        } else {

        }
        adapter.notifyDataSetChanged();
    }


    private void clearText() {
        KumFunTokoKain.setText(v, R.id.etNamaKain, "");
        KumFunTokoKain.setText(v, R.id.harga, "");
        KumFunTokoKain.setText(v, R.id.etJumlahPanjang, "");
        KumFunTokoKain.setText(v, R.id.etJumlahLebar, "");
        KumFunTokoKain.setText(v, R.id.etKeterangan, "");
        KumFunTokoKain.setText(v, R.id.etJumlah, "1");
        setPanjang(.0, "");
        setLebar(.0, "");
    }

    public void selesaiK(View view) {
        String eTglS = KumFunTokoKain.getText(v, R.id.tanggal);
        String eNPelanggan = KumFunTokoKain.getText(v, R.id.pelanggan);
        final String faktur = KumFunTokoKain.getText(v, R.id.faktur);
        String pelanggan = KumFunTokoKain.getText(v, R.id.pelanggan);

        Cursor c = db.sq("SELECT * FROM qorder WHERE faktur='" + faktur + "'");
        isikeranjang = c.getCount();
        if (TextUtils.isEmpty(eTglS) || TextUtils.isEmpty(eNPelanggan) || isikeranjang == 0) {
            Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_SHORT).show();
        } else {
            if (db.exc("UPDATE tblorder SET total="+totalbayar+" WHERE faktur='"+faktur+"'")) {
                Intent i = new Intent(this, Activity_Bayar_Toko_Kain.class) ;
                i.putExtra("faktur",faktur) ;
                startActivity(i);
            }else{
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();

            }
        }


    }


}
class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder> {
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterTransaksi(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_terima_kain, viewGroup, false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int i) {
        final String[] row = data.get(i).split("__");


        holder.kain.setText("(" + row[1] + ") " + row[2] + " - p" + row[3] + " - l" + row[4] + " - jumlah " + row[5].replace(".", ","));
        holder.kain.setSelected(true);

        holder.harga.setText("Rp. " + row[6]);
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseTokoKain db = new DatabaseTokoKain(ctxAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                builder.create();
                builder.setMessage("Anda yakin ingin menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tblorderdetail WHERE idorderdetail=" + row[0];
                                if (db.exc(q)) {
                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
                                    ((Transaksi_Toko_Kain) ctxAdapter).getTotal();
                                    ((Transaksi_Toko_Kain) ctxAdapter).loadCart();
                                } else {
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
        if (row[7].equals("Ket : ")) {
            holder.keterangan.setText("Tanpa Keterangan");
        } else {
            holder.keterangan.setText(row[7]);
        }
        holder.keterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (row[7].equals("Ket : ")) {
                    message = "Tanpa Keterangan";
                } else {
                    message = row[7];
                }
                androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(ctxAdapter)
                        .setTitle("Keterangan " + row[1] + " - " + row[2])
                        .setMessage(message)
                        .setPositiveButton("Ok", null)
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TransaksiViewHolder extends RecyclerView.ViewHolder {
        TextView kain, harga, keterangan;
        ImageButton hapus;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            kain = (TextView) itemView.findViewById(R.id.tvNamaKain);
            harga = (TextView) itemView.findViewById(R.id.tvHargaJumlah);
            keterangan = (TextView) itemView.findViewById(R.id.tvKeterangan);
            hapus = (ImageButton) itemView.findViewById(R.id.ibtnHapus);
        }
    }
}




