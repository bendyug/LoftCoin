package com.dbendyug.loftcoin.wallets;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.main.MainViewModel;
import com.dbendyug.loftcoin.util.WalletsDecorator;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class WalletsFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;
    private MainViewModel mainViewModel;
    private WalletsViewModel walletsViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView wallets;
    private RecyclerView walletsTransactions;
    private SnapHelper snapHelper;
    @Inject
    WalletsAdapter walletsAdapter;
    @Inject
    TransactionAdapter transactionAdapter;
    private RecyclerView.OnScrollListener onWalletsScroll;
    private static Bundle bundleRecyclerViewState;
    private Parcelable listState = null;
    private static final String KEY_RECYCLER_STATE = "recycler_state";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerWalletsComponent.builder().fragment(this).build().inject(this);
        mainViewModel = ViewModelProviders.of(requireActivity(), viewModelProviderFactory).get(MainViewModel.class);
        walletsViewModel = ViewModelProviders.of(requireActivity(), viewModelProviderFactory).get(WalletsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel.submitTitle(getString(R.string.wallets));

        wallets = view.findViewById(R.id.wallets);
        wallets.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        wallets.addItemDecoration(new WalletsDecorator(view.getContext(), 10));
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(wallets);
        wallets.swapAdapter(walletsAdapter, false);

        View walletCard = view.findViewById(R.id.wallet_card);
        walletCard.setOnClickListener(view1 -> {
            createWallet();
        });
        compositeDisposable.add(walletsViewModel.wallets().subscribe(wallets -> {
            if (wallets.isEmpty()) {
                walletCard.setVisibility(View.VISIBLE);
            } else {
                walletCard.setVisibility(View.GONE);
            }
            walletsAdapter.submitList(wallets);
        }));

        onWalletsScroll = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    View snapView = snapHelper.findSnapView((recyclerView.getLayoutManager()));
                    if (snapView != null) {
                        walletsViewModel.submitWalletPosition(recyclerView.getChildAdapterPosition(snapView));
                    }
                }
            }
        };
        wallets.addOnScrollListener(onWalletsScroll);

        walletsTransactions = view.findViewById(R.id.wallets_transactions);
        walletsTransactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        walletsTransactions.setHasFixedSize(true);
        walletsTransactions.swapAdapter(transactionAdapter, false);
        compositeDisposable.add(walletsViewModel.transactions().subscribe(list -> transactionAdapter.submitList(list)));

        compositeDisposable.add(walletsViewModel.createTransaction().subscribe());
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRecyclerViewState = new Bundle();
        listState = Objects.requireNonNull(wallets.getLayoutManager()).onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleRecyclerViewState != null) {
            new Handler().postDelayed(() -> {
                listState = bundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                Objects.requireNonNull(wallets.getLayoutManager()).onRestoreInstanceState(listState);
            }, 50);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_wallet, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add_wallet == item.getItemId()) {
            createWallet();
        }
        return super.onOptionsItemSelected(item);
    }


    public void createWallet() {
        compositeDisposable.add(walletsViewModel.createWallet().subscribe(
                () -> Toast.makeText(requireContext(), R.string.wallet_created, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show()
        ));
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
        snapHelper.attachToRecyclerView(null);
        wallets.removeOnScrollListener(onWalletsScroll);
        wallets.swapAdapter(null, false);
        walletsTransactions.swapAdapter(null, false);
        super.onDestroyView();
    }
}
