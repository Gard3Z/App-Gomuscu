package com.example.gomuscu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.data.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Vibrator;
import android.widget.Toast;


public class MesStatsActivity extends AppCompatActivity {

    private LinearLayout containerLayout;
    private TextView tvDate;
    private DatabaseAcess db;

    // Déclaration des variables de graphique et de vue de graphique en tant que membres de classe
    private AnyChartView anyChartView;
    private com.anychart.charts.Cartesian lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_stats);

        db = DatabaseAcess.getInstance(getApplicationContext());
        db.open();

        anyChartView = findViewById(R.id.any_chart_view);
        lineChart = AnyChart.line();


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
                    //updateAllExercisesSpinners(position);
                    updateExercisesSpinner(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Faites quelque chose en cas de sélection nulle
                }
            });

            Spinner spinnerExercices = findViewById(R.id.spinner_exercices);

            // Gestionnaire d'événements pour le Spinner des exercices
            spinnerExercices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Lorsqu'un exercice est sélectionné, récupérez le nom de l'exercice
                    String selectedExo = (String) parentView.getItemAtPosition(position);
                    Log.d("DEBUG", "selected exo onitem"+ selectedExo);
                            createGraph(selectedExo);


                    // Appelez la fonction getStats avec le nom de l'exercice sélectionné
                    if (!"bench".equals(selectedExo)) {
                        //getStats(selectedExo);
                        //anyChartView.clear();
                        createGraph(selectedExo);
                        Log.d("DEBUG", "selected exo onitem condition"+ selectedExo);
                        //updateGraph(selectedExo);



                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Gestion de l'événement lorsque rien n'est sélectionné
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    private void createGraph(String exerciseName) {
        anyChartView.clear();
        // Supposons que vous ayez une liste d'objets Performance pour chaque séance
        List<Performance> performances = db.getStats(exerciseName);

        // Créer une liste pour stocker les données du graphique
        List<DataEntry> data = new ArrayList<>();

        // Remplir la liste de données avec les performances
        for (Performance performance : performances) {
            data.add(new ValueDataEntry(performance.getDate(), performance.getWeight()));
        }

        // Initialiser la vue de graphique et le graphique une seule fois dans onCreate
        //com.anychart.charts.Cartesian lineChart = AnyChart.line();

        // Ajouter les données au graphique
        lineChart.data(data);

        // Ajouter le titre et les titres des axes
        lineChart.title("Évolution des performances");
        lineChart.xAxis(0).title("Date");
        lineChart.yAxis(0).title("Poids (kg)");

        lineChart.background("#2C3950");

        // Définir le graphique sur la vue
        anyChartView.setChart(lineChart);

        // Afficher le graphique
        anyChartView.invalidate();
    }

    private void updateGraph(String exerciseName) {
        anyChartView.clear();
        // Supposons que vous ayez une liste d'objets Performance pour chaque séance
        List<Performance> performances = db.getStats(exerciseName);

        // Créer une liste pour stocker les données du graphique
        List<DataEntry> data2 = new ArrayList<>();

        // Remplir la liste de données avec les performances
        for (Performance performance : performances) {
            data2.add(new ValueDataEntry(performance.getDate(), performance.getWeight()));
        }

        // Ajouter les données au graphique
        lineChart.data(data2);
        anyChartView.invalidate();

    }



    // Méthode factice pour simuler des données de performance
    private List<Performance> getPerformanceData() {
        List<Performance> performances = new ArrayList<>();

        performances.add(new Performance("2022-01-01", 60, 3));
        performances.add(new Performance("2022-01-02", 62, 3));
        performances.add(new Performance("2022-01-03", 65, 3));
        performances.add(new Performance("2022-01-04", 63, 3));
        performances.add(new Performance("2022-01-05", 68, 3));
        performances.add(new Performance("2022-01-06", 70, 3));
        performances.add(new Performance("2022-01-07", 72, 3));
        performances.add(new Performance("2022-01-08", 75, 3));
        performances.add(new Performance("2022-01-09", 78, 3));
        performances.add(new Performance("2022-01-10", 80, 3));

        return performances;
    }

    public static class Performance {
        private String date;
        private double weight;
        private int sets;

        public Performance(String date, double weight, int sets) {
            this.date = date;
            this.weight = weight;
            this.sets = sets;
        }

        public String getDate() {
            return date;
        }

        public double getWeight() {
            return weight;
        }

        public int getSets() {
            return sets;
        }

        public String getDateSeance() {
            return date;
        }
        @Override
        public String toString() {
            return "Performance{" +
                    "date='" + date + '\'' +
                    ", weight=" + weight +
                    ", sets=" + sets +
                    '}';
        }
    }


    private void updateExercisesSpinner(int selectedTypeIndex) {
        Spinner spinnerExercices = findViewById(R.id.spinner_exercices);
        List<String> selectedExos;

        if (selectedTypeIndex == 0) {
            // Haut du corps
            selectedExos = db.getExosHaut();
        } else {
            // Bas du corps
            selectedExos = db.getExosBas();
        }

        // Mettez à jour l'adaptateur du Spinner avec les noms des exercices
        ArrayAdapter<String> adapterExercices = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, selectedExos);
        adapterExercices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercices.setAdapter(adapterExercices);
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

    private void getStats(String nomExo) {
        // Appelez ici votre fonction getStats avec le nom de l'exercice
        List<Performance> performances = db.getStats(nomExo);

        for (Performance performance : performances) {
            Log.d("DEBUG", "Performance: Date: " + performance.getDateSeance() + ", Poids: " + performance.getWeight() + ", Sets=" + performance.getSets());
        }

        // Mettez à jour le graphique avec les nouvelles données
        updateChartWithNewData(performances);
    }

    // Fonction pour mettre à jour le graphique avec de nouvelles données
    private void updateChartWithNewData(List<Performance> performances) {

        // Créer une liste pour stocker les données du graphique
        List<DataEntry> data = new ArrayList<>();
        Log.d("DEBUG", "Received performances list: " + performances.toString());

        // Utilisez SimpleDateFormat pour reformater la date
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Remplir la liste de données avec les performances
        for (Performance performance : performances) {
            try {
                // Reformatez la date avant de l'ajouter aux données du graphique
                String formattedDate = outputFormat.format(inputFormat.parse(performance.getDateSeance()));
                Log.d("DEBUG", "Formatted Date: " + formattedDate);
                data.add(new ValueDataEntry(formattedDate, performance.getWeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("DEBUG", "Data for the chart: " + data.toString());
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        // Créer le graphique de ligne
        com.anychart.charts.Cartesian lineChart = AnyChart.line();

        // Ajouter les données au graphique
        lineChart.data(data);

        // Ajouter le titre et les titres des axes
        lineChart.title("Évolution des performances");
        lineChart.xAxis(0).title("Date de séance");
        lineChart.yAxis(0).title("Poids (kg)");

        // Définir le graphique sur la vue
        anyChartView.setChart(lineChart);

        // Afficher le graphique
        anyChartView.invalidate();
    }
    public void OnclickLogo(View view) {
        Utilitaire.performVibration(this);
        Utilitaire.showNotification(this, "Test", "TEst");
        this.finish();
    }




}