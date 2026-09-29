# Aplikasi Data Mahasiswa (Tugas 4 Pemrograman Mobile)

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)
[![Database](https://img.shields.io/badge/Database-SQLite-blue.svg)](https://www.sqlite.org)
[![UI/UX](https://img.shields.io/badge/Design-Material%20Design%203-indigo.svg)](https://m3.material.io)

Proyek ini dibangun untuk memenuhi kriteria **Tugas 4: Fungsionalitas, Kebenaran Skema & Query SQLite, Uji Persistensi Data** pada mata kuliah Pemrograman Mobile. Aplikasi menyajikan manajemen data mahasiswa secara *offline-first* dengan arsitektur *Repository Pattern*, query aman dari *SQL Injection*, serta antarmuka modern Material Design 3 (M3).

---

## ðŸ“Œ Pemenuhan Rubrik Penilaian Tugas 4

| Kriteria | Implementasi | Status |
|---|---|:---:|
| **1. Fungsionalitas CRUD** | **Create:** Input data baru via `MahasiswaFormActivity`<br>**Read:** Tampil list via `RecyclerView` ber-kartu M3<br>**Update:** Edit data mahasiswa via Form Mode Edit<br>**Delete:** Hapus data dengan dialog konfirmasi aman | âœ… **Lengkap** |
| **2. Fitur Pencarian & Filter** | Pencarian *real-time* berbasis Nama, NIM, atau Jurusan, serta menu multi-kriteria sort (Terbaru, Terlama, Nama A-Z, NIM) | âœ… **Lengkap** |
| **3. Kebenaran Skema SQLite** | Menggunakan `SQLiteOpenHelper` dengan DDL: `id` (PK AUTOINCREMENT), `nim` (TEXT NOT NULL UNIQUE), `nama` & `jurusan` (TEXT NOT NULL), `angkatan` (INTEGER NOT NULL), `timestamp` (DATETIME DEFAULT CURRENT_TIMESTAMP) | âœ… **Lengkap** |
| **4. Kebenaran Query SQLite** | Semua manipulasi data menggunakan *parameterized queries* (`?`) dan `ContentValues` untuk proteksi total terhadap serangan *SQL Injection*. Penutupan `Cursor` selalu terjamin di blok `finally`. | âœ… **Lengkap** |
| **5. Uji Persistensi Data** | Siklus koneksi database dikelola secara rapi di `onStart()` dan `onStop()`. Data telah diuji tetap tersimpan 100% setelah aplikasi dimatikan paksa (*force-stop*) dan dibuka kembali (*cold restart*). | âœ… **Lengkap** |
| **6. Kualitas UI/UX M3** | Menggunakan *Google Material Design 3*: palet warna *Deep Slate Indigo*, *monogram initial avatar*, *pills & badges*, `TextInputLayout` dengan ikon penanda, serta FAB adaptif scroll. | âœ… **Lengkap** |

---

## ðŸ—„ï¸ Skema Database SQLite (`mahasiswa.db`)

Didefinisikan dalam kelas `com.example.diaryapp.data.DbHelper`:

```sql
CREATE TABLE mahasiswa (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nama      TEXT NOT NULL,
    nim       TEXT NOT NULL UNIQUE,
    jurusan   TEXT NOT NULL,
    angkatan  INTEGER NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## ðŸ–¼ï¸ Tampilan Antarmuka & Hasil Pengujian

| 1. Status Kosong (*Empty State*) | 2. Form Input Mahasiswa (M3) |
|:---:|:---:|
| <img src="docs/screenshots/01_empty_state.png" width="320"/> | <img src="docs/screenshots/03_form_filled.png" width="320"/> |

| 3. Daftar Mahasiswa Terdaftar | 4. Detail Profil Mahasiswa |
|:---:|:---:|
| <img src="docs/screenshots/04_list_mahasiswa.png" width="320"/> | <img src="docs/screenshots/05_detail_mahasiswa.png" width="320"/> |

| 5. Dialog Konfirmasi Hapus | 6. Menu Urutan Sortir & Persistensi |
|:---:|:---:|
| <img src="docs/screenshots/06_delete_dialog.png" width="320"/> | <img src="docs/screenshots/07_sort_menu.png" width="320"/> |

---

## ðŸ—ï¸ Struktur Arsitektur Kode

```
app/src/main/
â”œâ”€â”€ AndroidManifest.xml
â”œâ”€â”€ java/com/example/diaryapp/
â”‚   â”œâ”€â”€ data/
â”‚   â”‚   â”œâ”€â”€ Mahasiswa.java             # Model Entity POJO
â”‚   â”‚   â”œâ”€â”€ DbHelper.java              # SQLiteOpenHelper & DDL
â”‚   â”‚   â””â”€â”€ MahasiswaRepository.java   # Data Access Object (CRUD & Parameterized Query)
â”‚   â””â”€â”€ ui/
â”‚       â”œâ”€â”€ MainActivity.java          # Layar Utama, Search, Sort & Lifecycle
â”‚       â”œâ”€â”€ MahasiswaAdapter.java      # Adapter RecyclerView & Monogram
â”‚       â”œâ”€â”€ MahasiswaFormActivity.java # Form Tambah / Edit & Validasi Input
â”‚       â””â”€â”€ MahasiswaDetailActivity.java# Detail Mahasiswa & Dialog Hapus
â””â”€â”€ res/
    â”œâ”€â”€ layout/                        # Desain Layout XML Material Design 3
    â”œâ”€â”€ menu/                          # Menu Pencarian & Sortir
    â””â”€â”€ values/                        # Color Tokens, Themes, & Strings
```

---

## ðŸš€ Cara Menjalankan Proyek

1. **Clone repository ini:**
   ```bash
   git clone https://github.com/knownasrayy/PEMMOB-5027241102-TUGAS4.git
   ```
2. **Buka dengan Android Studio** (Ladybug / Koala atau versi lebih baru).
3. **Sinkronkan Gradle** (*Sync Project with Gradle Files*).
4. **Jalankan pada Emulator atau Perangkat Fisik** (Min SDK: 24, Target SDK: 37).