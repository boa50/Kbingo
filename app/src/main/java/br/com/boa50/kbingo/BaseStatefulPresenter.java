package br.com.boa50.kbingo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface BaseStatefulPresenter<V extends BaseView, S extends BaseState> extends BasePresenter<V> {
    void subscribe(@NonNull V view, @Nullable S state);
    @NonNull S getState();
}
