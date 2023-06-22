package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;
import com.itbrain.aplikasitoko.TokoKredit.FConfigKredit;
import com.itbrain.aplikasitoko.TokoKredit.FKoneksiKredit;


public class DialogBayarTagihanKredit extends AppCompatDialogFragment {

    String faktur;
    EditText eTerlambat, eDenda, eBayarDenda, eAngsuran;
    FConfigKredit config;
    FKoneksiKredit conn;
    ListenerDialogTagihan listenerDialogTagihan;
    Double angsuran, denda, bayarDenda;
    Integer jumlahTerlambat;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listenerDialogTagihan = (ListenerDialogTagihan) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement ListenerDialogBayarPesanan");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_bayar_tagihan_kredit, null);

        init(view);
        showDataInfield();

        builder.setView(view).setTitle("Informasi");
        builder.setPositiveButton("bayar", null);
        builder.setNegativeButton("batal", null);
        return builder.create();
    }

    void init(View itemView) {
        eTerlambat = itemView.findViewById(R.id.eTerlambat);
        eDenda = itemView.findViewById(R.id.eDenda);
        eBayarDenda = itemView.findViewById(R.id.eBayarDenda);
        eAngsuran = itemView.findViewById(R.id.eAngsuran);
        config = new FConfigKredit(getActivity().getSharedPreferences("config", Context.MODE_PRIVATE));
        conn = new FKoneksiKredit(getActivity(), config);
        faktur = getArguments().getString("faktur");
        Log.i("info", faktur);
    }

    void showDataInfield() {
        Cursor c = conn.sq("SELECT * FROM qtagihan WHERE faktur = '" + faktur + "' AND tgltempo IN (SELECT MIN(tgltempo) FROM qtagihan WHERE faktur = '" + faktur + "' AND flag_tagihan = 0)");
        c.moveToFirst();

        String tglSekarang = ModuleKredit.getCurrentDate();
//        String tglSekarang = "2019-02-28";
        String tglTempo = c.getString(c.getColumnIndex("tgltempo"));

        int selisihHari = ModuleKredit.diffBetween(tglSekarang, tglTempo);
        angsuran = c.getDouble(c.getColumnIndex("jumlahangsuran"));

        jumlahTerlambat = 0;
        denda = 0.0;
        bayarDenda = 0.0;
        if (selisihHari < 0) {
            jumlahTerlambat = selisihHari * -1;
            denda = c.getDouble(c.getColumnIndex("jumlahdenda"));
            bayarDenda = denda * jumlahTerlambat;
        }
        eTerlambat.setText(jumlahTerlambat.toString());
        eDenda.setText(ModuleKredit.numFormat(denda));
        eBayarDenda.setText(ModuleKredit.numFormat(bayarDenda));
        eAngsuran.setText(ModuleKredit.numFormat(angsuran));
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog currentDialog = (AlertDialog) getDialog();
        Button positiveButton, negativeButton;
        positiveButton = currentDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = currentDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(v -> {
            String updateTagihan = "UPDATE tbltagihan SET flag_tagihan = 1, tglbayar = '" + ModuleKredit.getCurrentDate() + "', bayardenda = " + bayarDenda + ", jumlahterlambat = " + jumlahTerlambat + " " +
                    "WHERE idkredit IN (SELECT idkredit FROM tblkredit WHERE faktur = '" + faktur + "') AND tgltempo IN (SELECT MIN(tgltempo) FROM tbltagihan WHERE idkredit IN (SELECT idkredit FROM tblkredit WHERE faktur = '" + faktur + "') AND flag_tagihan = 0)";
            String updateSaldo = "UPDATE tblkredit SET saldokredit = saldokredit - angsuran WHERE faktur = '" + faktur + "'";
            if (conn.exc(updateTagihan) && conn.exc(updateSaldo)) {
                Cursor c = conn.sq("SELECT * FROM qtagihan WHERE faktur = '" + faktur + "' AND flag_tagihan = 0");
                // Jika kredit sudah lunas
                if (c.getCount() == 0) {
                    String updateKredit = "UPDATE tblkredit SET flag_kredit = 1 WHERE faktur = '" + faktur + "'";
                    conn.exc(updateKredit);
                }
                ModuleKredit.info(getActivity(), "Pembayaran berhasil");
                listenerDialogTagihan.onCreditPaid(faktur);
                currentDialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(v -> currentDialog.dismiss());
    }

    public interface ListenerDialogTagihan {
        void onCreditPaid(String faktur);
    }

}
