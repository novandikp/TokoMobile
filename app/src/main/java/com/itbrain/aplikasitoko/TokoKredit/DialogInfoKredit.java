package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;
import com.itbrain.aplikasitoko.TokoKredit.FConfigKredit;
import com.itbrain.aplikasitoko.TokoKredit.FFunctionKredit;
import com.itbrain.aplikasitoko.TokoKredit.FKoneksiKredit;


public class DialogInfoKredit extends AppCompatDialogFragment {

    TextView tvFaktur, tvPelanggan, tvTgl, tvJumlah, tvCicil, tvSisa, tvAngsuran, tvDenda, tvSaldo, tvStatus;
    String faktur;
    FKoneksiKredit conn;
    FConfigKredit config;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info_kredit, null);

        init(view);
        showDataInField();

        builder.setView(view).setTitle("Informasi");
        builder.setPositiveButton("tutup", (dialog, which) -> {
            dialog.dismiss();
        });
        return builder.create();
    }

    void init(View itemView) {
        tvFaktur = itemView.findViewById(R.id.tvFaktur);
        tvPelanggan = itemView.findViewById(R.id.tvPelanggan);
        tvTgl = itemView.findViewById(R.id.tvTgl);
        tvJumlah = itemView.findViewById(R.id.tvJumlah);
        tvCicil = itemView.findViewById(R.id.tvCicil);
        tvSisa = itemView.findViewById(R.id.tvSisa);
        tvAngsuran = itemView.findViewById(R.id.tvAngsuran);
        tvDenda = itemView.findViewById(R.id.tvDenda);
        tvSaldo = itemView.findViewById(R.id.tvSaldo);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        faktur = getArguments().getString("faktur");
        config = new FConfigKredit(getActivity().getSharedPreferences("config", Context.MODE_PRIVATE));
        conn = new FKoneksiKredit(getActivity(), config);
    }

    void showDataInField() {
        Cursor c = conn.sq("SELECT * FROM qkredit WHERE faktur = '" + faktur + "'");
        c.moveToFirst();

        Cursor tagihan = conn.sq("SELECT * FROM qtagihan WHERE faktur = '" + faktur + "' AND flag_tagihan = 0");
        Integer sisacicil = tagihan.getCount();

        tvFaktur.setText(c.getString(c.getColumnIndex("faktur")));
        tvPelanggan.setText(c.getString(c.getColumnIndex("pelanggan")));
        tvTgl.setText(FFunctionKredit.getDateString(c.getString(c.getColumnIndex("tglkredit"))));
        tvJumlah.setText(ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("angsuran")) * c.getDouble(c.getColumnIndex("lamakredit"))));
        tvCicil.setText(c.getString(c.getColumnIndex("lamakredit")));
        tvSisa.setText(sisacicil.toString());
        tvAngsuran.setText(ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("angsuran"))));
        tvDenda.setText(ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("denda"))));
        tvSaldo.setText(ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("saldokredit"))));
        tvStatus.setText(c.getInt(c.getColumnIndex("flag_kredit")) == 1 ? "Lunas" : "Belum Lunas");
    }

}
