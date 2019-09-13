package com.dbendyug.loftcoin.converter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.main.MainViewModel;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ConverterFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    ImageURLFormatter imageURLFormatter;

    private MainViewModel mainViewModel;
    private ConverterViewModel converterViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerConverterComponent.builder().fragment(this).build().inject(this);
        mainViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainViewModel.class);
        converterViewModel = ViewModelProviders.of(this, viewModelFactory).get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.submitTitle(getString(R.string.converter));

        TextView fromCoin = view.findViewById(R.id.from_coin);
        TextView toCoin = view.findViewById(R.id.to_coin);
        ImageView fromImage = view.findViewById(R.id.from_image);
        ImageView toImage = view.findViewById(R.id.to_image);

        compositeDisposable.add(converterViewModel.fromCoin().subscribe(coin -> {
                    fromCoin.setText(coin.symbol());
                    Picasso.get().load(imageURLFormatter.format(coin.id())).into(fromImage);
                }
        ));

        compositeDisposable.add(converterViewModel.toCoin().subscribe(coin -> {
                    toCoin.setText(coin.symbol());
                    Picasso.get().load(imageURLFormatter.format(coin.id())).into(toImage);
                }
        ));

        compositeDisposable.add(RxView.clicks(fromCoin).subscribe(none ->
                ConverterCoinsDialog.chooseFromCoin(getChildFragmentManager())
        ));

        compositeDisposable.add(RxView.clicks(toCoin).subscribe(none ->
                ConverterCoinsDialog.chooseToCoin(getChildFragmentManager())
        ));

        EditText from = view.findViewById(R.id.from_currency);
        EditText to = view.findViewById(R.id.to_currency);

        compositeDisposable.add(RxTextView.textChanges(from)
                .map(CharSequence::toString)
                .subscribe(converterViewModel::changeFromValue));

        compositeDisposable.add(RxTextView.textChanges(to)
                .map(CharSequence::toString)
                .subscribe(converterViewModel::changeToValue));

        compositeDisposable.add(converterViewModel.fromValue()
                .filter(value -> !from.hasFocus())
                .subscribe(from::setText));

        compositeDisposable.add(converterViewModel.toValue()
                .filter(value -> !to.hasFocus())
                .subscribe(to::setText));

        LinearLayout fromItem = view.findViewById(R.id.from_item);
        LinearLayout toItem = view.findViewById(R.id.to_item);


        from.setOnTouchListener((view2, event) -> {
            fromItem.setBackgroundColor(getResources().getColor(R.color.dark_four));
            toItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            return false;
        });

        to.setOnTouchListener((view2, event) -> {
            toItem.setBackgroundColor(getResources().getColor(R.color.dark_four));
            fromItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            return false;
        });

    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
        super.onDestroyView();
    }
}
