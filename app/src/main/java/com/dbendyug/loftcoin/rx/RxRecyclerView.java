package com.dbendyug.loftcoin.rx;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.MainThreadDisposable;

public class RxRecyclerView {

    public static Observable<View> onItemClick(RecyclerView recyclerView) {
        return Observable.create(emitter -> {
            MainThreadDisposable.verifyMainThread();
            RecyclerView.OnItemTouchListener listener = new ItemClickHelper(recyclerView.getContext(), emitter);
            emitter.setCancellable(() -> recyclerView.removeOnItemTouchListener(listener));
            recyclerView.addOnItemTouchListener(listener);
        });
    }

    private static class ItemClickHelper implements RecyclerView.OnItemTouchListener {

        private GestureDetectorCompat mGestureDetector;

        private ObservableEmitter<View> emitter;

        ItemClickHelper(Context context, ObservableEmitter<View> emitter) {
            mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent event) {
                    return true;
                }
            });
            this.emitter = emitter;
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null && mGestureDetector.onTouchEvent(event) && !emitter.isDisposed()) {
                emitter.onNext(child);
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
}
