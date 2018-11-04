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
import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.com.boa50.kbingo.Constants;

public class ServiceHandler extends Handler {
    private WeakReference<Context> mContext;

    public ServiceHandler(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMsg = new String(readBuf, 0, msg.arg1);
                Toast.makeText(mContext.get(), "Read: " + readMsg,
                        Toast.LENGTH_LONG).show();
//                mainActivity.get().sendMessage(readMsg + " ok!");
                break;
            case Constants.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMsg = new String(writeBuf);
                Toast.makeText(mContext.get(), "Write: " + writeMsg,
                        Toast.LENGTH_LONG).show();
                break;
            case Constants.MESSAGE_TOAST:
                Toast.makeText(mContext.get(), msg.getData().getString(Constants.EXTRA_TOAST),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}
