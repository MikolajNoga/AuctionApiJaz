package pl.edu.pjwstk.jaz.requests;

public class PhotoRequest {

    private String name;
    private int position;

    public PhotoRequest(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
