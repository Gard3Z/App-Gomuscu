package com.example.gomuscu;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


public class PerfSeanceActivity extends AppCompatActivity {
    private LinearLayout containerLayout;
    private boolean isInitialSelection;
    private TextView tvDate;
    private DatabaseAcess db;
    private String hautDuCorpsExo ="";
    private static final String CHANNEL_ID = "MyChannelID";

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

            Spinner spinnerTypeEntrainement = findViewById(R.id.spinner_type_entrainement);

            // Utilisation d'un SimpleAdapter personnalisé avec un layout personnalisé pour les éléments du Spinner
            ArrayAdapter<String> adapterTypeEntrainement = new ArrayAdapter<>(
                    this, R.layout.spinner_item_layout,
                    new String[]{"Haut du corps", "Bas du corps"});
            adapterTypeEntrainement.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTypeEntrainement.setAdapter(adapterTypeEntrainement);

            // Gestionnaire d'événements pour le Spinner principal (type d'entraînement)

            // Ajoutez cette ligne pour définir la couleur du texte
            spinnerTypeEntrainement.getBackground().setColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_ATOP);
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
                selectedExos = db.getExosHaut();
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

        // Obtenez la sélection actuelle du Spinner principal (type d'entraînement)
        Spinner spinnerTypeEntrainement = findViewById(R.id.spinner_type_entrainement);
        int selectedTypeIndex = spinnerTypeEntrainement.getSelectedItemPosition();

        // Obtenez la liste appropriée d'exercices en fonction du type d'entraînement sélectionné
        List<String> selectedExos;
        if (selectedTypeIndex == 0) {
            // Haut du corps
            selectedExos = db.getExosHaut();
        } else {
            // Bas du corps
            selectedExos = db.getExosBas();
        }

        // Mettez à jour le Spinner des exercices avec le nom des exercices appropriés
        ArrayAdapter<String> adapterExercices = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, selectedExos);
        adapterExercices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercices.setAdapter(adapterExercices);

        // Ajoutez la nouvelle ligne au conteneur
        containerLayout.addView(rowLayout);
    }



    public void onSaveButtonClick(View view) {
        Log.d("SaveButtonClick", "Button Clicked");

        // Obtenez la date actuelle pour la séance
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Vérifiez si au moins un exercice a été ajouté
        if (containerLayout.getChildCount() == 0) {
            // Affichez un message indiquant que la liste d'exercices est vide
            Toast.makeText(this, "Veuillez ajouter au moins un exercice.", Toast.LENGTH_SHORT).show();
            Log.d("SaveButtonClick", "No exercises added");
            return; // Sortez de la méthode pour éviter la sauvegarde
        }

        // Créez une seule séance pour tous les exercices
        long seanceId = db.insertSeance(currentDate, "Type de séance à déterminer");

        // Parcourez toutes les lignes pour récupérer les exercices sélectionnés
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View rowLayout = containerLayout.getChildAt(i);
            Spinner spinnerExercices = rowLayout.findViewById(R.id.spinner_exercices);
            EditText etPoids = rowLayout.findViewById(R.id.et_poids);
            EditText etSeries = rowLayout.findViewById(R.id.et_series);

            String selectedExo = (String) spinnerExercices.getSelectedItem();
            double poids;
            int serie;

            // Vérifiez si les champs "poids" et "série" sont remplis
            if (etPoids.getText().toString().isEmpty() || etSeries.getText().toString().isEmpty()) {
                // Affichez un message indiquant que les champs doivent être remplis
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                Log.d("SaveButtonClick", "Fields not filled");
                return; // Sortez de la méthode pour éviter la sauvegarde
            } else {
                poids = Double.parseDouble(etPoids.getText().toString());
                serie = Integer.parseInt(etSeries.getText().toString());
            }

            Log.d("ExerciseDetails", "Exercise: " + selectedExo + ", Weight: " + poids + ", Sets: " + serie);

            // Récupérez l'ID de l'exercice depuis la base de données en fonction du nom
            long idExo = db.getExoIdByName(selectedExo);

            // Récupérez les détails de l'exercice depuis la base de données
            ExoDetails exoDetails = db.getExoDetails(selectedExo);

            // Déterminez le type de séance en fonction de type_exo
            String seanceType = (exoDetails.getTypeExo() == 0) ? "Bas du corps" : "Haut du corps";

            // Insérez l'exercice associé à la séance en utilisant le même ID de séance pour tous les exercices
            long exosSeanceId = db.insertExosSeance(seanceId, idExo);

            // Insérez les détails de l'exercice associé à la séance
            db.insertExosDetails(exosSeanceId, poids, serie);
        }
        Utilitaire.performVibration(this);
        Utilitaire.showNotification(this, "Seance", "Bien enregistrée");

        // Affichez un message ou effectuez d'autres actions après avoir sauvegardé les données
        // ...
        // Fermez l'activité ou effectuez d'autres actions en fonction de vos besoins
        finish();
    }






    public void OnclickLogo(View view) {
        Utilitaire.performVibration(this);
        this.finish();
    }
}

