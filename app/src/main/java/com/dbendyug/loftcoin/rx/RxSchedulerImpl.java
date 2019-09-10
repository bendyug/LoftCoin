package com.dbendyug.loftcoin.rx;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class RxSchedulerImpl implements RxScheduler {

    @Inject
    RxSchedulerImpl() {

    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }
}
