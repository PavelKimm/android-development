package ru.tpu.courses.lab5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchTask extends Task<List<Repo>> {

    private String repoString;
    private int pageCount;
    private final ReposCache reposCache = ReposCache.getInstance();

    SearchTask(Observer<List<Repo>> observer, String repoString, int pageCount) {
        super(observer);
        this.repoString = repoString;
        this.pageCount = pageCount;
    }

    @Override
    protected List<Repo> executeInBackground() throws Exception {
        String response = search();
        return parseSearch(response);
    }

    private String search() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/search/repositories?q=" + repoString + "&page=" +
                        pageCount + "&per_page=20")
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    private List<Repo> parseSearch(String response) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        JSONArray items = responseJson.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject repoJson = items.getJSONObject(i);
            Repo repo = new Repo();
            repo.fullName = repoJson.getString("full_name");
            repo.description = repoJson.getString("description");
            reposCache.addRepo(repo);
        }
        return reposCache.getRepos();
    }
}

