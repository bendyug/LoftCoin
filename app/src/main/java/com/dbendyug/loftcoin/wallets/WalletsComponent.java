package com.dbendyug.loftcoin.wallets;

import androidx.fragment.app.Fragment;

import com.dbendyug.loftcoin.util.UtilModule;
import com.dbendyug.loftcoin.viewmodel.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {
        WalletsModule.class,
        ViewModelModule.class,
        UtilModule.class
})
interface WalletsComponent {

    void inject(WalletsFragment fragment);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder fragment(Fragment walletsFragment);

        WalletsComponent build();
    }
}
