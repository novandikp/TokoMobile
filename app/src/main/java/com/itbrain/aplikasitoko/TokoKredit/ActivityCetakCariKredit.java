package com.itbrain.aplikasitoko.TokoKredit;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModelBluetoothDeviceKredit;
import com.itbrain.aplikasitoko.kasir.ActivityCetakUtangKasir;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActivityCetakCariKredit extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    List<ModelBluetoothDeviceKredit> daftarBluetoothDevice;
    RecyclerView reBluetoothDevice;
    AdapterBluetoothDevice adapter;
    String faktur, fakturbayar;
    FConfigKredit config, temp;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_cari_satu_kredit);

        init();

        try {
            findBT();
        } catch (Exception e) {
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }


        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    void init() {
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        faktur = getIntent().getStringExtra("faktur");
        fakturbayar = getIntent().getStringExtra("fakturbayar");
        type = getIntent().getStringExtra("type");
        // RecyclerView Stuff
        daftarBluetoothDevice = new ArrayList<>();
        adapter = new AdapterBluetoothDevice(this, daftarBluetoothDevice);
        reBluetoothDevice = findViewById(R.id.reBluetoothDevice);
        reBluetoothDevice.setAdapter(adapter);
        reBluetoothDevice.setLayoutManager(new LinearLayoutManager(this));
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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.BLUETOOTH_CONNECT},1);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    daftarBluetoothDevice.add(new ModelBluetoothDeviceKredit(
                            device.getName(),
                            device.getAddress()
                    ));
                }
                adapter.notifyDataSetChanged();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDevice(View itemView) {
        String deviceName = itemView.getTag().toString();
        config.setCustom("Printer", deviceName);

        Class classToIntent;
        if (type != null && type.equals("utang")) {
            classToIntent = ActivityCetakUtangKredit.class;
        } else if (type != null && type.equals("kredit")) {
            classToIntent = ActivityCetakKredit.class;
        } else {
            classToIntent = ActivityCetakSatuKredit.class;
        }

        Intent in = new Intent(this, classToIntent);
        in.putExtra("fakturbayar", fakturbayar);
        in.putExtra("faktur", faktur);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }

}

class AdapterBluetoothDevice extends RecyclerView.Adapter<AdapterBluetoothDevice.BluetoothDeviceViewHolder> {

    private Context ctx;
    private List<ModelBluetoothDeviceKredit> daftarBluetoothDevice;

    AdapterBluetoothDevice(Context ctx, List<ModelBluetoothDeviceKredit> daftarBluetoothDevice) {
        this.ctx = ctx;
        this.daftarBluetoothDevice = daftarBluetoothDevice;
    }

    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_bluetooth_device_kredit, parent, false);
        return new BluetoothDeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceViewHolder holder, int position) {
        ModelBluetoothDeviceKredit m = daftarBluetoothDevice.get(position);
        holder.tvDevice.setText(m.getDeviceName());
        holder.tvAddress.setText(m.getAddress());
        holder.cvBluetoothDevice.setTag(m.getDeviceName());
    }

    @Override
    public int getItemCount() {
        return daftarBluetoothDevice.size();
    }

    class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {

        TextView tvDevice, tvAddress;
        CardView cvBluetoothDevice;

        BluetoothDeviceViewHolder(View itemView) {
            super(itemView);
            tvDevice = itemView.findViewById(R.id.tvDevice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            cvBluetoothDevice = itemView.findViewById(R.id.cvBluetoothDevice);
        }

    }

}