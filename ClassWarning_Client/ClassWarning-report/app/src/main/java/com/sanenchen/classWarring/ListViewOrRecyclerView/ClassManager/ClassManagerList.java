package com.sanenchen.classWarring.ListViewOrRecyclerView.ClassManager;

public class ClassManagerList {
    private String classID;
    private String inSchoolID;
    private String inGrade;
    private String className;

    public ClassManagerList(String classID, String inSchoolID, String inGrade, String className) {
        this.classID = classID;
        this.inSchoolID = inSchoolID;
        this.inGrade = inGrade;
        this.className = inSchoolID;
    }

    public String getClassID() {
        return classID;
    }

    public String getInSchoolID() {
        return inSchoolID;
    }

    public String getInGrade() {
        return inGrade;
    }

    public String getClassName() {
        return className;
    }
}
