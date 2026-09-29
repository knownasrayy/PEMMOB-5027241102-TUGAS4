package com.example.diaryapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.diaryapp.R;
import com.example.diaryapp.data.Mahasiswa;
import com.example.diaryapp.data.MahasiswaRepository;

public class MahasiswaDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MHS_ID = "mhs_id";

    private TextView tvAvatar;
    private TextView tvNama;
    private TextView tvJurusanHeader;
    private TextView tvNim;
    private TextView tvJurusan;
    private TextView tvAngkatan;
    private TextView tvTimestamp;

    private MahasiswaRepository repository;
    private long mhsId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvAvatar        = findViewById(R.id.tv_detail_avatar);
        tvNama          = findViewById(R.id.tv_nama);
        tvJurusanHeader = findViewById(R.id.tv_jurusan_header);
        tvNim           = findViewById(R.id.tv_nim);
        tvJurusan       = findViewById(R.id.tv_jurusan);
        tvAngkatan      = findViewById(R.id.tv_angkatan);
        tvTimestamp     = findViewById(R.id.tv_timestamp);

        mhsId = getIntent().getLongExtra(EXTRA_MHS_ID, -1L);
        if (mhsId == -1L) {
            Toast.makeText(this, "ID mahasiswa tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repository = new MahasiswaRepository(this);

        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            Intent intent = new Intent(this, MahasiswaFormActivity.class);
            intent.putExtra(MahasiswaFormActivity.EXTRA_MHS_ID, mhsId);
            startActivity(intent);
        });

        findViewById(R.id.btn_delete).setOnClickListener(v -> showDeleteConfirmation());
    }

    @Override
    protected void onStart() {
        super.onStart();
        repository.open();
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        repository.close();
    }

    private void loadData() {
        Mahasiswa m = repository.getById(mhsId);
        if (m != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(m.getNama());
            }
            tvNama.setText(m.getNama());
            tvJurusanHeader.setText(m.getJurusan() + " \u2022 Angkatan " + m.getAngkatan());
            tvNim.setText(m.getNim());
            tvJurusan.setText(m.getJurusan());
            tvAngkatan.setText(String.valueOf(m.getAngkatan()));
            tvTimestamp.setText(m.getTimestamp());

            if (m.getNama() != null && !m.getNama().trim().isEmpty()) {
                tvAvatar.setText(m.getNama().trim().substring(0, 1).toUpperCase());
            } else {
                tvAvatar.setText("M");
            }
        } else {
            Toast.makeText(this, "Data mahasiswa telah dihapus", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                    int rows = repository.delete(mhsId);
                    if (rows > 0) {
                        Toast.makeText(this, R.string.toast_delete_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.toast_delete_failed, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> dialog.dismiss())
                .show();
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