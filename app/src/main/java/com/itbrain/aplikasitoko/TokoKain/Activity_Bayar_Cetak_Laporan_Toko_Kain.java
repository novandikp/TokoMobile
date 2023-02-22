package com.itbrain.aplikasitoko.TokoKain;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.itbrain.aplikasitoko.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Activity_Bayar_Cetak_Laporan_Toko_Kain extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    DatabaseTokoKain db;
    SharedPreferences pref;
    SharedPreferences.Editor editPref;
    String device, faktur, hasil, tFaktur = "", notelp = "";
    View v;
    int flagready = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_cetak_laporan_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.kembali4);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            db = new DatabaseTokoKain(this);
            pref = getSharedPreferences("config", this.MODE_PRIVATE);
            editPref = pref.edit();

            device = pref.getString("Printer", "");
            faktur = getIntent().getStringExtra("faktur");
            v = this.findViewById(android.R.id.content);
            KumFunTokoKain.btnBack("Cetak Struk", getSupportActionBar());

            try {
                findBT();
                openBT();
            } catch (Exception e) {
                Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
            }
            ImageView btnCari = (ImageView) findViewById(R.id.btnCariPerangkat);
            btnCari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cari(v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Tidak ada Bluetooth Adapter", Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                KumFunTokoKain.setText(v,R.id.viewNamaPrinter,"Printer Belum Dipilih") ;
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(this.device)) {
                        mmDevice = device;
                        KumFunTokoKain.setText(v,R.id.viewNamaPrinter,this.device) ;
                        break;
                    }
                }
            } else {
                KumFunTokoKain.setText(v,R.id.viewNamaPrinter,"Tidak Ada Perangkat") ;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void openBT() throws IOException {
        try {
            resetConnection();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //00001101-0000-1000-8000-00805F9B34FB
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            ConstraintLayout c = (ConstraintLayout) findViewById(R.id.simbol) ;
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                c.setBackgroundDrawable( getResources().getDrawable(R.drawable.ovalgreen_toko_kain) );
            } else {
                c.setBackground( getResources().getDrawable(R.drawable.ovalgreen_toko_kain));
            }
            flagready = 1 ;
//            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void sendData(String hasil) throws IOException {
        try {
            hasil += "\n\n\n";
            mmOutputStream.write(hasil.getBytes());
            resetConnection();
            Toast.makeText(this, "Print Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetConnection() {
        if (mmInputStream != null) {
            try {mmInputStream.close();} catch (Exception e) {}
            mmInputStream = null;
        }
        if (mmOutputStream != null) {
            try {mmOutputStream.close();} catch (Exception e) {}
            mmOutputStream = null;
        }
        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {}
            mmSocket = null;
        }
    }
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(Activity_Bayar_Cetak_Laporan_Toko_Kain.this, data, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    public void cari(View view) {
        Intent i = new Intent(this, Activity_Cari_Printer_Laporan_Toko_Kain.class);
        i.putExtra("faktur",faktur);
        i.putExtra("type","pesan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void setPreview(){
        Cursor identitas=db.sq(FQueryTokoKain.selectwhere("tblidentitas")+FQueryTokoKain.sWhere("id","1"));
        identitas.moveToNext();
        Cursor order=db.sq(FQueryTokoKain.selectwhere("qorder")+FQueryTokoKain.sWhere("faktur",faktur));
        order.moveToNext();

        Cursor cart=db.sq(FQueryTokoKain.selectwhere("qorder")+FQueryTokoKain.sWhere("faktur",faktur));
        Cursor notelppelanggan=db.sq(FQueryTokoKain.selectwhere("tblpelanggan")+FQueryTokoKain.sWhere("idpelanggan",KumFunTokoKain.getString(order,"idpelanggan")));
        notelppelanggan.moveToFirst();
        notelp=KumFunTokoKain.getString(notelppelanggan,"telppelanggan");

        String toko = KumFunTokoKain.getString(identitas,"namatoko");
        String alamat = KumFunTokoKain.getString(identitas,"alamattoko");
        String telp = KumFunTokoKain.getString(identitas,"notelptoko");
        KumFunTokoKain.setText(v,R.id.tvHeader,KumFunTokoKain.setCenter(toko)+"\n"+KumFunTokoKain.setCenter(alamat)+"\n"+
                KumFunTokoKain.setCenter(telp)+"\n");
        String tFaktur = "Faktur : "+faktur;
        KumFunTokoKain.setText(v,R.id.tvFaktur,tFaktur);
        String tTglTerima = "Tanggal Order : "+KumFunTokoKain.dateToNormal(KumFunTokoKain.getString(order,"tglorder"));
        KumFunTokoKain.setText(v,R.id.tvTanggalTerima,tTglTerima);
        String pelanggan = KumFunTokoKain.getString(order,"namapelanggan");
        KumFunTokoKain.setText(v,R.id.tvPelanggan,pelanggan);

        String header= KumFunTokoKain.setCenter(toko)+"\n"+
                KumFunTokoKain.setCenter(alamat)+"\n"+
                KumFunTokoKain.setCenter(telp)+"\n\n\n\n"+
                tFaktur+"\n"+
                tTglTerima+"\n"+
                pelanggan+"\n"+
                KumFunTokoKain.getStrip();
        String body="";
        String view="";
        while(cart.moveToNext()){
            String kain = KumFunTokoKain.getString(cart,"kategori")+" - "+KumFunTokoKain.getString(cart,"kain");
            String panjang = KumFunTokoKain.getString(cart,"panjang");
            String lebar = KumFunTokoKain.getString(cart,"lebar");
            String jumlah = KumFunTokoKain.getString(cart,"jumlah");
            String harga = KumFunTokoKain.removeE(KumFunTokoKain.getString(cart,"biaya"));
            String total = KumFunTokoKain.removeE(KumFunTokoKain.getString(cart,"hargaakhir"));
            String keterangan="";
            if (!KumFunTokoKain.getString(cart,"keterangan").equals("")){
                keterangan="("+KumFunTokoKain.getString(cart,"keterangan")+")\n";
            }

            body+= kain+"\n"+
                    "(Panjang "+panjang+" x Lebar "+lebar+" x Jumlah "+jumlah+") \n"+" x "+harga+"\n"+
                    keterangan+
                    KumFunTokoKain.setRight(total)+"\n";
            view+= kain+"\n"+
                    "(Panjang "+panjang+" x Lebar "+lebar+" x Jumlah "+jumlah+") \n"+" x "+harga+"\n"+
                    keterangan+
                    KumFunTokoKain.setRight(total)+"\n";
        }
        KumFunTokoKain.setText(v,R.id.tvListOrder,view);
        body+=KumFunTokoKain.getStrip();

        String jumlahbayar = "Total : "+KumFunTokoKain.removeE(KumFunTokoKain.getString(order,"total")); KumFunTokoKain.setText(v,R.id.tvTotal,jumlahbayar);
        String dibayar = "Bayar : " + KumFunTokoKain.removeE(KumFunTokoKain.getString(order,"bayar")) ; KumFunTokoKain.setText(v,R.id.tBayar,dibayar) ;
        String kembali = "" ;
        String caption =  KumFunTokoKain.getString(identitas,"caption_1") ;
        String caption2 = KumFunTokoKain.getString(identitas,"caption_2") ;
        String caption3 = KumFunTokoKain.getString(identitas,"caption_3") ;
        KumFunTokoKain.setText(v,R.id.tvCaption,caption+"\n"+caption2+"\n"+caption3);
        kembali = KumFunTokoKain.setRight("Kembali : "+  KumFunTokoKain.removeE(KumFunTokoKain.getString(order,"kembali")));

        KumFunTokoKain.setText(v,R.id.tKembali,kembali) ;

        String footer = KumFunTokoKain.setRight(jumlahbayar)+"\n"+
                KumFunTokoKain.setRight(dibayar)+"\n"+
                KumFunTokoKain.setRight(kembali)+"\n\n"+
                KumFunTokoKain.setCenter(caption)+"\n"+
                KumFunTokoKain.setCenter(caption2)+"\n"+
                KumFunTokoKain.setCenter(caption3) ;

        hasil = header+body+footer;
    }

    public void cetak(View view) throws IOException {
        if(KumFunTokoKain.getText(v,R.id.viewNamaPrinter).equals("Tidak Ada Perangkat")){
            Toast.makeText(this, "Tidak ada Printer", Toast.LENGTH_SHORT).show();
        } else if (flagready == 1){
            try {
                ConstraintLayout vStruk=(ConstraintLayout)findViewById(R.id.viewStruk);
                setPreview();
                vStruk.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Toast.makeText(this, "Preview Gagal", Toast.LENGTH_SHORT).show();
            }
            sendData(hasil);

        } else {
            Toast.makeText(this, "Printer belum siap", Toast.LENGTH_SHORT).show();
        }
    }

    public void preview(View view) {
        try {
            ConstraintLayout vStruk=(ConstraintLayout)findViewById(R.id.viewStruk);
            setPreview();
            vStruk.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Toast.makeText(this, "Gagal Memuat Struk", Toast.LENGTH_SHORT).show();
        }
    }
}
