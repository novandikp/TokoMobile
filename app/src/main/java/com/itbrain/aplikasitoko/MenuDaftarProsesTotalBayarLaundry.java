package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MenuDaftarProsesTotalBayarLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;
    String faktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menudaftarprosestotalbayarlaundry);
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().hide();
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);
        faktur=getIntent().getStringExtra("faktur");
        loadcartproses();
        setTextProses();
    }
    public void setTextProses(){
        String q=Query.selectwhere("qlaundry")+Query.sWhere("faktur",faktur);
        Cursor c=db.sq(q);
        c.moveToNext();
        Modul.setText(v,R.id.edtFaktur,faktur);
        Modul.setText(v,R.id.edtTanggalTerima,Modul.dateToNormal(Modul.getString(c,"tgllaundry")));
        Modul.setText(v,R.id.edtTanggalSelesai,Modul.dateToNormal(Modul.getString(c,"tglselesai")));
        Modul.setText(v,R.id.edtNamaPegawai,Modul.getString(c,"pegawai"));
        Modul.setText(v,R.id.edtNamaPelanggan,Modul.getString(c,"pelanggan"));
        Modul.setText(v,R.id.tvTotalBayar,"Rp. "+Modul.removeE(Modul.getString(c,"total")));
        String q2=Query.selectwhere("qproses")+Query.sWhere("faktur",faktur);
        Cursor c2=db.sq(q2);
        c2.moveToNext();
        Modul.setText(v,R.id.edtAlamat,Modul.getString(c2,"alamat"));
        Modul.setText(v,R.id.edtNomorTelepon,Modul.getString(c2,"notelp"));
        TextInputLayout alamat,notelp;
        alamat=(TextInputLayout)findViewById(R.id.textInputLayout26);
        notelp=(TextInputLayout)findViewById(R.id.textInputLayout27);
        if (Modul.getInt(c,"idpelanggan")==0){
            alamat.setVisibility(View.GONE);
            notelp.setVisibility(View.GONE);
        }
    }
    public void loadcartproses(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.Reclist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListProsesBayar(this,arrayList);
        recyclerView.setAdapter(adapter);

        String q=Query.selectwhere("qcart")+Query.sWhere("faktur",faktur);
        Cursor c = db.sq(q);
        if (Modul.getCount(c)>0){
            while (c.moveToNext()){
                String campur=Modul.getString(c,"idlaundrydetail")+"__"+
                        Modul.getString(c,"kategori")+"__"+
                        Modul.getString(c,"jasa")+"__"+
                        Modul.getString(c,"jumlahlaundry")+"__"+
                        Modul.getString(c,"satuan")+"__"+
                        Modul.getString(c,"biayalaundry")+"__Ket : "+
                        Modul.getString(c,"keterangan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void bayar(View view) {
        Intent i = new Intent(this,MenuDaftarProsesBayarLaundry.class);
        i.putExtra("faktur",faktur);
        startActivity(i);
    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Tersalin", Toast.LENGTH_SHORT).show();
        }
    }

    public void kopiNama(View view) {
        setClipboard(this,Modul.getText(v,R.id.edtNamaPelanggan));
    }
    public void kopiAlamat(View view) {
        setClipboard(this,Modul.getText(v,R.id.edtAlamatPelanggan));
    }
    public void kopiNoTelp(View view) {
        setClipboard(this,Modul.getText(v,R.id.edtNomorTelepon));
    }
}
class AdapterListProsesBayar extends RecyclerView.Adapter<AdapterListProsesBayar.ProsesBayarViewHolder>{
    private ArrayList<String> data=new ArrayList<>();
    private Context ctx;

    public AdapterListProsesBayar(Context ctx, ArrayList<String> data) {
        this.data = data;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ProsesBayarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_terima_laundry_taruh_keranjang_laundry,viewGroup,false);
        return new ProsesBayarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProsesBayarViewHolder holder, int i) {
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

        holder.harga.setText("Rp. "+Modul.removeE(row[5]));
        holder.harga.setSelected(true);
        holder.hapus.setVisibility(View.GONE);
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
                AlertDialog dialog=new AlertDialog.Builder(ctx)
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

    class ProsesBayarViewHolder extends RecyclerView.ViewHolder{
        TextView jasa,jumlah,satuan,harga,keterangan;
        ImageButton hapus;
        public ProsesBayarViewHolder(@NonNull View itemView) {
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
