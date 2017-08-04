package com.mygdx.game.views.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mygdx.game.AndroidLauncher;
import com.mygdx.game.BluetoothConstants;
import com.mygdx.game.BluetoothService;
import com.mygdx.game.IBluetooth;
import com.mygdx.game.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Demo on 02/08/2017.
 */

public class BluetoothFragment extends Fragment implements View.OnClickListener, IBluetooth
{
    private List<String> stringArray;
    private LinkedList<BluetoothDevice> mDevices;
    private Spinner spinnerDevices;
    private ArrayAdapter<String> itemsAdapter;
    private BluetoothService bluetoothService;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevices.add(device);
                stringArray.add(device.getName());
                itemsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bluetooth_ui, container, false);

        ((Button) view.findViewById(R.id.button_host)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.button_connect)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.button_discover)).setOnClickListener(this);
        spinnerDevices = (Spinner) view.findViewById(R.id.spinner_devices);

        stringArray = new ArrayList<>();
        mDevices = new LinkedList<BluetoothDevice>();

        itemsAdapter = new ArrayAdapter<String>(AndroidLauncher.instance, android.R.layout.simple_list_item_1, stringArray);
        spinnerDevices.setAdapter(itemsAdapter);

        bluetoothService = AndroidLauncher.instance.getBluetoothService();

        return view;
    }

    @Override
    public void write(String str) {
        bluetoothService.write(str.getBytes());
    }

    @Override
    public boolean isConnected() {
        return (bluetoothService.getState() == BluetoothConstants.STATE_CONNECTED);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_discover:
                stringArray.clear();
                mDevices.clear();
                bluetoothService.stop();
                bluetoothService.discoverDevices();
                break;

            case R.id.button_host:
                bluetoothService.stop();
                bluetoothService.enableDiscoveribility();
                bluetoothService.startListening();
                break;

            case R.id.button_connect:
                if(mDevices.isEmpty() == false){
                    bluetoothService.connect(mDevices.get(spinnerDevices.getSelectedItemPosition()));
                }
                break;

        }
    }
}
