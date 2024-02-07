package com.example.gomuscu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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


    public String getNombreExos(String exos) {
        c = db.rawQuery("SELECT COUNT(DISTINCT id_exo) AS nombre_d_ids_differents FROM exos", null);

        if (c.getCount() != 1) {
            return "erreur";
        }

        c.moveToFirst();
        String result = c.getString(0);

        // Ne fermez pas la base de données ici

        return result;
    }

    public List<String> getExosHaut(){
        List<String> result = new ArrayList<>();
        c = db.rawQuery("SELECT nom FROM exos WHERE type_exo = 1", null);

        if (c.getCount() == 0){
            return result; // Ou vous pouvez retourner "erreur" selon vos besoins.
        }

        while (c.moveToNext()) {
            result.add(c.getString(0));
        }

        return result;
    }

    public List<String> getExosBas(){
        List<String> result = new ArrayList<>();
        c = db.rawQuery("SELECT nom FROM exos WHERE type_exo = 0", null);

        if (c.getCount() == 0){
            return result; // Ou vous pouvez retourner "erreur" selon vos besoins.
        }

        while (c.moveToNext()) {
            result.add(c.getString(0));
        }

        return result;
    }

    public List<SeanceDetails> getAllSeances() {
        List<SeanceDetails> seancesList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT seance.id_seance, seance.date, GROUP_CONCAT(exos.description) AS description " +
                "FROM seance " +
                "JOIN exos_seance ON seance.id_seance = exos_seance.id_seance " +
                "JOIN exos ON exos_seance.id_exo = exos.id_exo " +
                "GROUP BY seance.id_seance", null);

        while (cursor.moveToNext()) {
            long seanceId = cursor.getLong(cursor.getColumnIndex("id_seance"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            seancesList.add(new SeanceDetails(seanceId, date, description));
        }

        cursor.close();
        return seancesList;
    }

    public List<ExoDetails> getExoDetailsForSeance(long idSeance) {
        List<ExoDetails> exoDetailsList = new ArrayList<>();

        // Utilisez JOIN pour récupérer les détails de exo_details
        c = db.rawQuery("SELECT exos.nom, exos.type_exo, exo_details.poids, exo_details.serie " +
                "FROM exos_seance " +
                "JOIN exos ON exos_seance.id_exo = exos.id_exo " +
                "LEFT JOIN exo_details ON exos_seance.id_details = exo_details.id_details " +
                "WHERE exos_seance.id_seance = ?", new String[]{String.valueOf(idSeance)});

        while (c.moveToNext()) {
            ExoDetails exoDetails = new ExoDetails();
            exoDetails.setNomExo(c.getString(0));
            exoDetails.setTypeExo(c.getInt(1));
            exoDetails.setPoids(c.getDouble(2));
            exoDetails.setSerie(c.getInt(3));
            exoDetailsList.add(exoDetails);
        }

        c.close();
        return exoDetailsList;
    }


    public long insertSeance(String date, String type) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("type", type);

        // Insérez la séance et récupérez son ID
        return db.insert("seance", null, values);
    }

    public long insertExosSeance(long seanceId, long idExo) {
        ContentValues values = new ContentValues();
        values.put("id_seance", seanceId);
        values.put("id_exo", idExo);

        // Insérez l'exercice associé à la séance
        long result = db.insert("exos_seance", null, values);

        // Vérifiez si l'insertion s'est bien déroulée
        if (result == -1) {
            // Gérer l'erreur (journalisation, affichage d'un message, etc.)
            // Vous pouvez également lancer une exception si vous le souhaitez.
        }

        return result;
    }
    public long getExoIdByName(String nomExo) {
        Cursor cursor = db.rawQuery("SELECT id_exo FROM exos WHERE nom = ?", new String[]{nomExo});

        long idExo = -1; // Valeur par défaut si l'exercice n'est pas trouvé
        if (cursor.moveToFirst()) {
            idExo = cursor.getLong(cursor.getColumnIndex("id_exo"));
        }

        cursor.close();
        return idExo;
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



    public long insertExosDetails(long idDetails, double poids, int serie) {

        ContentValues values = new ContentValues();
        values.put("id_details", idDetails);
        values.put("poids", poids);
        values.put("serie", serie);

        Log.d("DatabaseAcess", "Inserting into exo_details: id_details=" + idDetails + ", poids=" + poids + ", serie=" + serie);

        // Insérez les détails de l'exercice associé à la séance
        return db.insert("exo_details", null, values);
    }

    public void closeDatabase() {
        if (db != null) {
            db.close();
        }
    }

}
