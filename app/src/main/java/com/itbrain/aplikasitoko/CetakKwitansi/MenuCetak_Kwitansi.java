package com.itbrain.aplikasitoko.CetakKwitansi;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.print.PrintAttributes;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MenuCetak_Kwitansi extends AppCompatActivity {

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

    EditText ePrinter;
    TextView tHeader, tFaktur, tPelanggan, tTanggal, tBarang, teks, tCaption,tKeterangan;
    ConfigKwitansi config,temp ;
    DatabaseCetakKwitansi db ;
    String device,faktur,hasil ;
    View v ;
    int flagready = 0 ;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucetak_kwitansi);

        ImageButton imageButton = findViewById(R.id.imageButton31);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new ConfigKwitansi(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new ConfigKwitansi(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseCetakKwitansi(this) ;
        v = this.findViewById(android.R.id.content);

        ePrinter = (EditText) findViewById(R.id.ePrinter);
        tHeader = (TextView) findViewById(R.id.tHeader);
        tFaktur = (TextView) findViewById(R.id.tFaktur);
        tPelanggan = (TextView) findViewById(R.id.tPelanggan);
        tTanggal = (TextView) findViewById(R.id.tTanggal);
        tBarang = (TextView) findViewById(R.id.tbarang);
        teks = (TextView) findViewById(R.id.teks);
        tCaption = (TextView) findViewById(R.id.tCaption);
        tKeterangan = (TextView) findViewById(R.id.tKeterangan);

        device = config.getCustom("Printer","");
        faktur = getIntent().getStringExtra("faktur") ;

        if(TextUtils.isEmpty(faktur)){
            Intent i = new Intent(this, MenuTransaksi_Kwitansi.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }

        try {
            findBT();
            openBT();
        }catch (Exception e){
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Intent i = new Intent(this, MenuUtama_Kwitansi.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuUtama_Kwitansi.class);
        startActivity(i);
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

    public void preview(View view){
        try {
            ConstraintLayout w = (ConstraintLayout) findViewById(R.id.wTeks) ;
            setPreview() ;
            w.setVisibility(View.VISIBLE);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Preview gagal, Karena pengisian toko kurang lengkap", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPreview(){
        Cursor identitas = db.sq("SELECT * FROM tblidentitas WHERE ididentitas=1") ;
        identitas.moveToNext() ;
        Cursor bayar = db.sq("SELECT * FROM vtransaksi WHERE faktur='"+faktur+"'");
        bayar.moveToNext() ;
        Cursor penj = db.sq("SELECT * FROM vtransaksi WHERE faktur='"+faktur+"'");


        @SuppressLint("Range") String toko = identitas.getString(identitas.getColumnIndex("nama"));
        @SuppressLint("Range") String alamat = identitas.getString(identitas.getColumnIndex("alamatid"));
        @SuppressLint("Range") String telp = identitas.getString(identitas.getColumnIndex("cap2")) ; tHeader.setText(toko+"\n"+alamat+"\n"+telp);

        String tfaktur   = setCenter("KWITANSI")+"\n"+setCenter("No : "+faktur) ; tFaktur.setText(tfaktur);
        @SuppressLint("Range") String pelanggan = "Telah Terima Dari : \n"+bayar.getString(bayar.getColumnIndex("penerima")) ; tPelanggan.setText(pelanggan);
//        String pembayaran = "Pembayaran : "+typebayar ; Function.setText(v,R.id.tPembayaran,pembayaran) ;
//        String tgl       = "Tanggal : "+getDate("dd-MM-yyyy") ; tTanggal.setText(tgl);
        @SuppressLint("Range") String tgl       = "Uang Sejumlah : \n" + angkaToTerbilang(Long.valueOf(removeE(bayar.getString(bayar.getColumnIndex("total"))))) + "Rupiah" ; tTanggal.setText(tgl);

        String header = setCenter(toko)+"\n"+
                setCenter(alamat)+"\n"+
                setCenter(telp)+"\n"+
                "\n"+
                getStrip()+
                tfaktur+"\n"+
                getStrip()+
                pelanggan+"\n\n"+
                tgl+"\n\n";

        String body = "Untuk : \n" ;
        String view = "Untuk : \n" ;
//        String barangPDF = "";
        while(penj.moveToNext()){
            @SuppressLint("Range") String barang = penj.getString(penj.getColumnIndex("jasatransaksi")) ;
            @SuppressLint("Range") String jumlah = penj.getString(penj.getColumnIndex("jumlah")) ;
            @SuppressLint("Range") String harga = penj.getString(penj.getColumnIndex("harga")) ;
            double total = Double.parseDouble(jumlah)*Double.parseDouble(harga) ;
            @SuppressLint("Range") String keterangan = penj.getString(penj.getColumnIndex("keterangan"));
            String ket="";

            if (keterangan.equals("")){
                ket = " -";
            } else {
                ket = keterangan;
            }

//            body+=  barang+"\n"+
//                    ProsesCari.removeE(jumlah)+" x "+ProsesCari.removeE(harga)+"\n"+
//                    "Keterangan : "+ket+"\n"+
//                    setFRight(ProsesCari.removeE(String.valueOf(total)))+"\n" ;
//            view+=  barang+"\n"+
//                    ProsesCari.removeE(jumlah)+" x "+ProsesCari.removeE(harga)+"\n"+
//                    "Keterangan : "+ket+"\n"+
//                    setRight(ProsesCari.removeE(String.valueOf(total)))+"\n" ;
            body+=  "- "+barang+"\n";
            view+=  "- "+barang+"\n";
//            barangPDF+=  "<h4>- "+barang+"</h4>";
        }
        @SuppressLint("Range") String ket = "Keterangan :\n"+bayar.getString(bayar.getColumnIndex("keterangan")); tKeterangan.setText(ket);
        tKeterangan.setVisibility(View.GONE);
        body+=  "\n"+""+"";
        tBarang.setText(view);
        body+=getStrip();
        @SuppressLint("Range") String jumlahbayar = "Jumlah : " + ProsesCari_Kwitansi.removeE(bayar.getString(bayar.getColumnIndex("total"))); teks.setText(jumlahbayar);
        body+=jumlahbayar+"\n";
        body+=getStrip();


        @SuppressLint("Range") String caption =  identitas.getString(identitas.getColumnIndex("telp")) ;
        @SuppressLint("Range") String tglT = bayar.getString(bayar.getColumnIndex("tgltransaksi"));
        @SuppressLint("Range") String caption2 = identitas.getString(identitas.getColumnIndex("cap1")) ;tCaption.setText(caption+setRight(LaporanTransaksi_Kwitansi.dateToNormal(tglT))+"\n\n\n\n\n\n"+caption2);

        String footer = setCenter(caption+", "+LaporanTransaksi_Kwitansi.dateToNormal(tglT))+"\n\n\n\n\n\n"+
                setCenter(caption2);
        hasil = header+body+footer ;
    }

    public static String setRight(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += "  " ;
            }
        }
        return hasil ;
    }

    public void cari(View view){
        Intent i = new Intent(this,CariPrinter_Kwitansi.class) ;
        i.putExtra("faktur",faktur) ;
        startActivity(i);
    }

    public void cetak(View view) throws IOException {
        try {
            if(ePrinter.getText().equals("Tidak Ada Perangkat")){
                Toast.makeText(this, "Tidak ada Printer", Toast.LENGTH_SHORT).show();
            } else if (flagready == 1){
                try {
                    setPreview();
                }catch (Exception e){
                    Toast.makeText(this, "Preview Gagal", Toast.LENGTH_SHORT).show();
                }
                sendData(hasil);

                onBackPressed();

            } else {
                Toast.makeText(this, "Printer belum siap", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Proses Cetak Gagal, Harap periksa Printer atau bluetooth anda", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
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

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                ePrinter.setText("Printer Belum Dipilih");
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(this.device)) {
                        mmDevice = device;
                        ePrinter.setText(this.device);
                        break;
                    }
                }
            } else {
                ePrinter.setText("Tidak Ada Perangkat");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                c.setBackgroundDrawable( getResources().getDrawable(R.drawable.ovalgreen) );
            } else {
                c.setBackground( getResources().getDrawable(R.drawable.ovalgreen));
            }
            flagready = 1 ;
//            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal tersambung dengan printer", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal tersambung dengan printer", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(MenuCetak_Kwitansi.this, data, Toast.LENGTH_SHORT).show();
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

    @SuppressLint("MissingPermission")
    void sendData(String hasil) throws IOException {
        try {
            hasil += "\n\n\n";
            mmOutputStream.write(hasil.getBytes());mBluetoothAdapter.cancelDiscovery() ; mmSocket.close();

            resetConnection();
            Toast.makeText(this, "Print Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public static String setCenter(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<30-leng;i++){
            if((30
                    -leng)/2+1 == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }

    public static String getStrip(){
        String a = "" ;
        for(int i = 0 ; i < 32 ; i++){
            a+="-" ;
        }
        return a+"\n" ;
    }

    public static String setFRight(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }
    public static String angkaToTerbilang(Long angka){
        String[] huruf={"","Satu","Dua","Tiga","Empat","Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
        if(angka < 12)
            return huruf[angka.intValue()];
        if(angka >=12 && angka <= 19)
            return huruf[angka.intValue() % 10] + " Belas";
        if(angka >= 20 && angka <= 99)
            return angkaToTerbilang(angka / 10) + " Puluh " + huruf[angka.intValue() % 10];
        if(angka >= 100 && angka <= 199)
            return "Seratus " + angkaToTerbilang(angka % 100);
        if(angka >= 200 && angka <= 999)
            return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
        if(angka >= 1000 && angka <= 1999)
            return "Seribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 2000 && angka <= 999999)
            return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 1000000 && angka <= 999999999)
            return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
        if(angka >= 1000000000 && angka <= 999999999999L)
            return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return angkaToTerbilang(angka / 1000000000000L) + " Triliun " + angkaToTerbilang(angka % 1000000000000L);
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun " + angkaToTerbilang(angka % 1000000000000000L);
        return "";
    }
    public static String removeE(String value){
        double hasil = Double.parseDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(hasil) ;
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir;
        if(Build.VERSION.SDK_INT >= 29) {
            pictureFileDir = new File(this.getExternalFilesDir("Struk").toString()+"/");
            //only api 21 above
        }else{
            pictureFileDir = new File(Environment.getExternalStorageDirectory().toString() + "/Download/");
            //only api 21 down
        }

        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();

            Toast.makeText(context, "Gambar tersimpan :\n"+filename, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ConstraintLayout Struk;
    public void save() {
        Struk = findViewById(R.id.wTeks);
        saveBitMap(this, Struk);
    }
    void shareImage() {
        Struk = findViewById(R.id.wTeks);
        Bitmap bitmap = getBitmapFromView(Struk);
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(this.getExternalCacheDir(), "Struk.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void download2(View view) {
        setPreview();
        save();
    }

    public void share2(View view) {
        setPreview();
        shareImage();
    }
}

