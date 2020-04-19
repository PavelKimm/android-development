package ru.tpu.courses.lab5.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.courses.lab5.Repo;


public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Repo> reposStringsFiltered;

    public RepoAdapter(List<Repo> repos) {

        reposStringsFiltered = new ArrayList<>();
        reposStringsFiltered.addAll(repos);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoHolder(parent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RepoHolder repoHolder = (RepoHolder) holder;
        Repo repo = reposStringsFiltered.get(position);
        repoHolder.repo.setText((position+1) + " " + repo.fullName);
        repoHolder.description.setText(repo.description.equals("null") ? "No description" : repo.description);
    }

    @Override
    public int getItemCount() {
        return reposStringsFiltered.size();
    }
}
