package com.example.diaryapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diaryapp.R;
import com.example.diaryapp.data.Mahasiswa;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MhsViewHolder> {

    public interface OnMahasiswaClickListener {
        void onMahasiswaClick(Mahasiswa mhs);
    }

    private List<Mahasiswa> list;
    private final OnMahasiswaClickListener listener;

    public MahasiswaAdapter(List<Mahasiswa> list, OnMahasiswaClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MhsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mahasiswa, parent, false);
        return new MhsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MhsViewHolder holder, int position) {
        Mahasiswa m = list.get(position);
        holder.bind(m, listener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateList(List<Mahasiswa> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    static class MhsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAvatarInitial;
        private final TextView tvNama;
        private final TextView tvNim;
        private final TextView tvJurusan;
        private final TextView tvAngkatan;

        MhsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatarInitial = itemView.findViewById(R.id.tv_avatar_initial);
            tvNama          = itemView.findViewById(R.id.tv_mhs_nama);
            tvNim           = itemView.findViewById(R.id.tv_mhs_nim);
            tvJurusan       = itemView.findViewById(R.id.tv_mhs_jurusan);
            tvAngkatan      = itemView.findViewById(R.id.tv_mhs_angkatan);
        }

        void bind(final Mahasiswa m, final OnMahasiswaClickListener listener) {
            tvNama.setText(m.getNama());
            tvNim.setText(m.getNim());
            tvJurusan.setText(m.getJurusan());
            tvAngkatan.setText("Angkatan " + m.getAngkatan());

            // Initial Avatar (e.g. "R" for Rayhan)
            if (m.getNama() != null && !m.getNama().trim().isEmpty()) {
                tvAvatarInitial.setText(m.getNama().trim().substring(0, 1).toUpperCase());
            } else {
                tvAvatarInitial.setText("M");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMahasiswaClick(m);
                }
            });
        }
    }
}