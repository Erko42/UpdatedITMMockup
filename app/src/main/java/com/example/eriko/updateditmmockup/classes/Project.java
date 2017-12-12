package com.example.eriko.updateditmmockup.classes;

import java.io.Serializable;

public class Project implements Serializable{

    String AppName;
    int CustomerID;
    String ProjectDuration;
    String BackgroundImg;
    int HideInMultiApp;
    int ProjectID;

    public Project() {

    }

    public Project(String AppName, int CustomerID, String ProjectDuration, String BackgroundImg, int HideInMultiApp, int ProjectID) {
        this.AppName = AppName;
        this.CustomerID = CustomerID;
        this.ProjectDuration = ProjectDuration;
        this.BackgroundImg = BackgroundImg;
        this.HideInMultiApp = HideInMultiApp;
        this.ProjectID = ProjectID;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public void setProjectDuration(String projectDuration) {
        ProjectDuration = projectDuration;
    }

    public void setBackgroundImg(String backgroundImg) { BackgroundImg = backgroundImg; }

    public void setHideInMultiApp(int hideInMultiApp) {
        HideInMultiApp = hideInMultiApp;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public String getAppName() {
        return AppName;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public String getProjectDuration() {
        return ProjectDuration;
    }

    public String getBackgroundImg() { return BackgroundImg; }

    public int getHideInMultiApp() {
        return HideInMultiApp;
    }

    public int getProjectID() {
        return ProjectID;
    }
}