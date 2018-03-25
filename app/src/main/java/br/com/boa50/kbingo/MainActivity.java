package br.com.boa50.kbingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.boa50.kbingo.realizasorteio.RealizaSorteioActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO depois remover essa activity
        Intent intent = new Intent(MainActivity.this, RealizaSorteioActivity.class);
        startActivity(intent);
    }
}
