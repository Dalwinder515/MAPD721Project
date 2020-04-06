package com.ds.mapd721project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Set;

public class HomeScreen extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    Button logout, buy, bluetooth_enable, bluetooth_disable, scanDevices, showDevices;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    BillingProcessor billingProcessor;
    BluetoothAdapter bluetoothAdapter;
    Intent enable_bluetooth;
    int requestCodebt;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /*if (ContextCompat.checkSelfPermission(HomeScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }*/

        //mAuth = FirebaseAuth.getInstance();
        billingProcessor = new BillingProcessor(this, null, this);
        logout = findViewById(R.id.logout);
        buy = findViewById(R.id.buy);
        bluetooth_enable = findViewById(R.id.bluetooth_enable);
        bluetooth_disable = findViewById(R.id.bluetooth_disable);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = findViewById(R.id.deviceList);
        scanDevices = findViewById(R.id.scanDevices);
        showDevices = findViewById(R.id.showDevices);

        enable_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodebt = 24;

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilter);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeScreen.this, MainActivity.class));
    }

    public void buy(View view) {
        billingProcessor.purchase(HomeScreen.this, "android.test.purchased");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == requestCodebt)
        {
            if(resultCode == RESULT_OK)
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            else if(resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Bluetooth Enabling Cancelled", Toast.LENGTH_SHORT).show();
        }
        else if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "New Product Purchased", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    public void enable_bluetooth(View view) {
        if(bluetoothAdapter == null)
        {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        else if(!bluetoothAdapter.isEnabled())
        {
            startActivityForResult(enable_bluetooth, requestCodebt);
        }
    }

    public void disable_bluetooth(View view) {
        if(bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }

    public void showDevices(View view) {
        Set<BluetoothDevice> bd = bluetoothAdapter.getBondedDevices();
        String[] devices = new String[bd.size()];
        int index = 0;

        if(bd.size() > 0)
        {
            for(BluetoothDevice device:bd)
            {
                devices[index] = device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, devices);
            listView.setAdapter(arrayAdapter);
        }
    }

    public void scanDevices(View view) {
        //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        bluetoothAdapter.startDiscovery();
        //Toast.makeText(this, "function", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "Device", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayList.add(device.getName());
                //Toast.makeText(context, "Device" + device.getName(), Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
}
