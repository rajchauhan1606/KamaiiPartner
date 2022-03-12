package com.kamaii.partner.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface GorgasAPI {

    @GET("CmsService.svc/GetCms?")
    Call<ResponseBody> getaboutus(@Query("cmid") String cmid);

    @POST("getAllBookingArtist")
    Call<ResponseBody> viewproduct(@Query("artist_id") String artist_id, @Query("booking_flag") String booking_flag);


}
