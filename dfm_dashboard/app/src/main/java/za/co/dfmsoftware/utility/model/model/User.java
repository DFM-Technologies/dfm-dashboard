package za.co.dfmsoftware.utility.model.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * USER
 * This Class will be used  to log users into Mobile App
 * It will store user credentials and save tokens
 * */
public class User extends RealmObject {

    //variables
    @PrimaryKey
    @SerializedName("username")
    private String username;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private long tokenExpireTime;

    private String firstName;
    private String lastName;

    public User() {} //default Constructor

    //setters
    public void setUsername(String username){
        this.username = username;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    //getters
    public String getUsername() { return username; }

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public String getRefreshToken() { return refreshToken; }
    public long getTokenExpireTime() { return tokenExpireTime; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

}
