package com.hyunki.aryoulearning2.network.main;

import com.hyunki.aryoulearning2.model.ModelResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MainApi {

    @GET("kelveenfabian/75380ae0e467f513762454bbe49a6c2e/raw/7c745364e690cb292ee13eae0a157df6323e3a19/category.json")
    Flowable<List<ModelResponse>> getModels();
}
