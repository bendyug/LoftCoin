package com.dbendyug.loftcoin.fcm;

import androidx.annotation.NonNull;

import com.dbendyug.loftcoin.AppComponent;
import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class FcmService extends FirebaseMessagingService {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    FcmChannel fcmChannel;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent.from(getApplicationContext()).inject(this);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        final RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            compositeDisposable.add(fcmChannel.notify(
                    Objects.toString(notification.getTitle(), getString(R.string.app_name)),
                    Objects.toString(notification.getBody(), ""),
                    MainActivity.class
            ).subscribe());
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
