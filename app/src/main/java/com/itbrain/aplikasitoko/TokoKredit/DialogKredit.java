package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;
import com.itbrain.aplikasitoko.TokoKredit.FConfigKredit;
import com.itbrain.aplikasitoko.TokoKredit.FKoneksiKredit;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogKredit extends AppCompatDialogFragment {

    EditText eAngsuran, eJumlahCicilan, eDenda, eLamaKredit, eJumlahKredit;
    String faktur;
    Spinner spLamaKredit;
    private ListenerDialogKredit listener;
    FConfigKredit config;
    List<String> daftarCicilan;
    Double jumlahcicilan, jumlahkredit;
    int lamakredit;
    FKoneksiKredit db;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_kredit, null);

        init(view);
//        fillSpinner();
//        handleSpinnerChange();
        handleLamaKreditChange();

        builder.setView(view).setTitle("Konfirmasi")
                .setNegativeButton("batal", null)
                .setPositiveButton("konfirm", null);

        eJumlahKredit.setText(ModuleKredit.numFormat(jumlahkredit));
        eLamaKredit.requestFocus();

        return builder.create();
    }

    private void init(View view) {
        eAngsuran = view.findViewById(R.id.eAngsuran);
        eJumlahCicilan = view.findViewById(R.id.eJumlahCicilan);
        eDenda = view.findViewById(R.id.eDenda);
        eLamaKredit = view.findViewById(R.id.eLamaKredit);
        eJumlahKredit = view.findViewById(R.id.eJumlahKredit);
//        spLamaKredit = view.findViewById(R.id.spLamaKredit);
        config = new FConfigKredit(getActivity().getSharedPreferences("config", Context.MODE_PRIVATE));
        db = new FKoneksiKredit(getActivity(), config);
        daftarCicilan = new ArrayList<>(Arrays.asList("10", "20"));
        faktur = getArguments().getString("faktur");
        jumlahkredit = getArguments().getDouble("jumlahkredit");
    }

    private void fillSpinner() {
        ArrayAdapter metodeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, daftarCicilan);
        metodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLamaKredit.setAdapter(metodeAdapter);
    }

    void handleSpinnerChange() {
        spLamaKredit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lamakredit = Integer.parseInt(daftarCicilan.get(position));
                jumlahcicilan = jumlahkredit / lamakredit;
                eJumlahCicilan.setText(ModuleKredit.numFormat(jumlahcicilan));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void handleLamaKreditChange() {
        eLamaKredit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eLamaKredit.getText().length() > 0) {
                    lamakredit = Integer.parseInt(eLamaKredit.getText().toString());
                    jumlahcicilan = jumlahkredit / lamakredit;
                    eJumlahCicilan.setText(ModuleKredit.numFormat(jumlahcicilan));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog currentDialog = (AlertDialog) getDialog();
        Button positiveButton, negativeButton;
        positiveButton = currentDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = currentDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setOnClickListener(v -> {
            if (!ModuleKredit.notEmpty(new EditText[]{eDenda, eAngsuran, eLamaKredit})) {
                ModuleKredit.info(getActivity(), "Harap isi field dengan benar");
                return;
            }
            // data initialization
            int idpelanggan = getArguments().getInt("idpelanggan");
            String tglkredit = getArguments().getString("tglbayar");
            String tgljatuhtempo = getArguments().getString("tglJatuhTempo");
            Double angsuran = Double.parseDouble(eAngsuran.getText().toString());
            Double denda = Double.parseDouble(eDenda.getText().toString());
            Cursor tblkredit = db.sq("SELECT * FROM tblkredit ORDER BY idkredit DESC");
            int currentIdKredit = 1;
            if (tblkredit.getCount() > 0) {
                tblkredit.moveToFirst();
                currentIdKredit = tblkredit.getInt(tblkredit.getColumnIndex("idkredit")) + 1;
            }
            // Insertion
            String kredit = "INSERT INTO tblkredit(idkredit, faktur, idpelanggan, tglkredit, jumlahkredit, lamakredit, angsuran, jumlahcicilan, denda, tgljatuhtempo, flag_kredit, saldokredit) VALUES" +
                    "(" + currentIdKredit + ", '" + faktur + "', " + idpelanggan + ", '" + tglkredit + "', " + jumlahkredit + ", " + lamakredit + ", " + angsuran + ", " + jumlahcicilan + ", " + denda + ", '" + tgljatuhtempo + "', 0, " + angsuran * lamakredit + ")";

            List<String> tagihanCommand = new ArrayList<>();
            int bulanSekarang = Integer.parseInt(String.valueOf(tgljatuhtempo).substring(5, 7));
            int tahunSekarang = Integer.parseInt(String.valueOf(tgljatuhtempo).substring(0, 4));
            for (int i = 1; i <= lamakredit; i++) {
                try {
                    boolean kabisat = tahunSekarang % 4 == 0;
                    String tanggalKredit = String.valueOf(tgljatuhtempo).substring(8);

                    String blnTagihan, tahunTagihan, tanggalTagihan = "";
                    // Tambah Bulan
                    if (bulanSekarang == 13) {
                        bulanSekarang = 1;
                        blnTagihan = "01";
                        tahunTagihan = String.valueOf(++tahunSekarang);
                    } else {
                        blnTagihan = bulanSekarang < 10 ? "0" + bulanSekarang : String.valueOf(bulanSekarang);
                        tahunTagihan = String.valueOf(tahunSekarang);
                    }

                    tanggalTagihan = tanggalKredit;
                    // Cek Jika tanggal 31
                    int[] daftarBulanTidakLebih30 = {2, 4, 6, 9, 11};
                    if (Integer.parseInt(tanggalKredit) == 31) {
                        for (int bulanTidakLebih30 : daftarBulanTidakLebih30) {
                            if (bulanSekarang == bulanTidakLebih30) {
                                tanggalTagihan = Integer.parseInt(blnTagihan) == 2 ? "28" : "30";
                            }
                        }
                    }
                    // Jika tanggal 30 atau 29 "DAN" bulan februari
                    if ((Integer.parseInt(tanggalKredit) == 30 && bulanSekarang == 2) || (Integer.parseInt(tanggalKredit) == 29 && bulanSekarang == 2)) {
                        tanggalTagihan = "28";
                    }
                    // Jika kabisat
                    if (kabisat && tanggalTagihan.equals("28")) tanggalTagihan = "29";
                    String tgltempo = tahunTagihan + "-" + blnTagihan + "-" + tanggalTagihan;

                    // Mass insertion
                    tagihanCommand.add("INSERT INTO tbltagihan(idkredit, jumlahangsuran, cicilan, jumlahdenda, tgltempo, flag_tagihan) " +
                            "VALUES(" + currentIdKredit + ", " + angsuran + ", " + i + ", " + denda + ", '" + tgltempo + "', 0); \n");
                    bulanSekarang++;
                } catch (Exception e) {
                    Log.e("err", e.getMessage());
                }
            }
//            Log.i("info", kredit);
//            Log.i("info", Arrays.toString(tagihanCommand.toArray()));
            if (db.exc(kredit)) {
                String[] insertCommand = new String[tagihanCommand.size()];
                insertCommand = tagihanCommand.toArray(insertCommand);
                boolean success = true;

                for (String command : insertCommand) {
                    try {
                        db.exc(command);
                    } catch (Exception e) {
                        Log.e("err", e.getMessage());
                        success = false;
                    }
                }
                if (success) {
                    ModuleKredit.info(getActivity(), "Data berhasil disimpan");
                    listener.onKreditConfirmed();
                    currentDialog.dismiss();
                }
            }
        });
        negativeButton.setOnClickListener(v -> currentDialog.dismiss());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ListenerDialogKredit) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement ListenerDialogKredit");
        }
    }

    public interface ListenerDialogKredit {
        void onKreditConfirmed();
    }

}
