package com.capstone.aryoulearning.network.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.Resource;

public class MainResource<T> {


    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public MainResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> MainResource<T> success(@Nullable T data) {
        return new MainResource<>(Status.SUCCESS, data, null);
    }

    public static <T> MainResource<T> error(@NonNull String msg, @Nullable T data) {
        return new MainResource<>(Status.ERROR, data, msg);
    }

    public static <T> MainResource<T> loading(@Nullable T data) {
        return new MainResource<>(Status.LOADING, data, null);
    }

    public enum Status {SUCCESS, ERROR, LOADING}
}

