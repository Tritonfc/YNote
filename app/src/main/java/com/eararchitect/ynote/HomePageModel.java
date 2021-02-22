package com.eararchitect.ynote;

public class HomePageModel {
    String menuText;
    int menuPic;




    public HomePageModel(String menuText, int menuPic) {
        this.menuText = menuText;
        this.menuPic = menuPic;
    }

    public String getMenuText() {
        return menuText;
    }

    public int getMenuPic() {
        return menuPic;
    }
}
