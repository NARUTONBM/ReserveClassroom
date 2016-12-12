package com.naruto.reserveclassroom.bean;
/*
 * Created by NARUTO on 2016/11/14.
 */

public class ReserveInfoBean {

    /**
     * manager : 某某#13777777777
     * borrower : 宋加鑫#15062248932
     * startT : 2016-11-01 18:00:00
     * endT : 2016-11-01 21:00:00
     * multiMedia : 1
     * activityDetail : xxxx活动
     * currentState : 1
     */

    private String manager;
    private String borrower;
    private String startT;
    private String endT;
    private int multiMedia;
    private String activityDetail;
    private int currentState;

    public String getManager() {

        return manager;
    }

    public void setManager(String manager) {

        this.manager = manager;
    }

    public String getBorrower() {

        return borrower;
    }

    public void setBorrower(String borrower) {

        this.borrower = borrower;
    }

    public String getStartT() {

        return startT;
    }

    public void setStartT(String startT) {

        this.startT = startT;
    }

    public String getEndT() {

        return endT;
    }

    public void setEndT(String endT) {

        this.endT = endT;
    }

    public int getMultiMedia() {

        return multiMedia;
    }

    public void setMultiMedia(int multiMedia) {

        this.multiMedia = multiMedia;
    }

    public String getActivityDetail() {

        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {

        this.activityDetail = activityDetail;
    }

    public int getCurrentState() {

        return currentState;
    }

    public void setCurrentState(int currentState) {

        this.currentState = currentState;
    }
}
