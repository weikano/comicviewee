package com.wkswind.comicviewer.rxext;

import android.support.annotation.NonNull;

import com.wkswind.comicviewer.adapter.PageIndicatorAdapter;

import io.reactivex.Observable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2017/5/24.
 */

public final class RxPageIndicatorView {
    public static Observable<Integer> rxPageChanges(@NonNull PageIndicatorAdapter adapter) {
        checkNotNull(adapter, "view == null");
        return new PageIndicatorAdapterPageChangeObservable(adapter);
    }
}
