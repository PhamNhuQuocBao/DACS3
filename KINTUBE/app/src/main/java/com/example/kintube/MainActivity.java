package com.example.kintube;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.kintube.DataLocal.DataLocalManager;
import com.example.kintube.Database.VideoDatabase;
import com.example.kintube.Fragments.TaoFragment;
import com.example.kintube.Fragments.ThuvienFragment;
import com.example.kintube.Fragments.TrangchuFragment;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.Model.Video.VideoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrangchuFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutoolbar, menu);

        videoList = new ArrayList<>();
        videoList = VideoDatabase.getInstance(MainActivity.this).videoDAO().getListVideo();

        videoAdapter = new VideoAdapter(videoList, MainActivity.this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        return true;
    }

    private void filterList(String newText) {
        List<Video> filteredList = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(video);
            }
        }

        videoAdapter.setFilteredList(filteredList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //lấy id của các menu item
        int id = item.getItemId();
        switch (id) {
            case R.id.login:
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.Register:
                Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.Profile:
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.trangchu) {
            selectedFragment = new TrangchuFragment();
        } else if (itemId == R.id.tao) {
            selectedFragment = new TaoFragment();
        } else if (itemId == R.id.thuvien) {
            selectedFragment = new ThuvienFragment();
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();

    }
}
