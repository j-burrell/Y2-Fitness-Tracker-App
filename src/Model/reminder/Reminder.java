package Model.reminder;

import java.time.LocalDate;
import java.time.LocalTime;

import Model.email.Email;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reminder {
    public enum EventType
    {
        MEDICINE,
        APPOINTMENT
    }
    private LocalDate date;
    private LocalTime time;
    private String event;
    private String remarks;
    private EventType type;
    private Boolean isEmailSent;



    public Reminder()
    {
        isEmailSent=false;
    }
    public Reminder(LocalDate date, LocalTime time, String event, String remarks, EventType type,Boolean isEmailSent ) {
        this.date = date;
        this.time = time;
        this.event = event;
        this.remarks = remarks;
        this.type = type;
        this.isEmailSent=isEmailSent;
    }

    public Reminder(LocalDate date, LocalTime time, String event, String remarks, EventType type) {
        this.date = date;
        this.time = time;
        this.event = event;
        this.remarks = remarks;
        this.type = type;
        this.isEmailSent=false;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
    public StringProperty getNameProperty()
    {
        return new SimpleStringProperty(event);
    }
    public Boolean getEmailSent() {
        return isEmailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        isEmailSent = emailSent;
    }
}
