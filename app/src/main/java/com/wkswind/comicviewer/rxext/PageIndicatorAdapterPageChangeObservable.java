package com.wkswind.comicviewer.rxext;

import com.wkswind.comicviewer.adapter.PageIndicatorAdapter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

/**
 * Created by Administrator on 2017/5/24.
 */

public class PageIndicatorAdapterPageChangeObservable extends Observable<Integer> {
    private PageIndicatorAdapter adapter;
    public PageIndicatorAdapterPageChangeObservable(PageIndicatorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(adapter, observer);
        observer.onSubscribe(listener);
        adapter.setOnPageChangeListener(listener.onPageChangeListener);
    }

    final class Listener extends MainThreadDisposable {
        private final PageIndicatorAdapter adapter;
        private final PageIndicatorAdapter.OnPageChangeListener onPageChangeListener;

        Listener(PageIndicatorAdapter adapter, final Observer<? super Integer> observer) {
            this.adapter = adapter;
            this.onPageChangeListener = new PageIndicatorAdapter.OnPageChangeListener() {
                @Override
                public void onPageChange(int page) {
                    if(!isDisposed()){
                        observer.onNext(page);
                    }
                }
            };
        }

        @Override protected void onDispose() {
            adapter.setOnPageChangeListener(null);
        }
    }
}
