package com.mygdx.game;

/**
 * Created by Demo on 03/08/2017.
 */

//Interface necessary to Write message from libgdx Core.
public interface IBluetooth {
    void write(String str);
    boolean isConnected();
}
