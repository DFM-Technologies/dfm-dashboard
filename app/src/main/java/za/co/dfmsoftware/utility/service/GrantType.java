package za.co.dfmsoftware.utility.service;

import androidx.annotation.NonNull;

public enum GrantType {
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token");

    String value;

    GrantType(@NonNull String value) { this.value = value; }

    public String getValue() { return this.value; }
}
