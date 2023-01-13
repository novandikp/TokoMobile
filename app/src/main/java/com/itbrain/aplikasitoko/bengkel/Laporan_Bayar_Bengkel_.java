package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.itbrain.aplikasitoko.AdapterLapBayar;
import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Bayar_Bengkel_ extends AppCompatActivity {

    ArrayList arrayList = new ArrayList();
    Database_Bengkel_ db;
    View v;
    String type;
    EditText TxTanggal;
    EditText TxAkhir;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    Calendar calendar;
    int year, day, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_bayar_bengkel_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        getBayar("");
        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String a = eCari.getText().toString();
                arrayList.clear();
                getBayar(a);
            }
        });

        TxTanggal = findViewById(R.id.txTanggalAwal);
        TxAkhir = findViewById(R.id.txTanggalAkhir);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        TxAkhir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDateDialog2();
            }
        });

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDateDialog1();
            }
        });
    }

    public void getBayar(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvBayar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLapBayar(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qorder")+ " statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.txTanggalAwal),ModulBengkel.getText(v,R.id.txTanggalAkhir))+" AND "+ModulBengkel.sLike("pelanggan",cari)+ModulBengkel.sOrderASC("faktur");
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"idorder")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"total")+ "__" + ModulBengkel.getString(c,"bayar");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    private void showDateDialog2() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxAkhir.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog1() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}

//class AdapterLapBayar extends RecyclerView.Adapter<AdapterLapBayar.ViewHolder>{
//    private ArrayList<String> data;
//    Context c;
//    View v;
//
//    public AdapterLapBayar(Context a, ArrayList<String> kota) {
//        this.data = kota;
//        c = a;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pembayaran, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AdapterLapBayar.ViewHolder holder, int position) {
//        String[] row = data.get(position).split("__");
//        holder.cv.setTag(row[0]);
//        holder.nama.setText(row[1]);
//        holder.alamat.setText(row[2]);
//        holder.notelp.setText("Total :"+"Rp."+ModulBengkel.removeE(row[3]));
//        holder.tvOpt.setText("Bayar :"+"Rp."+ModulBengkel.removeE(row[4]));
//        holder.print.setVisibility(View.VISIBLE);
//        holder.print.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(c,MenuCetak.class);
//                i.putExtra("idorder",holder.cv.getTag().toString());
//                i.putExtra("type","laporan");
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                c.startActivity(i);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView nama,alamat,notelp,tvOpt;
//        CardView cv;
//        ImageView print;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            cv = itemView.findViewById(R.id.cv);
//            nama= (TextView) itemView.findViewById(R.id.tNamaPel);
//            alamat = (TextView) itemView.findViewById(R.id.tAlamatPel);
//            notelp=(TextView) itemView.findViewById(R.id.tNo);
//            tvOpt=(TextView) itemView.findViewById(R.id.tgl);
//            print=itemView.findViewById(R.id.print);
//        }
//    }
//}