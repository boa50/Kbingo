/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    MainActivity.java is part of Kbingo

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
package br.com.boa50.kbingo;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import javax.inject.Inject;

import br.com.boa50.kbingo.bluetooth.BluetoothService;
import br.com.boa50.kbingo.bluetooth.ServiceHandler;
import br.com.boa50.kbingo.data.AppDataSource;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;

    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothService bluetoothService = null;
    private StringBuffer outputStringBuffer;
    private Handler handler;

    @Inject
    AppDataSource appDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDataSource.initializeDatabase();

        setupBluetooth();

        Intent intent = new Intent(MainActivity.this, BaseActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            setTheme(R.style.AppTheme);
            startActivity(intent);
        }, 2000);
    }

    private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth indisponível",
                    Toast.LENGTH_LONG).show();
        }
        if (this.handler == null) {
            this.handler = new ServiceHandler(getApplicationContext());
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (bluetoothService == null) {
            setupService();
        }
    }

    private void setupService() {
        bluetoothService = new BluetoothService(handler);
        outputStringBuffer = new StringBuffer();
    }
}
