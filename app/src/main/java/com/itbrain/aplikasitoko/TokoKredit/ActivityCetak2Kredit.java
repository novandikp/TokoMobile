package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ActivityCetak2Kredit extends AppCompatActivity {

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

    FConfigKredit config, temp;
    FKoneksiKredit db;
    String device, faktur, hasil;
    View v;
    int flagready = 0;
//    CekInApp cekInApp;

    static final String ITEM_EXPORT = "com.komputerkit.kasirsupermudah.inlimit";
    String deviceid;
    SharedPreferences getPrefs;
    boolean bHapus = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_satu_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);
//        cekInApp = new CekInApp(this);

        device = config.getCustom("Printer", "");
        faktur = getIntent().getStringExtra("fakturbayar");

        if (TextUtils.isEmpty(faktur)) {
            Intent i = new Intent(this, ActivityPenjualanTunaiKredit.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        try {
            findBT();
            openBT();
        } catch (Exception e) {
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetConnection();
    }

    void resetConnection() {
        // Menutup semua koneksi
        if (mmInputStream != null) {
            try {
                mmInputStream.close();
            } catch (Exception e) {
                Log.e("err", e.getMessage());
            }
        }
        if (mmOutputStream != null) {
            try {
                mmOutputStream.close();
            } catch (Exception e) {
                Log.e("err", e.getMessage());
            }
        }
        if (mmSocket != null) {
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e("err", e.getMessage());
            }
        }
    }

    public void preview(View view) {
        try {
            ConstraintLayout w = findViewById(R.id.wTeks);
            setPreview();
            w.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(this, "Preview gagal, Karena pengisian toko kurang lengkap", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPreview() {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
        String def ="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";
        String strBase64 = shre.getString("image_data",def);
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView img = findViewById(R.id.logo);
        img.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 200, 200, false));

        Cursor identitas = db.sq(FQueryKredit.selectwhere("tblidentitas") + FQueryKredit.sWhere("id", "1"));
        identitas.moveToNext();
        Cursor bayar = db.sq(FQueryKredit.selectwhere("qbayar") + FQueryKredit.sWhere("fakturbayar", faktur));
        bayar.moveToNext();
        Cursor penj = db.sq(FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("fakturbayar", faktur));

        String flagbayar = FFunctionKredit.getString(bayar, "flagbayar");
        String typebayar = "";
        if (flagbayar.equals("1")) {
            typebayar = "Tunai";
        } else {
            typebayar = "Hutang";
        }

        String toko = FFunctionKredit.getString(identitas, "nama");
        String alamat = FFunctionKredit.getString(identitas, "alamat");
        String telp = FFunctionKredit.getString(identitas, "telp");
        FFunctionKredit.setText(v, R.id.tHeader, toko + "\n" + alamat + "\n" + telp);
        String tfaktur = "Faktur : " + faktur;
        FFunctionKredit.setText(v, R.id.tFaktur, tfaktur);
        String pelanggan = "Pelanggan : " + FFunctionKredit.getString(bayar, "pelanggan");
        FFunctionKredit.setText(v, R.id.tPelanggan, pelanggan);
        String pembayaran = "Pembayaran : " + typebayar;
        FFunctionKredit.setText(v, R.id.tPembayaran, pembayaran);
        String tgl = "Tanggal : " + FFunctionKredit.getDate("dd-MM-yyyy");
        FFunctionKredit.setText(v, R.id.tTanggal, tgl);

        String header = FFunctionKredit.setCenter(toko) + "\n" +
                FFunctionKredit.setCenter(alamat) + "\n" +
                FFunctionKredit.setCenter(telp) + "\n" +
                "\n" +
                tfaktur + "\n" +
                tgl + "\n" +
                pelanggan + "\n" +
                pembayaran + "\n" +
                FFunctionKredit.getStrip();

        String body = "";
        String view = "";
        while (penj.moveToNext()) {
            String barang = FFunctionKredit.getString(penj, "barang");
            String jumlah = FFunctionKredit.getString(penj, "jumlahjual");
            String harga = FFunctionKredit.getString(penj, "hargajual:1");
            String keterangan = FFunctionKredit.getString(penj, "ketpenjualan");
            double total = FFunctionKredit.strToDouble(jumlah) * FFunctionKredit.strToDouble(harga);

            body += barang + "\n" +
                    jumlah + " x " + FFunctionKredit.removeE(harga) + "\n" +
                    keterangan + "\n" +
                    FFunctionKredit.setRight(FFunctionKredit.removeE(total)) + "\n";
            view += barang + "\n" +
                    jumlah + " x " + FFunctionKredit.removeE(harga) + "\n" +
                    keterangan + "\n" +
                    setRight(FFunctionKredit.removeE(total)) + "\n";
        }
        FFunctionKredit.setText(v, R.id.tBarang, view);
        body += FFunctionKredit.getStrip();


        String jumlahbayar = "Total : " + FFunctionKredit.removeE(FFunctionKredit.getString(bayar, "jumlahbayar"));
        FFunctionKredit.setText(v, R.id.teks, jumlahbayar);
        String dibayar = "Bayar : " + FFunctionKredit.removeE(FFunctionKredit.getString(bayar, "bayar"));
        FFunctionKredit.setText(v, R.id.tBayar, dibayar);
        String kembali = "";
        String caption = FFunctionKredit.getString(identitas, "caption1");
        String caption2 = FFunctionKredit.getString(identitas, "caption2");
        String caption3 = FFunctionKredit.getString(identitas, "caption3");
        FFunctionKredit.setText(v, R.id.tCaption, caption + "\n" + caption2 + "\n" + caption3);

        if (FFunctionKredit.getString(bayar, "flagbayar").equals("0")) {
            kembali = FFunctionKredit.setRight("Kembali : -" + FFunctionKredit.removeE(FFunctionKredit.getString(bayar, "kembali")));
        } else {
            kembali = FFunctionKredit.setRight("Kembali : " + FFunctionKredit.removeE(FFunctionKredit.getString(bayar, "kembali")));
        }
        FFunctionKredit.setText(v, R.id.tKembali, kembali);

        String footer = FFunctionKredit.setRight(jumlahbayar) + "\n" +
                FFunctionKredit.setRight(dibayar) + "\n" +
                FFunctionKredit.setRight(kembali) + "\n\n" +
                FFunctionKredit.setCenter(caption) + "\n" +
                FFunctionKredit.setCenter(caption2) + "\n" +
                FFunctionKredit.setCenter(caption3);


        hasil = header + body + footer;
    }

    public static String setRight(String item) {
        int leng = item.length();
        String hasil = "";
        for (int i = 0; i < 32 - leng; i++) {
            if ((31 - leng) == i) {
                hasil += item;
            } else {
                hasil += "  ";
            }
        }
        return hasil;
    }

    public void cari(View view) {
        Intent i = new Intent(this, ActivityCetakCariKredit.class);
        i.putExtra("fakturbayar", faktur);
        startActivity(i);
    }

    public void cetak(View view) {
        try {
            if (FFunctionKredit.getText(v, R.id.ePrinter).equals("Tidak Ada Perangkat")) {
                Toast.makeText(this, "Tidak ada Printer", Toast.LENGTH_SHORT).show();
            } else if (flagready == 1) {
                try {
                    setPreview();
                } catch (Exception e) {
                    Toast.makeText(this, "Preview Gagal", Toast.LENGTH_SHORT).show();
                }

                sendData(hasil);

                onBackPressed();
//                Intent i = new Intent(this, ActivityPenjualan.class) ;
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//                startActivity(i);
            } else {
                Toast.makeText(this, "Printer belum siap", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Proses Cetak Gagal, Harap periksa Printer atau bluetooth anda", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap addPaddingLeftForBitmap(Bitmap bitmap, int paddingLeft) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth() + paddingLeft, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, paddingLeft, 0, null);
        return outputBitmap;
    }

    public void printPhoto() {
        String base64 = "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";
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit=shre.edit();
        String def ="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";
        base64 = shre.getString("image_data",def);

        try {
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = (BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            if(decodedByte!=null){
                byte[] command = UtilsKredit.decodeBitmap(addPaddingLeftForBitmap(Bitmap.createScaledBitmap(decodedByte, 200, 200, false),80));
                byte[] ESC_ALIGN_CENTER = new byte[] { 0x1b, 0x61, 0x01 };
                byte[] SELECT = new byte[] { 0x1b, 0x3d, 0x01 };

                try {
                    // Print normal text
                    mmOutputStream.write(ESC_ALIGN_CENTER);
                    Thread.sleep(50);
                    mmOutputStream.write(command);
                    byte[] ESC_ALIGN_LEFT = new byte[]{0x1b, 'a', 0x00};
                    mmOutputStream.write(ESC_ALIGN_LEFT);
                    printNewLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printNewLine() {
        try {
            byte[] FEED_LINE = {10};
            mmOutputStream.write(FEED_LINE);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void cetak2(View view) {
        try {
            try {
                setPreview();

                Intent intent = new Intent("pe.diegoveloper.printing");
                //intent.setAction(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, hasil);
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, "Gagal Print", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Proses Cetak Gagal, Harap periksa Printer atau bluetooth anda", Toast.LENGTH_SHORT).show();
        }
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

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                FFunctionKredit.setText(v, R.id.ePrinter, "Printer Belum Dipilih");
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(this.device)) {
                        mmDevice = device;
                        FFunctionKredit.setText(v, R.id.ePrinter, this.device);
                        break;
                    }
                }
            } else {
                FFunctionKredit.setText(v, R.id.ePrinter, "Tidak Ada Perangkat");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void openBT() {
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

            ConstraintLayout c = findViewById(R.id.simbol);
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                c.setBackgroundDrawable(getResources().getDrawable(R.drawable.ovalgreen));
            } else {
                c.setBackground(getResources().getDrawable(R.drawable.ovalgreen));
            }
            flagready = 1;
//            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
                                                Toast.makeText(ActivityCetak2Kredit.this, data, Toast.LENGTH_SHORT).show();
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

    void sendData(String hasil) {
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

    public void download(View view) {
        setPreview();
        save();
    }
    public void share(View view) {
        setPreview();
        shareImage();
    }
}