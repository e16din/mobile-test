package com.e16din.goeurotest;

import com.e16din.goeurotest.model.City;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface Services {

    @GET("position/suggest/{locale}/{city}")
    Call<ArrayList<City>> getCities(@Path("locale") String locale,
                                    @Path("city") String city);

}
