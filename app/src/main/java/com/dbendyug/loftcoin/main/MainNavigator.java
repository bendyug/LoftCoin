package com.dbendyug.loftcoin.main;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.util.Supplier;

import javax.inject.Inject;

class MainNavigator {

    private FragmentActivity fragmentActivity;
    private SparseArrayCompat<Supplier<Fragment>> fragments;

    @Inject
    MainNavigator(FragmentActivity fragmentActivity,
                  SparseArrayCompat<Supplier<Fragment>> fragments) {

        this.fragmentActivity = fragmentActivity;
        this.fragments = fragments;
    }

    void replaceFragment(int id){
        Supplier<Fragment> factory = fragments.get(id);
        if (factory != null){
            fragmentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, factory.get()).commit();
        }
    }
}
