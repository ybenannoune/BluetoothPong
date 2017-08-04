package com.mygdx.game;

import java.util.UUID;

/**
 * Created by Demo on 02/08/2017.
 */

public class BluetoothConstants
{
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final int REQUEST_ENABLE_BT = 3;

    // Constants that indicate the current connection state
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
}
