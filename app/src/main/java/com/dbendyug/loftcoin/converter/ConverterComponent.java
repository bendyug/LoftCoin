package com.dbendyug.loftcoin.converter;

import androidx.fragment.app.Fragment;

import com.dbendyug.loftcoin.util.UtilModule;
import com.dbendyug.loftcoin.viewmodel.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {
        ConverterModule.class,
        ViewModelModule.class,
        UtilModule.class
})
public interface ConverterComponent {

    void inject(ConverterFragment converterFragment);

    void inject(ConverterCoinsDialog converterCoinsDialog);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder fragment(Fragment fragment);

        ConverterComponent build();
    }
}
