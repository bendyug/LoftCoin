package com.dbendyug.loftcoin.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.rx.RxScheduler;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

class FcmChannelImpl implements FcmChannel {

    private Context context;

    private RxScheduler rxScheduler;

    private Executor executor;

    private NotificationManager notificationManager;

    private final static String CHANNEL_ID = "Channel id";

    @Inject
    FcmChannelImpl(Context context, RxScheduler rxScheduler) {
        this.context = context;
        this.rxScheduler = rxScheduler;
        this.executor = rxScheduler.io()::scheduleDirect;
        notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
    }

    @Override
    public Single<String> token() {
        return Single.create(emitter -> {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnSuccessListener(executor, result -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(result.getToken());
                        }
                    })
                    .addOnFailureListener(executor, emitter::tryOnError);
        });
    }

    @Override
    public Completable createDefaultChannel() {
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID,
                        context.getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_LOW
                ));
            }
        }).subscribeOn(rxScheduler.main());
    }

    @Override
    public Completable notify(String title, String message,
                              Class<?> receiver) {
        return Completable
                .fromAction(() -> {
                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setContentIntent(PendingIntent.getActivity(
                                    context,
                                    0,
                                    new Intent(context, receiver).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                                    PendingIntent.FLAG_ONE_SHOT
                            ))
                            .build();
                    notificationManager.notify(1, notification);
                })
                .startWith(createDefaultChannel())
                .subscribeOn(rxScheduler.main());
    }
}