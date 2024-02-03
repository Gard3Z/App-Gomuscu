package com.example.gomuscu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAcess {

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAcess instance;
    Cursor c = null;

    private DatabaseAcess(Context context) { this.openHelper = new DatabaseOpenHelper(context); }

    public static DatabaseAcess getInstance(Context context){
        if (instance==null){
            instance = new DatabaseAcess(context);
        }
        return instance;
    }

    public void open() { this.db=openHelper.getWritableDatabase(); }

    public void close(){
        if(db!=null){
            this.db.close();
        }
    }


    public String getNombreExos(String exos){
        c=db.rawQuery("SELECT COUNT(DISTINCT id_exo) AS nombre_d_ids_differents FROM exos",null);

        if (c.getCount()!=1){
            return "erreur";
        }
        c.moveToFirst();
        return c.getString(0);
    }

    public String getExosHaut(){
        c=db.rawQuery("SELECT nom FROM exos WHERE description LIKE '%Haut du corps%'", null);

        if (c.getCount()!=1){
            return "erreur";
        }
        c.moveToFirst();
        return c.getString(0);
    }

    public List<String> getExosBas(){
        List<String> result = new ArrayList<>();
        c = db.rawQuery("SELECT nom FROM exos WHERE description LIKE '%Bas du corps%'", null);

        if (c.getCount() == 0){
            return result; // Ou vous pouvez retourner "erreur" selon vos besoins.
        }

        while (c.moveToNext()) {
            result.add(c.getString(0));
        }

        return result;
    }


    public long insertSeance(String date) {
        ContentValues values = new ContentValues();
        values.put("date", date);

        // Insérez la séance et récupérez son ID
        return db.insert("seance", null, values);
    }

    public long insertExosSeance(long seanceId, String nomExo) {
        ContentValues values = new ContentValues();
        values.put("id_seance", seanceId);
        values.put("nom_exo", nomExo);

        // Insérez l'exercice associé à la séance
        return db.insert("exos_seance", null, values);
    }

    public ExoDetails getExoDetails(String nomExo) {
        // Récupérez les détails de l'exercice à partir de la base de données
        // Assurez-vous d'ajuster cette méthode en fonction de votre structure de base de données
        Cursor c = db.rawQuery("SELECT * FROM exos WHERE nom = ?", new String[]{nomExo});

        ExoDetails exoDetails = new ExoDetails();
        if (c.moveToFirst()) {
            exoDetails.setIdExo(c.getLong(c.getColumnIndex("id_exo")));
            exoDetails.setDescription(c.getString(c.getColumnIndex("description")));
        }

        c.close();
        return exoDetails;
    }

    public long insertExosDetails(long exosSeanceId, long idExo, String description) {
        ContentValues values = new ContentValues();
        values.put("id_exos_seance", exosSeanceId);
        values.put("id_exo", idExo);
        values.put("description", description);

        // Insérez les détails de l'exercice associé à la séance
        return db.insert("exos_details", null, values);
    }

}
