package com.azizapp.test.ui.navigationbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azizapp.test.R;
import com.azizapp.test.ui.laporan.LaporanFragment;
import com.azizapp.test.ui.map.HomeFragment;
import com.azizapp.test.ui.profile.ProfileFragment;
import com.azizapp.test.ui.riwayat.RiwayatFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivityNav extends AppCompatActivity {
    private static final String TAG = MainActivityNav.class.getSimpleName();
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private RiwayatFragment riwayatFragment;
    private FragmentManager fragmentManager;
    private EditText editText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChipNavigationBar mMainNav = findViewById(R.id.main_nav);

        if (savedInstanceState == null) {
            mMainNav.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame, homeFragment)
                    .commit();
        }

        mMainNav.setOnItemSelectedListener(id -> {
            Fragment fragment = null;
            switch (id) {
                case R.id.nav_new:
                    fragment = new LaporanFragment("login");
                    break;
                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_history:
                    fragment = new RiwayatFragment();
                    break;
                case R.id.nav_profile:
                    fragment = new ProfileFragment();
                    break;
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            } else {
                Log.e(TAG, "Error in creating fragment");
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
