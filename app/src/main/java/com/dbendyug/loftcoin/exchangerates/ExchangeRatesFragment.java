package com.dbendyug.loftcoin.exchangerates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.main.MainViewModel;

import javax.inject.Inject;


public class ExchangeRatesFragment extends Fragment {

    private MainViewModel mainViewModel;
    private ExchangeRatesViewModel exchangeRatesViewModel;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject ViewModelProvider.Factory viewModelProviderFactory;

    @Inject ExchangeRatesAdapter exchangeRatesAdapter;

    @Inject
    CurrenciesReposytory currenciesReposytory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerExchangeRatesComponent
                .builder()
                .fragment(this)
                .build()
                .inject(this);

        mainViewModel = ViewModelProviders.of(requireActivity(), viewModelProviderFactory)
                .get(MainViewModel.class);
        exchangeRatesViewModel = ViewModelProviders.of(this, viewModelProviderFactory)
                .get(ExchangeRatesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.submitTitle(getString(R.string.exchange_rates_tab_name));


        recyclerView = view.findViewById(R.id.recyclerview_rates);
        recyclerView.swapAdapter(exchangeRatesAdapter, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        swipeRefreshLayout = view.findViewById(R.id.refresher);
        swipeRefreshLayout.setOnRefreshListener(() -> exchangeRatesViewModel.refresh());

        exchangeRatesViewModel.isRefreshing().observe(this,
                isRefreshing -> swipeRefreshLayout.setRefreshing(isRefreshing));
        exchangeRatesViewModel.error().observe(this,
                error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
        exchangeRatesViewModel.coinData().observe(this,
                coinEntities -> exchangeRatesAdapter.submitList(coinEntities));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_exchnge_rates, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.menu_change_currency == item.getItemId()){
            CurrencyDialog dialog = new CurrencyDialog(currenciesReposytory);
            dialog.show(getChildFragmentManager(), CurrencyDialog.TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.swapAdapter(null, false);
    }
}
