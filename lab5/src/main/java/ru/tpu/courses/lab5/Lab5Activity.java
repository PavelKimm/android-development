package ru.tpu.courses.lab5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.tpu.courses.lab5.adapter.RepoAdapter;

public class Lab5Activity extends AppCompatActivity {

    private static final String TAG = Lab5Activity.class.getSimpleName();
    private SearchTask task;
    private SwipeRefreshLayout swipeContainer;
    private Runnable r;
    Handler handler;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }

    private ProgressBar pgBar;
    private RepoAdapter repoAdapter;
    private Button RepeatButton;
    private LinearLayout ErrorLayout;
    private TextView ErrorMsg;
    ReposCache rc = ReposCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.lab5_activity);
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
        list = findViewById(R.id.ReposList);

        pgBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        ErrorLayout = findViewById(R.id.ErrorLayout);
        ErrorMsg = findViewById(R.id.ErrorMesg);
        RepeatButton = findViewById(R.id.button);

        list.setAdapter(repoAdapter = new RepoAdapter(rc.getRepos()));

        isLoading = true;
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading) {
                    if (dy > 0) {
                        if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                                >= layoutManager.getItemCount()) {
                            Log.d(TAG, "scrolled");
                            insertionIndex = layoutManager.findFirstVisibleItemPosition();
                            rc.setPageCount(rc.getPageCount() + 1);
                            int pageCount = rc.getPageCount();
                            String currFindingString = rc.getSearchWord();
                            pgBar.setVisibility(View.VISIBLE);
                            loadRepos(currFindingString, pageCount);
                            isLoading = false;

                        }
                    }
                }
            }
        });
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            rc.setPageCount(1);
            rc.clear();
            loadRepos(rc.getSearchWord(), rc.getPageCount());
            swipeContainer.setRefreshing(false);
            insertionIndex = 0;
        });
    }

    int insertionIndex = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.unregisterObserver();
        requestThread.interrupt();
    }

    Thread requestThread = new Thread();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_repos);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (requestThread.isAlive())
                    requestThread.interrupt();
                if (newText.length() > 2) {
                    rc.clear();
                    rc.setSearchWord(newText);
                    rc.setPageCount(1);
                    Log.d(TAG, "textChanged");
                    pgBar.setVisibility(View.VISIBLE);
                    loadRepos(newText, rc.getPageCount());
                    insertionIndex = 0;
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void loadRepos(final String repoSubstring, final int page) {
        Log.d(TAG, "loadRepo");

        handler.removeCallbacks(r);
        r = () -> {
            task = new SearchTask(searchObserver, repoSubstring, page);
            requestThread = new Thread(task);
            requestThread.start();
        };
        handler.postDelayed(r, 500);
    }

    private RecyclerView list;
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {

        @Override
        public void onLoading(Task<List<Repo>> task) {
            Log.d(TAG, "onLoad");
        }

        @Override
        public void onSuccess(Task<List<Repo>> task, List<Repo> data) {
            ErrorLayout.setVisibility(View.INVISIBLE);
            requestThread.interrupt();

            Log.d(TAG, "onSuccess");

            list.setAdapter(repoAdapter = new RepoAdapter(data));

            pgBar.setVisibility(View.INVISIBLE);
            if (insertionIndex > 0) {
                int itemCount = repoAdapter.getItemCount();
                Log.d(TAG, "itemCount= " + itemCount);
                Log.d(TAG, "scrollPosition= " + insertionIndex);
                repoAdapter.notifyItemRangeInserted(itemCount, itemCount + 20);
                list.scrollToPosition(insertionIndex + 1);
            }
            isLoading = true;
        }

        @Override
        public void onError(Task<List<Repo>> task, Exception e) {
            RepeatButton.setOnClickListener(view -> {
                pgBar.setVisibility(View.VISIBLE);
                ErrorLayout.setVisibility(View.INVISIBLE);
                loadRepos(rc.getSearchWord(), rc.getPageCount());
            });

            Log.d(TAG, "onError");
            pgBar.setVisibility(View.INVISIBLE);
            ErrorLayout.setVisibility(View.VISIBLE);
            ErrorMsg.setText(e.getMessage());
        }
    };
}
