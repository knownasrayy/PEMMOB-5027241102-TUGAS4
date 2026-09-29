package com.example.diaryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * MahasiswaRepository â€” Lapisan data access object untuk SQLite.
 * Semua query memakai parameterized "?" atau SQLite ContentValues (aman dari SQL Injection).
 * Pola lifecycle: open() di onStart(), close() di onStop().
 */
public class MahasiswaRepository {

    private final DbHelper dbHelper;
    private SQLiteDatabase db;

    public MahasiswaRepository(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // â”€â”€ CREATE â”€â”€
    public long insert(String nama, String nim, String jurusan, int angkatan) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_NAMA, nama);
        cv.put(DbHelper.COL_NIM, nim);
        cv.put(DbHelper.COL_JURUSAN, jurusan);
        cv.put(DbHelper.COL_ANGKATAN, angkatan);
        return db.insert(DbHelper.TABLE_MAHASISWA, null, cv);
    }

    // â”€â”€ READ ALL with Sort â”€â”€
    public List<Mahasiswa> getAll(String sortOrder) {
        List<Mahasiswa> list = new ArrayList<>();
        String orderBy;
        if ("oldest".equals(sortOrder)) {
            orderBy = DbHelper.COL_TIMESTAMP + " ASC, " + DbHelper.COL_ID + " ASC";
        } else if ("name_asc".equals(sortOrder)) {
            orderBy = DbHelper.COL_NAMA + " COLLATE NOCASE ASC";
        } else if ("nim_asc".equals(sortOrder)) {
            orderBy = DbHelper.COL_NIM + " ASC";
        } else {
            // default: newest
            orderBy = DbHelper.COL_TIMESTAMP + " DESC, " + DbHelper.COL_ID + " DESC";
        }

        Cursor cursor = db.rawQuery(
                "SELECT " + DbHelper.COL_ID + ", "
                + DbHelper.COL_NAMA + ", "
                + DbHelper.COL_NIM + ", "
                + DbHelper.COL_JURUSAN + ", "
                + DbHelper.COL_ANGKATAN + ", "
                + "datetime(" + DbHelper.COL_TIMESTAMP + ", 'localtime') AS ts "
                + "FROM " + DbHelper.TABLE_MAHASISWA + " "
                + "ORDER BY " + orderBy,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToMahasiswa(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    // â”€â”€ READ BY ID â”€â”€
    public Mahasiswa getById(long id) {
        Mahasiswa mhs = null;
        Cursor cursor = db.rawQuery(
                "SELECT " + DbHelper.COL_ID + ", "
                + DbHelper.COL_NAMA + ", "
                + DbHelper.COL_NIM + ", "
                + DbHelper.COL_JURUSAN + ", "
                + DbHelper.COL_ANGKATAN + ", "
                + "datetime(" + DbHelper.COL_TIMESTAMP + ", 'localtime') AS ts "
                + "FROM " + DbHelper.TABLE_MAHASISWA + " "
                + "WHERE " + DbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        try {
            if (cursor.moveToFirst()) {
                mhs = cursorToMahasiswa(cursor);
            }
        } finally {
            cursor.close();
        }
        return mhs;
    }

    // â”€â”€ SEARCH (Nama, NIM, atau Jurusan) â”€â”€
    public List<Mahasiswa> search(String query, String sortOrder) {
        List<Mahasiswa> list = new ArrayList<>();
        String orderBy;
        if ("oldest".equals(sortOrder)) {
            orderBy = DbHelper.COL_TIMESTAMP + " ASC, " + DbHelper.COL_ID + " ASC";
        } else if ("name_asc".equals(sortOrder)) {
            orderBy = DbHelper.COL_NAMA + " COLLATE NOCASE ASC";
        } else if ("nim_asc".equals(sortOrder)) {
            orderBy = DbHelper.COL_NIM + " ASC";
        } else {
            orderBy = DbHelper.COL_TIMESTAMP + " DESC, " + DbHelper.COL_ID + " DESC";
        }

        String pattern = "%" + query + "%";
        Cursor cursor = db.rawQuery(
                "SELECT " + DbHelper.COL_ID + ", "
                + DbHelper.COL_NAMA + ", "
                + DbHelper.COL_NIM + ", "
                + DbHelper.COL_JURUSAN + ", "
                + DbHelper.COL_ANGKATAN + ", "
                + "datetime(" + DbHelper.COL_TIMESTAMP + ", 'localtime') AS ts "
                + "FROM " + DbHelper.TABLE_MAHASISWA + " "
                + "WHERE " + DbHelper.COL_NAMA + " LIKE ? "
                + "OR " + DbHelper.COL_NIM + " LIKE ? "
                + "OR " + DbHelper.COL_JURUSAN + " LIKE ? "
                + "ORDER BY " + orderBy,
                new String[]{pattern, pattern, pattern}
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToMahasiswa(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    // â”€â”€ UPDATE â”€â”€
    public int update(long id, String nama, String nim, String jurusan, int angkatan) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_NAMA, nama);
        cv.put(DbHelper.COL_NIM, nim);
        cv.put(DbHelper.COL_JURUSAN, jurusan);
        cv.put(DbHelper.COL_ANGKATAN, angkatan);
        return db.update(
                DbHelper.TABLE_MAHASISWA,
                cv,
                DbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // â”€â”€ DELETE â”€â”€
    public int delete(long id) {
        return db.delete(
                DbHelper.TABLE_MAHASISWA,
                DbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    private Mahasiswa cursorToMahasiswa(Cursor cursor) {
        Mahasiswa m = new Mahasiswa();
        m.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COL_ID)));
        m.setNama(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_NAMA)));
        m.setNim(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_NIM)));
        m.setJurusan(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_JURUSAN)));
        m.setAngkatan(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COL_ANGKATAN)));
        m.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("ts")));
        return m;
    }
}