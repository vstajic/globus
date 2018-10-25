package com.busglo.globus;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.busglo.globus.db.GlobusDatabase;
import com.busglo.globus.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    public static GlobusDatabase globusDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        globusDatabase = Room
                .databaseBuilder(getApplicationContext(), GlobusDatabase.class, GlobusDatabase.DB_NAME)
                .build();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            fragmentManager.beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();
        }

    }

}
