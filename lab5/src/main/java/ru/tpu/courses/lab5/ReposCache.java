package ru.tpu.courses.lab5;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class ReposCache {
    private static ReposCache instance;

    static ReposCache getInstance() {
        if (instance == null) {
            synchronized (ReposCache.class) {
                if (instance == null) {
                    instance = new ReposCache();
                }
            }
        }
        return instance;
    }

    private List<Repo> repos = new ArrayList<>();
    private int pageCount;
    private String searchWord;

    private ReposCache() {
    }

    @NonNull
    List<Repo> getRepos() {
        return new ArrayList<>(repos);
    }

    @NonNull
    String getSearchWord() {
        return searchWord;
    }

    void setSearchWord(String pc) {
        searchWord = pc;
    }

    int getPageCount() {
        return pageCount;
    }

    void setPageCount(int pc) {
        pageCount = pc;
    }

    void clear() {
        repos.clear();
    }

    void addRepo(@NonNull Repo repo) {
        repos.add(repo);
    }
}
