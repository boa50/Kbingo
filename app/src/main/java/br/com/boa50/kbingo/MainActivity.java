package br.com.boa50.kbingo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    AppDataSource mAppDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppDataSource.initializeDatabase();
//        Intent intent = new Intent(MainActivity.this, RealizaSorteioActivity.class);
        Intent intent = new Intent(MainActivity.this, VisualizaCartelasActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            setTheme(R.style.AppTheme);
            startActivity(intent);
        }, 2000);

    }
}
