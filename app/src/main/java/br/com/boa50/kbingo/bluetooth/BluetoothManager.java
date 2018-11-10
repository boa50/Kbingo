/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    BaseActivity.java is part of Kbingo

    Kbingo is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Kbingo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package br.com.boa50.kbingo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;

import br.com.boa50.kbingo.Constants;

public class BluetoothManager {
    private static BluetoothManager instance = null;
    private BluetoothService bluetoothService = null;
    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;

    private BluetoothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public void setupBluetoothService(Context context) {
        if (isBluetoothAdapterAvailable()) {
            setupHandler(context);

            if (bluetoothAdapter.isEnabled()) {
                setupService();
            }
        }
    }

    private void setupHandler(Context context) {
        if (handler == null) {
            handler = new ServiceHandler(context);
        }
    }

    private void setupService() {
        if (bluetoothService == null && handler != null) {
            bluetoothService = new BluetoothService(handler);
        }

    }

    public void restartServiceIfStopped() {
        if (bluetoothService != null && !isServiceStarted()) {
            bluetoothService.start();
        }
    }

    void writeMessage(byte[] message) {
        bluetoothService.write(message);
    }

    private boolean isServiceStarted() {
        return  bluetoothService != null && (bluetoothService.getState() != Constants.STATE_NONE);
    }

    boolean isServiceConnected() {
        return bluetoothService != null && (bluetoothService.getState() == Constants.STATE_CONNECTED);
    }

    public boolean isBluetoothAdapterAvailable() {
        return bluetoothAdapter != null;
    }

    public void stopService() {
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
