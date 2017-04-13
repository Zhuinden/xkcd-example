package com.zhuinden.xkcdexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhuinden.xkcdexample.redux.ReduxStore;

import io.reactivex.disposables.Disposable;

/**
 * Created by Zhuinden on 2017.04.14..
 */

public class MainScopeListener
        extends Fragment {
    public MainScopeListener() {
        setRetainInstance(true);
    }

    Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReduxStore reduxStore = MainActivity.getStore(getContext());
        disposable = reduxStore.state().subscribe();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        disposable = null;
        super.onDestroy();
    }
}
