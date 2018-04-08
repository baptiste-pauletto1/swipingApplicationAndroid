package com.example.p16005334.onboardapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.p16005334.onboardapplication.fragments.AchievementFragment;
import com.example.p16005334.onboardapplication.fragments.MainMenuFragment;
import com.example.p16005334.onboardapplication.fragments.MapFragment;
import com.example.p16005334.onboardapplication.fragments.ProfileFragment;
import com.example.p16005334.onboardapplication.fragments.TutorialFragment;

public class Main extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView mDrawer = (NavigationView) findViewById(R.id.navigationView);
        setupDrawerContent(mDrawer);

//        mDrawer.setCheckedItem(R.id.menu);

        //Launch the tutorial just the first time !
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();
            showTutorial();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void showTutorial() {
        Intent intent = Tutorial.makeIntent(Main.this);
        startActivity(intent);
    }

    public void selectItemDrawer(MenuItem menuItem) {
        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()){
            case (R.id.menu):
                fragmentClass = MainMenuFragment.class;
                break;
            case (R.id.profil):
                fragmentClass = ProfileFragment.class;
                break;
            case (R.id.succes):
                fragmentClass = AchievementFragment.class;
                break;
            case (R.id.carte):
                fragmentClass = MapFragment.class;
                break;
            case (R.id.tutoriel):
                //Need to find a proper way to close the fragment. Working atm, will look for it later BP
                fragmentClass = TutorialFragment.class;
                showTutorial();
                finish();
                break;
            default:
                fragmentClass = MainMenuFragment.class;
        }
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayoutContent,fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, Main.class);
    }
}
