package com.dbendyug.loftcoin.fcm;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface FcmChannel {

    Single<String> token();

    Completable createDefaultChannel();

    Completable notify(String title, String message, Class<?> receiver);
}
