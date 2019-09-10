package com.dbendyug.loftcoin.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

class ViewModelFactory implements ViewModelProvider.Factory {

    private Map<Class<?>, Provider<ViewModel>> providerMap;

    @Inject
    ViewModelFactory(Map<Class<?>, Provider<ViewModel>> providerMap) {
        this.providerMap = providerMap;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<ViewModel> provider = providerMap.get(modelClass);
        if (provider != null) {
            return (T) provider.get();
        }
        throw new IllegalArgumentException("No provider for " + modelClass);
    }
}
