package org.example.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private int id = 0;
    private String title;
    private Date ETA;
    private Date startDateTime;
    private Date endDateTime;

    public Task() {
        this.title = "";
        this.ETA = null;
        this.startDateTime = null;
        this.endDateTime = null;
    }

    public Task(String title, Date ETA, Date startDateTime, Date endDateTime) {
        this.title = title;
        this.ETA = ETA;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Task(int id, String title, Date ETA, Date startDateTime, Date endDateTime) {
        this.id = id;
        this.title = title;
        this.ETA = ETA;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String ETATime() {
        return ETA == null ? null : timeFormat.format(ETA);
    }

    public void setETATime(Date etaTime) {
        this.ETA = etaTime;
    }

    public String startTime() {
        return startDateTime == null ? null : timeFormat.format(startDateTime);
    }

    public void setStartTime(Date startTime) {
        this.startDateTime = startTime;
    }

    public String endTime() {
        return endDateTime == null ? null : timeFormat.format(endDateTime);
    }

    public void setEndTime(Date endTime) {
        this.endDateTime = endTime;
    }
}