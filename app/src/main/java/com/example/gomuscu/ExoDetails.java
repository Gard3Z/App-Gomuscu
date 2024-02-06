package com.example.gomuscu;

public class ExoDetails {
    private long idExoDetails;
    private long idExo;

    private long idDetails;
    private String nomExo;
    private long idSeance;
    private int repetitions;

    private String description;

    // Séries et poids
    private int serie;
    private double poids;


    public long getIdDetails() {
        return idDetails;
    }

    public void setIdDetails(long idDetails) {
        this.idDetails = idDetails;
    }
    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }


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

    public String getNomExo() {
        return nomExo;
    }

    public void setNomExo(String nomExo) {
        this.nomExo = nomExo;
    }

    // Getter et setter pour idSeance
    public long getIdSeance() {
        return idSeance;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIdSeance(long idSeance) {
        this.idSeance = idSeance;
    }

    private int typeExo; // Assurez-vous que cette variable est déclarée dans votre classe

    // Autres membres de la classe

    public int getTypeExo() {
        return typeExo;
    }

    public void setTypeExo(int typeExo) {
        this.typeExo = typeExo;
    }
}

