package Model;

public class Story {

    private String imageUrl;
    private long timestart;
    private long timeend;
    private String storyid;
    private String userid;


    public Story() {
    }

    public Story(String imageUrl, long timestart, long timeend, String storyid, String userid) {
        this.imageUrl = imageUrl;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.userid = userid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
