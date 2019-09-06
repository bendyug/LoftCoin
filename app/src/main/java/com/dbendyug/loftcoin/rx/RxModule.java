package com.dbendyug.loftcoin.rx;

import dagger.Binds;
import dagger.Module;

@Module
public interface RxModule {

    @Binds
    RxScheduler rxScheduler(RxSchedulerImpl rxSchedulerImpl);
}
