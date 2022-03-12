package com.kamaii.partner.interfacess;


import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface apiRest {
    @FormUrlEncoded
    @POST("server_execution2")
    Call<ResponseBody> server_execution2(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("check_approve_new_flow2")
    Call<ResponseBody> checkApprove(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("SignUp")
    Call<ResponseBody> SignUp(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("token") String token);

    @FormUrlEncoded
    @POST("getMapCategory")
    Call<ResponseBody> getMapCategory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getallbookingartistcab")
    Call<ResponseBody> getAllbooking(@Field("artist_id") String user_id,@Field("kono_data") String kono_data,@Field("version") String version);

  /*  @FormUrlEncoded
    @POST("getallbookingfromid")
    Call<ResponseBody> getallbookingfromid(@Field("artist_id") String user_id,@Field("order_ids") String id);
*/ @FormUrlEncoded
    @POST("getallbookingfromid23")
    Call<ResponseBody> getallbookingfromid(@Field("artist_id") String user_id,@Field("order_ids") String id);

    @FormUrlEncoded
    @POST("getallbookingfromstartpreprationid")
    Call<ResponseBody> getallbookingfromstartpreprationid(@Field("artist_id") String user_id,@Field("order_ids") String id);
  @FormUrlEncoded
    @POST("cancel_rider_all_rider_request")
    Call<ResponseBody> cancel_rider_all_rider_request(@Field("artist_id") String user_id,@Field("order_ids") String id);

    @FormUrlEncoded
    @POST("getallbookingfromrequestforriderid")
    Call<ResponseBody> getallbookingfromrequestforriderid(@Field("artist_id") String user_id,@Field("order_ids") String id);

    @FormUrlEncoded
    @POST("getallbookingartist")
    Call<ResponseBody> getallbookingartist(@Field("artist_id") String user_id,@Field("offset") String id);

    @FormUrlEncoded
    @POST("packageing_order_summary")
    Call<ResponseBody> packageing_order_summary(@Field("artist_id") String user_id, @Field("offset") String id);

    @FormUrlEncoded
    @POST("getallbookingfromclick")
    Call<ResponseBody> getLunchDinnerOrder(@Field("artist_id") String user_id,@Field("lunch_or_dinner") String lunch_or_dinner,@Field("day") String day);

    @FormUrlEncoded
    @POST("getallbookingartistdateclick")
    Call<ResponseBody> getallbookingartistdateclick(@Field("artist_id") String user_id,@Field("day") String day,@Field("kono_data") String kono_data);

    @FormUrlEncoded
    @POST("checksignin")
    Call<ResponseBody> checksignin(@Field("mobile") String mobile, @Field("role") String role);

    @FormUrlEncoded
    @POST("getAllBookingArtistCab")
    Call<ResponseBody> getAllBookingArtistCab(@Field("artist_id") String artist_id);

    @FormUrlEncoded
    @POST("getAllBookingArtistCabdhaval_new")
    Call<ResponseBody> getAllBookingArtistCabdhavalnew(@Field("artist_id") String artist_id,@Field("version") int version,@Field("offset") String offset,@Field("date_time_date") String date_time_date ,@Field("date_time_time") String date_time_time );

    @FormUrlEncoded
    @POST("getAllBookingArtistCabdhaval2")
    Call<ResponseBody> getAllBookingArtistCabDhavalClick(@Field("artist_id") String artist_id,@Field("date_time_time") String date_time_time,@Field("date_time_date") String date_time_date,@Field("date_time_date2") String date_time_date2,@Field("version") int version,@Field("offset") String offset);

    @FormUrlEncoded
    @POST("getAllBookingArtist")
    Call<ResponseBody> getAllBookingArtist(@Field("artist_id") String artist_id, @Field("booking_flag") String booking_flag);

    @FormUrlEncoded
    @POST("onlineOffline")
    Call<ResponseBody> onlineOffline(@Field("user_id") String user_id, @Field("is_online") String is_online,@Field("device_token") String devicetoken,@Field("model") String model);

    @FormUrlEncoded
    @POST("addProduct")
    Call<ResponseBody> addProduct(@Field("category_id") String category_id, @Field("sub_category_id") String sub_category_id, @Field("third_id") String third_id, @Field("user_id") String user_id, @Field("product_name") String product_name, @Field("service_id") String service_id, @Field("partner_number") String partner_number, @Field("service_image") String image_str, @Field("service_price") String price, @Field("description_id") String description_id, @Field("max_qty") String max_qty);

    @FormUrlEncoded
    @POST("deleteNotification")
    Call<ResponseBody> deleteNotification(@Field("id") String id);

    @FormUrlEncoded
    @POST("getMyInvoice")
    Call<ResponseBody> getMyInvoice(@Field("user_id") String user_id, @Field("role") String role);


    @FormUrlEncoded
    @POST("getMyTicket")
    Call<ResponseBody> getMyTicket(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("booking_operation")
    Call<ResponseBody> booking_operation(@Field("booking_id") String booking_id, @Field("request") String request, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("send_otp")
    Call<ResponseBody> send_otp(@Field("mobile") String mobile,@Field("role") String role);

    @FormUrlEncoded
    @POST("updateLocation")
    Call<ResponseBody> updateLocation(@Field("user_id") String user_id, @Field("role") String role, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResponseBody> resend_otp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("getArtistByid")
    Call<ResponseBody> getArtistByid(@Field("artist_id") String artist_id, @Field("user_id") String user_id, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("arrival")
    Call<ResponseBody> arival(@Field("artist_id") String artist_id, @Field("booking_id") String booking_id);

    @Multipart
    @POST("addPartnerDocuments")
    Call<ResponseBody> Update_Step_One(@Part("model_name") RequestBody model_name, @Part("city") RequestBody city, @Part("car_booking") RequestBody car_booking,
                                       @Part("car_category") RequestBody car_category,
                                       @Part("ref_number") RequestBody ref_number,
                                       @Part("car_number") RequestBody car_number,
                                       @Part("bank_name") RequestBody bank_name,
                                       @Part("benefiacary_name") RequestBody benefiacary_name,
                                       @Part("account_number") RequestBody account_number,
                                       @Part("ifsc_code") RequestBody ifsc_code,
                                       @Part("branch") RequestBody branch,
                                       @Part MultipartBody.Part fileone, @Part MultipartBody.Part filetwo, @Part MultipartBody.Part filethree, @Part MultipartBody.Part filefour,
                                       @Part MultipartBody.Part filefive, @Part MultipartBody.Part filesix, @Part MultipartBody.Part fileseven, @Part MultipartBody.Part fileeight, @Part MultipartBody.Part filenine, @Part MultipartBody.Part fileten, @Part("artist_id") RequestBody artist_id, @Part MultipartBody.Part fileeleven, @Part MultipartBody.Part filetwelve, @Part MultipartBody.Part filethirtenn, @Part MultipartBody.Part filethirdfourtenn, @Part("cat_id") RequestBody cat_id);

    @Multipart
    @POST("basicinfostep4")
    Call<ResponseBody> upload_business_data(@Part("model_name") RequestBody model_name, @Part("car_booking") RequestBody car_booking,
                                            @Part("car_category") RequestBody car_category,
                                            @Part("car_number") RequestBody car_number,
                                            @Part MultipartBody.Part filethree, @Part MultipartBody.Part filefour,
                                            @Part MultipartBody.Part filefive, @Part MultipartBody.Part filesix, @Part MultipartBody.Part fileseven, @Part MultipartBody.Part fileeight, @Part MultipartBody.Part filenine, @Part("artist_id") RequestBody artist_id, @Part MultipartBody.Part fileeleven, @Part MultipartBody.Part filetwelve, @Part MultipartBody.Part filethirtenn);

    @Multipart
    @POST("basicinfostep6")
    Call<ResponseBody> Add_bank_details(
            @Part("bank_name") RequestBody bank_name,
            @Part("benefiacary_name") RequestBody benefiacary_name,
            @Part("account_number") RequestBody account_number,
            @Part("ifsc_code") RequestBody ifsc_code,
            @Part("branch") RequestBody branch,
            @Part MultipartBody.Part fileten,
            @Part("artist_id") RequestBody artist_id);

    @Multipart
    @POST("basicinfostep3")
    Call<ResponseBody> Add_PERSONAL_DOC(
            @Part MultipartBody.Part fileone,
            @Part MultipartBody.Part filetwo,
            @Part MultipartBody.Part filethree,
            @Part("artist_id") RequestBody artist_id);

    @Multipart
    @POST("upload_data_of_document")
    Call<ResponseBody> Add_PERSONAL_DOC22(
            @Part MultipartBody.Part fileone,
            @Part MultipartBody.Part filetwo,
            @Part MultipartBody.Part filethree,
            @Part("artist_id") RequestBody artist_id);

 @Multipart
    @POST("upload_data_of_document")
    Call<ResponseBody> Add_PERSONAL_DOC2(
            @Part MultipartBody.Part fileone,
            @Part MultipartBody.Part filetwo,
            @Part MultipartBody.Part filethree,
            @Part MultipartBody.Part filefour,
            @Part("artist_id") RequestBody artist_id);

    @Multipart
    @POST("update_bank_data")
    Call<ResponseBody> update_bank_data(
            @Part MultipartBody.Part fileone,
            @Part MultipartBody.Part filetwo,
            @Part MultipartBody.Part filethree,
            @Part MultipartBody.Part filefour,
            @Part("artist_id") RequestBody artist_id,
            @Part("name") RequestBody name,
            @Part("account_number") RequestBody account_number,
            @Part("ifsc_code") RequestBody ifsc_code,
            @Part("bank_name") RequestBody bank_name
    );

    @Multipart
    @POST("updateLocation")
    Call<ResponseBody> updateLocation(
            @Part("user_id") RequestBody user_id,
            @Part("role") RequestBody role,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude);

}
