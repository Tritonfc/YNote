package com.eararchitect.ynote;

public class SearchModel {

    private String userName;
    private String userPic;
    private String userId;

    public SearchModel(){
        //Empty constructor
    }

    public SearchModel(String userName, String userPic,String userId){
        this.userName = userName;
        this.userPic = userPic;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPic() {
        return userPic;
    }
}
