package pl.edu.pjwstk.jaz.requests;

public class ParameterRequest {

    private String key;
    private String value;

    public ParameterRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}