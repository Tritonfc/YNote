package com.eararchitect.ynote;

//POJO class for an announcement item
public class Announcement {
    private String userName;
    private String userPic;
    private String announcement;

    public Announcement(){

    }

    public Announcement(String userName, String userPic,String announcement){
        this.userName = userName;
        this.userPic = userPic;
        this.announcement = announcement;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getAnnouncement() {
        return announcement;
    }
}
