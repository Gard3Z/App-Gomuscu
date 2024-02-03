package com.example.gomuscu;

public class ExoDetails {
    private long idExoDetails;
    private long idExo;
    private long idSeance;
    private int repetitions;

    // Séries et poids
    private int serie1;
    private int serie2;
    private int serie3;
    private double poids1;
    private double poids2;
    private double poids3;

    // Constructeurs, getters et setters...

    // Getter et setter pour idExoDetails
    public long getIdExoDetails() {
        return idExoDetails;
    }

    public void setIdExoDetails(long idExoDetails) {
        this.idExoDetails = idExoDetails;
    }

    // Getter et setter pour idExo
    public long getIdExo() {
        return idExo;
    }

    public void setIdExo(long idExo) {
        this.idExo = idExo;
    }

    // Getter et setter pour idSeance
    public long getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(long idSeance) {
        this.idSeance = idSeance;
    }

    // Getter et setter pour repetitions
    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    // Getter et setter pour serie1
    public int getSerie1() {
        return serie1;
    }

    public void setSerie1(int serie1) {
        this.serie1 = serie1;
    }

    // Getter et setter pour serie2
    public int getSerie2() {
        return serie2;
    }

    public void setSerie2(int serie2) {
        this.serie2 = serie2;
    }

    // Getter et setter pour serie3
    public int getSerie3() {
        return serie3;
    }

    public void setSerie3(int serie3) {
        this.serie3 = serie3;
    }

    // Getter et setter pour poids1
    public double getPoids1() {
        return poids1;
    }

    public void setPoids1(double poids1) {
        this.poids1 = poids1;
    }

    // Getter et setter pour poids2
    public double getPoids2() {
        return poids2;
    }

    public void setPoids2(double poids2) {
        this.poids2 = poids2;
    }

    // Getter et setter pour poids3
    public double getPoids3() {
        return poids3;
    }

    public void setPoids3(double poids3) {
        this.poids3 = poids3;
    }
}
