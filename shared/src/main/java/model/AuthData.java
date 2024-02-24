package model;

import java.util.Objects;

public class AuthData {
    private final String authToken;
    private final String username;

    AuthData(String authToken, String username) {

        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final AuthData authData=(AuthData) o;
        return Objects.equals(this.authToken, authData.authToken) && Objects.equals(this.username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.authToken, this.username);
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
