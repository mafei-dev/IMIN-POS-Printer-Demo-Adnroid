package com.example.iminposprinterdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil {

    public static boolean isBluetoothOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null)
            if (mBluetoothAdapter.isEnabled())
                return true;
        return false;
    }

    public static List<BluetoothDevice> getPairedDevices() {
        List deviceList = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
            }
        }
        return deviceList;
    }

    public static List<BluetoothDevice> getPairedPrinterDevices() {
        return getSpecificDevice(BluetoothClass.Device.Major.IMAGING);
    }

    public static List<BluetoothDevice> getSpecificDevice(int deviceClass) {
        List<BluetoothDevice> devices = BluetoothUtil.getPairedDevices();
        List<BluetoothDevice> printerDevices = new ArrayList<>();

        for (BluetoothDevice device : devices) {
            BluetoothClass klass = device.getBluetoothClass();
//			http://stackoverflow.com/q/23273355/4242112
            if (klass.getMajorDeviceClass() == deviceClass)
                printerDevices.add(device);
        }

        return printerDevices;
    }

    public static void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 666);
    }

    public static BluetoothSocket connectDevice(BluetoothDevice device) {
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException closeException) {
                return null;
            }
            return null;
        }
        return socket;
    }
}