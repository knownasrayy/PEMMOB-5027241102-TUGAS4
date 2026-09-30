package com.example.diaryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DbHelper â€” SQLiteOpenHelper untuk database mahasiswa ITS.
 * Database: mahasiswa.db
 * Tabel: mahasiswa
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME    = "mahasiswa.db";
    public static final int    DATABASE_VERSION = 1;

    public static final String TABLE_MAHASISWA  = "mahasiswa";
    public static final String COL_ID           = "id";
    public static final String COL_NAMA         = "nama";
    public static final String COL_NIM          = "nim";
    public static final String COL_JURUSAN      = "jurusan";
    public static final String COL_ANGKATAN     = "angkatan";
    public static final String COL_TIMESTAMP    = "timestamp";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_MAHASISWA + " (" +
            COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAMA      + " TEXT NOT NULL, " +
            COL_NIM       + " TEXT NOT NULL UNIQUE, " +
            COL_JURUSAN   + " TEXT NOT NULL, " +
            COL_ANGKATAN  + " INTEGER NOT NULL, " +
            COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

        // Seed data dummy resmi: Rayhan Agnan Kusuma & Tiffany Viriya Chu
        db.execSQL("INSERT INTO " + TABLE_MAHASISWA + " (" +
                COL_NAMA + ", " + COL_NIM + ", " + COL_JURUSAN + ", " + COL_ANGKATAN + ") VALUES " +
                "('Rayhan Agnan Kusuma', '5027241102', 'Teknologi Informasi', 2024), " +
                "('Tiffany Viriya Chu', '5027241088', 'Teknologi Informasi', 2024)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAHASISWA);
        onCreate(db);
    }
}