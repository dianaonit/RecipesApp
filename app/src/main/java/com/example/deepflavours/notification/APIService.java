package com.example.deepflavours.notification;

import com.example.deepflavours.Model.MyResponse;
import com.example.deepflavours.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAPb_DZFU:APA91bFPWnVe_PdddyBZMpALgnesYMq-_zMd4A2-o0RcNMZs_qWf_oDRU2wKO2iImuUnP_Dsm3pih70epFw4SCmgS36E9_5198oudxahmVBNn8rgvaeFwFQA4Q8Le2veFI03o1fkgqkl"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
