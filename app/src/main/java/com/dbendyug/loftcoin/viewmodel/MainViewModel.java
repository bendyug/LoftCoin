package com.dbendyug.loftcoin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.R;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedId = new MutableLiveData<>();

    public MainViewModel() {
        selectedId.postValue(R.id.wallets);
    }


    public void submitTitle(String title) {
        this.title.postValue(title);
    }

    public void submitSelectedId(Integer selectedId) {
        this.selectedId.postValue(selectedId);
    }


    public LiveData<String> title() {
        return title;
    }

    public LiveData<Integer> selectedId() {
        return selectedId;
    }

}
