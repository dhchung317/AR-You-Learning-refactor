package com.capstone.aryoulearning.network.main;

import com.capstone.aryoulearning.model.ModelResponse;

import org.checkerframework.common.reflection.qual.GetClass;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface MainApi {

    @GET("kelveenfabian/75380ae0e467f513762454bbe49a6c2e/raw/7c745364e690cb292ee13eae0a157df6323e3a19/category.json")
    Flowable<List<ModelResponse>> getModels();
}
