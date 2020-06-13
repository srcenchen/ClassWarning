package com.sanenchen.classWarring;

public class WarningSearchAd {
    private String title;
    private String Student;
    private String id;
    private String UplodDate;

    public WarningSearchAd(String title, String Student, String id, String uplodDate) {
        this.title = title;
        this.Student = Student;
        this.id = id;
        this.UplodDate = uplodDate;
    }

    public String getTitle() {
        return title;
    }

    public String getStudent() {
        return Student;
    }

    public String getID() {
        return id;
    }

    public String getUplodDate() {
        return UplodDate;
    }
}
