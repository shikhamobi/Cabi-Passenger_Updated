package com.cabipassenger.service;

import com.cabipassenger.data.apiData.ApiRequestData;
import com.cabipassenger.data.apiData.CompanyDomainResponse;
import com.cabipassenger.data.apiData.HelpResponse;
import com.cabipassenger.data.apiData.PastBookingResponse;
import com.cabipassenger.data.apiData.StandardResponse;
import com.cabipassenger.data.apiData.TripDetailResponse;
import com.cabipassenger.data.apiData.UpcomingResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by developer on 8/31/16.
 * it consists of interface that required by the retrofit api to call appropriate data
 */

public interface CoreClient {

    String owner="dGF4aV9hbGw=/";

    //    @GET("{owner}")
//    Call<String> coreDetails(@Path(value = "owner",encoded = true) String owner);
    @GET("{owner}")
    Call<ResponseBody> coreDetailsg(@Header("Cache-Control") String cacheControl,@Path(value = "owner", encoded = true) String owner, @Query("type") String url, @Query(value = "encode", encoded = true) String auth_key);

    @GET("{owner}")
    Call<ResponseBody> coreDetails( @Path(value = "owner", encoded = true) String owner, @Query("type") String url, @Query("lang") String lang, @Query(value = "encode", encoded = true) String auth_key);

    @POST("{owner}")
    Call<ResponseBody> updateUser(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Body RequestBody body, @Query("type") String url, @Query("lang") String lang);

    @GET
    Call<ResponseBody> getWhole(@Header("Cache-Control") String cacheControl,@Url String url);

    @POST("{owner}"+"?type=booking_list")
    Call<UpcomingResponse> callData(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Body ApiRequestData.UpcomingRequest body, @Query("lang") String lang);

    @POST("{owner}"+"?type=completed_journey_monthwise")
    Call<PastBookingResponse> callData(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Body ApiRequestData.PastBookingRequest body, @Query("lang") String lang);

    @POST("{owner}"+"?type=get_trip_detail")
    Call<TripDetailResponse> callData(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Body ApiRequestData.getTripDetailRequest body, @Query("lang") String lang);

    @GET("{owner}"+"?type=help_content")
    Call<HelpResponse> helpContent(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Query("lang") String lang);

    @POST("{owner}"+"?type=help_comment_update")
    Call<StandardResponse> helpSubmit(@Path(value = "owner", encoded = true) String owner, @Query(value = "encode", encoded = true) String auth_key, @Body ApiRequestData.HelpSubmit body, @Query("lang") String lang);

    @POST("{owner}"+"?type=check_companydomain")
    Call<CompanyDomainResponse> callData(@Path(value = "owner", encoded = true) String owner, @Body ApiRequestData.BaseUrl body);
}
