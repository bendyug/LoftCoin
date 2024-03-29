package com.dbendyug.loftcoin.exchangerates;

import android.app.Dialog;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.Currency;

import java.util.List;

import javax.inject.Inject;

public class CurrencyDialog extends DialogFragment {
    private TextView dialogTitle;

    private TextView currencyUsdSymbol;
    private TextView currencyEurSymbol;
    private TextView currencyRubSymbol;

    private TextView currencyUsd;
    private TextView currencyEur;
    private TextView currencyRub;

    private LinearLayout itemUsd;
    private LinearLayout itemEur;
    private LinearLayout itemRub;

    private AppCompatDialog dialog;

    private List<Currency> currencies;

    @Inject
    CurrenciesReposytory currenciesReposytory;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    @Inject
    CurrencyDialog(CurrenciesReposytory currenciesReposytory) {
        this.currencies = currenciesReposytory.getAvailableCurrencies();
    }

    public static final String TAG = "CURRENCY_CHANGE_TAG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerExchangeRatesComponent.builder()
                .fragment(requireParentFragment())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_change_currency, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new AppCompatDialog(requireContext());
        dialog.setTitle(R.string.change_currency);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialogTitle = getView().findViewById(R.id.dialog_title);
        dialogTitle.setText(R.string.change_currency);

        currencyUsd = getView().findViewById(R.id.currency_name_usd);
        currencyUsd.setText(R.string.dollar);
        currencyEur = getView().findViewById(R.id.currency_name_eur);
        currencyEur.setText(R.string.euro);
        currencyRub = getView().findViewById(R.id.currency_name_rub);
        currencyRub.setText(R.string.rouble);

        currencyUsdSymbol = getView().findViewById(R.id.symbol_usd);
        currencyUsdSymbol.setText(R.string.dollar_symbol);
        currencyEurSymbol = getView().findViewById(R.id.symbol_eur);
        currencyEurSymbol.setText(R.string.euro_symbol);
        currencyRubSymbol = getView().findViewById(R.id.symbol_rub);
        currencyRubSymbol.setText(R.string.rouble_symbol);

        itemUsd = getView().findViewById(R.id.item_usd);
        itemEur = getView().findViewById(R.id.item_eur);
        itemRub = getView().findViewById(R.id.item_rub);

        itemUsd.setOnClickListener(view -> {
            currenciesReposytory.setCurrentCurrency(currencies.get(0));
            dialog.dismiss();
        });
        itemEur.setOnClickListener(view -> {
            currenciesReposytory.setCurrentCurrency(currencies.get(1));
            dialog.dismiss();
        });
        itemRub.setOnClickListener(view -> {
            currenciesReposytory.setCurrentCurrency(currencies.get(2));
            dialog.dismiss();
        });

        roundIcon(currencyUsdSymbol);
        roundIcon(currencyEurSymbol);
        roundIcon(currencyRubSymbol);
    }

    public void roundIcon(View view) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2);
            }
        });
        view.setClipToOutline(true);
    }

}
