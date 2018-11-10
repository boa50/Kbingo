/*
    Kbingo é um programa utilizado para gerenciar partidas de bingo.
    Copyright (C) 2018  Bruno Oliveira de Albuquerque

    Constants.java is part of Kbingo

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

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.com.boa50.kbingo.Constants;

public class ServiceHandler extends Handler {
    private WeakReference<Context> context;
    private StringBuffer outputStringBuffer;

    ServiceHandler(@NonNull Context context) {
        this.context = new WeakReference<>(context);
        outputStringBuffer = new StringBuffer();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMsg = new String(readBuf, 0, msg.arg1);
                manageMessage(readMsg);
                break;
            case Constants.MESSAGE_TOAST:
                Toast.makeText(context.get(), msg.getData().getString(Constants.EXTRA_TOAST),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void manageMessage(String message) {
        int idCartelaValue = Integer.valueOf(message);
        if (idCartelaValue > 0) {
            Toast.makeText(context.get(), "!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context.get(), "?", Toast.LENGTH_SHORT).show();
        }

        sendMessage(message + " ok!");
        Maruagem.getInstance().setIdCartelaSorteada(idCartelaValue);
    }

    private void sendMessage(String message) {
        BluetoothManager bluetoothManager = BluetoothManager.getInstance();
        if (bluetoothManager.isServiceConnected()) {
            if (message.length() > 0) {
                byte[] send = message.getBytes();
                bluetoothManager.writeMessage(send);
                outputStringBuffer.setLength(0);
            }
        } else {
            Toast.makeText(context.get(), "Não está conectado",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
