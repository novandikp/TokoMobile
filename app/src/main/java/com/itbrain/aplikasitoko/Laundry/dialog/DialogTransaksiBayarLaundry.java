package com.itbrain.aplikasitoko.Laundry.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.Laundry.DatabaseLaundry;
import com.itbrain.aplikasitoko.Laundry.LaundryMenuTransaksi;
import com.itbrain.aplikasitoko.Laundry.MenuCetaklaundry;
import com.itbrain.aplikasitoko.Laundry.ModulLaundry;
import com.itbrain.aplikasitoko.Laundry.NumberTextWatcherKembali;
import com.itbrain.aplikasitoko.Laundry.QueryLaundry;
import com.itbrain.aplikasitoko.R;

import java.util.Locale;

public class DialogTransaksiBayarLaundry {
    Context context;
    View dialogView;
    DatabaseLaundry db;

    public DialogTransaksiBayarLaundry(@NonNull final Context context, @NonNull String faktur) {
        this.context = context;
        db=new DatabaseLaundry(context);
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_transaksi_bayar_laundry,null);

        Cursor c=db.sq("SELECT * FROM tbllaundry WHERE faktur='"+faktur+"'");
        c.moveToNext();
        ModulLaundry.setText(dialogView, R.id.viewFaktur,faktur);
        ModulLaundry.setText(dialogView,R.id.edtTotalBayar, ModulLaundry.removeE(ModulLaundry.getString(c,"total")));
        double kembali=0.0- ModulLaundry.strToDouble(ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtTotalBayar)));
        ModulLaundry.setText(dialogView,R.id.edtKembali, ModulLaundry.removeE(ModulLaundry.doubleToStr(kembali)));
        final Integer idpelanggan= ModulLaundry.getInt(c,"idpelanggan");
        final String[] metodeBayar = new String[1];
        Spinner spMetode=dialogView.findViewById(R.id.spinMetodeBayar);
        spMetode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metodeBayar[0] =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TextInputEditText eJumlahBayar=dialogView.findViewById(R.id.edtJumlahBayar);
        EditText edtKembali=dialogView.findViewById(R.id.edtKembali);
        eJumlahBayar.addTextChangedListener(new NumberTextWatcherKembali(eJumlahBayar,new Locale("in","ID"),2, ModulLaundry.justRemoveE(ModulLaundry.getString(c,"total")),edtKembali));

        final AlertDialog dialog=new AlertDialog.Builder(context,R.style.CustomDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageButton btnClose=dialogView.findViewById(R.id.ibtnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button btnBayar=dialogView.findViewById(R.id.btnBayar);
                btnBayar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String faktur= ModulLaundry.getText(dialogView,R.id.viewFaktur);
                        final String mBayar=metodeBayar[0];
                        String total= ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtTotalBayar));
                        final String jBayar= ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtJumlahBayar));
                        final String kembali= ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtKembali));
                        String tglbayar= ModulLaundry.getDate("yyyyMMdd");

                        if (mBayar.equals("Tunai")&& ModulLaundry.strToInt(total)> ModulLaundry.strToInt(jBayar)){
                            Toast.makeText(context, "Uang bayar tidak cukup!", Toast.LENGTH_SHORT).show();
                        }else if (mBayar.equals("Hutang")&& ModulLaundry.strToInt(kembali)>0){
                            Toast.makeText(context, "Uang cukup untuk pembayaran tunai", Toast.LENGTH_SHORT).show();
                        }else if (idpelanggan==0&&mBayar.equals("Hutang")){
                            Toast.makeText(context, "Sistem bayar hutang hanya berlaku untuk pelanggan terdaftar", Toast.LENGTH_SHORT).show();
                        }else {
                            final String q = "UPDATE tbllaundry SET " +
                                    "bayar=" + jBayar + "," +
                                    "kembali=" + kembali + "," +
                                    "tglbayar=" + tglbayar + "," +
                                    "statuslaundry='Selesai'," +
                                    "statusbayar='" + mBayar + "'" +
                                    " WHERE faktur='" + faktur + "'";
                            Cursor cKeuangan = db.sq(QueryLaundry.select("tblkeuangan"));
                            cKeuangan.moveToLast();
                            double saldo = 0;
                            final String[] pKeuangan = new String[5];
                            if (mBayar.equals("Tunai")) {
                                if (cKeuangan.getCount() > 0) {
                                    saldo = ModulLaundry.strToDouble(total) + cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"));
                                } else {
                                    saldo = ModulLaundry.strToDouble(total);
                                }
                                pKeuangan[0] = ModulLaundry.getDate("yyyyMMdd");
                                pKeuangan[1] = faktur;
                                pKeuangan[2] = "Pendapatan Jasa";
                                pKeuangan[3] = total;
                                pKeuangan[4] = String.valueOf(saldo);
                            } else {
                                if (cKeuangan.getCount() > 0) {
                                    saldo = ModulLaundry.strToDouble(jBayar) + cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"));
                                } else {
                                    saldo = ModulLaundry.strToDouble(jBayar);
                                }
                                pKeuangan[0] = ModulLaundry.getDate("yyyyMMdd");
                                pKeuangan[1] = faktur;
                                pKeuangan[2] = "Pendapatan Jasa";
                                pKeuangan[3] = jBayar;
                                pKeuangan[4] = String.valueOf(saldo);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.create();
                            builder.setMessage("Konfirmasi pesanan laundry " + faktur + " selesai?");
                           builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (db.exc(q)){
                                        if (ModulLaundry.strToDouble(jBayar)>0){
                                            if (db.exc(QueryLaundry.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

                                            }else {
                                                Toast.makeText(context, "Gagal Menambahkan Data Keuangan", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        if (mBayar.equals("Hutang")){
                                            Cursor c=db.sq("SELECT * FROM tbllaundry WHERE faktur='"+faktur+"'");
                                            c.moveToNext();
                                            String idpelanggan= ModulLaundry.getString(c,"idpelanggan");
                                            db.exc("UPDATE tblpelanggan SET " +
                                                    "hutang=hutang+"+ ModulLaundry.removeMinus(kembali)+
                                                    " WHERE idpelanggan="+idpelanggan);
                                        }
                                        Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        AlertDialog dialogCetak=new AlertDialog.Builder(context)
                                                .setMessage("Simpan data berhasil")
                                                .setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i=new Intent(context, MenuCetaklaundry.class);
                                                        i.putExtra("faktur",faktur);
                                                        context.startActivity(i);
                                                    }
                                                }).setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        context.startActivity(new Intent(context, LaundryMenuTransaksi.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    }
                                                })
                                                .setCancelable(false)
                                                .create();
                                        dialogCetak.show();
                                    }else {
                                        Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            builder.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Lakukan Pembayaran jika proses laundry sudah selesai", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
