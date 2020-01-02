package org.users.beans;

import java.io.Serializable;
import java.util.logging.Logger;

public class Time implements Serializable {

    private static final long serialVersionUID = 1L;

    private int hour;
    private int minute;
    private boolean am = true;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isAm() {
        return am;
    }

    public void setAm(boolean am) {
        this.am = am;
    }

    public String getPeriod() {
        if (am)
            return "AM";
        return "PM";
    }
    @Override
    public String toString() {
        return String.format("[Time hour=%d minute=%d am=%s]", hour, minute, getPeriod());
    }
}