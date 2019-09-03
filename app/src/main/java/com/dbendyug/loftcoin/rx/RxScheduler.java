package com.dbendyug.loftcoin.rx;

import io.reactivex.Scheduler;

public interface RxScheduler {
    Scheduler io();

    Scheduler main();
}
