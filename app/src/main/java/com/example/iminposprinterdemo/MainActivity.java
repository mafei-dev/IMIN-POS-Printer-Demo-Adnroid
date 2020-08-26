package com.example.iminposprinterdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.imin.printerlib.IminPrintUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestPrintActivity";
    private IminPrintUtils mIminPrintUtils;
    private BluetoothStateReceiver mBluetoothStateReceiver;
    private int mSelectedPosition = -1;
    private DeviceListAdapter mAdapter;
    private int connectType;
    private static MainActivity instance;

    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        initReceiver();
        mIminPrintUtils = IminPrintUtils.getInstance(MainActivity.this);
    }

    private void initView() {
        mAdapter = new DeviceListAdapter(this);
        connectType = 1;
        try {
            if (BluetoothUtil.isBluetoothOn()) {
                fillAdapter();
            } else {
                BluetoothUtil.openBluetooth(MainActivity.this);
            }
        } finally {
            mSelectedPosition = 0;
            mAdapter.notifyDataSetChanged();
        }

        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("TestPrintActivity.onClick");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: connectType " + connectType);
                        if (0 == connectType) {
                            mIminPrintUtils.initPrinter();
                        } else if (1 == connectType) {
                            if (mSelectedPosition >= 0) {
                                Log.d(TAG, "run: ----- 01");
                                System.out.println("mSelectedPosition = " + mSelectedPosition);

                                BluetoothDevice device = mAdapter.getItem(mSelectedPosition);
                                try {
                                    mIminPrintUtils.initPrinter(device);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }).start();
                int status = mIminPrintUtils.getPrinterStatus();
                Toast.makeText(MainActivity.this, " " + status, Toast.LENGTH_SHORT).show();
                mIminPrintUtils.printText("TEST - " + new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date()), 1);

                mIminPrintUtils.printAndFeedPaper(30);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillAdapter();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBluetoothStateReceiver);
        super.onDestroy();
    }

    private void initReceiver() {
        mBluetoothStateReceiver = new BluetoothStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStateReceiver, filter);
    }

    private void fillAdapter() {
        List<BluetoothDevice> printerDevices = BluetoothUtil.getPairedDevices();
        mAdapter.clear();
        mAdapter.addAll(printerDevices);
    }

}
