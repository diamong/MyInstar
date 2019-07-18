package Model;

public class Post {

    private String postid,postimage, description,publisher;

    public Post() {
    }

    public Post(String postid, String postimage, String postdescription, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = postdescription;
        this.publisher = publisher;
    }


    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
