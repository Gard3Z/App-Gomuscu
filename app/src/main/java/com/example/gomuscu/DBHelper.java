package com.example.gomuscu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // Création de la table Seance
        DB.execSQL("CREATE TABLE Seance (id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE)");

        // Création de la table ExosSeance
        DB.execSQL("CREATE TABLE ExosSeance (id INTEGER PRIMARY KEY AUTOINCREMENT, id_seance INTEGER, id_exo INTEGER, " +
                "FOREIGN KEY (id_seance) REFERENCES Seance(id), FOREIGN KEY (id_exo) REFERENCES Exercice(id))");

        // Création de la table Details avec une seule colonne pour poids et série
        DB.execSQL("CREATE TABLE Details (id INTEGER PRIMARY KEY AUTOINCREMENT, id_seance INTEGER, id_exo INTEGER, " +
                "poids_serie TEXT, " +
                "FOREIGN KEY (id_seance) REFERENCES Seance(id), FOREIGN KEY (id_exo) REFERENCES Exercice(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        // Suppression des tables si elles existent déjà
        DB.execSQL("DROP TABLE IF EXISTS Seance");
        DB.execSQL("DROP TABLE IF EXISTS ExosSeance");
        DB.execSQL("DROP TABLE IF EXISTS Details");

        // Recréation des tables après la mise à jour
        onCreate(DB);
    }
    public Boolean insertuserdata(String weightReps) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("weight_reps", weightReps); // Suppose que vous avez une colonne "weight_reps" dans votre table

        long results = DB.insert("table_name", null, contentValues); // Assurez-vous de remplacer "table_name" par le nom correct de votre table
        return results != -1; // Retourne vrai si l'insertion a réussi, faux sinon
    }


    public Boolean updateuserdata(String weightReps) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("weight_reps", weightReps); // Suppose que vous avez une colonne "weight_reps" dans votre table

        int affectedRows = DB.update("table_name", contentValues, null, null); // Assurez-vous de remplacer "table_name" par le nom correct de votre table
        return affectedRows > 0; // Retourne vrai si au moins une ligne a été mise à jour, faux sinon
    }

    public Boolean deletedata(int id_seance, int id_exo){
        SQLiteDatabase DB = this.getWritableDatabase();

        // Clause WHERE pour sélectionner la ligne à supprimer
        String selection = "id_seance = ? AND id_exo = ?";
        String[] selectionArgs = { String.valueOf(id_seance), String.valueOf(id_exo) };

        int rowsAffected = DB.delete("Details", selection, selectionArgs);

        return rowsAffected > 0; // Vérifie si des lignes ont été affectées par la suppression
    }



    public Cursor getdata(int id_seance, int id_exo){
        SQLiteDatabase DB = this.getWritableDatabase();

        // Clause WHERE pour sélectionner les données spécifiques
        String selection = "id_seance = ? AND id_exo = ?";
        String[] selectionArgs = { String.valueOf(id_seance), String.valueOf(id_exo) };

        return DB.query("Details", null, selection, selectionArgs, null, null, null);
    }
}
