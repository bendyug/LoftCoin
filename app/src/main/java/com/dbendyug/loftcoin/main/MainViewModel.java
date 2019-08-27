package com.dbendyug.loftcoin.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.R;

import java.util.Objects;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedId = new MutableLiveData<>();

    public MainViewModel() {
        selectedId.postValue(R.id.wallets);
    }


    public void submitTitle(String title) {
        this.title.postValue(title);
    }

    public void submitSelectedId(int selectedId) {

        if (!Objects.equals(selectedId, this.selectedId.getValue())){

            this.selectedId.postValue(selectedId);
        }

    }

    public LiveData<String> title() {
        return title;
    }

    public LiveData<Integer> selectedId() {
        return selectedId;
    }

}
