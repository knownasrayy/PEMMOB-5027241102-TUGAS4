package com.example.diaryapp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.diaryapp.R;
import com.example.diaryapp.data.Mahasiswa;
import com.example.diaryapp.data.MahasiswaRepository;
import com.google.android.material.textfield.TextInputLayout;

public class MahasiswaFormActivity extends AppCompatActivity {

    public static final String EXTRA_MHS_ID = "mhs_id";

    private TextInputLayout tilNama, tilNim, tilJurusan, tilAngkatan;
    private EditText etNama, etNim, etJurusan, etAngkatan;

    private MahasiswaRepository repository;
    private long mhsId = -1L;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_form);

        Toolbar toolbar = findViewById(R.id.toolbar_form);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tilNama     = findViewById(R.id.til_nama);
        tilNim      = findViewById(R.id.til_nim);
        tilJurusan  = findViewById(R.id.til_jurusan);
        tilAngkatan = findViewById(R.id.til_angkatan);

        etNama      = findViewById(R.id.et_nama);
        etNim       = findViewById(R.id.et_nim);
        etJurusan   = findViewById(R.id.et_jurusan);
        etAngkatan  = findViewById(R.id.et_angkatan);

        repository = new MahasiswaRepository(this);

        mhsId = getIntent().getLongExtra(EXTRA_MHS_ID, -1L);
        isEditMode = (mhsId != -1L);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditMode ? R.string.title_edit_mhs : R.string.title_add_mhs);
        }

        addClearErrorWatcher(etNama, tilNama);
        addClearErrorWatcher(etNim, tilNim);
        addClearErrorWatcher(etJurusan, tilJurusan);
        addClearErrorWatcher(etAngkatan, tilAngkatan);

        findViewById(R.id.btn_save).setOnClickListener(v -> saveData());
    }

    @Override
    protected void onStart() {
        super.onStart();
        repository.open();
        if (isEditMode && etNama.getText().toString().isEmpty()) {
            loadData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        repository.close();
    }

    private void loadData() {
        Mahasiswa m = repository.getById(mhsId);
        if (m != null) {
            etNama.setText(m.getNama());
            etNim.setText(m.getNim());
            etJurusan.setText(m.getJurusan());
            etAngkatan.setText(String.valueOf(m.getAngkatan()));
        } else {
            Toast.makeText(this, "Data mahasiswa tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveData() {
        String nama     = getText(etNama);
        String nim      = getText(etNim);
        String jurusan  = getText(etJurusan);
        String angkStr  = getText(etAngkatan);

        boolean valid = true;

        if (nama.isEmpty()) {
            tilNama.setError(getString(R.string.error_nama_empty));
            valid = false;
        }

        if (nim.isEmpty()) {
            tilNim.setError(getString(R.string.error_nim_empty));
            valid = false;
        }

        if (jurusan.isEmpty()) {
            tilJurusan.setError(getString(R.string.error_jurusan_empty));
            valid = false;
        }

        if (angkStr.isEmpty()) {
            tilAngkatan.setError(getString(R.string.error_angkatan_empty));
            valid = false;
        }

        if (!valid) return;

        int angkatan;
        try {
            angkatan = Integer.parseInt(angkStr);
            if (angkatan < 1950 || angkatan > 2100) {
                tilAngkatan.setError(getString(R.string.error_angkatan_invalid));
                return;
            }
        } catch (NumberFormatException e) {
            tilAngkatan.setError(getString(R.string.error_angkatan_invalid));
            return;
        }

        if (isEditMode) {
            int rows = repository.update(mhsId, nama, nim, jurusan, angkatan);
            if (rows > 0) {
                Toast.makeText(this, R.string.toast_update_success, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.toast_update_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            long newId = repository.insert(nama, nim, jurusan, angkatan);
            if (newId > 0) {
                Toast.makeText(this, R.string.toast_save_success, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                tilNim.setError("NIM sudah terdaftar! Gunakan NIM lain.");
                Toast.makeText(this, R.string.toast_save_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getText(EditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void addClearErrorWatcher(EditText et, TextInputLayout til) {
        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                if (til.getError() != null) til.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}