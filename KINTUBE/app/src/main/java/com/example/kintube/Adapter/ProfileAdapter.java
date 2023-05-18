package com.example.kintube.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kintube.Fragments.ProfileFrament.TabVideoFragment;


public class ProfileAdapter extends FragmentStateAdapter {
    public ProfileAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TabVideoFragment();
            default:
                return new TabVideoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

