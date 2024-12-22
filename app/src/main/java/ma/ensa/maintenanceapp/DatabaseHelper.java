package ma.ensa.maintenanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom de la base de données et version
    private static final String DATABASE_NAME = "MaintenanceApp.db";
    private static final int DATABASE_VERSION = 2;

    // Tables
    public static final String TABLE_PROFESSIONNEL = "Professionnel";
    public static final String TABLE_CLIENT = "Client";
    public static final String TABLE_INTERVENTION = "Intervention";

    // Colonnes de la table Professionnel
    public static final String COL_PROF_ID = "id";
    public static final String COL_PROF_NOM = "nomComplet";
    public static final String COL_PROF_EMAIL = "email";
    public static final String COL_PROF_PASSWORD = "motDePasse";
    public static final String COL_PROF_TEL = "numTel";
    public static final String COL_PROF_METIER = "metier";

    // Colonnes de la table Client
    public static final String COL_CLIENT_ID = "id";
    public static final String COL_CLIENT_NOM = "nomComplet";
    public static final String COL_CLIENT_ADRESSE = "adresse";
    public static final String COL_CLIENT_TEL = "numTel";

    // Colonnes de la table Intervention
    public static final String COL_INTERVENTION_ID = "id";
    public static final String COL_INTERVENTION_PROF_ID = "idProfessionnel";
    public static final String COL_INTERVENTION_CLIENT_ID = "idClient";
    public static final String COL_INTERVENTION_TYPE = "type";
    public static final String COL_INTERVENTION_DATE = "date";
    public static final String COL_INTERVENTION_PRIX_E = "PrixE"; // Prix estimatif
    public static final String COL_INTERVENTION_PRIX_R = "PrixR"; // Prix réel
    public static final String COL_INTERVENTION_DUREE_E = "DureeE"; // Durée estimée
    public static final String COL_INTERVENTION_DUREE_R = "DureeR"; // Durée réelle
    public static final String COL_INTERVENTION_STATUT = "statut"; // 0 = en cours, 1 = terminé

    // Requêtes de création
    private static final String CREATE_TABLE_PROFESSIONNEL =
            "CREATE TABLE " + TABLE_PROFESSIONNEL + " (" +
                    COL_PROF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PROF_NOM + " TEXT, " +
                    COL_PROF_EMAIL + " TEXT UNIQUE, " +
                    COL_PROF_PASSWORD + " TEXT, " +
                    COL_PROF_TEL + " TEXT, " +
                    COL_PROF_METIER + " TEXT);";

    private static final String CREATE_TABLE_CLIENT =
            "CREATE TABLE " + TABLE_CLIENT + " (" +
                    COL_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CLIENT_NOM + " TEXT, " +
                    COL_CLIENT_ADRESSE + " TEXT, " +
                    COL_CLIENT_TEL + " TEXT);";

    private static final String CREATE_TABLE_INTERVENTION =
            "CREATE TABLE " + TABLE_INTERVENTION + " (" +
                    COL_INTERVENTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_INTERVENTION_PROF_ID + " INTEGER, " +
                    COL_INTERVENTION_CLIENT_ID + " INTEGER, " +
                    COL_INTERVENTION_TYPE + " TEXT, " +
                    COL_INTERVENTION_DATE + " TEXT, " +
                    COL_INTERVENTION_PRIX_E + " REAL, " +
                    COL_INTERVENTION_PRIX_R + " REAL, " +
                    COL_INTERVENTION_DUREE_E + " INTEGER, " +
                    COL_INTERVENTION_DUREE_R + " INTEGER, " +
                    COL_INTERVENTION_STATUT + " INTEGER, " +
                    "FOREIGN KEY(" + COL_INTERVENTION_PROF_ID + ") REFERENCES " + TABLE_PROFESSIONNEL + "(" + COL_PROF_ID + "), " +
                    "FOREIGN KEY(" + COL_INTERVENTION_CLIENT_ID + ") REFERENCES " + TABLE_CLIENT + "(" + COL_CLIENT_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROFESSIONNEL);
        db.execSQL(CREATE_TABLE_CLIENT);
        db.execSQL(CREATE_TABLE_INTERVENTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSIONNEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVENTION);
        onCreate(db);
    }

    // Méthodes d'insertion
    public boolean insertProfessionnel(String nom, String email, String password, String tel, String metier) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_PROF_NOM, nom);
        values.put(COL_PROF_EMAIL, email);
        values.put(COL_PROF_PASSWORD, password);
        values.put(COL_PROF_TEL, tel);
        values.put(COL_PROF_METIER, metier);

        long result = db.insert(TABLE_PROFESSIONNEL, null, values);
        return result != -1;
    }

    public boolean insertClient(String nom, String adresse, String tel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CLIENT_NOM, nom);
        values.put(COL_CLIENT_ADRESSE, adresse);
        values.put(COL_CLIENT_TEL, tel);

        long result = db.insert(TABLE_CLIENT, null, values);
        return result != -1;
    }

    public boolean insertIntervention(int profId, int clientId, String type, String date,
                                      double prixE, int dureeE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_INTERVENTION_PROF_ID, profId);
        values.put(COL_INTERVENTION_CLIENT_ID, clientId);
        values.put(COL_INTERVENTION_TYPE, type);
        values.put(COL_INTERVENTION_DATE, date);
        values.put(COL_INTERVENTION_PRIX_E, prixE);
        values.put(COL_INTERVENTION_PRIX_R, 0); // Prix réel par défaut
        values.put(COL_INTERVENTION_DUREE_E, dureeE);
        values.put(COL_INTERVENTION_DUREE_R, 0); // Durée réelle par défaut
        values.put(COL_INTERVENTION_STATUT, 0); // 0 = Non finalisé

        long result = db.insert(TABLE_INTERVENTION, null, values);
        return result != -1;
    }

    public boolean validateIntervention(int interventionId, double prixR, int dureeR) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_INTERVENTION_PRIX_R, prixR);
        values.put(COL_INTERVENTION_DUREE_R, dureeR);
        values.put(COL_INTERVENTION_STATUT, 1); // 1 = Finalisé

        int rowsUpdated = db.update(TABLE_INTERVENTION, values, COL_INTERVENTION_ID + " = ?",
                new String[]{String.valueOf(interventionId)});
        return rowsUpdated > 0; // Retourne true si une ligne a été mise à jour
    }
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFESSIONNEL, new String[]{COL_PROF_ID},
                COL_PROF_EMAIL + "=? AND " + COL_PROF_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}