package com.mygdx.game.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.mygdx.game.BluetoothPong;

/**
 * Created by Demo on 02/08/2017.
 */

public class GdxFragment extends AndroidFragmentApplication
{
    private static BluetoothPong libGDXapp = null;

    public BluetoothPong getGameInstance()
    {
        return libGDXapp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if( libGDXapp == null)
        {
            libGDXapp = new BluetoothPong();
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return initializeForView(libGDXapp,config);
    }
}
