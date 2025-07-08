package org.bihe.finalproject.network.service;

import org.bihe.finalproject.model.User;
import org.bihe.finalproject.utils.NetworkConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @POST(NetworkConstants.SIGN_UP_USER)
    Call<User> signUpUser(@Body User user);

    @GET(NetworkConstants.LOGIN_USER)
    Call<User> loginUser(@Query("username") String username, @Query("password") String password);
}
