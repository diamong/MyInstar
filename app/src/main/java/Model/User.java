package Model;

public class User {

    private String id;
    private String nickname;
    private String username;
    private String imageurl;
    private String bio;

    public User() {
    }

    public User(String id, String nickname, String username, String imageurl, String bio) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.imageurl = imageurl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
