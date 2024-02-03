package com.example.gomuscu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MesSeancesActivity extends AppCompatActivity {

    private String nombreExos ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_seances);

        final DatabaseAcess db = DatabaseAcess.getInstance(getApplicationContext());
        db.open();

        nombreExos = db.getNombreExos("");

        TextView testdb = (TextView) findViewById(R.id.testdb);
        testdb.setText(nombreExos);

        db.close();
    }

    public void OnclickLogo(View view) {
        this.finish();
    }
}