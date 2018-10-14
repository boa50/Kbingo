package br.com.boa50.kbingo.helper;

import android.os.Environment;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnviromentHelper {
    @Inject
    EnviromentHelper(){}

    public File getDowloadDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }
}