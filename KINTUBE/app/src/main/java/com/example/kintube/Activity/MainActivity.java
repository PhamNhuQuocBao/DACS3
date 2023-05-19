package com.example.kintube.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kintube.DataLocal.DataLocalManager;
import com.example.kintube.Fragments.TaoFragment;
import com.example.kintube.Fragments.ThuvienFragment;
import com.example.kintube.Fragments.TrangchuFragment;
import com.example.kintube.Model.Video.Video;
import com.example.kintube.Adapter.VideoAdapter;
import com.example.kintube.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private List<Video> filteredList;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrangchuFragment()).commit();

        System.out.println("my id: " + DataLocalManager.getIdAccountLogin());
        System.out.println("my email: " + DataLocalManager.getEmailAccountLogin());
        System.out.println("my name: " + DataLocalManager.getNameAccountLogin());
        System.out.println("my image: " + DataLocalManager.getImageAccountLogin());

        videoList = new ArrayList<>();
        DatabaseReference mDatabse = FirebaseDatabase.getInstance().getReference();
        mDatabse.child("VIDEOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Video video = itemSnapshot.getValue(Video.class);
                    videoList.add(video);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutoolbar, menu);

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
        filteredList = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(video);
            }
        }
        VideoAdapter videoAdapter = new VideoAdapter(filteredList, MainActivity.this);
        videoAdapter.setFilteredList(filteredList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(videoAdapter);
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
                if (DataLocalManager.getEmailAccountLogin() != "") {
                    Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intentProfile);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
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
