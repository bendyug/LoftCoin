package com.dbendyug.loftcoin.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.rx.RxRecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ConverterCoinsDialog extends BottomSheetDialogFragment {

    private static final String KEY_MODE = "key_mode";

    private static final int MODE_FROM = 1;

    private static final int MODE_TO = 2;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ConverterCoinsAdapter converterCoinsAdapter;

    private ConverterViewModel converterViewModel;

    private RecyclerView recyclerView;

    private int mode = MODE_FROM;

    static void chooseFromCoin(FragmentManager fragmentManager) {
        ConverterCoinsDialog dialog = new ConverterCoinsDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, MODE_FROM);
        dialog.setArguments(arguments);
        dialog.show(fragmentManager, ConverterCoinsDialog.class.getSimpleName());
    }

    static void chooseToCoin(FragmentManager fragmentManager) {
        ConverterCoinsDialog dialog = new ConverterCoinsDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, MODE_TO);
        dialog.setArguments(arguments);
        dialog.show(fragmentManager, ConverterCoinsDialog.class.getSimpleName());
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_BottomSheet_Dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerConverterComponent.builder()
                .fragment(requireParentFragment())
                .build()
                .inject(this);

        converterViewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory)
                .get(ConverterViewModel.class);

        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_currency_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.swapAdapter(converterCoinsAdapter, false);

        compositeDisposable.add(converterViewModel.topCoins().subscribe(converterCoinsAdapter::submitList));
        compositeDisposable.add(RxRecyclerView.onItemClick(recyclerView)
                .map(recyclerView::getChildAdapterPosition)
                .doOnNext(position -> {
                    if (MODE_FROM == mode) {
                        converterViewModel.changeFromCoin(position);
                    } else {
                        converterViewModel.changeToCoin(position);
                    }
                })
                .doOnNext(position -> dismissAllowingStateLoss())
                .subscribe());
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
        recyclerView.swapAdapter(null, false);
        super.onDestroyView();
    }
}
