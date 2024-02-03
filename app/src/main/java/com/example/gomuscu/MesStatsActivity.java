package com.example.gomuscu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.List;

public class MesStatsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_stats);

        // Supposons que vous ayez une liste d'objets Performance pour chaque séance
        List<Performance> performances = getPerformanceData();

        // Créer une liste pour stocker les données du graphique
        List<DataEntry> data = new ArrayList<>();

        // Remplir la liste de données avec les performances
        for (Performance performance : performances) {
            data.add(new ValueDataEntry(performance.getDate(), performance.getWeight()));
        }

        // Créer le graphique de ligne
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        // Créer le graphique de ligne
        com.anychart.charts.Cartesian lineChart = AnyChart.line();

        // Ajouter les données au graphique
                lineChart.data(data);

        // Ajouter le titre et les titres des axes
                lineChart.title("Évolution des performances");
                lineChart.xAxis(0).title("Date");
                lineChart.yAxis(0).title("Poids (kg)");


        // Définir le graphique sur la vue
                anyChartView.setChart(lineChart);

        // Afficher le graphique
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

    public class Performance {
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
    }



}