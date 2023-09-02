package com.example.sensordaten;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAdapterJ extends FragmentStateAdapter {
    public ViewAdapterJ(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HealthFragment();

            case 1:
                return new GraphFragment();

            case 2:
                return new SettingsFragment();

            default:
               return new HealthFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
