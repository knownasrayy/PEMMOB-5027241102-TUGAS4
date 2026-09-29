package com.example.diaryapp.data;

/**
 * Model data Mahasiswa.
 * Merepresentasikan satu record baris pada tabel "mahasiswa".
 */
public class Mahasiswa {

    private long id;
    private String nama;
    private String nim;
    private String jurusan;
    private int angkatan;
    private String timestamp;

    public Mahasiswa() {}

    public Mahasiswa(long id, String nama, String nim, String jurusan, int angkatan, String timestamp) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.jurusan = jurusan;
        this.angkatan = angkatan;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }

    public int getAngkatan() { return angkatan; }
    public void setAngkatan(int angkatan) { this.angkatan = angkatan; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}