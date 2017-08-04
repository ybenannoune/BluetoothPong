package com.mygdx.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.mygdx.game.views.fragment.BluetoothFragment;
import com.mygdx.game.views.fragment.GdxFragment;

public class AndroidLauncher extends AppCompatActivity implements AndroidFragmentApplication.Callbacks
{
    public static AndroidLauncher instance;
	private BluetoothService bluetoothService;

	private GdxFragment gdxFragment;
	private BluetoothFragment bluetoothFragment;

	private Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {

				case BluetoothConstants.MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					gdxFragment.getGameInstance().incomingMessage(readMessage);
					break;

				case BluetoothConstants.MESSAGE_DEVICE_NAME:
					// save the connected device's name
					CharSequence connectedDevice = "Connected to " + msg.getData().getString(BluetoothConstants.DEVICE_NAME);
					Toast.makeText(instance, connectedDevice, Toast.LENGTH_SHORT).show();
					gdxFragment.getGameInstance().onConnected(bluetoothService.isHost());
					break;

				case BluetoothConstants.MESSAGE_STATE_CHANGE:
					if(bluetoothService.getState() == BluetoothConstants.STATE_NONE )
					{
						gdxFragment.getGameInstance().onDisconnect();
					}
					break;

				case BluetoothConstants.MESSAGE_TOAST:
					CharSequence content = msg.getData().getString(BluetoothConstants.TOAST);
					Toast.makeText(instance, content , Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	public BluetoothService getBluetoothService()
	{
		return bluetoothService;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        instance = this;

		bluetoothService = new BluetoothService(this, handler);
		bluetoothService.enableBluetooth();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gdxFragment = ((GdxFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_libgdx));
		bluetoothFragment = ((BluetoothFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_ui));
		gdxFragment.getGameInstance().setBluetoothInterface(bluetoothFragment);

		//Add Toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

        Permissions.verifyLocationPermissions(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_credits)
		{
			Snackbar.make(this.findViewById(R.id.toolbar), "Created by Yacine BENANNOUNE", Snackbar.LENGTH_SHORT).setAction("Action", null).show();;
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case BluetoothConstants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session

                }
				else {
                    Toast.makeText(this, "Bluetooth is needed!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

	@Override
	public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
		switch (requestCode)
		{
			case Permissions.MY_PERMISSIONS_REQUEST_LOCATION_ID:
			{
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0	&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
					//ok
				}
				else{
					Toast.makeText(this,"Application need location ! (Functionnality Disabled)",Toast.LENGTH_LONG).show();
					this.finish();
				}
			}
		}
	}

	@Override
	public void exit() {

	}

}
