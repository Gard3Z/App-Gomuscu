package com.example.gomuscu;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PerfSeanceActivity extends AppCompatActivity {
    private LinearLayout containerLayout;
    private boolean isInitialSelection;
    private TextView tvDate;
    private DatabaseAcess db;
    private String hautDuCorpsExo ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf_seance);

        db = DatabaseAcess.getInstance(getApplicationContext());
        db.open();

        containerLayout = findViewById(R.id.line_container);
        tvDate = findViewById(R.id.tv_date);

        // Obtenez la date actuelle
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Affichez la date actuelle dans le TextView
        tvDate.setText(currentDate);

        // Lire le fichier JSON
        String json = loadJSONFromResource(R.raw.exercices);

        try {
            JSONObject jsonObject = new JSONObject(json);

            // Spinner pour choisir entre "haut du corps" et "bas du corps"
            Spinner spinnerTypeEntrainement = findViewById(R.id.spinner_type_entrainement);
            ArrayAdapter<String> adapterTypeEntrainement = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    new String[]{"Haut du corps", "Bas du corps"});
            adapterTypeEntrainement.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTypeEntrainement.setAdapter(adapterTypeEntrainement);

            // Gestionnaire d'événements pour le Spinner principal (type d'entraînement)
            spinnerTypeEntrainement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Mettez à jour tous les Spinners des exercices lorsque le type d'entraînement change
                    updateAllExercisesSpinners(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Faites quelque chose en cas de sélection nulle
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Fermez la base de données lorsque l'activité est détruite
        db.close();
    }

    private void updateAllExercisesSpinners(int selectedTypeIndex) {
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View rowLayout = containerLayout.getChildAt(i);
            Spinner spinnerExercices = rowLayout.findViewById(R.id.spinner_exercices);

            List<String> selectedExos;
            if (selectedTypeIndex == 0) {
                // Haut du corps
                selectedExos = Collections.singletonList(db.getExosHaut());
            } else {
                // Bas du corps
                selectedExos = db.getExosBas();
            }

            // Mise à jour du Spinner avec le nom des exercices
            ArrayAdapter<String> adapterExercices = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, selectedExos);
            adapterExercices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerExercices.setAdapter(adapterExercices);
        }
    }


    private void updateExercisesSpinner(JSONObject jsonObject, int selectedTypeIndex, Spinner spinnerExercices) {
        ArrayAdapter<String> adapterExercices;

        try {
            JSONArray exercicesArray;
            if (selectedTypeIndex == 0) {
                // Haut du corps
                exercicesArray = jsonObject.getJSONArray("haut_du_corps");
            } else {
                // Bas du corps
                exercicesArray = jsonObject.getJSONArray("bas_du_corps");
            }

            // Extraire les noms des exercices
            List<String> exercicesList = new ArrayList<>();
            for (int i = 0; i < exercicesArray.length(); i++) {
                JSONObject exercice = exercicesArray.getJSONObject(i);
                exercicesList.add(exercice.getString("nom"));
            }

            // Mettez à jour l'adaptateur du Spinner avec les noms des exercices
            adapterExercices = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, exercicesList);
            adapterExercices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerExercices.setAdapter(adapterExercices);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromResource(int resourceId) {
        String json;
        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(resourceId);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            json = null;
        }
        return json;
    }

    public void onAddLineClick(View view) {
        // Utilisez LayoutInflater pour créer une nouvelle ligne
        View rowLayout = getLayoutInflater().inflate(R.layout.row_layout, null);

        // Trouvez le Spinner des exercices dans la nouvelle ligne
        Spinner spinnerExercices = rowLayout.findViewById(R.id.spinner_exercices);

        // Utilisez la fonction getExosHaut pour obtenir les exercices du haut du corps
        hautDuCorpsExo = db.getExosHaut();

        // Mettez à jour le Spinner des exercices avec le nom des exercices du haut du corps
        ArrayAdapter<String> adapterExercices = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Collections.singletonList(hautDuCorpsExo));
        adapterExercices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercices.setAdapter(adapterExercices);

        // Ajoutez la nouvelle ligne au conteneur
        containerLayout.addView(rowLayout);
    }



    public void onSaveButtonClick(View view) {
        // Obtenez la date actuelle pour la séance
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Insérez la séance dans la base de données
        long seanceId = db.insertSeance(currentDate);

        // Parcourez toutes les lignes pour récupérer les exercices sélectionnés
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View rowLayout = containerLayout.getChildAt(i);
            Spinner spinnerExercices = rowLayout.findViewById(R.id.spinner_exercices);

            String selectedExo = (String) spinnerExercices.getSelectedItem();

            // Insérez l'exercice associé à la séance
            db.insertExosSeance(seanceId, selectedExo);

            // Récupérez les détails de l'exercice depuis la base de données
            ExoDetails exoDetails = db.getExoDetails(selectedExo);

            // Insérez les détails de l'exercice associé à la séance
            db.insertExosDetails(seanceId, exoDetails.getIdExo(), exoDetails.getDescription());
        }

        // Affichez un message ou effectuez d'autres actions après avoir sauvegardé les données
        // ...

        // Fermez l'activité ou effectuez d'autres actions en fonction de vos besoins
        finish();
    }


    public void OnclickLogo(View view) {
        this.finish();
    }
}
