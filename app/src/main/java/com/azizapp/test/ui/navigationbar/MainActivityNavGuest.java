package com.azizapp.test.ui.navigationbar;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azizapp.test.R;
import com.azizapp.test.ui.laporan.LaporanFragment;
import com.azizapp.test.ui.map.HomeFragment;
import com.azizapp.test.ui.navigationbar.MainActivityNav;
import com.azizapp.test.ui.profile.ProfileFragment;
import com.azizapp.test.ui.riwayat.RiwayatFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivityNavGuest extends AppCompatActivity {
    private static final String TAG = MainActivityNav.class.getSimpleName();
    private ChipNavigationBar mMainNav;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private RiwayatFragment riwayatFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guest);

        mMainNav = findViewById(R.id.main_nav_guest);

        if (savedInstanceState == null) {
            mMainNav.setItemSelected(R.id.nav_home_guest, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_guest, homeFragment)
                    .commit();
        }

        mMainNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.nav_new_guest:
                        fragment = new LaporanFragment("anonim");
                        break;
                    case R.id.nav_home_guest:
                        fragment = new HomeFragment();
                        break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame_guest, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_guest, fragment);
        fragmentTransaction.commit();
    }
}
