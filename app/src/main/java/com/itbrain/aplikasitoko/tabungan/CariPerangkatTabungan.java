package com.itbrain.aplikasitoko.tabungan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itbrain.aplikasitoko.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CariPerangkatTabungan extends AppCompatActivity {
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

    PrefTabungan pref;
    ArrayList arrayList = new ArrayList() ;
    ListView list ;
    SimpleAdapter adapter ;
    String deviceName = "" ;
    String idsimpanan,notransaksi;
    List<Map<String,String>> data=new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_perangkat_tabungan);

        ImageButton imageButton = findViewById(R.id.kembaliCariPerangkatTabungan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
//        ModulTabungan.btnBack(ModulTabungan.getResString(this,R.string.cariperangkat),getSupportActionBar());
        pref = new PrefTabungan(getSharedPreferences("device_index",MODE_PRIVATE));
        list = (ListView) findViewById(R.id.listPerangkat) ;

        adapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"title","addr"},
                new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(adapter);
        idsimpanan = getIntent().getStringExtra("idsimpanan") ;
        notransaksi = getIntent().getStringExtra("notransaksi") ;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> tmpArray=data.get(position);
//                deviceName = arrayList.get(position).toString() ;
                deviceName = tmpArray.get("title") ;
                pref.setCustom("Printer", deviceName);
                Intent in = new Intent(CariPerangkatTabungan.this,CetakActivityTabungan.class) ;
                in.putExtra("idsimpanan",idsimpanan) ;
                in.putExtra("notransaksi",notransaksi) ;
                in.putExtra("laporan",getIntent().getBooleanExtra("laporan",false));
                startActivity(in);
                finish();
            }
        });
        try {
            findBT();
            openBT();
        }catch (Exception e){
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(CariPerangkatTabungan.this,CetakActivityTabungan.class) ;
        in.putExtra("idsimpanan",idsimpanan) ;
        in.putExtra("notransaksi",notransaksi) ;
        in.putExtra("laporan",getIntent().getBooleanExtra("laporan",false));
        startActivity(in);
        finish();
    }

    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, ModulTabungan.getResString(this,R.string.nobtadapter), Toast.LENGTH_SHORT).show();
            }

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT}, 1);
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Map<String,String> tmp1=new HashMap<String, String>(2);
                    tmp1.put("title",device.getName());
                    tmp1.put("addr",device.getAddress());
                    data.add(tmp1);
//                    arrayList.add(device.getName());
                    if (device.getName().equals(deviceName)) {
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //00001101-0000-1000-8000-00805F9B34FB

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.BLUETOOTH_CONNECT}, 1);
            }

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            Toast.makeText(this, ModulTabungan.getResString(this,R.string.btactivated), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(CariPerangkatTabungan.this, data, Toast.LENGTH_SHORT).show();
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
}