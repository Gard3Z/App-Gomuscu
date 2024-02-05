package com.example.gomuscu;

import java.util.List;

public class SeanceDetails {
    private long idSeance;
    private String date;
    private String description;
    private int repetitions;

    private List<ExoDetails> exercicesList;

    public SeanceDetails(long idSeance, String date, String description) {
        this.idSeance = idSeance;
        this.date = date;
        this.description = description;
        this.exercicesList = exercicesList;
    }

    public long getIdSeance() {
        return idSeance;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    // Ajoutez le getter pour les répétitions
    public int getRepetitions() {
        return repetitions;
    }

    // Ajoutez le setter pour les répétitions si nécessaire
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

}




