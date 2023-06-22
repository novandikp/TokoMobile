package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityLaporanReturnKredit extends AppCompatActivity {

    String type;
    View v;
    FConfigKredit config;
    FKoneksiKredit db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_transaksi_return_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);

        // Calendar
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");



        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText eCari = findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString();

                        getReturn(a);

            }
        });

        setText();
        getReturn("");
    }

    @Override
    protected void onResume(){
        super.onResume();
        getReturn("");
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", type);
        startActivity(i);
    }

    public void setText() {
        dari = FFunctionKredit.setDatePicker(year, month, 1);
        ke = FFunctionKredit.setDatePicker(year, month, day);
        String dari = FFunctionKredit.setDatePickerNormal(year, month + 1, 1);
        String ke = FFunctionKredit.setDatePickerNormal(year, month + 1, day);

        FFunctionKredit.setText(v, R.id.eDari, dari);
        FFunctionKredit.setText(v, R.id.eKe, ke);
    }

    public void getReturn(String cari) {
        arrayList.clear();
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.selectwhere("qreturn") + FQueryKredit.sBetween("tglreturn", dari, ke);
        } else {
            q = FQueryKredit.selectwhere("qreturn") + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tglreturn", dari, ke);
        }

        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterReturnKredit(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            int total = 0;
            while (c.moveToNext()) {
                String nama = FFunctionKredit.getString(c, "barang");
                String tgl = FFunctionKredit.getString(c, "tglreturn");
                String jumlah = FFunctionKredit.getString(c, "jumlah");

                String campur = FFunctionKredit.getCampur(nama, "Jumlah Barang : " + jumlah, FFunctionKredit.dateToNormal(tgl));
                arrayList.add(campur);
                total += FFunctionKredit.strToInt(jumlah);
            }
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : " + String.valueOf(c.getCount()));
            FFunctionKredit.setText(v, R.id.tCaption, "Return :");
            FFunctionKredit.setText(v, R.id.tValue, FFunctionKredit.intToStr(total) + " Barang");
            FFunctionKredit.setText(v, R.id.tValue2, "");
        } else {
            FFunctionKredit.setText(v, R.id.tCaption, "Return :");
            FFunctionKredit.setText(v, R.id.tValue, "0 Barang");
            FFunctionKredit.setText(v, R.id.tValue2, "");
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void submit() {
        String a = FFunctionKredit.getText(v, R.id.eCari);
        switch (type) {
            case "return":
                getReturn(a);
                break;
        }
    }

    public void open(final String faktur) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus transaksi ini?");
        alertDialogBuilder.setPositiveButton("Iya", (arg0, arg1) -> {
            //yes
            if (!faktur.equals("tidak")) {
                Cursor c = db.sq(FQueryKredit.selectwhere("tblpenjualan") + FQueryKredit.sWhere("fakturbayar", faktur));
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        String id = FFunctionKredit.getString(c, "idpenjualan");
                        db.exc("DELETE FROM tblpenjualan WHERE idpenjualan='" + id + "'");
                    }
                }
                db.exc("DELETE FROM tblbayar WHERE fakturbayar='" + faktur + "'");
                Toast.makeText(ActivityLaporanReturnKredit.this, "Hapus Berhasil", Toast.LENGTH_SHORT).show();
                getReturn("");
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, 1);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eDari, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            dari = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            Log.i("info", "dari" + dari);
            submit();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eKe, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            Log.i("info", "ke : " + ke);
            submit();
        }
    };
    //end date time picker

    public void delete(View view) {
        String faktur = view.getTag().toString();
        open(faktur);
    }
}

class AdapterReturnKredit extends RecyclerView.Adapter<AdapterReturnKredit.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    AdapterReturnKredit(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_return_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nama, nma, jumlah;

        public ViewHolder(View view) {
            super(view);
            nma = view.findViewById(R.id.tHitung);
            nama = view.findViewById(R.id.judul);
            jumlah = view.findViewById(R.id.tNama);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");
        viewHolder.jumlah.setText(row[2]);
        viewHolder.nma.setText(row[1]);
        viewHolder.nama.setText(row[0]);
    }
}

