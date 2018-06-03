package br.com.boa50.kbingo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;
import br.com.boa50.kbingo.visualizacartelas.VisualizaCartelasActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(MainActivity.this, RealizaSorteioActivity.class);
        Intent intent = new Intent(MainActivity.this, VisualizaCartelasActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            setTheme(R.style.AppTheme);
            startActivity(intent);
        }, 2000);

    }
}
