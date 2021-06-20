package com.azizapp.test.ui.navigationbar;

import android.os.Bundle;

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

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivityNav extends AppCompatActivity {
    private FragmentManager fragmentManager;


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
            fragmentManager = getSupportFragmentManager();
            switch (id) {
                case R.id.nav_new:
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, new LaporanFragment("login")
                            )
                            .commit();
                    break;
                case R.id.nav_home:
                    replace_fragment(new HomeFragment());
                    break;
                case R.id.nav_history:
                    replace_fragment(new RiwayatFragment());
                    break;
                case R.id.nav_profile:
                    replace_fragment(new ProfileFragment());
                    break;
            }
        });
    }

    private void replace_fragment(@NotNull Fragment fragment) {

        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment cacheFrag = getSupportFragmentManager().findFragmentByTag(tag);

        if (curFrag != null)
            tr.hide(curFrag);

        if (cacheFrag == null) {
            tr.add(R.id.main_frame, fragment, tag);
        } else {
            tr.show(cacheFrag);
            fragment = cacheFrag;
        }

        tr.setPrimaryNavigationFragment(fragment);
        tr.commit();

    }
}
