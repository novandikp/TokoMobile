package com.itbrain.aplikasitoko.dialogTabungan;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.tabungan.DatabaseTabungan;
import com.itbrain.aplikasitoko.tabungan.ModulTabungan;
import com.itbrain.aplikasitoko.tabungan.QueryTabungan;

//import com.komputerkit.aplikasitabunganplus.Database;
//import com.komputerkit.aplikasitabunganplus.Modul;
//import com.komputerkit.aplikasitabunganplus.Query;
//import com.komputerkit.aplikasitabunganplus.R;

public class DialogPanggil {
    Context context;
    String idpelanggan;
    DatabaseTabungan db;
    private TextView mTvNoTelp;
    private ConstraintLayout mCTelp, mCSms, mCWa;

    public DialogPanggil(final Context context, String idpelanggan) {
        this.context = context;
        this.idpelanggan = idpelanggan;
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_panggil_tabungan, null);
        db = new DatabaseTabungan(context);
        //init view
        mTvNoTelp = (TextView) dialogView.findViewById(R.id.tvNoTelp);
        mCTelp = (ConstraintLayout) dialogView.findViewById(R.id.cTelp);
        mCSms = (ConstraintLayout) dialogView.findViewById(R.id.cSms);
        mCWa = (ConstraintLayout) dialogView.findViewById(R.id.cWa);

        Cursor c = db.sq(QueryTabungan.selectwhere("tblanggota") + QueryTabungan.sWhere("idanggota", idpelanggan));
        c.moveToFirst();
        final String notelp1 = "+" + ModulTabungan.getCurrentCountryCode(context) + ModulTabungan.getString(c, "notelpanggota");

        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.CustomDialog2)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                mTvNoTelp.setText(notelp1);
                mCTelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:" + notelp1));
                        context.startActivity(i);
                        dialog.dismiss();
                    }
                });
                mCSms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + notelp1));
                        context.startActivity(i);
                        dialog.dismiss();
                    }
                });
                mCWa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String number = notelp1.replace(" ", "").replace("+", "");
                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
                            context.startActivity(sendIntent);

                        } catch (Exception e) {
                            Toast.makeText(context, R.string.nowhatsapp, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);
        dialog.show();
    }

}
