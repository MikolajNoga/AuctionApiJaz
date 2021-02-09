package pl.edu.pjwstk.jaz.requests;

public class RegisterRequest {

    private String username;
    private String password;
    private String firstname;
    private String lastname;

    public RegisterRequest(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

}
