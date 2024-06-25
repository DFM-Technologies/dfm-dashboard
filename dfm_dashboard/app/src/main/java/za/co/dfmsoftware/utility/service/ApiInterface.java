package za.co.dfmsoftware.utility.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.model.model.User;

/**
 * HTTP ENDPOINT
 * This is used as configuration for the HTTP Calls
 * */
public interface ApiInterface {

    /* Post Authenticate User */
    @FormUrlEncoded
    @POST("authentication")
    Observable<User> authenticateUser(@Field("grant_type") String grantType,
                                      @Field("username") String username,
                                      @Field("password") String password);
    /* Post Refresh token */
    @FormUrlEncoded
    @POST("authentication")
    Observable<User> refreshAuthorizationToken(@Field("grant_type") String grantType,
                                               @Field("refresh_token") String refreshToken);

    /* Get Probes */
    @GET("probe/userprobes")
    Observable<List<Probe>> getProbes(@Header("Authorization") String authorization);

}
