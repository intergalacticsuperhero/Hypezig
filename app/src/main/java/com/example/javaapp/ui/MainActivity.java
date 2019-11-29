package com.example.javaapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.javaapp.R;
import com.example.javaapp.ui.FavoritesFragment;
import com.example.javaapp.ui.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.javaapp.BaseApplication.LOG_UI;


public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment = new HomeFragment();
    private Fragment favoritesFragment = new FavoritesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_UI, getClass().getName() + ".onCreate() called with: savedInstanceState = ["
                + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                homeFragment).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Log.i(LOG_UI, "Option '" + menuItem.toString() + "' selected");

                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = favoritesFragment;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onAttachFragment(Fragment fragment){
        Log.i(LOG_UI, "Fragment " + fragment.getClass().getSimpleName() + " attached to "
                + this.getClass().getSimpleName()  );
    }
}
