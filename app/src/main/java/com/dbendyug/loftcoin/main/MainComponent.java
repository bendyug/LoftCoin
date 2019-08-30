package com.dbendyug.loftcoin.main;

import androidx.fragment.app.FragmentActivity;

import com.dbendyug.loftcoin.viewmodel.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component (modules = {
        MainModule.class,
        ViewModelModule.class
})
interface MainComponent {

    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder activity(FragmentActivity fragmentActivity);

        MainComponent build();
    }

}
