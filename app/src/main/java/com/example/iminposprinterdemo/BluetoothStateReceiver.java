package com.example.iminposprinterdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_ON:
                toast("Bluetooth ON");
                break;

            case BluetoothAdapter.STATE_TURNING_OFF:
                toast("Bluetooth OFF");
                break;
        }
    }

    protected void toast(String message) {
        Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
