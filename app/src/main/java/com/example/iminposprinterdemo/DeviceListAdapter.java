package com.example.iminposprinterdemo;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    public DeviceListAdapter(Context context) {
        super(context, 0);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("DeviceListAdapter.getView");
        BluetoothDevice device = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        }

        TextView tvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);

        CheckBox cbDevice = (CheckBox) convertView.findViewById(R.id.cb_device);

        tvDeviceName.setText(device.getName());

        cbDevice.setChecked(position == 0);

        return convertView;
    }
}