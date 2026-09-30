package com.example.diaryapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diaryapp.R;
import com.example.diaryapp.data.Mahasiswa;
import com.example.diaryapp.data.MahasiswaRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME     = "mhs_prefs";
    private static final String PREF_SORT_KEY = "sort_order";

    private RecyclerView rvMahasiswa;
    private LinearLayout layoutEmpty;
    private MaterialCardView cardHero;
    private TextView tvHeroCounter;
    private ExtendedFloatingActionButton fabAdd;

    private MahasiswaAdapter adapter;
    private MahasiswaRepository repository;
    private SharedPreferences prefs;

    private String currentSort = "newest";
    private String currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        rvMahasiswa   = findViewById(R.id.rv_mahasiswa);
        layoutEmpty   = findViewById(R.id.layout_empty);
        cardHero      = findViewById(R.id.card_hero);
        tvHeroCounter = findViewById(R.id.tv_hero_counter);
        fabAdd        = findViewById(R.id.fab_add);

        View btnEmptyAdd = findViewById(R.id.btn_empty_add);
        if (btnEmptyAdd != null) {
            btnEmptyAdd.setOnClickListener(v ->
                startActivity(new Intent(this, MahasiswaFormActivity.class))
            );
        }

        prefs       = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        currentSort = prefs.getString(PREF_SORT_KEY, "newest");
        repository  = new MahasiswaRepository(this);

        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MahasiswaAdapter(null, mhs -> {
            Intent intent = new Intent(this, MahasiswaDetailActivity.class);
            intent.putExtra(MahasiswaDetailActivity.EXTRA_MHS_ID, mhs.getId());
            startActivity(intent);
        });
        rvMahasiswa.setAdapter(adapter);

        rvMahasiswa.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy > 0 && fabAdd.isExtended()) {
                    fabAdd.shrink();
                } else if (dy < 0 && !fabAdd.isExtended()) {
                    fabAdd.extend();
                }
            }
        });

        fabAdd.setOnClickListener(v ->
            startActivity(new Intent(this, MahasiswaFormActivity.class))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        repository.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        repository.close();
    }

    private void loadData() {
        List<Mahasiswa> list;
        if (currentSearch.isEmpty()) {
            list = repository.getAll(currentSort);
        } else {
            list = repository.search(currentSearch, currentSort);
        }

        adapter.updateList(list);

        if (list == null || list.isEmpty()) {
            rvMahasiswa.setVisibility(View.GONE);
            if (cardHero != null) cardHero.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle("0 Mahasiswa");
            }
        } else {
            rvMahasiswa.setVisibility(View.VISIBLE);
            if (cardHero != null) cardHero.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            String countText = list.size() + " Mahasiswa Terdaftar";
            if (tvHeroCounter != null) {
                tvHeroCounter.setText(countText);
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(getString(R.string.institution_name));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView sv = (SearchView) searchItem.getActionView();
            if (sv != null) {
                sv.setQueryHint("Cari nama, NRP, jurusan...");
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String q) {
                        currentSearch = q != null ? q.trim() : "";
                        loadData();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        currentSearch = text != null ? text.trim() : "";
                        loadData();
                        return true;
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_newest) {
            setSortOrder("newest");
            return true;
        } else if (id == R.id.action_sort_oldest) {
            setSortOrder("oldest");
            return true;
        } else if (id == R.id.action_sort_name_asc) {
            setSortOrder("name_asc");
            return true;
        } else if (id == R.id.action_sort_nim_asc) {
            setSortOrder("nim_asc");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortOrder(String sort) {
        currentSort = sort;
        prefs.edit().putString(PREF_SORT_KEY, sort).apply();
        loadData();
    }
}