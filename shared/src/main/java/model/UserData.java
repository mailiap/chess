package model;

import java.util.Objects;

public class UserData {
    private final String username;
    private final String password;
    private final String email;

    UserData(String usernmae, String password, String email) {

        this.username = usernmae;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final UserData userData=(UserData) o;
        return Objects.equals(this.username, userData.username) && Objects.equals(this.password, userData.password) && Objects.equals(this.email, userData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.password, this.email);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
