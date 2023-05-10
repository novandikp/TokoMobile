package com.itbrain.aplikasitoko.Laundry;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ActivityTransaksiProsesLaundry extends AppCompatActivity {
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_proses_laundry);
        db=new DatabaseLaundry(this);
        ImageButton i = findViewById(R.id.Kembali);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getProses("");

        final EditText edtCari=(EditText)findViewById(R.id.eCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword=edtCari.getText().toString();
                getProses(keyword);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getProses(String keyword){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcProses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter= new AdapterListProses(this,arrayList);
        recyclerView.setAdapter(adapter);

        String q;
        if (keyword.isEmpty()){
            q= QueryLaundry.selectwhere("qproses")+ QueryLaundry.sWhere("statuslaundry","Proses")+ QueryLaundry.sOrderASC("tgllaundry");
        }else {
            q= QueryLaundry.selectwhere("qproses")+"("+ QueryLaundry.sWhere("statuslaundry","Proses")+" AND "+ QueryLaundry.sLike("pelanggan",keyword)+") OR ("+ QueryLaundry.sWhere("statuslaundry","Proses")+" AND "+ QueryLaundry.sLike("faktur",keyword)+") "+ QueryLaundry.sOrderASC("tgllaundry");
        }

        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idlaundry")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"tgllaundry")+"__"+
                        ModulLaundry.getString(c,"tglselesai")+"__"+
                        ModulLaundry.getString(c,"faktur")+"__"+
                        ModulLaundry.getString(c,"notelp")+"__"+
                        ModulLaundry.getString(c,"alamat")+"__"+
                        ModulLaundry.getString(c,"idpelanggan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
class AdapterListProses extends RecyclerView.Adapter<AdapterListProses.ProsesViewHolder>{
    private ArrayList<String> data;
    private Context ctx;

    public AdapterListProses(Context ctx, ArrayList<String> data) {
        this.data = data;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ProsesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_transaksi_prose_laundrys,viewGroup,false);
        return new ProsesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProsesViewHolder holder, final int i) {
        final String[] row=data.get(i).split("__");

        holder.nama.setText(row[1]);
        holder.tglselesai.setText(ModulLaundry.dateToNormal(row[2])+" - "+ ModulLaundry.dateToNormal(row[3]));
        holder.faktur.setText(row[4]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx,MenuDaftarProsesTotalBayarLaundry.class);
                i.putExtra("faktur",row[4]);
                ctx.startActivity(i);
            }
        });
        if (ModulLaundry.strToInt(row[7])==0){
            holder.btnWa.setVisibility(View.INVISIBLE);
            holder.btnSms.setVisibility(View.INVISIBLE);
            holder.btnTelp.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ctx,holder.itemView);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Intent intent = new Intent(ctx, ActivityTransaksiTerimaLaundry.class);
                                intent.putExtra("status","update");
                                intent.putExtra("faktur",row[4]);
                                ctx.startActivity(intent);
                                break;
                            case R.id.menu_Hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseLaundry db = new DatabaseLaundry(ctx);
                                        String q = "DELETE FROM tbllaundry WHERE idlaundry="+row[0];
                                        if (db.exc(q)){
                                            data.remove(i);
                                            notifyDataSetChanged();
                                            Toast.makeText(ctx, "Delete pesanan "+row[4]+" berhasil", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ctx, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus pesanan "+row[4])
                                        .setMessage("Anda yakin ingin menghapus "+row[4]+" dari data pesanan");
                                builder.show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        Long notelp = null;
        if (!row[5].equals(" ")){
            notelp=Long.valueOf(row[5]);
        }
        final Long finalNotelp = notelp;
        ReceiptBuilderLaundry receiptBuilder = new ReceiptBuilderLaundry(row[4],ctx);
        holder.btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
////
                   Intent sendIntent =new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.setPackage("com.whatsapp");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra("jid", "62"+String.valueOf(finalNotelp) +"@s.whatsapp.net");
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT,receiptBuilder.getFaktur());
                    ctx.startActivity(sendIntent);
                        PackageManager packageManager = ctx.getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        String url = "https://api.whatsapp.com/send?phone=" + "62" + String.valueOf(finalNotelp) + "&text=" + URLEncoder.encode(receiptBuilder.getFaktur(), "UTF-8");

                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            ctx.startActivity(i);
                        }

                    } catch (Exception e) {
                        Toast.makeText(ctx, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        holder.btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "+62"+String.valueOf(finalNotelp), null)));
                }catch (Exception e){
                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.btnTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+"+"+ ModulLaundry.getCurrentCountryCode(ctx)+String.valueOf(finalNotelp)));
                    ctx.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ProsesViewHolder extends RecyclerView.ViewHolder{
        TextView nama,tglselesai,faktur;
        ImageButton btnWa,btnSms,btnTelp;
        public ProsesViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaPelanggan);
            tglselesai=(TextView)itemView.findViewById(R.id.tvTglSelesai);
            faktur=(TextView)itemView.findViewById(R.id.tvFaktur);
            btnWa=(ImageButton)itemView.findViewById(R.id.ibtnWhatsapp);
            btnSms=(ImageButton)itemView.findViewById(R.id.ibtnSms);
            btnTelp=(ImageButton)itemView.findViewById(R.id.ibtnCall);
        }
    }
}
