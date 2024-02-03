package com.example.gomuscu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnclickPerf(View view) {
        Intent intent = new Intent(this, PerfSeanceActivity.class);
        startActivity(intent);

    }

    public void OnclickMesStats(View view) {
        Intent intent = new Intent(this, MesStatsActivity.class);
        startActivity(intent);

    }

    public void OnclickMesSeances(View view) {
        Intent intent = new Intent(this, MesSeancesActivity.class);
        startActivity(intent);

    }

}
